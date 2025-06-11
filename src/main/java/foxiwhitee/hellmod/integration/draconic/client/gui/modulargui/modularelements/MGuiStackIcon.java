package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.modularelements;

import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.IModularGui;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.MGuiElementBase;

import java.util.List;

import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;


public class MGuiStackIcon
        extends MGuiElementBase {
    public boolean drawCount = true;
    public boolean drawToolTip = true;
    public boolean drawHoverHighlight = false;
    public int xOffset = 0;
    public int yOffset = 0;
    protected List<String> toolTipOverride = null;
    private MGuiElementBase background = null;
    private ItemStack stackReference;

    public MGuiStackIcon(IModularGui modularGui, int xPos, int yPos, int xSize, int ySize, ItemStack stackReference) {
        super(modularGui, xPos, yPos, xSize, ySize);
        this.stackReference = OreDictUtil.isEmpty(stackReference) ? new ItemStack(Blocks.bedrock) : stackReference;
    }

    public MGuiStackIcon(IModularGui modularGui, int xPos, int yPos, ItemStack stackReference) {
        super(modularGui, xPos, yPos);
        this.stackReference = OreDictUtil.isEmpty(stackReference) ? new ItemStack(Blocks.bedrock) : stackReference;
        this.xSize = this.ySize = 18;
    }


    public void renderBackgroundLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        super.renderBackgroundLayer(minecraft, mouseX, mouseY, partialTicks);
        if (this.drawHoverHighlight && isMouseOver(mouseX, mouseY)) {
            drawColouredRect(this.xPos, this.yPos, this.xSize, this.ySize, -2130706433);
        }
        if (OreDictUtil.isEmpty(getStack()))
            return;
        GL11.glPushMatrix();
        double scaledWidth = this.xSize / 18.0D;
        double scaledHeight = this.ySize / 18.0D;

        GL11.glTranslated(this.xPos + scaledWidth + this.xOffset, this.yPos + scaledHeight + this.yOffset, getRenderZLevel() - 80.0D);
        GL11.glScaled(scaledWidth, scaledHeight, 1.0D);
        itemRender.zLevel = 300.0F;
        GL11.glEnable(2929);
        itemRender.renderItemAndEffectIntoGUI(minecraft.fontRenderer, minecraft.getTextureManager(), getStack(), 0, 0);
        itemRender.zLevel = 0.0F;
        itemRender.renderItemIntoGUI(minecraft.fontRenderer, minecraft.getTextureManager(), getStack(), 0, 0);

        if (this.drawCount && (getStack()).stackSize > 1) {
            String s = (getStack()).stackSize + "";
            GL11.glTranslated(0.0D, 0.0D, -(getRenderZLevel() - 80.0D));
            this.zOffset = 45.0D;
            drawString(minecraft.fontRenderer, s, (this.xSize - minecraft.fontRenderer.getStringWidth(s) - 1), minecraft.fontRenderer.FONT_HEIGHT, 16777215, true);
            this.zOffset = 0.0D;
        }

        GL11.glPopMatrix();
    }


    public boolean renderOverlayLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        if (isMouseOver(mouseX - this.xOffset, mouseY - this.yOffset) && (this.drawToolTip || this.toolTipOverride != null)) {
            List<String> list = (this.toolTipOverride != null) ? this.toolTipOverride : getStack().getTooltip((EntityPlayer) minecraft.thePlayer, minecraft.gameSettings.advancedItemTooltips);
            drawHoveringText(list, mouseX, mouseY, minecraft.fontRenderer, this.modularGui.screenWidth(), this.modularGui.screenHeight());
            return true;
        }
        return super.renderOverlayLayer(minecraft, mouseX, mouseY, partialTicks);
    }


    public MGuiStackIcon setBackground(MGuiElementBase background) {
        if (background == null) {
            if (this.background != null) {
                removeChild(this.background);
                this.background = null;
            }
        } else {
            if (this.background != null) {
                removeChild(this.background);
            }
            this.background = background;
            background.xPos = this.xPos;
            background.yPos = this.yPos;
            background.xSize = this.xSize;
            background.ySize = this.ySize;
            addChild(background);
        }

        return this;
    }


    public MGuiStackIcon setToolTip(boolean drawToolTip) {
        this.drawToolTip = drawToolTip;
        return this;
    }

    public ItemStack getStack() {
        return OreDictUtil.isEmpty(this.stackReference) ? new ItemStack(Blocks.bedrock) : this.stackReference;
    }

    public MGuiStackIcon setStack(ItemStack stackReference) {
        this.stackReference = stackReference;
        return this;
    }

    public MGuiStackIcon setDrawHoverHighlight(boolean drawHoverHighlight) {
        this.drawHoverHighlight = drawHoverHighlight;
        return this;
    }
}
