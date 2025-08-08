package foxiwhitee.hellmod.integration.avaritia.client.gui;

import appeng.api.config.ActionItems;
import appeng.api.config.ItemSubstitution;
import appeng.api.config.Settings;
import appeng.api.storage.ITerminalHost;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.container.slot.AppEngSlot;
import appeng.core.localization.GuiText;
import appeng.core.sync.AppEngPacket;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketSwitchGuis;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.terminals.GuiPatternTerminal;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerPartBigProcessingPatternTerminal;
import foxiwhitee.hellmod.network.BasePacket;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiPartBigProcessingPatternTerminal extends GuiPatternTerminal {
    private static final ResourceLocation GUI = new ResourceLocation(HellCore.MODID, "textures/gui/gui_terminal_avaritia_pattern_big_3.png");


    public GuiPartBigProcessingPatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, new ContainerPartBigProcessingPatternTerminal(inventoryPlayer, te), 550, 250);
    }

    protected ResourceLocation getBackgroundLocation() {
        return GUI;
    }

    @Override
    protected String getBackground() {
        return "gui/gui_terminal_avaritia_pattern_big_3.png";
    }

}
