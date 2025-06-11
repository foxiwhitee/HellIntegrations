package foxiwhitee.hellmod.integration.draconic.client.render;

import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.tile.TileCustomUpgradeModifier;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RenderCustomUpgradeModifier extends TileEntitySpecialRenderer {
    private static float pxl = 0.00390625F;

    public RenderCustomUpgradeModifier() {
    }

    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        TileCustomUpgradeModifier tile = (TileCustomUpgradeModifier)tileEntity;
        this.renderBlock(tile, f);
        GL11.glPopMatrix();
    }

    public void renderBlock(TileCustomUpgradeModifier tile, float pt) {
        Tessellator tess = Tessellator.instance;
        tess.setColorRGBA(255, 255, 255, 255);
        DraconicEvolutionIntegration.bindResource("textures/models/customUpgradeModifierGear.png");
        GL11.glPushMatrix();
        GL11.glEnable(3008);
        GL11.glScaled(0.8, 0.8, 0.8);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(-0.375F, -0.375F, -0.47F);
        GL11.glTranslated((double)1.0F, (double)1.0F, (double)0.0F);
        GL11.glRotatef(tile.rotation + pt * tile.rotationSpeed, 0.0F, 0.0F, 1.0F);
        GL11.glTranslated((double)-1.0F, (double)-1.0F, (double)0.0F);
        render2DWithThicness(tess, 1.0F, 0.0F, 0.0F, 1.0F, 128, 128, 0.0625F);
        GL11.glPopMatrix();
        GL11.glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glScaled(0.4, 0.4, 0.4);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.25F, 0.25F, -0.945F);
        GL11.glTranslated((double)1.0F, (double)1.0F, (double)0.0F);
        GL11.glRotatef(-tile.rotation + pt * -tile.rotationSpeed, 0.0F, 0.0F, 1.0F);
        GL11.glTranslated((double)-1.0F, (double)-1.0F, (double)0.0F);
        render2DWithThicness(tess, 1.0F, 0.0F, 0.0F, 1.0F, 128, 128, 0.0625F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        this.drawBase(tess);
        this.renderChargingItem(tile, pt);
    }

    private void drawBase(Tessellator tess) {
        DraconicEvolutionIntegration.bindResource("textures/models/customUpgradeModifier.png");
        GL11.glPushMatrix();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, 1.0F);
        double srcXMin = (double)0.0F;
        double srcYMin = (double)0.0F;
        double srcXMax = (double)64.0F * (double)pxl;
        double srcYMax = (double)64.0F * (double)pxl;
        tess.addVertexWithUV((double)0.0F, 5.0E-4, (double)0.0F, srcXMin, srcYMin);
        tess.addVertexWithUV((double)1.0F, 5.0E-4, (double)0.0F, srcXMax, srcYMin);
        tess.addVertexWithUV((double)1.0F, 5.0E-4, (double)1.0F, srcXMax, srcYMax);
        tess.addVertexWithUV((double)0.0F, 5.0E-4, (double)1.0F, srcXMin, srcYMax);
        srcXMin = (double)128.0F * (double)pxl;
        srcYMin = (double)0.0F;
        srcXMax = (double)192.0F * (double)pxl;
        srcYMax = (double)64.0F * (double)pxl;
        tess.addVertexWithUV((double)0.0F, 0.3745, (double)0.0F, srcXMin, srcYMin);
        tess.addVertexWithUV((double)0.0F, 0.3745, (double)1.0F, srcXMax, srcYMin);
        tess.addVertexWithUV((double)1.0F, 0.3745, (double)1.0F, srcXMax, srcYMax);
        tess.addVertexWithUV((double)1.0F, 0.3745, (double)0.0F, srcXMin, srcYMax);
        srcXMin = (double)64.0F * (double)pxl;
        srcYMin = (double)0.0F;
        srcXMax = (double)128.0F * (double)pxl;
        srcYMax = (double)24.0F * (double)pxl;
        tess.addVertexWithUV((double)1.0F, (double)0.0F, (double)0.0F, srcXMin, srcYMin);
        tess.addVertexWithUV((double)0.0F, (double)0.0F, (double)0.0F, srcXMax, srcYMin);
        tess.addVertexWithUV((double)0.0F, (double)0.375F, (double)0.0F, srcXMax, srcYMax);
        tess.addVertexWithUV((double)1.0F, (double)0.375F, (double)0.0F, srcXMin, srcYMax);
        tess.addVertexWithUV((double)1.0F, (double)0.0F, (double)1.0F, srcXMin, srcYMin);
        tess.addVertexWithUV((double)1.0F, (double)0.375F, (double)1.0F, srcXMin, srcYMax);
        tess.addVertexWithUV((double)0.0F, (double)0.375F, (double)1.0F, srcXMax, srcYMax);
        tess.addVertexWithUV((double)0.0F, (double)0.0F, (double)1.0F, srcXMax, srcYMin);
        tess.addVertexWithUV((double)0.0F, (double)0.375F, (double)1.0F, srcXMin, srcYMin);
        tess.addVertexWithUV((double)0.0F, (double)0.375F, (double)0.0F, srcXMax, srcYMin);
        tess.addVertexWithUV((double)0.0F, (double)0.0F, (double)0.0F, srcXMax, srcYMax);
        tess.addVertexWithUV((double)0.0F, (double)0.0F, (double)1.0F, srcXMin, srcYMax);
        tess.addVertexWithUV((double)1.0F, (double)0.375F, (double)1.0F, srcXMin, srcYMin);
        tess.addVertexWithUV((double)1.0F, (double)0.0F, (double)1.0F, srcXMin, srcYMax);
        tess.addVertexWithUV((double)1.0F, (double)0.0F, (double)0.0F, srcXMax, srcYMax);
        tess.addVertexWithUV((double)1.0F, (double)0.375F, (double)0.0F, srcXMax, srcYMin);
        tess.draw();
        GL11.glPopMatrix();
    }

    public static void render2DWithThicness(Tessellator tess, float maxU, float minV, float minU, float maxV, int width, int height, float thickness) {
        double pix = (double)0.015625F;
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, 1.0F);
        tess.addVertexWithUV((double)0.0F, (double)0.0F, (double)0.0F, (double)maxU, (double)maxV);
        tess.addVertexWithUV((double)width * pix, (double)0.0F, (double)0.0F, (double)minU, (double)maxV);
        tess.addVertexWithUV((double)width * pix, (double)height * pix, (double)0.0F, (double)minU, (double)minV);
        tess.addVertexWithUV((double)0.0F, (double)height * pix, (double)0.0F, (double)maxU, (double)minV);
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, -1.0F);
        tess.addVertexWithUV((double)0.0F, (double)height * pix, (double)(0.0F - thickness), (double)maxU, (double)minV);
        tess.addVertexWithUV((double)width * pix, (double)height * pix, (double)(0.0F - thickness), (double)minU, (double)minV);
        tess.addVertexWithUV((double)width * pix, (double)0.0F, (double)(0.0F - thickness), (double)minU, (double)maxV);
        tess.addVertexWithUV((double)0.0F, (double)0.0F, (double)(0.0F - thickness), (double)maxU, (double)maxV);
        tess.draw();
        float f5 = 0.5F * (maxU - minU) / (float)width;
        float f6 = 0.5F * (maxV - minV) / (float)height;
        tess.startDrawingQuads();
        tess.setNormal(-1.0F, 0.0F, 0.0F);

        for(int k = 0; k < width; ++k) {
            double d = (double)k * pix;
            float f7 = (float)k / (float)width;
            float f8 = maxU + (minU - maxU) * f7 - f5;
            tess.addVertexWithUV(d, (double)0.0F, (double)(0.0F - thickness), (double)f8, (double)maxV);
            tess.addVertexWithUV(d, (double)0.0F, (double)0.0F, (double)f8, (double)maxV);
            tess.addVertexWithUV(d, (double)height * pix, (double)0.0F, (double)f8, (double)minV);
            tess.addVertexWithUV(d, (double)height * pix, (double)(0.0F - thickness), (double)f8, (double)minV);
        }

        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(1.0F, 0.0F, 0.0F);

        for(int var17 = 0; var17 < width; ++var17) {
            double d = (double)(var17 + 1) * pix;
            float f7 = (float)var17 / (float)width;
            float f8 = maxU + (minU - maxU) * f7 - f5;
            tess.addVertexWithUV(d, (double)height * pix, (double)(0.0F - thickness), (double)f8, (double)minV);
            tess.addVertexWithUV(d, (double)height * pix, (double)0.0F, (double)f8, (double)minV);
            tess.addVertexWithUV(d, (double)0.0F, (double)0.0F, (double)f8, (double)maxV);
            tess.addVertexWithUV(d, (double)0.0F, (double)(0.0F - thickness), (double)f8, (double)maxV);
        }

        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 1.0F, 0.0F);

        for(int var18 = 0; var18 < height; ++var18) {
            double d = (double)(var18 + 1) * pix;
            float f7 = (float)var18 / (float)height;
            float f8 = maxV + (minV - maxV) * f7 - f6;
            tess.addVertexWithUV((double)0.0F, d, (double)0.0F, (double)maxU, (double)f8);
            tess.addVertexWithUV((double)width * pix, d, (double)0.0F, (double)minU, (double)f8);
            tess.addVertexWithUV((double)width * pix, d, (double)(0.0F - thickness), (double)minU, (double)f8);
            tess.addVertexWithUV((double)0.0F, d, (double)(0.0F - thickness), (double)maxU, (double)f8);
        }

        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, -1.0F, 0.0F);

        for(int var19 = 0; var19 < height; ++var19) {
            double d = (double)var19 * pix;
            float f7 = (float)var19 / (float)height;
            float f8 = maxV + (minV - maxV) * f7 - f6;
            tess.addVertexWithUV((double)width * pix, d, (double)0.0F, (double)minU, (double)f8);
            tess.addVertexWithUV((double)0.0F, d, (double)0.0F, (double)maxU, (double)f8);
            tess.addVertexWithUV((double)0.0F, d, (double)(0.0F - thickness), (double)maxU, (double)f8);
            tess.addVertexWithUV((double)width * pix, d, (double)(0.0F - thickness), (double)minU, (double)f8);
        }

        tess.draw();
    }

    public void renderChargingItem(TileCustomUpgradeModifier tile, float pt) {
        if (tile.getStackInSlot(0) != null) {
            GL11.glPushMatrix();
            ItemStack stack = tile.getStackInSlot(0);
            EntityItem itemEntity = new EntityItem(tile.getWorldObj(), (double)0.0F, (double)0.0F, (double)0.0F, tile.getStackInSlot(0));
            itemEntity.hoverStart = 0.0F;
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glRotatef((tile.rotation + pt * tile.rotationSpeed) * 0.2F, 0.0F, -1.0F, 0.0F);
            if (stack.getItem() instanceof ItemBlock) {
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                GL11.glTranslatef(0.0F, 0.045F, 0.0F);
            }

            RenderItem.renderInFrame = true;
            RenderManager.instance.renderEntityWithPosYaw(itemEntity, (double)0.0F, (double)0.0F, (double)0.0F, 0.0F, 0.0F);
            RenderItem.renderInFrame = false;
            GL11.glPopMatrix();
        }

    }
}