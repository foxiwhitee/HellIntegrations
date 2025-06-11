package foxiwhitee.hellmod.container.slots;

import appeng.container.slot.AppEngSlot;
import appeng.container.slot.SlotFake;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotFakeAdv extends SlotFake {
    public SlotFakeAdv(IInventory inv, int idx, int x, int y) {
        super(inv, idx, x, y);
    }

    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {}

    public ItemStack decrStackSize(int par1) {
        return null;
    }

    public boolean isItemValid(ItemStack par1ItemStack) {
        return true;
    }

    public void putStack(ItemStack is) {
        if (is != null) {
            is = is.copy();
            is.stackSize = 1;
        }
        super.putStack(is);
    }

    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return false;
    }
}