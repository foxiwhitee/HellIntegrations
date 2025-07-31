package foxiwhitee.hellmod.network;

import appeng.core.AELog;
import appeng.core.sync.network.INetworkInfo;
import appeng.core.sync.network.IPacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.InvocationTargetException;

@SideOnly(Side.CLIENT)
public class ClientPacketProcessor implements IPacketHandler {
    @Override
    public void onPacketData(INetworkInfo network, FMLProxyPacket packet, EntityPlayer player) {
        try {
            int packetId = packet.payload().readInt();
            BasePacket basePacket = NetworkPackets.fromId(packetId).createInstance(packet.payload());
            EntityPlayer clientPlayer = Minecraft.getMinecraft().thePlayer;
            basePacket.handleClientSide(network, basePacket, clientPlayer);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | RuntimeException e) {
            AELog.debug("Client packet processing failed: " + e.getMessage());
        }
    }
}
