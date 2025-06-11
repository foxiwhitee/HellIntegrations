package foxiwhitee.hellmod.utils.cells;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.IncludeExclude;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.util.prioitylist.DefaultPriorityList;
import appeng.util.prioitylist.IPartitionList;

public class CustomMEInventoryHandler <T extends IAEStack<T>> implements IMEInventoryHandler<T> {
    private final IMEInventoryHandler<T> internal;
    private int myPriority;
    private IncludeExclude myWhitelist;
    private AccessRestriction myAccess;
    private IPartitionList<T> myPartitionList;
    private AccessRestriction cachedAccessRestriction;
    private boolean hasReadAccess;
    private boolean hasWriteAccess;

    public CustomMEInventoryHandler(IMEInventory<T> i, StorageChannel channel) {
        if (i instanceof IMEInventoryHandler) {
            this.internal = (IMEInventoryHandler)i;
        } else {
            this.internal = new CustomMEPassThrough(i, channel);
        }

        this.myPriority = 0;
        this.myWhitelist = IncludeExclude.WHITELIST;
        this.setBaseAccess(AccessRestriction.READ_WRITE);
        this.myPartitionList = new DefaultPriorityList();
    }

    IncludeExclude getWhitelist() {
        return this.myWhitelist;
    }

    public void setWhitelist(IncludeExclude myWhitelist) {
        this.myWhitelist = myWhitelist;
    }

    public AccessRestriction getBaseAccess() {
        return this.myAccess;
    }

    public void setBaseAccess(AccessRestriction myAccess) {
        this.myAccess = myAccess;
        this.cachedAccessRestriction = this.myAccess.restrictPermissions(this.internal.getAccess());
        this.hasReadAccess = this.cachedAccessRestriction.hasPermission(AccessRestriction.READ);
        this.hasWriteAccess = this.cachedAccessRestriction.hasPermission(AccessRestriction.WRITE);
    }

    IPartitionList<T> getPartitionList() {
        return this.myPartitionList;
    }

    public void setPartitionList(IPartitionList<T> myPartitionList) {
        this.myPartitionList = myPartitionList;
    }

    public T injectItems(T input, Actionable type, BaseActionSource src) {
        return (T)(!this.canAccept(input) ? input : this.internal.injectItems(input, type, src));
    }

    public T extractItems(T request, Actionable type, BaseActionSource src) {
        return (T)(!this.hasReadAccess ? null : this.internal.extractItems(request, type, src));
    }

    public IItemList<T> getAvailableItems(IItemList<T> out) {
        return !this.hasReadAccess ? out : this.internal.getAvailableItems(out);
    }

    public StorageChannel getChannel() {
        return this.internal.getChannel();
    }

    public AccessRestriction getAccess() {
        return this.cachedAccessRestriction;
    }

    public boolean isPrioritized(T input) {
        if (this.myWhitelist != IncludeExclude.WHITELIST) {
            return false;
        } else {
            return this.myPartitionList.isListed(input) || this.internal.isPrioritized(input);
        }
    }

    public boolean canAccept(T input) {
        if (!this.hasWriteAccess) {
            return false;
        } else if (this.myWhitelist == IncludeExclude.BLACKLIST && this.myPartitionList.isListed(input)) {
            return false;
        } else if (!this.myPartitionList.isEmpty() && this.myWhitelist != IncludeExclude.BLACKLIST) {
            return this.myPartitionList.isListed(input) && this.internal.canAccept(input);
        } else {
            return this.internal.canAccept(input);
        }
    }

    public int getPriority() {
        return this.myPriority;
    }

    public void setPriority(int myPriority) {
        this.myPriority = myPriority;
    }

    public int getSlot() {
        return this.internal.getSlot();
    }

    public boolean validForPass(int i) {
        return true;
    }

    public IMEInventory<T> getInternal() {
        return this.internal;
    }
}
