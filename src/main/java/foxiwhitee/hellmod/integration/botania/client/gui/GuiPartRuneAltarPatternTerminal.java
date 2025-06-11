package foxiwhitee.hellmod.integration.botania.client.gui;


import appeng.api.storage.ITerminalHost;
import appeng.container.slot.SlotFakeCraftingMatrix;
import foxiwhitee.hellmod.client.gui.terminals.GuiPatternTerminal;
import foxiwhitee.hellmod.integration.botania.container.ContainerPartRuneAltarPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.IThaumcraftRecipe;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiPartRuneAltarPatternTerminal extends GuiPatternTerminal {

    public GuiPartRuneAltarPatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, new ContainerPartRuneAltarPatternTerminal(inventoryPlayer, te), 511, 250);
        this.hasCycleItems = true;
        this.cycleItemsCordX = 403;
        this.cycleItemsCordY = 90;
    }

    @Override
    protected String getBackground() {
        return "gui/gui_terminal_botania_pattern_rune_altar.png";
    }

}

