package foxiwhitee.hellmod.integration.botania.blocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.client.render.spreaders.*;
import foxiwhitee.hellmod.integration.botania.tile.spreaders.*;
import foxiwhitee.hellmod.utils.helpers.RenderIDs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.item.ModItems;

public class BlockCustomSpreader  extends Block implements ITileEntityProvider, IWandable, IWandHUD, IWireframeAABBProvider {
    public IIcon iIcon;
    private String name;

    public BlockCustomSpreader(String name) {
        super(Material.wood);
        this.name = name;
        setHardness(2.0F);
        setStepSound(soundTypeWood);
        setBlockName(name);
        setCreativeTab(HellCore.HELL_TAB);
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase base, ItemStack par6ItemStack) {
        int orientation = BlockPistonBase.determineOrientation(world, x, y, z, base);
        TileSpreader spreader = (TileSpreader)world.getTileEntity(x, y, z);
        switch (orientation) {
            case 0:
                spreader.rotationY = -90.0F;
            case 1:
                spreader.rotationY = 90.0F;
            case 2:
                spreader.rotationX = 270.0F;
            case 3:
                spreader.rotationX = 90.0F;
            case 4:
                return;
        }
        spreader.rotationX = 180.0F;
    }

    public int damageDropped(int meta) {
        return meta;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public void registerBlockIcons(IIconRegister reg) {
        switch (name) {
            case "asgardSpreader": {
                RenderingRegistry.registerBlockHandler(getRenderId(), (ISimpleBlockRenderingHandler)new RenderItemAsgardBlockSpreader(name));
                break;
            }
            case "helhelmSpreader": {
                RenderingRegistry.registerBlockHandler(getRenderId(), (ISimpleBlockRenderingHandler)new RenderItemHelhelmBlockSpreader(name));
                break;
            }
            case "valhallaSpreader": {
                RenderingRegistry.registerBlockHandler(getRenderId(), (ISimpleBlockRenderingHandler)new RenderItemValhallaBlockSpreader(name));
                break;
            }
            default: {
                RenderingRegistry.registerBlockHandler(getRenderId(), (ISimpleBlockRenderingHandler)new RenderItemMidgardBlockSpreader(name));
            }
        }
    }

    public int getManaPerSec() {
        switch (name) {
            case "asgardSpreader": {
                return HellConfig.manaPerSecAsgardSpreader;
            }
            case "helhelmSpreader": {
                return HellConfig.manaPerSecHelhelmSpreader;
            }
            case "valhallaSpreader": {
                return HellConfig.manaPerSecValhallaSpreader;
            }
            default: {
                return HellConfig.manaPerSecMidgardSpreader;
            }
        }
    }



    public boolean renderAsNormalBlock() {
        return false;
    }

    public IIcon getIcon(int side, int meta) {
        return ModBlocks.livingwood.getIcon(side, 0);
    }

    public int getRenderType() {
        return getRenderId();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (!(tile instanceof TileSpreader))
            return false;
        TileSpreader spreader = (TileSpreader)tile;
        ItemStack lens = spreader.getStackInSlot(0);
        ItemStack heldItem = player.getCurrentEquippedItem();
        boolean isHeldItemLens = (heldItem != null && heldItem.getItem() instanceof vazkii.botania.api.mana.ILens);
        boolean wool = (heldItem != null && heldItem.getItem() == Item.getItemFromBlock(Blocks.wool));
        if (heldItem != null && heldItem.getItem() == ModItems.twigWand)
            return false;
        if (lens == null && isHeldItemLens) {
            if (!player.capabilities.isCreativeMode)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            spreader.setInventorySlotContents(0, heldItem.copy());
            spreader.markDirty();
        } else if (lens != null && !wool) {
            ItemStack add = lens.copy();
            if (!player.inventory.addItemStackToInventory(add))
                player.dropPlayerItemWithRandomChoice(add, false);
            spreader.setInventorySlotContents(0, null);
            spreader.markDirty();
        }
        if (wool && spreader.paddingColor == -1) {
            spreader.paddingColor = heldItem.getItemDamage();
            ItemStack itemStack = heldItem;
            itemStack.stackSize--;
            if (heldItem.stackSize == 0)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        } else if (heldItem == null && spreader.paddingColor != -1 && lens == null) {
            ItemStack pad = new ItemStack(Blocks.wool, 1, spreader.paddingColor);
            if (!player.inventory.addItemStackToInventory(pad))
                player.dropPlayerItemWithRandomChoice(pad, false);
            spreader.paddingColor = -1;
            spreader.markDirty();
        }
        return true;
    }

    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (!(tile instanceof TileSpreader))
            return;
        TileSpreader inv = (TileSpreader)tile;
        for (int j1 = 0; j1 < inv.getSizeInventory() + 1; j1++) {
            ItemStack itemstack = (j1 >= inv.getSizeInventory()) ? ((inv.paddingColor == -1) ? null : new ItemStack(Blocks.wool, 1, inv.paddingColor)) : inv.getStackInSlot(j1);
            if (itemstack != null)
                dropBlockAsItem(world, x, y + 1, z, itemstack);
        }
        world.notifyBlockChange(x, y, z, par5);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
        ((TileSpreader)world.getTileEntity(x, y, z)).onWanded(player, stack);
        return true;
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        switch (name) {
            case "asgardSpreader": {
                return new TileAsgardSpreader();
            }
            case "helhelmSpreader": {
                return new TileHelhelmSpreader();
            }
            case "valhallaSpreader": {
                return new TileValhallaSpreader();
            }
            default: {
                return new TileMidgardSpreader();
            }
        }
    }

    public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
        ((TileSpreader)world.getTileEntity(x, y, z)).renderHUD(mc, res);
    }

    public AxisAlignedBB getWireframeAABB(World world, int x, int y, int z) {
        float f = 0.0625F;
        return AxisAlignedBB.getBoundingBox((x + f), (y + f), (z + f), ((x + 1) - f), ((y + 1) - f), ((z + 1) - f));
    }

    private int getRenderId() {
        switch (name) {
            case "asgardSpreader": return RenderIDs.ASGARD_SPREADER.getId();
            case "helhelmSpreader": return RenderIDs.HELHELM_SPREADER.getId();
            case "valhallaSpreader": return RenderIDs.VALHALLA_SPREADER.getId();
            case "midgardSpreader": return RenderIDs.MIDGARD_SPREADER.getId();
            default: return 0;
        }
    }
}
