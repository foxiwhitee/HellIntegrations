package foxiwhitee.hellmod.integration.avaritia.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.avaritia.client.gui.GuiCustomNeutronCollector;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerCustomNeutronCollector;
import foxiwhitee.hellmod.integration.avaritia.helpers.INeutronCollector;
import foxiwhitee.hellmod.integration.avaritia.tile.collectors.*;
import foxiwhitee.hellmod.integration.avaritia.utils.NeutronCollectorsOutput;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

@SimpleGuiHandler(index = GuiHandlers.customNeutronCollector, tile = TileCustomNeutronCollector.class, gui = GuiCustomNeutronCollector.class, container = ContainerCustomNeutronCollector.class, integration = "Avariatia")
public class BlockCustomNeutronCollector extends BlockContainer implements INeutronCollector {
    private final Random randy = new Random();

    private IIcon top;

    private IIcon sides;

    private IIcon front;

    public BlockCustomNeutronCollector(String name) {
        super(Material.iron);
        setHardness(3.0F);
        setCreativeTab(HellCore.HELL_TAB);
        setBlockName(name);
    }

    public ItemStack getStack() {
        switch (this.getUnlocalizedName().replace("tile.", "")) {
            case "advancedNeutronCollector": return NeutronCollectorsOutput.ADVANCED.getStack();
            case "hybridNeutronCollector": return NeutronCollectorsOutput.HYBRID.getStack();
            case "ultimateNeutronCollector": return NeutronCollectorsOutput.ULTIMATE.getStack();
            case "quantiumNeutronCollector": return NeutronCollectorsOutput.QUANTUM.getStack();
            default: return NeutronCollectorsOutput.BASE.getStack();
        }
    }

    public int getTicks() {
        switch (this.getUnlocalizedName().replace("tile.", "")) {
            case "advancedNeutronCollector": return HellConfig.advTicks;
            case "hybridNeutronCollector": return HellConfig.hybTicks;
            case "ultimateNeutronCollector": return HellConfig.ultTicks;
            case "quantiumNeutronCollector": return HellConfig.quantTicks;
            default: return HellConfig.baseTicks;
        }
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.top = iconRegister.registerIcon(HellCore.MODID + ":neutronsmachines/" + this.getUnlocalizedName().replace("tile.", "") + "_top");
        this.sides = iconRegister.registerIcon(HellCore.MODID + ":neutronsmachines/" + this.getUnlocalizedName().replace("tile.", "") + "_side");
        this.front = iconRegister.registerIcon(HellCore.MODID + ":neutronsmachines/" + this.getUnlocalizedName().replace("tile.", "") + "_front");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (side == 1)
            return this.top;
        int facing = 2;
        TileCustomNeutronCollector machine = (TileCustomNeutronCollector)world.getTileEntity(x, y, z);
        if (machine != null)
            facing = machine.getFacing();
        if (side == facing)
            return this.front;
        return this.sides;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (side == 1)
            return this.top;
        if (side == 3)
            return this.front;
        return this.sides;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileCustomNeutronCollector) {
            TileCustomNeutronCollector machine = (TileCustomNeutronCollector)tile;
            int l = MathHelper.floor_double((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
            if (l == 0)
                machine.setFacing(2);
            if (l == 1)
                machine.setFacing(5);
            if (l == 2)
                machine.setFacing(3);
            if (l == 3)
                machine.setFacing(4);
        }
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int wut) {
        TileCustomNeutronCollector collector = (TileCustomNeutronCollector)world.getTileEntity(x, y, z);
        if (collector != null) {
            ItemStack itemstack = collector.getStackInSlot(0);
            if (itemstack != null) {
                float f = this.randy.nextFloat() * 0.8F + 0.1F;
                float f1 = this.randy.nextFloat() * 0.8F + 0.1F;
                float f2 = this.randy.nextFloat() * 0.8F + 0.1F;
                while (itemstack.stackSize > 0) {
                    int j1 = this.randy.nextInt(21) + 10;
                    if (j1 > itemstack.stackSize)
                        j1 = itemstack.stackSize;
                    itemstack.stackSize -= j1;
                    EntityItem entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                    if (itemstack.hasTagCompound())
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    float f3 = 0.05F;
                    entityitem.motionX = ((float)this.randy.nextGaussian() * f3);
                    entityitem.motionY = ((float)this.randy.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = ((float)this.randy.nextGaussian() * f3);
                    world.spawnEntityInWorld((Entity)entityitem);
                }
            }
            world.func_147453_f(x, y, z, block);
        }
        super.breakBlock(world, x, y, z, block, wut);
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        switch (this.getUnlocalizedName().replace("tile.", "")) {
            case "advancedNeutronCollector": return new TileAdvancedNeutronCollector();
            case "hybridNeutronCollector": return new TileHybridNeutronCollector();
            case "ultimateNeutronCollector": return new TileUltimateNeutronCollector();
            case "quantiumNeutronCollector": return new TileQuantumNeutronCollector();
            default: return new TileBaseNeutronCollector();
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (world.isRemote)
            return true;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null)
            player.openGui(HellCore.MODID, GuiHandlers.customNeutronCollector, world, x, y, z);
        return true;
    }

}
