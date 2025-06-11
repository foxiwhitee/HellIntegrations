package foxiwhitee.hellmod.container.terminals;

import appeng.api.storage.ITerminalHost;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.IOptionalSlotHost;
import appeng.helpers.IContainerCraftingPacket;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.tile.inventory.InvOperation;
import foxiwhitee.hellmod.parts.PartTerminal;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class ContainerTerminal extends ContainerMEMonitorable implements IAEAppEngInventory, IOptionalSlotHost, IContainerCraftingPacket {
    protected final PartTerminal terminal;

    public ContainerTerminal(InventoryPlayer ip, ITerminalHost monitorable) {
        super(ip, monitorable, false);
        this.terminal = (PartTerminal)monitorable;
        this.bindPlayerInventory(ip, 25, -55);
    }

    @Override
    public boolean isSlotEnabled(int idx) {
        if (idx == 1) {
            return true;
        } else if (idx == 2) {
            return false;
        } else {
            return false;
        }
    }

    @Override
    public IInventory getInventoryByName(String name) {
        return (IInventory)(name.equals("player") ? this.getInventoryPlayer() : this.getTerminal().getInventoryByName(name));
    }

    @Override
    public boolean useRealItems() { return false; }

    @Override
    public void saveChanges() {}

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {}

    public PartTerminal getTerminal() {
        return terminal;
    }
}
