package foxiwhitee.hellmod.integration.draconic.client.render;

import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.entity.EntityArialHeart;
import foxiwhitee.hellmod.integration.draconic.entity.EntityChaoticHeart;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderArialHeart extends Render {
    private static final ResourceLocation texture = new ResourceLocation(HellCore.MODID + ":" + "textures/items/draconic/arialHeart.png");
    private static final int FRAME_COUNT = 12;
    private static final float FRAME_TIME = 16.0F;

    private void doRender(EntityArialHeart entity, double x, double y, double z, float f) {
        float sine = (float) Math.abs(Math.cos(ClientEventHandler.elapsedTicks / 1000D));


        {
            GL11.glRotatef(entity.rotation + f * entity.rotationInc, 0f, 1f, 0f);
            EntityItem itemEntity = new EntityItem(entity.worldObj, 0, 0, 0, new ItemStack(DraconicEvolutionIntegration.arialHeart));
            itemEntity.hoverStart = 0.0F;
            GL11.glScalef(2F, 2F, 2F);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, sine * 100f, sine * 100f);
            RenderItem.renderInFrame = true;
            RenderManager.instance.renderEntityWithPosYaw(itemEntity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            RenderItem.renderInFrame = false;
        }
        {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200f, 200f);
            Tessellator tess = Tessellator.instance;
            bindTexture(texture);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glScalef(0.55f, 0.55f, 0.55f);
            GL11.glTranslated(-0.5, -0.15, 0.05);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1f, 1f, 1f, 1f);

            int frame = (int) ((ClientEventHandler.elapsedTicks / FRAME_TIME) % FRAME_COUNT);
            float vMin = frame / (float) FRAME_COUNT;
            float vMax = (frame + 1) / (float) FRAME_COUNT;

            tess.startDrawingQuads();
            tess.addVertexWithUV(0, 1, 0, 0, vMin);
            tess.addVertexWithUV(0, 0, 0, 0, vMax);
            tess.addVertexWithUV(1, 0, 0, 1, vMax);
            tess.addVertexWithUV(1, 1, 0, 1, vMin);
            tess.draw();

            GL11.glTranslated(0, 0, -0.12);

            tess.startDrawingQuads();
            tess.addVertexWithUV(0, 1, 0, 0, vMin);
            tess.addVertexWithUV(0, 0, 0, 0, vMax);
            tess.addVertexWithUV(1, 0, 0, 1, vMax);
            tess.addVertexWithUV(1, 1, 0, 1, vMin);
            tess.draw();

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return null;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f1, float f2) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        doRender((EntityArialHeart) entity, x, y, z, f2);
        GL11.glPopMatrix();
    }
}