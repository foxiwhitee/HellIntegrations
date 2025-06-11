package foxiwhitee.hellmod.integration.botania.tile.pools;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;

public class TileHelHelmManaPool extends TileCustomManaPool{
    @Override
    public int getMaxMana() {
        return HellConfig.manaHelhelmPool;
    }

    @Override
    public String getName() {
        return BotaniaIntegration.helhelmPool.getUnlocalizedName().replace("tile.", "");
    }
}
