package foxiwhitee.hellmod.blocks;

import appeng.util.Platform;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.GuiCobblestoneDuper;
import foxiwhitee.hellmod.client.gui.GuiFluidReceiver;
import foxiwhitee.hellmod.container.ContainerCobblestoneDuper;
import foxiwhitee.hellmod.container.ContainerFluidReceiver;
import foxiwhitee.hellmod.tile.TileCobblestoneDuper;
import foxiwhitee.hellmod.tile.fluid.TileFluidReceiver;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;

@SimpleGuiHandler(index = GuiHandlers.fluidReceiver, tile = TileFluidReceiver.class, gui = GuiFluidReceiver.class, container = ContainerFluidReceiver.class)
public class BlockFluidReceiver extends Block implements ITileEntityProvider {
    private final String name;
    public BlockFluidReceiver(String name) {
        super(Material.iron);
        this.name = name;
        setCreativeTab(HellCore.HELL_TAB);
        setBlockName(name);
        setHardness(0.5F);
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        return (TileEntity)new TileFluidReceiver();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileFluidReceiver)
            FMLNetworkHandler.openGui(player, HellCore.instance, GuiHandlers.fluidReceiver, world, x, y, z);
        return true;
    }
}