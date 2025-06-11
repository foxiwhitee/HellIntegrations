package foxiwhitee.hellmod.integration.botania.tile.spreaders;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;

public class TileValhallaSpreader extends TileCustomSpreader{
    @Override
    public String getName() {
        return BotaniaIntegration.valhallaSpreader.getUnlocalizedName().replace("tile.", "");
    }

    @Override
    public int getManaPerSec() {
        return HellConfig.manaPerSecValhallaSpreader;
    }
}
