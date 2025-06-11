package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import foxiwhitee.hellmod.integration.draconic.helpers.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


public class MGuiElementBase {
    protected static RenderItem itemRender = GuiHelper.itemRender;
    private final List<String> groups = new ArrayList<>();
    private final Rectangle rectangle = new Rectangle();

    public int xPos;

    public int yPos;

    public int xSize;
    public int ySize;
    public IModularGui modularGui;
    public String id = "";
    public LinkedList<MGuiElementBase> childElements = new LinkedList<>();

    public FontRenderer fontRenderer;

    public Minecraft mc;

    public Object linkedObject = null;
    public MGuiElementBase parent = null;


    public int displayLevel = 0;
    protected List<MGuiElementBase> toRemove = new ArrayList<>();


    protected double zOffset = 0.0D;
    private boolean enabled = true;

    public MGuiElementBase(IModularGui modularGui) {
        this.modularGui = modularGui;
    }

    public MGuiElementBase(IModularGui modularGui, int xPos, int yPos) {
        this(modularGui);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public MGuiElementBase(IModularGui modularGui, int xPos, int yPos, int xSize, int ySize) {
        this(modularGui, xPos, yPos);
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
        return (mouseX >= x && mouseX <= x + xSize && mouseY >= y && mouseY <= y + ySize);
    }


    public static int mixColours(int colour1, int colour2) {
        return mixColours(colour1, colour2, false);
    }

    public static int mixColours(int colour1, int colour2, boolean subtract) {
        int alpha1 = colour1 >> 24 & 0xFF;
        int alpha2 = colour2 >> 24 & 0xFF;
        int red1 = colour1 >> 16 & 0xFF;
        int red2 = colour2 >> 16 & 0xFF;
        int green1 = colour1 >> 8 & 0xFF;
        int green2 = colour2 >> 8 & 0xFF;
        int blue1 = colour1 & 0xFF;
        int blue2 = colour2 & 0xFF;

        int alpha = clamp(alpha1 + (subtract ? -alpha2 : alpha2), 0, 255);
        int red = clamp(red1 + (subtract ? -red2 : red2), 0, 255);
        int green = clamp(green1 + (subtract ? -green2 : green2), 0, 255);
        int blue = clamp(blue1 + (subtract ? -blue2 : blue2), 0, 255);

        return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
    }

    private static int clamp(int a, int b, int c) {
        return (a < b) ? b : Math.min(a, c);
    }


    public void initElement() {
        for (MGuiElementBase element : this.childElements) {
            element.initElement();
        }
    }

    public MGuiElementBase addChild(MGuiElementBase element) {
        this.childElements.add(element);
        element.parent = this;
        return this;
    }

    public MGuiElementBase addChildren(List<MGuiElementBase> elements) {
        this.childElements.addAll(elements);
        for (MGuiElementBase element : elements) {
            element.parent = this;
        }
        return this;
    }

    public MGuiElementBase removeChild(MGuiElementBase element) {
        if (element != null && this.childElements.contains(element)) {
            this.toRemove.add(element);
        }
        return this;
    }


    public MGuiElementBase removeChildByID(String id) {
        Iterator<MGuiElementBase> i = this.childElements.iterator();
        while (i.hasNext()) {
            MGuiElementBase element = i.next();
            if (element.id != null && element.id.equals(id)) {
                this.toRemove.add(element);
                return this;
            }
        }
        return this;
    }

    public MGuiElementBase removeChildByGroup(String group) {
        Iterator<MGuiElementBase> i = this.childElements.iterator();
        while (i.hasNext()) {
            MGuiElementBase element = i.next();
            if (element.isInGroup(group)) {
                this.toRemove.add(element);
            }
        }
        return this;
    }

    public MGuiElementBase setChildIDEnabled(String id, boolean enabled) {
        Iterator<MGuiElementBase> i = this.childElements.iterator();
        while (i.hasNext()) {
            MGuiElementBase element = i.next();
            if (element.id != null && element.id.equals(id)) {
                element.enabled = enabled;
                return this;
            }
        }
        return this;
    }

    public MGuiElementBase setChildGroupEnabled(String group, boolean enabled) {
        Iterator<MGuiElementBase> i = this.childElements.iterator();
        while (i.hasNext()) {
            MGuiElementBase element = i.next();
            if (element.isInGroup(group)) {
                element.enabled = enabled;
            }
        }
        return this;
    }

    public MGuiElementBase addToGroup(String group) {
        this.groups.add(group);
        return this;
    }


    public MGuiElementBase removeFromGroup(String group) {
        this.groups.remove(group);
        return this;
    }

    public MGuiElementBase removeFromAllGroups() {
        this.groups.clear();
        return this;
    }


    public boolean isInGroup(String group) {
        return this.groups.contains(group);
    }

    public List<String> getGroups() {
        return this.groups;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public MGuiElementBase setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }


    public boolean isMouseOver(int mouseX, int mouseY) {
        return (mouseX >= this.xPos && mouseX <= this.xPos + this.xSize && mouseY >= this.yPos && mouseY <= this.yPos + this.ySize);
    }


    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (MGuiElementBase element : this.childElements) {
            if (element.isEnabled() && element.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        return false;
    }


    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        for (MGuiElementBase element : this.childElements) {
            if (element.isEnabled() && element.mouseReleased(mouseX, mouseY, state)) {
                return true;
            }
        }
        return false;
    }


    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for (MGuiElementBase element : this.childElements) {
            if (element.isEnabled() && element.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)) {
                return true;
            }
        }
        return false;
    }


    public boolean handleMouseInput() {
        int mouseX = Mouse.getEventX() * this.modularGui.screenWidth() / (this.modularGui.getMinecraft()).displayWidth;
        int mouseY = this.modularGui.screenHeight() - Mouse.getEventY() * this.modularGui.screenHeight() / (this.modularGui.getMinecraft()).displayHeight - 1;
        int scrollDirection = Mouse.getEventDWheel();

        if (scrollDirection != 0) {
            for (MGuiElementBase element : this.childElements) {
                if (element.isEnabled() && element.handleMouseScroll(mouseX, mouseY, scrollDirection)) {
                    return true;
                }
            }
        }

        for (MGuiElementBase element : this.childElements) {
            if (element.isEnabled() && element.handleMouseInput()) {
                return true;
            }
        }
        return false;
    }


    public boolean handleMouseScroll(int mouseX, int mouseY, int scrollDirection) {
        for (MGuiElementBase element : this.childElements) {
            if (element.isEnabled() && element.handleMouseScroll(mouseX, mouseY, scrollDirection)) {
                return true;
            }
        }
        return false;
    }


    protected boolean keyTyped(char typedChar, int keyCode) {
        for (MGuiElementBase element : this.childElements) {
            if (element.isEnabled() && element.keyTyped(typedChar, keyCode)) {
                return true;
            }
        }
        return false;
    }


    public boolean onUpdate() {
        if (!this.toRemove.isEmpty()) {
            this.childElements.removeAll(this.toRemove);
            this.toRemove.clear();
        }

        for (MGuiElementBase element : this.childElements) {
            if (element.onUpdate()) {
                return true;
            }
        }
        return false;
    }


    public void renderBackgroundLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        for (MGuiElementBase element : this.childElements) {
            if (element.isEnabled()) {
                element.renderBackgroundLayer(minecraft, mouseX, mouseY, partialTicks);
            }
        }
    }

    public void renderForegroundLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        for (MGuiElementBase element : this.childElements) {
            if (element.isEnabled()) {
                element.renderForegroundLayer(minecraft, mouseX, mouseY, partialTicks);
            }
        }
    }


    public boolean renderOverlayLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        for (MGuiElementBase element : this.childElements) {
            if (element.isEnabled() && element.renderOverlayLayer(minecraft, mouseX, mouseY, partialTicks)) {
                return true;
            }
        }
        return false;
    }

    public GuiScreen getScreen() {
        return this.modularGui.getScreen();
    }


    public int hashCode() {
        return ("[" + this.id + "-" + this.xPos + "-" + this.yPos + "-" + this.xSize + "-" + this.ySize + "" + this.displayLevel + "]").hashCode();
    }

    public void bindTexture(ResourceLocation resourceLocation) {
        this.modularGui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    }

    public MGuiElementBase setId(String id) {
        this.id = id;
        return this;
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
        for (MGuiElementBase element : this.childElements) {
            element.setWorldAndResolution(mc, width, height);
        }
    }


    public void moveBy(int xAmount, int yAmount) {
        this.xPos += xAmount;
        this.yPos += yAmount;
        for (MGuiElementBase element : this.childElements) {
            element.moveBy(xAmount, yAmount);
        }
    }


    public void setXPos(int x) {
        moveBy(x - this.xPos, 0);
    }


    public void setYPos(int y) {
        moveBy(0, y - this.yPos);
    }

    public MGuiElementBase setLinkedObject(Object linkedObject) {
        this.linkedObject = linkedObject;
        return this;
    }

    public Rectangle getRectangle() {
        this.rectangle.setBounds(this.xPos, this.yPos, this.xSize, this.ySize);
        return this.rectangle;
    }

    public double getRenderZLevel() {
        return this.modularGui.getZLevel() + this.zOffset;
    }

    public void drawHorizontalLine(double startX, double endX, double y, int color) {
        if (endX < startX) {
            double i = startX;
            startX = endX;
            endX = i;
        }

        drawRect(startX, y, endX + 1.0D, y + 1.0D, color);
    }

    public void drawVerticalLine(double x, double startY, double endY, int color) {
        if (endY < startY) {
            double i = startY;
            startY = endY;
            endY = i;
        }

        drawRect(x, startY + 1.0D, x + 1.0D, endY, color);
    }

    public void drawRect(double left, double top, double right, double bottom, int color) {
        double zLevel = getRenderZLevel();
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (color >> 24 & 0xFF) / 255.0F;
        float f = (color >> 16 & 0xFF) / 255.0F;
        float f1 = (color >> 8 & 0xFF) / 255.0F;
        float f2 = (color & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);

        tessellator.setColorRGBA_F(f, f1, f2, f3);
        tessellator.startDrawingQuads();
        tessellator.addVertex(left, bottom, zLevel);
        tessellator.addVertex(right, bottom, zLevel);
        tessellator.addVertex(right, top, zLevel);
        tessellator.addVertex(left, top, zLevel);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        double zLevel = getRenderZLevel();
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((x + 0), (y + height), zLevel, ((textureX + 0) * 0.00390625F), ((textureY + height) * 0.00390625F));
        tessellator.addVertexWithUV((x + width), (y + height), zLevel, ((textureX + width) * 0.00390625F), ((textureY + height) * 0.00390625F));
        tessellator.addVertexWithUV((x + width), (y + 0), zLevel, ((textureX + width) * 0.00390625F), ((textureY + 0) * 0.00390625F));
        tessellator.addVertexWithUV((x + 0), (y + 0), zLevel, ((textureX + 0) * 0.00390625F), ((textureY + 0) * 0.00390625F));
        tessellator.draw();
    }


    public void drawTexturedModalRect(double xCoord, double yCoord, int minU, int minV, int maxU, int maxV) {
        double zLevel = getRenderZLevel();
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(xCoord + 0.0D, yCoord + maxV, zLevel, ((minU + 0) * 0.00390625F), ((minV + maxV) * 0.00390625F));
        tessellator.addVertexWithUV(xCoord + maxU, yCoord + maxV, zLevel, ((minU + maxU) * 0.00390625F), ((minV + maxV) * 0.00390625F));
        tessellator.addVertexWithUV(xCoord + maxU, yCoord + 0.0D, zLevel, ((minU + maxU) * 0.00390625F), ((minV + 0) * 0.00390625F));
        tessellator.addVertexWithUV(xCoord + 0.0D, yCoord + 0.0D, zLevel, ((minU + 0) * 0.00390625F), ((minV + 0) * 0.00390625F));
        tessellator.draw();
    }

    public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn) {
        double zLevel = getRenderZLevel();
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((xCoord + 0), (yCoord + heightIn), zLevel, textureSprite.getMinU(), textureSprite.getMaxV());
        tessellator.addVertexWithUV((xCoord + widthIn), (yCoord + heightIn), zLevel, textureSprite.getMaxU(), textureSprite.getMaxV());
        tessellator.addVertexWithUV((xCoord + widthIn), (yCoord + 0), zLevel, textureSprite.getMaxU(), textureSprite.getMinV());
        tessellator.addVertexWithUV((xCoord + 0), (yCoord + 0), zLevel, textureSprite.getMinU(), textureSprite.getMinV());
        tessellator.draw();
    }

    public void drawModalRectWithCustomSizedTexture(double x, double y, double u, double v, double width, double height, double textureWidth, double textureHeight) {
        double zLevel = getRenderZLevel();
        double f = 1.0D / textureWidth;
        double f1 = 1.0D / textureHeight;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, zLevel, u * f, (v + height) * f1);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, (u + width) * f, (v + height) * f1);
        tessellator.addVertexWithUV(x + width, y, zLevel, (u + width) * f, v * f1);
        tessellator.addVertexWithUV(x, y, zLevel, u * f, v * f1);
        tessellator.draw();
    }

    public int drawString(FontRenderer fontRenderer, String text, float x, float y, int color) {
        return drawString(fontRenderer, text, x, y, color, false);
    }

    public int drawString(FontRenderer fontRenderer, String text, float x, float y, int color, boolean dropShadow) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.0D, 0.0D, getRenderZLevel() + 1.0D);
        int i = fontRenderer.drawString(text, (int) x, (int) y, color, dropShadow);
        GL11.glPopMatrix();
        return i;
    }

    public void drawCenteredString(FontRenderer fontRenderer, String text, float x, float y, int color, boolean dropShadow) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.0D, 0.0D, getRenderZLevel() + 1.0D);
        fontRenderer.drawString(text, (int) x - fontRenderer.getStringWidth(text) / 2, (int) y, color, dropShadow);
        GL11.glPopMatrix();
    }


    public void drawSplitString(FontRenderer fontRenderer, String text, float x, float y, int wrapWidth, int color, boolean dropShadow) {
        for (Object o : fontRenderer.listFormattedStringToWidth(text, wrapWidth)) {
            String s = (String) o;
            drawString(fontRenderer, s, x, y, color, dropShadow);
            y += fontRenderer.FONT_HEIGHT;
        }
    }

    public void drawCenteredSplitString(FontRenderer fontRenderer, String str, float x, float y, int wrapWidth, int color, boolean dropShadow) {
        for (Object o : fontRenderer.listFormattedStringToWidth(str, wrapWidth)) {
            String s = (String) o;
            drawCenteredString(fontRenderer, s, x, y, color, dropShadow);
            y += fontRenderer.FONT_HEIGHT;
        }
    }

    public void drawHoveringText(List<String> textLines, int mouseX, int mouseY, FontRenderer font, int screenWidth, int screenHeight) {
        double oldOffset = this.zOffset;
        this.zOffset = 190.0D;
        drawHoveringText(textLines, mouseX, mouseY, font, screenWidth, screenHeight, -1);
        this.zOffset = oldOffset;
    }


    public void drawHoveringText(List<String> textLines, int mouseX, int mouseY, FontRenderer font, int screenWidth, int screenHeight, int maxTextWidth) {
        if (!textLines.isEmpty()) {
            GL11.glDisable(32826);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            int tooltipTextWidth = 0;

            for (String textLine : textLines) {
                int textLineWidth = font.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) {

                    if (mouseX > screenWidth / 2) {
                        tooltipTextWidth = mouseX - 12 - 8;
                    } else {
                        tooltipTextWidth = screenWidth - 16 - mouseX;
                    }
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap) {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = new ArrayList<>();
                for (int i = 0; i < textLines.size(); i++) {
                    String textLine = textLines.get(i);
                    List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                    if (i == 0) {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine) {
                        int lineWidth = font.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (mouseX > screenWidth / 2) {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                } else {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 12;

            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2;
                }
            }

            if (tooltipY + tooltipHeight + 6 > screenHeight) {
                tooltipY = screenHeight - tooltipHeight - 6;
            }

            if (tooltipY < 4) {
                tooltipY = 4;
            }

            this.zOffset++;
            int backgroundColor = -267386864;
            drawGradientRect((tooltipX - 3), (tooltipY - 4), (tooltipX + tooltipTextWidth + 3), (tooltipY - 3), -267386864, -267386864);
            drawGradientRect((tooltipX - 3), (tooltipY + tooltipHeight + 3), (tooltipX + tooltipTextWidth + 3), (tooltipY + tooltipHeight + 4), -267386864, -267386864);
            drawGradientRect((tooltipX - 3), (tooltipY - 3), (tooltipX + tooltipTextWidth + 3), (tooltipY + tooltipHeight + 3), -267386864, -267386864);
            drawGradientRect((tooltipX - 4), (tooltipY - 3), (tooltipX - 3), (tooltipY + tooltipHeight + 3), -267386864, -267386864);
            drawGradientRect((tooltipX + tooltipTextWidth + 3), (tooltipY - 3), (tooltipX + tooltipTextWidth + 4), (tooltipY + tooltipHeight + 3), -267386864, -267386864);
            int borderColorStart = 1347420415;
            int borderColorEnd = 1344798847;
            drawGradientRect((tooltipX - 3), (tooltipY - 3 + 1), (tooltipX - 3 + 1), (tooltipY + tooltipHeight + 3 - 1), 1347420415, 1344798847);
            drawGradientRect((tooltipX + tooltipTextWidth + 2), (tooltipY - 3 + 1), (tooltipX + tooltipTextWidth + 3), (tooltipY + tooltipHeight + 3 - 1), 1347420415, 1344798847);
            drawGradientRect((tooltipX - 3), (tooltipY - 3), (tooltipX + tooltipTextWidth + 3), (tooltipY - 3 + 1), 1347420415, 1347420415);
            drawGradientRect((tooltipX - 3), (tooltipY + tooltipHeight + 2), (tooltipX + tooltipTextWidth + 3), (tooltipY + tooltipHeight + 3), 1344798847, 1344798847);

            for (int lineNumber = 0; lineNumber < textLines.size(); lineNumber++) {
                String line = textLines.get(lineNumber);
                drawString(font, line, tooltipX, tooltipY, -1, true);


                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }
            this.zOffset--;

            GL11.glEnable(2896);
            GL11.glEnable(2929);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(32826);
        }
    }

    public void drawGradientRect(double left, double top, double right, double bottom, int colour1, int colour2) {
        double zLevel = getRenderZLevel();
        float alpha1 = (colour1 >> 24 & 0xFF) / 255.0F;
        float red1 = (colour1 >> 16 & 0xFF) / 255.0F;
        float green1 = (colour1 >> 8 & 0xFF) / 255.0F;
        float blue1 = (colour1 & 0xFF) / 255.0F;
        float alpha2 = (colour2 >> 24 & 0xFF) / 255.0F;
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
        tessellator.addVertex(right, top, getRenderZLevel());
        tessellator.addVertex(left, top, getRenderZLevel());
        tessellator.setColorRGBA_F(red2, green2, blue2, alpha2);
        tessellator.addVertex(left, bottom, getRenderZLevel());
        tessellator.addVertex(right, bottom, getRenderZLevel());
        tessellator.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }

    public void drawColouredRect(double posX, double posY, double xSize, double ySize, int colour) {
        drawGradientRect(posX, posY, posX + xSize, posY + ySize, colour, colour);
    }

    public void drawBorderedRect(double posX, double posY, double xSize, double ySize, double borderWidth, int fillColour, int borderColour) {
        drawColouredRect(posX, posY, xSize, borderWidth, borderColour);
        drawColouredRect(posX, posY + ySize - borderWidth, xSize, borderWidth, borderColour);
        drawColouredRect(posX, posY + borderWidth, borderWidth, ySize - 2.0D * borderWidth, borderColour);
        drawColouredRect(posX + xSize - borderWidth, posY + borderWidth, borderWidth, ySize - 2.0D * borderWidth, borderColour);
        drawColouredRect(posX + borderWidth, posY + borderWidth, xSize - 2.0D * borderWidth, ySize - 2.0D * borderWidth, fillColour);
    }
}
