package foxiwhitee.hellmod.integration.botania.flowers;

import foxiwhitee.hellmod.config.EffectConfig;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.Random;

public enum PartRarity {
    COMMON(EffectConfig.rarityCommonChance, EffectConfig.rarityCommonMultiplier, EffectConfig.rarityCommonMaxPositive, EffectConfig.rarityCommonMaxNegative),
    UNCOMMON(EffectConfig.rarityUncommonChance, EffectConfig.rarityUncommonMultiplier,  EffectConfig.rarityUncommonMaxPositive, EffectConfig.rarityUncommonMaxNegative),
    RARE(EffectConfig.rarityRareChance, EffectConfig.rarityRareMultiplier, EffectConfig.rarityRareMaxPositive, EffectConfig.rarityRareMaxNegative),
    EPIC(EffectConfig.rarityEpicChance, EffectConfig.rarityEpicMultiplier,  EffectConfig.rarityEpicMaxPositive, EffectConfig.rarityEpicMaxNegative),
    LEGENDARY(EffectConfig.rarityLegendaryChance, EffectConfig.rarityLegendaryMultiplier,  EffectConfig.rarityLegendaryMaxPositive, EffectConfig.rarityLegendaryMaxNegative),
    DIVINE(EffectConfig.rarityDivineChance, EffectConfig.rarityDivineMultiplier,  EffectConfig.rarityDivineMaxPositive, EffectConfig.rarityDivineMaxNegative),
    FOX(EffectConfig.rarityFoxChance, EffectConfig.rarityFoxMultiplier,  EffectConfig.rarityFoxMaxPositive, EffectConfig.rarityFoxMaxNegative);

    private final double multiplier;
    private final int maxPositive;
    private final int maxNegative;
    private final int chance;
    private static final PartRarity[] VALUES = values();
    private static final int[] upgradeBonusCount = {2, 3, 4, 5, LEGENDARY.maxNegative + LEGENDARY.maxPositive};
    private static final double[] upgradeBonus = {1.03, 1.04, 1.06, 1.09, 1.1};

    PartRarity(int chance, double multiplier, int maxPositive, int maxNegative) {
        this.multiplier = multiplier;
        this.maxPositive = maxPositive;
        this.maxNegative = maxNegative;
        this.chance = chance;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getMaxPositive() {
        return maxPositive;
    }

    public int getMaxNegative() {
        return maxNegative;
    }

    public int getMaxEffects() {return getMaxNegative() + getMaxPositive();}

    public static PartRarity generateRarity() {
        int totalWeight = 0;
        for (PartRarity value : values()) {
            totalWeight += value.getChance();
        }

        Random random = new Random();
        int r = random.nextInt(totalWeight) + 1;

        int cumulative = 0;
        for (PartRarity value : values()) {
            cumulative += value.getChance();
            if (r <= cumulative) {
                return value;
            }
        }

        throw new IllegalStateException("Should never reach here");
    }

    public static void generateDescription(NBTTagCompound tag, List<String> list) {
        if (tag == null || list == null) throw new IllegalArgumentException("tag and list must not be null");
        if (!tag.hasKey("rarity")) return;

        PartRarity rarity = PartRarity.valueOf(tag.getString("rarity").toUpperCase());
        if (rarity == LEGENDARY) {
            list.add(LocalizationUtils.makeRainbow("LEGENDARY ITEM", EffectConfig.rarityLegendaryColorSpeed, 1, true));
        } else if (rarity == DIVINE) {
            list.add(LocalizationUtils.makeDivine("DIVINE ITEM", EffectConfig.rarityDivineColorSpeed, 1, true));
        } else if (rarity == FOX) {
            list.add(LocalizationUtils.makeFox("FOX'S ITEM", EffectConfig.rarityFoxColorSpeed, 1, true));
        } else {
            String color = "";
            switch (rarity) {
                case COMMON: color = EffectConfig.rarityCommonColor; break;
                case UNCOMMON: color = EffectConfig.rarityUncommonColor; break;
                case RARE: color = EffectConfig.rarityRareColor; break;
                case EPIC: color = EffectConfig.rarityEpicColor; break;
            }
            list.add(EnumChatFormatting.RESET + color + EnumChatFormatting.BOLD + rarity + " ITEM");
        }
    }

    public int getChance() {
        return chance;
    }

}
