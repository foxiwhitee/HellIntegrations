package foxiwhitee.hellmod.integration.thaumcraft;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.GuiHostType;
import appeng.items.misc.ItemEncodedPattern;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.integration.thaumcraft.client.render.RenderBlockStabilizer;
import foxiwhitee.hellmod.integration.thaumcraft.client.render.RenderItemStabilizer;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import foxiwhitee.hellmod.integration.thaumcraft.blocks.BlockStabilizer;
import foxiwhitee.hellmod.integration.thaumcraft.container.ContainerPartAlchemicalConstructionPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.container.ContainerPartInfusionPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.items.*;
import foxiwhitee.hellmod.integration.thaumcraft.parts.PartAlchemicalConstructionPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.parts.PartInfusionPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.tile.TileStabilizer;
import foxiwhitee.hellmod.items.ModItemBlock;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.EnumHelper;

import java.util.HashMap;

@Integration(modid = "Thaumcraft")
public class ThaumcraftIntegration implements IIntegration {
    public static ItemMultiThaumcraftParts ITEM_PARTS_TERMINALS = new ItemMultiThaumcraftParts(AEApi.instance().partHelper());

    public static BlockStabilizer stabilizer = new BlockStabilizer("stabilizer");

    public static ThaumBooks taumBook = new ThaumBooks("taumBook", "magic/taumBook", "ThaumBook");
    public static ThaumBooks distortionBook = new ThaumBooks("distortionBook", "magic/distortionBook", "DistortionBook");
    public static ItemStabilizationChecker itemStabilizationChecker = new ItemStabilizationChecker();

    public static ItemEncodedPattern ALCHEMICAL_CONSTRUCTION_PATTERN = new ItemEncodedAlchemicalConstructionPattern("encoded_alchemical_construction_pattern");
    public static ItemEncodedPattern INFUSION_PATTERN = new ItemEncodedInfusionPattern("encoded_infusion_pattern");

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


    public void preInit(FMLPreInitializationEvent e) {
        RegisterUtils.registerBlock(stabilizer, ModItemBlock.class);
        RegisterUtils.registerItems(taumBook, distortionBook, itemStabilizationChecker);
        GameRegistry.registerItem(ALCHEMICAL_CONSTRUCTION_PATTERN, "encoded_alchemical_construction_pattern");
        GameRegistry.registerItem(INFUSION_PATTERN, "encoded_infusion_pattern");
        GameRegistry.registerItem(ITEM_PARTS_TERMINALS, "thaumcraftPart");
        RegisterUtils.findClasses("foxiwhitee.hellmod.integration.thaumcraft.tile", TileEntity.class).forEach(RegisterUtils::registerTile);
    }

    public void init(FMLInitializationEvent e) {
        if (isClient())
            clientInit();
    }

    @SideOnly(Side.CLIENT)
    public void clientInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileStabilizer.class, new RenderBlockStabilizer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(stabilizer), new RenderItemStabilizer());
    }

    public void postInit(FMLPostInitializationEvent e) {
        guiBridges.put(0, EnumHelper.addEnum(GuiBridge.class, "PartAlchemicalConstructionPatternTerminal", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerPartAlchemicalConstructionPatternTerminal.class, PartAlchemicalConstructionPatternTerminal.class, GuiHostType.WORLD, SecurityPermissions.CRAFT}));
        guiBridges.put(1, EnumHelper.addEnum(GuiBridge.class, "PartInfusionPatternTerminal", new Class[]{Class.class, Class.class, GuiHostType.class, SecurityPermissions.class}, new Object[]{ContainerPartInfusionPatternTerminal.class, PartInfusionPatternTerminal.class, GuiHostType.WORLD, SecurityPermissions.CRAFT}));

        if (isClient()) {
            registerClientCableBusTextures();
        }
    }

    @SideOnly(Side.CLIENT)
    private void registerClientCableBusTextures() {
        cableBusTexturesBright.put(0, EnumHelper.addEnum(CableBusTextures.class, "AlchemicalConstructionPatternTerminal", new Class[]{String.class}, new Object[]{"PartAlchemicalConstructionPatternTerm_Bright"}));
        cableBusTexturesDark.put(0, EnumHelper.addEnum(CableBusTextures.class, "AlchemicalConstructionPatternTerminal", new Class[]{String.class}, new Object[]{"PartAlchemicalConstructionPatternTerm_Dark"}));
        cableBusTexturesColored.put(0, EnumHelper.addEnum(CableBusTextures.class, "AlchemicalConstructionPatternTerminal", new Class[]{String.class}, new Object[]{"PartAlchemicalConstructionPatternTerm_Colored"}));

        cableBusTexturesBright.put(1, EnumHelper.addEnum(CableBusTextures.class, "InfusionPatternTerminal", new Class[]{String.class}, new Object[]{"PartInfusionPatternTerminal_Bright"}));
        cableBusTexturesDark.put(1, EnumHelper.addEnum(CableBusTextures.class, "InfusionPatternTerminal", new Class[]{String.class}, new Object[]{"PartInfusionPatternTerminal_Dark"}));
        cableBusTexturesColored.put(1, EnumHelper.addEnum(CableBusTextures.class, "InfusionPatternTerminal", new Class[]{String.class}, new Object[]{"PartInfusionPatternTerminal_Colored"}));
    }

}
