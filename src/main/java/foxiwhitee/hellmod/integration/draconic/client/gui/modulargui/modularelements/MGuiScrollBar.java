package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.modularelements;

import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.IModularGui;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.MGuiElementBase;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.lib.IScrollListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class MGuiScrollBar
        extends MGuiElementBase {
    public int backColour = -16777216;
    public int borderColour = -1;
    public int scrollColour = -8947849;
    public boolean horizontal = false;
    public MGuiElementBase parentScrollable = null;
    public IScrollListener listener = null;
    public double barSizeRatio = 20.0D;
    public int clickOffset = 0;
    public double clickPos = 0.0D;
    protected double increment = 0.1D;
    protected double shiftIncrement = 0.2D;
    private double scrollPos = 0.0D;
    private boolean isDragging = false;

    public MGuiScrollBar(IModularGui modularGui) {
        super(modularGui);
    }

    public MGuiScrollBar(IModularGui modularGui, int xPos, int yPos) {
        super(modularGui, xPos, yPos);
    }

    public MGuiScrollBar(IModularGui modularGui, int xPos, int yPos, int xSize, int ySize) {
        super(modularGui, xPos, yPos, xSize, ySize);
    }


    public void renderBackgroundLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        drawBorderedRect(this.xPos, this.yPos, this.xSize, this.ySize, 1.0D, getBackColour(), getBorderColour());

        double barWidth = this.horizontal ? (this.xSize / this.barSizeRatio) : (this.xSize - 2);
        double barHeight = this.horizontal ? (this.ySize - 2) : (this.ySize / this.barSizeRatio);
        double barXPos = this.horizontal ? ((this.xPos + 1) + this.scrollPos * ((this.xSize - 2) - barWidth)) : (this.xPos + 1);
        double barYPos = this.horizontal ? (this.yPos + 1) : ((this.yPos + 1) + this.scrollPos * ((this.ySize - 2) - barHeight));

        drawColouredRect(barXPos, barYPos, barWidth, barHeight, getScrollColour());
    }


    public boolean handleMouseScroll(int mouseX, int mouseY, int scrollDirection) {
        if (isMouseOver(mouseX, mouseY) || (this.parentScrollable != null && this.parentScrollable.isMouseOver(mouseX, mouseY))) {
            double increment = GuiScreen.isShiftKeyDown() ? this.shiftIncrement : this.increment;
            setScrollPos(this.scrollPos + ((scrollDirection > 0) ? -increment : increment));
            return true;
        }
        return false;
    }


    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY)) {
            double barWidth = this.horizontal ? (this.xSize / this.barSizeRatio) : (this.xSize - 2);
            double barHeight = this.horizontal ? (this.ySize - 2) : (this.ySize / this.barSizeRatio);
            double barXPos = this.horizontal ? ((this.xPos + 1) + this.scrollPos * ((this.xSize - 2) - barWidth)) : (this.xPos + 1);
            double barYPos = this.horizontal ? (this.yPos + 1) : ((this.yPos + 1) + this.scrollPos * ((this.ySize - 2) - barHeight));
            if (isInRect((int) barXPos, (int) barYPos, (int) barWidth, (int) barHeight, mouseX, mouseY)) {
                this.isDragging = true;
                this.clickOffset = this.horizontal ? mouseX : mouseY;
                this.clickPos = this.scrollPos;
            } else {
                if (this.horizontal) {
                    double pos = (mouseX - this.xPos);
                    setScrollPos(pos / this.xSize);
                } else {
                    double pos = (mouseY - this.yPos);
                    setScrollPos(pos / this.ySize);
                }
                this.isDragging = true;
                this.clickOffset = this.horizontal ? mouseX : mouseY;
                this.clickPos = this.scrollPos;
            }
        }

        return false;
    }


    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (this.isDragging) {
            double barWidth = this.horizontal ? (this.xSize / this.barSizeRatio) : (this.xSize - 2);
            double barHeight = this.horizontal ? (this.ySize - 2) : (this.ySize / this.barSizeRatio);
            if (this.horizontal) {
                int movement = mouseX - this.clickOffset;

                double pos = this.clickPos + movement / (this.xSize - barWidth);

                setScrollPos(pos);
            } else {
                int movement = mouseY - this.clickOffset;

                double pos = this.clickPos + movement / (this.ySize - barHeight);


                setScrollPos(pos);
            }
        }
        return super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }


    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        this.isDragging = false;
        return super.mouseReleased(mouseX, mouseY, state);
    }


    public double getScrollPos() {
        return this.scrollPos;
    }


    public MGuiScrollBar setScrollPos(double scrollPos) {
        if (scrollPos > 1.0D) {
            scrollPos = 1.0D;
        } else if (scrollPos < 0.0D) {
            scrollPos = 0.0D;
        }

        this.scrollPos = scrollPos;

        if (this.listener != null) {
            this.listener.scrollBarMoved(this.scrollPos);
        }
        return this;
    }

    public MGuiScrollBar setListener(IScrollListener listener) {
        this.listener = listener;
        return this;
    }

    public void setIncrements(double increment, double shiftIncrement) {
        this.increment = increment;
        this.shiftIncrement = shiftIncrement;
    }

    public int getBackColour() {
        return this.backColour;
    }

    public int getBorderColour() {
        return this.borderColour;
    }

    public int getScrollColour() {
        return this.scrollColour;
    }


    public MGuiScrollBar setBarSizeRatio(double barSizeRatio) {
        this.barSizeRatio = barSizeRatio;
        return this;
    }
}
