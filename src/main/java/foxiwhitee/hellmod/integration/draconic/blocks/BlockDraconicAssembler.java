package foxiwhitee.hellmod.integration.draconic.blocks;

import appeng.util.Platform;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.GuiAutomatedUpgradeableBlock;
import foxiwhitee.hellmod.container.ContainerAutomatedUpgradeableBlock;
import foxiwhitee.hellmod.integration.draconic.client.gui.GuiDraconicAssembler;
import foxiwhitee.hellmod.integration.draconic.container.ContainerDraconicAssembler;
import foxiwhitee.hellmod.integration.draconic.tile.TileDraconicAssembler;
import foxiwhitee.hellmod.tile.TileAutomatedUpgradeableBlock;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import foxiwhitee.hellmod.utils.helpers.RenderIDs;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

@SimpleGuiHandler(index = GuiHandlers.draconicAssembler, tile = TileDraconicAssembler.class, gui = GuiDraconicAssembler.class, container = ContainerDraconicAssembler.class, integration = "DraconicEvolution")
public class BlockDraconicAssembler extends Block implements ITileEntityProvider {

    public BlockDraconicAssembler(String name) {
        super(Material.iron);
        this.setBlockName(name);
        this.setBlockTextureName(HellCore.MODID + ":draconic/" + name);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setLightLevel(2f);
        this.setLightOpacity(10);
        this.setHardness(3);
        this.setResistance(10);
        this.setHarvestLevel("pickaxe", 3);
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return RenderIDs.DRACONIC_ASSEMBLER.getId();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileDraconicAssembler();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (world.isRemote)
            return true;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null)
            player.openGui(HellCore.MODID, GuiHandlers.draconicAssembler, world, x, y, z);
        return true;
    }

    @Override
    protected void dropBlockAsItem(World p_149642_1_, int p_149642_2_, int p_149642_3_, int p_149642_4_, ItemStack p_149642_5_) {
        //super.dropBlockAsItem(p_149642_1_, p_149642_2_, p_149642_3_, p_149642_4_, p_149642_5_);
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileDraconicAssembler tile = (TileDraconicAssembler)world.getTileEntity(x, y, z);
        ArrayList<ItemStack> stacks = new ArrayList<>();
        if (tile != null) {
            tile.dropAllItems(world, x, y, z);
            ItemStack stack = new ItemStack(getItemDropped(meta, world.rand, damageDropped(meta)), 1, meta);
            NBTTagCompound compound = stack.stackTagCompound = new NBTTagCompound();
            TileEntity tileentity = world.getTileEntity(x, y, z);
            tile.writeItemNBT(compound);
            stacks.add(stack);
            Platform.spawnDrops(world, x, y, z, stacks);
        }
        world.removeTileEntity(x, y, z);
        world.setBlockToAir(x, y, z);
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, p_149689_5_, stack);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileDraconicAssembler && stack.hasTagCompound())
            ((TileDraconicAssembler)tile).readItemNBT(stack.stackTagCompound);
    }

    public boolean isOpaqueCube() {
        return false;
    }
}
