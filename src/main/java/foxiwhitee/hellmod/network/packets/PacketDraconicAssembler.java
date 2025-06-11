package foxiwhitee.hellmod.network.packets;

import appeng.core.sync.network.INetworkInfo;
import foxiwhitee.hellmod.client.gui.terminals.GuiAdvancedInterfaceTerminal;
import foxiwhitee.hellmod.integration.draconic.tile.TileDraconicAssembler;
import foxiwhitee.hellmod.network.BasePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PacketDraconicAssembler extends BasePacket {
    private String packetName;
    private int xCoord = 0, yCoord = 0, zCoord = 0;

    public PacketDraconicAssembler(final ByteBuf data) throws IOException {
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(
                data.array(), data.readerIndex(), data.readableBytes()));
        this.packetName = input.readUTF();
        this.xCoord = Integer.parseInt(input.readUTF());
        this.yCoord = Integer.parseInt(input.readUTF());
        this.zCoord = Integer.parseInt(input.readUTF());
    }

    public PacketDraconicAssembler(TileEntity tile, String name) throws IOException {
        this.packetName = name;
        this.xCoord = tile.xCoord;
        this.yCoord = tile.yCoord;
        this.zCoord = tile.zCoord;
        ByteBuf data = Unpooled.buffer();
        data.writeInt(getId());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(output);
        outputStream.writeUTF(name);
        outputStream.writeUTF(String.valueOf(this.xCoord));
        outputStream.writeUTF(String.valueOf(this.yCoord));
        outputStream.writeUTF(String.valueOf(this.zCoord));
        data.writeBytes(output.toByteArray());
        setPacketData(data);
    }

    @Override
    public void handleClientSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        TileEntity tile = player.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord);
        if (tile instanceof TileDraconicAssembler) {
            if (packetName.equals("ClearCraft")) {
                ((TileDraconicAssembler) tile).clearCraft();
            } else if (packetName.equals("UpdateUpgradesInventory")) {
                ((TileDraconicAssembler)tile).updateUpgradeInventory();
            }
        }
    }
}