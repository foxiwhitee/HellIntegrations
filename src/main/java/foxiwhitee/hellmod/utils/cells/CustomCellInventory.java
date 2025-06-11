package foxiwhitee.hellmod.utils.cells;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.FuzzyMode;
import appeng.api.exceptions.AppEngException;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.Set;

public class CustomCellInventory implements ICellInventory {
    private static final String ITEM_TYPE_TAG = "it";
    private static final String ITEM_COUNT_TAG = "ic";
    private static final String ITEM_SLOT = "#";
    private static final String ITEM_SLOT_COUNT = "@";
    private static final Set<Integer> BLACK_LIST = new HashSet();
    private static String[] itemSlots;
    private static String[] itemSlotCount;
    private final NBTTagCompound tagCompound;
    private final ISaveProvider container;
    private int maxItemTypes = 1024;
    private short storedItems = 0;
    private long storedItemCount = 0;
    private IItemList<IAEItemStack> cellItems;
    private final ItemStack cellItem;
    private ICustomStorageCell cellType;

    private CustomCellInventory(ItemStack o, ISaveProvider container) throws AppEngException {
        if (itemSlots == null) {
            itemSlots = new String[this.maxItemTypes];
            itemSlotCount = new String[this.maxItemTypes];

            for(int x = 0; x < this.maxItemTypes; ++x) {
                itemSlots[x] = "#" + x;
                itemSlotCount[x] = "@" + x;
            }
        }

        if (o == null) {
            throw new AppEngException("ItemStack was used as a cell, but was not a cell!");
        } else {
            this.cellType = null;
            this.cellItem = o;
            Item type = this.cellItem.getItem();
            if (type instanceof ICustomStorageCell) {
                this.cellType = (ICustomStorageCell)this.cellItem.getItem();
                this.maxItemTypes = this.cellType.getTotalTypes(this.cellItem);
            }

            if (this.cellType == null) {
                throw new AppEngException("ItemStack was used as a cell, but was not a cell!");
            } else if (!this.cellType.isStorageCell(this.cellItem)) {
                throw new AppEngException("ItemStack was used as a cell, but was not a cell!");
            } else {
                if (this.maxItemTypes > 1024) {
                    this.maxItemTypes = 1024;
                }

                if (this.maxItemTypes < 1) {
                    this.maxItemTypes = 1;
                }

                this.container = container;
                this.tagCompound = Platform.openNbtData(o);
                this.storedItems = this.tagCompound.getShort("it");
                this.storedItemCount = this.tagCompound.getLong("ic");
                this.cellItems = null;
            }
        }
    }

    public static IMEInventoryHandler<IAEItemStack> getCell(ItemStack o, ISaveProvider container2) {
        try {
            return new CustomCellInventoryHandler(new CustomCellInventory(o, container2));
        } catch (AppEngException var3) {
            return null;
        }
    }

    private static boolean isStorageCell(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        } else {
            try {
                Item type = itemStack.getItem();
                if (type instanceof ICustomStorageCell) {
                    return !((ICustomStorageCell)type).storableInStorageCell();
                } else {
                    return false;
                }
            } catch (Throwable var2) {
                return true;
            }
        }
    }

    public static boolean isCell(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        } else {
            Item type = itemStack.getItem();
            return type instanceof ICustomStorageCell ? ((ICustomStorageCell)type).isStorageCell(itemStack) : false;
        }
    }

    public static void addBasicBlackList(int itemID, int meta) {
        BLACK_LIST.add(meta << 16 | itemID);
    }

    private static boolean isBlackListed(IAEItemStack input) {
        return BLACK_LIST.contains(2147418112 | Item.getIdFromItem(input.getItem())) ? true : BLACK_LIST.contains(input.getItemDamage() << 16 | Item.getIdFromItem(input.getItem()));
    }

    private boolean isEmpty(IMEInventory<IAEItemStack> meInventory) {
        return meInventory.getAvailableItems(AEApi.instance().storage().createItemList()).isEmpty();
    }

    public IAEItemStack injectItems(IAEItemStack input, Actionable mode, BaseActionSource src) {
        if (input == null) {
            return null;
        } else if (input.getStackSize() == 0L) {
            return null;
        } else if (!isBlackListed(input) && !this.cellType.isBlackListed(this.cellItem, input)) {
            ItemStack sharedItemStack = input.getItemStack();
            if (isStorageCell(sharedItemStack)) {
                IMEInventory<IAEItemStack> meInventory = getCell(sharedItemStack, (ISaveProvider)null);
                if (meInventory != null && !this.isEmpty(meInventory)) {
                    return input;
                }
            }

            IAEItemStack l = (IAEItemStack)this.getCellItems().findPrecise(input);
            if (l != null) {
                long remainingItemSlots = this.getRemainingItemCount();
                if (remainingItemSlots < 0L) {
                    return input;
                } else if (input.getStackSize() > remainingItemSlots) {
                    IAEItemStack r = input.copy();
                    r.setStackSize(r.getStackSize() - remainingItemSlots);
                    if (mode == Actionable.MODULATE) {
                        l.setStackSize(l.getStackSize() + remainingItemSlots);
                        this.updateItemCount(remainingItemSlots);
                        this.saveChanges();
                    }

                    return r;
                } else {
                    if (mode == Actionable.MODULATE) {
                        l.setStackSize(l.getStackSize() + input.getStackSize());
                        this.updateItemCount(input.getStackSize());
                        this.saveChanges();
                    }

                    return null;
                }
            } else {
                if (this.canHoldNewItem()) {
                    long remainingItemCount = this.getRemainingItemCount() - this.getBytesPerType() * 8L;
                    if (remainingItemCount > 0) {
                        if (input.getStackSize() > remainingItemCount) {
                            final IAEItemStack toReturn = input.copy();
                            toReturn.decStackSize(remainingItemCount);

                            if (mode == Actionable.MODULATE) {
                                final IAEItemStack toWrite = input.copy();
                                toWrite.setStackSize(remainingItemCount);

                                this.cellItems.add(toWrite);
                                this.updateItemCount(toWrite.getStackSize());
                                this.saveChanges();
                            }
                            return toReturn;
                        }

                        if (mode == Actionable.MODULATE) {
                            this.updateItemCount(input.getStackSize());
                            this.cellItems.add(input);
                            this.saveChanges();
                        }

                        return null;
                    }
                }

                return input;
            }
        } else {
            return input;
        }
    }

    public IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src) {
        if (request == null) {
            return null;
        } else {
            long size = Math.min(2147483647L, request.getStackSize());
            IAEItemStack results = null;
            IAEItemStack l = (IAEItemStack)this.getCellItems().findPrecise(request);
            if (l != null) {
                results = l.copy();
                if (l.getStackSize() <= size) {
                    results.setStackSize(l.getStackSize());
                    if (mode == Actionable.MODULATE) {
                        this.updateItemCount(-l.getStackSize());
                        l.setStackSize(0L);
                        this.saveChanges();
                    }
                } else {
                    results.setStackSize(size);
                    if (mode == Actionable.MODULATE) {
                        l.setStackSize(l.getStackSize() - size);
                        this.updateItemCount(-size);
                        this.saveChanges();
                    }
                }
            }

            return results;
        }
    }

    private IItemList<IAEItemStack> getCellItems() {
        if (this.cellItems == null) {
            this.loadCellItems();
        }

        return this.cellItems;
    }

    private void updateItemCount(long delta) {
        this.storedItemCount = ((long)this.storedItemCount + delta);
        this.tagCompound.setLong("ic", this.storedItemCount);
    }

    private void saveChanges() {
        long itemCount = 0;
        int x = 0;

        for(IAEItemStack v : this.cellItems) {
            itemCount = (long)itemCount + v.getStackSize();
            NBTBase c = this.tagCompound.getTag(itemSlots[x]);
            if (c instanceof NBTTagCompound) {
                v.writeToNBT((NBTTagCompound)c);
            } else {
                NBTTagCompound g = new NBTTagCompound();
                v.writeToNBT(g);
                this.tagCompound.setTag(itemSlots[x], g);
            }

            this.tagCompound.setLong(itemSlotCount[x], v.getStackSize());
            ++x;
        }

        short oldStoredItems = this.storedItems;
        this.storedItems = (short)this.cellItems.size();
        if (this.cellItems.isEmpty()) {
            this.tagCompound.removeTag("it");
        } else {
            this.tagCompound.setShort("it", this.storedItems);
        }

        this.storedItemCount = itemCount;
        if (itemCount == 0) {
            this.tagCompound.removeTag("ic");
        } else {
            this.tagCompound.setLong("ic", itemCount);
        }

        while(x < oldStoredItems && x < this.maxItemTypes) {
            this.tagCompound.removeTag(itemSlots[x]);
            this.tagCompound.removeTag(itemSlotCount[x]);
            ++x;
        }

        if (this.container != null) {
            this.container.saveChanges(this);
        }

    }

    private void loadCellItems() {
        if (this.cellItems == null) {
            this.cellItems = AEApi.instance().storage().createItemList();
        }

        this.cellItems.resetStatus();
        int types = (int)this.getStoredItemTypes();

        for(int x = 0; x < types; ++x) {
            AEItemStack t = AEItemStack.create(ItemStack.loadItemStackFromNBT(this.tagCompound.getCompoundTag(itemSlots[x])));
            if (t != null) {
                t.setStackSize(this.tagCompound.getLong(itemSlotCount[x]));
                if (t.getStackSize() > 0) {
                    this.cellItems.add(t);
                }
            }
        }

    }

    public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> out) {
        for(IAEItemStack i : this.getCellItems()) {
            out.add(i);
        }

        return out;
    }

    public StorageChannel getChannel() {
        return StorageChannel.ITEMS;
    }

    public ItemStack getItemStack() {
        return this.cellItem;
    }

    public double getIdleDrain() {
        return this.cellType.getIdleDrain();
    }

    public FuzzyMode getFuzzyMode() {
        return this.cellType.getFuzzyMode(this.cellItem);
    }

    public IInventory getConfigInventory() {
        return this.cellType.getConfigInventory(this.cellItem);
    }

    public IInventory getUpgradesInventory() {
        return this.cellType.getUpgradesInventory(this.cellItem);
    }

    public int getBytesPerType() {
        return this.cellType.getBytesPerType(this.cellItem);
    }

    public boolean canHoldNewItem() {
        long bytesFree = this.getFreeBytes();
        return (bytesFree > (long)this.getBytesPerType() || bytesFree == (long)this.getBytesPerType() && this.getUnusedItemCount() > 0) && this.getRemainingItemTypes() > 0L;
    }

    public long getTotalBytes() {
        return this.cellType.getBytes(this.cellItem);
    }

    public long getFreeBytes() {
        long a  = getTotalBytes();
        long b = getUsedBytes();
        return this.getTotalBytes() - this.getUsedBytes();
    }

    public long getUsedBytes() {
        long bytesForItemCount = (this.getStoredItemCount() + (long)this.getUnusedItemCount()) / 8L;
        return this.getStoredItemTypes() * (long)this.getBytesPerType() + bytesForItemCount;
    }

    public long getTotalItemTypes() {
        return (long)this.maxItemTypes;
    }

    public long getStoredItemCount() {
        return (long)this.storedItemCount;
    }

    public long getStoredItemTypes() {
        return (long)this.storedItems;
    }

    public long getRemainingItemTypes() {
        long basedOnStorage = this.getFreeBytes() / (long)this.getBytesPerType();
        long baseOnTotal = this.getTotalItemTypes() - this.getStoredItemTypes();
        return basedOnStorage > baseOnTotal ? baseOnTotal : basedOnStorage;
    }

    public long getRemainingItemCount() {
        long freeBytes = this.getFreeBytes();
        long unusedItemCount = this.getUnusedItemCount();
        if (freeBytes >= 1152921504606846975L) {
            freeBytes = Long.MAX_VALUE;
            unusedItemCount = 0;
        } else {
            freeBytes *= 8;
        }
        long remaining = freeBytes + unusedItemCount;
        return remaining > 0L ? remaining : 0L;
    }

    public int getUnusedItemCount() {
        int div = (int)(this.getStoredItemCount() % 8L);
        return div == 0 ? 0 : 8 - div;
    }

    public int getStatusForCell() {
        if (this.canHoldNewItem()) {
            return 1;
        } else {
            return this.getRemainingItemCount() > 0L ? 2 : 3;
        }
    }
}
