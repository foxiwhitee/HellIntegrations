package foxiwhitee.hellmod.integration.botania.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.client.gui.GuiFlowerSynthesizer;
import foxiwhitee.hellmod.integration.botania.client.gui.GuiManaGenerator;
import foxiwhitee.hellmod.integration.botania.container.ContainerFlowerSynthesizer;
import foxiwhitee.hellmod.integration.botania.container.ContainerManaGenerator;
import foxiwhitee.hellmod.integration.botania.tile.ae.*;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@SimpleGuiHandler(index = GuiHandlers.flowerSynthesizer, tile = TileFlowerSynthesizer.class, gui = GuiFlowerSynthesizer.class, container = ContainerFlowerSynthesizer.class)
public class BlockFlowerSynthesizer extends Block implements ITileEntityProvider {
    public BlockFlowerSynthesizer(String name) {
        super(Material.iron);
        setCreativeTab(HellCore.HELL_TAB);
        setBlockName(name);
        setHardness(0.5F);
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFlowerSynthesizer();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileFlowerSynthesizer)
            FMLNetworkHandler.openGui(player, HellCore.instance, GuiHandlers.flowerSynthesizer, world, x, y, z);
        return true;
    }
}