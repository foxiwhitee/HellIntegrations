package foxiwhitee.hellmod.integration.ic2;

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
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.blocks.BaseBlock;
import foxiwhitee.hellmod.client.render.assemblers.RenderBlockBaseMolecularAssembler;
import foxiwhitee.hellmod.client.render.assemblers.RenderItemBaseMolecularAssembler;
import foxiwhitee.hellmod.integration.ic2.blocks.*;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import foxiwhitee.hellmod.integration.ic2.client.render.RenderBlockEUProvider;
import foxiwhitee.hellmod.integration.ic2.client.render.RenderItemEUProvider;
import foxiwhitee.hellmod.integration.ic2.helpers.IEUEnergyGrid;
import foxiwhitee.hellmod.integration.ic2.items.*;
import foxiwhitee.hellmod.integration.ic2.me.EUEnergyGrid;
import foxiwhitee.hellmod.integration.ic2.tile.*;
import foxiwhitee.hellmod.integration.thaumcraft.tile.TileStabilizer;
import foxiwhitee.hellmod.items.ItemBlockAE2;
import foxiwhitee.hellmod.items.ModItemBlock;
import foxiwhitee.hellmod.tile.assemblers.TileBaseMolecularAssembler;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.EnumHelper;

import java.util.HashMap;

@Integration(modid = "IC2")
public class IC2Integration implements IIntegration {

    public static Item energyUpgrades = new ItemEUEnergyUpgrades("energyUpgrades");
    public static Item energyStorageUpgrades = new ItemEUEnergyStorageUpgrades("energyStorageUpgrades");
    public static Item sunUpgrade = new ItemSunUpgrade("sunUpgrade");

    public static Block blood_casing = new BaseBlock("blood_casing");
    public static Block nano_casing = new BaseBlock("nano_casing");
    public static Block quantum_casing = new BaseBlock("quantum_casing");

    public static BlockCustomMatterGen advanced_matter = new BlockCustomMatterGen("advanced_matter");
    public static BlockCustomMatterGen nano_matter = new BlockCustomMatterGen("nano_matter");
    public static BlockCustomMatterGen quantum_matter = new BlockCustomMatterGen("quantum_matter");

    public static BlockAdvancedScanner advanced_scanner = new BlockAdvancedScanner("advanced_scanner");

    public static BlockAutoReplicator advanced_replicator = new BlockAutoReplicator("advanced_replicator");
    public static BlockAutoReplicator nano_replicator = new BlockAutoReplicator("nano_replicator");
    public static BlockAutoReplicator quantum_replicator = new BlockAutoReplicator("quantum_replicator");

    public static Block panel1 = (Block)new BlockCustomSolarPanel("panel1");
    public static Block panel2 = (Block)new BlockCustomSolarPanel("panel2");
    public static Block panel3 = (Block)new BlockCustomSolarPanel("panel3");
    public static Block panel4 = (Block)new BlockCustomSolarPanel("panel4");
    public static Block panel5 = (Block)new BlockCustomSolarPanel("panel5");
    public static Block panel6 = (Block)new BlockCustomSolarPanel("panel6");
    public static Block panel7 = (Block)new BlockCustomSolarPanel("panel7");
    public static Block panel8 = (Block)new BlockCustomSolarPanel("panel8");

    public static Block quantumGenerator = (Block)new BlockInfinityGenerator("quantumGenerator");
    public static Block singularGenerator = (Block)new BlockInfinityGenerator("singularGenerator");

    public static Block matterUnifier = new BlockMatterUnifier("matterUnifier");

    public static Block quantumMatterUnifier = new BlockQuantumMatterUnifier("quantumMatterUnifier");

    public static Block synthesizer = new BlockSynthesizer("synthesizer");

    public static Block energy_provider = new BlockEUProvider("energy_provider");

    public void preInit(FMLPreInitializationEvent e) {
        RegisterUtils.registerItems(energyStorageUpgrades, energyUpgrades, sunUpgrade);
        RegisterUtils.registerBlocks(ModItemBlock.class, advanced_matter, nano_matter, quantum_matter,
                advanced_scanner,
                advanced_replicator, nano_replicator, quantum_replicator,
                panel1, panel2, panel3, panel4, panel5, panel6, panel7, panel8,
                quantumGenerator, singularGenerator);
        RegisterUtils.registerBlocks(quantum_casing, nano_casing);
        RegisterUtils.registerBlocks(ItemBlockAE2.class, matterUnifier, quantumMatterUnifier);
        RegisterUtils.registerBlocks(synthesizer, energy_provider);
        if (HellCore.BloodMagic) {
            RegisterUtils.registerBlock(blood_casing);
        }
        //RegisterUtils.findClasses("foxiwhitee.hellmod.integration.ic2.tile", TileEntity.class).forEach(RegisterUtils::registerTile);
        RegisterUtils.registerTile(TileAdvancedScanner.class);
        RegisterUtils.registerTile(TileMatterUnifier.class);
        RegisterUtils.registerTile(TileQuantumMatterUnifier.class);
        RegisterUtils.registerTile(TileSynthesizer.class);
        RegisterUtils.registerTile(TileEUProvider.class);
        RegisterUtils.findClasses("foxiwhitee.hellmod.integration.ic2.tile.replicators", TileEntity.class).forEach(RegisterUtils::registerTile);
        RegisterUtils.findClasses("foxiwhitee.hellmod.integration.ic2.tile.generators.panels", TileEntity.class).forEach(RegisterUtils::registerTile);
        RegisterUtils.findClasses("foxiwhitee.hellmod.integration.ic2.tile.generators.infinity", TileEntity.class).forEach(RegisterUtils::registerTile);
        RegisterUtils.findClasses("foxiwhitee.hellmod.integration.ic2.tile.matter", TileEntity.class).forEach(RegisterUtils::registerTile);
    }

    public void init(FMLInitializationEvent e) {
        if (isClient())
            clientInit();
    }

    @SideOnly(Side.CLIENT)
    public void clientInit() {
        RegisterUtils.registerItemRenderer(Item.getItemFromBlock(energy_provider), (IItemRenderer)new RenderItemEUProvider());
        RegisterUtils.registerTileRenderer(TileEUProvider.class, (TileEntitySpecialRenderer)new RenderBlockEUProvider());
    }

    public void postInit(FMLPostInitializationEvent e) {
        AEApi.instance().registries().gridCache().registerGridCache(IEUEnergyGrid.class, EUEnergyGrid.class);
    }
}
