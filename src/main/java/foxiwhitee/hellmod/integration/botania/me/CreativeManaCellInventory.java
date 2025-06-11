package foxiwhitee.hellmod.integration.botania.me;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeManaCellInventory implements IMEInventoryHandler<IAEItemStack> {
    public IAEItemStack injectItems(IAEItemStack stack, Actionable p1, BaseActionSource p2) {
        if (stack != null && stack.getItem() == BotaniaIntegration.mana_drop) {
            return null;
        }
        return stack;
    }

    public IAEItemStack extractItems(IAEItemStack stack, Actionable p1, BaseActionSource p2) {
        if (stack != null && stack.getItem() == BotaniaIntegration.mana_drop) {
            return stack.copy();
        }
        return null;
    }

    public IItemList<IAEItemStack> getAvailableItems(IItemList list) {
        IItemList<IAEItemStack> l = AEApi.instance().storage().createItemList();
        AEItemStack stack = AEItemStack.create(new ItemStack(BotaniaIntegration.mana_drop));
        stack.setStackSize(2305843009213693951L);
        l.add(stack);
        return l;
    }

    public StorageChannel getChannel() {
        return StorageChannel.ITEMS;
    }

    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    public boolean isPrioritized(IAEItemStack stack) {
        return stack != null && stack.getItem() == BotaniaIntegration.mana_drop;
    }

    public boolean canAccept(IAEItemStack stack) {
        return stack != null && stack.getItem() == BotaniaIntegration.mana_drop;
    }

    public int getPriority() {
        return 0;
    }

    public int getSlot() {
        return 0;
    }

    public boolean validForPass(int p0) {
        return true;
    }
}
