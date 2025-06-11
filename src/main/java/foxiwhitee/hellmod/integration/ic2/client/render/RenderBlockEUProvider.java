package foxiwhitee.hellmod.integration.ic2.client.render;

import foxiwhitee.hellmod.client.render.TileEntitySpecialRendererObjWrapper;
import foxiwhitee.hellmod.integration.ic2.tile.TileEUProvider;
import foxiwhitee.hellmod.tile.assemblers.TileBaseMolecularAssembler;
import org.lwjgl.opengl.GL11;

public class RenderBlockEUProvider extends TileEntitySpecialRendererObjWrapper<TileEUProvider> {
    public RenderBlockEUProvider() {
        super(TileEUProvider.class, "models/energy_provider.obj", "textures/blocks/ic2/energy_provider.png");
        createList("all");
    }

    public void renderAt(TileEUProvider tileEntity, double x, double y, double z, double f) {
        GL11.glPushMatrix();
        GL11.glEnable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(x, y, z);
        GL11.glPushMatrix();
        GL11.glScaled(1.0D, 1.0D, 1.0D);
        bindTexture();
        GL11.glTranslated(0.5D, 0.0D, 0.5D);
        renderPart("all");
        GL11.glDisable(3008);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}