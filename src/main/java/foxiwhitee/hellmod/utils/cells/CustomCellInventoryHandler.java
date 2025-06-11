package foxiwhitee.hellmod.utils.cells;

import appeng.api.AEApi;
import appeng.api.config.*;
import appeng.api.implementations.items.IUpgradeModule;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;
import appeng.util.prioitylist.FuzzyPriorityList;
import appeng.util.prioitylist.PrecisePriorityList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class CustomCellInventoryHandler extends CustomMEInventoryHandler<IAEItemStack> implements ICellInventoryHandler {
    CustomCellInventoryHandler(IMEInventory<IAEItemStack> c) {
        super(c, StorageChannel.ITEMS);
        ICellInventory ci = this.getCellInv();
        if (ci != null) {
            IItemList<IAEItemStack> priorityList = AEApi.instance().storage().createItemList();
            IInventory upgrades = ci.getUpgradesInventory();
            IInventory config = ci.getConfigInventory();
            FuzzyMode fzMode = ci.getFuzzyMode();
            boolean hasInverter = false;
            boolean hasFuzzy = false;

            for(int x = 0; x < upgrades.getSizeInventory(); ++x) {
                ItemStack is = upgrades.getStackInSlot(x);
                if (is != null && is.getItem() instanceof IUpgradeModule) {
                    Upgrades u = ((IUpgradeModule)is.getItem()).getType(is);
                    if (u != null) {
                        switch (u) {
                            case FUZZY:
                                hasFuzzy = true;
                                break;
                            case INVERTER:
                                hasInverter = true;
                        }
                    }
                }
            }

            for(int x = 0; x < config.getSizeInventory(); ++x) {
                ItemStack is = config.getStackInSlot(x);
                if (is != null) {
                    priorityList.add(AEItemStack.create(is));
                }
            }

            this.setWhitelist(hasInverter ? IncludeExclude.BLACKLIST : IncludeExclude.WHITELIST);
            if (!priorityList.isEmpty()) {
                if (hasFuzzy) {
                    this.setPartitionList(new FuzzyPriorityList(priorityList, fzMode));
                } else {
                    this.setPartitionList(new PrecisePriorityList(priorityList));
                }
            }
        }

    }

    public ICellInventory getCellInv() {
        Object o = this.getInternal();
        if (o instanceof CustomMEPassThrough) {
            o = ((CustomMEPassThrough)o).getInternal();
        }

        return (ICellInventory)(o instanceof ICellInventory ? o : null);
    }

    public boolean isPreformatted() {
        return !this.getPartitionList().isEmpty();
    }

    public boolean isFuzzy() {
        return this.getPartitionList() instanceof FuzzyPriorityList;
    }

    public IncludeExclude getIncludeExcludeMode() {
        return this.getWhitelist();
    }

    public int getStatusForCell() {
        int val = this.getCellInv().getStatusForCell();
        if (val == 1 && this.isPreformatted()) {
            val = 2;
        }

        return val;
    }
}