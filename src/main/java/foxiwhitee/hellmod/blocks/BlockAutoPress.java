package foxiwhitee.hellmod.blocks;


import appeng.block.AEBaseTileBlock;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.tile.TileAutoPress;
import net.minecraft.block.material.Material;

public class BlockAutoPress extends AEBaseTileBlock {

    public BlockAutoPress(String name) {
        super(Material.iron);
        this.setTileEntity( TileAutoPress.class );
        this.setBlockName(name);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setBlockTextureName(HellCore.MODID + ":ae2/" + name);

    }
}
