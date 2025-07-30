package foxiwhitee.hellmod.integration.botania;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.GuiHostType;
import appeng.items.misc.ItemEncodedPattern;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.blocks.BlockAENetwork;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import foxiwhitee.hellmod.integration.botania.blocks.*;
import foxiwhitee.hellmod.integration.botania.client.ManaPoolInfoRenderer;
import foxiwhitee.hellmod.integration.botania.client.render.pools.RenderCustomManaPool;
import foxiwhitee.hellmod.integration.botania.client.render.spreaders.RenderCustomSpreader;
import foxiwhitee.hellmod.integration.botania.client.render.RenderItemManaCharger;
import foxiwhitee.hellmod.integration.botania.client.render.RenderManaCharger;
import foxiwhitee.hellmod.integration.botania.client.render.entity.RenderCustomSpark;
import foxiwhitee.hellmod.integration.botania.container.*;
import foxiwhitee.hellmod.integration.botania.entity.AsgardSpark;
import foxiwhitee.hellmod.integration.botania.entity.HelhelmSpark;
import foxiwhitee.hellmod.integration.botania.entity.MidgardSpark;
import foxiwhitee.hellmod.integration.botania.entity.ValhallaSpark;
import foxiwhitee.hellmod.integration.botania.event.ServerEventHandler;
import foxiwhitee.hellmod.integration.botania.helpers.IManaStorageGrid;
import foxiwhitee.hellmod.integration.botania.items.*;
import foxiwhitee.hellmod.integration.botania.items.ae.*;
import foxiwhitee.hellmod.integration.botania.items.generating.*;
import foxiwhitee.hellmod.integration.botania.items.patterns.*;
import foxiwhitee.hellmod.integration.botania.me.CreativeManaCellHandler;
import foxiwhitee.hellmod.integration.botania.me.ManaCellHandler;
import foxiwhitee.hellmod.integration.botania.me.ManaStorageGrid;
import foxiwhitee.hellmod.integration.botania.parts.*;
import foxiwhitee.hellmod.integration.botania.tile.*;
import foxiwhitee.hellmod.integration.botania.tile.ae.*;
import foxiwhitee.hellmod.integration.botania.tile.pools.*;
import foxiwhitee.hellmod.integration.botania.tile.spreaders.*;
import foxiwhitee.hellmod.items.ModItemBlock;
import foxiwhitee.hellmod.me.ItemDisplayBlackList;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.signature.SubTileSignature;

import java.util.HashMap;

@Integration(modid = "Botania")
public class BotaniaIntegration implements IIntegration {
    public static ItemMultiBotaniaParts ITEM_PARTS_TERMINALS = new ItemMultiBotaniaParts(AEApi.instance().partHelper());

    public static BlockManaCharger manaCharger = new BlockManaCharger("mana_charger");
    public static BlockCustomManaPool asgardPool = new BlockCustomManaPool("asgardPool", HellConfig.manaAsgardPool);
    public static BlockCustomManaPool helhelmPool = new BlockCustomManaPool("helhelmPool", HellConfig.manaHelhelmPool);
    public static BlockCustomManaPool valhallaPool = new BlockCustomManaPool("valhallaPool", HellConfig.manaValhallaPool);
    public static BlockCustomManaPool midgardPool = new BlockCustomManaPool("midgardPool", HellConfig.manaMidgardPool);
    public static BlockCustomSpreader asgardSpreader = new BlockCustomSpreader("asgardSpreader");
    public static BlockCustomSpreader helhelmSpreader = new BlockCustomSpreader("helhelmSpreader");
    public static BlockCustomSpreader valhallaSpreader = new BlockCustomSpreader("valhallaSpreader");
    public static BlockCustomSpreader midgardSpreader = new BlockCustomSpreader("midgardSpreader");

    public static Block manaSupplier = new BlockAENetwork("manaSupplier", TileManaSupplier.class);
    public static Block manaSupplierDwimerite = new BlockAENetwork("manaSupplierDwimerite", TileManaSupplierDwimerite.class);
    public static Block manaReceiver = new BlockAENetwork("manaReceiver", TileManaReceiver.class);
    public static Block manaReceiverDwimerite = new BlockAENetwork("manaReceiverDwimerite", TileManaReceiverDwimerite.class);

    public static Block manaGeneratorTier1 = new BlockManaGenerator("manaGeneratorTier1", 1);
    public static Block manaGeneratorTier2 = new BlockManaGenerator("manaGeneratorTier2", 2);
    public static Block manaGeneratorTier3 = new BlockManaGenerator("manaGeneratorTier3", 3);
    public static Block manaGeneratorTier4 = new BlockManaGenerator("manaGeneratorTier4", 4);
    public static Block manaGeneratorTier5 = new BlockManaGenerator("manaGeneratorTier5", 5);

    public static Block flowerSynthesizer = new BlockFlowerSynthesizer("flowerSynthesizer");

    public static ItemEncodedPattern ELVEN_TRADE_PATTERN = new ItemEncodedElvenTradePattern("encoded_elven_trade_pattern");
    public static ItemEncodedPattern MANA_POOL_PATTERN = new ItemEncodedManaPoolPattern("encoded_mana_pool_pattern");
    public static ItemEncodedPattern PETALS_PATTERN = new ItemEncodedPetalsPattern("encoded_petals_pattern");
    public static ItemEncodedPattern PURE_DAISY_PATTERN = new ItemEncodedPureDaisyPattern("encoded_pure_daisy_pattern");
    public static ItemEncodedPattern RUNE_ALTAR_PATTERN = new ItemEncodedRuneAltarPattern("encoded_rune_altar_pattern");

    public static Item ASGARD_SPARK = (Item)new ItemCustomSpark("asgardSpark", HellConfig.manaPerSecSparkAsgard);
    public static Item HELHELM_SPARK = (Item)new ItemCustomSpark("helhelmSpark", HellConfig.manaPerSecSparkHelhelm);
    public static Item VALHALLA_SPARK = (Item)new ItemCustomSpark("valhallaSpark", HellConfig.manaPerSecSparkValhalla);
    public static Item MIDGARD_SPARK = (Item)new ItemCustomSpark("midgardSpark", HellConfig.manaPerSecSparkMidgard);

    public static Item manaStorageComponent = new ItemManaStorageComponent("manaStorageComponent");
    public static Item empty_mana_storage_cell = new ItemEmptyManaCell("empty_mana_storage_cell");
    public static Item creative_mana_storage_cell = new ItemManaCreativeCell("creative_mana_storage_cell");
    public static Item manaStorageCell = new ItemManaCell("manaStorageCell");

    public static Item mana_drop = new ItemManaDrop("mana_drop");

    public static Item generationFlowerCores = new ItemGenerationFlowerCores("generationFlowerCores");
    public static Item functionalFlowerCores = new ItemFunctionalFlowerCores("functionalFlowerCores");
    public static Item essenceOfLife = new ItemEssenceOfLife("essenceOfLife");
    public static Item coreOfLife = new ItemCoreOfLife("coreOfLife");

    public static Item upgradeGeneratorMultiply = new ItemUpgradeGeneratorMultiply("upgradeGeneratorMultiply");
    public static Item upgradeGeneratorSpeed = new ItemUpgradeGeneratorSpeed("upgradeGeneratorSpeed");

    public static Item upgradeFlowerSynthesizer = new ItemUpgradeFlowerSynthesizer("upgradeFlowerSynthesizer");

    private static HashMap<Integer, GuiBridge> guiBridges = new HashMap<>();

    private static HashMap<Integer, CableBusTextures> cableBusTexturesBright = new HashMap<>();
    private static HashMap<Integer, CableBusTextures> cableBusTexturesDark = new HashMap<>();
    private static HashMap<Integer, CableBusTextures> cableBusTexturesColored = new HashMap<>();

    public static GuiBridge getGuiBridge(int id) {
        return guiBridges.get(id);
    }

    public static CableBusTextures getBusTextureBright(int id) {
        return cableBusTexturesBright.get(id);
    }

    public static CableBusTextures getBusTextureDark(int id) {
        return cableBusTexturesDark.get(id);
    }

    public static CableBusTextures getBusTextureColored(int id) {
        return cableBusTexturesColored.get(id);
    }

    public static int sparkColorAsgard;
    public static int sparkColorHelhelm;
    public static int sparkColorValhalla;
    public static int sparkColorMidgard;


    public void preInit(FMLPreInitializationEvent e) {
        BotaniaAPI.registerSubTile("helibrium", SubTileHelibrium.class);
        BotaniaAPI.registerSubTileSignature(SubTileHelibrium.class, (SubTileSignature)new Signature("helibrium"));
        BotaniaAPI.addSubTileToCreativeMenu("helibrium");
        BotaniaAPI.registerSubTile("valharin", SubTileValharin.class);
        BotaniaAPI.registerSubTileSignature(SubTileValharin.class, (SubTileSignature)new Signature("valharin"));
        BotaniaAPI.addSubTileToCreativeMenu("valharin");
        BotaniaAPI.registerSubTile("midgaran", SubTileMidgaran.class);
        BotaniaAPI.registerSubTileSignature(SubTileMidgaran.class, (SubTileSignature)new Signature("midgaran"));
        BotaniaAPI.addSubTileToCreativeMenu("midgaran");

        EntityRegistry.registerModEntity(AsgardSpark.class, "AsgardSpark", 1, HellCore.instance, 64, 10, false);
        EntityRegistry.registerModEntity(HelhelmSpark.class, "HelhelmSpark", 2, HellCore.instance, 64, 10, false);
        EntityRegistry.registerModEntity(ValhallaSpark.class, "ValhallaSpark", 3, HellCore.instance, 64, 10, false);
        EntityRegistry.registerModEntity(MidgardSpark.class, "MidgardSpark", 4, HellCore.instance, 64, 10, false);

        GameRegistry.registerItem(ITEM_PARTS_TERMINALS, "botaniaPart");
        GameRegistry.registerItem(ELVEN_TRADE_PATTERN, "encoded_elven_trade_pattern");
        GameRegistry.registerItem(MANA_POOL_PATTERN, "encoded_mana_pool_pattern");
        GameRegistry.registerItem(PETALS_PATTERN, "encoded_petals_pattern");
        GameRegistry.registerItem(PURE_DAISY_PATTERN, "encoded_pure_daisy_pattern");
        GameRegistry.registerItem(RUNE_ALTAR_PATTERN, "encoded_rune_altar_pattern");
        GameRegistry.registerItem(ASGARD_SPARK, "asgardSpark");
        GameRegistry.registerItem(HELHELM_SPARK, "helhelmSpark");
        GameRegistry.registerItem(VALHALLA_SPARK, "valhallaSpark");
        GameRegistry.registerItem(MIDGARD_SPARK, "midgardSpark");
        RegisterUtils.registerItems(mana_drop, manaStorageComponent, empty_mana_storage_cell, manaStorageCell, creative_mana_storage_cell, generationFlowerCores, functionalFlowerCores, essenceOfLife, coreOfLife, upgradeGeneratorMultiply, upgradeGeneratorSpeed, upgradeFlowerSynthesizer);

        manaCharger.register();
        //RegisterUtils.registerBlocks(ModItemBlock.class, asgardPool, helhelmPool, valhallaPool, midgardPool, asgardSpreader, helhelmSpreader, valhallaSpreader, midgardSpreader);
        RegisterUtils.registerBlocks(ModItemBlock.class, asgardPool, helhelmPool, valhallaPool, midgardPool,
                asgardSpreader, helhelmSpreader, valhallaSpreader, midgardSpreader, manaSupplier, manaSupplierDwimerite, manaReceiver, manaReceiverDwimerite,
                manaGeneratorTier1, manaGeneratorTier2, manaGeneratorTier3, manaGeneratorTier4, manaGeneratorTier5, flowerSynthesizer);
        RegisterUtils.findClasses("foxiwhitee.hellmod.integration.botania.tile.pools", TileEntity.class).forEach(RegisterUtils::registerTile);
        RegisterUtils.findClasses("foxiwhitee.hellmod.integration.botania.tile.spreaders", TileEntity.class).forEach(RegisterUtils::registerTile);
        RegisterUtils.registerTile(TileManaSupplier.class);
        RegisterUtils.registerTile(TileManaSupplierDwimerite.class);
        RegisterUtils.registerTile(TileManaReceiver.class);
        RegisterUtils.registerTile(TileManaReceiverDwimerite.class);
        RegisterUtils.registerTile(TileManaGeneratorTier1.class);
        RegisterUtils.registerTile(TileManaGeneratorTier2.class);
        RegisterUtils.registerTile(TileManaGeneratorTier3.class);
        RegisterUtils.registerTile(TileManaGeneratorTier4.class);
        RegisterUtils.registerTile(TileManaGeneratorTier5.class);
        RegisterUtils.registerTile(TileFlowerSynthesizer.class);

    }

    public void init(FMLInitializationEvent e) {
        if (isClient())
            clientInit();
    }

    @SideOnly(Side.CLIENT)
    public void clientInit() {
        MinecraftForge.EVENT_BUS.register(new ManaPoolInfoRenderer());
        initHex();
        ClientRegistry.bindTileEntitySpecialRenderer(TileManaCharger.class, (TileEntitySpecialRenderer)new RenderManaCharger());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAsgardManaPool.class, (TileEntitySpecialRenderer)new RenderCustomManaPool());
        ClientRegistry.bindTileEntitySpecialRenderer(TileHelHelmManaPool.class, (TileEntitySpecialRenderer)new RenderCustomManaPool());
        ClientRegistry.bindTileEntitySpecialRenderer(TileValhallaManaPool.class, (TileEntitySpecialRenderer)new RenderCustomManaPool());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMidgardManaPool.class, (TileEntitySpecialRenderer)new RenderCustomManaPool());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAsgardSpreader.class, (TileEntitySpecialRenderer)new RenderCustomSpreader());
        ClientRegistry.bindTileEntitySpecialRenderer(TileHelhelmSpreader.class, (TileEntitySpecialRenderer)new RenderCustomSpreader());
        ClientRegistry.bindTileEntitySpecialRenderer(TileValhallaSpreader.class, (TileEntitySpecialRenderer)new RenderCustomSpreader());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMidgardSpreader.class, (TileEntitySpecialRenderer)new RenderCustomSpreader());
        RenderingRegistry.registerEntityRenderingHandler(AsgardSpark.class, (Render)new RenderCustomSpark());
        RenderingRegistry.registerEntityRenderingHandler(HelhelmSpark.class, (Render)new RenderCustomSpark());
        RenderingRegistry.registerEntityRenderingHandler(ValhallaSpark.class, (Render)new RenderCustomSpark());
        RenderingRegistry.registerEntityRenderingHandler(MidgardSpark.class, (Render)new RenderCustomSpark());

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock((Block) manaCharger), (IItemRenderer)new RenderItemManaCharger());
    }

    public void postInit(FMLPostInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);

        AEApi.instance().registries().cell().addCellHandler(new ManaCellHandler());
        AEApi.instance().registries().cell().addCellHandler(new CreativeManaCellHandler());
        AEApi.instance().registries().gridCache().registerGridCache(IManaStorageGrid.class, ManaStorageGrid.class);
        ItemDisplayBlackList.blacklistItemDisplay(mana_drop);

        if (isClient()) {
            postInitClient();
        }
    }

    @SideOnly(Side.CLIENT)
    private void postInitClient() {
        guiBridges.put(0, EnumHelper.addEnum(GuiBridge.class, "PartManaPoolPatternTerminal", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerPartManaPoolPatternTerminal.class, PartManaPoolPatternTerminal.class, GuiHostType.WORLD, SecurityPermissions.CRAFT}));
        guiBridges.put(2, EnumHelper.addEnum(GuiBridge.class, "PartElvenTradePatternTerminal", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerPartElvenTradePatternTerminal.class, PartElvenTradePatternTerminal.class, GuiHostType.WORLD, SecurityPermissions.CRAFT}));
        guiBridges.put(3, EnumHelper.addEnum(GuiBridge.class, "PartPetalsPatternTerminal", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerPartPetalsPatternTerminal.class, PartPetalsPatternTerminal.class, GuiHostType.WORLD, SecurityPermissions.CRAFT}));
        guiBridges.put(4, EnumHelper.addEnum(GuiBridge.class, "PartPureDaisyPatternTerminal", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerPartPureDaisyPatternTerminal.class, PartPureDaisyPatternTerminal.class, GuiHostType.WORLD, SecurityPermissions.CRAFT}));
        guiBridges.put(5, EnumHelper.addEnum(GuiBridge.class, "PartRuneAltarPatternTerminal", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerPartRuneAltarPatternTerminal.class, PartRuneAltarPatternTerminal.class, GuiHostType.WORLD, SecurityPermissions.CRAFT}));

        cableBusTexturesBright.put(0, EnumHelper.addEnum(CableBusTextures.class, "ManaPoolPatternTerminal", new Class[]{String.class}, new Object[]{"PartManaPoolPatternTerm_Bright"}));
        cableBusTexturesDark.put(0, EnumHelper.addEnum(CableBusTextures.class, "ManaPoolPatternTerminal", new Class[]{String.class}, new Object[]{"PartManaPoolPatternTerm_Dark"}));
        cableBusTexturesColored.put(0, EnumHelper.addEnum(CableBusTextures.class, "ManaPoolPatternTerminal", new Class[]{String.class}, new Object[]{"PartManaPoolPatternTerm_Colored"}));

        cableBusTexturesBright.put(2, EnumHelper.addEnum(CableBusTextures.class, "ElvenTradePatternTerminal", new Class[] { String.class }, new Object[] { "PartElvenTradePatternTerminal_Bright" }));
        cableBusTexturesDark.put(2, EnumHelper.addEnum(CableBusTextures.class, "ElvenTradePatternTerminal", new Class[] { String.class }, new Object[] { "PartElvenTradePatternTerminal_Dark" }));
        cableBusTexturesColored.put(2, EnumHelper.addEnum(CableBusTextures.class, "ElvenTradePatternTerminal", new Class[] { String.class }, new Object[] { "PartElvenTradePatternTerminal_Colored" }));

        cableBusTexturesBright.put(3, EnumHelper.addEnum(CableBusTextures.class, "PetalsPatternTerminal", new Class[] { String.class }, new Object[] { "PartPetalsPatternTerminal_Bright" }));
        cableBusTexturesDark.put(3, EnumHelper.addEnum(CableBusTextures.class, "PetalsPatternTerminal", new Class[] { String.class }, new Object[] { "PartPetalsPatternTerminal_Dark" }));
        cableBusTexturesColored.put(3, EnumHelper.addEnum(CableBusTextures.class, "PetalsPatternTerminal", new Class[] { String.class }, new Object[] { "PartPetalsPatternTerminal_Colored" }));

        cableBusTexturesBright.put(4, EnumHelper.addEnum(CableBusTextures.class, "PureDaisyPatternTerminal", new Class[] { String.class }, new Object[] { "PartPureDaisyPatternTerminal_Bright" }));
        cableBusTexturesDark.put(4, EnumHelper.addEnum(CableBusTextures.class, "PureDaisyPatternTerminal", new Class[] { String.class }, new Object[] { "PartPureDaisyPatternTerminal_Dark" }));
        cableBusTexturesColored.put(4, EnumHelper.addEnum(CableBusTextures.class, "PartPureDaisyPatternTerminal", new Class[] { String.class }, new Object[] { "PartPureDaisyPatternTerminal_Colored" }));

        cableBusTexturesBright.put(5, EnumHelper.addEnum(CableBusTextures.class, "RuneAltarPatternTerminal", new Class[] { String.class }, new Object[] { "PartRuneAltarPatternTerminal_Bright" }));
        cableBusTexturesDark.put(5, EnumHelper.addEnum(CableBusTextures.class, "RuneAltarPatternTerminal", new Class[] { String.class }, new Object[] { "PartRuneAltarPatternTerminal_Dark" }));
        cableBusTexturesColored.put(5, EnumHelper.addEnum(CableBusTextures.class, "RuneAltarPatternTerminal", new Class[] { String.class }, new Object[] { "PartRuneAltarPatternTerminal_Colored" }));
    }


    public static void initHex() {
        sparkColorAsgard = parseColor("#D66122", 16711680);
        sparkColorHelhelm = parseColor("#6f00cc", 16711680);
        sparkColorValhalla = parseColor("#d9c836", 16711680);
        sparkColorMidgard = parseColor("#1D4DE1", 16711680);
    }

    public static int parseColor(String hex, int defaultValue) {
        if (hex.startsWith("#"))
            hex = hex.substring(1);
        try {
            return Integer.parseInt(hex, 16);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
