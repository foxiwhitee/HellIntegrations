package foxiwhitee.hellmod.container;

import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotRestrictedInput;
import foxiwhitee.hellmod.tile.assemblers.TileCustomMolecularAssembler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public class ContainerCustomMolecularAssembler extends AEBaseContainer {
    public ContainerCustomMolecularAssembler(InventoryPlayer ip, TileCustomMolecularAssembler te) {
        super(ip, (TileEntity)te, null);
        for (int x = 0; x < 36; x++) {
            int y = x / 9;
            addSlotToContainer((Slot)new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.ENCODED_PATTERN, te.getInternalInventory(), x, 8 + 18 * x - 162 * y, 29 + 18 * y,
                    getInventoryPlayer()));
        }
        bindPlayerInventory(ip, 0, 124);
    }
}
