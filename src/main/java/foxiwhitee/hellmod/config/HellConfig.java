package foxiwhitee.hellmod.config;

@Config(folder = "Fox-Mods", name = "Hell-Integration")
public class HellConfig {
    @ConfigValue(desc = "Enable tooltips?")
    public static boolean enable_tooltips = true;


    @ConfigValue(category = "Avaritia.NeutronTicks", name = "BaseNeutronCollectorTicks", desc = "Affects the rate of neutron production in the Basic Neutron Collector")
    public static int baseTicks = 6000;

    @ConfigValue(category = "Avaritia.NeutronTicks", name = "AdvancedNeutronCollectorTicks", desc = "Affects the rate of neutron production in the Advanced Neutron Collector")
    public static int advTicks = 5000;

    @ConfigValue(category = "Avaritia.NeutronTicks", name = "HybridNeutronCollectorTicks", desc = "Affects the rate of neutron production in the Hybrid Neutron Collector")
    public static int hybTicks = 4000;

    @ConfigValue(category = "Avaritia.NeutronTicks", name = "UltimateNeutronCollectorTicks", desc = "Affects the rate of neutron production in the Ultimate Neutron Collector")
    public static int ultTicks = 2500;

    @ConfigValue(category = "Avaritia.NeutronTicks", name = "QuantiumNeutronCollectorTicks", desc = "Affects the rate of neutron production in the Quantium Neutron Collector")
    public static int quantTicks = 1000;


    @ConfigValue(category = "Botania.Spark", desc = "The amount of mana a asgard spark transfers per second")
    public static int manaPerSecSparkAsgard = 1_250_000;

    @ConfigValue(category = "Botania.Spark", desc = "The amount of mana a helhelm spark transfers per second")
    public static int manaPerSecSparkHelhelm = 500_000;

    @ConfigValue(category = "Botania.Spark", desc = "The amount of mana a valhalla spark transfers per second")
    public static int manaPerSecSparkValhalla = 115_000;

    @ConfigValue(category = "Botania.Spark", desc = "The amount of mana a midgard spark transfers per second")
    public static int manaPerSecSparkMidgard = 25_000;


    @ConfigValue(category = "Botania.Pool", desc = "The amount of mana stored in the asgard mana pool")
    public static int manaAsgardPool = 750_000_000;

    @ConfigValue(category = "Botania.Pool", desc = "The amount of mana stored in the helhelm mana pool")
    public static int manaHelhelmPool = 250_000_000;

    @ConfigValue(category = "Botania.Pool", desc = "The amount of mana stored in the valhalla mana pool")
    public static int manaValhallaPool = 125_000_000;

    @ConfigValue(category = "Botania.Pool", desc = "The amount of mana stored in the midgard mana pool")
    public static int manaMidgardPool = 50_000_000;


    @ConfigValue(category = "Botania.Spreader", desc = "The amount of mana a asgard spreader transfers per second")
    public static int manaPerSecAsgardSpreader = 100_000_000;

    @ConfigValue(category = "Botania.Spreader", desc = "The amount of mana a helhelm spreader transfers per second")
    public static int manaPerSecHelhelmSpreader = 6_000_000;

    @ConfigValue(category = "Botania.Spreader", desc = "The amount of mana a valhalla spreader transfers per second")
    public static int manaPerSecValhallaSpreader = 2_500_000;

    @ConfigValue(category = "Botania.Spreader", desc = "The amount of mana a midgard spreader transfers per second")
    public static int manaPerSecMidgardSpreader = 700_000;


    @ConfigValue(category = "IC2.SolarPanels.Level1", name = "GenDay", desc = "Amount of energy produced by a level 1 solar panel during the day")
    public static int panel1GenDay = 1_024;

    @ConfigValue(category = "IC2.SolarPanels.Level1", name = "GenNight", desc = "Amount of energy produced by a level 1 solar panel at night")
    public static int panel1GenNight = 512;

    @ConfigValue(category = "IC2.SolarPanels.Level2", name = "GenDay", desc = "Amount of energy produced by a level 2 solar panel during the day")
    public static int panel2GenDay = 8_192;

    @ConfigValue(category = "IC2.SolarPanels.Level2", name = "GenNight", desc = "Amount of energy produced by a level 2 solar panel at night")
    public static int panel2GenNight = 4_096;

    @ConfigValue(category = "IC2.SolarPanels.Level3", name = "GenDay", desc = "Amount of energy produced by a level 3 solar panel during the day")
    public static int panel3GenDay = 32_768;

    @ConfigValue(category = "IC2.SolarPanels.Level3", name = "GenNight", desc = "Amount of energy produced by a level 3 solar panel at night")
    public static int panel3GenNight = 16_384;

    @ConfigValue(category = "IC2.SolarPanels.Level4", name = "GenDay", desc = "Amount of energy produced by a level 4 solar panel during the day")
    public static int panel4GenDay = 131_072;

    @ConfigValue(category = "IC2.SolarPanels.Level4", name = "GenNight", desc = "Amount of energy produced by a level 4 solar panel at night")
    public static int panel4GenNight = 65_536;

    @ConfigValue(category = "IC2.SolarPanels.Level5", name = "GenDay", desc = "Amount of energy produced by a level 5 solar panel during the day")
    public static int panel5GenDay = 524_288;

    @ConfigValue(category = "IC2.SolarPanels.Level5", name = "GenNight", desc = "Amount of energy produced by a level 5 solar panel at night")
    public static int panel5GenNight = 262_144;

    @ConfigValue(category = "IC2.SolarPanels.Level6", name = "GenDay", desc = "Amount of energy produced by a level 6 solar panel during the day")
    public static int panel6GenDay = 2_097_152;

    @ConfigValue(category = "IC2.SolarPanels.Level6", name = "GenNight", desc = "Amount of energy produced by a level 6 solar panel at night")
    public static int panel6GenNight = 1_048_576;

    @ConfigValue(category = "IC2.SolarPanels.Level7", name = "GenDay", desc = "Amount of energy produced by a level 7 solar panel during the day")
    public static int panel7GenDay = 8_388_608;

    @ConfigValue(category = "IC2.SolarPanels.Level7", name = "GenNight", desc = "Amount of energy produced by a level 7 solar panel at night")
    public static int panel7GenNight = 4_194_304;

    @ConfigValue(category = "IC2.SolarPanels.Level8", name = "GenDay", desc = "Amount of energy produced by a level 8 solar panel during the day")
    public static int panel8GenDay = 33_554_432;

    @ConfigValue(category = "IC2.SolarPanels.Level8", name = "GenNight", desc = "Amount of energy produced by a level 8 solar panel at night")
    public static int panel8GenNight = 16_777_216;


    @ConfigValue(category = "IC2.Generators", desc = "Amount of energy produced by a quantum generator")
    public static int quantumGeneratorGeneration = 100_000_000;

    @ConfigValue(category = "IC2.Generators", desc = "Amount of energy produced by a singular generator")
    public static int singularGeneratorGeneration = 200_000_000;


    @ConfigValue(category = "IC2.Matter", desc = "The amount of matter produced by an advanced matter generator in one operation")
    public static int advancedMatterGeneration = 8;

    @ConfigValue(category = "IC2.Matter", desc = "The amount of matter produced by a nano matter generator in one operation")
    public static int nanoMatterGeneration = 16;

    @ConfigValue(category = "IC2.Matter", desc = "The amount of matter produced by a quantum matter generator in one operation")
    public static int quantumMatterGeneration = 32;


    @ConfigValue(category = "IC2.Matter", desc = "The amount of matter that an advanced matter generator can store")
    public static int advancedMatterTank = 32_000;

    @ConfigValue(category = "IC2.Matter", desc = "The amount of matter that a nano matter generator can store")
    public static int nanoMatterTank = 96_000;

    @ConfigValue(category = "IC2.Matter", desc = "The amount of matter that a quantum matter generator can store")
    public static int quantumMatterTank = 128_000;


    @ConfigValue(category = "IC2.Matter", desc = "Number of threads for which the advanced scanner scans")
    public static int advancedScannerDuration = 10;


    @ConfigValue(category = "Botania.Flowers", desc = "The amount of mana produced by a helibrium in one operation")
    public static int helibriumGeneration = 10_000_000;

    @ConfigValue(category = "Botania.Flowers", desc = "The amount of mana produced by a valharin in one operation")
    public static int valharinGeneration = 30_000_000;

    @ConfigValue(category = "Botania.Flowers", desc = "The amount of mana produced by a midgaran in one operation")
    public static int midgaranGeneration = 60_000_000;


    @ConfigValue(category = "AE2.Cells", max = "1024", desc = "Number of types of items a storage cell stores from one million")
    public static int typesCellM = 126;

    @ConfigValue(category = "AE2.Cells", max = "1024", desc = "Number of types of items a storage cell can store from one billion")
    public static int typesCellG = 252;

    @ConfigValue(category = "AE2.Cells", max = "1024", desc = "Number of types of items how many storage cells can store from one trillion")
    public static int typesCellT = 504;

    @ConfigValue(category = "AE2.Cells", max = "1024", desc = "Number of types of items a storage cell holds per quadrillion")
    public static int typesCellP = 756;

    @ConfigValue(category = "AE2.Cells", max = "1024", desc = "Number of types of items how many storage cells can store from one quintillion")
    public static int typesCellE = 1;


    @ConfigValue(category = "AE2.FluidCells", max = "1024", desc = "Number of types of fluids a fluid storage cell stores from one thousand")
    public static int typesFluidCellK = 3;

    @ConfigValue(category = "AE2.FluidCells", max = "1024", desc = "Number of types of fluids a fluid storage cell stores from one million")
    public static int typesFluidCellM = 9;

    @ConfigValue(category = "AE2.FluidCells", max = "1024", desc = "Number of types of fluids a fluid storage cell can store from one billion")
    public static int typesFluidCellG = 27;

    @ConfigValue(category = "AE2.FluidCells", max = "1024", desc = "Number of types of fluids a fluid storage cells can store from one trillion")
    public static int typesFluidCellT = 63;


    @ConfigValue(category = "AE2.MolecularAssemblers", desc = "Number of items that the molecular assembler crafting per tick")
    public static long basic_molecular_assembler_speed = 200;

    @ConfigValue(category = "AE2.MolecularAssemblers", desc = "Number of items that the hybrid molecular assembler crafting per tick")
    public static long hybrid_molecular_assembler_speed = 25_000;

    @ConfigValue(category = "AE2.MolecularAssemblers", desc = "Number of items that the ultimate molecular assembler crafting per tick")
    public static long ultimate_molecular_assembler_speed = 25_000_000;


    @ConfigValue(category = "AE2.MolecularAssemblers", desc = "Amount of energy AE2 per tick (ae/t) consumed by the molecular assembler")
    public static int basic_molecular_assembler_power = 10;

    @ConfigValue(category = "AE2.MolecularAssemblers", desc = "Amount of energy AE2 per tick (ae/t) consumed by the hybrid molecular assembler")
    public static int hybrid_molecular_assembler_power = 50;

    @ConfigValue(category = "AE2.MolecularAssemblers", desc = "Amount of energy AE2 per tick (ae/t) consumed by the ultimate molecular assembler")
    public static int ultimate_molecular_assembler_power = 100;


    @ConfigValue(category = "IC2.Matter", desc = "Number of items that the advanced replicator crafting per tick")
    public static long advanced_replicator_speed = 10;

    @ConfigValue(category = "IC2.Matter", desc = "Number of items that the nano replicator crafting per tick")
    public static long nano_replicator_speed = 250;

    @ConfigValue(category = "IC2.Matter", desc = "Number of items that the quantum replicator crafting per tick")
    public static long quantum_replicator_speed = 1_000;


    @ConfigValue(category = "IC2.Matter", max = "100", desc = "Advanced replicator matter discount")
    public static byte advanced_replicator_discount = 1;

    @ConfigValue(category = "IC2.Matter", max = "100", desc = "Nano replicator matter discount")
    public static byte nano_replicator_discount = 10;

    @ConfigValue(category = "IC2.Matter", max = "100", desc = "Quantum replicator matter discount")
    public static byte quantum_replicator_discount = 50;


    @ConfigValue(category = "AE2.Accelerators", desc = "Advanced accelerator count")
    public static int advanced_accelerator = 9;

    @ConfigValue(category = "AE2.Accelerators", desc = "Hybrid accelerator count")
    public static int hybrid_accelerator = 27;

    @ConfigValue(category = "AE2.Accelerators", desc = "Ultimate accelerator count")
    public static int ultimate_accelerator = 81;

    @ConfigValue(category = "AE2.Accelerators", desc = "Quantum accelerator count")
    public static int quantum_accelerator = 243;


    @ConfigValue(category = "BloodMagic.Runes.Ichor", desc = "How many times is a rune more effective than a regular one?")
    public static int ichorRuneCapacity = 5;

    @ConfigValue(category = "BloodMagic.Runes.Ichor", desc = "How many times is a rune more effective than a regular one?")
    public static int ichorRuneBetterCapacity = 5;

    @ConfigValue(category = "BloodMagic.Runes.Ichor", desc = "How many times is a rune more effective than a regular one?")
    public static int ichorRuneDislocation = 5;

    @ConfigValue(category = "BloodMagic.Runes.Ichor", desc = "How many times is a rune more effective than a regular one?")
    public static int ichorRuneEfficiency = 5;

    @ConfigValue(category = "BloodMagic.Runes.Ichor", desc = "How many times is a rune more effective than a regular one?")
    public static int ichorRuneOrbCapacity = 5;

    @ConfigValue(category = "BloodMagic.Runes.Ichor", desc = "How many times is a rune more effective than a regular one?")
    public static int ichorRuneSacrifice = 5;

    @ConfigValue(category = "BloodMagic.Runes.Ichor", desc = "How many times is a rune more effective than a regular one?")
    public static int ichorRuneSelfSacrifice = 5;

    @ConfigValue(category = "BloodMagic.Runes.Ichor", desc = "How many times is a rune more effective than a regular one?")
    public static int ichorRuneSpeed = 5;

    @ConfigValue(category = "BloodMagic.Runes.Ichor", desc = "How many times is a rune more effective than a regular one?")
    public static int ichorRuneAcceleration = 5;


    @ConfigValue(category = "BloodMagic.Runes.singular", desc = "How many times is a rune more effective than a regular one?")
    public static int singularRuneCapacity = 15;

    @ConfigValue(category = "BloodMagic.Runes.singular", desc = "How many times is a rune more effective than a regular one?")
    public static int singularRuneBetterCapacity = 15;

    @ConfigValue(category = "BloodMagic.Runes.singular", desc = "How many times is a rune more effective than a regular one?")
    public static int singularRuneDislocation = 15;

    @ConfigValue(category = "BloodMagic.Runes.singular", desc = "How many times is a rune more effective than a regular one?")
    public static int singularRuneEfficiency = 15;

    @ConfigValue(category = "BloodMagic.Runes.singular", desc = "How many times is a rune more effective than a regular one?")
    public static int singularRuneOrbCapacity = 15;

    @ConfigValue(category = "BloodMagic.Runes.singular", desc = "How many times is a rune more effective than a regular one?")
    public static int singularRuneSacrifice = 15;

    @ConfigValue(category = "BloodMagic.Runes.singular", desc = "How many times is a rune more effective than a regular one?")
    public static int singularRuneSelfSacrifice = 15;

    @ConfigValue(category = "BloodMagic.Runes.singular", desc = "How many times is a rune more effective than a regular one?")
    public static int singularRuneSpeed = 15;

    @ConfigValue(category = "BloodMagic.Runes.singular", desc = "How many times is a rune more effective than a regular one?")
    public static int singularRuneAcceleration = 15;


    @ConfigValue(category = "Client", desc = "Show only interfaces with free slots")
    public static boolean showOnlyInterfacesWithFreeSlotsInInterfaceTerminal = false;


    @ConfigValue(category = "AE2.AutomatedBlocks", desc = "Number of items that the block crafting per tick")
    public static long speedAutoCrystallizer = 10_000;

    @ConfigValue(category = "AE2.AutomatedBlocks", desc = "Number of items that the block crafting per tick")
    public static long speedAutoPress = 1_000;

    @ConfigValue(category = "AE2.AutomatedBlocks", desc = "Number of items that the block crafting per tick")
    public static long speedAutoAwakenedBlocks = 200;

    @ConfigValue(category = "AE2.AutomatedBlocks", desc = "Number of items that the block crafting per tick")
    public static long speedAutoNeutronCompressor = 5;


    @ConfigValue(category = "DraconicEvolution.Blocks", desc = "Awakened Draconium Block: Amount of energy required to charge (RF)")
    public static int capacityAwake = 1_000_000_000;

    @ConfigValue(category = "DraconicEvolution.Blocks", desc = "Awakened Draconium Block: Maximum charging speed (RF/t)")
    public static int maxReceiveAwake = 100_000_000;

    @ConfigValue(category = "DraconicEvolution.Blocks", desc = "Chaos Block: Amount of energy required to charge (RF)")
    public static int capacityChaos = Integer.MAX_VALUE;

    @ConfigValue(category = "DraconicEvolution.Blocks", desc = "Chaos Block: Maximum charging speed (RF/t)")
    public static int maxReceiveChaos = 1_000_000_000;


    @ConfigValue(category = "DraconicEvolution.Cores", desc = "How many cores are needed for the chaos block ritual")
    public static int coresNeedsForChaotic = 16;

    @ConfigValue(category = "DraconicEvolution.Cores", desc = "How many cores are needed for the arial block ritual?")
    public static int coresNeedsForArial = 16;


    @ConfigValue(category = "DraconicEvolution.Upgrades", desc = "To what capacity does the card increase the block capacity?")
    public static long energyUpgradeWywern = 10_000_000;

    @ConfigValue(category = "DraconicEvolution.Upgrades", desc = "To what capacity does the card increase the block capacity?")
    public static long energyUpgradeAwakened = 1_000_000_000;

    @ConfigValue(category = "DraconicEvolution.Upgrades", desc = "To what capacity does the card increase the block capacity?")
    public static long energyUpgradeChaotic = 100_000_000_000L;

    @ConfigValue(category = "DraconicEvolution.Upgrades", desc = "To what capacity does the card increase the block capacity?")
    public static long energyUpgradeArial = 1_000_000_000_000L;


    @ConfigValue(category = "DraconicEvolution.Upgrades", max = "999", desc = "\"How many ticks faster does the card make the draconic assembler?")
    public static int draconicAssemblerSpeedUpgrade = 300;


    @ConfigValue(desc = "Use old armor model", category = "DraconicEvolution.Armor")
    public static boolean useOldArmorModel = false;

    @ConfigValue(desc = "Use old 3D armor model", category = "DraconicEvolution.Armor")
    public static boolean useOriginal3DArmorModel = true;

    @ConfigValue(desc = "Basic Energy Storage for Chaos Armor", category = "DraconicEvolution.Armor.Chaotic")
    public static int chaoticArmorBaseStorage = 100_000_000;

    @ConfigValue(desc = "The amount of energy added to the Chaos Armor's storage for each upgrade.", category = "DraconicEvolution.Armor.Chaotic")
    public static int chaoticArmorStoragePerUpgrade = 20_000_000;

    @ConfigValue(desc = "The amount of energy it will take to remove the effects", category = "DraconicEvolution.Armor.Chaotic")
    public static int chaoticArmorEnergyToRemoveEffects = 5_000;

    @ConfigValue(desc = "The maximum amount of energy that Chaos Armor can transfer.", category = "DraconicEvolution.Armor.Chaotic")
    public static int chaoticArmorMaxTransfer = 2_000_000;

    @ConfigValue(desc = "Max upgrades for Chaos armor", category = "DraconicEvolution.Armor.Chaotic")
    public static int chaoticArmorMaxUpgrades = 24;

    @ConfigValue(desc = "Max Chaos Armor Capacity Upgrade Points", category = "DraconicEvolution.Armor.Chaotic")
    public static int chaoticArmorMaxCapacityUpgradePoints = 100;

    @ConfigValue(desc = "Max Upgrade Points for Chaos Armor", category = "DraconicEvolution.Armor.Chaotic")
    public static int chaoticArmorMaxUpgradePoints = 100;

    @ConfigValue(desc = "Energy costs for damage protection", category = "DraconicEvolution.Armor.Chaotic")
    public static int chaoticArmorEnergyPerProtectionPoint = 1_000;

    @ConfigValue(desc = "Energy expended to satisfy hunger", category = "DraconicEvolution.Armor.Chaotic")
    public static int chaoticArmorEnergyToAddFood = 5_000;


    @ConfigValue(desc = "Basic Energy Storage for Arial Armor", category = "DraconicEvolution.Armor.Arial")
    public static int arialArmorBaseStorage = 500_000_000;

    @ConfigValue(desc = "The amount of energy added to the Arial Armor's storage for each upgrade.", category = "DraconicEvolution.Armor.Arial")
    public static int arialArmorStoragePerUpgrade = 60_000_000;

    @ConfigValue(desc = "The amount of energy it will take to remove the effects", category = "DraconicEvolution.Armor.Arial")
    public static int arialArmorEnergyToRemoveEffects = 1_000;

    @ConfigValue(desc = "The maximum amount of energy that Arial Armor can transfer.", category = "DraconicEvolution.Armor.Arial")
    public static int arialArmorMaxTransfer = 20_000_000;

    @ConfigValue(desc = "Max upgrades for Arial armor", category = "DraconicEvolution.Armor.Arial")
    public static int arialArmorMaxUpgrades = 36;

    @ConfigValue(desc = "Max Arial Armor Capacity Upgrade Points", category = "DraconicEvolution.Armor.Arial")
    public static int arialArmorMaxCapacityUpgradePoints = 300;

    @ConfigValue(desc = "Max Upgrade Points for Arial Armor", category = "DraconicEvolution.Armor.Arial")
    public static int arialArmorMaxUpgradePoints = 300;

    @ConfigValue(desc = "Energy costs for damage protection", category = "DraconicEvolution.Armor.Arial")
    public static int arialArmorEnergyPerProtectionPoint = 1_000;

    @ConfigValue(desc = "Energy expended to satisfy hunger", category = "DraconicEvolution.Armor.Arial")
    public static int arialArmorEnergyToAddFood = 5_000;


    @ConfigValue(desc = "Base Energy Storage Capacity for Chaos Capacitor", category = "DraconicEvolution.Items.Chaotic")
    public static long chaoticCapacitorBaseStorage = 1_000_000_000L;

    @ConfigValue(desc = "The amount of energy added to the Chaos Capacitor's storage for each upgrade.", category = "DraconicEvolution.Items.Chaotic")
    public static long chaoticCapacitorStoragePerUpgrade = 50_000_000L;

    @ConfigValue(desc = "The maximum amount of energy that the Chaos Capacitor can give off", category = "DraconicEvolution.Items.Chaotic")
    public static int chaoticCapacitorMaxExtract = 500_000_000;

    @ConfigValue(desc = "The maximum amount of energy that can enter the Chaos Capacitor", category = "DraconicEvolution.Items.Chaotic")
    public static int chaoticCapacitorMaxReceive = 20_000_000;

    @ConfigValue(desc = "Max Upgrade Points for Chaos Capacitor", category = "DraconicEvolution.Items.Chaotic")
    public static int chaoticCapacitorMaxUpgradePoints = 50;


    @ConfigValue(desc = "Base Energy Storage Capacity for Arial Capacitor", category = "DraconicEvolution.Items.Arial")
    public static long arialCapacitorBaseStorage = 2_000_000_000L;

    @ConfigValue(desc = "The amount of energy added to the Arial Capacitor's storage for each upgrade.", category = "DraconicEvolution.Items.Arial")
    public static long arialCapacitorStoragePerUpgrade = 75_000_000L;

    @ConfigValue(desc = "The maximum amount of energy that the Arial Capacitor can give off", category = "DraconicEvolution.Items.Arial")
    public static int arialCapacitorMaxExtract = 1_000_000_000;

    @ConfigValue(desc = "The maximum amount of energy that can enter the Arial Capacitor", category = "DraconicEvolution.Items.Arial")
    public static int arialCapacitorMaxReceive = 200_000_000;

    @ConfigValue(desc = "Max Upgrade Points for Arial Capacitor", category = "DraconicEvolution.Items.Arial")
    public static int arialCapacitorMaxUpgradePoints = 128;


    @ConfigValue(desc = "Chaotic Weapon Base Capacity", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticWeaponsBaseStorage = 100_000_000;

    @ConfigValue(desc = "The amount of energy added to the Chaotic Weapon pool for each upgrade.", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticWeaponsStoragePerUpgrade = 6_250_000;

    @ConfigValue(desc = "The maximum amount of energy that can be fed into a chaotic weapon.", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticWeaponsMaxTransfer = 50_000_000;

    @ConfigValue(desc = "Max Upgrades for Chaotic Weapons", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticWeaponsMaxUpgrades = 12;

    @ConfigValue(desc = "Max Upgrade Points for Chaotic Weapons", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticWeaponsMaxUpgradePoints = 50;

    @ConfigValue(desc = "Max Capacity Upgrade Points for Chaotic Weapons", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticWeaponsMaxCapacityUpgradePoints = 96;

    @ConfigValue(desc = "Attack Energy Cost for Chaotic Weapons", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticWeaponsEnergyPerAttack = 250;

    @ConfigValue(desc = "Basic capacity of chaotic instruments", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticToolsBaseStorage = 100_000_000;

    @ConfigValue(desc = "The amount of energy added to the Chaotic Tool storage for each upgrade.", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticToolsStoragePerUpgrade = 6_250_000;

    @ConfigValue(desc = "The maximum amount of energy that can be filled into chaotic instruments", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticToolsMaxTransfer = 500_000;

    @ConfigValue(desc = "Energy Costs Per Action for Chaotic Tools", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticToolsEnergyPerAction = 80;

    @ConfigValue(desc = "Max Capacity Upgrades for Chaotic Tools", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticToolsMaxCapacityUpgradePoints = 96;

    @ConfigValue(desc = "Max Upgrades for Chaotic Tools", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticToolsMaxUpgrades = 12;

    @ConfigValue(desc = "Max Upgrade Points for Chaotic Tools", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticToolsMaxUpgradePoints = 50;

    @ConfigValue(desc = "Max Capacity Upgrades for Chaotic Bow", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticBowMaxCapacityUpgradePoints = 50;

    @ConfigValue(desc = "Max Upgrades for Chaotic Bow", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticBowMaxUpgrades = 12;

    @ConfigValue(desc = "Max Upgrade Points for Chaotic Bow", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticBowMaxUpgradePoints = 50;

    @ConfigValue(desc = "The amount of energy it would take to fire a chaotic bow", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticBowEnergyPerShot = 80;

    @ConfigValue(desc = "Max Capacity Upgrades for Staff of Chaos", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticStaffMaxCapacityUpgradePoints = 96;

    @ConfigValue(desc = "Max Upgrades for Staff of Chaos", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticStaffMaxUpgrades = 24;

    @ConfigValue(desc = "Max Upgrade Points for Staff of Chaos", category = "DraconicEvolution.Tools.Chaotic")
    public static int chaoticStaffMaxUpgradePoints = 50;


    @ConfigValue(desc = "Arial Weapon Base Capacity", category = "DraconicEvolution.Tools.Arial")
    public static int arialWeaponsBaseStorage = 200_000_000;

    @ConfigValue(desc = "The amount of energy added to the Arial Weapon pool for each upgrade.", category = "DraconicEvolution.Tools.Arial")
    public static int arialWeaponsStoragePerUpgrade = 15_000_000;

    @ConfigValue(desc = "The maximum amount of energy that can be fed into a arial weapon.", category = "DraconicEvolution.Tools.Arial")
    public static int arialWeaponsMaxTransfer = 500_000_000;

    @ConfigValue(desc = "Max Upgrades for Arial Weapons", category = "DraconicEvolution.Tools.Arial")
    public static int arialWeaponsMaxUpgrades = 16;

    @ConfigValue(desc = "Max Upgrade Points for Arial Weapons", category = "DraconicEvolution.Tools.Arial")
    public static int arialWeaponsMaxUpgradePoints = 150;

    @ConfigValue(desc = "Max Capacity Upgrade Points for Arial Weapons", category = "DraconicEvolution.Tools.Arial")
    public static int arialWeaponsMaxCapacityUpgradePoints = 196;

    @ConfigValue(desc = "Attack Energy Cost for Arial Weapons", category = "DraconicEvolution.Tools.Arial")
    public static int arialWeaponsEnergyPerAttack = 500;

    @ConfigValue(desc = "Basic capacity of arial instruments", category = "DraconicEvolution.Tools.Arial")
    public static int arialToolsBaseStorage = 200_000_000;

    @ConfigValue(desc = "The amount of energy added to the Arial Tool storage for each upgrade.", category = "DraconicEvolution.Tools.Arial")
    public static int arialToolsStoragePerUpgrade = 15_000_000;

    @ConfigValue(desc = "The maximum amount of energy that can be filled into arial instruments", category = "DraconicEvolution.Tools.Arial")
    public static int arialToolsMaxTransfer = 500_000_000;

    @ConfigValue(desc = "Energy Costs Per Action for Arial Tools", category = "DraconicEvolution.Tools.Arial")
    public static int arialToolsEnergyPerAction = 50;

    @ConfigValue(desc = "Max Capacity Upgrades for Arial Tools", category = "DraconicEvolution.Tools.Arial")
    public static int arialToolsMaxCapacityUpgradePoints = 196;

    @ConfigValue(desc = "Max Upgrades for Arial Tools", category = "DraconicEvolution.Tools.Arial")
    public static int arialToolsMaxUpgrades = 16;

    @ConfigValue(desc = "Max Upgrade Points for Arial Tools", category = "DraconicEvolution.Tools.Arial")
    public static int arialToolsMaxUpgradePoints = 150;

    @ConfigValue(desc = "Max Capacity Upgrades for Arial Bow", category = "DraconicEvolution.Tools.Arial")
    public static int arialBowMaxCapacityUpgradePoints = 150;

    @ConfigValue(desc = "Max Upgrades for Arial Bow", category = "DraconicEvolution.Tools.Arial")
    public static int arialBowMaxUpgrades = 20;

    @ConfigValue(desc = "Max Upgrade Points for Arial Bow", category = "DraconicEvolution.Tools.Arial")
    public static int arialBowMaxUpgradePoints = 200;

    @ConfigValue(desc = "The amount of energy it would take to fire a arial bow", category = "DraconicEvolution.Tools.Arial")
    public static int arialBowEnergyPerShot = 100;

    @ConfigValue(desc = "Max Capacity Upgrades for Staff of Arial", category = "DraconicEvolution.Tools.Arial")
    public static int arialStaffMaxCapacityUpgradePoints = 64;

    @ConfigValue(desc = "Max Upgrades for Staff of Arial", category = "DraconicEvolution.Tools.Arial")
    public static int arialStaffMaxUpgrades = 48;

    @ConfigValue(desc = "Max Upgrade Points for Staff of Arial", category = "DraconicEvolution.Tools.Arial")
    public static int arialStaffMaxUpgradePoints = 384;


    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade1 = 0.5;

    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade2 = 1;

    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade3 = 2.5;

    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade4 = 5;

    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade5 = 10;

    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade6 = 15;

    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade7 = 25;

    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade8 = 50;

    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade9 = 100;

    @ConfigValue(desc = "By what percentage will the improvement increase the generation of panels in the Panel Synthesizer?", max = "1000", category = "IC2.Upgrades.Synthesizer")
    public static double synthesizerUpgrade10 = 150;


    @ConfigValue(desc = "How much energy does the Advanced Energy Cell hold?", category = "AE2.Cells.Energy")
    public static double advancedEnergyCellPower = 6_400_000;

    @ConfigValue(desc = "How much energy does the Hybrid Energy Cell hold?", category = "AE2.Cells.Energy")
    public static double hybridEnergyCellPower = 25_600_000;

    @ConfigValue(desc = "How much energy does the Ultimate Energy Cell hold?", category = "AE2.Cells.Energy")
    public static double ultimateEnergyCellPower = 102_400_000;


    @ConfigValue(desc = "The maximum number of channels this cable can transmit", category = "AE2.Channels.Cables")
    public static int cableAliteMaxChannelSize = 64;

    @ConfigValue(desc = "The maximum number of channels this cable can transmit", category = "AE2.Channels.Cables")
    public static int cableBimareMaxChannelSize = 128;

    @ConfigValue(desc = "The maximum number of channels this cable can transmit", category = "AE2.Channels.Cables")
    public static int cableDefitMaxChannelSize = 256;

    @ConfigValue(desc = "The maximum number of channels this cable can transmit", category = "AE2.Channels.Cables")
    public static int cableEfrimMaxChannelSize = 512;

    @ConfigValue(desc = "The maximum number of channels this cable can transmit", category = "AE2.Channels.Cables")
    public static int cableNurMaxChannelSize = 1024;

    @ConfigValue(desc = "The maximum number of channels this cable can transmit", category = "AE2.Channels.Cables")
    public static int cableXaurMaxChannelSize = 2048;


    @ConfigValue(desc = "The maximum number of channels that a wireless transmitter can transmit", category = "AE2.Channels.Wireless")
    public static int wirelessAliteMaxChannelSize = 64;

    @ConfigValue(desc = "The maximum number of channels that a wireless transmitter can transmit", category = "AE2.Channels.Wireless")
    public static int wirelessBimareMaxChannelSize = 128;

    @ConfigValue(desc = "The maximum number of channels that a wireless transmitter can transmit", category = "AE2.Channels.Wireless")
    public static int wirelessDefitMaxChannelSize = 256;

    @ConfigValue(desc = "The maximum number of channels that a wireless transmitter can transmit", category = "AE2.Channels.Wireless")
    public static int wirelessEfrimMaxChannelSize = 512;

    @ConfigValue(desc = "The maximum number of channels that a wireless transmitter can transmit", category = "AE2.Channels.Wireless")
    public static int wirelessNurMaxChannelSize = 1024;

    @ConfigValue(desc = "The maximum number of channels that a wireless transmitter can transmit", category = "AE2.Channels.Wireless")
    public static int wirelessXaurMaxChannelSize = 2048;

}
