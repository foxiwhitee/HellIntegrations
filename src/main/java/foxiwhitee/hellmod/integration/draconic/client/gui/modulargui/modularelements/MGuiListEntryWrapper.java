package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.modularelements;

import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.IModularGui;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.MGuiElementBase;

public class MGuiListEntryWrapper extends MGuiListEntry {
    private final MGuiElementBase element;
    private boolean lockXPos = false;

    public MGuiListEntryWrapper(IModularGui modularGui, MGuiElementBase element) {
        super(modularGui);
        this.element = element;
        addChild(element);
        this.xSize = element.xSize;
        this.ySize = element.ySize;
    }


    public int getEntryHeight() {
        return this.element.ySize;
    }


    public void moveEntry(int newXPos, int newYPos) {
        if (this.lockXPos) {
            newXPos = this.element.xPos;
        }

        moveBy(newXPos - this.xPos, newYPos - this.yPos);
    }


    public MGuiListEntryWrapper setLockXPos(boolean lockXPos) {
        this.lockXPos = lockXPos;
        return this;
    }
}
