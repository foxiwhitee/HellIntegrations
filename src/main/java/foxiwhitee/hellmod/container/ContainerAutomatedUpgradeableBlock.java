package foxiwhitee.hellmod.container;

import appeng.container.AEBaseContainer;
import appeng.container.slot.AppEngSlot;
import appeng.container.slot.SlotRestrictedInput;
import foxiwhitee.hellmod.container.slots.CustomAppEngSlot;
import foxiwhitee.hellmod.container.slots.CustomSlotRestrictedInput;
import foxiwhitee.hellmod.tile.TileAutomatedUpgradeableBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public class ContainerAutomatedUpgradeableBlock extends AEBaseContainer {
    private final TileAutomatedUpgradeableBlock tile;

    public ContainerAutomatedUpgradeableBlock(EntityPlayer ip, TileAutomatedUpgradeableBlock tile) {
        super(ip.inventory, (TileEntity)tile, null);
        this.tile = tile;
        addSlotToContainer((Slot)new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.AUTOMATED_BLOCK_UPGRADE, (IInventory)this.tile, 0, 97, 49, getInventoryPlayer()));
        bindPlayerInventory(ip.inventory, 17, 117);
    }

}