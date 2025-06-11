package foxiwhitee.hellmod.tile.wireless;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnection;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkTile;
import appeng.util.Platform;
import foxiwhitee.hellmod.utils.cables.ICustomChannelCount;
import foxiwhitee.hellmod.utils.wireless.ConnectionUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.Nullable;

public abstract class TileCustomWireless extends AENetworkTile implements IPowerChannelState, IGridTickable, ICustomChannelCount {
    public static final String NBT_KEY_LINK = "link";

    public ConnectionUtil connectionUtil;

    public IGridConnection connection;

    private boolean hasPower = false;

    private boolean disabled = false;

    public TileCustomWireless() {
        getProxy().setFlags(new GridFlags[] { GridFlags.DENSE_CAPACITY });
        getProxy().setIdlePowerUsage(4.0D);
    }

    public boolean isPowered() {
        if (Platform.isServer())
            try {
                return getProxy().getEnergy().isNetworkPowered();
            } catch (GridAccessException ex) {
                return false;
            }
        return this.hasPower;
    }

    public boolean isActive() {
        return isPowered();
    }

    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(1, 10, false, false);
    }
    
    public TickRateModulation tickingRequest(IGridNode iGridNode, int i) {
        if (this.worldObj != null && !this.worldObj.isRemote && this.connection == null && this.connectionUtil != null && getProxy().isReady() && !this.disabled)
            try {
                if (this.worldObj.blockExists(this.xCoord, this.yCoord, this.zCoord))
                    createConnection();
            } catch (Throwable t) {
                t.printStackTrace();
                System.err.format("Failed setting up wireless link %s <-> %s: %s", new Object[] { ConnectionUtil.getCoord((TileEntity)this), this.connectionUtil.toString(), t.getMessage() });
                breakConnection();
            }
        return TickRateModulation.IDLE;
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_Wireless(NBTTagCompound nbtTagCompound) {
        getProxy().readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey("link"))
            this.connectionUtil = ConnectionUtil.getTags(nbtTagCompound.getCompoundTag("link"));
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_Wireless(NBTTagCompound nbtTagCompound) {
        getProxy().writeToNBT(nbtTagCompound);
        if (this.connectionUtil != null)
            nbtTagCompound.setTag("link", (NBTBase)this.connectionUtil.writeToNBTWireless(new NBTTagCompound()));
    }

    @TileEvent(TileEventType.NETWORK_READ)
    public boolean readBuf_Wireless(ByteBuf byteBuf) {
        if (byteBuf.readBoolean())
            this.connectionUtil = new ConnectionUtil(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
        boolean powered = isPowered();
        setPower(byteBuf.readBoolean());
        return (isPowered() != powered);
    }

    @TileEvent(TileEventType.NETWORK_WRITE)
    public void writeBuf_Wireless(ByteBuf byteBuf) {
        byteBuf.writeBoolean(hasConnectionUtil());
        if (hasConnectionUtil()) {
            byteBuf.writeInt(this.connectionUtil.x);
            byteBuf.writeInt(this.connectionUtil.y);
            byteBuf.writeInt(this.connectionUtil.z);
        }
        try {
            byteBuf.writeBoolean(getProxy().getEnergy().isNetworkPowered());
        } catch (GridAccessException ex) {
            byteBuf.writeBoolean(false);
        }
    }

    public void setPower(boolean hasPower) {
        this.hasPower = hasPower;
    }

    public boolean hasConnectionUtil() {
        return (this.connectionUtil != null);
    }

    public TileCustomWireless getLink() {
        if (this.connectionUtil == null || this.worldObj == null)
            return null;
        TileEntity te = this.connectionUtil.getTile((IBlockAccess)this.worldObj);
        if (!(te instanceof TileCustomWireless))
            return null;
        return (TileCustomWireless)te;
    }

    public void breakConnection() {
        if (this.connection != null) {
            this.connection.destroy();
            this.connection = null;
        }
        getProxy().setIdlePowerUsage(0.0D);
        TileCustomWireless te = getLink();
        if (te != null) {
            te.connection = null;
            te.getProxy().setIdlePowerUsage(0.0D);
            this.worldObj.setBlockMetadataWithNotify(te.xCoord, te.yCoord, te.zCoord, 0, 3);
        }
        this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.disabled ? 2 : 0, 3);
    }

    public void createConnection() {
        try {
            TileCustomWireless tile = getLink();
            if (tile == null)
                return;
            try {
                this.connection = AEApi.instance().createGridConnection(getProxy().getNode(), tile.getProxy().getNode());
            } catch (FailedConnection e) {
                e.printStackTrace();
            }
            if (tile.isDisabled() || isDisabled() || !tile.hasConnectionUtil() || !hasConnectionUtil()) {
                breakConnection();
                doUnlink();
                return;
            }
            tile.connection = this.connection;
            int dx = tile.xCoord - this.xCoord;
            int dy = tile.yCoord - this.yCoord;
            int dz = tile.zCoord - this.zCoord;
            double dist = (dx * dx + dy * dy + dz * dz);
            double power = 10.0D + 1.0D * dist;
            getProxy().setIdlePowerUsage(power);
            tile.getProxy().setIdlePowerUsage(power);
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 3);
            this.worldObj.setBlockMetadataWithNotify(tile.xCoord, tile.yCoord, tile.zCoord, 1, 3);
        } catch (Exception exception) {}
    }

    public void doLink(ConnectionUtil other) {
        if (this.disabled)
            return;
        if (this.connectionUtil != null)
            return;
        this.connectionUtil = other;
        TileCustomWireless tile = getLink();
        if (tile == null) {
            this.connectionUtil = null;
            return;
        }
        tile.connectionUtil = ConnectionUtil.getCoord((TileEntity)this);
    }

    public void doUnlink() {
        if (this.connectionUtil == null)
            return;
        TileCustomWireless tile = getLink();
        this.connectionUtil = null;
        if (tile == null)
            return;
        tile.connectionUtil = null;
    }

    public boolean runConnection(TileCustomWireless tileWireless) throws FailedConnection {
        if (this.disabled || tileWireless.disabled)
            return false;
        if (tileWireless.connectionUtil == null && this.connectionUtil == null) {
            tileWireless.connectionUtil = ConnectionUtil.getCoord((TileEntity)this);
            this.connectionUtil = ConnectionUtil.getCoord((TileEntity)tileWireless);
            createConnection();
            return true;
        }
        return false;
    }

    public void setDisabled(boolean disabled) {
        if (this.disabled != disabled) {
            this.disabled = disabled;
            if (this.disabled) {
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 2, 3);
                breakConnection();
                doUnlink();
            } else {
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 0, 3);
            }
        }
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public abstract int getMaxChannelSize();

    protected abstract ItemStack getItemFromTile(Object obj);
}