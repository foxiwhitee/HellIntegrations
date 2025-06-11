package foxiwhitee.hellmod.network.packets;

import foxiwhitee.hellmod.integration.draconic.client.render.effect.SEffectHandler;
import foxiwhitee.hellmod.network.BasePacket;
import appeng.core.sync.network.INetworkInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PacketSpawnParticle extends BasePacket {
    private int particleID;

    private double xCoord;

    private double yCoord;

    private double zCoord;

    private double xSpeed;

    private double ySpeed;

    private double zSpeed;

    private double viewRange;

    private int[] args;

    public PacketSpawnParticle(ByteBuf buf) {
        this.particleID = buf.readInt();
        this.xCoord = buf.readDouble();
        this.yCoord = buf.readDouble();
        this.zCoord = buf.readDouble();
        this.xSpeed = buf.readDouble();
        this.ySpeed = buf.readDouble();
        this.zSpeed = buf.readDouble();
        this.viewRange = buf.readDouble();
        int argsL = buf.readByte();
        this.args = new int[argsL];
        for (int i = 0; i < argsL; i++)
            this.args[i] = buf.readInt();
    }

    public PacketSpawnParticle(int particleID, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, double viewRange, int... args) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(getId());
        buf.writeInt(particleID);
        buf.writeDouble(xCoord);
        buf.writeDouble(yCoord);
        buf.writeDouble(zCoord);
        buf.writeDouble(xSpeed);
        buf.writeDouble(ySpeed);
        buf.writeDouble(zSpeed);
        buf.writeDouble(viewRange);
        buf.writeByte(args.length);
        for (int i : args)
            buf.writeInt(i);
        setPacketData(buf);
    }
    public void handleClientSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        if (packet.createProxyPacket().getTarget().isClient()) {
            SEffectHandler.spawnFX(this.particleID, (World)(Minecraft.getMinecraft()).theWorld, this.xCoord, this.yCoord, this.zCoord, this.xSpeed, this.ySpeed, this.zSpeed, this.viewRange, this.args);
        }
    }
}
