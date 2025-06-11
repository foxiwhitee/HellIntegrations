package foxiwhitee.hellmod.tile;

import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.tile.grid.AENetworkInvTile;
import appeng.tile.inventory.InvOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileNetworkInv extends AENetworkInvTile implements ICraftingProvider, IGridTickable {
    protected boolean isBusy = false;

    public abstract void provideCrafting(ICraftingProviderHelper iCraftingProviderHelper);

    public abstract boolean pushPattern(ICraftingPatternDetails iCraftingPatternDetails, InventoryCrafting inventoryCrafting);

    public abstract TickingRequest getTickingRequest(IGridNode iGridNode);

    public abstract TickRateModulation tickingRequest(IGridNode iGridNode, int i);

    public boolean isBusy() {
        return this.isBusy;
    }

    public abstract IInventory getInternalInventory();

    public abstract void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1);

    public abstract int[] getAccessibleSlotsBySide(ForgeDirection forgeDirection);

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }
}
