package foxiwhitee.hellmod.integration.botania.client.gui;


import appeng.api.storage.ITerminalHost;
import foxiwhitee.hellmod.client.gui.terminals.GuiPatternTerminal;
import foxiwhitee.hellmod.integration.botania.container.ContainerPartPureDaisyPatternTerminal;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiPartPureDaisyPatternTerminal extends GuiPatternTerminal {

    public GuiPartPureDaisyPatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, new ContainerPartPureDaisyPatternTerminal(inventoryPlayer, te), 511, 250);
    }

    @Override
    protected String getBackground() {
        return "gui/gui_terminal_botania_pattern_pure_daisy.png";
    }

}

