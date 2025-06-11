package foxiwhitee.hellmod.tile.fluid;

import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TileFluidSupplier extends NetworkFluidTile implements IGridTickable {
    private final AppEngInternalInventory inv = new AppEngInternalInventory(this, 1);

    @Override
    public IInventory getInternalInventory() {
        return inv;
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {}

    @Override
    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(1, 1, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode iGridNode, int i) {
        try {
            if (getTank().getFluid() != null) {
                injectFluidDrop(getTank().getFluid().amount);
                markDirty();
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        return TickRateModulation.IDLE;
    }
}
