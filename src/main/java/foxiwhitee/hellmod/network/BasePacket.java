package foxiwhitee.hellmod.network;

import appeng.core.AEConfig;
import appeng.core.AELog;
import appeng.core.features.AEFeature;
import appeng.core.sync.AppEngPacketHandlerBase;
import appeng.core.sync.network.INetworkInfo;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BasePacket {
    private ByteBuf packetData;
    private final int packetId;

    protected BasePacket() {
        this.packetData = Unpooled.buffer();
        this.packetId = NetworkPackets.fromClass(this.getClass()).ordinal();
        this.packetData.writeInt(this.packetId);
    }

    protected BasePacket(ByteBuf buffer) {
        this.packetData = buffer;
        this.packetId = NetworkPackets.fromClass(this.getClass()).ordinal();
    }

    protected void serialize(ByteBuf buffer) {
    }

    public void handleServerSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        throw new UnsupportedOperationException("Server handler not implemented for packet ID: " + packetId);
    }

    public void handleClientSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        throw new UnsupportedOperationException("Client handler not implemented for packet ID: " + packetId);
    }

    public final int getId() {
        return packetId;
    }

    protected void setPacketData(ByteBuf buffer) {
        buffer.capacity(buffer.readableBytes());
        this.packetData = buffer;
    }

    public FMLProxyPacket createProxyPacket() {
        byte[] data = packetData.array();
        if (data.length > 2097152) {
            throw new IllegalArgumentException("Packet size too large: " + data.length + " bytes");
        }

        FMLProxyPacket proxy = new FMLProxyPacket(packetData, NetworkManager.instance.getChannelName());
        if (AEConfig.instance.isFeatureEnabled(AEFeature.PacketLogging)) {
            AELog.info("Packet " + this.getClass().getSimpleName() + ": " + proxy.payload().readableBytes() + " bytes");
        }
        return proxy;
    }

    protected ByteBuf getDataBuffer() {
        return packetData;
    }
}
