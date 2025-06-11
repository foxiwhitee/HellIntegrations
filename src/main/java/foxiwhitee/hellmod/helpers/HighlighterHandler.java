package foxiwhitee.hellmod.helpers;

import java.util.List;

import foxiwhitee.hellmod.client.render.BlockPosHighlighter;
import foxiwhitee.hellmod.utils.coord.CustomDimensionalCoord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class HighlighterHandler {
    public static void tick(RenderWorldLastEvent event) {
        renderHilightedBlock(event);
    }

    private static void renderHilightedBlock(RenderWorldLastEvent event) {
        List<CustomDimensionalCoord> list = BlockPosHighlighter.getHighlightedBlocks();
        if (list.isEmpty())
            return;
        Minecraft mc = Minecraft.getMinecraft();
        int dimension = mc.theWorld.provider.dimensionId;
        long time = System.currentTimeMillis();
        EntityClientPlayerMP entityClientPlayerMP = mc.thePlayer;
        double doubleX = ((EntityPlayerSP)entityClientPlayerMP).lastTickPosX + (((EntityPlayerSP)entityClientPlayerMP).posX - ((EntityPlayerSP)entityClientPlayerMP).lastTickPosX) * event.partialTicks;
        double doubleY = ((EntityPlayerSP)entityClientPlayerMP).lastTickPosY + (((EntityPlayerSP)entityClientPlayerMP).posY - ((EntityPlayerSP)entityClientPlayerMP).lastTickPosY) * event.partialTicks;
        double doubleZ = ((EntityPlayerSP)entityClientPlayerMP).lastTickPosZ + (((EntityPlayerSP)entityClientPlayerMP).posZ - ((EntityPlayerSP)entityClientPlayerMP).lastTickPosZ) * event.partialTicks;
        if ((time / 500L & 0x1L) == 0L)
            return;
        if (time > BlockPosHighlighter.getExpireHighlight())
            BlockPosHighlighter.clear();
        for (CustomDimensionalCoord c : (CustomDimensionalCoord[])list.<CustomDimensionalCoord>toArray(new CustomDimensionalCoord[0])) {
            if (dimension != c.getDimension()) {
                BlockPosHighlighter.remove(c);
                if (BlockPosHighlighter.getHighlightedBlocks().isEmpty())
                    return;
            }
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glLineWidth(3.0F);
            GL11.glTranslated(-doubleX, -doubleY, -doubleZ);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
            renderHighLightedBlocksOutline(c.x, c.y, c.z);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

    static void renderHighLightedBlocksOutline(double x, double y, double z) {
        GL11.glBegin(3);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + 1.0D, z);
        GL11.glVertex3d(x, y + 1.0D, z + 1.0D);
        GL11.glVertex3d(x, y, z + 1.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x + 1.0D, y, z);
        GL11.glVertex3d(x + 1.0D, y + 1.0D, z);
        GL11.glVertex3d(x + 1.0D, y + 1.0D, z + 1.0D);
        GL11.glVertex3d(x + 1.0D, y, z + 1.0D);
        GL11.glVertex3d(x + 1.0D, y, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x + 1.0D, y, z);
        GL11.glVertex3d(x + 1.0D, y, z + 1.0D);
        GL11.glVertex3d(x, y, z + 1.0D);
        GL11.glVertex3d(x, y + 1.0D, z + 1.0D);
        GL11.glVertex3d(x + 1.0D, y + 1.0D, z + 1.0D);
        GL11.glVertex3d(x + 1.0D, y + 1.0D, z);
        GL11.glVertex3d(x + 1.0D, y, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x + 1.0D, y, z);
        GL11.glVertex3d(x + 1.0D, y + 1.0D, z);
        GL11.glVertex3d(x, y + 1.0D, z);
        GL11.glVertex3d(x, y + 1.0D, z + 1.0D);
        GL11.glVertex3d(x + 1.0D, y + 1.0D, z + 1.0D);
        GL11.glVertex3d(x + 1.0D, y, z + 1.0D);
        GL11.glVertex3d(x, y, z + 1.0D);
        GL11.glEnd();
    }
}
