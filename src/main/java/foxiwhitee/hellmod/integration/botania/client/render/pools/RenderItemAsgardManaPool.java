package foxiwhitee.hellmod.integration.botania.client.render.pools;

import foxiwhitee.hellmod.integration.botania.tile.pools.TileAsgardManaPool;
import foxiwhitee.hellmod.integration.botania.tile.pools.TileCustomManaPool;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RenderItemAsgardManaPool extends RenderItemCustomManaPool{
    public RenderItemAsgardManaPool(String name) {
        super(name);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        TileEntityRendererDispatcher.instance.renderTileEntityAt((TileEntity)new TileAsgardManaPool(), 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glPopMatrix();
    }
}
