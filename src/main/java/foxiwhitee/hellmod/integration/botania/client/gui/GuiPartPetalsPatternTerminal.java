package foxiwhitee.hellmod.integration.botania.client.gui;


import appeng.api.storage.ITerminalHost;
import appeng.client.gui.AEBaseMEGui;
import appeng.container.slot.SlotFakeCraftingMatrix;
import foxiwhitee.hellmod.client.gui.terminals.GuiPatternTerminal;
import foxiwhitee.hellmod.integration.botania.container.ContainerPartPetalsPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.IThaumcraftRecipe;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiPartPetalsPatternTerminal extends GuiPatternTerminal {

    public GuiPartPetalsPatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, new ContainerPartPetalsPatternTerminal(inventoryPlayer, te), 511, 250);
        this.hasCycleItems = true;
        this.cycleItemsCordX = 403;
        this.cycleItemsCordY = 90;
    }

    @Override
    protected String getBackground() {
        return "gui/gui_terminal_botania_pattern_petals.png";
    }

}

