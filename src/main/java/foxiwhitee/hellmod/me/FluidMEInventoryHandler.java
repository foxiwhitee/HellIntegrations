package foxiwhitee.hellmod.me;

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

public class FluidMEInventoryHandler<T extends IAEStack<T>> implements IMEInventoryHandler<T> {
    private final IMEInventoryHandler<T> internal;
    private int myPriority;
    private IncludeExclude myWhitelist;
    private AccessRestriction myAccess;
    private IPartitionList<T> myPartitionList;
    private AccessRestriction cachedAccessRestriction;
    private boolean hasReadAccess;
    private boolean hasWriteAccess;

    public FluidMEInventoryHandler(IMEInventory<T> i, StorageChannel channel) {
        this.internal = i instanceof IMEInventoryHandler ? (IMEInventoryHandler<T>) i : new FluidMEPassThrough<>(i, channel);
        this.myPriority = 0;
        this.myWhitelist = IncludeExclude.WHITELIST;
        setBaseAccess(AccessRestriction.READ_WRITE);
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
        return canAccept(input) ? this.internal.injectItems(input, type, src) : input;
    }

    public T extractItems(T request, Actionable type, BaseActionSource src) {
        return this.hasReadAccess ? this.internal.extractItems(request, type, src) : null;
    }

    public IItemList<T> getAvailableItems(IItemList<T> out) {
        return this.hasReadAccess ? this.internal.getAvailableItems(out) : out;
    }

    public StorageChannel getChannel() {
        return this.internal.getChannel();
    }

    public AccessRestriction getAccess() {
        return this.cachedAccessRestriction;
    }

    public boolean isPrioritized(T input) {
        return this.myWhitelist == IncludeExclude.WHITELIST &&
                (this.myPartitionList.isListed(input) || this.internal.isPrioritized(input));
    }

    public boolean canAccept(T input) {
        if (!this.hasWriteAccess) return false;
        if (this.myWhitelist == IncludeExclude.BLACKLIST && this.myPartitionList.isListed(input)) return false;
        if (!this.myPartitionList.isEmpty() && this.myWhitelist != IncludeExclude.BLACKLIST)
            return this.myPartitionList.isListed(input) && this.internal.canAccept(input);
        return this.internal.canAccept(input);
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
