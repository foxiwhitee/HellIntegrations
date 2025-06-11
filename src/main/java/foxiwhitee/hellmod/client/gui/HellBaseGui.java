package foxiwhitee.hellmod.client.gui;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class HellBaseGui extends GuiContainer {

    public HellBaseGui(Container container) {
        super(container);
    }

    public HellBaseGui(Container container, int xSize, int ySize) {
        super(container);
        this.ySize = ySize;
        this.xSize = xSize;
    }

    public void setSizeX(int x) {
        this.xSize = x;
    }

    public void setSizeY(int y) {
        this.ySize = y;
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {}

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.bindTexture(HellCore.MODID, this.getBackground());
        UtilGui.drawTexture(offsetX, offsetY, 0, 0, xSize, ySize, xSize, ySize, getSizeTexture()[0], getSizeTexture()[1]);
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY, float partialTicks) {}

    public void bindTexture(String base, String file) {
        ResourceLocation loc = new ResourceLocation(base, "textures/" + file);
        this.mc.getTextureManager().bindTexture(loc);
    }

    protected abstract String getBackground();

    protected int[] getSizeTexture() {
        return new int[]{512, 512};
    }

    protected final void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int ox = this.guiLeft;
        int oy = this.guiTop;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawBG(ox, oy, x, y);
        this.drawBG(ox, oy, x, y, f);
    }

    protected final void drawGuiContainerForegroundLayer(int x, int y) {
        int ox = this.guiLeft;
        int oy = this.guiTop;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawFG(ox, oy, x, y);
    }

    public final void drawIfInMouse(int mouseX, int mouseY, int x, int y, int w, int h, String... str) {
        if (mouseX >= this.guiLeft + x && mouseX <= this.guiLeft + x + w && mouseY >= this.guiTop + y && mouseY <= this.guiTop + y + h)
            drawHoveringText(new ArrayList<>(Arrays.asList(Arrays.copyOf((Object[]) str, str.length))), mouseX, mouseY, this.mc.fontRenderer);
    }

    protected void drawHoveringText(List p_drawHoveringText_1_, int p_drawHoveringText_2_, int p_drawHoveringText_3_, FontRenderer p_drawHoveringText_4_) {
        if (!p_drawHoveringText_1_.isEmpty()) {
            GL11.glDisable(32826);
            //RenderHelper.disableStandardItemLighting();
            //GL11.glDisable(2896);
            GL11.glDisable(2929);
            int k = 0;

            for(Object s : p_drawHoveringText_1_) {
                int l = p_drawHoveringText_4_.getStringWidth((String) s);
                if (l > k) {
                    k = l;
                }
            }

            int j2 = p_drawHoveringText_2_ + 12;
            int k2 = p_drawHoveringText_3_ - 12;
            int i1 = 8;
            if (p_drawHoveringText_1_.size() > 1) {
                i1 += 2 + (p_drawHoveringText_1_.size() - 1) * 10;
            }

            if (j2 + k > this.width) {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > this.height) {
                k2 = this.height - i1 - 6;
            }

            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int j1 = -267386864;
            this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for(int i2 = 0; i2 < p_drawHoveringText_1_.size(); ++i2) {
                String s1 = (String)p_drawHoveringText_1_.get(i2);
                p_drawHoveringText_4_.drawStringWithShadow(s1, j2, k2, -1);
                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            //GL11.glEnable(2896);
            GL11.glEnable(2929);
            //RenderHelper.enableStandardItemLighting();
            GL11.glEnable(32826);
        }

    }

}
