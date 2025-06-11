package foxiwhitee.hellmod.integration.nei.draconic;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import com.brandon3055.draconicevolution.client.gui.GUIUpgradeModifier;
import com.brandon3055.draconicevolution.client.gui.componentguis.GUIManual;
import com.brandon3055.draconicevolution.client.gui.componentguis.GUIToolConfig;
import foxiwhitee.hellmod.integration.draconic.client.gui.GuiCustomUpgradeModifier;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class DraconicNEIGuiHandler implements INEIGuiHandler {
    public DraconicNEIGuiHandler() {
    }

    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
        if (gui instanceof GuiCustomUpgradeModifier && ((GuiCustomUpgradeModifier)gui).inUse) {
            currentVisibility.showNEI = false;
        }

        return currentVisibility;
    }

    public Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
        return Collections.emptyList();
    }

    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
        return null;
    }

    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
        return false;
    }

    public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
        return false;
    }
}