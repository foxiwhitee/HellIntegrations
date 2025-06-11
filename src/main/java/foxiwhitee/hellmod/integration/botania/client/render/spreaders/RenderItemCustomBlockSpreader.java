package foxiwhitee.hellmod.integration.botania.client.render.spreaders;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import foxiwhitee.hellmod.utils.helpers.RenderIDs;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public abstract class RenderItemCustomBlockSpreader  implements ISimpleBlockRenderingHandler {
    public abstract void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer);
    private String name;
    public RenderItemCustomBlockSpreader(String name) {
        this.name = name;
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    public int getRenderId() {
        switch (name) {
            case "asgardSpreader": return RenderIDs.ASGARD_SPREADER.getId();
            case "helhelmSpreader": return RenderIDs.HELHELM_SPREADER.getId();
            case "valhallaSpreader": return RenderIDs.VALHALLA_SPREADER.getId();
            case "midgardSpreader": return RenderIDs.MIDGARD_SPREADER.getId();
            default: return 0;
        }
    }
}
