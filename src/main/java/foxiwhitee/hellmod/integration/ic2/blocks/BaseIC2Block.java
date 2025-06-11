package foxiwhitee.hellmod.integration.ic2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.block.BlockBase;
import ic2.core.block.BlockTextureStitched;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public abstract class BaseIC2Block extends BlockContainer {
    protected final IIcon[] icons = new IIcon[12];
    private boolean canActive = true;
    Random rand;
    private String name;
    public BaseIC2Block(String name) {
        super(Material.iron);
        this.name = name;
        this.rand = new Random();
        setCreativeTab(HellCore.HELL_TAB);
        setHardness(5.0F);
        setResistance(1000.0F);
        setBlockName(name);
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int wut) {
        TileEntityElectricMachine tile = (TileEntityElectricMachine)world.getTileEntity(x, y, z);
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

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IWrenchable) {
            IWrenchable te = (IWrenchable)tileEntity;
            if (entityliving == null) {
                te.setFacing((short)2);
            } else {
                int l = MathHelper.floor_double((entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
                switch (l) {
                    case 0:
                        te.setFacing((short)2);
                        break;
                    case 1:
                        te.setFacing((short)5);
                        break;
                    case 2:
                        te.setFacing((short)3);
                        break;
                    case 3:
                        te.setFacing((short)4);
                        break;
                }
            }
        }
    }

    public abstract boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int s, float f1, float f2, float f3);

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        String name = HellCore.MODID + ":ic2/" + this.name;
        int b = isCanActive() ? 2 : 1;
        for (int a = 0; a < b; a++) {
            for (int s = 0; s < 6; s++) {
                int subIndex = a * 6 + s;
                BlockTextureStitched blockTextureStitched = new BlockTextureStitched(name + ":" + subIndex, subIndex);
                this.getIcons()[subIndex] = (IIcon)blockTextureStitched;
                ((TextureMap)register).setTextureEntry(name + ":" + subIndex, (TextureAtlasSprite)blockTextureStitched);
            }
        }
    }

    public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z) {
        TileEntity te = iBlockAccess.getTileEntity(x, y, z);
        if (te instanceof TileEntityBlock)
            return ((TileEntityBlock)te).getFacing();
        int meta = iBlockAccess.getBlockMetadata(x, y, z);
        return 3;
    }

    public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
        int facing = getFacing(iBlockAccess, x, y, z);
        boolean active = isActive(iBlockAccess, x, y, z);
        int meta = iBlockAccess.getBlockMetadata(x, y, z);
        int index = meta;
        if (index >= this.getIcons().length)
            return null;
        int subIndex = BlockBase.getTextureSubIndex(facing, side);
        if (active) {
            subIndex += this.isCanActive() ? 6 : 0;
        }
        try {
            return this.getIcons()[subIndex];
        } catch (Exception var12) {
            IC2.platform.displayError(var12, "Coordinates: %d/%d/%d\nSide: %d\nBlock: %s\nMeta: %d\nFacing: %d\nActive: %s\nIndex: %d\nSubIndex: %d", new Object[] { Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z), Integer.valueOf(side), this, Integer.valueOf(meta), Integer.valueOf(facing), Boolean.valueOf(active), Integer.valueOf(index), Integer.valueOf(subIndex) });
            return null;
        }
    }

    public IIcon getIcon(int side, int meta) {
        int facing = 3;
        int index = meta;
        int subIndex = BlockBase.getTextureSubIndex(facing, side);
        if (index >= this.getIcons().length)
            return null;
        try {
            return this.getIcons()[subIndex];
        } catch (Exception var7) {
            IC2.platform.displayError(var7, "Side: " + side + "\nBlock: " + this + "\nMeta: " + meta + "\nFacing: " + facing + "\nIndex: " + index + "\nSubIndex: " + subIndex, new Object[0]);
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    private boolean isActive(IBlockAccess iba, int x, int y, int z) {
        return ((TileEntityBlock)iba.getTileEntity(x, y, z)).getActive();
    }

    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {
        if (axis == ForgeDirection.UNKNOWN)
            return false;
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IWrenchable) {
            IWrenchable te = (IWrenchable)tileEntity;
            int newFacing = ForgeDirection.getOrientation(te.getFacing()).getRotation(axis).ordinal();
            if (te.wrenchCanSetFacing(null, newFacing))
                te.setFacing((short)newFacing);
        }
        return false;
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        if (!IC2.platform.isRendering())
            return;
    }

    public abstract TileEntity createNewTileEntity(World world, int meta);

    public boolean isCanActive() {
        return canActive;
    }

    public IIcon[] getIcons() {
        return icons;
    }
}
