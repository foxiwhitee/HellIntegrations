package foxiwhitee.hellmod.integration.botania.me;

import appeng.api.config.IncludeExclude;
import appeng.api.storage.ICellInventory;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.prioitylist.FuzzyPriorityList;

public class ManaCellInventoryHandler extends ManaMEInventoryHandler<IAEItemStack> implements ICellInventoryHandler {
    public ManaCellInventoryHandler(IMEInventory<IAEItemStack> c) {
        super(c, StorageChannel.ITEMS);
    }

    public ICellInventory getCellInv() {
        IMEInventory<IAEItemStack> inventory = getInternal();
        if (inventory instanceof ManaMEPassThrough) {
            inventory = ((ManaMEPassThrough) inventory).getInternal();
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
