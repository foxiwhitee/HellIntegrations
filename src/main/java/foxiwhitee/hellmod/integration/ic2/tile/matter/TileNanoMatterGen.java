package foxiwhitee.hellmod.integration.ic2.tile.matter;

import foxiwhitee.hellmod.config.HellConfig;
import net.minecraftforge.fluids.FluidTank;

public class TileNanoMatterGen extends TileCustomMatterGen{
    protected final FluidTank fluidTank;
    public TileNanoMatterGen() {
        this.fluidTank = new FluidTank(HellConfig.nanoMatterTank);
    }
    @Override
    public String getName() {
        return "nano_matter";
    }

    @Override
    public int getMatter() {
        return HellConfig.nanoMatterGeneration;
    }

    @Override
    public FluidTank getFluidTank() {
        return this.fluidTank;
    }
}
