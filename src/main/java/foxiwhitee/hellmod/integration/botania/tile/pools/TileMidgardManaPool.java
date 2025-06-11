package foxiwhitee.hellmod.integration.botania.tile.pools;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;

public class TileMidgardManaPool extends TileCustomManaPool{
    @Override
    public int getMaxMana() {
        return HellConfig.manaMidgardPool;
    }

    @Override
    public String getName() {
        return BotaniaIntegration.midgardPool.getUnlocalizedName().replace("tile.", "");
    }
}
