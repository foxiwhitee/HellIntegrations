package foxiwhitee.hellmod.integration.draconic.client.render;

import foxiwhitee.hellmod.client.render.TileEntitySpecialRendererObjWrapper;
import foxiwhitee.hellmod.integration.draconic.tile.TileDraconicAssembler;
import foxiwhitee.hellmod.tile.assemblers.TileBaseMolecularAssembler;
import org.lwjgl.opengl.GL11;

public class RenderBlockDraconicAssembler extends TileEntitySpecialRendererObjWrapper<TileDraconicAssembler> {
    public RenderBlockDraconicAssembler() {
        super(TileDraconicAssembler.class, "models/dragon_assembler.obj", "textures/blocks/draconic/dragon_assembler.png");
        createList("Body");
        createList("Down");
    }

    public void renderAt(TileDraconicAssembler tileEntity, double x, double y, double z, double f) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y, z + 0.5D);
        bindTexture();
        renderPart("Down");
        GL11.glPushMatrix();
        double time = (tileEntity.getWorldObj().getTotalWorldTime() + f) / 10.0D;
        GL11.glTranslated(0.0D, Math.cos(time) * 0.1D, 0.0D);
        GL11.glRotated(-((tileEntity.getWorldObj().getTotalWorldTime() * 4L % 360L)), 0.0D, 1.0D, 0.0D);
        renderPart("Body");
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}