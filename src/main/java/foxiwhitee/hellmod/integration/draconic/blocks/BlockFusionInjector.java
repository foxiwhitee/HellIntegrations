package foxiwhitee.hellmod.integration.draconic.blocks;

import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.draconicevolution.common.utills.IHudDisplayBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.tile.TileFusionInjector;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFusionInjector extends Block implements ITileEntityProvider, IHudDisplayBlock {
    private final Random rand;

    public IIcon[] icon;

    public BlockFusionInjector(String name) {
        super(Material.iron);
        this.rand = new Random();
        setHardness(5.0F);
        setResistance(10.0F);
        setBlockName(name);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.icon = new IIcon[1];
        this.icon[0] = register.registerIcon(HellCore.MODID + ":models/base_fusion_injector");
    }

    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 5; i++)
            list.add(new ItemStack(item, 1, i));
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        return (TileEntity)new TileFusionInjector(meta);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (world.isRemote)
            return true;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (!(tile instanceof TileFusionInjector))
            return false;
        TileFusionInjector craftingPedestal = (TileFusionInjector)tile;
        if (player.isSneaking()) {
            craftingPedestal.singleItem = !craftingPedestal.singleItem;
            craftingPedestal.markForUpdate();
        } else {
            if (craftingPedestal.getStackInSlot(0) != null) {
                if (player.getHeldItem() == null) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, craftingPedestal.getStackInSlot(0));
                    craftingPedestal.setInventorySlotContents(0, null);
                    world.markBlockForUpdate(x, y, z);
                } else {
                    world.spawnEntityInWorld((Entity) new EntityItem(world, player.posX, player.posY, player.posZ, craftingPedestal.getStackInSlot(0)));
                    craftingPedestal.setInventorySlotContents(0, null);
                    world.markBlockForUpdate(x, y, z);
                }
            } else {
                ItemStack stack = player.getHeldItem();
                craftingPedestal.setInventorySlotContents(0, stack);
                player.destroyCurrentEquippedItem();
                world.markBlockForUpdate(x, y, z);
            }
        }
        return true;
    }

    public int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(DraconicEvolutionIntegration.fusion_injector);
    }

    public int damageDropped(int metadata) {
        return metadata;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    public int getRenderType() {
        return -1;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileFusionInjector) {
            TileFusionInjector te = (TileFusionInjector)tile;
            if (Math.abs(x - player.posX + 0.5D) < 2.0D && Math.abs(z - player.posZ + 0.5D) < 2.0D) {
                double d0 = player.posY + player.getEyeHeight();
                if (d0 - y > 2.0D) {
                    te.setFace(1);
                    return;
                }
                if (y - d0 > 0.0D) {
                    te.setFace(0);
                    return;
                }
            }
            int l = MathHelper.floor_double((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
            switch (l) {
                case 0:
                    te.setFace(2);
                    break;
                case 1:
                    te.setFace(5);
                    break;
                case 2:
                    te.setFace(3);
                    break;
                case 3:
                    te.setFace(4);
                    break;
            }
        }
    }

    public List<String> getDisplayData(World world, int x, int y, int z) {
        List<String> list = new ArrayList<>();
        EnumChatFormatting s = EnumChatFormatting.DARK_AQUA;
        EnumChatFormatting g = EnumChatFormatting.GOLD;
        EnumChatFormatting p = EnumChatFormatting.DARK_PURPLE;
        TileFusionInjector tile = (TileFusionInjector)world.getTileEntity(x, y, z);
        if (tile != null) {
            list.add(InfoHelper.HITC() + p + LocalizationUtils.localize(getUnlocalizedName() + "." + tile.getBlockMetadata() + ".name"));
            list.add(InfoHelper.HITC() + LocalizationUtils.localize("tooltip.msg.craftingInjector.singleItem" + (tile.singleItem ? "On" : "Off")));
            switch (tile.getPedestalTier()) {
                case 0:
                    list.add(s + LocalizationUtils.localize("tooltip.fusionCrafting.tier") + ": " + g + "Draconic");
                    break;
                case 1:
                    list.add(s + LocalizationUtils.localize("tooltip.fusionCrafting.tier") + ": " + g + "Wyvern");
                    break;
                case 2:
                    list.add(s + LocalizationUtils.localize("tooltip.fusionCrafting.tier") + ": " + g + "Awakened");
                    break;
                case 3:
                    list.add(s + LocalizationUtils.localize("tooltip.fusionCrafting.tier") + ": " + g + "Chaotic");
                    break;
                case 4:
                    list.add(s + LocalizationUtils.localize("tooltip.fusionCrafting.tier") + ": " + g + "Arial");
                    break;
            }
            if (tile.getStackInPedestal() != null)
                list.add(s + LocalizationUtils.localize("tooltip.fusionCrafting.currentItem") + ": " + g + tile.getStackInPedestal().getDisplayName() + " x " + (tile.getStackInPedestal()).stackSize);
        }
        return list;
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int wut) {
        TileFusionInjector tile = (TileFusionInjector)world.getTileEntity(x, y, z);
        if (tile != null) {
            for (int i1 = 0; i1 < tile.getSizeInventory(); i1++) {
                ItemStack itemstack = tile.getStackInSlot(i1);
                if (itemstack != null) {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f3 = this.rand.nextFloat() * 0.8F + 0.1F;
                    while (itemstack.stackSize > 0) {
                        int j1 = this.rand.nextInt(21) + 10;
                        if (j1 > itemstack.stackSize)
                            j1 = itemstack.stackSize;
                        ItemStack itemStack = itemstack;
                        itemStack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (x + f), (y + f2), (z + f3), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f4 = 0.05F;
                        entityitem.motionX = ((float)this.rand.nextGaussian() * f4);
                        entityitem.motionY = ((float)this.rand.nextGaussian() * f4 + 0.2F);
                        entityitem.motionZ = ((float)this.rand.nextGaussian() * f4);
                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        world.spawnEntityInWorld((Entity)entityitem);
                    }
                }
            }
            world.func_147453_f(x, y, z, block);
        }
        super.breakBlock(world, x, y, z, block, wut);
    }
}