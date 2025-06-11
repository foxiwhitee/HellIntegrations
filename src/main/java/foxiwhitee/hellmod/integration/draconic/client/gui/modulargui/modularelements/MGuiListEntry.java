package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.modularelements;

import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.IModularGui;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.MGuiElementBase;

public abstract class MGuiListEntry extends MGuiElementBase {
    protected MGuiList list;

    public MGuiListEntry(IModularGui modularGui) {
        super(modularGui);
    }

    public MGuiListEntry(IModularGui modularGui, int ySize) {
        super(modularGui, 0, 0, 0, ySize);
    }


    public abstract int getEntryHeight();

    public MGuiElementBase setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.list.schedualUpdate();
        return this;
    }


    public abstract void moveEntry(int paramInt1, int paramInt2);


    public void setList(MGuiList list) {
        this.list = list;
    }
}

