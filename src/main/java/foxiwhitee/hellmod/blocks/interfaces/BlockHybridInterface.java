package foxiwhitee.hellmod.blocks.interfaces;

import appeng.core.features.AEFeature;
import appeng.util.Platform;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.proxy.CommonProxy;
import foxiwhitee.hellmod.tile.interfaces.TileHybridInterface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

public class BlockHybridInterface extends BlockAdvancedInterface {

    public BlockHybridInterface() {
        super();
        this.setTileEntity(TileHybridInterface.class);
        this.setFeature(EnumSet.of(AEFeature.Core));
        setBlockName("hybrid_interface");
    }

    @SideOnly(Side.CLIENT)
    protected String getTextureName() {
        return HellCore.MODID + ":ae2/" + this.getUnlocalizedName().replace("tile.", "");
    }

    @Override
    public boolean onActivated(World w, int x, int y, int z, EntityPlayer p, int side, float hitX, float hitY, float hitZ) {
        if (p.isSneaking()) {
            return false;
        }
        TileHybridInterface tg = this.getTileEntity(w, x, y, z);
        if (tg != null) {
            if (Platform.isServer()) {
                Platform.openGUI(p, tg, ForgeDirection.getOrientation(side), CommonProxy.getGuiHybridInterface());
            }
            return true;
        }
        return false;
    }

    @Override
    public void registerBlockIconsSpecial(IIconRegister register) {
        this.icons[0] = register.registerIcon(HellCore.MODID + ":ae2/hybrid_interface");
        this.icons[1] = register.registerIcon(HellCore.MODID + ":ae2/hybrid_interface_alternate");
        this.icons[2] = register.registerIcon(HellCore.MODID + ":ae2/hybrid_interface_alternate_arrow");
    }
}
