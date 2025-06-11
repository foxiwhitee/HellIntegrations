package foxiwhitee.hellmod.integration.draconic.blocks;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.common.ModBlocks;
import com.brandon3055.draconicevolution.common.blocks.BlockCustomDrop;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.GuiAutomatedUpgradeableBlock;
import foxiwhitee.hellmod.container.ContainerAutomatedUpgradeableBlock;
import foxiwhitee.hellmod.integration.draconic.client.gui.GuiCustomUpgradeModifier;
import foxiwhitee.hellmod.integration.draconic.container.ContainerCustomUpgradeModifier;
import foxiwhitee.hellmod.integration.draconic.tile.TileCustomUpgradeModifier;
import foxiwhitee.hellmod.tile.TileAutomatedUpgradeableBlock;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

@SimpleGuiHandler(index = GuiHandlers.customUpgradeModifier, tile = TileCustomUpgradeModifier.class, gui = GuiCustomUpgradeModifier.class, container = ContainerCustomUpgradeModifier.class, integration = "DraconicEvolution")
public class BlockCustomUpgradeModifier extends BlockCustomDrop {
    public BlockCustomUpgradeModifier(String name) {
        super(Material.iron);
        this.setBlockName(name);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setStepSound(soundTypeStone);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileCustomUpgradeModifier();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float prx, float pry, float prz) {
        if (!world.isRemote) {
            FMLNetworkHandler.openGui(player, HellCore.instance, GuiHandlers.customUpgradeModifier, world, x, y, z);
        }

        world.markBlockForUpdate(x, y, z);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return -1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    protected boolean dropInventory() {
        return true;
    }

    protected boolean hasCustomDropps() {
        return false;
    }

    protected void getCustomTileEntityDrops(TileEntity te, List<ItemStack> droppes) {
    }

    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        return false;
    }

    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return side == ForgeDirection.DOWN;
    }
}