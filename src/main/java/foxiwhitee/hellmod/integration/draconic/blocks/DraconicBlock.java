package foxiwhitee.hellmod.integration.draconic.blocks;

import com.brandon3055.draconicevolution.common.ModBlocks;
import com.brandon3055.draconicevolution.common.blocks.BlockDE;
import com.brandon3055.draconicevolution.common.lib.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.itemBlock.BlockAwakenedDraconiumItemBlock;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class DraconicBlock extends BlockDE {
    IIcon[] icons = new IIcon[2];
    IIcon top;

    public DraconicBlock() {
        this.setHardness(10.0F);
        this.setResistance(500.0F);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setBlockName("draconicBlock");
        this.setHarvestLevel("pickaxe", 4);
        ModBlocks.register(this, BlockAwakenedDraconiumItemBlock.class);
    }

    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return entity instanceof EntityPlayer;
    }

    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {}

    public boolean canDropFromExplosion(Explosion p_149659_1_) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icons[0] = iconRegister.registerIcon(References.RESOURCESPREFIX + "draconic_block");
        this.icons[1] = iconRegister.registerIcon(HellCore.MODID + ":draconic/charged_awakened_dragon");
        this.top = iconRegister.registerIcon(References.RESOURCESPREFIX + "draconic_block_blank");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta == 0) {
            return side != 0 && side != 1 ? this.icons[0] : this.top;
        }
        return meta >= this.icons.length ? this.icons[0] : this.icons[meta];
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs p_149666_2_, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
        return true;
    }

    public boolean hasTileEntity(int metadata) {
        return false;
    }

    public int damageDropped(int meta) {
        return meta;
    }

    public float getEnchantPowerBonus(World world, int x, int y, int z) {
        if (world.getBlockMetadata(x, y, z) == 0) {
            return 4.0F;
        } else {
            return world.getBlockMetadata(x, y, z) == 2 ? 12.0F : 0.0F;
        }
    }
}
