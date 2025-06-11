package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui;

import net.minecraft.client.Minecraft;

public interface IModularGui<T extends net.minecraft.client.gui.GuiScreen> {
    T getScreen();

    int xSize();

    int ySize();

    int guiLeft();

    int guiTop();

    int screenWidth();

    int screenHeight();

    Minecraft getMinecraft();

    ModuleManager getManager();

    int getZLevel();

    void setZLevel(int paramInt);
}
