package foxiwhitee.hellmod.integration.botania.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.GuiFluidReceiver;
import foxiwhitee.hellmod.container.ContainerFluidReceiver;
import foxiwhitee.hellmod.integration.botania.client.gui.GuiManaGenerator;
import foxiwhitee.hellmod.integration.botania.container.ContainerManaGenerator;
import foxiwhitee.hellmod.integration.botania.tile.ae.*;
import foxiwhitee.hellmod.tile.fluid.TileFluidReceiver;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@SimpleGuiHandler(index = GuiHandlers.manaGenerator, tile = TileManaGenerator.class, gui = GuiManaGenerator.class, container = ContainerManaGenerator.class)
public class BlockManaGenerator  extends Block implements ITileEntityProvider {
    private final String name;
    private int index;
    public BlockManaGenerator(String name, int index) {
        super(Material.iron);
        this.name = name;
        this.index = index;
        setCreativeTab(HellCore.HELL_TAB);
        setBlockName(name);
        setHardness(0.5F);
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        switch (index) {
            case 2: return new TileManaGeneratorTier2();
            case 3: return new TileManaGeneratorTier3();
            case 4: return new TileManaGeneratorTier4();
            case 5: return new TileManaGeneratorTier5();
            default: return new TileManaGeneratorTier1();
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileManaGenerator)
            FMLNetworkHandler.openGui(player, HellCore.instance, GuiHandlers.manaGenerator, world, x, y, z);
        return true;
    }
}