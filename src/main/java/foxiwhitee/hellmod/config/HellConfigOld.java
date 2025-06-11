package foxiwhitee.hellmod.config;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

@Deprecated
public class HellConfigOld {
  public static Configuration config;

  public static boolean enable_tooltips;

  public static int baseTicks;
  public static int advTicks;
  public static int hybTicks;
  public static int ultTicks;
  public static int quantTicks;

  public static int manaPerSecSparkAsgard;
  public static int manaPerSecSparkHelhelm;
  public static int manaPerSecSparkValhalla;
  public static int manaPerSecSparkMidgard;

  public static int manaAsgardPool;
  public static int manaHelhelmPool;
  public static int manaValhallaPool;
  public static int manaMidgardPool;

  public static int manaPerSecAsgardSpreader;
  public static int manaPerSecHelhelmSpreader;
  public static int manaPerSecValhallaSpreader;
  public static int manaPerSecMidgardSpreader;

  public static int panel1GenDay;
  public static int panel1GenNight;
  public static int panel2GenDay;
  public static int panel2GenNight;
  public static int panel3GenDay;
  public static int panel3GenNight;
  public static int panel4GenDay;
  public static int panel4GenNight;
  public static int panel5GenDay;
  public static int panel5GenNight;
  public static int panel6GenDay;
  public static int panel6GenNight;
  public static int panel7GenDay;
  public static int panel7GenNight;
  public static int panel8GenDay;
  public static int panel8GenNight;

  public static int advancedMatterGeneration;
  public static int nanoMatterGeneration;
  public static int quantumMatterGeneration;

  public static int advancedMatterTank;
  public static int nanoMatterTank;
  public static int quantumMatterTank;

  public static int helibriumGeneration;
  public static int valharinGeneration;
  public static int midgaranGeneration;

  public static int advancedScannerDuration;

  public static int typesCellM;
  public static int typesCellG;
  public static int typesCellT;
  public static int typesCellP;
  public static int typesCellE;

  public static long basic_molecular_assembler_speed;
  public static long hybrid_molecular_assembler_speed;
  public static long ultimate_molecular_assembler_speed;

  public static int basic_molecular_assembler_power;
  public static int hybrid_molecular_assembler_power;
  public static int ultimate_molecular_assembler_power;

  public static long advanced_replicator_speed;
  public static long nano_replicator_speed;
  public static long quantum_replicator_speed;

  public static byte advanced_replicator_discount;
  public static byte nano_replicator_discount;
  public static byte quantum_replicator_discount;

  public static int advanced_accelerator;
  public static int hybrid_accelerator;
  public static int ultimate_accelerator;
  public static int quantum_accelerator;

  public static int ichorRuneCapacity;
  public static int ichorRuneBetterCapacity;
  public static int ichorRuneDislocation;
  public static int ichorRuneEfficiency;
  public static int ichorRuneOrbCapacity;
  public static int ichorRuneSacrifice;
  public static int ichorRuneSelfSacrifice;
  public static int ichorRuneSpeed;
  public static int ichorRuneAcceleration;

  public static int singularRuneCapacity;
  public static int singularRuneBetterCapacity;
  public static int singularRuneDislocation;
  public static int singularRuneEfficiency;
  public static int singularRuneOrbCapacity;
  public static int singularRuneSacrifice;
  public static int singularRuneSelfSacrifice;
  public static int singularRuneSpeed;
  public static int singularRuneAcceleration;

  public static boolean showOnlyInterfacesWithFreeSlotsInInterfaceTerminal = false;

  public static long speedAutoCrystallizer;
  public static long speedAutoPress;
  public static long speedAutoAwakenedBlocks;
  public static long speedAutoNeutronCompressor;

  public static int capacityAwake;
  public static int maxReceiveAwake;
  public static int capacityChaos;
  public static int maxReceiveChaos;

  public static int coresNeedsForChaotic;
  public static int coresNeedsForArial;

  public static long energyUpgradeWywern;
  public static long energyUpgradeAwakened;
  public static long energyUpgradeChaotic;
  public static long energyUpgradeArial;

  public static int draconicAssemblerSpeedUpgrade;

  public static void init(String configDir) {
    if (config == null) {
      String configName = "Hell-Integration.cfg";
      config = new Configuration(new File(configDir + "/Fox-Mods/" + configName));
      //config = new Configuration(confFile);
      config.load();
      syncConfig();
    } 
  }
  
  private static void syncConfig() {
    try {
      enable_tooltips = config.getBoolean("enable_tooltips", "General", true, "");

      baseTicks = config.getInt("BaseNeutronCollectorTicks", "NeutronTicks", 6000, 1, Integer.MAX_VALUE, "Affects the rate of neutron production in the Basic Neutron Collector.");
      advTicks = config.getInt("AdvancedNeutronCollectorTicks", "NeutronTicks", 5000, 1, Integer.MAX_VALUE, "Affects the rate of neutron production in the Basic Neutron Collector.");
      hybTicks = config.getInt("HybridNeutronCollectorTicks", "NeutronTicks", 4000, 1, Integer.MAX_VALUE, "Affects the rate of neutron production in the Basic Neutron Collector.");
      ultTicks = config.getInt("UltimateNeutronCollectorTicks", "NeutronTicks", 2500, 1, Integer.MAX_VALUE, "Affects the rate of neutron production in the Basic Neutron Collector.");
      quantTicks = config.getInt("QuantiumNeutronCollectorTicks", "NeutronTicks", 1000, 1, Integer.MAX_VALUE, "Affects the rate of neutron production in the Basic Neutron Collector.");

      manaPerSecSparkAsgard = config.getInt("manaPerSecSparkAsgard", "Botania", 1250000, 1, Integer.MAX_VALUE, "The amount of mana a asgard spark transfers per second");
      manaPerSecSparkHelhelm = config.getInt("manaPerSecSparkHelhelm", "Botania", 500000, 1, Integer.MAX_VALUE, "The amount of mana a helhelm spark transfers per second");
      manaPerSecSparkValhalla = config.getInt("manaPerSecSparkValhalla", "Botania", 115000, 1, Integer.MAX_VALUE, "The amount of mana a valhalla spark transfers per second");
      manaPerSecSparkMidgard = config.getInt("manaPerSecSparkMidgard", "Botania", 25000, 1, Integer.MAX_VALUE, "The amount of mana a midgard spark transfers per second");

      manaAsgardPool = config.getInt("manaAsgardPool", "Botania", 750000000, 1, Integer.MAX_VALUE, "The amount of mana stored in the asgard mana pool");
      manaHelhelmPool = config.getInt("manaHelhelmPool", "Botania", 250000000, 1, Integer.MAX_VALUE, "The amount of mana stored in the helhelm mana pool");
      manaValhallaPool = config.getInt("manaValhallaPool", "Botania", 125000000, 1, Integer.MAX_VALUE, "The amount of mana stored in the valhalla mana pool");
      manaMidgardPool = config.getInt("manaMidgardPool", "Botania", 50000000, 1, Integer.MAX_VALUE, "The amount of mana stored in the midgard mana pool");

      manaPerSecAsgardSpreader = config.getInt("manaPerSecAsgardSpreader", "Botania", 1000000, 1, Integer.MAX_VALUE, "The amount of mana a asgard spreader transfers per second");
      manaPerSecHelhelmSpreader = config.getInt("manaPerSecHelhelmSpreader", "Botania", 600000, 1, Integer.MAX_VALUE, "The amount of mana a helhelm spreader transfers per second");
      manaPerSecValhallaSpreader = config.getInt("manaPerSecValhallaSpreader", "Botania", 250000, 1, Integer.MAX_VALUE, "The amount of mana a valhalla spreader transfers per second");
      manaPerSecMidgardSpreader = config.getInt("manaPerSecMidgardSpreader", "Botania", 100000, 1, Integer.MAX_VALUE, "The amount of mana a midgard spreader transfers per second");

      //LEVEL 1
      panel1GenDay = config.getInt("panel1GenDay", "IC2.SolarPanels", 1024, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 1 solar panel during the day");
      panel1GenNight = config.getInt("panel1GenNight", "IC2.SolarPanels", 512, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 1 solar panel at night");
      //LEVEL 2
      panel2GenDay = config.getInt("panel2GenDay", "IC2.SolarPanels", 8192, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 2 solar panel during the day");
      panel2GenNight = config.getInt("panel2GenNight", "IC2.SolarPanels", 4096, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 2 solar panel at night");
      //LEVEL 3
      panel3GenDay = config.getInt("panel3GenDay", "IC2.SolarPanels", 32768, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 3 solar panel during the day");
      panel3GenNight = config.getInt("panel3GenNight", "IC2.SolarPanels", 16384, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 3 solar panel at night");
      //LEVEL 4
      panel4GenDay = config.getInt("panel4GenDay", "IC2.SolarPanels", 131072, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 4 solar panel during the day");
      panel4GenNight = config.getInt("panel4GenNight", "IC2.SolarPanels", 65536, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 4 solar panel at night");
      //LEVEL 5
      panel5GenDay = config.getInt("panel5GenDay", "IC2.SolarPanels", 524288, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 5 solar panel during the day");
      panel5GenNight = config.getInt("panel5GenNight", "IC2.SolarPanels", 262144, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 5 solar panel at night");
      //LEVEL 6
      panel6GenDay = config.getInt("panel6GenDay", "IC2.SolarPanels", 2097152, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 6 solar panel during the day");
      panel6GenNight = config.getInt("panel6GenNight", "IC2.SolarPanels", 1048576, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 6 solar panel at night");
      //LEVEL 7
      panel7GenDay = config.getInt("panel7GenDay", "IC2.SolarPanels", 8388608, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 7 solar panel during the day");
      panel7GenNight = config.getInt("panel7GenNight", "IC2.SolarPanels", 4194304, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 7 solar panel at night");
      //LEVEL 8
      panel8GenDay = config.getInt("panel8GenDay", "IC2.SolarPanels", 33554432, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 8 solar panel during the day");
      panel8GenNight = config.getInt("panel8GenNight", "IC2.SolarPanels", 16777216, 1, Integer.MAX_VALUE, "Amount of energy produced by a level 8 solar panel at night");

      advancedMatterGeneration = config.getInt("advancedMatterGeneration", "IC2.Matter", 8, 1, Integer.MAX_VALUE, "The amount of matter produced by an advanced matter generator in one operation");
      nanoMatterGeneration = config.getInt("nanoMatterGeneration", "IC2.Matter", 16, 1, Integer.MAX_VALUE, "The amount of matter produced by a nano matter generator in one operation");
      quantumMatterGeneration = config.getInt("quantumMatterGeneration", "IC2.Matter", 32, 1, Integer.MAX_VALUE, "The amount of matter produced by a quantum matter generator in one operation");

      advancedMatterTank = config.getInt("advancedMatterTank", "IC2.Matter", 32000, 1, Integer.MAX_VALUE, "The amount of matter that an advanced matter generator can store");
      nanoMatterTank = config.getInt("nanoMatterTank", "IC2.Matter", 96000, 1, Integer.MAX_VALUE, "The amount of matter that a nano matter generator can store");
      quantumMatterTank = config.getInt("quantumMatterTank", "IC2.Matter", 128000, 1, Integer.MAX_VALUE, "The amount of matter that a quantum matter generator can store");

      advancedScannerDuration = config.getInt("advancedScannerDuration", "IC2", 10, 1, Integer.MAX_VALUE, "Number of threads for which the advanced scanner scans");

      helibriumGeneration = config.getInt("helibriumGeneration", "Botania", 10000000, 1, Integer.MAX_VALUE, "The amount of mana produced by a helibrium in one operation");
      valharinGeneration = config.getInt("valharinGeneration", "Botania", 30000000, 1, Integer.MAX_VALUE, "The amount of mana produced by a valharin in one operation");
      midgaranGeneration = config.getInt("midgaranGeneration", "Botania", 60000000, 1, Integer.MAX_VALUE, "The amount of mana produced by a midgaran in one operation");

      typesCellM = config.getInt("typesCellM", "AE2.Cells", 126, 1, 1024, "Number of types of items a storage cell stores from one million");
      typesCellG = config.getInt("typesCellG", "AE2.Cells", 252, 1, 1024, "Number of types of items a storage cell can store from one billion");
      typesCellT = config.getInt("typesCellT", "AE2.Cells", 504, 1, 1024, "Number of types of items how many storage cells can store from one trillion");
      typesCellP = config.getInt("typesCellP", "AE2.Cells", 756, 1, 1024, "Number of types of items a storage cell holds per quadrillion");
      typesCellE = config.getInt("typesCellE", "AE2.Cells", 1, 1, 1024, "Number of types of items how many storage cells can store from one quintillion");

      basic_molecular_assembler_speed = Long.parseLong(config.getString("basic_molecular_assembler_speed", "AE2.MolecularAssemblers", "200", "Number of items that the molecular assembler crafting per tick [range: 1 ~ 9223372036854775807]"));
      hybrid_molecular_assembler_speed = Long.parseLong(config.getString("hybrid_molecular_assembler_speed", "AE2.MolecularAssemblers", "25000", "Number of items that the hybrid molecular assembler crafting per tick [range: 1 ~ 9223372036854775807]"));
      ultimate_molecular_assembler_speed = Long.parseLong(config.getString("ultimate_molecular_assembler_speed", "AE2.MolecularAssemblers", "25000000", "Number of items that the ultimate molecular assembler crafting per tick [range: 1 ~ 9223372036854775807]"));
      basic_molecular_assembler_power = config.getInt("basic_molecular_assembler_power", "AE2.MolecularAssemblers", 10, 1, Integer.MAX_VALUE, "Amount of energy AE2 per tick (ae/t) consumed by the molecular assembler");
      hybrid_molecular_assembler_power = config.getInt("hybrid_molecular_assembler_power", "AE2.MolecularAssemblers", 50, 1, Integer.MAX_VALUE, "Amount of energy AE2 per tick (ae/t) consumed by the hybrid molecular assembler");
      ultimate_molecular_assembler_power = config.getInt("ultimate_molecular_assembler_power", "AE2.MolecularAssemblers", 100, 1, Integer.MAX_VALUE, "Amount of energy AE2 per tick (ae/t) consumed by the ultimate molecular assembler");

      advanced_replicator_speed = Long.parseLong(config.getString("advanced_replicator_speed", "IC2.Matter", "10", "Number of items that the advanced replicator crafting per tick  [range: 1 ~ 9223372036854775807]"));
      nano_replicator_speed = Long.parseLong(config.getString("nano_replicator_speed", "IC2.Matter", "250", "Number of items that the nano replicator crafting per tick  [range: 1 ~ 9223372036854775807]"));
      quantum_replicator_speed = Long.parseLong(config.getString("quantum_replicator_speed", "IC2.Matter", "1000", "Number of items that the quantum replicator crafting per tick  [range: 1 ~ 9223372036854775807]"));

      advanced_replicator_discount = (byte) config.getInt("advanced_replicator_discount", "IC2.Matter", 1, 1, 100, "Advanced replicator matter discount");
      nano_replicator_discount = (byte) config.getInt("nano_replicator_discount", "IC2.Matter", 10, 1, 100, "Nano replicator matter discount");
      quantum_replicator_discount = (byte) config.getInt("quantum_replicator_discount", "IC2.Matter", 50, 1, 100, "Quantum replicator matter discount");

      advanced_accelerator = config.getInt("advanced_accelerator", "AE2.Accelerators", 9, 1, Integer.MAX_VALUE, "Advanced accelerator count");
      hybrid_accelerator = config.getInt("hybrid_accelerator", "AE2.Accelerators", 27, 1, Integer.MAX_VALUE, "Hybrid accelerator count");
      ultimate_accelerator = config.getInt("ultimate_accelerator", "AE2.Accelerators", 81, 1, Integer.MAX_VALUE, "Ultimate accelerator count");
      quantum_accelerator = config.getInt("quantum_accelerator", "AE2.Accelerators", 243, 1, Integer.MAX_VALUE, "Quantum accelerator count");

      ichorRuneCapacity = config.getInt("ichorRuneCapacity", "BloodMagic.Runes.Ichor", 5, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      ichorRuneBetterCapacity = config.getInt("ichorRuneBetterCapacity", "BloodMagic.Runes.Ichor", 5, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      ichorRuneDislocation = config.getInt("ichorRuneDislocation", "BloodMagic.Runes.Ichor", 5, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      ichorRuneEfficiency = config.getInt("ichorRuneEfficiency", "BloodMagic.Runes.Ichor", 5, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      ichorRuneOrbCapacity = config.getInt("ichorRuneOrbCapacity", "BloodMagic.Runes.Ichor", 5, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      ichorRuneSacrifice = config.getInt("ichorRuneSacrifice", "BloodMagic.Runes.Ichor", 5, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      ichorRuneSelfSacrifice = config.getInt("ichorRuneSelfSacrifice", "BloodMagic.Runes.Ichor", 5, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      ichorRuneSpeed = config.getInt("ichorRuneSpeed", "BloodMagic.Runes.Ichor", 5, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      ichorRuneAcceleration = config.getInt("ichorRuneAcceleration", "BloodMagic.Runes.Ichor", 5, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");

      singularRuneCapacity = config.getInt("singularRuneCapacity", "BloodMagic.Runes.singular", 15, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      singularRuneBetterCapacity = config.getInt("singularRuneBetterCapacity", "BloodMagic.Runes.singular", 15, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      singularRuneDislocation = config.getInt("singularRuneDislocation", "BloodMagic.Runes.singular", 15, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      singularRuneEfficiency = config.getInt("singularRuneEfficiency", "BloodMagic.Runes.singular", 15, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      singularRuneOrbCapacity = config.getInt("singularRuneOrbCapacity", "BloodMagic.Runes.singular", 15, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      singularRuneSacrifice = config.getInt("singularRuneSacrifice", "BloodMagic.Runes.singular", 15, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      singularRuneSelfSacrifice = config.getInt("singularRuneSelfSacrifice", "BloodMagic.Runes.singular", 15, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      singularRuneSpeed = config.getInt("singularRuneSpeed", "BloodMagic.Runes.singular", 15, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");
      singularRuneAcceleration = config.getInt("singularRuneAcceleration", "BloodMagic.Runes.singular", 15, 1, Integer.MAX_VALUE, "How many times is a rune more effective than a regular one?");

      showOnlyInterfacesWithFreeSlotsInInterfaceTerminal = config.getBoolean("showOnlyInterfacesWithFreeSlotsInInterfaceTerminal", "Client", false, "Show only interfaces with free slots");

      speedAutoCrystallizer = Long.parseLong(config.getString("speedAutoCrystallizer", "AE2.AutomatedBlocks", "10000", "Number of items that the block crafting per tick [range: 1 ~ 9223372036854775807]"));
      speedAutoPress = Long.parseLong(config.getString("speedAutoPress", "AE2.AutomatedBlocks", "1000", "Number of items that the block crafting per tick [range: 1 ~ 9223372036854775807]"));
      speedAutoAwakenedBlocks = Long.parseLong(config.getString("speedAutoAwakenedBlocks", "AE2.AutomatedBlocks", "200", "Number of items that the block crafting per tick [range: 1 ~ 9223372036854775807]"));
      speedAutoNeutronCompressor = Long.parseLong(config.getString("speedAutoNeutronCompressor", "AE2.AutomatedBlocks", "5", "Number of items that the block crafting per tick [range: 1 ~ 9223372036854775807]"));

      capacityAwake = config.getInt("capacityAwake", "DraconicEvolution.Blocks", 1000000000, 1, Integer.MAX_VALUE, "Awakened Draconium Block: Amount of energy required to charge (RF)");
      maxReceiveAwake = config.getInt("maxReceiveAwake", "DraconicEvolution.Blocks", 100000000, 1, Integer.MAX_VALUE, "Awakened Draconium Block: Maximum charging speed (RF/t)");
      capacityChaos = config.getInt("capacityChaos", "DraconicEvolution.Blocks", Integer.MAX_VALUE, 1, Integer.MAX_VALUE, "Chaos Block: Amount of energy required to charge (RF)");
      maxReceiveChaos = config.getInt("maxReceiveChaos", "DraconicEvolution.Blocks", 1000000000, 1, Integer.MAX_VALUE, "Chaos Block: Maximum charging speed (RF/t)");

      coresNeedsForChaotic = config.getInt("coresNeedsForChaotic", "DraconicEvolution.Cores", 16, 1, Integer.MAX_VALUE, "How many cores are needed for the chaos block ritual");
      coresNeedsForArial = config.getInt("coresNeedsForArial", "DraconicEvolution.Cores", 16, 1, Integer.MAX_VALUE, "How many cores are needed for the arial block ritual?");

      energyUpgradeWywern = Long.parseLong(config.getString("energyUpgradeWywern", "DraconicEvolution.Upgrades", "10000000", "To what capacity does the card increase the block capacity? [range: 1 ~ 9223372036854775807]"));
      energyUpgradeAwakened = Long.parseLong(config.getString("energyUpgradeAwakened", "DraconicEvolution.Upgrades", "1000000000", "To what capacity does the card increase the block capacity? [range: 1 ~ 9223372036854775807]"));
      energyUpgradeChaotic = Long.parseLong(config.getString("energyUpgradeChaotic", "DraconicEvolution.Upgrades", "100000000000", "To what capacity does the card increase the block capacity? [range: 1 ~ 9223372036854775807]"));
      energyUpgradeArial = Long.parseLong(config.getString("energyUpgradeArial", "DraconicEvolution.Upgrades", "1000000000000", "To what capacity does the card increase the block capacity? [range: 1 ~ 9223372036854775807]"));

      draconicAssemblerSpeedUpgrade = config.getInt("draconicAssemblerSpeedUpgrade", "DraconicEvolution.Upgrades", 600, 1, 999, "How many ticks faster does the card make the draconic assembler?");

    } finally {
      if (config.hasChanged())
        config.save(); 
    } 
  }
}
