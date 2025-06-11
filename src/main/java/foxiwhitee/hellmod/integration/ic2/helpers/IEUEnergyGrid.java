package foxiwhitee.hellmod.integration.ic2.helpers;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridCache;

public interface IEUEnergyGrid extends IGridCache {
    double extractPower(double paramDouble);
}

