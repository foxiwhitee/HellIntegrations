package foxiwhitee.hellmod.network.packets;

import appeng.api.config.FuzzyMode;
import appeng.api.config.Settings;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.client.gui.implementations.GuiCraftingCPU;
import appeng.container.AEBaseContainer;
import appeng.container.implementations.*;
import appeng.core.sync.network.INetworkInfo;
import appeng.helpers.IMouseWheelItem;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerPartBigPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.container.ContainerPartAlchemicalConstructionPatternTerminal;
import foxiwhitee.hellmod.network.BasePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.io.*;

public class DefaultPacket extends BasePacket {
    private final String packetName;
    private final String packetValue;

    public DefaultPacket(ByteBuf buffer) throws IOException {
        super(buffer);
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(
                buffer.array(), buffer.readerIndex(), buffer.readableBytes()));
        this.packetName = input.readUTF();
        this.packetValue = input.readUTF();
    }

    public DefaultPacket(String name, String value) throws IOException {
        super();
        this.packetName = name;
        this.packetValue = value;
        ByteBuf data = Unpooled.buffer();
        data.writeInt(getId());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(output);
        outputStream.writeUTF(name);
        outputStream.writeUTF(value);
        data.writeBytes(output.toByteArray());
        setPacketData(data);
    }

    @Override
    public void handleServerSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        Container container = player.openContainer;
        if (packetName.equals("Item") && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IMouseWheelItem) {
            ItemStack heldItem = player.getHeldItem();
            IMouseWheelItem wheelItem = (IMouseWheelItem) heldItem.getItem();
            wheelItem.onWheel(heldItem, packetValue.equals("WheelUp"));
        } else if (packetName.equals("TileSecurity.ToggleOption") && container instanceof ContainerSecurity) {
            ((ContainerSecurity) container).toggleSetting(packetValue, player);
        } else if (packetName.startsWith("PatternTerminal.") && container instanceof ContainerPatternTerminal) {
            ContainerPatternTerminal terminal = (ContainerPatternTerminal) container;
            switch (packetName) {
                case "PatternTerminal.Encode":
                    terminal.encode();
                    break;
                case "PatternTerminal.Clear":
                    terminal.clear();
                    break;
                case "PatternTerminal.Index":
                    if (container instanceof ContainerPartAlchemicalConstructionPatternTerminal) {
                        ContainerPartAlchemicalConstructionPatternTerminal cpt = (ContainerPartAlchemicalConstructionPatternTerminal) container;
                        cpt.receiveEventId(Byte.parseByte(this.packetValue));
                    }
                    break;
            }
        } else if (packetName.startsWith("BigPatternTerminal.") && container instanceof ContainerPartBigPatternTerminal) {
            ContainerPartBigPatternTerminal terminal = (ContainerPartBigPatternTerminal) container;
            switch (packetName) {
                case "BigPatternTerminal.CraftMode":
                    (terminal.getTerminal()).setCraftingRecipe(packetValue.equals("1"));
                    break;
                case "BigPatternTerminal.Encode":
                    terminal.encode();
                    break;
                case "BigPatternTerminal.Clear":
                    terminal.clear();
                    break;
                case "BigPatternTerminal.Substitute":
                    terminal.getTerminal().setSubstitution(packetValue.equals("1"));
                    break;
            }
        } else if (packetName.equals("NetworkTool") && packetValue.equals("Toggle") && container instanceof ContainerNetworkTool) {
            ((ContainerNetworkTool) container).toggleFacadeMode();
        } else if (container instanceof IConfigurableObject) {
            IConfigManager config = ((IConfigurableObject) container).getConfigManager();
            for (Settings setting : config.getSettings()) {
                if (setting.name().equals(packetName)) {
                    Enum<?> current = config.getSetting(setting);
                    try {
                        config.putSetting(setting, Enum.valueOf(current.getClass(), packetValue));
                    } catch (IllegalArgumentException ignored) {
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void handleClientSide(INetworkInfo network, BasePacket packet, EntityPlayer player) {
        Container container = player.openContainer;
        if (container instanceof ContainerPartBigPatternTerminal && packetName.equals("BigPatternTerminal.CraftMode")) {
            ((ContainerPartBigPatternTerminal)container).updateOrderOfOutputSlots();
        }
        if (packetName.equals("CustomName") && container instanceof AEBaseContainer) {
            ((AEBaseContainer) container).setCustomName(packetValue);
        } else if (packetName.startsWith("SyncDat.") && container instanceof AEBaseContainer) {
            ((AEBaseContainer) container).stringSync(Integer.parseInt(packetName.substring(8)), packetValue);
        } else if (packetName.equals("CraftingStatus") && packetValue.equals("Clear")) {
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (screen instanceof GuiCraftingCPU) {
                ((GuiCraftingCPU) screen).clearItems();
            }
        } else if (container instanceof IConfigurableObject) {
            IConfigManager config = ((IConfigurableObject) container).getConfigManager();
            for (Settings setting : config.getSettings()) {
                if (setting.name().equals(packetName)) {
                    Enum<?> current = config.getSetting(setting);
                    try {
                        config.putSetting(setting, Enum.valueOf(current.getClass(), packetValue));
                    } catch (IllegalArgumentException ignored) {
                    }
                    break;
                }
            }
        }
    }
}