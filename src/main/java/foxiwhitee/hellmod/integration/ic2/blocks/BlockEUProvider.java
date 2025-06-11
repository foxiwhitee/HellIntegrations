package foxiwhitee.hellmod.integration.ic2.blocks;

import appeng.block.AEBaseBlock;
import appeng.block.AEBaseTileBlock;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.tile.TileEUProvider;
import foxiwhitee.hellmod.utils.helpers.RenderIDs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEUProvider extends AEBaseTileBlock {

    public BlockEUProvider(String name) {
        super(Material.iron);
        this.isOpaque = false;
        setBlockName(name);
        setTileEntity(TileEUProvider.class);
        setCreativeTab(HellCore.HELL_TAB);
        setHardness(1.0F);
        this.lightOpacity = 1;
    }

    public int getRenderType() {
        return RenderIDs.EU_ENERGY_PROVIDER.getId();
    }

    public boolean isBlockNormalCube() {
        return false;
    }

}