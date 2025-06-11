package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.modularelements;

import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.IModularGui;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.MGuiElementBase;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.lib.IScrollListener;

import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class MGuiList
        extends MGuiElementBase
        implements IScrollListener {
    public int leftPadding = 1;
    public int rightPadding = 1;
    public int topPadding = 1;
    public int bottomPadding = 1;
    public boolean disableList = false;
    public boolean allowOutsideClicks = false;
    public boolean lockScrollBar = false;
    public LinkedList<MGuiListEntry> listEntries = new LinkedList<>();
    public LinkedList<MGuiElementBase> nonListEntries = new LinkedList<>();
    protected boolean scrollBarEnabled = true;
    protected MGuiScrollBar scrollBar;
    protected boolean updateRequired = true;
    private boolean scrollingEnabled = true;

    public MGuiList(IModularGui modularGui) {
        super(modularGui);
    }

    public MGuiList(IModularGui modularGui, int xPos, int yPos) {
        super(modularGui, xPos, yPos);
    }

    public MGuiList(IModularGui modularGui, int xPos, int yPos, int xSize, int ySize) {
        super(modularGui, xPos, yPos, xSize, ySize);
    }


    public void initElement() {
        initScrollBar();
        updateEntriesAndScrollBar();
        super.initElement();
    }

    protected void initScrollBar() {
        if (this.scrollBar != null) {
            removeChild(this.scrollBar);
        }
        if (!this.scrollingEnabled) {
            return;
        }
        this.scrollBar = new MGuiScrollBar(this.modularGui, this.xPos + this.xSize - 10, this.yPos + 1, 10, this.ySize - 2);
        addChild(this.scrollBar);
        this.scrollBar.setListener(this);
        this.scrollBar.parentScrollable = this;
    }


    public MGuiList addEntry(MGuiListEntry entry) {
        this.listEntries.add(entry);
        entry.setList(this);
        super.addChild(entry);
        this.updateRequired = true;
        return this;
    }


    public MGuiElementBase addChild(MGuiElementBase element) {
        this.nonListEntries.add(element);
        return super.addChild(element);
    }

    public void clear() {
        this.toRemove.addAll(this.listEntries);
        this.listEntries.clear();
    }


    public void renderBackgroundLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        for (MGuiElementBase element : this.nonListEntries) {
            if (element.isEnabled() && !this.listEntries.contains(element)) {
                element.renderBackgroundLayer(minecraft, mouseX, mouseY, partialTicks);
            }
        }

        if (this.disableList) {
            return;
        }


        GL11.glEnable(3089);
        int xPos = this.xPos + this.leftPadding;
        int yPos = this.yPos + this.topPadding;
        int xSize = this.xSize - this.leftPadding - this.rightPadding;
        int ySize = this.ySize - this.topPadding - this.bottomPadding;

        double hs = minecraft.displayHeight / this.modularGui.screenHeight();
        double ws = minecraft.displayWidth / this.modularGui.screenWidth();
        int sx = (int) (xSize * ws);
        int sy = (int) (ySize * hs);
        GL11.glScissor((int) (xPos * ws), minecraft.displayHeight - (int) (yPos * hs) - sy, sx, sy);

        for (MGuiElementBase element : this.listEntries) {
            if (element.isEnabled()) {
                element.renderBackgroundLayer(minecraft, mouseX, mouseY, partialTicks);
            }
        }

        GL11.glScissor(0, 0, minecraft.displayWidth, minecraft.displayHeight);
        GL11.glDisable(3089);
    }


    public void renderForegroundLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        for (MGuiElementBase element : this.nonListEntries) {
            if (element.isEnabled() && !this.listEntries.contains(element)) {
                element.renderForegroundLayer(minecraft, mouseX, mouseY, partialTicks);
            }
        }

        if (this.disableList) {
            return;
        }


        GL11.glEnable(3089);
        int xPos = this.xPos + this.leftPadding;
        int yPos = this.yPos + this.topPadding;
        int xSize = this.xSize - this.leftPadding - this.rightPadding;
        int ySize = this.ySize - this.topPadding - this.bottomPadding;

        double hs = minecraft.displayHeight / this.modularGui.screenHeight();
        double ws = minecraft.displayWidth / this.modularGui.screenWidth();
        int sx = (int) (xSize * ws);
        int sy = (int) (ySize * hs);
        GL11.glScissor((int) (xPos * ws), minecraft.displayHeight - (int) (yPos * hs) - sy, sx, sy);

        for (MGuiElementBase element : this.listEntries) {
            if (element.isEnabled()) {
                element.renderForegroundLayer(minecraft, mouseX, mouseY, partialTicks);
            }
        }

        GL11.glScissor(0, 0, minecraft.displayWidth, minecraft.displayHeight);
        GL11.glDisable(3089);
    }


    public boolean renderOverlayLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        for (MGuiElementBase element : this.nonListEntries) {
            if (element.isEnabled() && !this.listEntries.contains(element) && element.renderOverlayLayer(minecraft, mouseX, mouseY, partialTicks)) {
                return true;
            }
        }


        if (this.disableList) {
            return false;
        }

        for (MGuiElementBase element : this.listEntries) {
            if (element.isEnabled() && element.renderOverlayLayer(minecraft, mouseX, mouseY, partialTicks)) {
                return true;
            }
        }
        return false;
    }


    protected void cullList() {
    }


    public void scrollBarMoved(double pos) {
        if (!this.scrollingEnabled) {
            return;
        }
        if (this.scrollBar == null) {
            initScrollBar();
        }

        int maxMove = getListHeight() - this.ySize - 1;
        this.scrollBar.setIncrements(50.0D / maxMove, 0.1D);
        updateEntriesAndScrollBar();
    }

    protected void updateEntriesAndScrollBar() {
        if (!this.scrollingEnabled) {
            return;
        }
        if (this.scrollBar == null) {
            initScrollBar();
        }

        double scrollPos = (this.scrollBar == null) ? 0.0D : this.scrollBar.getScrollPos();
        int yOffset = this.topPadding;

        int maxMove = getListHeight() - this.ySize - 1;

        if (maxMove > 0 && scrollPos > 0.0D) {
            yOffset = this.topPadding - (int) (scrollPos * maxMove);
        }

        for (MGuiListEntry entry : this.listEntries) {
            if (!entry.isEnabled()) {
                continue;
            }

            entry.moveEntry(this.xPos + this.leftPadding, this.yPos + yOffset);

            yOffset += entry.getEntryHeight();
        }

        boolean canScroll = (maxMove > 0);

        if (!canScroll && this.lockScrollBar) {
            this.scrollBar.setEnabled(this.scrollBarEnabled = true);
            this.scrollBar.setBarSizeRatio(0.0D);
        } else {
            this.scrollBar.setEnabled(this.scrollBarEnabled = canScroll);
            this.scrollBar.setBarSizeRatio((maxMove + this.ySize) / this.ySize);
        }
    }


    protected int getListHeight() {
        int height = 0;

        for (MGuiListEntry entry : this.listEntries) {
            if (!entry.isEnabled()) {
                continue;
            }

            height += entry.getEntryHeight();
        }

        return height + this.topPadding + this.bottomPadding;
    }


    protected int getRawListHeight() {
        int height = 0;

        for (MGuiListEntry entry : this.listEntries) {
            if (!entry.isEnabled()) {
                continue;
            }

            height += entry.getEntryHeight();
        }

        return height;
    }


    public boolean handleMouseScroll(int mouseX, int mouseY, int scrollDirection) {
        return super.handleMouseScroll(mouseX, mouseY, scrollDirection);
    }


    public boolean onUpdate() {
        if (!this.toRemove.isEmpty()) {
            this.nonListEntries.removeAll(this.toRemove);
        }

        if (this.updateRequired) {
            this.updateRequired = false;
            updateEntriesAndScrollBar();
        }

        return super.onUpdate();
    }


    public void schedualUpdate() {
        this.updateRequired = true;
    }


    public MGuiList setScrollBarEnabled(boolean scrollBarEnabled) {
        this.scrollBarEnabled = scrollBarEnabled;
        return this;
    }


    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isMouseOver(mouseX, mouseY) && !this.allowOutsideClicks) {
            return false;
        }

        for (MGuiElementBase element : this.nonListEntries) {
            if (element.isEnabled() && element.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }

        if (this.disableList) {
            return false;
        }

        for (MGuiElementBase element : this.listEntries) {
            if (element.isEnabled() && element.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        return false;
    }


    public MGuiList setScrollingEnabled(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
        return this;
    }


    public MGuiList sortEvenSpacing(boolean compress) {
        int totalEntryHeight = getRawListHeight();
        int remainingSpace = this.ySize - totalEntryHeight;

        double y = this.yPos;
        if (remainingSpace >= 0) {

            for (MGuiListEntry entry : this.listEntries) {
                entry.setXPos(this.xPos + (this.xSize - entry.xSize) / 2);
                entry.setYPos((int) y);
                y += entry.ySize;
            }


            y = 0.0D;
            for (MGuiListEntry entry : this.listEntries) {
                double eHeight = entry.getEntryHeight();
                double scale = eHeight / totalEntryHeight;
                double offsetShare = scale * remainingSpace / 2.0D;
                entry.moveBy(0, (int) (y + offsetShare));
                y += offsetShare * 2.0D;
            }
        } else {
            int ySize = compress ? this.ySize : totalEntryHeight;
            int i = 0;
            for (MGuiListEntry entry : this.listEntries) {
                double eHeight = entry.getEntryHeight();
                double yAllocation = ySize / this.listEntries.size();
                double overlap = eHeight - yAllocation;
                yAllocation = (ySize - overlap) / this.listEntries.size();

                entry.setXPos(this.xPos + (this.xSize - entry.xSize) / 2);
                entry.setYPos(this.yPos + (int) (i * yAllocation));
                i++;
            }
        }

        return this;
    }
}
