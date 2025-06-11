package foxiwhitee.hellmod.tile.energycell;

import foxiwhitee.hellmod.config.HellConfig;

public class TileUltimateEnergyCell extends TileCustomEnergyCell{
    @Override
    protected double getInternalMaxPower() {
        return HellConfig.ultimateEnergyCellPower;
    }
}
