package foxiwhitee.hellmod.blocks.assemblers;

import appeng.block.AEBaseTileBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockCustomMolecularAssembler extends AEBaseTileBlock {
    public BlockCustomMolecularAssembler(String name) {
        super(Material.iron);
        setHardness(1.0F);
        setBlockName(name);
        this.isOpaque = false;
        this.lightOpacity = 1;

    }

    public abstract boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ);

    private static boolean boolAlphaPass = false;

    public static boolean isBooleanAlphaPass() {
        return boolAlphaPass;
    }

    private static void setBooleanAlphaPass(boolean booleanAlphaPass) {
        boolAlphaPass = booleanAlphaPass;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    public abstract int getRenderType();

    public int getRenderBlockPass() {
        return 1;
    }

    public boolean canRenderInPass(int pass) {
        setBooleanAlphaPass((pass == 1));
        return (pass == 0 || pass == 1);
    }
}