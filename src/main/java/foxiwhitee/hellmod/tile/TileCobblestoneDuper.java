package foxiwhitee.hellmod.tile;

import appeng.api.AEApi;
import appeng.api.config.*;
import appeng.api.implementations.tiles.IChestOrDrive;
import appeng.api.implementations.tiles.IColorableTile;
import appeng.api.implementations.tiles.IMEChest;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.events.*;
import appeng.api.networking.security.*;
import appeng.api.networking.storage.IBaseMonitor;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.IConfigManager;
import appeng.helpers.IPriorityHost;
import appeng.me.GridAccessException;
import appeng.me.storage.MEInventoryHandler;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkInvTile;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.ConfigManager;
import appeng.util.IConfigManagerHost;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.ModBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TileCobblestoneDuper extends AENetworkInvTile implements IMEChest, IPriorityHost, IConfigManagerHost, IColorableTile {
    private static final int[] SIDES = {0};
    private static final int[] FRONT = {1};
    private static final int[] NO_SLOTS = {};

    private final AppEngInternalInventory inventory = new AppEngInternalInventory(this, 2);
    private final MachineSource source = new MachineSource(this);
    private final IConfigManager settings = new ConfigManager(this);
    private ItemStack cellType;
    private long lastBlinkTime;
    private int status;
    private boolean isActive;
    private AEColor color = AEColor.Transparent;
    private boolean isHandlerCached;
    private ICellHandler cellHandler;
    private MEMonitorHandler<IAEItemStack> itemMonitor;
    private MEMonitorHandler<IAEFluidStack> fluidMonitor;
    private int priority;

    public TileCobblestoneDuper() {
        getProxy().setFlags(GridFlags.REQUIRE_CHANNEL);
        settings.registerSetting(Settings.SORT_BY, SortOrder.NAME);
        settings.registerSetting(Settings.VIEW_MODE, ViewItems.ALL);
        settings.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING);
    }

    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(ModBlocks.cobblestone_duper);
    }

    private void updateStatus() {
        int prevStatus = status;
        status = getCellStatus(0);
        status |= isPowered() ? 0x40 : 0;
        boolean currentActive = getProxy().isActive();
        if (isActive != currentActive) {
            isActive = currentActive;
            try {
                getProxy().getGrid().postEvent(new MENetworkCellArrayUpdate());
            } catch (GridAccessException ignored) {}
        }
        if (prevStatus != status) {
            markForUpdate();
        }
    }

    public int getCellCount() {
        return 1;
    }

    private IMEInventoryHandler<?> getInventoryHandler(StorageChannel channel) throws NoHandlerException {
        if (!isHandlerCached) {
            itemMonitor = null;
            fluidMonitor = null;
            ItemStack cell = inventory.getStackInSlot(1);
            if (cell != null) {
                isHandlerCached = true;
                cellHandler = AEApi.instance().registries().cell().getHandler(cell);
                if (cellHandler != null) {
                    double power = 1.0;
                    IMEInventoryHandler<IAEItemStack> itemHandler = cellHandler.getCellInventory(cell, this, StorageChannel.ITEMS);
                    IMEInventoryHandler<IAEFluidStack> fluidHandler = cellHandler.getCellInventory(cell, this, StorageChannel.FLUIDS);
                    if (itemHandler != null) {
                        AEItemStack cobble = AEItemStack.create(new ItemStack(Blocks.cobblestone));
                        cobble.setStackSize(Long.MAX_VALUE);
                        itemHandler.injectItems(cobble, Actionable.MODULATE, source);
                        power += cellHandler.cellIdleDrain(cell, itemHandler);
                    } else if (fluidHandler != null) {
                        power += cellHandler.cellIdleDrain(cell, fluidHandler);
                    }
                    getProxy().setIdlePowerUsage(power);
                    itemMonitor = createMonitor(itemHandler);
                    fluidMonitor = createMonitor(fluidHandler);
                }
            }
        }
        switch (channel) {
            case ITEMS: {
                if (itemMonitor != null) {
                    return itemMonitor;
                } else {
                    throw new NoHandlerException();
                }
            }
            case FLUIDS: {
                if (fluidMonitor != null) {
                    return fluidMonitor;
                } else {
                    throw new NoHandlerException();
                }
            }

        }
        return null;
    }

    private <T extends IAEStack<T>> MEMonitorHandler<T> createMonitor(IMEInventoryHandler<T> handler) {
        if (handler == null) return null;
        MEInventoryHandler<T> internal = new MEInventoryHandler<>(handler, handler.getChannel());
        internal.setPriority(priority);
        MEMonitorHandler<T> monitor = new StorageMonitor<>(internal);
        monitor.addListener(new StorageNotifier(handler.getChannel()), monitor);
        return monitor;
    }

    public int getCellStatus(int slot) {
        if (Platform.isClient()) {
            return status >> (slot * 3) & 0x3;
        }
        ItemStack cell = inventory.getStackInSlot(1);
        ICellHandler handler = AEApi.instance().registries().cell().getHandler(cell);
        if (handler != null) {
            try {
                IMEInventoryHandler<?> inv = getInventoryHandler(StorageChannel.ITEMS);
                if (inv instanceof StorageMonitor) {
                    return handler.getStatusForCell(cell, ((StorageMonitor<?>) inv).getInternal());
                }
            } catch (NoHandlerException ignored) {}
            try {
                IMEInventoryHandler<?> inv = getInventoryHandler(StorageChannel.FLUIDS);
                if (inv instanceof StorageMonitor) {
                    return handler.getStatusForCell(cell, ((StorageMonitor<?>) inv).getInternal());
                }
            } catch (NoHandlerException ignored) {}
        }
        return 0;
    }

    public boolean isPowered() {
        return Platform.isClient() ? (status & 0x40) == 0x40 : getProxy().isPowered();
    }

    public boolean isCellBlinking(int slot) {
        return worldObj.getTotalWorldTime() - lastBlinkTime <= 8 && (status >> (slot * 3 + 2) & 0x1) == 1;
    }

    @TileEvent(TileEventType.TICK)
    public void onTick() {
        if (worldObj.isRemote) return;
        double idlePower = getProxy().getIdlePowerUsage();
        try {
            if (!getProxy().getEnergy().isNetworkPowered()) {
                double used = extractAEPower(idlePower, Actionable.MODULATE, PowerMultiplier.CONFIG);
                if ((used + 0.1 >= idlePower) != ((status & 0x40) > 0)) {
                    updateStatus();
                }
            }
        } catch (GridAccessException e) {
            double used = extractAEPower(idlePower, Actionable.MODULATE, PowerMultiplier.CONFIG);
            if ((used + 0.1 >= idlePower) != ((status & 0x40) > 0)) {
                updateStatus();
            }
        }
        if (inventory.getStackInSlot(0) != null) {
            storeContents();
        }
    }

    @TileEvent(TileEventType.NETWORK_WRITE)
    public void writeToStream(ByteBuf data) {
        status = worldObj.getTotalWorldTime() - lastBlinkTime > 8 ? 0 : status & 0x24924924;
        status |= getCellStatus(0);
        status |= isPowered() ? 0x40 : 0;
        data.writeByte(status);
        data.writeByte(color.ordinal());
        ItemStack cell = inventory.getStackInSlot(1);
        data.writeInt(cell == null ? 0 : (cell.getItemDamage() << 16) | Item.getIdFromItem(cell.getItem()));
    }

    @TileEvent(TileEventType.NETWORK_READ)
    public boolean readFromStream(ByteBuf data) {
        int prevStatus = status;
        ItemStack prevCell = cellType;
        status = data.readByte();
        AEColor prevColor = color;
        color = AEColor.values()[data.readByte()];
        int item = data.readInt();
        cellType = item == 0 ? null : new ItemStack(Item.getItemById(item & 0xFFFF), 1, item >> 16);
        lastBlinkTime = worldObj.getTotalWorldTime();
        return prevColor != color || (status & 0xDB6DB6DB) != (prevStatus & 0xDB6DB6DB) || !Platform.isSameItemPrecise(prevCell, cellType);
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBTC(NBTTagCompound data) {
        settings.readFromNBT(data);
        priority = data.getInteger("priority");
        if (data.hasKey("paintedColor")) {
            color = AEColor.values()[data.getByte("paintedColor")];
        }
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBTC(NBTTagCompound data) {
        settings.writeToNBT(data);
        data.setInteger("priority", priority);
        data.setByte("paintedColor", (byte) color.ordinal());
    }

    @MENetworkEventSubscribe
    public void onPowerChange(MENetworkPowerStatusChange event) {
        updateStatus();
    }

    @MENetworkEventSubscribe
    public void onChannelChange(MENetworkChannelsChanged event) {
        updateStatus();
    }

    public IMEMonitor<IAEItemStack> getItemInventory() {
        return itemMonitor;
    }

    public IMEMonitor<IAEFluidStack> getFluidInventory() {
        return fluidMonitor;
    }

    public IInventory getInternalInventory() {
        return inventory;
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
        storeContents();
    }

    public void onChangeInventory(IInventory inv, int slot, InvOperation op, ItemStack removed, ItemStack added) {
        if (slot == 1) {
            itemMonitor = null;
            fluidMonitor = null;
            isHandlerCached = false;
            try {
                getProxy().getGrid().postEvent(new MENetworkCellArrayUpdate());
                IStorageGrid storage = getProxy().getStorage();
                Platform.postChanges(storage, removed, added, source);
            } catch (GridAccessException ignored) {}
            //Platform.notifyBlocksOfNeighbors(worldObj, xCoord, yCoord, zCoord);
            markForUpdate();
        }
    }

    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return false;
    }

    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 1;
    }

    public int[] getAccessibleSlotsBySide(ForgeDirection side) {
        if (side == ForgeDirection.SOUTH) return FRONT;
        if (isPowered()) {
            try {
                if (getInventoryHandler(StorageChannel.ITEMS) != null) return SIDES;
            } catch (NoHandlerException ignored) {}
        }
        return NO_SLOTS;
    }

    private void storeContents() {
        try {
            ItemStack stack = inventory.getStackInSlot(0);
            if (stack != null) {
                IMEInventoryHandler<IAEItemStack> handler = (IMEInventoryHandler<IAEItemStack>) getInventoryHandler(StorageChannel.ITEMS);
                IAEItemStack result = Platform.poweredInsert(this, handler, AEApi.instance().storage().createItemStack(stack), source);
                inventory.setInventorySlotContents(0, result == null ? null : result.getItemStack());
            }
        } catch (NoHandlerException ignored) {}
    }

    public List<IMEInventoryHandler> getCellArray(StorageChannel channel) {
        if (getProxy().isActive()) {
            try {
                return Collections.singletonList(getInventoryHandler(channel));
            } catch (NoHandlerException ignored) {}
        }
        return new ArrayList<>();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int value) {
        priority = value;
        itemMonitor = null;
        fluidMonitor = null;
        isHandlerCached = false;
        try {
            getProxy().getGrid().postEvent(new MENetworkCellArrayUpdate());
        } catch (GridAccessException ignored) {}
    }

    public void blinkCell(int slot) {
        long currentTime = worldObj.getTotalWorldTime();
        if (currentTime - lastBlinkTime > 8) {
            status = 0;
        }
        lastBlinkTime = currentTime;
        status |= 1 << (slot * 3 + 2);
        updateStatus();
    }

    public IStorageMonitorable getMonitorable(ForgeDirection side, BaseActionSource src) {
        return Platform.canAccess(getProxy(), src) && side != getForward() ? (IStorageMonitorable) this : null;
    }

    public ItemStack getStorageType() {
        return isPowered() ? cellType : null;
    }

    public IConfigManager getConfigManager() {
        return settings;
    }

    public void updateSetting(IConfigManager manager, Enum setting, Enum value) {}

    public boolean openGui(EntityPlayer player, ICellHandler handler, ItemStack cell, int side) {
        try {
            IMEInventoryHandler<?> inv = getInventoryHandler(StorageChannel.ITEMS);
            if (handler != null && inv != null) {
                handler.openChestGui(player, this, handler, inv, cell, StorageChannel.ITEMS);
                return true;
            }
        } catch (NoHandlerException ignored) {}
        try {
            IMEInventoryHandler<?> inv = getInventoryHandler(StorageChannel.FLUIDS);
            if (handler != null && inv != null) {
                handler.openChestGui(player, this, handler, inv, cell, StorageChannel.FLUIDS);
                return true;
            }
        } catch (NoHandlerException ignored) {}
        return false;
    }

    public AEColor getColor() {
        return color;
    }

    public boolean recolourBlock(ForgeDirection side, AEColor newColor, EntityPlayer player) {
        if (color == newColor) return false;
        color = newColor;
        markDirty();
        markForUpdate();
        return true;
    }

    public void saveChanges(IMEInventory inventory) {
        worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
    }

    public double extractAEPower(double amount, Actionable mode, PowerMultiplier multiplier) {
        return 200000.0;
    }

    public DimensionalCoord getLocation() {
        return new DimensionalCoord(worldObj, xCoord, yCoord, zCoord);
    }

    public AECableType getCableConnectionType(ForgeDirection dir) {
        return AECableType.SMART;
    }

    private static class NoHandlerException extends Exception {}

    private class StorageNotifier<T extends IAEStack<T>> implements IMEMonitorHandlerReceiver<T> {
        private final StorageChannel channel;

        StorageNotifier(StorageChannel channel) {
            this.channel = channel;
        }

        public boolean isValid(Object token) {
            return channel == StorageChannel.ITEMS ? token == itemMonitor : token == fluidMonitor;
        }

        public void postChange(IBaseMonitor<T> monitor, Iterable<T> changes, BaseActionSource src) {
            if (src == source || (src instanceof PlayerSource && ((PlayerSource) src).via == TileCobblestoneDuper.this)) {
                try {
                    if (getProxy().isActive()) {
                        getProxy().getStorage().postAlterationOfStoredItems(channel, changes, source);
                    }
                } catch (GridAccessException ignored) {}
            }
            blinkCell(0);
        }

        public void onListUpdate() {}
    }

    private class StorageMonitor<T extends IAEStack<T>> extends MEMonitorHandler<T> {
        StorageMonitor(IMEInventoryHandler<T> handler) {
            super(handler);
        }

        IMEInventoryHandler<T> getInternal() {
            IMEInventoryHandler<T> handler = getHandler();
            return handler instanceof MEInventoryHandler ? (IMEInventoryHandler<T>) ((MEInventoryHandler<T>) handler).getInternal() : handler;
        }

        public T extractItems(T request, Actionable mode, BaseActionSource src) {
            if (src.isPlayer() && !hasPermission(((PlayerSource) src).player, SecurityPermissions.EXTRACT)) {
                return null;
            }
            if (request instanceof IAEItemStack && ((IAEItemStack)request).getItem() == Item.getItemFromBlock(Blocks.cobblestone)) {
                return request;
            }
            return super.extractItems(request, mode, src);
        }

        private boolean hasPermission(EntityPlayer player, SecurityPermissions permission) {
            if (permission == null) return true;
            IGridNode node = getActionableNode();
            if (node == null) return false;
            IGrid grid = node.getGrid();
            if (grid == null) return false;
            ISecurityGrid security = grid.getCache(ISecurityGrid.class);
            return security.hasPermission(player, permission);
        }
    }
}