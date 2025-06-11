package foxiwhitee.hellmod.integration.ic2.blocks;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.client.gui.GuiAdvancedScanner;
import foxiwhitee.hellmod.integration.ic2.container.ContainerAdvancedScanner;
import foxiwhitee.hellmod.integration.ic2.tile.TileAdvancedScanner;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

@SimpleGuiHandler(index = GuiHandlers.advancedScanner, tile = TileAdvancedScanner.class, gui = GuiAdvancedScanner.class, container = ContainerAdvancedScanner.class, integration = "IC2")
public class BlockAdvancedScanner extends BaseIC2Block {
    private final IIcon[] icons = new IIcon[6];

    Random rand;
    private String name;
    private int duration;

    public BlockAdvancedScanner(String name) {
        super(name);
        this.name = name;
        this.rand = new Random();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int s, float f1, float f2, float f3) {
        if (player.isSneaking())
            return false;
        if (world.isRemote)
            return true;
        TileEntity tileentity = world.getTileEntity(x, y, z);
        if (tileentity != null && world.getTileEntity(x, y, z) instanceof TileAdvancedScanner)
            player.openGui(HellCore.instance, GuiHandlers.advancedScanner, world, x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return (TileEntity)new TileAdvancedScanner();
    }

    @Override
    public IIcon[] getIcons() {
        return icons;
    }

    @Override
    public boolean isCanActive() {
        return false;
    }
}
