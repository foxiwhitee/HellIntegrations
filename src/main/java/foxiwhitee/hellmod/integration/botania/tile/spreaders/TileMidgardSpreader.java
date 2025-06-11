package foxiwhitee.hellmod.integration.botania.tile.spreaders;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;

public class TileMidgardSpreader extends TileCustomSpreader{
    @Override
    public String getName() {
        return BotaniaIntegration.midgardSpreader.getUnlocalizedName().replace("tile.", "");
    }

    @Override
    public int getManaPerSec() {
        return HellConfig.manaPerSecMidgardSpreader;
    }
}
