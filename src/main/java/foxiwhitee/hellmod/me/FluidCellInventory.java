package foxiwhitee.hellmod.me;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.FuzzyMode;
import appeng.api.exceptions.AppEngException;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.PlayerSource;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.core.AELog;
import appeng.tile.inventory.AppEngNullInventory;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import extracells.item.ItemFluid;
import foxiwhitee.hellmod.helpers.IFluidStorageCell;
import foxiwhitee.hellmod.items.ItemFluidDrop;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FluidCellInventory implements ICellInventory {
    private static final Set<Fluid> blackList = new TreeSet<>();
    private AEItemStack cellItem;
    private final ISaveProvider container;
    private final NBTTagCompound tagCompound;
    private final IFluidStorageCell cellType;
    private int maxItemTypes = 1024;
    private int storedItems = 0;
    private long storedItemCount = 0;
    private static String[] itemSlots;
    private static String[] itemSlotCount;
    private List<IAEItemStack> inv = new ArrayList<>();

    public static void addBlackList(Fluid fluid) {
        blackList.add(fluid);
    }

    public static void removeBlackList(Fluid fluid) {
        blackList.remove(fluid);
    }

    public static void clearBlackList() {
        blackList.clear();
    }

    public static boolean contains(Fluid fluid) {
        for (Fluid f : blackList) {
            if (f.equals(fluid)) return true;
        }
        return false;
    }

    private FluidCellInventory(ItemStack cellItem, ISaveProvider container) throws AppEngException {
        if (itemSlots == null) {
            itemSlots = new String[this.maxItemTypes];
            itemSlotCount = new String[this.maxItemTypes];

            for(int x = 0; x < this.maxItemTypes; ++x) {
                itemSlots[x] = "#" + x;
                itemSlotCount[x] = "@" + x;
            }
        }

        this.cellItem = AEItemStack.create(cellItem);
        this.container = container;
        if (cellItem == null || !(cellItem.getItem() instanceof IFluidStorageCell)) {
            throw new AppEngException("ItemStack was used as a cell, but was not a cell!");
        }
        this.cellType = (IFluidStorageCell) cellItem.getItem();
        this.maxItemTypes = (cellType.getTypes(cellItem) > 0 && cellType.getTypes(cellItem) < 1024) ? cellType.getTypes(cellItem) : (cellType.getTypes(cellItem) <= 0 ? 1 : 1024);
        this.tagCompound = Platform.openNbtData(cellItem);
        this.storedItems = this.tagCompound.getShort("it");
        this.storedItemCount = this.tagCompound.getLong("ic");
        this.inv = null;
    }

    private void updateItemCount(long delta) {
        this.storedItemCount = ((long)this.storedItemCount + delta);
        this.tagCompound.setLong("ic", this.storedItemCount);
    }

    public IAEItemStack findPrecise(IAEItemStack input) {
        for (IAEItemStack stack : getInv()) {
            if (simpleAreAEStacksEqual(stack, input)) return stack;
        }
        return null;
    }

    public boolean simpleAreAEStacksEqual(IAEItemStack stack, IAEItemStack stack2) {
        return stack.getTagCompound() != null && stack2.getTagCompound() != null ? stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage() && stack.getTagCompound().equals(stack2.getTagCompound()) : stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }

    public IAEItemStack injectItems(IAEItemStack input, Actionable mode, BaseActionSource source) {
        if (input == null || input.getStackSize() == 0L) {
            return null;
        } else if (input.getItem() != ItemFluidDrop.DROP) {
            return input;
        } else if (contains(ItemFluidDrop.getFluidStack(input.getItemStack()).getFluid())) {
            return null;
        } else {
            ItemStack sharedItemStack = input.getItemStack();
            if (isCell(sharedItemStack)) {
                IMEInventory<IAEItemStack> meInventory = getCell(sharedItemStack, (ISaveProvider)null);
                if (meInventory != null && !this.isEmpty(meInventory)) {
                    return input;
                }
            }

            IAEItemStack l = findPrecise(input);
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
                } else if (input.getStackSize() > 0 && input.getStackSize() <= remainingItemSlots) {
                    final IAEItemStack toReturn = input.copy();
                    toReturn.setStackSize(input.getStackSize());
                    if (mode == Actionable.MODULATE) {
                        l.setStackSize(l.getStackSize() + input.getStackSize());
                        this.updateItemCount(l.getStackSize());
                        this.saveChanges();

                    }
                    return mode == Actionable.MODULATE || (source instanceof PlayerSource && ((PlayerSource)source).isPlayer()) ? null : toReturn;
                } else {
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

                                this.getInv().add(toWrite);
                                this.updateItemCount(toWrite.getStackSize());
                                this.saveChanges();
                            }
                            return toReturn;
                        } else if (input.getStackSize() > 0 && input.getStackSize() <= remainingItemCount) {
                            final IAEItemStack toReturn = input.copy();
                            toReturn.setStackSize(input.getStackSize());
                            if (mode == Actionable.MODULATE) {
                                final IAEItemStack toWrite = input.copy();
                                toWrite.setStackSize(input.getStackSize());

                                this.getInv().add(toWrite);
                                this.updateItemCount(toWrite.getStackSize());
                                this.saveChanges();
                            }
                            return toReturn;
                        }

                        return null;
                    }
                }

                return input;
            }
        }
    }

    private void saveChanges() {
        long itemCount = 0;
        int x = 0;

        for(IAEItemStack v : this.getInv()) {
            if (x < itemSlots.length) {
                itemCount = (long) itemCount + v.getStackSize();
                NBTBase c = this.tagCompound.getTag(itemSlots[x]);
                if (c instanceof NBTTagCompound) {
                    v.writeToNBT((NBTTagCompound) c);
                } else {
                    NBTTagCompound g = new NBTTagCompound();
                    v.writeToNBT(g);
                    this.tagCompound.setTag(itemSlots[x], g);
                }

                this.tagCompound.setLong(itemSlotCount[x], v.getStackSize());
                ++x;
            }
        }

        int oldStoredItems = this.storedItems;
        this.storedItems = this.getInv().size();
        if (this.getInv().isEmpty()) {
            this.tagCompound.removeTag("it");
        } else {
            this.tagCompound.setInteger("it", this.storedItems);
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

    public IAEItemStack extractItems(IAEItemStack input, Actionable mode, BaseActionSource source) {
        if (input == null || input.getItem() != ItemFluidDrop.DROP || (source instanceof PlayerSource && ((PlayerSource)source).isPlayer() && !(((PlayerSource)source).player.capabilities.isCreativeMode))) {
            return null;
        } else if (contains(ItemFluidDrop.getFluidStack(input.getItemStack()).getFluid())) {
            return null;
        } else {
            long size = Math.min(2147483647L, input.getStackSize());
            IAEItemStack results = null;
            if (getInv() == null) loadCellItems();
            IAEItemStack l = findPrecise(input);
            boolean isPlayer = source instanceof PlayerSource && ((PlayerSource)source).isPlayer();
            if (l != null) {
                results = l.copy();
                if (l.getStackSize() <= size) {
                    results.setStackSize(l.getStackSize());
                    if (mode == Actionable.MODULATE) {
                        inv.remove(l);
                        this.updateItemCount(0L);
                        this.saveChanges();
                        results = isPlayer ? results : null;
                    }
                } else {
                    results.setStackSize(size);
                    if (mode == Actionable.MODULATE) {
                        l.setStackSize(l.getStackSize() - size);
                        this.updateItemCount(-size);
                        this.saveChanges();
                        results = isPlayer ? results : null;
                    }
                }
            }
            return results;
        }
    }

    private void loadCellItems() {
        if (this.inv == null) {
            this.inv = new ArrayList<>();
        }

        int types = (int)this.getStoredItemTypes();
        types = Math.min(types, 1024);
        for(int x = 0; x < types; ++x) {
            AEItemStack t = AEItemStack.create(ItemStack.loadItemStackFromNBT(this.tagCompound.getCompoundTag(itemSlots[x])));
            if (t != null) {
                t.setStackSize(this.tagCompound.getLong(itemSlotCount[x]));
                if (t.getStackSize() > 0) {
                    this.inv.add(t);
                }
            }
        }

    }

    private List<IAEItemStack> getInv() {
        if (this.inv == null) {
            this.loadCellItems();
        }

        return this.inv;
    }

    private boolean isEmpty(IMEInventory<IAEItemStack> meInventory) {
        return meInventory.getAvailableItems(AEApi.instance().storage().createItemList()).isEmpty();
    }

    public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> list) {
        for(IAEItemStack i : this.getInv()) {
            list.add(i);
        }

        return list;
    }

    public static IMEInventoryHandler<IAEItemStack> getCell(ItemStack o, ISaveProvider container) {
        try {
            return new FluidCellInventoryHandler(new FluidCellInventory(o, container));
        } catch (AppEngException ex) {
            return null;
        }
    }

    public static boolean isCell(ItemStack it) {
        if (it == null) return false;
        Item item = it.getItem();
        return item instanceof IFluidStorageCell && ((IFluidStorageCell) item).isFluidCell(it);
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
        return 8;
    }

    public boolean canHoldNewItem() {
        long bytesFree = this.getFreeBytes();
        return (bytesFree > (long)this.getBytesPerType() || bytesFree == (long)this.getBytesPerType() && this.getUnusedItemCount() > 0) && this.getRemainingItemTypes() > 0L;
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
        return maxItemTypes;
    }

    public long getStoredItemCount() {
        return this.storedItemCount;
    }

    public long getStoredItemTypes() {
        return storedItems;
    }

    public long getRemainingItemTypes() {
        long basedOnStorage = this.getFreeBytes() / (long)this.getBytesPerType();
        long baseOnTotal = this.getTotalItemTypes() - this.getStoredItemTypes();
        return Math.min(basedOnStorage, baseOnTotal);
    }

    public long getRemainingItemCount() {
        long l = getFreeBytes() * 8L + getUnusedItemCount();
        return getFreeBytes() * 8L + getUnusedItemCount();
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
