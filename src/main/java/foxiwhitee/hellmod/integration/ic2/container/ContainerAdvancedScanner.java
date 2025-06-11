package foxiwhitee.hellmod.integration.ic2.container;

import foxiwhitee.hellmod.integration.ic2.tile.TileAdvancedScanner;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerAdvancedScanner extends ContainerFullInv {
    private TileAdvancedScanner tile;
    public ContainerAdvancedScanner(EntityPlayer entityPlayer, TileAdvancedScanner tileEntity1) {
        super(entityPlayer, tileEntity1, 220 , 234);
        this.tile = tileEntity1;
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, 0, 101, 56));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.diskSlot, 0, 101, 92));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiChargeLevel");
        ret.add("tier");
        ret.add("state");
        ret.add("progress");
        ret.add("patternEu");
        ret.add("patternUu");
        return ret;
    }

    public TileAdvancedScanner getTile() {
        return tile;
    }
}
