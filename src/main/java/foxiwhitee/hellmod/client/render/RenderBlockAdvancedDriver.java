package foxiwhitee.hellmod.client.render;

import appeng.util.Platform;
import cpw.mods.fml.common.FMLLog;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.tile.TileAdvancedDrive;
import foxiwhitee.hellmod.tile.assemblers.TileBaseMolecularAssembler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RenderBlockAdvancedDriver extends TileEntitySpecialRenderer {
    private static final ResourceLocation TOP_TEXTURE = new ResourceLocation(HellCore.MODID, "textures/blocks/ae2/adv_me_drive.png");
    private static final ResourceLocation BOTTOM_TEXTURE = new ResourceLocation(HellCore.MODID, "textures/blocks/ae2/adv_me_driveBottom.png");
    private static final ResourceLocation SIDE_TEXTURE = new ResourceLocation(HellCore.MODID, "textures/blocks/ae2/adv_me_driveSide.png");
    private static final ResourceLocation FRONT_TEXTURE = new ResourceLocation(HellCore.MODID, "textures/blocks/ae2/adv_me_driveFront.png");
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        if (!(te instanceof TileAdvancedDrive)) return;
        TileAdvancedDrive sp = (TileAdvancedDrive) te;
        ForgeDirection forward = sp.getForward();

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glTranslated(x, y, z);

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setBrightness(te.getWorldObj().getLightBrightnessForSkyBlocks(te.xCoord, te.yCoord, te.zCoord, 0));
        tess.setColorOpaque_I(0xFFFFFF);

        // Верхня грань (YPos)
        Minecraft.getMinecraft().renderEngine.bindTexture(TOP_TEXTURE);
        tess.setNormal(0, 1, 0);
        tess.addVertexWithUV(0, 1, 0, 0, 1);
        tess.addVertexWithUV(1, 1, 0, 1, 1);
        tess.addVertexWithUV(1, 1, 1, 1, 0);
        tess.addVertexWithUV(0, 1, 1, 0, 0);


        // Нижня грань (YNeg)
        Minecraft.getMinecraft().renderEngine.bindTexture(BOTTOM_TEXTURE);
        tess.setNormal(0, -1, 0);
        tess.addVertexWithUV(0, 0, 1, 0, 0);
        tess.addVertexWithUV(1, 0, 1, 1, 0);
        tess.addVertexWithUV(1, 0, 0, 1, 1);
        tess.addVertexWithUV(0, 0, 0, 0, 1);

        // Бічні грані
        for (ForgeDirection side : ForgeDirection.values()) {
            if (side == ForgeDirection.UP || side == ForgeDirection.DOWN) continue;
            ResourceLocation texture = (side == forward) ? FRONT_TEXTURE : SIDE_TEXTURE;

            Minecraft.getMinecraft().renderEngine.bindTexture(texture);

            switch (side) {
                case NORTH: // ZNeg
                    tess.setNormal(0, 0, -1);
                    tess.addVertexWithUV(0, 1, 0, 0, 0);
                    tess.addVertexWithUV(1, 1, 0, 1, 0);
                    tess.addVertexWithUV(1, 0, 0, 1, 1);
                    tess.addVertexWithUV(0, 0, 0, 0, 1);
                    break;
                case SOUTH: // ZPos
                    tess.setNormal(0, 0, 1);
                    tess.addVertexWithUV(0, 1, 1, 0, 0);
                    tess.addVertexWithUV(1, 1, 1, 1, 0);
                    tess.addVertexWithUV(1, 0, 1, 1, 1);
                    tess.addVertexWithUV(0, 0, 1, 0, 1);
                    break;
                case EAST: // XPos
                    tess.setNormal(1, 0, 0);
                    tess.addVertexWithUV(1, 1, 0, 0, 0);
                    tess.addVertexWithUV(1, 1, 1, 1, 0);
                    tess.addVertexWithUV(1, 0, 1, 1, 1);
                    tess.addVertexWithUV(1, 0, 0, 0, 1);

                    break;
                case WEST: // XNeg
                    tess.setNormal(-1, 0, 0);
                    tess.addVertexWithUV(0, 1, 1, 0, 0);
                    tess.addVertexWithUV(0, 1, 0, 1, 0);
                    tess.addVertexWithUV(0, 0, 0, 1, 1);
                    tess.addVertexWithUV(0, 0, 1, 0, 1);
                    break;
            }
        }

        tess.draw();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}