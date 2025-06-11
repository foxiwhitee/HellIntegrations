package foxiwhitee.hellmod.integration.thaumcraft.blocks;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.thaumcraft.tile.TileStabilizer;
import foxiwhitee.hellmod.utils.helpers.RenderIDs;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliser;

public class BlockStabilizer extends Block  implements ITileEntityProvider, IInfusionStabiliser {

    public BlockStabilizer(String name) {
        super(Material.iron);
        this.setBlockName(name);
        this.setCreativeTab(HellCore.HELL_TAB);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileStabilizer();
    }

    public int getRenderType() {
        return RenderIDs.STABILIZER.getId();
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean canStabaliseInfusion(World world, int i, int i1, int i2) {
        return true;
    }
}

