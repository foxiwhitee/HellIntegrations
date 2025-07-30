package foxiwhitee.hellmod.integration.avaritia;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.GuiHostType;
import appeng.items.misc.ItemEncodedPattern;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.items.ItemSingularity;
import fox.spiteful.avaritia.items.LudicrousItems;
import foxiwhitee.hellmod.integration.avaritia.blocks.BlockNeutronUnifier;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerPartBigPatternTerminal;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerPartCraftingTerminal9x9;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerPartNeutronCompressorPatternTerminal;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import foxiwhitee.hellmod.integration.avaritia.blocks.BlockCustomNeutronCollector;
import foxiwhitee.hellmod.integration.avaritia.items.*;
import foxiwhitee.hellmod.integration.avaritia.tile.TileNeutronUnifier;
import foxiwhitee.hellmod.integration.avaritia.tile.collectors.*;
import foxiwhitee.hellmod.items.ItemBlockAE2;
import foxiwhitee.hellmod.items.ModItemBlock;
import foxiwhitee.hellmod.integration.avaritia.parts.PartBigPatternTerminal;
import foxiwhitee.hellmod.integration.avaritia.parts.PartCraftingTerminal9x9;
import foxiwhitee.hellmod.integration.avaritia.parts.PartNeutronCompressorPatternTerminal;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

@Integration(modid = "Avaritia")
public class AvaritiaIntegration implements IIntegration {
    public static ItemMultiAvaritiaParts ITEM_PARTS_TERMINALS = new ItemMultiAvaritiaParts(AEApi.instance().partHelper());

    public static BlockCustomNeutronCollector basicNeutronCollector = new BlockCustomNeutronCollector("basicNeutronCollector");
    public static BlockCustomNeutronCollector advancedNeutronCollector = new BlockCustomNeutronCollector("advancedNeutronCollector");
    public static BlockCustomNeutronCollector hybridNeutronCollector = new BlockCustomNeutronCollector("hybridNeutronCollector");
    public static BlockCustomNeutronCollector ultimateNeutronCollector = new BlockCustomNeutronCollector("ultimateNeutronCollector");
    public static BlockCustomNeutronCollector quantiumNeutronCollector = new BlockCustomNeutronCollector("quantiumNeutronCollector");

    public static Block neutronUnifier = new BlockNeutronUnifier("neutronUnifier");

    public static ItemEncodedPattern BIG_PATTERN = new ItemEncodedBigPattern("encoded_big_pattern");
    public static ItemEncodedPattern NEUTRON_PATTERN = new ItemEncodedNeutronPattern("encoded_neutron_pattern");

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

    @Override
    public void preInit(FMLPreInitializationEvent paramFMLPreInitializationEvent) {
        RegisterUtils.registerBlocks(ModItemBlock.class, basicNeutronCollector, advancedNeutronCollector, hybridNeutronCollector, ultimateNeutronCollector, quantiumNeutronCollector);
        RegisterUtils.registerBlocks(ItemBlockAE2.class, neutronUnifier);
        GameRegistry.registerItem(BIG_PATTERN, "encoded_big_pattern");
        GameRegistry.registerItem(NEUTRON_PATTERN, "encoded_neutron_pattern");
        GameRegistry.registerItem(ITEM_PARTS_TERMINALS, "avaritiaPart");
        //RegisterUtils.findClasses("foxiwhitee.hellmod.integration.avaritia.tile", TileEntity.class).forEach(RegisterUtils::registerTile);
        RegisterUtils.registerTile(TileBaseNeutronCollector.class);
        RegisterUtils.registerTile(TileAdvancedNeutronCollector.class);
        RegisterUtils.registerTile(TileHybridNeutronCollector.class);
        RegisterUtils.registerTile(TileUltimateNeutronCollector.class);
        RegisterUtils.registerTile(TileQuantumNeutronCollector.class);
        RegisterUtils.registerTile(TileNeutronUnifier.class);
    }

    @Override
    public void init(FMLInitializationEvent paramFMLInitializationEvent) {
        if (isClient())
            clientInit();
        OreDictionary.registerOre("catalyst", new ItemStack(LudicrousItems.resource, 64, 5));
        for (int i = 0; i < ItemSingularity.types.length; i++) {
            OreDictionary.registerOre("singularity", new ItemStack(LudicrousItems.singularity, 64, i));
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent paramFMLPostInitializationEvent) {
        guiBridges.put(0, EnumHelper.addEnum(GuiBridge.class, "PartPatternTerminal9x9", new Class[] { Class.class, Class.class, GuiHostType.class, SecurityPermissions.class }, new Object[] { ContainerPartBigPatternTerminal.class, PartBigPatternTerminal.class, GuiHostType.WORLD, SecurityPermissions.CRAFT }));
        guiBridges.put(1, EnumHelper.addEnum(GuiBridge.class, "PartCraftingTerminal9x9", new Class[] { Class.class, Class.class, GuiHostType.class, SecurityPermissions.class }, new Object[] { ContainerPartCraftingTerminal9x9.class, PartCraftingTerminal9x9.class, GuiHostType.WORLD, SecurityPermissions.CRAFT }));
        guiBridges.put(2, EnumHelper.addEnum(GuiBridge.class, "PartNeutronCompressorPatternTerminal", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerPartNeutronCompressorPatternTerminal.class, PartNeutronCompressorPatternTerminal.class, GuiHostType.WORLD, SecurityPermissions.CRAFT}));

        if (isClient()) {
            registerClientCableBusTextures();
        }
    }

    @SideOnly(Side.CLIENT)
    private void registerClientCableBusTextures() {
        cableBusTexturesBright.put(0, EnumHelper.addEnum(CableBusTextures.class, "BigPatternTerminal", new Class[] { String.class }, new Object[] { "PartBigPatternTerm_Bright" }));
        cableBusTexturesDark.put(0, EnumHelper.addEnum(CableBusTextures.class, "BigPatternTerminal", new Class[] { String.class }, new Object[] { "PartBigPatternTerm_Dark" }));
        cableBusTexturesColored.put(0, EnumHelper.addEnum(CableBusTextures.class, "BigPatternTerminal", new Class[] { String.class }, new Object[] { "PartBigPatternTerm_Colored" }));

        cableBusTexturesBright.put(1, EnumHelper.addEnum(CableBusTextures.class, "CraftingTerminal9x9", new Class[] { String.class }, new Object[] { "PartCraftingTerminal9x9_Bright" }));
        cableBusTexturesDark.put(1, EnumHelper.addEnum(CableBusTextures.class, "CraftingTerminal9x9", new Class[] { String.class }, new Object[] { "PartCraftingTerminal9x9_Dark" }));
        cableBusTexturesColored.put(1, EnumHelper.addEnum(CableBusTextures.class, "CraftingTerminal9x9", new Class[] { String.class }, new Object[] { "PartCraftingTerminal9x9_Colored" }));

        cableBusTexturesBright.put(2, EnumHelper.addEnum(CableBusTextures.class, "NeutronCompressorPatternTerminal", new Class[] { String.class }, new Object[] { "PartNeutronCompressorPatternTerminal_Bright" }));
        cableBusTexturesDark.put(2, EnumHelper.addEnum(CableBusTextures.class, "NeutronCompressorPatternTerminal", new Class[] { String.class }, new Object[] { "PartNeutronCompressorPatternTerminal_Dark" }));
        cableBusTexturesColored.put(2, EnumHelper.addEnum(CableBusTextures.class, "NeutronCompressorPatternTerminal", new Class[] { String.class }, new Object[] { "PartNeutronCompressorPatternTerminal_Colored" }));
    }

    @SideOnly(Side.CLIENT)
    public void clientInit() {}
}
