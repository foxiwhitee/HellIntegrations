package foxiwhitee.hellmod.network.packets;

import appeng.api.util.DimensionalCoord;
import appeng.core.sync.network.INetworkInfo;
import foxiwhitee.hellmod.integration.botania.helpers.IManaUpdateTile;
import foxiwhitee.hellmod.network.BasePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketUpdateMana extends BasePacket {
    int x;
    int y;
    int z;
    long manaStored;
    long maxManaStored;

    public PacketUpdateMana(ByteBuf byteBuf) {
        super(byteBuf);
        x = byteBuf.readInt();
        y = byteBuf.readInt();
        z = byteBuf.readInt();
        manaStored = byteBuf.readLong();
        maxManaStored = byteBuf.readLong();
    }

    public PacketUpdateMana(IManaUpdateTile tile) {
        DimensionalCoord coord = tile.getLocation();
        ByteBuf byteBuf = Unpooled.buffer();
        x = coord.x;
        y = coord.y;
        z = coord.z;
        manaStored = tile.getMaxStoredMana();
        maxManaStored = tile.getMaxStoredMana();
        byteBuf.writeInt(getId());
        byteBuf.writeInt(coord.x);
        byteBuf.writeInt(coord.y);
        byteBuf.writeInt(coord.z);
        byteBuf.writeLong(tile.getMaxStoredMana());
        byteBuf.writeLong(tile.getMaxStoredMana());
        setPacketData(byteBuf);
    }

    @Override
    public void handleClientSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        TileEntity tileEntity = player != null && player.worldObj != null ? player.worldObj.getTileEntity(x, y, z) : null;
        if (!(tileEntity instanceof IManaUpdateTile)) return;
        IManaUpdateTile manaTile = (IManaUpdateTile) tileEntity;
        manaTile.setStoredMana(manaStored);
        manaTile.setMaxStoredMana(maxManaStored);
    }

    @Override
    public void handleServerSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {

    }
}