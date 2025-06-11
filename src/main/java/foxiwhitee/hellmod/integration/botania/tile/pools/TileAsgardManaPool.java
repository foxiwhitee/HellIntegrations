package foxiwhitee.hellmod.integration.botania.tile.pools;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;

public class TileAsgardManaPool extends TileCustomManaPool{
    @Override
    public int getMaxMana() {
        return HellConfig.manaAsgardPool;
    }

    @Override
    public String getName() {
        return BotaniaIntegration.asgardPool.getUnlocalizedName().replace("tile.", "");
    }
}
