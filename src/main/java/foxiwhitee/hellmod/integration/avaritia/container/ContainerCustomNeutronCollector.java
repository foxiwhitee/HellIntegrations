package foxiwhitee.hellmod.integration.avaritia.container;

import foxiwhitee.hellmod.integration.avaritia.tile.collectors.TileCustomNeutronCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerCustomNeutronCollector extends Container {
    private final TileCustomNeutronCollector tileNeutron;

    public ContainerCustomNeutronCollector(EntityPlayer player, TileCustomNeutronCollector machine) {
        this.tileNeutron = machine;

        this.addSlotToContainer(new SlotFurnace(player.inventory.player, machine, 2, 79+1, 32));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 16 + 84 + i * 18));
            }
        }

        for(int var5 = 0; var5 < 9; ++var5) {
            this.addSlotToContainer(new Slot(player.inventory, var5, 8 + var5 * 18, 158));
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        return this.tileNeutron.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNumber);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (slotNumber == 0) {
                if (!mergeItemStack(itemstack1, 1, 37, true))
                    return null;
                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotNumber >= 1 && slotNumber < 28) {
                if (!mergeItemStack(itemstack1, 28, 37, false))
                    return null;
            } else if (slotNumber >= 28 && slotNumber < 37 && !mergeItemStack(itemstack1, 1, 28, false)) {
                return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize)
                return null;
            slot.onPickupFromSlot(player, itemstack1);
        }
        return itemstack;
    }

    public TileEntity getTile() {
        return tileNeutron;
    }
}
