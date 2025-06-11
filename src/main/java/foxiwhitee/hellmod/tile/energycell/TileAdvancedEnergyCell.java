package foxiwhitee.hellmod.tile.energycell;

import foxiwhitee.hellmod.config.HellConfig;

public class TileAdvancedEnergyCell extends TileCustomEnergyCell{
    @Override
    protected double getInternalMaxPower() {
        return HellConfig.advancedEnergyCellPower;
    }
}
