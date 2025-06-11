package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui;

import net.minecraft.inventory.Slot;

public interface IModularGuiAccessor {
    boolean isMouseOverSlotAcc(Slot paramSlot, int paramInt1, int paramInt2);

    default boolean overrideDefaultIsMouseOverSlot() {
        return false;
    }
}
