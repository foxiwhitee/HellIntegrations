package foxiwhitee.hellmod.integration.botania.flowers;

import foxiwhitee.hellmod.config.EffectConfig;

public enum EffectTypes {
    BONUS(EffectConfig.effectsManaBonusMinPositive, EffectConfig.effectsManaBonusMaxPositive, EffectConfig.effectsManaBonusMinNegative, EffectConfig.effectsManaBonusMaxNegative),
    DAY_BONUS(EffectConfig.effectsManaDayBonusMinPositive, EffectConfig.effectsManaDayBonusMaxPositive, EffectConfig.effectsManaDayBonusMinNegative, EffectConfig.effectsManaDayBonusMaxNegative),
    NIGHT_BONUS(EffectConfig.effectsManaNightBonusMinPositive, EffectConfig.effectsManaNightBonusMaxPositive, EffectConfig.effectsManaNightBonusMinNegative, EffectConfig.effectsManaNightBonusMaxNegative),
    RAIN_BONUS(EffectConfig.effectsManaRainBonusMinPositive, EffectConfig.effectsManaRainBonusMaxPositive, EffectConfig.effectsManaRainBonusMinNegative, EffectConfig.effectsManaRainBonusMaxNegative),
    FLAME_MATERIAL_BONUS(EffectConfig.effectsManaFlameMaterialBonusMinPositive, EffectConfig.effectsManaFlameMaterialBonusMaxPositive, EffectConfig.effectsManaFlameMaterialBonusMinNegative, EffectConfig.effectsManaFlameMaterialBonusMaxNegative),
    CHANCE_TO_FREE_ITEM_USE(EffectConfig.effectsManaChanceToFreeItemUseMinPositive, EffectConfig.effectsManaChanceToFreeItemUseMaxPositive, EffectConfig.effectsManaChanceToFreeItemUseMinNegative, EffectConfig.effectsManaChanceToFreeItemUseMaxNegative),
    SPEED_BONUS(EffectConfig.effectsManaSpeedBonusMinPositive, EffectConfig.effectsManaSpeedBonusMaxPositive, EffectConfig.effectsManaSpeedBonusMinNegative, EffectConfig.effectsManaSpeedBonusMaxNegative),
    EXP_BONUS(EffectConfig.effectsManaExpBonusMinPositive, EffectConfig.effectsManaExpBonusMaxPositive, EffectConfig.effectsManaExpBonusMinNegative, EffectConfig.effectsManaExpBonusMaxNegative);

    final double minPositive;
    final double minNegative;
    final double maxPositive;
    final double maxNegative;

    EffectTypes(double minPositive, double maxPositive, double minNegative, double maxNegative) {
        this.minNegative = minNegative;
        this.minPositive = minPositive;
        this.maxNegative = maxNegative;
        this.maxPositive = maxPositive;
    }

    public boolean isAllowed(LevelTypes type) {
        switch (this) {
            case FLAME_MATERIAL_BONUS: return type == LevelTypes.BURNING || type == LevelTypes.DESTRUCTIVE || type == LevelTypes.ETERNAL_SOLSTICE || type == LevelTypes.ASGARD;
            case EXP_BONUS: return type == LevelTypes.CRYSTAL || type == LevelTypes.WILD_HUNT || type == LevelTypes.YGGDRASIL || type == LevelTypes.ASGARD;
            default: return true;
        }
    }

}
