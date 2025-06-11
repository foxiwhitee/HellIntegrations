package foxiwhitee.hellmod.helpers;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridCache;
import appeng.api.networking.security.BaseActionSource;
import net.minecraftforge.fluids.Fluid;

public interface IFluidStorageGrid extends IGridCache {
    long injectFluid(Fluid fluid, long paramLong, Actionable paramActionable, BaseActionSource paramBaseActionSource);

    long extractFluid(Fluid fluid, long paramLong, Actionable paramActionable, BaseActionSource paramBaseActionSource);
}
