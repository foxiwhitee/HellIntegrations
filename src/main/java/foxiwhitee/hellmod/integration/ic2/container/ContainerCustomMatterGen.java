package foxiwhitee.hellmod.integration.ic2.container;

import foxiwhitee.hellmod.integration.ic2.tile.matter.TileCustomMatterGen;
import ic2.core.ContainerFullInv;
import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

import java.util.List;

public class ContainerCustomMatterGen extends ContainerFullInv<TileCustomMatterGen> {
    private TileCustomMatterGen tile;

    public ContainerCustomMatterGen(EntityPlayer entityPlayer, TileCustomMatterGen tileEntity1) {
        super(entityPlayer, tileEntity1, 217 , 234);
        this.tile = tileEntity1;
        addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity1.amplifierSlot, 0, 128, 38));
        addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity1.outputSlot, 0, 96, 96));
        addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity1.containerslot, 0, 96, 52));
        addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity1.upgradeSlot, 0, 17, 69));
        addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity1.upgradeSlot, 1, 17, 88));
        addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity1.upgradeSlot, 2, 17, 107));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("scrap");
        ret.add("fluidTank");
        return ret;
    }

    public TileCustomMatterGen getTile() {
        return tile;
    }
}
