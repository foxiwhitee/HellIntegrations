package foxiwhitee.hellmod.integration.botania.tile.ae;

import foxiwhitee.hellmod.config.HellConfig;

public class TileManaGeneratorTier5 extends TileManaGenerator{
    public TileManaGeneratorTier5() {
        super(5, 5, HellConfig.manaAsgardPool * 10L, 8);
    }
}
