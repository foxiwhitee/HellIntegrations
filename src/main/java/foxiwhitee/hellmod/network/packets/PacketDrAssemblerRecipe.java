package foxiwhitee.hellmod.network.packets;

import appeng.core.sync.network.INetworkInfo;
import cpw.mods.fml.common.network.ByteBufUtils;
import foxiwhitee.hellmod.integration.draconic.container.ContainerDraconicAssembler;
import foxiwhitee.hellmod.network.BasePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketDrAssemblerRecipe extends BasePacket {
    public PacketDrAssemblerRecipe(ByteBuf byteBuf) {
        super(byteBuf);
    }

    public PacketDrAssemblerRecipe(NBTTagCompound compound) {
        ByteBufUtils.writeTag(getDataBuffer(), compound);
    }

    @Override
    public void handleServerSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        NBTTagCompound compound = ByteBufUtils.readTag(getDataBuffer());
        if (player.openContainer instanceof ContainerDraconicAssembler) {
            ((ContainerDraconicAssembler)player.openContainer).getTile().readNEINBT(compound, player);
        }
    }

}
