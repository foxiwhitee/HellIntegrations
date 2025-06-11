package foxiwhitee.hellmod.container;

import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotFake;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.container.slot.SlotRestrictedInput;
import foxiwhitee.hellmod.container.slots.SlotFakeAdv;
import foxiwhitee.hellmod.tile.TileCobblestoneDuper;
import foxiwhitee.hellmod.tile.fluid.TileFluidReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerFluidReceiver extends AEBaseContainer {

    public ContainerFluidReceiver(EntityPlayer ip, TileFluidReceiver tile) {
        super(ip.inventory, (TileEntity)tile, null);
        addSlotToContainer((Slot)new SlotFakeAdv(tile.getInternalInventory(), 0, 97, 49));
        bindPlayerInventory(ip.inventory, 17, 117);
    }

    public ItemStack slotClick(int slotNum, int button, int modifiers, EntityPlayer player) {
        if (slotNum >= 0) {
            Object object = this.inventorySlots.get(slotNum);
            if (object instanceof SlotFake) {
                ((SlotFake) object).putStack(player.inventory.getItemStack());
            }
        }
        return super.slotClick(slotNum, button, modifiers, player);
    }
}
