package foxiwhitee.hellmod.tile;

import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.tile.grid.AENetworkTile;
import net.minecraft.inventory.InventoryCrafting;

public abstract class TileNetwork extends AENetworkTile implements ICraftingProvider, IGridTickable {
    protected boolean isBusy = false;

    public abstract void provideCrafting(ICraftingProviderHelper iCraftingProviderHelper);

    public abstract boolean pushPattern(ICraftingPatternDetails iCraftingPatternDetails, InventoryCrafting inventoryCrafting);

    public abstract TickRateModulation tickingRequest(IGridNode iGridNode, int i);

    public boolean isBusy() {
        return this.isBusy;
    }

}
