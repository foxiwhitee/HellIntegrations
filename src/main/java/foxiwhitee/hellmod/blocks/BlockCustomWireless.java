package foxiwhitee.hellmod.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.tile.wireless.*;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCustomWireless extends Block implements ITileEntityProvider {
    protected IIcon[] icons = new IIcon[6];
    private String name;
    public BlockCustomWireless(String name) {
        super(Material.iron);
        this.name = name;
        setHardness(1.0F);
        setBlockName(name);
    }

    public void registerBlockIcons(IIconRegister iconRegistry) {
        this.icons[0] = iconRegistry.registerIcon(HellCore.MODID + ":ae2/wireless/" + name + "_side_off");
        this.icons[1] = iconRegistry.registerIcon(HellCore.MODID + ":ae2/wireless/" + name + "_top_off");
        this.icons[2] = iconRegistry.registerIcon(HellCore.MODID + ":ae2/wireless/" + name + "_side_on");
        this.icons[3] = iconRegistry.registerIcon(HellCore.MODID + ":ae2/wireless/" + name + "_top_on");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        int id = ((meta == 0) ? 0 : ((meta == 1) ? 2 : 4)) + ((side <= 1) ? 1 : 0);
        return this.icons[id];
    }

    public void breakBlock(World w, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
        TileEntity tile = w.getTileEntity(x, y, z);
        if (tile instanceof TileCustomWireless) {
            TileCustomWireless te = (TileCustomWireless)tile;
            te.breakConnection();
            te.doUnlink();
        }
        super.breakBlock(w, x, y, z, p_149749_5_, p_149749_6_);
    }

    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        switch (name) {
            case "wirelessBimare": return new TileBimareWireless();
            case "wirelessDefit": return new TileDefitWireless();
            case "wirelessEfrim": return new TileEfrimWireless();
            case "wirelessNur": return new TileNurWireless();
            case "wirelessXaur": return new TileXaurWireless();
            default: return new TileAliteWireless();
        }
    }
}