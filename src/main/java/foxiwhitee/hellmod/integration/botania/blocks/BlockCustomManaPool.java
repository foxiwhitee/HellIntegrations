package foxiwhitee.hellmod.integration.botania.blocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.client.render.pools.*;
import foxiwhitee.hellmod.integration.botania.tile.pools.*;
import foxiwhitee.hellmod.utils.helpers.RenderIDs;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class BlockCustomManaPool  extends Block implements ITileEntityProvider, IWandHUD, IWandable, ICraftAchievement {
    public static IIcon manaIcon;

    boolean lastFragile;
    private String name;
    private int maxMana;
    public BlockCustomManaPool(String name, int maxMana) {
        super(Material.iron);
        this.name = name;
        this.maxMana = maxMana;
        this.lastFragile = false;
        setBlockTextureName(HellCore.MODID + ":botania/" + name);
        setBlockName(name);
        setHardness(2.0F);
        setResistance(10.0F);
        setStepSound(soundTypeStone);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        BotaniaAPI.blacklistBlockFromMagnet(this, 32767);
        setCreativeTab(HellCore.HELL_TAB);
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        super.onBlockPlacedBy(world, x, y, z, placer, itemIn);
        ((TileCustomManaPool)world.getTileEntity(x, y, z)).setFieldValue("manaCap", Integer.valueOf(maxMana));
    }

    public int onBlockPlaced(World world, int x, int y, int z, int side, float subX, float subY, float subZ, int meta) {
        return super.onBlockPlaced(world, x, y, z, side, subX, subY, subZ, meta);
    }

    public void registerBlockIcons(IIconRegister par1IconRegister) {
        manaIcon = IconHelper.forName(par1IconRegister, "manaWater");
        int renderID = 0;
        switch (name) {
            case "asgardPool": {
                renderID = RenderIDs.ASGARD_MANA_POOL.getId();
                RenderingRegistry.registerBlockHandler(renderID, (ISimpleBlockRenderingHandler)new RenderItemAsgardManaPool(this.name));
                break;
            }
            case "helhelmPool": {
                renderID = RenderIDs.HELHELM_MANA_POOL.getId();
                RenderingRegistry.registerBlockHandler(renderID, (ISimpleBlockRenderingHandler)new RenderItemHelhelmManaPool(this.name));
                break;
            }
            case "valhallaPool": {
                renderID = RenderIDs.VALHALLA_MANA_POOL.getId();
                RenderingRegistry.registerBlockHandler(renderID, (ISimpleBlockRenderingHandler)new RenderItemValhallaManaPool(this.name));
                break;
            }
            default: {
                renderID = RenderIDs.MIDGARD_MANA_POOL.getId();
                RenderingRegistry.registerBlockHandler(renderID, (ISimpleBlockRenderingHandler)new RenderItemMidgardManaPool(this.name));
            }
        }

    }

    public int damageDropped(int meta) {
        return meta;
    }

    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        TileCustomManaPool pool = (TileCustomManaPool)world.getTileEntity(x, y, z);
        this.lastFragile = pool.fragile;
        super.breakBlock(world, x, y, z, par5, par6);
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (!this.lastFragile)
            drops.add(new ItemStack(this));
        return drops;
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        switch (name) {
            case "asgardPool": return new TileAsgardManaPool();
            case "helhelmPool": return new TileHelHelmManaPool();
            case "valhallaPool": return new TileValhallaManaPool();
            default: return new TileMidgardManaPool();
        }
    }

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity par5Entity) {
        if (par5Entity instanceof EntityItem) {
            TileCustomManaPool tile = (TileCustomManaPool)world.getTileEntity(x, y, z);
            if (tile.collideEntityItem((EntityItem)par5Entity))
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, x, y, z);
        }
    }

    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bb, List list, Entity player) {
        float f = 0.0625F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, bb, list, player);
        setBlockBounds(0.0F, 0.0F, 0.0F, f, 0.5F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, bb, list, player);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, f);
        super.addCollisionBoxesToList(world, x, y, z, bb, list, player);
        setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, bb, list, player);
        setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 0.5F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, bb, list, player);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }

    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return (side == ForgeDirection.DOWN);
    }

    public boolean isFullBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public IIcon getIcon(int par1, int par2) {
        return ModBlocks.livingrock.getIcon(0, 0);
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
        TileCustomManaPool pool = (TileCustomManaPool)par1World.getTileEntity(par2, par3, par4);
        int val = (int)(pool.getCurrentMana() / 1.0E8D * 15.0D);
        if (pool.getCurrentMana() > 0)
            val = Math.max(val, 1);
        return val;
    }

    @SideOnly(Side.CLIENT)
    public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
        ((TileCustomManaPool)world.getTileEntity(x, y, z)).renderHUD(mc, res);
    }

    public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
        ((TileCustomManaPool)world.getTileEntity(x, y, z)).onWanded(player, stack);
        return true;
    }

    public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
        return ModAchievements.manaPoolPickup;
    }

    public int getRenderType() {
        switch (name) {
            case "asgardPool": return RenderIDs.ASGARD_MANA_POOL.getId();
            case "helhelmPool": return RenderIDs.HELHELM_MANA_POOL.getId();
            case "valhallaPool": return RenderIDs.VALHALLA_MANA_POOL.getId();
            case "midgardPool": return RenderIDs.MIDGARD_MANA_POOL.getId();
            default: return 0;
        }
    }

    public int getMaxMana() {
        return maxMana;
    }
}
