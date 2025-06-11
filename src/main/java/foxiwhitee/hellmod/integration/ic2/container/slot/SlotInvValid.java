package foxiwhitee.hellmod.integration.ic2.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInvValid extends Slot {
    private final IInventory inv;

    public SlotInvValid(IInventory inv, int p_i18242, int p_i18243, int p_i18244) {
        super(inv, p_i18242, p_i18243, p_i18244);
        this.inv = inv;
    }

    public boolean isItemValid(ItemStack stack) {
        return this.inv.isItemValidForSlot(getSlotIndex(), stack);
    }

    public int getSlotStackLimit() {
        return 64;
    }
}