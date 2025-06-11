package foxiwhitee.hellmod.integration.avaritia.client.gui;

import appeng.client.gui.AEBaseGui;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.container.ContainerCustomMolecularAssembler;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerNeutronUnifier;
import foxiwhitee.hellmod.tile.assemblers.TileCustomMolecularAssembler;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class GuiNeutronUnifier extends AEBaseGui {
    public GuiNeutronUnifier(ContainerNeutronUnifier c) {
        super(c);
        this.xSize = 210;
        this.ySize = 199;
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {}

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        bindTexture(HellCore.MODID, "gui/gui_neutron_unifier.png");
        UtilGui.drawTexture(offsetX - 17, offsetY + 7, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
        if (drawUpgrades())
            UtilGui.drawTexture(offsetX + xSize - 15, offsetY + 7 , 211, 0, 35, 104, 35, 104);
    }

    protected boolean drawUpgrades() {
        return false;
    }
}