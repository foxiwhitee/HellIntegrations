package foxiwhitee.hellmod.integration.botania.tile.spreaders;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;

public class TileAsgardSpreader extends TileCustomSpreader{
    @Override
    public String getName() {
        return BotaniaIntegration.asgardSpreader.getUnlocalizedName().replace("tile.", "");
    }

    @Override
    public int getManaPerSec() {
        return HellConfig.manaPerSecAsgardSpreader;
    }
}
