package foxiwhitee.hellmod.integration.draconic.helpers.gui;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiHelper {
    public static final RenderItem itemRender = new RenderItem();

    public static void drawGradientRect(int posX, int posY, int xSize, int ySize, int colour, int colour2) {
        drawGradientRect(posX, posY, (posX + xSize), (posY + ySize), colour, colour2, 1.0F, 0.0D);
    }

    public static void drawColouredRect(int posX, int posY, int xSize, int ySize, int colour) {
        drawGradientRect(posX, posY, (posX + xSize), (posY + ySize), colour, colour, 1.0F, 0.0D);
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int colour1, int colour2, float fade, double zLevel) {
        float alpha1 = (colour1 >> 24 & 0xFF) / 255.0F * fade;
        float red1 = (colour1 >> 16 & 0xFF) / 255.0F;
        float green1 = (colour1 >> 8 & 0xFF) / 255.0F;
        float blue1 = (colour1 & 0xFF) / 255.0F;
        float alpha2 = (colour2 >> 24 & 0xFF) / 255.0F * fade;
        float red2 = (colour2 >> 16 & 0xFF) / 255.0F;
        float green2 = (colour2 >> 8 & 0xFF) / 255.0F;
        float blue2 = (colour2 & 0xFF) / 255.0F;
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(red1, green1, blue1, alpha1);
        tessellator.addVertex(right, top, zLevel);
        tessellator.addVertex(left, top, zLevel);
        tessellator.setColorRGBA_F(red2, green2, blue2, alpha2);
        tessellator.addVertex(left, bottom, zLevel);
        tessellator.addVertex(right, bottom, zLevel);
        tessellator.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }

    public static void drawCenteredSplitString(FontRenderer fontRenderer, String str, int x, int y, int wrapWidth, int color, boolean dropShadow) {
        for (Object o : fontRenderer.listFormattedStringToWidth(str, wrapWidth)) {
            String s = (String)o;
            drawCenteredString(fontRenderer, s, x, y, color, dropShadow);
            y += fontRenderer.FONT_HEIGHT;
        }
    }

    public static void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y, int color, boolean dropShadow) {
        fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, color, dropShadow);
    }

    public static void drawGuiBaseBackground(Gui gui, int posX, int posY, int xSize, int ySize) {
        bindTexture("textures/gui/base_gui.png");
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        gui.drawTexturedModalRect(posX, posY, 0, 0, xSize - 3, ySize - 3);
        gui.drawTexturedModalRect(posX + xSize - 3, posY, 253, 0, 3, ySize - 3);
        gui.drawTexturedModalRect(posX, posY + ySize - 3, 0, 253, xSize - 3, 3);
        gui.drawTexturedModalRect(posX + xSize - 3, posY + ySize - 3, 253, 253, 3, 3);
    }

    public static void bindTexture(ResourceLocation texture) {
        (Minecraft.getMinecraft()).renderEngine.bindTexture(texture);
    }

    public static void bindTexture(String rs) {
        bindTexture(new ResourceLocation(HellCore.MODID, rs));
    }

    public static void drawPlayerSlots(Gui gui, int posX, int posY, boolean center) {
        bindTexture("textures/gui/bc_widgets.png");
        if (center)
            posX -= 81;
        for (int y = 0; y < 3; y++) {
            for (int i = 0; i < 9; i++)
                gui.drawTexturedModalRect(posX + i * 18, posY + y * 18, 138, 0, 18, 18);
        }
        for (int x = 0; x < 9; x++)
            gui.drawTexturedModalRect(posX + x * 18, posY + 58, 138, 0, 18, 18);
    }

    public static void drawStack2D(ItemStack stack, Minecraft mc, int x, int y, float scale) {
        if (OreDictUtil.isEmpty(stack))
            return;
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        itemRender.zLevel = 200.0F;
        FontRenderer font = mc.fontRenderer;
        itemRender.renderItemAndEffectIntoGUI(font, mc.getTextureManager(), stack, x, y);
        String count = (stack.stackSize > 1) ? String.valueOf(stack.stackSize) : "";
        itemRender.renderItemOverlayIntoGUI(font, mc.getTextureManager(), stack, x, y, count);
        itemRender.zLevel = 0.0F;
    }

    public static void drawBorderedRect(int posX, int posY, int xSize, int ySize, int borderWidth, int fillColour, int borderColour) {
        drawColouredRect(posX, posY, xSize, borderWidth, borderColour);
        drawColouredRect(posX, posY + ySize - borderWidth, xSize, borderWidth, borderColour);
        drawColouredRect(posX, posY + borderWidth, borderWidth, ySize - 2 * borderWidth, borderColour);
        drawColouredRect(posX + xSize - borderWidth, posY + borderWidth, borderWidth, ySize - 2 * borderWidth, borderColour);
        drawColouredRect(posX + borderWidth, posY + borderWidth, xSize - 2 * borderWidth, ySize - 2 * borderWidth, fillColour);
    }
}
