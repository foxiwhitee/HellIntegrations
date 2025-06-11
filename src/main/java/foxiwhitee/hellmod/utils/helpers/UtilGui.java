package foxiwhitee.hellmod.utils.helpers;

import foxiwhitee.hellmod.client.gui.HellBaseGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class UtilGui {
    public static void drawTexture(double x, double y, double u, double v, double width, double height,
                                   double uWidth, double uHeight) {
        drawTexture(x, y, u, v, width, height, uWidth, uHeight, 512, 512, 0, null, null, null);
    }

    public static void drawTexture(double x, double y, double u, double v, double width, double height,
                                   double uWidth, double uHeight, double textureWidth, double textureHeight) {
        drawTexture(x, y, u, v, width, height, uWidth, uHeight, textureWidth, textureHeight, 0, null, null, null);
    }

    public static void drawTexture(double x, double y, double u, double v, double width, double height,
                                   double uWidth, double uHeight, double textureWidth, double textureHeight, int z) {
        drawTexture(x, y, u, v, width, height, uWidth, uHeight, textureWidth, textureHeight, z, null, null, null);
    }

    public static void drawTexture(double x, double y, double u, double v, double width, double height,
                                   double uWidth, double uHeight, double textureWidth, double textureHeight,
                                   HellBaseGui gui, String modid, String texture) {
        drawTexture(x, y, u, v, width, height, uWidth, uHeight, textureWidth, textureHeight, 0, gui, modid, texture);
    }

    public static void drawTexture(double x, double y, double u, double v, double width, double height,
                                   double uWidth, double uHeight, double textureWidth, double textureHeight, int z,
                                   HellBaseGui gui, String modid, String texture) {
        if (gui != null && modid != null && texture != null) {
            gui.bindTexture(modid, texture);
        }
        double px = 1.0 / textureWidth;
        double py = 1.0 / textureHeight;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV(x, y + height, z, u * px, (v + uHeight) * py);
        tessellator.addVertexWithUV(x + width, y + height, z, (u + uWidth) * px, (v + uHeight) * py);
        tessellator.addVertexWithUV(x + width, y, z, (u + uWidth) * px, v * py);
        tessellator.addVertexWithUV(x, y, z, u * px, v * py);

        tessellator.draw();

        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawAreaTooltip(int x, int y, String tooltip, int minX, int minY, int maxX, int maxY, boolean drawborder) {
        drawAreaTooltip(x, y, tooltip, minX, minY, maxX, maxY, 0, 0, drawborder);
    }

    public static void drawAreaTooltip(int x, int y, String tooltip, int minX, int minY, int maxX, int maxY) {
        drawAreaTooltip(x, y, tooltip, minX, minY, maxX, maxY, 0, 0, true);
    }

    public static void drawAreaTooltip(int x, int y, String tooltip, int minX, int minY, int maxX, int maxY, int yoffset, int xoffset) {
        drawAreaTooltip(x, y, tooltip, minX, minY, maxX, maxY, yoffset, xoffset, true);
    }

    public static void drawAreaTooltip(int x, int y, String tooltip, int minX, int minY, int maxX, int maxY, int yoffset, int xoffset, boolean drawborder) {
        if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
            drawTooltip(x, y, yoffset, xoffset, tooltip, drawborder, 0);
        }

    }

    public static void drawTooltip(int x, int y, int yoffset, int xoffset, String tooltip, boolean drawborder, int width) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        if (width == 0) {
            x -= fontRenderer.getStringWidth(tooltip) / 2;
        }

        y -= 12;
        x += xoffset;
        y += yoffset;
        y += 50;
        if (width == 0) {
            width = fontRenderer.getStringWidth(tooltip);
        }

        width += 8;
        int height = 8;
        int backgroundColor = 255;
        int borderColor = -1162167553;
        GL11.glPushAttrib(16704);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        drawRectangle(tessellator, x - 3, y - 4, x + width + 3, y - 3, backgroundColor);
        drawRectangle(tessellator, x - 3, y + height + 3, x + width + 3, y + height + 4, backgroundColor);
        drawRectangle(tessellator, x - 3, y - 3, x + width + 3, y + height + 3, backgroundColor);
        drawRectangle(tessellator, x - 4, y - 3, x - 3, y + height + 3, backgroundColor);
        drawRectangle(tessellator, x + width + 3, y - 3, x + width + 4, y + height + 3, backgroundColor);
        if (drawborder) {
            drawRectangle(tessellator, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, borderColor);
            drawRectangle(tessellator, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, borderColor);
            drawRectangle(tessellator, x - 3, y - 3, x + width + 3, y - 3 + 1, borderColor);
            drawRectangle(tessellator, x - 3, y + height + 2, x + width + 3, y + height + 3, borderColor);
        }

        tessellator.draw();
        GL11.glEnable(3553);
        fontRenderer.drawStringWithShadow(tooltip, x + 4, y, -2);
        GL11.glPopAttrib();
    }

    private static void drawRectangle(Tessellator tessellator, int x1, int y1, int x2, int y2, int color) {
        tessellator.setColorRGBA(color >>> 24 & 255, color >>> 16 & 255, color >>> 8 & 255, color & 255);
        tessellator.addVertex((double)x2, (double)y1, (double)300.0F);
        tessellator.addVertex((double)x1, (double)y1, (double)300.0F);
        tessellator.addVertex((double)x1, (double)y2, (double)300.0F);
        tessellator.addVertex((double)x2, (double)y2, (double)300.0F);
    }

    public static void drawCustomTooltip(GuiScreen gui, RenderItem itemRenderer, FontRenderer fr, List var4, int par2, int par3, int subTipColor) {
        //GL11.glDisable(32826);
        GL11.glDisable(2929);
        if (!var4.isEmpty()) {
            int var5 = 0;

            for(Object var7 : var4) {
                int var8 = fr.getStringWidth((String)var7);
                if (var8 > var5) {
                    var5 = var8;
                }
            }

            int var15 = par2 + 12;
            int var16 = par3 - 12;
            int var9 = 8;
            if (var4.size() > 1) {
                var9 += 2 + (var4.size() - 1) * 10;
            }

            itemRenderer.zLevel = 300.0F;
            int var10 = -267386864;
            drawGradientRect(var15 - 3, var16 - 4, var15 + var5 + 3, var16 - 3, var10, var10);
            drawGradientRect(var15 - 3, var16 + var9 + 3, var15 + var5 + 3, var16 + var9 + 4, var10, var10);
            drawGradientRect(var15 - 3, var16 - 3, var15 + var5 + 3, var16 + var9 + 3, var10, var10);
            drawGradientRect(var15 - 4, var16 - 3, var15 - 3, var16 + var9 + 3, var10, var10);
            drawGradientRect(var15 + var5 + 3, var16 - 3, var15 + var5 + 4, var16 + var9 + 3, var10, var10);
            int var11 = 1347420415;
            int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
            drawGradientRect(var15 - 3, var16 - 3 + 1, var15 - 3 + 1, var16 + var9 + 3 - 1, var11, var12);
            drawGradientRect(var15 + var5 + 2, var16 - 3 + 1, var15 + var5 + 3, var16 + var9 + 3 - 1, var11, var12);
            drawGradientRect(var15 - 3, var16 - 3, var15 + var5 + 3, var16 - 3 + 1, var11, var11);
            drawGradientRect(var15 - 3, var16 + var9 + 2, var15 + var5 + 3, var16 + var9 + 3, var12, var12);

            for(int var13 = 0; var13 < var4.size(); ++var13) {
                String var14 = (String)var4.get(var13);
                if (var13 == 0) {
                    var14 = "§" + Integer.toHexString(subTipColor) + var14;
                } else {
                    var14 = "§7" + var14;
                }

                fr.drawStringWithShadow(var14, var15, var16, -1);
                if (var13 == 0) {
                    var16 += 2;
                }

                var16 += 10;
            }
        }

        itemRenderer.zLevel = 0.0F;
        GL11.glEnable(2929);
    }

    public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        float var7 = (float)(par5 >> 24 & 255) / 255.0F;
        float var8 = (float)(par5 >> 16 & 255) / 255.0F;
        float var9 = (float)(par5 >> 8 & 255) / 255.0F;
        float var10 = (float)(par5 & 255) / 255.0F;
        float var11 = (float)(par6 >> 24 & 255) / 255.0F;
        float var12 = (float)(par6 >> 16 & 255) / 255.0F;
        float var13 = (float)(par6 >> 8 & 255) / 255.0F;
        float var14 = (float)(par6 & 255) / 255.0F;
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex((double)par3, (double)par2, (double)300.0F);
        var15.addVertex((double)par1, (double)par2, (double)300.0F);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex((double)par1, (double)par4, (double)300.0F);
        var15.addVertex((double)par3, (double)par4, (double)300.0F);
        var15.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }
}
