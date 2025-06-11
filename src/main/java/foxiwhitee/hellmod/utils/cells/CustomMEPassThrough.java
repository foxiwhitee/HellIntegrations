package foxiwhitee.hellmod.utils.cells;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;

public class CustomMEPassThrough<T extends IAEStack<T>> implements IMEInventoryHandler<T> {
    private final StorageChannel wrappedChannel;
    private IMEInventory<T> internal;

    public CustomMEPassThrough(IMEInventory<T> i, StorageChannel channel) {
        this.wrappedChannel = channel;
        this.setInternal(i);
    }

    protected IMEInventory<T> getInternal() {
        return this.internal;
    }

    public void setInternal(IMEInventory<T> i) {
        this.internal = i;
    }

    public T injectItems(T input, Actionable type, BaseActionSource src) {
        return (T)this.internal.injectItems(input, type, src);
    }

    public T extractItems(T request, Actionable type, BaseActionSource src) {
        return (T)this.internal.extractItems(request, type, src);
    }

    public IItemList<T> getAvailableItems(IItemList out) {
        return this.internal.getAvailableItems(out);
    }

    public StorageChannel getChannel() {
        return this.internal.getChannel();
    }

    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    public boolean isPrioritized(T input) {
        return false;
    }

    public boolean canAccept(T input) {
        return true;
    }

    public int getPriority() {
        return 0;
    }

    public int getSlot() {
        return 0;
    }

    public boolean validForPass(int i) {
        return true;
    }

    StorageChannel getWrappedChannel() {
        return this.wrappedChannel;
    }
}
