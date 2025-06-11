package foxiwhitee.hellmod.integration.botania.me;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.FuzzyMode;
import appeng.api.exceptions.AppEngException;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.PlayerSource;
import appeng.api.storage.ICellInventory;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.tile.inventory.AppEngNullInventory;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.helpers.IManaStorageCell;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ManaCellInventory implements ICellInventory {
    private AEItemStack cellItem;
    private final ISaveProvider container;
    private final NBTTagCompound tagCompound;
    private final IManaStorageCell cellType;
    private long storedItems;

    private ManaCellInventory(ItemStack cellItem, ISaveProvider container) throws AppEngException {
        this.cellItem = AEItemStack.create(cellItem);
        this.container = container;
        if (cellItem == null || !(cellItem.getItem() instanceof IManaStorageCell)) {
            throw new AppEngException("ItemStack was used as a cell, but was not a cell!");
        }
        this.cellType = (IManaStorageCell) cellItem.getItem();
        this.tagCompound = Platform.openNbtData(cellItem);
        this.storedItems = this.tagCompound.getLong("ic");
    }

    private void setStoredItems(long value) {
        this.storedItems = value;
        this.tagCompound.setLong("ic", this.storedItems);
    }

    public IAEItemStack injectItems(IAEItemStack input, Actionable mode, BaseActionSource source) {
        if (input == null || input.getStackSize() == 0L) {
            return null;
        } else if (input.getItem() != BotaniaIntegration.mana_drop) {
            return input;
        } else  {
            if (cellItem != null) {
                long size = input.getStackSize();
                long remainingItemSlots = this.getRemainingItemCount();
                if (remainingItemSlots < 0L) {
                    return input;
                } else if (size > remainingItemSlots) {
                    IAEItemStack r = input.copy();
                    r.setStackSize(r.getStackSize() - remainingItemSlots);
                    if (mode == Actionable.MODULATE) {
                        this.setStoredItems(getStoredItemCount() + remainingItemSlots);
                    }
                    return r;
                } else if (this.canHoldNewItem()) {
                    long remainingItemCount = this.getRemainingItemCount() - this.getBytesPerType() * 8L;
                    if (remainingItemCount > 0) {
                        final IAEItemStack toReturn = input.copy();
                        if (input.getStackSize() > remainingItemCount) {
                            toReturn.decStackSize(remainingItemCount);

                            if (mode == Actionable.MODULATE) {
                                final IAEItemStack toWrite = input.copy();
                                toWrite.setStackSize(remainingItemCount);

                                this.setStoredItems(toWrite.getStackSize());
                            }
                            return toReturn;
                        }

                        if (mode == Actionable.MODULATE) {
                            this.setStoredItems(input.getStackSize());
                        }

                        return mode == Actionable.SIMULATE && (!(source instanceof PlayerSource) || !((PlayerSource) source).isPlayer()) ? toReturn : null;
                    }
                } else {
                    IAEItemStack r = input.copy();
                    r.setStackSize(remainingItemSlots - r.getStackSize());
                    if (mode == Actionable.MODULATE) {
                        this.setStoredItems(getStoredItemCount() + size);
                    }
                    return mode == Actionable.SIMULATE && (!(source instanceof PlayerSource) || !((PlayerSource) source).isPlayer()) ? r : null;
                }
                return input;
            }
            return null;
        }
    }

    public IAEItemStack extractItems(IAEItemStack input, Actionable mode, BaseActionSource source) {
        if (input == null || input.getItem() != BotaniaIntegration.mana_drop || (source instanceof PlayerSource && ((PlayerSource)source).isPlayer() && !(((PlayerSource)source).player.capabilities.isCreativeMode))) {
            return null;
        } else {
            long size = input.getStackSize();
            IAEItemStack results = null;
            if (cellItem != null) {
                results = input.copy();
                if (getStoredItemCount() <= size) {
                    results.setStackSize(getStoredItemCount());
                    if (mode == Actionable.MODULATE) {
                        this.setStoredItems(0);
                    }
                } else {
                    results.setStackSize(size);
                    if (mode == Actionable.MODULATE) {
                        this.setStoredItems(getStoredItemCount() - size);
                    }
                }
            }
            return results;
        }
    }

    public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> list) {
        if (list == null) list = AEApi.instance().storage().createItemList();
        AEItemStack stack = AEItemStack.create(new ItemStack(BotaniaIntegration.mana_drop));
        stack.setStackSize(this.storedItems);
        list.add(stack);
        return list;
    }

    public static IMEInventoryHandler<IAEItemStack> getCell(ItemStack o, ISaveProvider container) {
        try {
            return new ManaCellInventoryHandler(new ManaCellInventory(o, container));
        } catch (AppEngException ex) {
            return null;
        }
    }

    public static boolean isCell(ItemStack it) {
        if (it == null) return false;
        Item item = it.getItem();
        return item instanceof IManaStorageCell && ((IManaStorageCell) item).isManaCell(it);
    }

    public StorageChannel getChannel() {
        return StorageChannel.ITEMS;
    }

    public ItemStack getItemStack() {
        return this.cellItem.getItemStack();
    }

    public double getIdleDrain() {
        return this.cellType.getIdleDrain(this.cellItem.getItemStack());
    }

    public FuzzyMode getFuzzyMode() {
        return FuzzyMode.IGNORE_ALL;
    }

    public AppEngNullInventory getConfigInventory() {
        return new AppEngNullInventory();
    }

    public AppEngNullInventory getUpgradesInventory() {
        return new AppEngNullInventory();
    }

    public int getBytesPerType() {
        return 0;
    }

    public boolean canHoldNewItem() {
        return this.storedItems == 0L;
    }

    public long getTotalBytes() {
        return this.cellType.getBytes(this.cellItem.getItemStack());
    }

    public long getFreeBytes() {
        return getTotalBytes() - getUsedBytes();
    }

    public long getUsedBytes() {
        return (getStoredItemCount() + getUnusedItemCount()) / 8L;
    }

    public long getTotalItemTypes() {
        return 1L;
    }

    public long getStoredItemCount() {
        return this.storedItems;
    }

    public long getStoredItemTypes() {
        return 1L;
    }

    public long getRemainingItemTypes() {
        return 0L;
    }

    public long getRemainingItemCount() {
        return getFreeBytes() * 8L + getUnusedItemCount();
    }

    public int getUnusedItemCount() {
        int div = (int)(this.getStoredItemCount() % 8L);
        return div == 0 ? 0 : 8 - div;
    }

    public int getStatusForCell() {
        return getRemainingItemCount() > 0L ? 1 : 3;
    }

    public String getOreFilter() {
        return "";
    }

}
