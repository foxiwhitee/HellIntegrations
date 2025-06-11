package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.modularelements;

import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.IModularGui;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.MGuiElementBase;
import net.minecraft.client.Minecraft;

public class MGuiBorderedRect
        extends MGuiElementBase {
    public int fillColour = -16777216;
    public int borderColour = -1;
    public int borderWidth = 1;

    public MGuiBorderedRect(IModularGui modularGui) {
        super(modularGui);
    }

    public MGuiBorderedRect(IModularGui modularGui, int xPos, int yPos) {
        super(modularGui, xPos, yPos);
    }

    public MGuiBorderedRect(IModularGui modularGui, int xPos, int yPos, int xSize, int ySize) {
        super(modularGui, xPos, yPos, xSize, ySize);
    }


    public void renderBackgroundLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        super.renderBackgroundLayer(minecraft, mouseX, mouseY, partialTicks);
        drawBorderedRect(this.xPos, this.yPos, this.xSize, this.ySize, this.borderWidth, this.fillColour, this.borderColour);
    }

    public MGuiBorderedRect setFillColour(int fillColour) {
        this.fillColour = fillColour;
        return this;
    }

    public MGuiBorderedRect setBorderColour(int borderColour) {
        this.borderColour = borderColour;
        return this;
    }

    public MGuiBorderedRect setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }
}