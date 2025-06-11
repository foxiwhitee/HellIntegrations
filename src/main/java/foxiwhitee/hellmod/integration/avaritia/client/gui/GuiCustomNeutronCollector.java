package foxiwhitee.hellmod.integration.avaritia.client.gui;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerCustomNeutronCollector;
import foxiwhitee.hellmod.integration.avaritia.tile.collectors.TileCustomNeutronCollector;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCustomNeutronCollector extends GuiContainer {
    private static ResourceLocation TEXTURE;
    //protected int xSize = 210;
    //protected int ySize = 200;
    private TileCustomNeutronCollector tile;
    public GuiCustomNeutronCollector(ContainerCustomNeutronCollector container) {
        super(container);
        this.tile = (TileCustomNeutronCollector) container.getTile();
        this.ySize = 200;
        this.xSize = 210;
        TEXTURE = new ResourceLocation(HellCore.MODID, "textures/gui/gui_" + this.tile.getName() + ".png");
    }

    protected void drawGuiContainerForegroundLayer(int x, int y) {}

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        drawTexturedModalRect(k-17, l-17, 23, 28, this.xSize, this.ySize);
    }
}
