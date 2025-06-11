package foxiwhitee.hellmod.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import foxiwhitee.hellmod.client.render.CableBusTextures;
import foxiwhitee.hellmod.helpers.HighlighterHandler;
import foxiwhitee.hellmod.tile.wireless.TileCustomWireless;
import foxiwhitee.hellmod.utils.wireless.ConnectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler {
    @SubscribeEvent
    public void updateTextureSheet(TextureStitchEvent.Pre ev) {
        if (ev.map.getTextureType() == 0)
            for (CableBusTextures cb : CableBusTextures.values())
                cb.registerIcon(ev.map);
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent renderWorldLastEvent) {
        HighlighterHandler.tick(renderWorldLastEvent);

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP thePlayer = minecraft.thePlayer;
        double n = thePlayer.lastTickPosX + (thePlayer.posX - thePlayer.lastTickPosX) * renderWorldLastEvent.partialTicks;
        double n2 = thePlayer.lastTickPosY + (thePlayer.posY - thePlayer.lastTickPosY) * renderWorldLastEvent.partialTicks;
        double n3 = thePlayer.lastTickPosZ + (thePlayer.posZ - thePlayer.lastTickPosZ) * renderWorldLastEvent.partialTicks;
        GL11.glPushMatrix();
        GL11.glTranslated(-n, -n2, -n3);
        MovingObjectPosition objectMouseOver = minecraft.objectMouseOver;
        if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            ConnectionUtil loc = new ConnectionUtil(objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ);
            TileEntity tileEntity = loc.getTile((IBlockAccess)minecraft.theWorld);
            if (tileEntity instanceof TileCustomWireless) {
                TileCustomWireless tileWireless = (TileCustomWireless)tileEntity;
                if (tileWireless.hasConnectionUtil()) {
                    GL11.glPushAttrib(8192);
                    GL11.glDisable(2896);
                    GL11.glDisable(3553);
                    GL11.glDisable(2929);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(4.0F);
                    GL11.glBegin(1);
                    GL11.glColor3f(255.0F, 0.0F, 0.0F);
                    GL11.glVertex3d(loc.x + 0.5D, loc.y + 0.5D, loc.z + 0.5D);
                    GL11.glVertex3d(tileWireless.connectionUtil.x + 0.5D, tileWireless.connectionUtil.y + 0.5D, tileWireless.connectionUtil.z + 0.5D);
                    GL11.glEnd();
                    GL11.glPopAttrib();
                }
            }
        }
        GL11.glPopMatrix();
    }
}
