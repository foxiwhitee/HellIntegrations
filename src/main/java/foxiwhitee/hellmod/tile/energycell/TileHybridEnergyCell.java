package foxiwhitee.hellmod.tile.energycell;

import foxiwhitee.hellmod.config.HellConfig;

public class TileHybridEnergyCell extends TileCustomEnergyCell{
    @Override
    protected double getInternalMaxPower() {
        return HellConfig.hybridEnergyCellPower;
    }
}
