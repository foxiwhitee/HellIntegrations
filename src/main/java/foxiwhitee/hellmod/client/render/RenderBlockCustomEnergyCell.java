package foxiwhitee.hellmod.client.render;

import appeng.api.implementations.items.IAEItemPowerStorage;
import appeng.client.render.BaseBlockRender;
import foxiwhitee.hellmod.blocks.BlockCustomEnergyCell;
import foxiwhitee.hellmod.tile.energycell.TileCustomEnergyCell;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

public class RenderBlockCustomEnergyCell extends BaseBlockRender<BlockCustomEnergyCell, TileCustomEnergyCell> {
    public RenderBlockCustomEnergyCell() {
        super(false, (double)20.0F);
    }

    public void renderInventory(BlockCustomEnergyCell blk, ItemStack is, RenderBlocks renderer, IItemRenderer.ItemRenderType type, Object[] obj) {
        IAEItemPowerStorage myItem = (IAEItemPowerStorage)is.getItem();
        double internalCurrentPower = myItem.getAECurrentPower(is);
        double internalMaxPower = myItem.getAEMaxPower(is);
        int meta = (int)((double)8.0F * (internalCurrentPower / internalMaxPower));
        if (meta > 7) {
            meta = 7;
        }

        if (meta < 0) {
            meta = 0;
        }

        renderer.setOverrideBlockTexture(blk.getIcon(0, meta));
        super.renderInventory(blk, is, renderer, type, obj);
        renderer.setOverrideBlockTexture((IIcon)null);
    }

    public boolean renderInWorld(BlockCustomEnergyCell blk, IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {
        int meta = world.getBlockMetadata(x, y, z);
        renderer.overrideBlockTexture = blk.getIcon(0, meta);
        boolean out = renderer.renderStandardBlock(blk, x, y, z);
        renderer.overrideBlockTexture = null;
        return out;
    }
}