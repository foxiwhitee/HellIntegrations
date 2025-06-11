package foxiwhitee.hellmod.integration.ic2.blocks;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.client.gui.GuiCustomMatterGen;
import foxiwhitee.hellmod.integration.ic2.container.ContainerCustomMatterGen;
import foxiwhitee.hellmod.integration.ic2.helpers.IMatter;
import foxiwhitee.hellmod.integration.ic2.tile.matter.TileAdvancedMatterGen;
import foxiwhitee.hellmod.integration.ic2.tile.matter.TileCustomMatterGen;
import foxiwhitee.hellmod.integration.ic2.tile.matter.TileNanoMatterGen;
import foxiwhitee.hellmod.integration.ic2.tile.matter.TileQuantumMatterGen;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

@SimpleGuiHandler(index = GuiHandlers.customMatterGen, tile = TileCustomMatterGen.class, gui = GuiCustomMatterGen.class, container = ContainerCustomMatterGen.class, integration = "IC2")
public class BlockCustomMatterGen extends BaseIC2Block implements IMatter {

    private String name;

    public BlockCustomMatterGen(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int s, float f1, float f2, float f3) {
        if (player.isSneaking())
            return false;
        if (world.isRemote)
            return true;
        TileEntity tileentity = world.getTileEntity(x, y, z);
        if (tileentity != null && world.getTileEntity(x, y, z) instanceof TileCustomMatterGen)
            player.openGui(HellCore.instance, GuiHandlers.customMatterGen, world, x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        switch (name) {
            case "quantum_matter": return new TileQuantumMatterGen();
            case "nano_matter": return new TileNanoMatterGen();
            default: return new TileAdvancedMatterGen();
        }
    }

    public int getMatter() {
        switch (name) {
            case "quantum_matter": return HellConfig.quantumMatterGeneration;
            case "nano_matter": return HellConfig.nanoMatterGeneration;
            default: return HellConfig.advancedMatterGeneration;
        }
    }

    public int getTank() {
        switch (name) {
            case "quantum_matter": return HellConfig.quantumMatterTank;
            case "nano_matter": return HellConfig.nanoMatterTank;
            default: return HellConfig.advancedMatterTank;
        }
    }

    public FluidStack getFluid() {
        return new FluidStack(BlocksItems.getFluid(InternalName.fluidUuMatter), getMatter());
    }
}
