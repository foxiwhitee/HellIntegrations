package foxiwhitee.hellmod.network.packets;

import appeng.core.sync.network.INetworkInfo;
import foxiwhitee.hellmod.integration.draconic.container.ContainerFusionCraftingCore;
import foxiwhitee.hellmod.network.BasePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;

public class PacketFusionCore extends BasePacket {
    private int type = 0;
    private boolean craft = false;

    public PacketFusionCore(ByteBuf data) {
        this.type = data.readInt();
        this.craft = data.readBoolean();
    }

    public PacketFusionCore(int type) {
        this.type = type;
        ByteBuf data = Unpooled.buffer();
        data.writeInt(getId());
        data.writeInt(this.type);
        data.writeBoolean(false);
        setPacketData(data);
    }

    public PacketFusionCore(boolean craft) {
        this.craft = craft;
        ByteBuf data = Unpooled.buffer();
        data.writeInt(getId());
        data.writeInt(0);
        data.writeBoolean(this.craft);
        setPacketData(data);
    }

    @Override
    public void handleServerSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        if (this.type == 0 && player.openContainer instanceof ContainerFusionCraftingCore) {
            ContainerFusionCraftingCore c = (ContainerFusionCraftingCore)player.openContainer;
            c.getTile().attemptStartCrafting();
        }
    }

    @Override
    public void handleClientSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        if (player.openContainer instanceof ContainerFusionCraftingCore) {
            ContainerFusionCraftingCore c = (ContainerFusionCraftingCore)player.openContainer;
            c.getTile().setCrafting(craft);
        }
    }
}
