package foxiwhitee.hellmod.integration.botania.tile;

import foxiwhitee.hellmod.config.HellConfig;
import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;

public class SubTileHelibrium extends SubTileGenerating {
    public static LexiconEntry lexicon;

    public int getMaxMana() {
        return HellConfig.helibriumGeneration;
    }

    public int getColor() {
        return 1179392;
    }

    public LexiconEntry getEntry() {
        return lexicon;
    }

    public boolean canGeneratePassively() {
        return true;
    }

    public int getDelayBetweenPassiveGeneration() {
        return 2;
    }

    public int getValueForPassiveGeneration() {
        return HellConfig.helibriumGeneration;
    }

    public IIcon getIcon() {
        return BotaniaAPI.getSignatureForName("helibrium").getIconForStack(null);
    }
}
