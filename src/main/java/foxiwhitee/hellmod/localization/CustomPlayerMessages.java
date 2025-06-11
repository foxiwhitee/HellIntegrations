package foxiwhitee.hellmod.localization;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public enum CustomPlayerMessages {
    ChestCannotReadStorageCell, InvalidMachine, LoadedSettings, SavedSettings, MachineNotPowered, isNowLocked, isNowUnlocked, AmmoDepleted, CommunicationError, OutOfRange, DeviceNotPowered, DeviceNotWirelessTerminal, DeviceNotLinked, StationCanNotBeLocated, SettingCleared, TunnelNotConnected, TunnelInputIsAt, TunnelHasNoOutputs, TunnelOutputsAreAt, InterfaceInOtherDim, InterfaceHighlighted, CraftingItemsWentMissing;

    public IChatComponent get() {
        return (IChatComponent)new ChatComponentTranslation(getName(), new Object[0]);
    }

    public String getName() {
        return "chat.appliedenergistics2." + toString();
    }
}
