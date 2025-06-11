package foxiwhitee.hellmod.tile;

import appeng.api.implementations.tiles.ICraftingMachine;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileNetworkInvA extends TileNetworkInv implements ICraftingMachine {

    @Override
    public boolean pushPattern(ICraftingPatternDetails iCraftingPatternDetails, InventoryCrafting inventoryCrafting, ForgeDirection forgeDirection) {
        return false;
    }

    @Override
    public boolean acceptsPlans() {
        return false;
    }
}