package foxiwhitee.hellmod.integration.ic2.tile.generators.panels;

import foxiwhitee.hellmod.config.HellConfig;

public class TileSolarPanelLevel5 extends TileCustomSolarPanel{
    @Override
    public int getGenNight() {
        return HellConfig.panel5GenNight;
    }

    @Override
    public int getGenDay() {
        return HellConfig.panel5GenDay;
    }

    @Override
    public int getMaxStorage() {
        return this.getGenDay() * 10;
    }

    @Override
    public int getProduction() {
        return this.getGenDay() * 2;
    }

    @Override
    public String getName() {
        return "panel5";
    }
}
