package foxiwhitee.hellmod.blocks.cpu;

import appeng.block.crafting.BlockCraftingUnit;
import appeng.client.render.blocks.RenderBlockCraftingCPU;
import appeng.core.features.AEFeature;
import appeng.core.sync.GuiBridge;
import appeng.tile.crafting.TileCraftingTile;
import appeng.util.Platform;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.tile.cpu.TileCustomCraftingStorage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
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

public class BlockCustomCraftingStorage extends BlockCraftingUnit {
    public static BiMap<BlockCustomCraftingStorage, Long> storageBytesMap = HashBiMap.create();
    private final String name;
    private final IIcon[] icons = new IIcon[2];

    public BlockCustomCraftingStorage(String name) {
        this.name = name;
        setBlockName(name);
        setTileEntity(TileCustomCraftingStorage.class);
        this.setFeature(EnumSet.of(AEFeature.CraftingCPU));
    }

    @SideOnly(Side.CLIENT)
    protected RenderBlockCraftingCPU<? extends BlockCraftingUnit, ? extends TileCraftingTile> getRenderer() {
        return new RenderBlockCraftingCPU();
    }

    public boolean onActivated(World w, int x, int y, int z, EntityPlayer p, int side, float hitX, float hitY, float hitZ) {
        TileCraftingTile tg = this.getTileEntity(w, x, y, z);
        if (tg == null || p.isSneaking() || !tg.isFormed() || !tg.isActive()) {
            return false;
        }
        if (Platform.isClient()) {
            return true;
        }
        Platform.openGUI(p, tg, ForgeDirection.getOrientation(side), GuiBridge.GUI_CRAFTING_CPU);
        return true;
    }

    public void setRenderStateByMeta(int itemDamage) {
        IIcon front = this.getIcon(ForgeDirection.SOUTH.ordinal(), itemDamage);
        IIcon other = this.getIcon(ForgeDirection.NORTH.ordinal(), itemDamage);
        this.getRendererInstance().setTemporaryRenderIcons(other, other, front, other, other, other);
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileCraftingTile cp = this.getTileEntity(world, x, y, z);
        if (cp != null) {
            cp.breakCluster();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    public void onNeighborBlockChange(World w, int x, int y, int z, Block junk) {
        TileCraftingTile cp = this.getTileEntity(w, x, y, z);
        if (cp != null) {
            cp.updateMultiBlock();
        }
    }

    public int getDamageValue(World w, int x, int y, int z) {
        int meta = w.getBlockMetadata(x, y, z);
        return this.damageDropped(meta);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegistry) {
        icons[0] = iconRegistry.registerIcon(HellCore.MODID + ":ae2/blockCraftingStorages/" + name);
        icons[1] = iconRegistry.registerIcon(HellCore.MODID + ":ae2/blockCraftingStorages/" + name + "Fit");
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return icons[metadata >> 3];
    }

    public String getName() {
        return name;
    }
    @SideOnly(Side.CLIENT)
    public void getCheckedSubBlocks(Item item, CreativeTabs tabs, List<ItemStack> itemStacks) {
        itemStacks.add(new ItemStack(this, 1, 0));
    }

    public static void registerCraftingStorage(String name, long bytes) {
        storageBytesMap.put(new BlockCustomCraftingStorage(name), bytes);
    }

    public static BlockCustomCraftingStorage getBlockFromBytes(long bytes) {
        return storageBytesMap.inverse().get(bytes);
    }

    public static long getBytesFromBlock(Block b) {
        return storageBytesMap.getOrDefault(b, 0L);
    }

    public static void registerBlocks() {
        for (BlockCustomCraftingStorage b: storageBytesMap.keySet()) {
            RegisterUtils.registerBlock(b, b.getItemBlockClass());
        }
    }
}