package foxiwhitee.hellmod.integration.thaumcraft.client.render;

import foxiwhitee.hellmod.client.render.TileEntitySpecialRendererObjWrapper;
import foxiwhitee.hellmod.integration.thaumcraft.tile.TileStabilizer;
import foxiwhitee.hellmod.tile.assemblers.TileBaseMolecularAssembler;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class RenderBlockStabilizer extends TileEntitySpecialRendererObjWrapper<TileStabilizer> {
    public RenderBlockStabilizer() {
        super(TileStabilizer.class, "models/stabilizer.obj", "textures/blocks/stabilizer.png");
        createList("all");
    }

    public void renderAt(TileStabilizer tileEntity, double x, double y, double z, double f) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        GL11.glRotated((tileEntity.getWorldObj().getTotalWorldTime() % 2800) * 10.0D, 0.0F, -1.0F, 0.0F);
        bindTexture();
        renderPart("all");
        GL11.glPopMatrix();

    }
}