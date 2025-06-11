package foxiwhitee.hellmod.integration.ic2.client.gui;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.container.ContainerQuantumMatterUnifier;
import foxiwhitee.hellmod.utils.helpers.EnergyUtility;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiQuantumMatterUnifier extends GuiContainer {
    private final ResourceLocation texture = new ResourceLocation(HellCore.MODID, "textures/gui/gui_matter_unifier.png");

    ContainerQuantumMatterUnifier container;

    private int progress;

    public GuiQuantumMatterUnifier(ContainerQuantumMatterUnifier container) {
        super((Container)container);
        this.progress = 0;
        this.container = container;
        this.xSize = 210;
        this.ySize = 214;
    }

    public void drawScreen(int x, int y, float p_73863_3_) {
        super.drawScreen(x, y, p_73863_3_);
    }

    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String tooltipStorage = "EU: " + EnergyUtility.getString(this.progress) + "/" + EnergyUtility.getString(this.container.getTile().getMaxStorage());
        int nmPos = (super.xSize - super.fontRendererObj.getStringWidth(tooltipStorage)) / 2;
        super.fontRendererObj.drawString(tooltipStorage, nmPos, 104, 16777215  );
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        this.mc.renderEngine.bindTexture(this.texture);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        UtilGui.drawTexture(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
        GL11.glDisable(3042);
    }

    private void drawEnergyBarHoverText(int x, int y) {
        String tooltipStorage = "EU: " + EnergyUtility.getString(this.progress) + "/" + EnergyUtility.getString(this.container.getTile().getMaxStorage());
        if (isInRect(37, 79, 100, 4, x, y)) {
            ArrayList<String> internal = new ArrayList<>();
            internal.add(tooltipStorage);
            drawHoveringText(internal, x + this.guiLeft, y + this.guiTop, this.fontRendererObj);
        }
    }

    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
        return (mouseX >= x && mouseX <= x + xSize && mouseY >= y && mouseY <= y + ySize);
    }

    public void receiveData(int id, int value) {
        if (id == 0)
            this.progress = value;
    }
}

