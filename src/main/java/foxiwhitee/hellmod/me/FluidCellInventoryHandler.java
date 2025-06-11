package foxiwhitee.hellmod.me;

import appeng.api.config.IncludeExclude;
import appeng.api.storage.ICellInventory;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.prioitylist.FuzzyPriorityList;

public class FluidCellInventoryHandler extends FluidMEInventoryHandler<IAEItemStack> implements ICellInventoryHandler {
    public FluidCellInventoryHandler(IMEInventory<IAEItemStack> c) {
        super(c, StorageChannel.ITEMS);
    }

    public ICellInventory getCellInv() {
        IMEInventory<IAEItemStack> inventory = getInternal();
        if (inventory instanceof FluidMEPassThrough) {
            inventory = ((FluidMEPassThrough) inventory).getInternal();
        }
        return inventory instanceof ICellInventory ? (ICellInventory) inventory : null;
    }

    public boolean isPreformatted() {
        return !getPartitionList().isEmpty();
    }

    public boolean isFuzzy() {
        return getPartitionList() instanceof FuzzyPriorityList;
    }

    public IncludeExclude getIncludeExcludeMode() {
        return IncludeExclude.WHITELIST;
    }
}
