package foxiwhitee.hellmod.integration.draconic.tile;

import appeng.tile.AEBaseTile;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.util.Platform;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.client.render.effect.EffectTrackerFusionCrafting;
import foxiwhitee.hellmod.integration.draconic.client.render.effect.SEffectHandler;
import foxiwhitee.hellmod.integration.draconic.client.render.effect.SParticle;
import foxiwhitee.hellmod.integration.draconic.client.sound.FusionRotationSound;
import foxiwhitee.hellmod.integration.draconic.helpers.Cord3D;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingCharger;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingInjector;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingInventory;
import foxiwhitee.hellmod.integration.draconic.recipes.FusionRecipe;
import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileFusionCraftingCore extends AEBaseTile implements ISidedInventory, IFusionCraftingInventory {
    private final ItemStack[] coreInv = new ItemStack[2];

    private final boolean halfCycle = false;

    public FusionRecipe activeRecipe = null;

    public int craftingStage = 0;

    @SideOnly(Side.CLIENT)
    public LinkedList<EffectTrackerFusionCrafting> effects;

    protected List<IFusionCraftingInjector> pedestals = new ArrayList<>();

    protected List<IFusionCraftingCharger> chargers = new ArrayList<>();

    private int craftingSpeedBoost = 0;

    private boolean isCrafting = false;

    private double effectRotation = 0.0D;

    private boolean allLocked = false;

    private boolean updateBlock = false;

    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("isCrafting", this.isCrafting);
        compound.setInteger("craftingStage", this.craftingStage);
        return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, compound);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound compound = pkt.func_148857_g();
        this.isCrafting = compound.getBoolean("isCrafting");
        this.craftingStage = compound.getInteger("craftingStage");
    }

    private void invalidateCrafting() {
        if (this.worldObj.isRemote)
            return;
        this.isCrafting = false;
        this.activeRecipe = null;
        this.craftingStage = 0;
        this.pedestals.clear();
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord), this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
        markDirty();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @TileEvent(TileEventType.TICK)
    public void updateTick() {
        if (this.worldObj.isRemote) {
            updateEffects();
        }
        if (this.isCrafting && Platform.isServer()) {
            for (IFusionCraftingInjector pedestal : this.pedestals) {
                if (((TileEntity)pedestal).isInvalid()) {
                    invalidateCrafting();
                    return;
                }
            }
            if (this.activeRecipe == null || !this.activeRecipe.matches(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord) || this.activeRecipe.canCraft(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord) == null || !this.activeRecipe.canCraft(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord).equals("true")) {
                invalidateCrafting();
                return;
            }
            long totalCharge = 0L;
            for (IFusionCraftingInjector pedestal : this.pedestals) {
                if (pedestal.getStackInPedestal() != null)
                    totalCharge += ((TileFusionInjector)pedestal).getInjectorCharge();
            }
            int averageCharge = (int)(totalCharge / this.activeRecipe.getInputs().size());
            double percentage = (double) averageCharge / this.activeRecipe.getIngredientEnergyCost();
            if (percentage <= 1.0D && this.craftingStage < 1000) {
                this.craftingStage = (int)(percentage * 1000.0D);
                 if (this.craftingStage == 0 && percentage > 0.0D)
                    this.craftingStage = 1;
            } else if (this.craftingStage < 2000) {
                this.craftingStage = (this.craftingStage + 2);
            } else if (this.craftingStage >= 2000) {
                this.activeRecipe.craft(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                this.isCrafting = false;
                this.activeRecipe = null;
                this.craftingStage = 0;
            }
        }
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public void updateInjectors() {
        if (this.isCrafting)
            return;
        this.pedestals.clear();
        this.chargers.clear();
        int range = 16;
        int x;
        for (x = -range; x <= range; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++)
                    processPedestal(this.worldObj.getTileEntity(x + this.xCoord, y + this.yCoord, z + this.zCoord));
            }
        }
        for (x = -1; x <= 1; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -1; z <= 1; z++)
                    processPedestal(this.worldObj.getTileEntity(x + this.xCoord, y + this.yCoord, z + this.zCoord));
            }
        }
        for (x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -range; z <= range; z++)
                    processPedestal(this.worldObj.getTileEntity(x + this.xCoord, y + this.yCoord, z + this.zCoord));
            }
        }
    }

    public void attemptStartCrafting() {
        updateInjectors();
        this.activeRecipe = DraconicEvolutionIntegration.findRecipeFusion(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        if (this.activeRecipe != null && this.activeRecipe.canCraft(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord) != null && this.activeRecipe.canCraft(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord).equals("true")) {
            this.isCrafting = true;
            if (Platform.isServer()) {

            }
        } else {
            this.activeRecipe = null;
        }
    }

    public int getSizeInventory() {
        return 2;
    }

    public ItemStack getStackInSlot(int slot) {
        return this.coreInv[slot];
    }

    public boolean isSlotEmpty(int slot) {
        return (getStackInSlot(slot) == null);
    }

    public ItemStack decrStackSize(int slot, int count) {
        if (this.coreInv[slot] != null) {
            ItemStack split = getStackInSlot(slot);
            ItemStack ns = null;
            if (count >= split.stackSize) {
                ns = split;
                this.coreInv[slot] = null;
            } else {
                ns = split.splitStack(count);
            }
            markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            return ns;
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.coreInv[slot] = stack;
        markDirty();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public String getInventoryName() {
        return "FusionCore";
    }

    public boolean hasCustomInventoryName() {
        return true;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return (player != null && player.getDistanceSq(this.xCoord, this.yCoord, this.zCoord) <= 49.0D);
    }

    public void openInventory() {}

    public void closeInventory() {}

    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return (slot == 0);
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[] { 0, 1 };
    }

    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return (slot == 0);
    }

    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return (slot == 1);
    }

    public ItemStack getStackInCore(int slot) {
        return this.coreInv[slot];
    }

    public void setStackInCore(int slot, ItemStack stack) {
        this.coreInv[slot] = stack;
        markDirty();
    }

    public List<IFusionCraftingInjector> getInjectors() {
        return this.pedestals;
    }

    public List<IFusionCraftingCharger> getChargers() {
        return this.chargers;
    }

    public boolean craftingInProgress() {
        return this.isCrafting;
    }

    public int getCraftingStage() {
        return this.craftingStage;
    }

    public void setCraftingStage(int craftingStage) {
        this.craftingStage = craftingStage;
    }

    public int getComparatorOutput() {
        updateInjectors();
        if (!OreDictUtil.isEmpty(getStackInCore(1)))
            return 15;
        if (this.craftingStage > 0)
            return (int)Math.max(1.0D, this.craftingStage / 2000.0D * 15.0D);
        FusionRecipe recipe = DraconicEvolutionIntegration.findRecipeFusion(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        if (recipe != null && recipe.canCraft(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord).equals("true"))
            return 1;
        return 0;
    }

    public List<IFusionCraftingInjector> getPedestals() {
        return this.pedestals;
    }

    public ItemStack[] getCoreInv() {
        return this.coreInv;
    }

    public FusionRecipe getActiveRecipe() {
        return this.activeRecipe;
    }

    public int getCraftingSpeedBoost() {
        return this.craftingSpeedBoost;
    }

    public boolean isCrafting() {
        return this.isCrafting;
    }

    public void setCrafting(boolean crafting) {
        this.isCrafting = crafting;
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromWorldNBT_AF(NBTTagCompound compound) {
        this.isCrafting = compound.getBoolean("isCrafting");
        this.craftingStage = compound.getInteger("craftingStage");
        for (int i = 0; i < getSizeInventory(); i++) {
            if (compound.hasKey("inventory_" + i))
                this.coreInv[i] = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("inventory_" + i));
        }
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToWorldNBT_AF(NBTTagCompound compound) {
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (!OreDictUtil.isEmpty(stack)) {
                NBTTagCompound tag = new NBTTagCompound();
                stack.writeToNBT(tag);
                compound.setTag("inventory_" + i, (NBTBase)tag);
            }
        }
        compound.setBoolean("isCrafting", this.isCrafting);
        compound.setInteger("craftingStage", this.craftingStage);
    }

    @TileEvent(TileEventType.NETWORK_WRITE)
    public void writeToNetworkTAF(ByteBuf byteBuf) {
        byteBuf.writeBoolean(isCrafting);
        byteBuf.writeInt(craftingStage);
    }

    @TileEvent(TileEventType.NETWORK_READ)
    public boolean readFromNetworkTAF(ByteBuf byteBuf) {
        isCrafting = byteBuf.readBoolean();
        craftingStage = byteBuf.readInt();
        return true;
    }

    private void processPedestal(TileEntity tile) {
        if (tile instanceof IFusionCraftingCharger || tile instanceof IFusionCraftingInjector) {
            double x = (tile.xCoord - this.xCoord);
            double y = (tile.yCoord - this.yCoord);
            double z = (tile.zCoord - this.zCoord);
            int[][] vectors = { { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 }, { -1, 0, 0 }, { 1, 0, 0 } };
            double f = Double.MIN_VALUE;
            int s = 0;
            for (int i = 0; i < 6; i++) {
                double f2 = x * vectors[i][0] + y * vectors[i][1] + z * vectors[i][2];
                if (f2 > f) {
                    f = f2;
                    s = i;
                }
            }
            int m = 0;
            if (tile instanceof IFusionCraftingCharger) {
                m = ((IFusionCraftingCharger)tile).getFace();
            } else {
                m = ((IFusionCraftingInjector)tile).getFace();
            }
            m = (m % 2 == 0) ? (m + 1) : (m - 1);
            if (m == s) {
                if (tile instanceof IFusionCraftingInjector) {
                    IFusionCraftingInjector pedestal = (IFusionCraftingInjector)tile;
                    if (pedestal.setCraftingInventory(this))
                        this.pedestals.add(pedestal);
                }
                if (tile instanceof IFusionCraftingCharger) {
                    IFusionCraftingCharger charger = (IFusionCraftingCharger)tile;
                    if (charger.setCraftingInventory(this))
                        this.chargers.add(charger);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void initializeEffects() {
        this.pedestals.clear();
        this.chargers.clear();
        int range = 16;
        int x;
        for (x = -range; x <= range; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++)
                    processPedestal(this.worldObj.getTileEntity(x + this.xCoord, y + this.yCoord, z + this.zCoord));
            }
        }
        for (x = -1; x <= 1; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -1; z <= 1; z++)
                    processPedestal(this.worldObj.getTileEntity(x + this.xCoord, y + this.yCoord, z + this.zCoord));
            }
        }
        for (x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -range; z <= range; z++)
                    processPedestal(this.worldObj.getTileEntity(x + this.xCoord, y + this.yCoord, z + this.zCoord));
            }
        }
        this.activeRecipe = DraconicEvolutionIntegration.findRecipeFusion(this, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        if (this.activeRecipe == null) {
            this.effects = null;
            return;
        }
        this.effects = new LinkedList<>();
        for (IFusionCraftingInjector pedestal : this.pedestals) {
            if (OreDictUtil.isEmpty(pedestal.getStackInPedestal()))
                continue;
            pedestal.setCraftingInventory(this);
            TileEntity te = (TileEntity)pedestal;
            Cord3D spawn = new Cord3D(te.xCoord, te.yCoord, te.zCoord);
            EnumFacing facing = EnumFacing.values()[MathHelper.clamp_int(pedestal.getFace(), 0, 5)];
            spawn.add(0.5D + facing.getFrontOffsetX() * 0.45D, 0.5D + facing.getFrontOffsetY() * 0.45D, 0.5D + facing.getFrontOffsetZ() * 0.45D);
            this.effects.add(new EffectTrackerFusionCrafting(this.worldObj, spawn, new Cord3D(this.xCoord, this.yCoord, this.zCoord), this, this.activeRecipe.getInputs().size()));
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateEffects() {
        if (this.effects == null) {
            if (this.isCrafting) {
                initializeEffects();
                this.effectRotation = 0.0D;
                this.allLocked = false;
            }
            return;
        }
        double distFromCore = 1.2D;
        if (getCraftingStage() > 1600)
            distFromCore *= 1.0D - (getCraftingStage() - 1600) / 400.0D;
        if (this.allLocked) {
            this.effectRotation -= Math.min((getCraftingStage() - 1100.0D) / 900.0D * 0.8D, 0.5D);
            if (this.effectRotation > 0.0D)
                this.effectRotation = 0.0D;
        }
        int index = 0;
        int count = this.effects.size();
        boolean flag = true;
        boolean isMoving = (getCraftingStage() > 1000);
        for (EffectTrackerFusionCrafting effect : this.effects) {
            effect.onUpdate(isMoving);
            if (!effect.positionLocked)
                flag = false;
            if (isMoving) {
                effect.scale = 0.7F + (float)(distFromCore / 1.2D) * 0.3F;
                effect.green = effect.blue = (float)(distFromCore - 0.2D);
                effect.red = 1.0F - (float)(distFromCore - 0.2D);
            }
            double indexPos = (double) index / count;
            double offset = indexPos * 6.283185307179586D;
            double offsetX = Math.sin(this.effectRotation + offset) * distFromCore;
            double offsetZ = Math.cos(this.effectRotation + offset) * distFromCore;
            double mix = this.effectRotation / 5.0D;
            double xAdditive = offsetX * Math.sin(-mix);
            double zAdditive = offsetZ * Math.cos(-mix);
            double offsetY = (xAdditive + zAdditive) * 0.2D * distFromCore / 1.2D;
            effect.circlePosition.set(this.xCoord + 0.5D + offsetX, this.yCoord + 0.5D + offsetY, this.zCoord + 0.5D + offsetZ);
            index++;
        }
        SoundHandler soundManager = FMLClientHandler.instance().getClient().getSoundHandler();
        if (!this.allLocked && flag)
            FMLClientHandler.instance().getClient().getSoundHandler().playSound((ISound)new FusionRotationSound(this));
        this.allLocked = flag;
        if (!this.isCrafting) {
            for (int i = 0; i < 100; i++)
                SEffectHandler.spawnFXDirect(new ResourceLocation(HellCore.MODID, "textures/particle/particles.png"), (SParticle)new EffectTrackerFusionCrafting.SubParticle(this.worldObj, (new Cord3D(this.xCoord, this.yCoord, this.zCoord)).add(0.5D, 0.5D, 0.5D)));
            this.effects = null;
        }
    }

    @SideOnly(Side.CLIENT)
    public void renderEffects(float partialTicks) {
        if (this.effects != null) {
            (Minecraft.getMinecraft()).renderEngine.bindTexture(new ResourceLocation(HellCore.MODID, "textures/particle/fusion_particle.png"));
            Tessellator tessellator = Tessellator.instance;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            RenderHelper.disableStandardItemLighting();
            GL11.glDepthMask(true);
            GL11.glAlphaFunc(516, 0.0F);
            for (EffectTrackerFusionCrafting effect : this.effects)
                effect.renderEffect(tessellator, partialTicks);
            GL11.glDisable(3042);
            RenderHelper.enableStandardItemLighting();
            GL11.glDepthMask(true);
            GL11.glAlphaFunc(516, 0.1F);
        }
    }

    public long getIngredientEnergyCost() {
        return (this.activeRecipe != null) ? this.activeRecipe.getIngredientEnergyCost() : 0L;
    }
}
