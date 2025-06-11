package foxiwhitee.hellmod.integration.draconic.container;

import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.container.ContainerDataSync;
import com.brandon3055.draconicevolution.common.utills.IUpgradableItem;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.integration.draconic.tile.TileCustomUpgradeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerCustomUpgradeModifier extends ContainerDataSync {
    private static final Item[] CORES_INDEX;
    private TileCustomUpgradeModifier tile;
    private EntityPlayer player;
    private boolean slotsActive = true;

    public ContainerCustomUpgradeModifier(EntityPlayer invPlayer, TileCustomUpgradeModifier tile) {
        this.tile = tile;
        this.player = invPlayer;

        for(int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(invPlayer.inventory, x, 8 + 18 * x, 167));
        }

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(invPlayer.inventory, x + y * 9 + 9, 8 + 18 * x, 111 + y * 18));
            }
        }

        this.addSlotToContainer(new SlotUpgradable(tile, 0, 112, 48));
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            icrafting.sendProgressBarUpdate(this, 0, 0);
        }

        this.updateSlotState();
    }

    public void updateProgressBar(int index, int value) {
        super.updateProgressBar(index, value);
        if (index == 0) {
            this.updateSlotState();
        }

    }

    private void updateSlotState() {
        if (this.tile.getStackInSlot(0) != null && (this.tile.getStackInSlot(0).getItem() instanceof ICustomUpgradableItem || this.tile.getStackInSlot(0).getItem() instanceof IUpgradableItem) && this.slotsActive) {
            for(Object o : this.inventorySlots) {
                if (o instanceof Slot && !(o instanceof SlotUpgradable)) {
                    ((Slot)o).xDisplayPosition += 1000;
                }
            }

            this.slotsActive = false;
        } else if ((this.tile.getStackInSlot(0) == null || !(this.tile.getStackInSlot(0).getItem() instanceof ICustomUpgradableItem || this.tile.getStackInSlot(0).getItem() instanceof IUpgradableItem)) && !this.slotsActive) {
            for(Object o : this.inventorySlots) {
                if (o instanceof Slot && !(o instanceof SlotUpgradable)) {
                    ((Slot)o).xDisplayPosition -= 1000;
                }
            }

            this.slotsActive = true;
        }

    }

    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        Slot slot = this.getSlot(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            ItemStack result = stack.copy();
            if (i >= 36) {
                if (!this.mergeItemStack(stack, 0, 36, false)) {
                    return null;
                }
            } else if (!this.isStackValidForInventory(stack, 0) || !this.mergeItemStack(stack, 36, 36 + this.tile.getSizeInventory(), false)) {
                return null;
            }

            if (stack.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }

            slot.onPickupFromSlot(player, stack);
            return result;
        } else {
            return null;
        }
    }

    private boolean isStackValidForInventory(ItemStack stack, int slot) {
        return true;
    }

    public void receiveSyncData(int index, int value) {
        if (this.tile.getStackInSlot(0).getItem() instanceof ICustomUpgradableItem) {
            ICustomUpgradableItem.EnumUpgrade upgrade = ICustomUpgradableItem.EnumUpgrade.getUpgradeByIndex(index);
            int coreTier = value / 2;
            boolean addCore = value % 2 == 0;
            ItemStack stack = this.tile.getStackInSlot(0);
            if (upgrade != null &&
                    stack != null &&
                    stack.getItem() instanceof ICustomUpgradableItem &&
                    coreTier >= 0 &&
                    coreTier <= 4 &&
                    (upgrade.getCoresApplied(stack)[coreTier] > 0 || addCore)) {
                this.handleCoreTransaction(upgrade, coreTier, addCore, (ICustomUpgradableItem) stack.getItem(), stack);
            }
        } else if (this.tile.getStackInSlot(0).getItem() instanceof IUpgradableItem) {
            IUpgradableItem.EnumUpgrade upgrade = IUpgradableItem.EnumUpgrade.getUpgradeByIndex(index);
            int coreTier = value / 2;
            boolean addCore = value % 2 == 0;
            ItemStack stack = this.tile.getStackInSlot(0);
            if (upgrade != null &&
                    stack != null &&
                    stack.getItem() instanceof IUpgradableItem &&
                    coreTier >= 0 &&
                    coreTier <= 3 &&
                    (upgrade.getCoresApplied(stack)[coreTier] > 0 || addCore)) {
                this.handleCoreTransaction(upgrade, coreTier, addCore, (IUpgradableItem) stack.getItem(), stack);
            }
        }
    }

    private void handleCoreTransaction(ICustomUpgradableItem.EnumUpgrade upgrade, int coreTier, boolean addCoreElseRemove, ICustomUpgradableItem upgradableItem, ItemStack stack) {
        int coreSlots = upgradableItem.getUpgradeCap(stack);
        int totalCores = 0;
        int[] coresApplied = upgrade.getCoresApplied(stack);

        for(ICustomUpgradableItem.EnumUpgrade u : upgradableItem.getCUpgrades(stack)) {
            totalCores += u.getCoresApplied(stack)[coreTier];
        }

        if (addCoreElseRemove) {
            if (!this.player.inventory.hasItem(CORES_INDEX[coreTier]) ||
                    totalCores >= coreSlots ||
                    upgrade.getUpgradePoints(stack) >= upgradableItem.getMaxUpgradePoints(upgrade.index, stack)) {
                return;
            }

            int var10002 = coresApplied[coreTier]++;
            this.player.inventory.consumeInventoryItem(CORES_INDEX[coreTier]);
            upgrade.setCoresApplied(stack, coresApplied);
            upgrade.onAppliedToItem(stack);
        } else {
            if (coresApplied[coreTier] <= 0) {
                return;
            }

            int var12 = coresApplied[coreTier]--;
            upgrade.setCoresApplied(stack, coresApplied);
            upgrade.onRemovedFromItem(stack);
            if (!this.player.inventory.addItemStackToInventory(new ItemStack(CORES_INDEX[coreTier]))) {
                EntityItem entityItem = new EntityItem(this.player.worldObj, this.player.posX, this.player.posY, this.player.posZ, new ItemStack(CORES_INDEX[coreTier]));
                if (!this.player.worldObj.isRemote) {
                    this.player.worldObj.spawnEntityInWorld(entityItem);
                }
            }
        }

    }

    private void handleCoreTransaction(IUpgradableItem.EnumUpgrade upgrade, int coreTier, boolean addCoreElseRemove, IUpgradableItem upgradableItem, ItemStack stack) {
        int coreSlots = upgradableItem.getUpgradeCap(stack);
        int totalCores = 0;
        int[] coresApplied = upgrade.getCoresApplied(stack);

        for(IUpgradableItem.EnumUpgrade u : upgradableItem.getUpgrades(stack)) {
            totalCores += u.getCoresApplied(stack)[coreTier];
        }

        if (addCoreElseRemove) {
            if (!this.player.inventory.hasItem(CORES_INDEX[coreTier]) ||
                    totalCores >= coreSlots ||
                    upgrade.getUpgradePoints(stack) >= upgradableItem.getMaxUpgradePoints(upgrade.index, stack)) {
                return;
            }

            int var10002 = coresApplied[coreTier]++;
            this.player.inventory.consumeInventoryItem(CORES_INDEX[coreTier]);
            upgrade.setCoresApplied(stack, coresApplied);
            upgrade.onAppliedToItem(stack);
        } else {
            if (coresApplied[coreTier] <= 0) {
                return;
            }

            int var12 = coresApplied[coreTier]--;
            upgrade.setCoresApplied(stack, coresApplied);
            upgrade.onRemovedFromItem(stack);
            if (!this.player.inventory.addItemStackToInventory(new ItemStack(CORES_INDEX[coreTier]))) {
                EntityItem entityItem = new EntityItem(this.player.worldObj, this.player.posX, this.player.posY, this.player.posZ, new ItemStack(CORES_INDEX[coreTier]));
                if (!this.player.worldObj.isRemote) {
                    this.player.worldObj.spawnEntityInWorld(entityItem);
                }
            }
        }

    }

    static {
        CORES_INDEX = new Item[]{ModItems.draconicCore, ModItems.wyvernCore, ModItems.awakenedCore, ModItems.chaoticCore, DraconicEvolutionIntegration.arialCore};
    }

    public TileEntity getTile() {
        return tile;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public static class SlotUpgradable extends Slot {
        public SlotUpgradable(IInventory inventory1, int slot, int x, int y) {
            super(inventory1, slot, x, y);
        }

        public boolean isItemValid(ItemStack stack) {
            return super.isItemValid(stack);
        }
    }
}
