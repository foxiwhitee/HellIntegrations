package foxiwhitee.hellmod.integration.botania.client.render.pools;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import foxiwhitee.hellmod.utils.helpers.RenderIDs;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public abstract class RenderItemCustomManaPool  implements ISimpleBlockRenderingHandler {
    public abstract void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer);
    private String name;

    public RenderItemCustomManaPool(String name) {
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
            case "asgardPool": return RenderIDs.ASGARD_MANA_POOL.getId();
            case "helhelmPool": return RenderIDs.HELHELM_MANA_POOL.getId();
            case "valhallaPool": return RenderIDs.VALHALLA_MANA_POOL.getId();
            case "midgardPool": return RenderIDs.MIDGARD_MANA_POOL.getId();
            default: return 0;
        }
    }
}
