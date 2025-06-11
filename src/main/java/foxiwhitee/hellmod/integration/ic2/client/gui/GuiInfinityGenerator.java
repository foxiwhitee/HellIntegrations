package foxiwhitee.hellmod.integration.ic2.client.gui;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.container.ContainerInfinityGenerator;
import foxiwhitee.hellmod.integration.ic2.tile.generators.infinity.TileInfinityGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiInfinityGenerator extends GuiContainer {
    public TileInfinityGenerator tileentity;
    private static ResourceLocation tex = new ResourceLocation(HellCore.MODID, "textures/gui/GuiInfinityGenerator.png");

    public GuiInfinityGenerator(ContainerInfinityGenerator container) {
        super(container);
        this.tileentity = (TileInfinityGenerator) container.getTile();
        super.allowUserInput = false;
        super.xSize = 194;
        super.ySize = 168;
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String formatPanelName = I18n.format("tile." + this.tileentity.getName() + ".name", new Object[0]);
        int nmPos = (super.xSize - super.fontRendererObj.getStringWidth(formatPanelName)) / 2;
        super.fontRendererObj.drawString(formatPanelName, nmPos, 7, 7718655);
        String storageString = I18n.format("tooltip.panel.gui.storage", new Object[0]) + ": ";
        String maxOutputString = I18n.format("tooltip.panel.gui.maxOutput", new Object[0]) + ": ";
        String generatingString = I18n.format("tooltip.panel.gui.generating", new Object[0]) + ": ";
        String energyPerTickString = I18n.format("tooltip.panel.gui.energyPerTick", new Object[0]);
        super.fontRendererObj.drawString(storageString + this.tileentity.storage + "/" + this.tileentity.getMaxStorage(), 50, 22, 13487565);
        super.fontRendererObj.drawString(maxOutputString + this.tileentity.getProduction() + " " + energyPerTickString, 50, 32, 13487565);
        super.fontRendererObj.drawString(generatingString + this.tileentity.generating + " " + energyPerTickString, 50, 42, 13487565);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(tex);
        int h = (super.width - super.xSize) / 2;
        int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(h, k, 0, 0, super.xSize, super.ySize);
        if (this.tileentity.storage > 0) {
            int l = this.tileentity.gaugeEnergyScaled(24);
            this.drawTexturedModalRect(h + 19, k + 24, 195, 0, l + 1, 14);
        }
    }
}

