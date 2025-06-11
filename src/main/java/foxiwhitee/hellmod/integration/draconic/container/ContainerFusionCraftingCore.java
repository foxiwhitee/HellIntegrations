package foxiwhitee.hellmod.integration.draconic.container;

import appeng.container.slot.SlotPlayerHotBar;
import appeng.container.slot.SlotPlayerInv;
import foxiwhitee.hellmod.integration.draconic.tile.TileFusionCraftingCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFusionCraftingCore extends Container {
    private final TileFusionCraftingCore tile;
    private EntityPlayer player;

    public ContainerFusionCraftingCore(EntityPlayer player, TileFusionCraftingCore tile) {
        this.tile = tile;
        this.player = player;
        bindPlayerInventory(player.inventory, 2, 116);
        addSlotToContainer(new Slot((IInventory)tile, 0, 82, 26));
        addSlotToContainer(new OutputSlot((IInventory)tile, 1, 82, 70));
    }

    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        Slot slot = getSlot(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            ItemStack result = stack.copy();
            if (i >= 36) {
                if (!mergeItemStack(stack, 0, 36, false))
                    return null;
            } else if (!this.tile.isItemValidForSlot(0, stack) || !mergeItemStack(stack, 36, 36 + this.tile.getSizeInventory(), false)) {
                return null;
            }
            if (stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            slot.onPickupFromSlot(player, stack);
            return result;
        }
        return null;
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int offsetX, int offsetY) {
        int i;
        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++)
                addSlotToContainer((Slot)new SlotPlayerInv((IInventory)inventoryPlayer, j + i * 9 + 9, 8 + j * 18 + offsetX, offsetY + i * 18));
        }
        for (i = 0; i < 9; i++)
            addSlotToContainer((Slot)new SlotPlayerHotBar((IInventory)inventoryPlayer, i, 8 + i * 18 + offsetX, 58 + offsetY));
    }

    public TileFusionCraftingCore getTile() {
        return this.tile;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public class OutputSlot extends Slot {
        public OutputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}
