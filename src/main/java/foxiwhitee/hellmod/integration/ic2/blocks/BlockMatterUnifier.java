package foxiwhitee.hellmod.integration.ic2.blocks;

import java.util.Random;

import appeng.block.AEBaseTileBlock;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.avaritia.client.gui.GuiNeutronUnifier;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerNeutronUnifier;
import foxiwhitee.hellmod.integration.avaritia.tile.TileNeutronUnifier;
import foxiwhitee.hellmod.integration.ic2.client.gui.GuiMatterUnifier;
import foxiwhitee.hellmod.integration.ic2.container.ContainerMatterUnifier;
import foxiwhitee.hellmod.integration.ic2.tile.TileMatterUnifier;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@SimpleGuiHandler(index = GuiHandlers.matterUnifier, tile = TileMatterUnifier.class, container = ContainerMatterUnifier.class, gui = GuiMatterUnifier.class, integration = "IC2")
public class BlockMatterUnifier extends AEBaseTileBlock {
    protected final Random randy;

    public BlockMatterUnifier(String name) {
        super(Material.iron);
        this.randy = new Random();
        setHardness(10.0F);
        setBlockTextureName(HellCore.MODID + ":ae2/" + name);
        setBlockName(name);
        setTileEntity(TileMatterUnifier.class);
        this.isOpaque = false;
        this.lightOpacity = 1;
    }

    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileMatterUnifier && !world.isRemote)
            player.openGui(HellCore.instance, GuiHandlers.matterUnifier, world, x, y, z);
        return true;
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int wut) {
        TileMatterUnifier unifier = (TileMatterUnifier)world.getTileEntity(x, y, z);
        if (unifier != null) {
            for (int i1 = 0; i1 < unifier.getSizeInventory(); i1++) {
                ItemStack itemstack = unifier.getStackInSlot(i1);
                if (itemstack != null) {
                    float f = this.randy.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.randy.nextFloat() * 0.8F + 0.1F;
                    float f3 = this.randy.nextFloat() * 0.8F + 0.1F;
                    while (itemstack.stackSize > 0) {
                        int j1 = this.randy.nextInt(21) + 10;
                        if (j1 > itemstack.stackSize)
                            j1 = itemstack.stackSize;
                        ItemStack itemStack = itemstack;
                        itemStack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (x + f), (y + f2), (z + f3), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f4 = 0.05F;
                        entityitem.motionX = ((float)this.randy.nextGaussian() * f4);
                        entityitem.motionY = ((float)this.randy.nextGaussian() * f4 + 0.2F);
                        entityitem.motionZ = ((float)this.randy.nextGaussian() * f4);
                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        world.spawnEntityInWorld((Entity)entityitem);
                    }
                }
            }
            world.notifyBlockOfNeighborChange(x, y, z, block);
        }
        super.breakBlock(world, x, y, z, block, wut);
    }

    private static boolean boolAlphaPass = false;

    public static boolean isBooleanAlphaPass() {
        return boolAlphaPass;
    }

    private static void setBooleanAlphaPass(boolean booleanAlphaPass) {
        boolAlphaPass = booleanAlphaPass;
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

    public int getRenderBlockPass() {
        return 1;
    }

    public boolean canRenderInPass(int pass) {
        setBooleanAlphaPass((pass == 1));
        return (pass == 0 || pass == 1);
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileMatterUnifier) {
            TileMatterUnifier te = (TileMatterUnifier)tile;
            int l = MathHelper.floor_double((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
            switch (l) {
                case 0:
                    te.setOrientation(ForgeDirection.SOUTH, ForgeDirection.UP);
                    break;
                case 1:
                    te.setOrientation(ForgeDirection.NORTH, ForgeDirection.UP);
                    break;
                case 2:
                    te.setOrientation(ForgeDirection.WEST, ForgeDirection.UP);
                    break;
                case 3:
                    te.setOrientation(ForgeDirection.EAST, ForgeDirection.UP);
                    break;
            }
        }
    }
}