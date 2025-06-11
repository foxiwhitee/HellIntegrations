package foxiwhitee.hellmod.blocks.assemblers;

import appeng.util.Platform;
import foxiwhitee.hellmod.proxy.CommonProxy;
import foxiwhitee.hellmod.tile.assemblers.TileBaseMolecularAssembler;
import foxiwhitee.hellmod.utils.helpers.RenderIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBaseMolecularAssembler extends BlockCustomMolecularAssembler{
    public BlockBaseMolecularAssembler(String name) {
        super(name);
        setTileEntity(TileBaseMolecularAssembler.class);
    }

    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileBaseMolecularAssembler tile = (TileBaseMolecularAssembler) world.getTileEntity(x, y, z);
        if (tile != null) {
            if (Platform.isServer())
                Platform.openGUI(player, (TileEntity) tile, ForgeDirection.getOrientation(side), CommonProxy.getGuiBaseMolecularAssembler());
            return true;
        }
        return false;
    }

    public int getRenderType() {
        return RenderIDs.BASE_MOLECULAR_ASSEMBLER.getId();
    }
}
