package foxiwhitee.hellmod.integration.botania.tile.pools;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;

public class TileValhallaManaPool extends TileCustomManaPool{
    @Override
    public int getMaxMana() {
        return HellConfig.manaValhallaPool;
    }

    @Override
    public String getName() {
        return BotaniaIntegration.valhallaPool.getUnlocalizedName().replace("tile.", "");
    }
}
