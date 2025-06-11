package foxiwhitee.hellmod.integration.botania.helpers;

import appeng.api.util.DimensionalCoord;

public interface IManaUpdateTile {
    DimensionalCoord getLocation();
    long getStoredMana();
    long getMaxStoredMana();
    void setStoredMana(long storedMana);
    void setMaxStoredMana(long storedMana);
}
