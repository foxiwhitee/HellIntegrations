package foxiwhitee.hellmod.network.packets;

import appeng.core.sync.network.INetworkInfo;
import foxiwhitee.hellmod.client.gui.terminals.GuiAdvancedInterfaceTerminal;
import foxiwhitee.hellmod.network.BasePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PacketAdvancedInterfaceTerminal extends BasePacket {
    private final NBTTagCompound in;
    private final ByteBuf data;
    private final GZIPOutputStream compressFrame;

    public PacketAdvancedInterfaceTerminal(final ByteBuf stream) throws IOException {
        super();
        this.data = null;
        this.compressFrame = null;
        GZIPInputStream gzReader = new GZIPInputStream(new InputStream() {
            public int read() throws IOException {
                return stream.readableBytes() <= 0 ? -1 : stream.readByte() & 255;
            }
        });
        DataInputStream inStream = new DataInputStream(gzReader);
        this.in = CompressedStreamTools.read(inStream);
        inStream.close();
    }

    public PacketAdvancedInterfaceTerminal(NBTTagCompound din) throws IOException {
        this.data = Unpooled.buffer(2048);
        this.data.writeInt(this.getId());
        this.in = din;
        this.compressFrame = new GZIPOutputStream(new OutputStream() {
            public void write(int value) throws IOException {
                PacketAdvancedInterfaceTerminal.this.data.writeByte(value);
            }
        });
        CompressedStreamTools.write(din, new DataOutputStream(this.compressFrame));
        this.compressFrame.close();
        setPacketData(data);
    }

    @Override
    public void handleClientSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        GuiScreen gs = Minecraft.getMinecraft().currentScreen;
        if (gs instanceof GuiAdvancedInterfaceTerminal) {
            ((GuiAdvancedInterfaceTerminal)gs).postUpdate(this.in);
        }

    }
}