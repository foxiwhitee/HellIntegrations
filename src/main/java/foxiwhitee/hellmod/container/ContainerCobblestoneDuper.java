package foxiwhitee.hellmod.container;

import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotRestrictedInput;
import foxiwhitee.hellmod.tile.TileCobblestoneDuper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public class ContainerCobblestoneDuper extends AEBaseContainer {
    private final TileCobblestoneDuper chest;

    public ContainerCobblestoneDuper(EntityPlayer ip, TileCobblestoneDuper chest) {
        super(ip.inventory, (TileEntity)chest, null);
        this.chest = chest;
        addSlotToContainer((Slot)new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.STORAGE_CELLS, (IInventory)this.chest, 1, 97, 49, getInventoryPlayer()));
        bindPlayerInventory(ip.inventory, 17, 117);
    }
}
