package foxiwhitee.hellmod.integration.botania.client.render.spreaders;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.tile.spreaders.TileCustomSpreader;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.ModelSpreader;
import vazkii.botania.client.render.item.RenderLens;

import java.awt.*;
import java.util.Random;

public class RenderCustomSpreader  extends TileEntitySpecialRenderer {
    private ResourceLocation texture;

    private static final ModelSpreader model = new ModelSpreader();

    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float ticks) {
        TileCustomSpreader spreader = (TileCustomSpreader)tileentity;
        texture = new ResourceLocation(HellCore.MODID, "textures/blocks/botania/" + spreader.getName() + ".png");
        GL11.glPushMatrix();
        GL11.glEnable(32826);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(0.5F, 1.5F, 0.5F);
        GL11.glRotatef(spreader.rotationX + 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, -1.0F, 0.0F);
        GL11.glRotatef(spreader.rotationY, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, 1.0F, 0.0F);
        (Minecraft.getMinecraft()).renderEngine.bindTexture(texture);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        double time = (ClientTickHandler.ticksInGame + ticks);
        if (spreader.isULTRA_SPREADER()) {
            Color color = Color.getHSBColor((float)((time * 5.0D + (new Random((spreader.xCoord ^ spreader.yCoord ^ spreader.zCoord))).nextInt(10000)) % 360.0D) / 360.0F, 0.4F, 0.9F);
            GL11.glColor3f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
        }
        model.render();
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        double worldTicks = (tileentity.getWorldObj() == null) ? 0.0D : time;
        GL11.glRotatef((float)worldTicks % 360.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, (float)Math.sin(worldTicks / 20.0D) * 0.05F, 0.0F);
        model.renderCube();
        GL11.glPopMatrix();
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        ItemStack stack = spreader.getStackInSlot(0);
        if (stack != null) {
            (Minecraft.getMinecraft()).renderEngine.bindTexture(TextureMap.locationItemsTexture);
            ILens lens = (ILens)stack.getItem();
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.4F, -1.4F, -0.4375F);
            GL11.glScalef(0.8F, 0.8F, 0.8F);
            RenderLens.render(stack, lens.getLensColor(stack));
            GL11.glPopMatrix();
        }
        if (spreader.paddingColor != -1) {
            (Minecraft.getMinecraft()).renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            Block block = Blocks.carpet;
            int color2 = spreader.paddingColor;
            RenderBlocks render = RenderBlocks.getInstance();
            float f = 0.0625F;
            GL11.glTranslatef(0.0F, -f, 0.0F);
            render.renderBlockAsItem(block, color2, 1.0F);
            GL11.glTranslatef(0.0F, -f * 15.0F, 0.0F);
            render.renderBlockAsItem(block, color2, 1.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glPushMatrix();
            GL11.glScalef(f * 14.0F, 1.0F, 1.0F);
            render.renderBlockAsItem(block, color2, 1.0F);
            GL11.glPopMatrix();
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, -f / 2.0F);
            GL11.glScalef(f * 14.0F, 1.0F, f * 15.0F);
            render.renderBlockAsItem(block, color2, 1.0F);
            GL11.glTranslatef(0.0F, f * 15.0F, 0.0F);
            render.renderBlockAsItem(block, color2, 1.0F);
        }
        GL11.glEnable(32826);
        GL11.glPopMatrix();
    }
}
