package foxiwhitee.hellmod.integration.botania.me;


import appeng.api.storage.IMEInventory;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.me.storage.MEPassThrough;

public class ManaMEPassThrough<T extends IAEStack<T>> extends MEPassThrough<T> {
    public ManaMEPassThrough(IMEInventory<T> i, StorageChannel channel) {
        super(i, channel);
    }

    public IMEInventory<T> getInternal() {
        return super.getInternal();
    }
}

