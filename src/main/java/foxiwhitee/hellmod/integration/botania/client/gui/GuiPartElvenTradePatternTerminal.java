package foxiwhitee.hellmod.integration.botania.client.gui;


import appeng.api.storage.ITerminalHost;
import foxiwhitee.hellmod.client.gui.terminals.GuiPatternTerminal;
import foxiwhitee.hellmod.integration.botania.container.ContainerPartElvenTradePatternTerminal;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiPartElvenTradePatternTerminal extends GuiPatternTerminal {

    public GuiPartElvenTradePatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, new ContainerPartElvenTradePatternTerminal(inventoryPlayer, te), 511, 250);
    }

    @Override
    protected String getBackground() {
        return "gui/gui_terminal_botania_pattern_elven_trade.png";
    }

}

