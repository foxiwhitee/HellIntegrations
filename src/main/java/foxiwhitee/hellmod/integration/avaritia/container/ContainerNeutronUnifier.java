package foxiwhitee.hellmod.integration.avaritia.container;

import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotRestrictedInput;
import foxiwhitee.hellmod.container.slots.CustomSlotRestrictedInput;
import foxiwhitee.hellmod.integration.avaritia.tile.TileNeutronUnifier;
import foxiwhitee.hellmod.tile.assemblers.TileCustomMolecularAssembler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public class ContainerNeutronUnifier extends AEBaseContainer {
    public ContainerNeutronUnifier(EntityPlayer ip, TileNeutronUnifier te) {
        super(ip.inventory, (TileEntity)te, null);
        for (int x = 0; x < 36; x++) {
            int y = x / 9;
            addSlotToContainer((Slot)new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.NEUTRON, te.getInternalInventory(), x, 8 + 18 * x - 162 * y, 29 + 18 * y,
                    getInventoryPlayer()));
        }
        bindPlayerInventory(ip.inventory, 0, 124);
    }
}
