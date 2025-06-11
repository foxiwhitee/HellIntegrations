package foxiwhitee.hellmod.proxy;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.api.storage.ICellHandler;
import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.GuiHostType;
import appeng.helpers.IInterfaceHost;
import appeng.parts.misc.PartInterface;
import appeng.tile.misc.TileInterface;
import cpw.mods.fml.common.Loader;
import foxiwhitee.hellmod.config.ConfigHandler;
import foxiwhitee.hellmod.container.ContainerAdvancedDrive;
import foxiwhitee.hellmod.container.interfaces.ContainerAdvancedInterface;
import foxiwhitee.hellmod.container.ContainerCustomMolecularAssembler;
import foxiwhitee.hellmod.container.interfaces.ContainerHybridInterface;
import foxiwhitee.hellmod.container.interfaces.ContainerUltimateInterface;
import foxiwhitee.hellmod.container.terminals.ContainerAdvancedInterfaceTerminal;
import foxiwhitee.hellmod.event.ServerEventHandler;
import foxiwhitee.hellmod.helpers.IFluidStorageGrid;
import foxiwhitee.hellmod.helpers.IInterfaceTerminalSupport;
import foxiwhitee.hellmod.helpers.InterfaceTerminalSupportedClassProvider;
import foxiwhitee.hellmod.integration.botania.helpers.IManaStorageGrid;
import foxiwhitee.hellmod.integration.botania.me.CreativeManaCellHandler;
import foxiwhitee.hellmod.integration.botania.me.ManaCellHandler;
import foxiwhitee.hellmod.integration.botania.me.ManaStorageGrid;
import foxiwhitee.hellmod.items.ItemFluidDrop;
import foxiwhitee.hellmod.me.FluidCellHandler;
import foxiwhitee.hellmod.me.FluidStorageGrid;
import foxiwhitee.hellmod.me.ItemDisplayBlackList;
import foxiwhitee.hellmod.parts.PartAdvancedInterface;
import foxiwhitee.hellmod.parts.PartAdvancedInterfaceTerminal;
import foxiwhitee.hellmod.parts.PartHybridInterface;
import foxiwhitee.hellmod.parts.PartUltimateInterface;
import foxiwhitee.hellmod.tile.TileAdvancedDrive;
import foxiwhitee.hellmod.tile.assemblers.TileBaseMolecularAssembler;
import foxiwhitee.hellmod.tile.assemblers.TileHybridMolecularAssembler;
import foxiwhitee.hellmod.tile.assemblers.TileUltimateMolecularAssembler;
import foxiwhitee.hellmod.tile.interfaces.TileAdvancedInterface;
import foxiwhitee.hellmod.tile.interfaces.TileHybridInterface;
import foxiwhitee.hellmod.tile.interfaces.TileUltimateInterface;
import foxiwhitee.hellmod.utils.cells.CustomCellHandler;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.ModItems;
import foxiwhitee.hellmod.ModRecipes;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.utils.handler.GuiHandlerRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;

import java.util.HashMap;

public class CommonProxy {

    private static GuiBridge GUI_ADV_INTERFACE = null;
    private static GuiBridge GUI_HYBRID_INTERFACE = null;
    private static GuiBridge GUI_ULTIMATE_INTERFACE = null;
    private static GuiBridge GUI_BASE_MOLECULAR_ASSEMBLER = null;
    private static GuiBridge GUI_HYBRID_MOLECULAR_ASSEMBLER = null;
    private static GuiBridge GUI_ULTIMATE_MOLECULAR_ASSEMBLER = null;
    private static GuiBridge GUI_ADV_ME_DRIVE = null;

    private static CableBusTextures PART_ADV_INTERFACE_FRONT = null;

    private static CableBusTextures PART_HYBRID_INTERFACE_FRONT = null;
    private static CableBusTextures PART_ULTIMATE_INTERFACE_FRONT = null;
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

    public static GuiBridge getGuiAdvMeDrive() {
        return GUI_ADV_ME_DRIVE;
    }

    public static GuiBridge getGuiAdvInterface() {
        return GUI_ADV_INTERFACE;
    }

    public static GuiBridge getGuiHybridInterface() {
        return GUI_HYBRID_INTERFACE;
    }

    public static GuiBridge getGuiUltimateInterface() {
        return GUI_ULTIMATE_INTERFACE;
    }

    public static GuiBridge getGuiBaseMolecularAssembler() {
        return GUI_BASE_MOLECULAR_ASSEMBLER;
    }

    public static GuiBridge getGuiHybridMolecularAssembler() {
        return GUI_HYBRID_MOLECULAR_ASSEMBLER;
    }

    public static GuiBridge getGuiUltimateMolecularAssembler() {
        return GUI_ULTIMATE_MOLECULAR_ASSEMBLER;
    }

    public static CableBusTextures getPartAdvInterfaceFront() {
        return PART_ADV_INTERFACE_FRONT;
    }

    public static CableBusTextures getPartHybridInterfaceFront() {
        return PART_HYBRID_INTERFACE_FRONT;
    }

    public static CableBusTextures getPartUltimateInterfaceFront() {
        return PART_ULTIMATE_INTERFACE_FRONT;
    }

    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.loadConfigs(event);
        GuiHandlerRegistry.registerGuiHandlers(event);
        //HellConfigOld.init(event.getModConfigurationDirectory().getAbsolutePath());
        HellCore.BloodMagic = Loader.isModLoaded("AWWayofTime");

        ModBlocks.registerBlocks();
        ModItems.registerItems();

    }

    public void init(FMLInitializationEvent event) {
        AEApi.instance().registries().cell().addCellHandler((ICellHandler)new CustomCellHandler());
        AEApi.instance().registries().cell().addCellHandler(new FluidCellHandler());
        AEApi.instance().registries().gridCache().registerGridCache(IFluidStorageGrid.class, FluidStorageGrid.class);
        InterfaceTerminalSupportedClassProvider.register((Class<? extends IInterfaceTerminalSupport>)((Object)(TileInterface.class)));
        InterfaceTerminalSupportedClassProvider.register((Class<? extends IInterfaceTerminalSupport>)((Object)(PartInterface.class)));
        InterfaceTerminalSupportedClassProvider.register((Class<? extends IInterfaceTerminalSupport>)((Object)(TileAdvancedInterface.class)));
        InterfaceTerminalSupportedClassProvider.register((Class<? extends IInterfaceTerminalSupport>)((Object)(PartAdvancedInterface.class)));
        InterfaceTerminalSupportedClassProvider.register((Class<? extends IInterfaceTerminalSupport>)((Object)(TileHybridInterface.class)));
        InterfaceTerminalSupportedClassProvider.register((Class<? extends IInterfaceTerminalSupport>)((Object)(PartHybridInterface.class)));
        InterfaceTerminalSupportedClassProvider.register((Class<? extends IInterfaceTerminalSupport>)((Object)(TileUltimateInterface.class)));
        InterfaceTerminalSupportedClassProvider.register((Class<? extends IInterfaceTerminalSupport>)((Object)(PartUltimateInterface.class)));
        InterfaceTerminalSupportedClassProvider.register(TileBaseMolecularAssembler.class);
        InterfaceTerminalSupportedClassProvider.register(TileHybridMolecularAssembler.class);
        InterfaceTerminalSupportedClassProvider.register(TileUltimateMolecularAssembler.class);

    }

    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        ModRecipes.registerRecipes();
        GUI_ADV_INTERFACE = EnumHelper.addEnum(GuiBridge.class, "PartAdvInterface", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerAdvancedInterface.class, IInterfaceHost.class, GuiHostType.WORLD, SecurityPermissions.BUILD});
        GUI_HYBRID_INTERFACE = EnumHelper.addEnum(GuiBridge.class, "PartHybridInterface", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerHybridInterface.class, IInterfaceHost.class, GuiHostType.WORLD, SecurityPermissions.BUILD});
        GUI_ULTIMATE_INTERFACE = EnumHelper.addEnum(GuiBridge.class, "PartUltimateInterface", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerUltimateInterface.class, IInterfaceHost.class, GuiHostType.WORLD, SecurityPermissions.BUILD});

        PART_ADV_INTERFACE_FRONT = EnumHelper.addEnum(CableBusTextures.class, "PartAdvInterfaceFront", new Class[]{String.class}, new Object[]{"PartAdvInterfaceFront"});
        PART_HYBRID_INTERFACE_FRONT = EnumHelper.addEnum(CableBusTextures.class, "PartHybridInterfaceFront", new Class[]{String.class}, new Object[]{"PartHybridInterfaceFront"});
        PART_ULTIMATE_INTERFACE_FRONT = EnumHelper.addEnum(CableBusTextures.class, "PartUltimateInterfaceFront", new Class[]{String.class}, new Object[]{"PartUltimateInterfaceFront"});

        GUI_BASE_MOLECULAR_ASSEMBLER = (GuiBridge)EnumHelper.addEnum(GuiBridge.class, "BaseModularAssembler", new Class[] { Class.class, Class.class, GuiHostType.class, SecurityPermissions.class }, new Object[] { ContainerCustomMolecularAssembler.class, TileBaseMolecularAssembler.class, GuiHostType.WORLD, SecurityPermissions.BUILD });
        GUI_HYBRID_MOLECULAR_ASSEMBLER = (GuiBridge)EnumHelper.addEnum(GuiBridge.class, "HybridModularAssembler", new Class[] { Class.class, Class.class, GuiHostType.class, SecurityPermissions.class }, new Object[] { ContainerCustomMolecularAssembler.class, TileHybridMolecularAssembler.class, GuiHostType.WORLD, SecurityPermissions.BUILD });
        GUI_ULTIMATE_MOLECULAR_ASSEMBLER = (GuiBridge)EnumHelper.addEnum(GuiBridge.class, "UltimateModularAssembler", new Class[] { Class.class, Class.class, GuiHostType.class, SecurityPermissions.class }, new Object[] { ContainerCustomMolecularAssembler.class, TileUltimateMolecularAssembler.class, GuiHostType.WORLD, SecurityPermissions.BUILD });
        
        guiBridges.put(0, EnumHelper.addEnum(GuiBridge.class, "AdvancedInterfaceTerminal", new Class[] { Class.class, Class.class, GuiHostType.class, SecurityPermissions.class }, new Object[] { ContainerAdvancedInterfaceTerminal.class, PartAdvancedInterfaceTerminal.class, GuiHostType.WORLD, SecurityPermissions.BUILD }));
        cableBusTexturesBright.put(0, EnumHelper.addEnum(CableBusTextures.class, "AdvancedInterfaceTerminal", new Class[] { String.class }, new Object[] { "PartAdvancedInterfaceTerminal_Bright" }));
        cableBusTexturesDark.put(0, EnumHelper.addEnum(CableBusTextures.class, "AdvancedInterfaceTerminal", new Class[] { String.class }, new Object[] { "PartAdvancedInterfaceTerminal_Dark" }));
        cableBusTexturesColored.put(0, EnumHelper.addEnum(CableBusTextures.class, "AdvancedInterfaceTerminal", new Class[] { String.class }, new Object[] { "PartAdvancedInterfaceTerminal_Colored" }));

        GUI_ADV_ME_DRIVE = (GuiBridge)EnumHelper.addEnum(GuiBridge.class, "AdvMEDrive", new Class[] { Class.class, Class.class, GuiHostType.class, SecurityPermissions.class }, new Object[] { ContainerAdvancedDrive.class, TileAdvancedDrive.class, GuiHostType.WORLD, SecurityPermissions.BUILD });

    }
}
