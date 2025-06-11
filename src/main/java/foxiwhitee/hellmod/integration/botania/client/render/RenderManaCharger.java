package foxiwhitee.hellmod.integration.botania.client.render;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.client.model.ModelCharger;
import foxiwhitee.hellmod.integration.botania.tile.TileManaCharger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderManaCharger extends TileEntitySpecialRenderer {
    public static ModelCharger MODEl_CHARGER = new ModelCharger();

    public static ResourceLocation TEXTURE_CHARGER = new ResourceLocation(HellCore.MODID + ":textures/blocks/botania/manaCharger.png");

    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float p_147500_8_) {
        double time = Minecraft.getSystemTime();
        TileManaCharger manaCharger = (TileManaCharger)tileEntity;
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y - 0.5D, z + 0.5D);
        GL11.glTranslated(0.0D, 1.1D, 0.0D);
        GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
        bindTexture(TEXTURE_CHARGER);
        GL11.glPushMatrix();
        GL11.glTranslated(0.0D, Math.cos(time / 750.0D) * 0.2D, 0.0D);
        GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
        MODEl_CHARGER.Shape1.render(1.0F);
        MODEl_CHARGER.Shape2.render(1.0F);
        MODEl_CHARGER.Shape3.render(1.0F);
        MODEl_CHARGER.Shape4.render(1.0F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
        GL11.glRotated(time / 50.0D, 0.0D, 1.0D, 0.0D);
        GL11.glTranslated(Math.sin(time / 100.0D) / 20.0D, 0.0D, Math.cos(time / 100.0D) / 20.0D);
        MODEl_CHARGER.Rot1.render(1.0F);
        MODEl_CHARGER.Rot2.render(1.0F);
        MODEl_CHARGER.Rot3.render(1.0F);
        MODEl_CHARGER.Rot4.render(1.0F);
        MODEl_CHARGER.PlateRot1.render(1.0F);
        MODEl_CHARGER.PlateRot2.render(1.0F);
        MODEl_CHARGER.PlateRot3.render(1.0F);
        MODEl_CHARGER.PlateRot4.render(1.0F);
        GL11.glPopMatrix();
        GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
        MODEl_CHARGER.render(1.0F);
        GL11.glPopMatrix();
        if (manaCharger.stack == null)
            return;
        float prevLGTX = OpenGlHelper.lastBrightnessX;
        float prevLGTY = OpenGlHelper.lastBrightnessY;
        char bright = 8;
        int brightX = bright % 65536;
        int brightY = bright / 65536;
        GL11.glPushAttrib(1048575);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.6D + 0.07500000298023224D, y + 1.01D, z + 0.5D);
        GL11.glTranslated(0.0D, -1.0D, 0.0D);
        GL11.glTranslated(0.0D, Math.cos(time / 750.0D) * 0.2D, 0.0D);
        GL11.glRotatef(90.0F, -0.825F, 0.0F, 1.0F);
        GL11.glScaled(0.25D, 0.25D, 0.25D);
        RenderHelper.disableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightX / 1.0F, brightY / 1.0F);
        (Minecraft.getMinecraft()).entityRenderer.itemRenderer.renderItem((EntityLivingBase)(Minecraft.getMinecraft()).thePlayer, manaCharger.stack, 0, IItemRenderer.ItemRenderType.INVENTORY);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevLGTX, prevLGTY);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }
}

