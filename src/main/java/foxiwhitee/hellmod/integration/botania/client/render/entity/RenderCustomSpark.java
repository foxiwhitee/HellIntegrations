package foxiwhitee.hellmod.integration.botania.client.render.entity;

import foxiwhitee.hellmod.integration.botania.entity.CustomSpark;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.item.ItemSpark;
import vazkii.botania.common.item.ItemSparkUpgrade;

import java.util.Random;

public class RenderCustomSpark<T extends Entity> extends RenderEntity {
    IIcon iicon = ItemSpark.worldIcon;

    public void doRender(Entity entity, double x, double y, double z, float size, float floatTicks) {
        CustomSpark spark = (CustomSpark)entity;
        IIcon iicon = ItemSpark.worldIcon;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glEnable(32826);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.05F);
        double time = (ClientTickHandler.ticksInGame + floatTicks);
        time += (new Random(entity.getEntityId())).nextInt();
        float a = 0.1F + (1 - entity.getDataWatcher().getWatchableObjectInt(27)) * 0.8F;
        setColor(spark.getColor(), (0.7F + 0.3F * (float)(Math.sin(time / 5.0D) + 0.5D) * 2.0F) * a);
        float scale = 0.75F + 0.1F * (float)Math.sin(time / 10.0D);
        GL11.glScalef(scale, scale, scale);
        bindEntityTexture(entity);
        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        float r = 180.0F - this.renderManager.playerViewY;
        GL11.glRotatef(r, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        drawIcon(tessellator, iicon);
        IIcon spinningIcon = getSpinningIcon(spark);
        if (spinningIcon != null) {
            GL11.glTranslatef(-0.02F + (float)Math.sin(time / 20.0D) * 0.2F, 0.24F + (float)Math.cos(time / 20.0D) * 0.2F, 0.005F);
            GL11.glScalef(0.2F, 0.2F, 0.2F);
            drawIcon(tessellator, spinningIcon);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042);
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    private void setColor(int color, float alpha) {
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        GL11.glColor4f(r, g, b, alpha);
    }

    private IIcon getSpinningIcon(CustomSpark entity) {
        int upgrade = entity.getUpgrade() - 1;
        return (upgrade >= 0 && upgrade < ItemSparkUpgrade.worldIcons.length) ? ItemSparkUpgrade.worldIcons[upgrade] : null;
    }

    protected ResourceLocation getEntityTexture(Entity entity) {
        return TextureMap.locationItemsTexture;
    }

    private void drawIcon(Tessellator tessellator, IIcon icon) {
        float f = icon.getMinU();
        float f2 = icon.getMaxU();
        float f3 = icon.getMinV();
        float f4 = icon.getMaxV();
        float f5 = 1.0F;
        float f6 = 0.5F;
        float f7 = 0.25F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.setBrightness(240);
        tessellator.addVertexWithUV((0.0F - f6), (0.0F - f7), 0.0D, f, f4);
        tessellator.addVertexWithUV((f5 - f6), (0.0F - f7), 0.0D, f2, f4);
        tessellator.addVertexWithUV((f5 - f6), (f5 - f7), 0.0D, f2, f3);
        tessellator.addVertexWithUV((0.0F - f6), (f5 - f7), 0.0D, f, f3);
        tessellator.draw();
    }
}
