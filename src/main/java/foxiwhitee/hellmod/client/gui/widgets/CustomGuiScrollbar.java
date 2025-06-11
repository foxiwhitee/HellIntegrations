package foxiwhitee.hellmod.client.gui.widgets;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.widgets.GuiScrollbar;
import org.lwjgl.opengl.GL11;

public class CustomGuiScrollbar extends GuiScrollbar {
    protected int displayX = 0;
    protected int displayY = 0;
    protected int width = 12;
    protected int height = 16;
    protected int pageSize = 1;
    protected int maxScroll = 0;
    protected int minScroll = 0;
    protected int currentScroll = 0;

    public CustomGuiScrollbar() {
    }



    public void draw(AEBaseGui g) {
        g.bindTexture("minecraft", "gui/container/creative_inventory/tabs.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.getRange() == 0) {
            g.drawTexturedModalRect(this.displayX, this.displayY, 232 + this.width, 0, this.width, 15);
        } else {
            int offset = (this.currentScroll - this.minScroll) * (this.height - 15) / this.getRange();
            g.drawTexturedModalRect(this.displayX, offset + this.displayY, 232, 0, this.width, 15);
        }

    }

    protected int getRange() {
        return this.maxScroll - this.minScroll;
    }

    public int getLeft() {
        return this.displayX;
    }

    public GuiScrollbar setLeft(int v) {
        this.displayX = v;
        return this;
    }

    public int getTop() {
        return this.displayY;
    }

    public GuiScrollbar setTop(int v) {
        this.displayY = v;
        return this;
    }

    public int getWidth() {
        return this.width;
    }

    public GuiScrollbar setWidth(int v) {
        this.width = v;
        return this;
    }

    public int getHeight() {
        return this.height;
    }

    public GuiScrollbar setHeight(int v) {
        this.height = v;
        return this;
    }

    public void setRange(int min, int max, int pageSize) {
        this.minScroll = min;
        this.maxScroll = max;
        this.pageSize = pageSize;
        if (this.minScroll > this.maxScroll) {
            this.maxScroll = this.minScroll;
        }

        this.applyRange();
    }

    private void applyRange() {
        this.currentScroll = Math.max(Math.min(this.currentScroll, this.maxScroll), this.minScroll);
    }

    public int getCurrentScroll() {
        return this.currentScroll;
    }

    public void click(AEBaseGui aeBaseGui, int x, int y) {
        if (this.getRange() != 0) {
            if (x > this.displayX && x <= this.displayX + this.width && y > this.displayY && y <= this.displayY + this.height) {
                this.currentScroll = y - this.displayY;
                this.currentScroll = this.minScroll + this.currentScroll * 2 * this.getRange() / this.height;
                this.currentScroll = this.currentScroll + 1 >> 1;
                this.applyRange();
            }

        }
    }

    public void wheel(int delta) {
        delta = Math.max(Math.min(-delta, 1), -1);
        this.currentScroll += delta * this.pageSize;
        this.applyRange();
    }
}

