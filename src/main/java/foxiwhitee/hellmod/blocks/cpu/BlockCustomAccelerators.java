package foxiwhitee.hellmod.blocks.cpu;

import appeng.block.crafting.BlockCraftingUnit;
import appeng.client.render.blocks.RenderBlockCraftingCPU;
import appeng.core.features.AEFeature;
import appeng.core.sync.GuiBridge;
import appeng.tile.crafting.TileCraftingTile;
import appeng.util.Platform;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.tile.cpu.TileCustomAccelerators;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;
import java.util.List;

public class BlockCustomAccelerators extends BlockCraftingUnit {
    public static final String[] names = new String[]{"advanced", "hybrid", "ultimate", "quantum"};
    private static final IIcon[] REGISTERED_ICONS = new IIcon[24];


    public BlockCustomAccelerators() {
        super();
        this.hasSubtypes = true;
        this.setTileEntity(TileCustomAccelerators.class);
        this.setFeature(EnumSet.of(AEFeature.CraftingCPU));
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected RenderBlockCraftingCPU<? extends BlockCustomAccelerators, ? extends TileCraftingTile> getRenderer() {
        return new RenderBlockCraftingCPU<BlockCustomAccelerators, TileCraftingTile>();
    }

    @Override
    public String getUnlocalizedName(final ItemStack is) {
        for (int n = 0; n < names.length; n++) {
            return "tile" + HellCore.MODID + names[n] + "_crafting_accelerator";
        }

        return this.getItemUnlocalizedName(is);
    }

    public void registerBlockIcons(IIconRegister ir) {
        for (int x = 0; x < names.length; x++) {
            REGISTERED_ICONS[2 * x] = ir.registerIcon(HellCore.MODID + ":ae2/crafting_accelerators/" + names[x] + "_crafting_accelerator");
            REGISTERED_ICONS[2 * x + 1] = ir.registerIcon(HellCore.MODID + ":ae2/crafting_accelerators/" + names[x] + "_crafting_accelerator" + "Fit");
        }

    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        int meta = metadata & 3;
        int formed = metadata >> 3;
        return REGISTERED_ICONS[meta * 2 + formed];
    }

    @Override
    public boolean onActivated(World w, int x, int y, int z, EntityPlayer p, int side, float hitX, float hitY, float hitZ) {
        TileCustomAccelerators tg = this.getTileEntity(w, x, y, z);
        if (tg != null && !p.isSneaking() && tg.isFormed() && tg.isActive()) {
            if (Platform.isClient()) {
                return true;
            }

            Platform.openGUI(p, tg, ForgeDirection.getOrientation(side), GuiBridge.GUI_CRAFTING_CPU);
            return true;
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getCheckedSubBlocks(Item item, CreativeTabs tabs, List<ItemStack> itemStacks) {
        itemStacks.add(new ItemStack(this, 1, 0));
        itemStacks.add(new ItemStack(this, 1, 1));
        itemStacks.add(new ItemStack(this, 1, 2));
        itemStacks.add(new ItemStack(this, 1, 3));
    }

    @Override
    public void setRenderStateByMeta(int itemDamage) {
        IIcon front = this.getIcon(ForgeDirection.SOUTH.ordinal(), itemDamage);
        IIcon other = this.getIcon(ForgeDirection.NORTH.ordinal(), itemDamage);
        this.getRendererInstance().setTemporaryRenderIcons(other, other, front, other, other, other);
        //System.out.println("itemDamage = " + itemDamage);
    }

    @Override
    public void breakBlock(World w, int x, int y, int z, Block a, int b) {
        TileCustomAccelerators cp = this.getTileEntity(w, x, y, z);
        if (cp != null) {
            cp.breakCluster();
        }

        super.breakBlock(w, x, y, z, a, b);
    }


    @Override
    public void onNeighborBlockChange(World w, int x, int y, int z, Block junk) {
        TileCustomAccelerators cp = this.getTileEntity(w, x, y, z);
        if (cp != null) {
            cp.updateMultiBlock();
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta & 3;
    }

    @Override
    public int getDamageValue(World w, int x, int y, int z) {
        int meta = w.getBlockMetadata(x, y, z);
        return this.damageDropped(meta);
    }

    public static int getAcceleratorsFromMeta(int meta) {
        switch (meta) {
            case 0: return HellConfig.advanced_accelerator;
            case 1: return HellConfig.hybrid_accelerator;
            case 2: return HellConfig.ultimate_accelerator;
            case 3: return HellConfig.quantum_accelerator;
            default: return 0;
        }
    }
}
