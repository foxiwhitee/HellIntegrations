package foxiwhitee.hellmod.integration.ic2.helpers;

import appeng.api.util.DimensionalCoord;

public interface IEnergyUpdateTile {
    DimensionalCoord getCoord();

    void setEnergyStoredForUpdate(double paramDouble);

    double getEnergyStoredForUpdate();

    void setMaxEnergyStoredForUpdate(double paramDouble);

    double getMaxEnergyStoredForUpdate();

    default double getEnergyPerItemForUpdate() {
        return 0.0D;
    }

    default void setEnergyPerItemUpdate(double energy) {}

    default int getTicksPerOpForUpdate() {
        return 0;
    }

    default void setTicksPerOpForUpdate(int ticks) {}
}
