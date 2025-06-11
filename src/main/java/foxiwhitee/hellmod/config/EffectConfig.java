package foxiwhitee.hellmod.config;

import net.minecraft.util.EnumChatFormatting;

@Config(folder = "Fox-Mods", name = "Hell-Integration-Effects")
public final class EffectConfig {

    // Rarity
    @ConfigValue(name = "chance", category = "Rarity.Common", desc = "Chance of this rarity occurring (All chances are summed up and 1 number is randomly selected from the sum, if the number is within the rarity range, it will occur)")
    public static int rarityCommonChance = 4329920;
    @ConfigValue(name = "multiplier", category = "Rarity.Common", min = "1", desc = "How many times does the value of effects increase under the influence of rarity?")
    public static double rarityCommonMultiplier = 1.0;
    @ConfigValue(name = "max-positive", category = "Rarity.Common", min = "1", max = "1073741823", desc = "Maximum number of positive effects for this rarity")
    public static int rarityCommonMaxPositive = 6;
    @ConfigValue(name = "max-negative", category = "Rarity.Common", max = "1073741823", desc = "Maximum number of negative effects for this rarity")
    public static int rarityCommonMaxNegative = 5;
    @ConfigValue(name = "color", category = "Rarity.Common", desc = "The color that will be added before the rarity name (You can use the standard ones from Minecraft or HEX. In fact, this is a prefix. The text will always be bold!)")
    public static String rarityCommonColor = EnumChatFormatting.RESET.toString();

    @ConfigValue(name = "chance", category = "Rarity.Uncommon", desc = "Chance of this rarity occurring (All chances are summed up and 1 number is randomly selected from the sum, if the number is within the rarity range, it will occur)")
    public static int rarityUncommonChance = 2670000;
    @ConfigValue(name = "multiplier", category = "Rarity.Uncommon", min = "1", desc = "How many times does the value of effects increase under the influence of rarity?")
    public static double rarityUncommonMultiplier = 1.5;
    @ConfigValue(name = "max-positive", category = "Rarity.Uncommon", min = "1", max = "1073741823", desc = "Maximum number of positive effects for this rarity")
    public static int rarityUncommonMaxPositive = 8;
    @ConfigValue(name = "max-negative", category = "Rarity.Uncommon", max = "1073741823", desc = "Maximum number of negative effects for this rarity")
    public static int rarityUncommonMaxNegative = 4;
    @ConfigValue(name = "color", category = "Rarity.Uncommon", desc = "The color that will be added before the rarity name (You can use the standard ones from Minecraft or HEX. In fact, this is a prefix. The text will always be bold!)")
    public static String rarityUncommonColor = EnumChatFormatting.AQUA.toString();

    @ConfigValue(name = "chance", category = "Rarity.Rare", desc = "Chance of this rarity occurring (All chances are summed up and 1 number is randomly selected from the sum, if the number is within the rarity range, it will occur)")
    public static int rarityRareChance = 1960000;
    @ConfigValue(name = "multiplier", category = "Rarity.Rare", min = "1", desc = "How many times does the value of effects increase under the influence of rarity?")
    public static double rarityRareMultiplier = 2.3;
    @ConfigValue(name = "max-positive", category = "Rarity.Rare", min = "1", max = "1073741823", desc = "Maximum number of positive effects for this rarity")
    public static int rarityRareMaxPositive = 9;
    @ConfigValue(name = "max-negative", category = "Rarity.Rare", max = "1073741823", desc = "Maximum number of negative effects for this rarity")
    public static int rarityRareMaxNegative = 4;
    @ConfigValue(name = "color", category = "Rarity.Rare", desc = "The color that will be added before the rarity name (You can use the standard ones from Minecraft or HEX. In fact, this is a prefix. The text will always be bold!)")
    public static String rarityRareColor = EnumChatFormatting.BLUE.toString();

    @ConfigValue(name = "chance", category = "Rarity.Epic", desc = "Chance of this rarity occurring (All chances are summed up and 1 number is randomly selected from the sum, if the number is within the rarity range, it will occur)")
    public static int rarityEpicChance = 970000;
    @ConfigValue(name = "multiplier", category = "Rarity.Epic", min = "1", desc = "How many times does the value of effects increase under the influence of rarity?")
    public static double rarityEpicMultiplier = 5.0;
    @ConfigValue(name = "max-positive", category = "Rarity.Epic", min = "1", max = "1073741823", desc = "Maximum number of positive effects for this rarity")
    public static int rarityEpicMaxPositive = 12;
    @ConfigValue(name = "max-negative", category = "Rarity.Epic", max = "1073741823", desc = "Maximum number of negative effects for this rarity")
    public static int rarityEpicMaxNegative = 3;
    @ConfigValue(name = "color", category = "Rarity.Epic", desc = "The color that will be added before the rarity name (You can use the standard ones from Minecraft or HEX. In fact, this is a prefix. The text will always be bold!)")
    public static String rarityEpicColor = EnumChatFormatting.DARK_PURPLE.toString();

    @ConfigValue(name = "chance", category = "Rarity.Legendary", desc = "Chance of this rarity occurring (All chances are summed up and 1 number is randomly selected from the sum, if the number is within the rarity range, it will occur)")
    public static int rarityLegendaryChance = 70000;
    @ConfigValue(name = "multiplier", category = "Rarity.Legendary", min = "1", desc = "How many times does the value of effects increase under the influence of rarity?")
    public static double rarityLegendaryMultiplier = 10;
    @ConfigValue(name = "max-positive", category = "Rarity.Legendary", min = "1", max = "1073741823", desc = "Maximum number of positive effects for this rarity")
    public static int rarityLegendaryMaxPositive = 15;
    @ConfigValue(name = "max-negative", category = "Rarity.Legendary", max = "1073741823", desc = "Maximum number of negative effects for this rarity")
    public static int rarityLegendaryMaxNegative = 2;
    @ConfigValue(name = "color-speed", category = "Rarity.Legendary", desc = "Color animation speed. Not recommended to change")
    public static double rarityLegendaryColorSpeed = 100;

    @ConfigValue(name = "chance", category = "Rarity.Divine", desc = "Chance of this rarity occurring (All chances are summed up and 1 number is randomly selected from the sum, if the number is within the rarity range, it will occur)")
    public static int rarityDivineChance = 79;
    @ConfigValue(name = "multiplier", category = "Rarity.Divine", min = "1", desc = "How many times does the value of effects increase under the influence of rarity?")
    public static double rarityDivineMultiplier = 112.0;
    @ConfigValue(name = "max-positive", category = "Rarity.Divine", min = "1", max = "1073741823", desc = "Maximum number of positive effects for this rarity")
    public static int rarityDivineMaxPositive = 19;
    @ConfigValue(name = "max-negative", category = "Rarity.Divine", max = "1073741823", desc = "Maximum number of negative effects for this rarity")
    public static int rarityDivineMaxNegative = 1;
    @ConfigValue(name = "color-speed", category = "Rarity.Divine", desc = "Color animation speed. Not recommended to change")
    public static double rarityDivineColorSpeed = 100;

    @ConfigValue(name = "chance", category = "Rarity.Fox", desc = "Chance of this rarity occurring (All chances are summed up and 1 number is randomly selected from the sum, if the number is within the rarity range, it will occur)")
    public static int rarityFoxChance = 1;
    @ConfigValue(name = "multiplier", category = "Rarity.Fox", min = "1", desc = "How many times does the value of effects increase under the influence of rarity?")
    public static double rarityFoxMultiplier = 9800.0;
    @ConfigValue(name = "max-positive", category = "Rarity.Fox", min = "1", max = "1073741823", desc = "Maximum number of positive effects for this rarity")
    public static int rarityFoxMaxPositive = 31;
    @ConfigValue(name = "max-negative", category = "Rarity.Fox", max = "1073741823", desc = "Maximum number of negative effects for this rarity")
    public static int rarityFoxMaxNegative = 0;
    @ConfigValue(name = "color-speed", category = "Rarity.Fox", desc = "Color animation speed. Not recommended to change")
    public static double rarityFoxColorSpeed = 100;


    // Desc
    @ConfigValue(category = "Desc.Elements", desc = "What color will \"%\" be?")
    public static String percentColor = EnumChatFormatting.DARK_PURPLE.toString();
    @ConfigValue(category = "Desc.Elements", desc = "What color will \":\" be?")
    public static String colonColor = EnumChatFormatting.AQUA.toString();
    @ConfigValue(category = "Desc.Elements", desc = "What color will \"(\" and \")\" be?")
    public static String bracketColor = EnumChatFormatting.WHITE.toString();

    @ConfigValue(name = "fox", category = "Desc.Threshold", desc = "If the effect value is greater than or equal to this, the color will be as in the Fox rarity (Value cannot be equal to a similar value)")
    public static int thresholdFox = 100000;
    @ConfigValue(name = "divine", category = "Desc.Threshold", desc = "If the effect value is greater than or equal to this, the color will be as in the Divine rarity (Value cannot be equal to a similar value)")
    public static int thresholdDivine = 10000;
    @ConfigValue(name = "legendary", category = "Desc.Threshold", desc = "If the effect value is greater than or equal to this, the color will be as in the Legendary rarity (Value cannot be equal to a similar value)")
    public static int thresholdLegendary = 500;
    @ConfigValue(name = "level3", category = "Desc.Threshold", min = "-2147483647", desc = "If the effect value is greater than or equal to this then the color will be equal to Color3 (Value cannot be equal to a similar value)")
    public static int threshold3 = 100;
    @ConfigValue(name = "level2", category = "Desc.Threshold", min = "-2147483647", desc = "If the effect value is greater than or equal to this then the color will be equal to Color2 (Value cannot be equal to a similar value)")
    public static int threshold2 = 1;
    @ConfigValue(name = "level1", category = "Desc.Threshold", min = "-2147483647", desc = "If the effect value is greater than or equal to this then the color will be equal to Color1 (Value cannot be equal to a similar value)")
    public static int threshold1 = -99;

    @ConfigValue(name = "color3", category = "Desc.Color", desc = "The color of the effect value that will be displayed if the effect value is greater than or equal to threshold3")
    public static String descColor3 = EnumChatFormatting.GREEN.toString();
    @ConfigValue(name = "color2", category = "Desc.Color", desc = "The color of the effect value that will be displayed if the effect value is greater than or equal to threshold2")
    public static String descColor2 = EnumChatFormatting.DARK_GREEN.toString();
    @ConfigValue(name = "color1", category = "Desc.Color", desc = "The color of the effect value that will be displayed if the effect value is greater than or equal to threshold1")
    public static String descColor1 = EnumChatFormatting.RED.toString();
    @ConfigValue(name = "min", category = "Desc.Color", desc = "The color of the effect value that will be displayed if the effect value is minimal")
    public static String descColorMin = EnumChatFormatting.DARK_RED.toString();


    // Effects
    @ConfigValue(name = "min-positive", min = "0.0000001", category = "Effects.Botania.Mana.Bonus", desc = "Minimum positive bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaBonusMinPositive = 0.1;
    @ConfigValue(name = "max-positive", min = "0.0000001", category = "Effects.Botania.Mana.Bonus", desc = "Maximum positive bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaBonusMaxPositive = 0.7;
    @ConfigValue(name = "min-negative", min = "0", category = "Effects.Botania.Mana.Bonus", desc = "Minimum negative bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaBonusMinNegative = 1;
    @ConfigValue(name = "max-negative", min = "0", category = "Effects.Botania.Mana.Bonus", desc = "Maximum negative bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaBonusMaxNegative = 0.05;

    @ConfigValue(name = "min-positive", min = "0.0000001", category = "Effects.Botania.Mana.DayBonus", desc = "Minimum positive day bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaDayBonusMinPositive = 0.05;
    @ConfigValue(name = "max-positive", min = "0.0000001", category = "Effects.Botania.Mana.DayBonus", desc = "Maximum positive day bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaDayBonusMaxPositive = 0.5;
    @ConfigValue(name = "min-negative", min = "0", category = "Effects.Botania.Mana.DayBonus", desc = "Minimum negative day bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaDayBonusMinNegative = 1;
    @ConfigValue(name = "max-negative", min = "0", category = "Effects.Botania.Mana.DayBonus", desc = "Maximum negative day bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaDayBonusMaxNegative = 0.05;

    @ConfigValue(name = "min-positive", min = "0.0000001", category = "Effects.Botania.Mana.NightBonus", desc = "Minimum positive night bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaNightBonusMinPositive = 0.05;
    @ConfigValue(name = "max-positive", min = "0.0000001", category = "Effects.Botania.Mana.NightBonus", desc = "Maximum positive night bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaNightBonusMaxPositive = 0.5;
    @ConfigValue(name = "min-negative", min = "0", category = "Effects.Botania.Mana.NightBonus", desc = "Minimum negative night bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaNightBonusMinNegative = 1;
    @ConfigValue(name = "max-negative", min = "0", category = "Effects.Botania.Mana.NightBonus", desc = "Maximum negative night bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaNightBonusMaxNegative = 0.05;

    @ConfigValue(name = "min-positive", min = "0.0000001", category = "Effects.Botania.Mana.RainBonus", desc = "Minimum positive rain bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaRainBonusMinPositive = 0.05;
    @ConfigValue(name = "max-positive", min = "0.0000001", category = "Effects.Botania.Mana.RainBonus", desc = "Maximum positive rain bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaRainBonusMaxPositive = 0.5;
    @ConfigValue(name = "min-negative", min = "0", category = "Effects.Botania.Mana.RainBonus", desc = "Minimum negative rain bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaRainBonusMinNegative = 2;
    @ConfigValue(name = "max-negative", min = "0", category = "Effects.Botania.Mana.RainBonus", desc = "Maximum negative rain bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaRainBonusMaxNegative = 0.5;

    @ConfigValue(name = "min-positive", min = "0.0000001", category = "Effects.Botania.Mana.FlameMaterialBonus", desc = "Minimum positive flame material bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaFlameMaterialBonusMinPositive = 0.05;
    @ConfigValue(name = "max-positive", min = "0.0000001", category = "Effects.Botania.Mana.FlameMaterialBonus", desc = "Maximum positive bonus flame material bonus (When calculating in percentages, multiply by 100)")
    public static double effectsManaFlameMaterialBonusMaxPositive = 0.8;
    @ConfigValue(name = "min-negative", min = "0", category = "Effects.Botania.Mana.FlameMaterialBonus", desc = "Minimum negative flame material bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaFlameMaterialBonusMinNegative = 0;
    @ConfigValue(name = "max-negative", min = "0", category = "Effects.Botania.Mana.FlameMaterialBonus", desc = "Maximum negative flame material bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaFlameMaterialBonusMaxNegative = 0;

    @ConfigValue(name = "min-positive", min = "0.0000001", category = "Effects.Botania.Mana.ChanceToFreeItemUse", desc = "Minimum positive chance to free item use value (When calculating in percentages, multiply by 100)")
    public static double effectsManaChanceToFreeItemUseMinPositive = 0.1;
    @ConfigValue(name = "max-positive", min = "0.0000001", category = "Effects.Botania.Mana.ChanceToFreeItemUse", desc = "Maximum positive bonus chance to free item use (When calculating in percentages, multiply by 100)")
    public static double effectsManaChanceToFreeItemUseMaxPositive = 0.5;
    @ConfigValue(name = "min-negative", min = "0", category = "Effects.Botania.Mana.ChanceToFreeItemUse", desc = "Minimum negative chance to free item use value (When calculating in percentages, multiply by 100)")
    public static double effectsManaChanceToFreeItemUseMinNegative = 0;
    @ConfigValue(name = "max-negative", min = "0", category = "Effects.Botania.Mana.ChanceToFreeItemUse", desc = "Maximum negative chance to free item use value (When calculating in percentages, multiply by 100)")
    public static double effectsManaChanceToFreeItemUseMaxNegative = 0;

    @ConfigValue(name = "min-positive", min = "0.0000001", category = "Effects.Botania.Mana.SpeedBonus", desc = "Minimum positive speed bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaSpeedBonusMinPositive = 0.1;
    @ConfigValue(name = "max-positive", min = "0.0000001", category = "Effects.Botania.Mana.SpeedBonus", desc = "Maximum positive bonus speed bonus (When calculating in percentages, multiply by 100)")
    public static double effectsManaSpeedBonusMaxPositive = 0.3;
    @ConfigValue(name = "min-negative", min = "0", category = "Effects.Botania.Mana.SpeedBonus", desc = "Minimum negative speed bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaSpeedBonusMinNegative = 0.2;
    @ConfigValue(name = "max-negative", min = "0", category = "Effects.Botania.Mana.SpeedBonus", desc = "Maximum negative speed bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaSpeedBonusMaxNegative = 0.05;

    @ConfigValue(name = "min-positive", min = "0.0000001", category = "Effects.Botania.Mana.ExpBonus", desc = "Minimum positive exp bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaExpBonusMinPositive = 0.1;
    @ConfigValue(name = "max-positive", min = "0.0000001", category = "Effects.Botania.Mana.ExpBonus", desc = "Maximum positive exp bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaExpBonusMaxPositive = 0.7;
    @ConfigValue(name = "min-negative", min = "0", category = "Effects.Botania.Mana.ExpBonus", desc = "Minimum negative exp bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaExpBonusMinNegative = 1;
    @ConfigValue(name = "max-negative", min = "0", category = "Effects.Botania.Mana.ExpBonus", desc = "Maximum negative exp bonus value (When calculating in percentages, multiply by 100)")
    public static double effectsManaExpBonusMaxNegative = 0.05;


}
