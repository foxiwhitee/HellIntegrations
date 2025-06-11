package foxiwhitee.hellmod.blocks;

import appeng.block.AEBaseTileBlock;
import foxiwhitee.hellmod.HellCore;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockAENetwork extends AEBaseTileBlock {

    public BlockAENetwork(String name, Class<? extends TileEntity> tile) {
        super(Material.iron);
        setBlockName(name);
        setTileEntity(tile);
        setCreativeTab(HellCore.HELL_TAB);
        setHardness(1.0F);
        this.lightOpacity = 1;
    }
}