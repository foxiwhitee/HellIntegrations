package foxiwhitee.hellmod.integration.draconic.tile;

import appeng.core.AELog;
import appeng.tile.AEBaseInvTile;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import cofh.api.energy.IEnergyReceiver;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModRecipes;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.recipes.DraconicAssemblerRecipe;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketDraconicAssembler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TileDraconicAssembler extends AEBaseInvTile implements IEnergyReceiver {
    private final AppEngInternalInventory out = new AppEngInternalInventory(this, 1);
    private final AppEngInternalInventory inv = new AppEngInternalInventory(this, 7);
    private final AppEngInternalInventory upgrades = new AppEngInternalInventory(this, 3);

    private static final long[] CAPACITY_CARDS = new long[] {HellConfig.energyUpgradeWywern, HellConfig.energyUpgradeAwakened, HellConfig.energyUpgradeChaotic, HellConfig.energyUpgradeArial };
    private static final int MAX_TICKS_FOR_CRAFT = 1000;

    private int[] levels = new int[7];
    private long energyStored = 0L;
    private long needEnergy = 0L;
    private long maxEnergyStored = 100000L;
    private long receiveEnergy = 1000000L;
    private int maxCraftingTicks = 1000;
    private boolean stackCraft = false;
    private long thisTickEnergyReceived = 0L;
    private long lastTickEnergyReceived = 0L;
    private int craftingTicks = 0;
    private int count = 0;

    private DraconicAssemblerRecipe currentRecipe;

    @Override
    public IInventory getInternalInventory() {
        return inv;
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {
        updateUpgradeInventory();
        this.currentRecipe = findRecipe(stackCraft);
        if (this.currentRecipe == null && this.craftingTicks > 0) {
            this.craftingTicks = 0;
            this.needEnergy = 0L;
            markForUpdate();
            try {
                NetworkManager.instance.sendToAll(new PacketDraconicAssembler(this, "ClearCraft"));
            } catch (IOException ignored) {}
        }
    }

    public void clearCraft() {
        this.currentRecipe = null;
        this.craftingTicks = 0;
        this.needEnergy = 0L;
    }

    public void updateUpgradeInventory() {
        long localMaxEnergyStored = 0;
        long localReceiveEnergy = 0;
        int localTicksForCraft = MAX_TICKS_FOR_CRAFT;
        boolean hasStackUpgrade = false;
        for (int i = 0; i < this.upgrades.getSizeInventory(); i++) {
            ItemStack stack = this.upgrades.getStackInSlot(i);
            if (stack != null && stack.getItem() == DraconicEvolutionIntegration.draconicEnergyUpgrades) {
                int damage = stack.getItemDamage();
                localMaxEnergyStored += CAPACITY_CARDS[damage];
                localReceiveEnergy += CAPACITY_CARDS[damage] / 10;
            }
            if (stack != null && stack.getItem() == DraconicEvolutionIntegration.draconicAssemblerUpgrades) {
                int damage = stack.getItemDamage();
                switch (damage) {
                    case 0: localTicksForCraft -= HellConfig.draconicAssemblerSpeedUpgrade; break;
                    case 1: hasStackUpgrade = true; break;
                }
            }
        }
        stackCraft = hasStackUpgrade;
        this.maxEnergyStored = localMaxEnergyStored != 0 ? localMaxEnergyStored : 100000L;
        this.receiveEnergy = localReceiveEnergy != 0 ? localReceiveEnergy : 1000000L;
        if (Platform.isClient()) {
            int ii = 9;
        }
        this.maxCraftingTicks = localTicksForCraft <= 0 ? 1 : localTicksForCraft;
        if (this.energyStored > maxEnergyStored)
            this.energyStored = maxEnergyStored;
        if (Platform.isServer()) {
            try {
                NetworkManager.instance.sendToAll(new PacketDraconicAssembler(this, "UpdateUpgradesInventory"));
            } catch (IOException ignored) {}
        }
    }

    @TileEvent(TileEventType.TICK)
    public void updateTick() {
        this.lastTickEnergyReceived = this.thisTickEnergyReceived;
        this.thisTickEnergyReceived = 0L;
        if (this.currentRecipe == null) {
            this.currentRecipe = findRecipe(stackCraft);
        }
        if (this.currentRecipe == null) {
            try {
                NetworkManager.instance.sendToAll(new PacketDraconicAssembler(this, "ClearCraft"));
            } catch (IOException ignored) {}
        }
        if (this.currentRecipe != null) {
            if (this.energyStored >= this.needEnergy) {
                if (this.craftingTicks++ >= this.maxCraftingTicks) {
                    if (this.craftingTicks >= this.maxCraftingTicks) {
                        this.craftingTicks = this.maxCraftingTicks;
                    }
                    craft();
                }
            }
        }
    }

    private void craft() {
        ItemStack slotStack = inv.getStackInSlot(0);
        if (slotStack == null) return;

        ItemStack result = currentRecipe.getOut();
        if (result == null) return;

        int itemsToRemove = 0;
        for (int i = 0; i < 7; i++) {
            ItemStack ingredient = inv.getStackInSlot(i);
            if (ingredient != null && ingredient.getItem() == slotStack.getItem() &&
                    ingredient.getItemDamage() == slotStack.getItemDamage()) {
                itemsToRemove += ingredient.stackSize;
            }
        }
        int requiredItems = count;
        if (itemsToRemove < requiredItems) return;

        int remainingToRemove = requiredItems;
        for (int i = 0; i < 7 && remainingToRemove > 0; i++) {
            ItemStack ingredient = inv.getStackInSlot(i);
            if (ingredient != null && ingredient.getItem() == slotStack.getItem() &&
                    ingredient.getItemDamage() == slotStack.getItemDamage()) {
                int toRemoveFromSlot = Math.min(ingredient.stackSize, remainingToRemove);
                ingredient.stackSize -= toRemoveFromSlot;
                remainingToRemove -= toRemoveFromSlot;
                if (ingredient.stackSize <= 0) {
                    inv.setInventorySlotContents(i, null);
                } else {
                    inv.setInventorySlotContents(i, ingredient);
                }
            }
        }

        ItemStack output = out.getStackInSlot(0);
        int outputCount = count * result.stackSize;
        if (output == null) {
            out.setInventorySlotContents(0, result.copy().splitStack(outputCount));
        } else if (output.getItem() == result.getItem() &&
                output.getItemDamage() == result.getItemDamage() &&
                output.stackSize + outputCount <= output.getMaxStackSize()) {
            output.stackSize += outputCount;
            out.setInventorySlotContents(0, output);
        } else {
            return;
        }

        this.energyStored -= this.needEnergy;
        this.needEnergy = 0L;
        this.craftingTicks = 0;
        this.currentRecipe = null;
        markForUpdate();
    }

    @Override
    public int[] getAccessibleSlotsBySide(ForgeDirection forgeDirection) {
        return new int[0];
    }

    public DraconicAssemblerRecipe findRecipe(boolean multipleCounts) {
        List<Object> items = new ArrayList<>();
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack is = inv.getStackInSlot(i);
            items.add((Object)inv.getStackInSlot(i));
        }
        boolean allNull = items.stream().allMatch(Objects::isNull);
        if (allNull) return null;

        DraconicAssemblerRecipe recipe = null;
        int maxCount = 1;

        for (DraconicAssemblerRecipe r : ModRecipes.draconicAssemblerRecipes) {
            if (r == null) continue;

            if (multipleCounts) {
                int count = r.findMaxCraftCount(items, levels);
                if (count > 0) {
                    recipe = r;
                    int c = 0;
                    for (int i = 1; i < count + 1; i++) {
                        if (out.getStackInSlot(0) != null && out.getStackInSlot(0).stackSize + recipe.getOut().stackSize * i <= 64) {
                            c++;
                        } else if (out.getStackInSlot(0) == null && recipe.getOut().stackSize * i <= 64) {
                            c++;
                        } else {
                            break;
                        }
                    }
                    if (c == 0) {
                        return null;
                    }
                    maxCount = c;
                    break;
                }
            } else {
                if (r.matches(items, levels)) {
                    recipe = r;
                    if (out.getStackInSlot(0) != null && out.getStackInSlot(0).stackSize + recipe.getOut().stackSize > 64) {
                        return null;
                    } else if (out.getStackInSlot(0) == null && recipe.getOut().stackSize > 64) {
                        return null;
                    }
                    break;
                }
            }
        }
        count = maxCount;

        if (recipe != null) {
            needEnergy = (long) recipe.getEnergy() * maxCount;
            int level = recipe.getLevel();
            for (int i = 0; i < 7; i++) {
                if (this.levels[i] < level && getStackInSlot(i) != null) {
                    return null;
                }
            }
        }

        return recipe;
    }

    public AppEngInternalInventory getOut() {
        return out;
    }

    public AppEngInternalInventory getInv() {
        return inv;
    }

    public AppEngInternalInventory getUpgrades() {
        return upgrades;
    }

    public int[] getLevels() {
        return this.levels;
    }

    public ItemStack installUpgrade(int slot, ItemStack stack) {
        if (stack == null)
            return null;
        int level = stack.getItemDamage() - 1;
        if (this.levels[slot] + 1 != level)
            return stack;
        this.levels[slot] = level;
        stack.stackSize--;
        if (stack.stackSize > 0)
            return stack;
        return null;
    }

    public void dropAllItems(World world, int x, int y, int z) {
        dropInventoryItems(world, x, y, z, out);
        dropInventoryItems(world, x, y, z, inv);
        dropInventoryItems(world, x, y, z, upgrades);
    }

    private void dropInventoryItems(World world, int x, int y, int z, AppEngInternalInventory inventory) {
        if (world.isRemote) return;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack != null && stack.stackSize > 0) {
                float offsetX = world.rand.nextFloat() * 0.8F + 0.1F;
                float offsetY = world.rand.nextFloat() * 0.8F + 0.1F;
                float offsetZ = world.rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ, stack.copy());
                entityItem.motionX = world.rand.nextGaussian() * 0.05;
                entityItem.motionY = world.rand.nextGaussian() * 0.05 + 0.2;
                entityItem.motionZ = world.rand.nextGaussian() * 0.05;
                world.spawnEntityInWorld(entityItem);

                inventory.setInventorySlotContents(i, null);
            }
        }
    }

    public void writeItemNBT(NBTTagCompound compound) {
        compound.setLong("energy", energyStored);
        compound.setIntArray("levels", this.levels);
    }

    public void readItemNBT(NBTTagCompound compound) {
        this.energyStored = compound.getLong("energy");
        if (compound.hasKey("levels"))
            this.levels = compound.getIntArray("levels");
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromWorldNBT_AF(NBTTagCompound compound) {
        this.inv.readFromNBT(compound, "inv");
        this.out.readFromNBT(compound, "out");
        this.upgrades.readFromNBT(compound, "upgrades");
        this.energyStored = compound.getLong("energyStored");
        this.maxEnergyStored = compound.getLong("maxEnergy");
        this.needEnergy = compound.getLong("needEnergy");
        this.levels = compound.getIntArray("levels");
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToWorldNBT_AF(NBTTagCompound compound) {
        this.inv.writeToNBT(compound, "inv");
        this.out.writeToNBT(compound, "out");
        this.upgrades.writeToNBT(compound, "upgrades");
        compound.setLong("energyStored", this.energyStored);
        compound.setLong("maxEnergy", this.maxEnergyStored);
        compound.setLong("needEnergy", this.needEnergy);
        compound.setIntArray("levels", this.levels);
    }

    @TileEvent(TileEventType.NETWORK_WRITE)
    public void writeToNetworkTAF(ByteBuf byteBuf) {
        byteBuf.writeLong(energyStored);
        byteBuf.writeLong(maxEnergyStored);
        for (int i = 0; i < 7; i++)
            byteBuf.writeByte(this.levels[i]);
    }

    @TileEvent(TileEventType.NETWORK_READ)
    public boolean readFromNetworkTAF(ByteBuf byteBuf) {
        energyStored = byteBuf.readLong();
        maxEnergyStored = byteBuf.readLong();
        for (int i = 0; i < 7; i++)
            this.levels[i] = byteBuf.readByte();
        return true;
    }

    public void readNEINBT(NBTTagCompound compound, EntityPlayer player) {
        boolean isInvEmpty = true;
        for (int i = 0; i < this.getSizeInventory(); i++) {
            if (this.inv.getStackInSlot(i) != null) {
                isInvEmpty = false;
                break;
            }
        }

        if (!isInvEmpty) {
            boolean canTransferAll = true;
            ItemStack[] tempInv = new ItemStack[this.getSizeInventory()];
            for (int i = 0; i < this.getSizeInventory(); i++) {
                if (this.inv.getStackInSlot(i) != null) {
                    tempInv[i] = this.inv.getStackInSlot(i).copy();
                }
            }

            for (int i = 0; i < this.getSizeInventory(); i++) {
                if (tempInv[i] != null) {
                    ItemStack stack = tempInv[i];
                    boolean added = false;

                    for (int j = 0; j < player.inventory.getSizeInventory(); j++) {
                        ItemStack playerStack = player.inventory.getStackInSlot(j);
                        if (playerStack != null && playerStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(playerStack, stack)) {
                            int space = playerStack.getMaxStackSize() - playerStack.stackSize;
                            if (space >= stack.stackSize) {
                                added = true;
                                break;
                            }
                        }
                    }

                    if (!added && !player.inventory.addItemStackToInventory(stack.copy())) {
                        canTransferAll = false;
                        break;
                    }
                }
            }

            if (canTransferAll) {
                for (int i = 0; i < this.getSizeInventory(); i++) {
                    if (this.inv.getStackInSlot(i) != null) {
                        ItemStack stack = this.inv.getStackInSlot(i);
                        for (int j = 0; j < player.inventory.getSizeInventory(); j++) {
                            ItemStack playerStack = player.inventory.getStackInSlot(j);
                            if (playerStack != null && playerStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(playerStack, stack)) {
                                int space = playerStack.getMaxStackSize() - playerStack.stackSize;
                                if (space > 0) {
                                    int toAdd = Math.min(space, stack.stackSize);
                                    playerStack.stackSize += toAdd;
                                    stack.stackSize -= toAdd;
                                    if (stack.stackSize <= 0) {
                                        this.inv.setInventorySlotContents(i, null);
                                        break;
                                    }
                                }
                            }
                        }
                        if (stack.stackSize > 0) {
                            //player.inventory.addItemStackToInventory(stack);
                            this.inv.setInventorySlotContents(i, null);
                        }
                    }
                }
            } else {
                return;
            }
        }

        for (int i = 0; i < this.getSizeInventory(); i++) {
            try {
                NBTTagCompound itemTag = compound.getCompoundTag("#" + i);
                if (!itemTag.hasNoTags()) {
                    ItemStack requiredStack = ItemStack.loadItemStackFromNBT(itemTag);
                    if (requiredStack != null) {
                        for (int j = 0; j < player.inventory.getSizeInventory(); j++) {
                            ItemStack playerStack = player.inventory.getStackInSlot(j);
                            if (playerStack != null && playerStack.isItemEqual(requiredStack) && ItemStack.areItemStackTagsEqual(playerStack, requiredStack)) {
                                this.inv.setInventorySlotContents(i, playerStack.copy());
                                player.inventory.setInventorySlotContents(j, null);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                AELog.debug(e);
            }
        }
    }

    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        long canReceive = Math.min(Math.min(receiveEnergy - this.thisTickEnergyReceived, i), getMaxEnergyStored() - this.energyStored);
        if (canReceive < 0L)
            return 0;
        if (!b) {
            this.thisTickEnergyReceived += canReceive;
            this.energyStored += canReceive;
        }
        markForUpdate();
        return (int)canReceive;
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return (int)Math.max(0L, Math.min(2147483647L, this.energyStored));
    }

    public long getEnergyStored() {
        return this.energyStored;
    }

    public double gaugeEnergyScaled(int i) {
        double a = this.energyStored * i;
        return a / this.maxEnergyStored;
    }

    public double gaugeProcessScaled(double i) {
        double a = this.craftingTicks * i;
        return a / this.maxCraftingTicks;
    }

    public int getCraftingTicks() {
        return craftingTicks;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return (int)Math.max(0L, Math.min(2147483647L, this.maxEnergyStored));
    }

    public long getMaxEnergyStored() {
        return maxEnergyStored;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return true;
    }
}
