package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.modularelements;

import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.IModularGui;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.MGuiElementBase;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class MGuiBackground extends MGuiElementBase {
    public int textureX;
    public int textureY;
    public int textureSizeX = 256;
    public int textureSizeY = 256;
    public String texture;

    public MGuiBackground(IModularGui modularGui, int xPos, int yPos, int textureX, int textureY, int xSize, int ySize, String texture) {
        super(modularGui, xPos, yPos, xSize, ySize);
        this.textureX = textureX;
        this.textureY = textureY;
        this.texture = texture;
    }


    public void renderBackgroundLayer(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        super.renderBackgroundLayer(minecraft, mouseX, mouseY, partialTicks);
        bindTexture(new ResourceLocation(this.texture));
        drawModalRectWithCustomSizedTexture(this.xPos, this.yPos, this.textureX, this.textureY, this.xSize, this.ySize, this.textureSizeX, this.textureSizeY);
    }

    public MGuiBackground setTexturePos(int textureX, int textureY) {
        this.textureX = textureX;
        this.textureY = textureY;
        return this;
    }

    public MGuiBackground setTextureSize(int textureSize) {
        return setTextureSize(textureSize, textureSize);
    }

    public MGuiBackground setTexture(String texture) {
        this.texture = texture;
        return this;
    }

    public MGuiBackground setTextureSize(int textureSizeX, int textureSizeY) {
        this.textureSizeX = textureSizeX;
        this.textureSizeY = textureSizeY;
        return this;
    }
}
