package foxiwhitee.hellmod.integration.botania.client.gui;


import appeng.api.storage.ITerminalHost;
import appeng.client.texture.ExtraBlockTextures;
import foxiwhitee.hellmod.client.gui.terminals.GuiPatternTerminal;
import foxiwhitee.hellmod.api.config.Buttons;
import foxiwhitee.hellmod.client.gui.widgets.CustomAEGuiButton;
import foxiwhitee.hellmod.api.config.CustomAESetings;
import foxiwhitee.hellmod.integration.botania.container.ContainerPartManaPoolPatternTerminal;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiPartManaPoolPatternTerminal extends GuiPatternTerminal {
    public GuiPartManaPoolPatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, new ContainerPartManaPoolPatternTerminal(inventoryPlayer, te), 511, 250);
    }

    @Override
    protected String getBackground() {
        return "gui/gui_terminal_botania_pattern_mana_pool.png";
    }

}

