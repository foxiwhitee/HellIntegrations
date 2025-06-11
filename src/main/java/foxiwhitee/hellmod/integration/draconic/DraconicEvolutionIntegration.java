package foxiwhitee.hellmod.integration.draconic;

import appeng.api.AEApi;
import appeng.block.AEBaseItemBlock;
import com.brandon3055.draconicevolution.client.render.IRenderTweak;
import com.brandon3055.draconicevolution.common.ModBlocks;
import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.blocks.BlockDE;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.ModRecipes;
import foxiwhitee.hellmod.blocks.BaseBlock;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import foxiwhitee.hellmod.integration.draconic.blocks.*;
import foxiwhitee.hellmod.integration.draconic.client.render.*;
import foxiwhitee.hellmod.integration.draconic.client.render.armor.RenderArialArmor;
import foxiwhitee.hellmod.integration.draconic.client.render.armor.RenderChaoticArmor;
import foxiwhitee.hellmod.integration.draconic.client.render.effect.SEffectHandler;
import foxiwhitee.hellmod.integration.draconic.client.render.items.*;
import foxiwhitee.hellmod.integration.draconic.entity.EntityArialHeart;
import foxiwhitee.hellmod.integration.draconic.entity.EntityChaoticHeart;
import foxiwhitee.hellmod.integration.draconic.entity.EntityHeart;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingInventory;
import foxiwhitee.hellmod.integration.draconic.itemBlock.BlockAwakenedDraconiumItemBlock;
import foxiwhitee.hellmod.integration.draconic.itemBlock.BlockChaosItemBlock;
import foxiwhitee.hellmod.integration.draconic.items.*;
import foxiwhitee.hellmod.integration.draconic.items.armors.ArialArmor;
import foxiwhitee.hellmod.integration.draconic.items.armors.ChaoticArmor;
import foxiwhitee.hellmod.integration.draconic.items.capicators.ArialFluxCapicator;
import foxiwhitee.hellmod.integration.draconic.items.capicators.ChaoticFluxCapicator;
import foxiwhitee.hellmod.integration.draconic.items.tools.*;
import foxiwhitee.hellmod.integration.draconic.items.weapons.ArialBow;
import foxiwhitee.hellmod.integration.draconic.items.weapons.ArialSword;
import foxiwhitee.hellmod.integration.draconic.items.weapons.ChaoticBow;
import foxiwhitee.hellmod.integration.draconic.items.weapons.ChaoticSword;
import foxiwhitee.hellmod.integration.draconic.recipes.FusionRecipe;
import foxiwhitee.hellmod.integration.draconic.tile.*;
import foxiwhitee.hellmod.items.DefaultItem;
import foxiwhitee.hellmod.utils.event.AEventHandler;
import foxiwhitee.hellmod.utils.event.CommonEventHandler;
import foxiwhitee.hellmod.utils.event.CEventHandler;
import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;

import java.util.HashMap;
import java.util.Map;

import static foxiwhitee.hellmod.ModRecipes.*;
import static foxiwhitee.hellmod.ModRecipes.registerDraconicAssemblerRecipe;

@Integration(modid = "DraconicEvolution")
public class DraconicEvolutionIntegration implements IIntegration {
    private static Map<String, ResourceLocation> cachedResources = new HashMap<>();

    public static ItemArmor.ArmorMaterial CHAOTIC_ARMOR = EnumHelper.addArmorMaterial("CHAOTIC_ARMOR", -1, new int[] { 4, 9, 7, 4 }, 40);
    public static Item.ToolMaterial CHAOTIC = EnumHelper.addToolMaterial("CHAOTIC", 10, -1, 400.0F, 60.0F, 45);

    public static ItemArmor.ArmorMaterial ARIAL_ARMOR = EnumHelper.addArmorMaterial("ARIAL_ARMOR", -1, new int[] { 4, 9, 7, 4 }, 80);
    public static Item.ToolMaterial ARIAL = EnumHelper.addToolMaterial("ARIAL", 11, -1, 500.0F, 90.0F, 85);

    public static ItemArmor chaotic_helm = (ItemArmor)new ChaoticArmor(CHAOTIC_ARMOR, 0, "chaotic_helm");
    public static ItemArmor chaotic_chest = (ItemArmor)new ChaoticArmor(CHAOTIC_ARMOR, 1, "chaotic_chest");
    public static ItemArmor chaotic_legs = (ItemArmor)new ChaoticArmor(CHAOTIC_ARMOR, 2, "chaotic_legs");
    public static ItemArmor chaotic_boots = (ItemArmor)new ChaoticArmor(CHAOTIC_ARMOR, 3, "chaotic_boots");

    public static ItemArmor arial_helm = (ItemArmor)new ArialArmor(ARIAL_ARMOR, 0, "arial_helm");
    public static ItemArmor arial_chest = (ItemArmor)new ArialArmor(ARIAL_ARMOR, 1, "arial_chest");
    public static ItemArmor arial_legs = (ItemArmor)new ArialArmor(ARIAL_ARMOR, 2, "arial_legs");
    public static ItemArmor arial_boots = (ItemArmor)new ArialArmor(ARIAL_ARMOR, 3, "arial_boots");

    public static Item chaotic_capicator = (Item)new ChaoticFluxCapicator("chaoticFluxCapacitor");
    public static Item arial_capicator = (Item)new ArialFluxCapicator("arial_capicator");

    public static ItemSword chaotic_sword = (ItemSword)new ChaoticSword(CHAOTIC, "chaotic_sword");
    public static ItemBow chaotic_bow = (ItemBow)new ChaoticBow( "chaotic_bow");
    public static ItemSword arial_sword = (ItemSword)new ArialSword(ARIAL, "arial_sword");
    public static ItemBow arial_bow = (ItemBow)new ArialBow( "arial_bow");

    public static Item chaotic_pickaxe = (Item)new ChaoticPickaxe(CHAOTIC, "chaotic_pickaxe");
    public static Item chaotic_shovel = (Item)new ChaoticShovel(CHAOTIC, "chaotic_shovel");
    public static Item chaotic_axe = (Item)new ChaoticAxe(CHAOTIC, "chaotic_axe");
    public static Item chaotic_distruction_staff = (Item)new ChaoticDistructionStaff(CHAOTIC, "chaoticdistructionstaff");

    public static Item arial_pickaxe = (Item)new ArialPickaxe(ARIAL, "arial_pickaxe");
    public static Item arial_shovel = (Item)new ArialShovel(ARIAL, "arial_shovel");
    public static Item arial_axe = (Item)new ArialAxe(ARIAL, "arial_axe");
    public static Item arial_distruction_staff = (Item)new ArialDistructionStaff(ARIAL, "arial_distruction_staff");

    public static Item upgradeDragonAutoAwakenedBlocks = new ItemDragonUpgrade("upgradeDragonAutoAwakenedBlocks", "draconic/upgradeDragonAutoAwakenedBlocks");
    public static Item upgradeChaosAutoAwakenedBlocks = new ItemChaosUpgrade("upgradeChaosAutoAwakenedBlocks", "draconic/upgradeChaosAutoAwakenedBlocks");
    public static Item upgradeArialAutoAwakenedBlocks = new ItemArialUpgrade("upgradeArialAutoAwakenedBlocks", "draconic/upgradeArialAutoAwakenedBlocks");
    public static Item arialCore = new DefaultItem("arialCore", "draconic/arialCore") {
        @Override
        public boolean hasEffect(ItemStack par1ItemStack, int pass) {
            return true;
        }
    };
    public static Item arialEnergyCore = new DefaultItem("arialEnergyCore", "draconic/arialEnergyCore") {
        @Override
        public boolean hasEffect(ItemStack par1ItemStack, int pass) {
            return true;
        }
    };
    public static Item arialHeart = new ItemArialHeart("arialHeart");
    public static Item chaoticHeart = new ItemChaoticHeart("chaoticHeart");

    public static Item draconicAssemblerUpgrades = new ItemDraconicAssemblerUpgrades("draconicAssemblerUpgrades");
    public static Item draconicEnergyUpgrades = new ItemDraconicEnergyUpgrades("draconicEnergyUpgrades");

    public static Block autoAwakener = (Block)new BlockAutoAwakenedBlocks("autoAwakener");

    public static Block chaotic_block = new BlockChaos();
    public static Block arial_block = new BaseBlock("arial_block").setBlockTextureName(HellCore.MODID + ":draconic/arial_block");

    public static Block draconicAssembler = new BlockDraconicAssembler("dragon_assembler");

    public static Block fusion_core = new BlockFusionCraftingCore("fusion_core");
    public static Block fusion_injector = new BlockFusionInjector("fusion_injector");

    public static Block custom_upgrade_modifier = new BlockCustomUpgradeModifier("custom_upgrade_modifier");

    public void preInit(FMLPreInitializationEvent e) {
        RegisterUtils.registerItems(chaotic_helm, chaotic_chest, chaotic_legs, chaotic_boots,
                chaotic_capicator, chaotic_sword, chaotic_pickaxe, chaotic_shovel, chaotic_axe, chaotic_bow, chaotic_distruction_staff);
        RegisterUtils.registerItems(arial_helm, arial_chest, arial_legs, arial_boots,
                arial_capicator, arial_sword, arial_pickaxe, arial_shovel, arial_axe, arial_bow, arial_distruction_staff);
        RegisterUtils.registerBlock(autoAwakener, AEBaseItemBlock.class);
        RegisterUtils.registerBlocks(arial_block, draconicAssembler, fusion_core, custom_upgrade_modifier);
        RegisterUtils.registerBlock(fusion_injector, ItemBlockFusionInjector.class);
        RegisterUtils.registerTile(TileAutoAwakenedBlocks.class);
        RegisterUtils.registerTile(TileDraconicAssembler.class);
        RegisterUtils.registerTile(TileFusionCraftingCore.class);
        RegisterUtils.registerTile(TileFusionInjector.class);
        RegisterUtils.registerTile(TileCustomUpgradeModifier.class);
        RegisterUtils.registerItems(upgradeDragonAutoAwakenedBlocks, upgradeChaosAutoAwakenedBlocks, upgradeArialAutoAwakenedBlocks, arialCore, arialEnergyCore, arialHeart, chaoticHeart, draconicAssemblerUpgrades, draconicEnergyUpgrades);
        foxiwhitee.hellmod.ModItems.addItems("draconic", "", "ingot_chaotic", "fragment_chaos", "chaoticEnergyCore");

        EntityRegistry.registerModEntity(EntityHeart.class, "Persistent Heart", 12, HellCore.instance, 32, 5, true);
        EntityRegistry.registerModEntity(EntityChaoticHeart.class, "Chaotic Heart", 13, HellCore.instance, 32, 5, true);
        EntityRegistry.registerModEntity(EntityArialHeart.class, "Arial Heart", 14, HellCore.instance, 32, 5, true);
        //RegisterUtils.findClasses("foxiwhitee.hellmod.integration.draconic.tile", TileEntity.class).forEach(RegisterUtils::registerTile);
    }

    public void init(FMLInitializationEvent e) {
        if (isClient())
            clientInit();
        MinecraftForge.EVENT_BUS.register(new CEventHandler());
        MinecraftForge.EVENT_BUS.register(new AEventHandler());
        FMLCommonHandler.instance().bus().register(new CEventHandler());
        FMLCommonHandler.instance().bus().register(new AEventHandler());
        FMLCommonHandler.instance().bus().register(new CommonEventHandler());

        BlockAwakenedDraconiumItemBlock.update();
        BlockChaosItemBlock.update();
    }

    @SideOnly(Side.CLIENT)
    public void clientInit() {
        SEffectHandler.iniEffectRenderer();

        MinecraftForgeClient.registerItemRenderer(chaotic_helm, new RenderChaoticArmor(chaotic_helm));
        MinecraftForgeClient.registerItemRenderer(chaotic_chest, new RenderChaoticArmor(chaotic_chest));
        MinecraftForgeClient.registerItemRenderer(chaotic_legs, new RenderChaoticArmor(chaotic_legs));
        MinecraftForgeClient.registerItemRenderer(chaotic_boots, new RenderChaoticArmor(chaotic_boots));

        MinecraftForgeClient.registerItemRenderer(chaotic_bow, new RenderChaoticBow());
        MinecraftForgeClient.registerItemRenderer(chaotic_bow, new RenderChaoticBowModel());
        MinecraftForgeClient.registerItemRenderer(chaotic_sword, new RenderChaoticTools("models/tools/ChaoticSword.obj", "textures/models/tools/ChaoticSword.png", (IRenderTweak) chaotic_sword));

        MinecraftForgeClient.registerItemRenderer(chaotic_pickaxe, new RenderChaoticTools("models/tools/ChaoticPickaxe.obj", "textures/models/tools/ChaoticPickaxe.png", (IRenderTweak) chaotic_pickaxe));
        MinecraftForgeClient.registerItemRenderer(chaotic_axe, new RenderChaoticTools("models/tools/ChaoticLumberAxe.obj", "textures/models/tools/ChaoticLumberAxe.png", (IRenderTweak) chaotic_axe));
        MinecraftForgeClient.registerItemRenderer(chaotic_shovel, new RenderChaoticTools("models/tools/ChaoticShovel.obj", "textures/models/tools/ChaoticShovel.png", (IRenderTweak) chaotic_shovel));
        MinecraftForgeClient.registerItemRenderer(chaotic_distruction_staff, new RenderChaoticTools("models/tools/ChaoticStaffOfPower.obj", "textures/models/tools/ChaoticStaffOfPower.png", (IRenderTweak) chaotic_distruction_staff));


        MinecraftForgeClient.registerItemRenderer(arial_helm, new RenderArialArmor(arial_helm));
        MinecraftForgeClient.registerItemRenderer(arial_chest, new RenderArialArmor(arial_chest));
        MinecraftForgeClient.registerItemRenderer(arial_legs, new RenderArialArmor(arial_legs));
        MinecraftForgeClient.registerItemRenderer(arial_boots, new RenderArialArmor(arial_boots));

        MinecraftForgeClient.registerItemRenderer(arial_bow, new RenderArialBow());
        MinecraftForgeClient.registerItemRenderer(arial_bow, new RenderArialBowModel());
        MinecraftForgeClient.registerItemRenderer(arial_sword, new RenderArialTools("models/tools/ArialSword.obj", "textures/models/tools/ArialSword.png", (IRenderTweak) arial_sword));

        MinecraftForgeClient.registerItemRenderer(arial_pickaxe, new RenderArialTools("models/tools/ArialPickaxe.obj", "textures/models/tools/ArialPickaxe.png", (IRenderTweak) arial_pickaxe));
        MinecraftForgeClient.registerItemRenderer(arial_axe, new RenderArialTools("models/tools/ArialLumberAxe.obj", "textures/models/tools/ArialLumberAxe.png", (IRenderTweak) arial_axe));
        MinecraftForgeClient.registerItemRenderer(arial_shovel, new RenderArialTools("models/tools/ArialShovel.obj", "textures/models/tools/ArialShovel.png", (IRenderTweak) arial_shovel));
        MinecraftForgeClient.registerItemRenderer(arial_distruction_staff, new RenderArialTools("models/tools/ArialStaffOfPower.obj", "textures/models/tools/ArialStaffOfPower.png", (IRenderTweak) arial_distruction_staff));


        RenderingRegistry.registerEntityRenderingHandler(EntityChaoticHeart.class, new RenderChaoticHeart());
        RenderingRegistry.registerEntityRenderingHandler(EntityArialHeart.class, new RenderArialHeart());

        RegisterUtils.registerItemRenderer(Item.getItemFromBlock(draconicAssembler), (IItemRenderer)new RenderItemDraconicAssembler());
        RegisterUtils.registerTileRenderer(TileDraconicAssembler.class, (TileEntitySpecialRenderer)new RenderBlockDraconicAssembler());

        RegisterUtils.registerItemRenderer(Item.getItemFromBlock(custom_upgrade_modifier), (IItemRenderer)new RenderItemCustomUpgradeModifier());
        RegisterUtils.registerTileRenderer(TileCustomUpgradeModifier.class, (TileEntitySpecialRenderer)new RenderCustomUpgradeModifier());

        RegisterUtils.registerItemRenderer(Item.getItemFromBlock(fusion_core), (IItemRenderer)new RenderItemFusionCraftingCore());
        RegisterUtils.registerTileRenderer(TileFusionCraftingCore.class, (TileEntitySpecialRenderer)new RenderTileFusionCraftingCore());
        RegisterUtils.registerItemRenderer(Item.getItemFromBlock(fusion_injector), (IItemRenderer)new RenderItemFusionInjector());
        RegisterUtils.registerTileRenderer(TileFusionInjector.class, (TileEntitySpecialRenderer)new RenderTileFusionInjector());
    }

    public void postInit(FMLPostInitializationEvent e) {
        registerAutoAwakenedBlocksDragonRecipe(new ItemStack(com.brandon3055.draconicevolution.common.ModBlocks.draconicBlock, 4),
                new ItemStack(com.brandon3055.draconicevolution.common.ModBlocks.draconiumBlock, 4, 2),
                new ItemStack(com.brandon3055.draconicevolution.common.ModItems.dragonHeart),
                new ItemStack(ModItems.draconicCore, 16),
                new ItemStack(Blocks.tnt));

        registerAutoAwakenedBlocksChaosRecipe(new ItemStack(chaotic_block, 4),
                new ItemStack(com.brandon3055.draconicevolution.common.ModBlocks.draconicBlock, 4, 1),
                new ItemStack(chaoticHeart),
                new ItemStack(ModItems.awakenedCore, HellConfig.coresNeedsForChaotic),
                new ItemStack(Blocks.tnt));
        registerAutoAwakenedBlocksChaosRecipe(new ItemStack(com.brandon3055.draconicevolution.common.ModBlocks.draconicBlock, 4),
                new ItemStack(com.brandon3055.draconicevolution.common.ModBlocks.draconiumBlock, 4, 2),
                new ItemStack(com.brandon3055.draconicevolution.common.ModItems.dragonHeart),
                new ItemStack(ModItems.draconicCore, 16),
                new ItemStack(Blocks.tnt));

        registerAutoAwakenedBlocksArialRecipe(new ItemStack(arial_block, 4),
                new ItemStack(chaotic_block, 4, 1),
                new ItemStack(arialHeart),
                new ItemStack(ModItems.awakenedCore, HellConfig.coresNeedsForArial),
                new ItemStack(Blocks.tnt));
        registerAutoAwakenedBlocksArialRecipe(new ItemStack(chaotic_block, 4),
                new ItemStack(com.brandon3055.draconicevolution.common.ModBlocks.draconicBlock, 4, 1),
                new ItemStack(chaoticHeart),
                new ItemStack(ModItems.awakenedCore, HellConfig.coresNeedsForChaotic),
                new ItemStack(Blocks.tnt));
        registerAutoAwakenedBlocksArialRecipe(new ItemStack(com.brandon3055.draconicevolution.common.ModBlocks.draconicBlock, 4),
                new ItemStack(com.brandon3055.draconicevolution.common.ModBlocks.draconiumBlock, 4, 2),
                new ItemStack(com.brandon3055.draconicevolution.common.ModItems.dragonHeart),
                new ItemStack(ModItems.draconicCore, 16),
                new ItemStack(Blocks.tnt));

        registerDraconicAssemblerRecipe(new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("fragment_chaos"), 3), 2, 100000, new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("ingot_chaotic")));
        registerDraconicAssemblerRecipe(new ItemStack(draconicEnergyUpgrades, 1, 0), 1, 75000,
                new ItemStack(ModBlocks.draconiumBlock, 6, 2),
                new ItemStack(ModBlocks.energyCrystal, 2),
                new ItemStack(ModItems.draconiumEnergyCore, 4));
        registerDraconicAssemblerRecipe(new ItemStack(draconicEnergyUpgrades, 1, 1), 2, 1500000,
                new ItemStack(ModBlocks.draconicBlock, 8),
                new ItemStack(ModItems.awakenedCore, 2),
                new ItemStack(ModItems.draconiumEnergyCore, 6, 1),
                new ItemStack(draconicEnergyUpgrades, 1, 0),
                new ItemStack(draconicEnergyUpgrades, 1, 0));
        registerDraconicAssemblerRecipe(new ItemStack(draconicEnergyUpgrades, 1, 2), 3, 750000000,
                new ItemStack(chaotic_block, 12),
                new ItemStack(ModItems.chaoticCore, 1),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("chaoticEnergyCore"), 8),
                new ItemStack(draconicEnergyUpgrades, 1, 1),
                new ItemStack(draconicEnergyUpgrades, 1, 1));
        registerDraconicAssemblerRecipe(new ItemStack(draconicEnergyUpgrades, 1, 3), 4, 75000000000L,
                new ItemStack(arial_block, 4),
                new ItemStack(arialCore, 1),
                new ItemStack(arialEnergyCore, 8),
                new ItemStack(draconicEnergyUpgrades, 1, 2),
                new ItemStack(draconicEnergyUpgrades, 1, 2));
        registerDraconicAssemblerRecipe(new ItemStack(ModItems.draconiumEnergyCore, 1), 0, 10000,
                new ItemStack(ModItems.draconicCore, 2),
                new ItemStack(ModItems.draconiumIngot, 6),
                new ItemStack(Blocks.redstone_block, 8));
        removeCraftingRecipe(new ItemStack(ModItems.draconiumEnergyCore));
        registerDraconicAssemblerRecipe(new ItemStack(ModItems.draconiumEnergyCore, 1, 1), 1, 100000,
                new ItemStack(ModItems.wyvernCore, 2),
                new ItemStack(ModItems.draconiumEnergyCore, 6),
                new ItemStack(ModItems.draconiumIngot, 8));
        removeCraftingRecipe(new ItemStack(ModItems.draconiumEnergyCore, 1, 1));
        registerDraconicAssemblerRecipe(new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("chaoticEnergyCore"), 1), 2, 1000000,
                new ItemStack(ModItems.awakenedCore, 2),
                new ItemStack(ModItems.draconiumEnergyCore, 6, 1),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("ingot_chaotic"), 8),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("fragment_chaos"), 4));
        registerDraconicAssemblerRecipe(new ItemStack(arialEnergyCore, 1), 3, 10000000,
                new ItemStack(ModItems.chaoticCore, 2),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("chaoticEnergyCore"), 6),
                new ItemStack(arial_block),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("fragment_chaos"), 8));
        registerDraconicAssemblerRecipe(new ItemStack(upgradeDragonAutoAwakenedBlocks), 1, 10000000,
                AEApi.instance().definitions().materials().basicCard().maybeStack(16).get(),
                AEApi.instance().definitions().materials().advCard().maybeStack(16).get(),
                new ItemStack(ModBlocks.draconicBlock, 8),
                new ItemStack(ModItems.awakenedCore));
        registerDraconicAssemblerRecipe(new ItemStack(upgradeChaosAutoAwakenedBlocks), 2, 1000000000,
                new ItemStack(upgradeDragonAutoAwakenedBlocks),
                AEApi.instance().definitions().materials().basicCard().maybeStack(32).get(),
                AEApi.instance().definitions().materials().advCard().maybeStack(32).get(),
                new ItemStack(chaotic_block, 4),
                new ItemStack(ModItems.chaoticCore),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("fragment_chaos"), 8));
        registerDraconicAssemblerRecipe(new ItemStack(upgradeArialAutoAwakenedBlocks), 3, 100000000000L,
                new ItemStack(upgradeChaosAutoAwakenedBlocks),
                AEApi.instance().definitions().materials().basicCard().maybeStack(64).get(),
                AEApi.instance().definitions().materials().advCard().maybeStack(64).get(),
                new ItemStack(arial_block, 4),
                new ItemStack(arialCore),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("fragment_chaos"), 64));
        registerDraconicAssemblerRecipe(new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("advancedEmptyCell")), 0, 100000,
                AEApi.instance().definitions().materials().emptyStorageCell().maybeStack(2).get(),
                AEApi.instance().definitions().materials().singularity().maybeStack(4).get(),
                AEApi.instance().definitions().materials().calcProcessor().maybeStack(8).get(),
                AEApi.instance().definitions().materials().engProcessor().maybeStack(8).get(),
                AEApi.instance().definitions().materials().logicProcessor().maybeStack(8).get());
        registerDraconicAssemblerRecipe(new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("hybridEmptyCell")), 0, 100000,
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("advancedEmptyCell"), 2),
                AEApi.instance().definitions().materials().singularity().maybeStack(8).get(),
                AEApi.instance().definitions().materials().calcProcessor().maybeStack(16).get(),
                AEApi.instance().definitions().materials().engProcessor().maybeStack(16).get(),
                AEApi.instance().definitions().materials().logicProcessor().maybeStack(16).get());
        registerDraconicAssemblerRecipe(new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("ultimateEmptyCell")), 0, 100000,
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("hybridEmptyCell"), 2),
                AEApi.instance().definitions().materials().singularity().maybeStack(16).get(),
                AEApi.instance().definitions().materials().calcProcessor().maybeStack(32).get(),
                AEApi.instance().definitions().materials().engProcessor().maybeStack(32).get(),
                AEApi.instance().definitions().materials().logicProcessor().maybeStack(32).get());
        registerDraconicAssemblerRecipe(new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("quantumEmptyCell")), 0, 100000,
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("ultimateEmptyCell"), 2),
                AEApi.instance().definitions().materials().singularity().maybeStack(32).get(),
                AEApi.instance().definitions().materials().calcProcessor().maybeStack(64).get(),
                AEApi.instance().definitions().materials().engProcessor().maybeStack(64).get(),
                AEApi.instance().definitions().materials().logicProcessor().maybeStack(64).get());
        registerDraconicAssemblerRecipe(new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("singularEmptyCell")), 0, 100000,
                AEApi.instance().definitions().materials().singularity().maybeStack(64).get(),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("advancedEmptyCell"), 8),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("hybridEmptyCell"), 8),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("ultimateEmptyCell"), 8),
                new ItemStack(foxiwhitee.hellmod.ModItems.ITEMS.get("quantumEmptyCell"), 8));
        registerFusionRecipe(new ItemStack(draconicAssembler), new ItemStack(fusion_core), 0, 100000,
                new ItemStack(fusion_injector),
                new ItemStack(fusion_injector),
                new ItemStack(fusion_injector),
                new ItemStack(fusion_injector),
                new ItemStack(fusion_injector),
                new ItemStack(fusion_injector),
                new ItemStack(fusion_injector),
                new ItemStack(ModItems.draconicCore),
                new ItemStack(ModItems.draconicCore));
    }

    public static void register(BlockDE block, Class<? extends ItemBlock> item) {
        String name = block.getUnwrappedUnlocalizedName(block.getUnlocalizedName());
        GameRegistry.registerBlock(block, item, name.substring(name.indexOf(":") + 1));
    }

    public static FusionRecipe findRecipeFusion(IFusionCraftingInventory inventory, World world, int x, int y, int z) {
        if (inventory == null || OreDictUtil.isEmpty(inventory.getStackInCore(0)))
            return null;
        for (FusionRecipe recipe : ModRecipes.fusionRecipes) {
            if (recipe.matches(inventory, world, x, y, z))
                return recipe;
        }
        return null;
    }

    public static void bindResource(String rs) {
        bindTexture(getResource(rs));
    }

    public static void bindTexture(ResourceLocation texture) {
        (Minecraft.getMinecraft()).renderEngine.bindTexture(texture);
    }

    public static ResourceLocation getResource(String rs) {
        if (!cachedResources.containsKey(rs))
            cachedResources.put(rs, new ResourceLocation(HellCore.MODID + ":" + rs));
        return cachedResources.get(rs);
    }
}
