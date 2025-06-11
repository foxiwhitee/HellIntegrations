package foxiwhitee.hellmod.integration.botania.client.render.pools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.tile.pools.TileCustomManaPool;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.model.ModelPool;
import vazkii.botania.common.block.mana.BlockPool;

public class RenderCustomManaPool  extends TileEntitySpecialRenderer {
    private ResourceLocation texture;

    private static final ModelPool model = new ModelPool();

    public static int forceMeta = 0;

    public static boolean forceMana = false;

    public static int forceManaNumber = -1;

    RenderItem renderItem = new RenderItem();

    @SideOnly(Side.CLIENT)
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
        TileCustomManaPool pool = (TileCustomManaPool)tileentity;
        texture = new ResourceLocation(HellCore.MODID, "textures/blocks/botania/" + pool.getName() + ".png");GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(32826);
        float a = MultiblockRenderHandler.rendering ? 0.6F : 1.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, a);
        GL11.glTranslated(x, y, z);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GL11.glTranslatef(0.5F, 1.5F, 0.5F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        int color = pool.color;
        float[] acolor = EntitySheep.fleeceColorTable[color];
        GL11.glColor4f(acolor[0], acolor[1], acolor[2], a);
        model.render();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, a);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glEnable(32826);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        int mana = pool.getCurrentMana();
        if (forceManaNumber > -1)
            mana = forceManaNumber;
        int cap = pool.getMaxMana();
        float waterLevel = (float) mana / (float)cap * 0.4F;
        if (forceMana)
            waterLevel = 0.4F;
        float s = 0.0625F;
        float v = 0.125F;
        float w = -v * 3.5F;
        if (pool.getWorldObj() != null) {
            Block below = pool.getWorldObj().getBlock(pool.xCoord, pool.yCoord - 1, pool.zCoord);
            if (below instanceof IPoolOverlayProvider) {
                IIcon overlay = ((IPoolOverlayProvider)below).getIcon(pool.getWorldObj(), pool.xCoord, pool.yCoord - 1, pool.zCoord);
                if (overlay != null) {
                    GL11.glPushMatrix();
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glDisable(3008);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, a * (float)((Math.sin((double)((float)ClientTickHandler.ticksInGame + f) / (double)20.0F) + (double)1.0F) * 0.3 + 0.2));
                    GL11.glTranslatef(-0.5F, -1.4300001F, -0.5F);
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(s, s, s);
                    this.renderIcon(0, 0, overlay, 16, 16, 240);
                    GL11.glEnable(3008);
                    GL11.glDisable(3042);
                    GL11.glPopMatrix();
                }
            }
        }
        if (waterLevel > 0.0F) {
            s = 0.0546875F;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3008);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, a);
            GL11.glTranslatef(w, -1.0F - (0.43F - waterLevel), w);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(s, s, s);
            ShaderHelper.useShader(ShaderHelper.manaPool);
            this.renderIcon(0, 0, BlockPool.manaIcon, 16, 16, 240);
            ShaderHelper.releaseShader();
            GL11.glEnable(3008);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
        forceMeta = 0;
        forceMana = false;
        forceManaNumber = -1;
    }

    public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5, int brightness) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(brightness);
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par5), (double)0.0F, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + par5), (double)0.0F, (double)par3Icon.getMaxU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + 0), (double)0.0F, (double)par3Icon.getMaxU(), (double)par3Icon.getMinV());
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)0.0F, (double)par3Icon.getMinU(), (double)par3Icon.getMinV());
        tessellator.draw();
    }
}

