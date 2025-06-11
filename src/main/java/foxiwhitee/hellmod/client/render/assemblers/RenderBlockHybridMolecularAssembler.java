package foxiwhitee.hellmod.client.render.assemblers;

import foxiwhitee.hellmod.client.render.TileEntitySpecialRendererObjWrapper;
import foxiwhitee.hellmod.tile.assemblers.TileHybridMolecularAssembler;
import org.lwjgl.opengl.GL11;

public class RenderBlockHybridMolecularAssembler extends TileEntitySpecialRendererObjWrapper<TileHybridMolecularAssembler> {
    public RenderBlockHybridMolecularAssembler() {
        super(TileHybridMolecularAssembler.class, "models/molecular_assembler_interface.obj", "textures/blocks/ae2/hybrid_molecular_assembler.png");
        createList("all");
    }

    public void renderAt(TileHybridMolecularAssembler tileEntity, double x, double y, double z, double f) {
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