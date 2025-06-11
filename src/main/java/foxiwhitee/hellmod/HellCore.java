package foxiwhitee.hellmod;

import appeng.block.AEBaseItemBlock;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import foxiwhitee.hellmod.client.CustomFontRender;
import foxiwhitee.hellmod.commands.CommandReloadLocalization;
import foxiwhitee.hellmod.integration.IntegrationLoader;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxiwhitee.hellmod.utils.helpers.GuiHandler;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import foxiwhitee.hellmod.utils.localization.TooltipHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Field;

import static foxiwhitee.hellmod.HellCore.*;

@Mod(modid = MODID, name = MODNAME, version = VERSION, dependencies = DEPEND)
public class HellCore {
    public static final String
            MODID = "hellmod",
            MODNAME = "Hell Integrations",
            VERSION = "1.0.0",
            DEPEND = "required-after:appliedenergistics2;";// +
                    //"required-after:Avaritia;" +
                    //"required-after:AdvancedSolarPanel;" +
                    //"required-after:Botania;" +
                    //"required-after:DraconicEvolution;" +
                   // "required-after:Thaumcraft;";


    public static final CreativeTabs HELL_TAB = new CreativeTabs("HELL_TAB") {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.bedrock);
        }
    };
    public static boolean BloodMagic;

    @Mod.Instance(MODID)
    public static HellCore instance;

    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @SidedProxy(clientSide = "foxiwhitee.hellmod.proxy.ClientProxy", serverSide = "foxiwhitee.hellmod.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance
    public static HellCore INS;

    public static Block pfpConverter;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
        NetworkRegistry.INSTANCE.registerGuiHandler(INS, new GuiHandler());
        IntegrationLoader.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        IntegrationLoader.init(e);
        //MinecraftForge.EVENT_BUS.register(new TooltipHandler());

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            try {
                Field fontRendererField = Minecraft.class.getDeclaredField("fontRenderer");
                //fontRendererField.setAccessible(true);
                //fontRendererField.set(Minecraft.getMinecraft(), new CustomFontRender(Minecraft.getMinecraft().gameSettings, new ResourceLocation("minecraft:textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false));
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
        NetworkManager.instance = new NetworkManager("hellmod");
        IntegrationLoader.postInit(e);
        LocalizationUtils.findUnlocalizedNames();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadLocalization());
    }
}
