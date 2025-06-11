package foxiwhitee.hellmod.config;

@Config(folder = "Fox-Mods", name = "Hell-Integration-FlowerLogic")
public final class FlowerLogicConfig {

    // Generating
    @ConfigValue(name = "ticks", category = "Logic.Generating.Daybloom", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicDaybloomTicks = 12;
    @ConfigValue(name = "generating", category = "Logic.Generating.Daybloom", desc = "How much mana will a flower generate?")
    public static long flowerLogicDaybloomGenerating = 1;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Daybloom", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicDaybloomLevel = 1;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Endoflame", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicEndoflameTicks = 2;
    @ConfigValue(name = "generating", category = "Logic.Generating.Endoflame", desc = "How much mana will a flower generate?")
    public static long flowerLogicEndoflameGenerating = 3;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Endoflame", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicEndoflameLevel = 1;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Entropinnyum", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicEntropinnyumTicks = 1;
    @ConfigValue(name = "generating", category = "Logic.Generating.Entropinnyum", desc = "How much mana will a flower generate?")
    public static long flowerLogicEntropinnyumGenerating = 6500;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Entropinnyum", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicEntropinnyumLevel = 3;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Gourmaryllis", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicGourmaryllisTicks = 48;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Gourmaryllis", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicGourmaryllisLevel = 3;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Hydroangeas", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicHydroangeasTicks = 2;
    @ConfigValue(name = "generating", category = "Logic.Generating.Hydroangeas", desc = "How much mana will a flower generate?")
    public static long flowerLogicHydroangeasGenerating = 1;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Hydroangeas", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicHydroangeasLevel = 1;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Kekimurus", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicKekimurusTicks = 80 * 7;
    @ConfigValue(name = "generating", category = "Logic.Generating.Kekimurus", desc = "How much mana will a flower generate?")
    public static long flowerLogicKekimurusGenerating = 1800 * 7;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Kekimurus", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicKekimurusLevel = 2;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Munchdew", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicMunchdewTicks = 4;
    @ConfigValue(name = "generating", category = "Logic.Generating.Munchdew", desc = "How much mana will a flower generate?")
    public static long flowerLogicMunchdewGenerating = 160;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Munchdew", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicMunchdewLevel = 2;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Nightshade", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicNightshadeTicks = 12;
    @ConfigValue(name = "generating", category = "Logic.Generating.Nightshade", desc = "How much mana will a flower generate?")
    public static long flowerLogicNightshadeGenerating = 1;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Nightshade", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicNightshadeLevel = 1;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Rafflowsia", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicRafflowsiaTicks = 10;
    @ConfigValue(name = "generating", category = "Logic.Generating.Rafflowsia", desc = "How much mana will a flower generate?")
    public static long flowerLogicRafflowsiaGenerating = 2100;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Rafflowsia", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicRafflowsiaLevel = 1;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Spectrolus", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicSpectrolusTicks = 1;
    @ConfigValue(name = "generating", category = "Logic.Generating.Spectrolus", desc = "How much mana will a flower generate?")
    public static long flowerLogicSpectrolusGenerating = 300;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Spectrolus", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicSpectrolusLevel = 1;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Thermalily", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicThermalilyTicks = 1;
    @ConfigValue(name = "generating", category = "Logic.Generating.Thermalily", desc = "How much mana will a flower generate?")
    public static long flowerLogicThermalilyGenerating = 20;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Thermalily", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicThermalilyLevel = 1;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Arcanerose", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicArcaneroseTicks = 1;
    @ConfigValue(name = "generating", category = "Logic.Generating.Arcanerose", desc = "How much mana will a flower generate?")
    public static long flowerLogicArcaneroseGenerating = 50;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Arcanerose", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicArcaneroseLevel = 4;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Asgardandelion", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicAsgardandelionTicks = 10000000;
    @ConfigValue(name = "generating", category = "Logic.Generating.Asgardandelion", desc = "How much mana will a flower generate?")
    public static long flowerLogicAsgardandelionGenerating = 1000000;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Asgardandelion", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicAsgardandelionLevel = 5;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Helibrium", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicHelibriumTicks = 10000000;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Helibrium", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicHelibriumLevel = 5;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Midgaran", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicMidgaranTicks = 10000000;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Midgaran", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicMidgaranLevel = 5;

    @ConfigValue(name = "ticks", category = "Logic.Generating.Valharin", desc = "How many ticks will it take to generate mana (Multiplied by 1000)")
    public static int flowerLogicValharinTicks = 10000000;
    @ConfigValue(name = "level", max = "5", category = "Logic.Generating.Valharin", desc = "From what level of mana generator will the flower work?")
    public static int flowerLogicValharinLevel = 5;


    // Functional
    @ConfigValue(name = "ticks", category = "Logic.Functional.AdedAmaranthus", desc = "How many ticks will it take to do action (Multiplied by 1000)")
    public static int flowerLogicAdedAmaranthusTicks = 30;
    @ConfigValue(name = "consume", category = "Logic.Functional.AdedAmaranthus", desc = "How much mana will a flower consume?")
    public static long flowerLogicAdedAmaranthusConsume = 100;

    @ConfigValue(name = "ticks", category = "Logic.Functional.Loonuim", desc = "How many ticks will it take to do action (Multiplied by 1000)")
    public static int flowerLogicLoonuimTicks = 300;
    @ConfigValue(name = "consume", category = "Logic.Functional.Loonuim", desc = "How much mana will a flower consume?")
    public static long flowerLogicLoonuimConsume = 35000;

    @ConfigValue(name = "ticks", category = "Logic.Functional.Orechid", desc = "How many ticks will it take to do action (Multiplied by 1000)")
    public static int flowerLogicOrechidTicks = 100;
    @ConfigValue(name = "consume", category = "Logic.Functional.Orechid", desc = "How much mana will a flower consume?")
    public static long flowerLogicOrechidConsume = 17500;

    @ConfigValue(name = "ticks", category = "Logic.Functional.OrechidIgnem", desc = "How many ticks will it take to do action (Multiplied by 1000)")
    public static int flowerLogicOrechidIgnemTicks = 100;
    @ConfigValue(name = "consume", category = "Logic.Functional.OrechidIgnem", desc = "How much mana will a flower consume?")
    public static long flowerLogicOrechidIgnemConsume = 17500;



}