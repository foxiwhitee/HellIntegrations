package foxiwhitee.hellmod.integration.draconic.client.render;

import foxiwhitee.hellmod.client.render.TileEntitySpecialRendererObjWrapper;
import foxiwhitee.hellmod.integration.draconic.client.render.effect.EffectTrackerFusionCrafting;
import foxiwhitee.hellmod.integration.draconic.tile.TileFusionCraftingCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RenderTileFusionCraftingCore extends TileEntitySpecialRendererObjWrapper<TileFusionCraftingCore> {
    public RenderTileFusionCraftingCore() {
        super(TileFusionCraftingCore.class, "models/fusion_crafting_core.obj", "textures/models/fusion_crafting_core.png");
        createList("all");
    }

    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float particalTicks) {
        super.renderTileEntityAt(tile, x, y, z, particalTicks);
    }

    public void renderAt(TileFusionCraftingCore tileEntity, double x, double y, double z, double f) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glPushMatrix();
        bindTexture();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        GL11.glTranslated(1.0D, 1.0D, 1.0D);
        renderPart("all");
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        EntityClientPlayerMP entityClientPlayerMP = (Minecraft.getMinecraft()).thePlayer;
        if (entityClientPlayerMP == null)
            return;
        EffectTrackerFusionCrafting.interpPosX = ((EntityPlayerSP)entityClientPlayerMP).lastTickPosX + (((EntityPlayerSP)entityClientPlayerMP).posX - ((EntityPlayerSP)entityClientPlayerMP).lastTickPosX) * f;
        EffectTrackerFusionCrafting.interpPosY = ((EntityPlayerSP)entityClientPlayerMP).lastTickPosY + (((EntityPlayerSP)entityClientPlayerMP).posY - ((EntityPlayerSP)entityClientPlayerMP).lastTickPosY) * f;
        EffectTrackerFusionCrafting.interpPosZ = ((EntityPlayerSP)entityClientPlayerMP).lastTickPosZ + (((EntityPlayerSP)entityClientPlayerMP).posZ - ((EntityPlayerSP)entityClientPlayerMP).lastTickPosZ) * f;
        tileEntity.renderEffects((float)f);
    }
}

