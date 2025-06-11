package foxiwhitee.hellmod.container.slots;

import appeng.container.slot.IOptionalSlotHost;
import appeng.container.slot.SlotPatternOutputs;
import net.minecraft.inventory.IInventory;

public class CustomSlotPatternOutputs extends SlotPatternOutputs {
    public boolean display = true;

    public CustomSlotPatternOutputs(IInventory inv, IOptionalSlotHost containerBus, int idx, int x, int y, int offX, int offY, int groupNum) {
        super(inv, containerBus, idx, x, y, offX, offY, groupNum);
    }

    @Override
    public boolean shouldDisplay() {
        return display;
    }
}
