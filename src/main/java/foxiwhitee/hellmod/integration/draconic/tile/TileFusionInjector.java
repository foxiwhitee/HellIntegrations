package foxiwhitee.hellmod.integration.draconic.tile;

import appeng.tile.AEBaseInvTile;
import appeng.tile.AEBaseTile;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import cofh.api.energy.IEnergyReceiver;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingCharger;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingInjector;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingInventory;
import foxiwhitee.hellmod.integration.draconic.helpers.MaxTierException;
import foxiwhitee.hellmod.utils.energy.IExtendedRFStorage;
import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileFusionInjector extends AEBaseInvTile implements IFusionCraftingInjector, IFusionCraftingCharger, IEnergyReceiver, ISidedInventory, IExtendedRFStorage {
    public IFusionCraftingInventory currentCraftingInventory = null;

    private AppEngInternalInventory inventory = new AppEngInternalInventory(this, 1);

    public boolean singleItem = false;

    private long energy = 0L;

    private long maxEnergy = 2000000000;

    private int chargeSpeedModifier = 300;

    private int tier = 0;

    private int face = 0;

    public TileFusionInjector(int level) {
        if (this.tier > 4)
            throw new MaxTierException("Max tier of injectors is 5");
        this.tier = level;
    }

    public TileFusionInjector() {}

    @Override
    public IInventory getInternalInventory() {
        return inventory;
    }

    public int getFace() {
        return this.face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    @Override
    public TileEntity getTile() {
        return this;
    }

    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tagCompound);
        return pack;
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    public int receiveEnergy(ForgeDirection from, int i, boolean b) {
        long canReceive = Math.min(i, getExtendedCapacity() - this.energy);
        if (canReceive < 0L)
            return 0;
        if (!b) {
            this.energy += canReceive;
        }
        return (int)canReceive;
    }

    public long getInjectorCharge() {
        return this.energy;
    }

    public int getEnergyStored(ForgeDirection from) {
        return (int)Math.min(2147483647L, getExtendedStorage());
    }

    public int getMaxEnergyStored(ForgeDirection from) {
        return (int)Math.min(2147483647L, getExtendedCapacity());
    }

    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return true;
    }

    public int getPedestalTier() {
        return this.tier;
    }

    public ItemStack getStackInPedestal() {
        return inventory.getStackInSlot(0);
    }

    public void setStackInPedestal(ItemStack stack) {
        setInventorySlotContents(0, stack);
    }

    public void onCraft() {
        if (this.currentCraftingInventory != null)
            this.energy = 0;
        //if (stack.stackSize == 1) {
        //    stack = null;
        //} else {
        //    stack.stackSize--;
        //}
    }

    public long getExtendedStorage() {
        return this.energy;
    }

    public long getExtendedCapacity() {
        validateCraftingInventory();
        if (this.currentCraftingInventory != null)
            return this.currentCraftingInventory.getIngredientEnergyCost();
        return 0L;
    }

    public boolean setCraftingInventory(IFusionCraftingInventory craftingInventory) {
        if (craftingInventory == null) {
            this.currentCraftingInventory = null;
            return false;
        }
        if (validateCraftingInventory() && !this.worldObj.isRemote)
            return false;
        this.currentCraftingInventory = craftingInventory;
        this.chargeSpeedModifier = 201 - getPedestalTier() * 50;
        return true;
    }

    private boolean validateCraftingInventory() {
        if (!OreDictUtil.isEmpty(getStackInPedestal()) && this.currentCraftingInventory != null && this.currentCraftingInventory.craftingInProgress() && !((TileEntity)this.currentCraftingInventory).isInvalid())
            return true;
        this.currentCraftingInventory = null;
        return false;
    }

    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return true;
    }

    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsBySide(ForgeDirection forgeDirection) {
        return new int[0];
    }

    public int getSizeInventory() {
        return 1;
    }

    public ItemStack getStackInSlot(int i) {
        return this.inventory.getStackInSlot(i);
    }

    public ItemStack getStackInSlot() {
        return this.getStackInSlot(0);
    }

    public ItemStack decrStackSize(int slot, int var) {
        if (this.getStackInSlot() == null)
            return null;
        if (this.getStackInSlot().stackSize >= var) {
            this.getStackInSlot().stackSize -= var;
            if (this.getStackInSlot().stackSize <= 0)
                this.setStackInPedestal(null);
            markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            return this.getStackInSlot();
        }
        this.setStackInPedestal(null);
        markDirty();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        inventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
        markDirty();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public String getInventoryName() {
        return "F";
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    public int getInventoryStackLimit() {
        return this.singleItem ? 1 : 64;
    }

    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    public void openInventory() {}

    public void closeInventory() {}

    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {}

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromWorldNBT_AF(NBTTagCompound compound) {
        this.inventory.readFromNBT(compound, "inv");
        this.energy = compound.getLong("energy");
        this.singleItem = compound.getBoolean("singleItem");
        this.tier = compound.getInteger("tier");
        this.face = compound.getInteger("face");
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToWorldNBT_AF(NBTTagCompound compound) {
        this.inventory.writeToNBT(compound, "inv");
        compound.setBoolean("singleItem", this.singleItem);
        compound.setLong("energy", this.energy);
        compound.setInteger("tier", this.tier);
        compound.setInteger("face", this.face);
    }

}
