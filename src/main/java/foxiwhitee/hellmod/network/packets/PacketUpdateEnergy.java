package foxiwhitee.hellmod.network.packets;

import appeng.api.util.DimensionalCoord;
import appeng.core.sync.network.INetworkInfo;
import foxiwhitee.hellmod.integration.ic2.helpers.IEnergyUpdateTile;
import foxiwhitee.hellmod.network.BasePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketUpdateEnergy extends BasePacket {
    int x;
    int y;
    int z;
    double energyStored;
    double maxEnergyStored;
    double energyPerItem;
    int ticksPerOp;

    public PacketUpdateEnergy(ByteBuf byteBuf) {
        super(byteBuf);
        x = byteBuf.readInt();
        y = byteBuf.readInt();
        z = byteBuf.readInt();
        energyStored = byteBuf.readDouble();
        maxEnergyStored = byteBuf.readDouble();
        energyPerItem = byteBuf.readDouble();
        ticksPerOp = byteBuf.readInt();
    }

    public PacketUpdateEnergy(IEnergyUpdateTile tile) {
        DimensionalCoord coord = tile.getCoord();
        ByteBuf byteBuf = Unpooled.buffer();
        x = coord.x;
        y = coord.y;
        z = coord.z;
        energyStored = tile.getEnergyStoredForUpdate();
        maxEnergyStored = tile.getMaxEnergyStoredForUpdate();
        energyPerItem = tile.getEnergyPerItemForUpdate();
        ticksPerOp = tile.getTicksPerOpForUpdate();
        byteBuf.writeInt(getId());
        byteBuf.writeInt(coord.x);
        byteBuf.writeInt(coord.y);
        byteBuf.writeInt(coord.z);
        byteBuf.writeDouble(tile.getEnergyStoredForUpdate());
        byteBuf.writeDouble(tile.getMaxEnergyStoredForUpdate());
        byteBuf.writeDouble(tile.getEnergyPerItemForUpdate());
        byteBuf.writeInt(tile.getTicksPerOpForUpdate());
        setPacketData(byteBuf);
    }

    @Override
    public void handleClientSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        TileEntity tileEntity = player != null && player.worldObj != null ? player.worldObj.getTileEntity(x, y, z) : null;
        if (!(tileEntity instanceof IEnergyUpdateTile)) return;
        IEnergyUpdateTile energyTile = (IEnergyUpdateTile) tileEntity;
        energyTile.setEnergyStoredForUpdate(energyStored);
        energyTile.setMaxEnergyStoredForUpdate(maxEnergyStored);
        energyTile.setEnergyPerItemUpdate(energyPerItem);
        energyTile.setTicksPerOpForUpdate(ticksPerOp);
    }

    @Override
    public void handleServerSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {

    }
}