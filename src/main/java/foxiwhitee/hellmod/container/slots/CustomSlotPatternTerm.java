package foxiwhitee.hellmod.container.slots;

import appeng.api.AEApi;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IStorageMonitorable;
import appeng.container.slot.IOptionalSlotHost;
import appeng.container.slot.SlotCraftingTerm;
import appeng.core.sync.AppEngPacket;
import appeng.core.sync.packets.PacketPatternSlot;
import appeng.helpers.IContainerCraftingPacket;
import appeng.helpers.InventoryAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.io.IOException;

public class CustomSlotPatternTerm extends SlotCraftingTerm {
    private final int groupNum;
    private final IOptionalSlotHost host;
    private final IInventory pattern;

    public CustomSlotPatternTerm(EntityPlayer player, BaseActionSource mySrc, IEnergySource energySrc, IStorageMonitorable storage, IInventory cMatrix, IInventory secondMatrix, IInventory output, int x, int y, IOptionalSlotHost h, int groupNumber, IContainerCraftingPacket c) {
        super(player, mySrc, energySrc, storage, cMatrix, secondMatrix, output, x, y, c);
        this.host = h;
        this.groupNum = groupNumber;
        this.pattern = cMatrix;
    }

    public AppEngPacket getRequest(boolean shift) throws IOException {

        return new PacketPatternSlot(this.getPattern(), AEApi.instance().storage().createItemStack(this.getStack()), shift);
    }


    public ItemStack getStack() {
        if (!this.isEnabled() && this.getDisplayStack() != null) {
            this.clearStack();
        }

        return super.getStack();
    }

    public boolean isEnabled() {
        return this.host == null ? false : this.host.isSlotEnabled(this.groupNum);
    }

    IInventory getPattern() {
        return this.pattern;
    }

    @Override
    public void doClick(InventoryAction action, EntityPlayer who) {

    }
}
