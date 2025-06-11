package foxiwhitee.hellmod.integration.botania.tile.ae;


import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkInvTile;
import appeng.tile.inventory.AppEngInternalInventory;
import java.util.HashMap;

import appeng.util.Platform;
import appeng.util.item.AEFluidStack;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.helpers.IManaUpdateTile;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketUpdateMana;
import foxiwhitee.hellmod.utils.wireless.ConnectionUtil;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public abstract class TIleAEMana extends AENetworkInvTile implements IGridTickable, IManaUpdateTile {
    private final MachineSource source = new MachineSource((IActionHost)this);
    private final HashMap<IAEItemStack, IAEItemStack> itemQueue = new HashMap<>();
    private long maxStoredMana = 10000000L;
    private int ticksToInsert = 100;
    private long storedMana;

    public long getStoredMana() {
        return storedMana;
    }

    public void setStoredMana(long storedMana) {
        this.storedMana = storedMana;
    }

    public long getMaxStoredMana() {
        return maxStoredMana;
    }

    public void setMaxStoredMana(long maxStoredMana) {
        this.maxStoredMana = maxStoredMana;
    }

    public final MachineSource getSource() {
        return this.source;
    }

    public final AppEngInternalInventory getInternalInventory() {
        return getInventory();
    }

    public int[] getAccessibleSlotsBySide(ForgeDirection p0) {
        return new int[]{0};
    }

    public AECableType getCableConnectionType(ForgeDirection p0) {
        return AECableType.SMART;
    }

    public abstract DimensionalCoord getLocation();

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_AEMana(NBTTagCompound data) {
        data.setLong("mana", getStoredMana());
        data.setLong("maxStoredMana", getMaxStoredMana());
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_AEMana(NBTTagCompound data) {
        setStoredMana(data.getLong("mana"));
        setMaxStoredMana(data.getLong("maxStoredMana"));
    }

    @TileEvent(TileEventType.NETWORK_READ)
    public boolean readBuf_AEMana(ByteBuf byteBuf) {
        long old_mana = this.storedMana;
        long old_maxMana = this.maxStoredMana;
        this.storedMana = byteBuf.readLong();
        this.maxStoredMana = byteBuf.readLong();
        return old_mana != storedMana && old_maxMana != maxStoredMana;
    }

    @TileEvent(TileEventType.NETWORK_WRITE)
    public void writeBuf_AEMana(ByteBuf byteBuf) {
        byteBuf.writeLong(this.storedMana);
        byteBuf.writeLong(this.maxStoredMana);
    }

    public final long extractMana(long amount) {
        if (Platform.isServer()) {
            try {
                AEItemStack mana = AEItemStack.create(new ItemStack(BotaniaIntegration.mana_drop));
                mana.setStackSize(amount);
                IAEItemStack remainder = getProxy().getStorage().getItemInventory().extractItems(mana.copy(), Actionable.SIMULATE, source);
                if (remainder != null && remainder.getStackSize() > 0) {
                    IAEItemStack items = getProxy().getStorage().getItemInventory().extractItems(remainder.copy(), Actionable.MODULATE, source);
                    setStoredMana(Math.min(getMaxStoredMana(), getStoredMana() + items.getStackSize()));
                    return Math.min(getMaxStoredMana(), getStoredMana() + items.getStackSize());
                }
            } catch (GridAccessException e) {}
        }
        return 0;
    }

    public final void injectMana(long amount) {
        if (Platform.isServer()) {
            try {
                AEItemStack mana = AEItemStack.create(new ItemStack(BotaniaIntegration.mana_drop));
                mana.setStackSize(amount);
                IAEItemStack remainder = getProxy().getStorage().getItemInventory().injectItems(mana.copy(), Actionable.SIMULATE, source);
                if (remainder != null && remainder.getStackSize() > 0 && remainder.getStackSize() != Integer.MAX_VALUE) {
                    IAEItemStack remainderNew = getProxy().getStorage().getItemInventory().injectItems(remainder.copy(), Actionable.MODULATE, source);
                    if (remainder.getStackSize() != remainderNew.getStackSize()) {
                        setStoredMana(getStoredMana() - remainder.getStackSize());
                    }
                }
            } catch (GridAccessException e) {}
        }
    }

    public final TickRateModulation tickingRequest(IGridNode node, int ticks) {
        return tick(node, ticks);
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return getInternalInventory().isItemValidForSlot(i, itemstack);
    }

    public double gaugeManaScaled(int i) {
        double a = this.getStoredMana() * i;
        return a / this.getMaxStoredMana();
    }

    public int getInventoryStackLimit() {
        return getInventory().getInventoryStackLimit();
    }

    protected abstract AppEngInternalInventory getInventory();

    public abstract TickRateModulation tick(IGridNode node, int ticks);
}