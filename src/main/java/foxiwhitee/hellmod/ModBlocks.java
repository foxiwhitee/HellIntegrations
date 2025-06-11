package foxiwhitee.hellmod;

import foxiwhitee.hellmod.blocks.*;
import foxiwhitee.hellmod.blocks.assemblers.BlockBaseMolecularAssembler;
import foxiwhitee.hellmod.blocks.assemblers.BlockHybridMolecularAssembler;
import foxiwhitee.hellmod.blocks.assemblers.BlockUltimateMolecularAssembler;
import foxiwhitee.hellmod.blocks.cpu.BlockCustomAccelerators;
import foxiwhitee.hellmod.blocks.cpu.BlockCustomCraftingStorage;
import foxiwhitee.hellmod.blocks.cpu.BlockMEServer;
import foxiwhitee.hellmod.blocks.interfaces.BlockAdvancedInterface;
import foxiwhitee.hellmod.blocks.interfaces.BlockHybridInterface;
import foxiwhitee.hellmod.blocks.interfaces.BlockUltimateInterface;
import foxiwhitee.hellmod.items.ItemBlockCustomEnergyCell;
import foxiwhitee.hellmod.items.ModItemBlock;
import foxiwhitee.hellmod.items.ItemBlockAAccelerators;
import foxiwhitee.hellmod.items.ItemBlockAE2;
import foxiwhitee.hellmod.tile.TileAdvancedDrive;
import foxiwhitee.hellmod.tile.TileAutoCrystallizer;
import foxiwhitee.hellmod.tile.TileAutoPress;
import foxiwhitee.hellmod.tile.TileCobblestoneDuper;
import foxiwhitee.hellmod.tile.assemblers.TileBaseMolecularAssembler;
import foxiwhitee.hellmod.tile.assemblers.TileHybridMolecularAssembler;
import foxiwhitee.hellmod.tile.assemblers.TileUltimateMolecularAssembler;
import foxiwhitee.hellmod.tile.cpu.TileCustomAccelerators;
import foxiwhitee.hellmod.tile.cpu.TileCustomCraftingStorage;
import foxiwhitee.hellmod.tile.cpu.TileMEServer;
import foxiwhitee.hellmod.tile.energycell.TileAdvancedEnergyCell;
import foxiwhitee.hellmod.tile.energycell.TileHybridEnergyCell;
import foxiwhitee.hellmod.tile.energycell.TileUltimateEnergyCell;
import foxiwhitee.hellmod.tile.fluid.TileFluidReceiver;
import foxiwhitee.hellmod.tile.fluid.TileFluidSupplier;
import foxiwhitee.hellmod.tile.interfaces.TileAdvancedInterface;
import foxiwhitee.hellmod.tile.interfaces.TileHybridInterface;
import foxiwhitee.hellmod.tile.interfaces.TileUltimateInterface;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;


public class ModBlocks {

    public static final Block A_ACCELERATORS = new BlockCustomAccelerators().setCreativeTab(HellCore.HELL_TAB);

    public static final Block A_ADVANCED_INTERFACE = new BlockAdvancedInterface();
    public static final Block A_HYBRID_INTERFACE = new BlockHybridInterface();
    public static final Block A_ULTIMATE_INTERFACE = new BlockUltimateInterface();

    public static BlockAutoCrystallizer autoCrystallizer = new BlockAutoCrystallizer("autoCrystallizer");
    public static BlockAutoPress autoPress = new BlockAutoPress("autoPress");

    public static BlockCobblestoneDuper cobblestone_duper = new BlockCobblestoneDuper("cobblestone_duper");

    public static BlockBaseMolecularAssembler baseMolecularAssembler = new BlockBaseMolecularAssembler("baseMolecularAssembler");
    public static BlockHybridMolecularAssembler hybridMolecularAssembler = new BlockHybridMolecularAssembler("hybridMolecularAssembler");
    public static BlockUltimateMolecularAssembler ultimateMolecularAssembler = new BlockUltimateMolecularAssembler("ultimateMolecularAssembler");

    public static Block serverME = new BlockMEServer("meServer");

    public static Block advanced_energy_cell = new BlockCustomEnergyCell("advanced_energy_cell");
    public static Block hybrid_energy_cell = new BlockCustomEnergyCell("hybrid_energy_cell");
    public static Block ultimate_energy_cell = new BlockCustomEnergyCell("ultimate_energy_cell");

    public static Block wirelessAlite = new BlockCustomWireless("wirelessAlite");
    public static Block wirelessBimare = new BlockCustomWireless("wirelessBimare");
    public static Block wirelessDefit = new BlockCustomWireless("wirelessDefit");
    public static Block wirelessEfrim = new BlockCustomWireless("wirelessEfrim");
    public static Block wirelessNur = new BlockCustomWireless("wirelessNur");
    public static Block wirelessXaur = new BlockCustomWireless("wirelessXaur");

    public static Block advanced_driver = new BlockAdvancedDriver("adv_me_drive");

    public static Block fluidReceiver = new BlockFluidReceiver("fluidReceiver");
    public static Block fluidSupplier = new BlockAENetwork("fluidSupplier", TileFluidSupplier.class);

    public static void registerBlocks() {
        RegisterUtils.registerBlocks(ItemBlockAE2.class, A_ADVANCED_INTERFACE, A_HYBRID_INTERFACE, A_ULTIMATE_INTERFACE,
                autoCrystallizer, autoPress, serverME, advanced_driver);
        RegisterUtils.registerBlock(A_ACCELERATORS, ItemBlockAAccelerators.class);
        RegisterUtils.registerBlocks(ModItemBlock.class, cobblestone_duper, baseMolecularAssembler, hybridMolecularAssembler, ultimateMolecularAssembler);
        RegisterUtils.registerBlocks(ItemBlockCustomEnergyCell.class, advanced_energy_cell, hybrid_energy_cell, ultimate_energy_cell);
        RegisterUtils.registerBlocks(wirelessAlite, wirelessBimare, wirelessDefit, wirelessEfrim, wirelessNur, wirelessXaur, fluidReceiver, fluidSupplier);

        RegisterUtils.registerTile(TileAdvancedEnergyCell.class);
        RegisterUtils.registerTile(TileHybridEnergyCell.class);
        RegisterUtils.registerTile(TileUltimateEnergyCell.class);

        RegisterUtils.registerTile(TileBaseMolecularAssembler.class);
        RegisterUtils.registerTile(TileHybridMolecularAssembler.class);
        RegisterUtils.registerTile(TileUltimateMolecularAssembler.class);

        RegisterUtils.registerTile(TileCobblestoneDuper.class);

        RegisterUtils.registerTile(TileMEServer.class);

        RegisterUtils.registerTile(TileFluidReceiver.class);
        RegisterUtils.registerTile(TileFluidSupplier.class);

        RegisterUtils.registerTile(TileAdvancedDrive.class);

        RegisterUtils.registerTile(TileAutoCrystallizer.class);
        RegisterUtils.registerTile(TileAutoPress.class);

        RegisterUtils.registerTile(TileCustomCraftingStorage.class);
        RegisterUtils.registerTile(TileCustomAccelerators.class);
        RegisterUtils.registerTile(TileAdvancedInterface.class);
        RegisterUtils.registerTile(TileHybridInterface.class);
        RegisterUtils.registerTile(TileUltimateInterface.class);

        RegisterUtils.findClasses("foxiwhitee.hellmod.tile.wireless", TileEntity.class).forEach(RegisterUtils::registerTile);

        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage256K", 262144);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage1M", 1048576);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage4M", 4194304);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage16M", 16777216);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage65M", 67108864);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage262M", 268435456);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage1G", 1073741824);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage4G", 4294967296L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage16G", 17179869184L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage67G", 68719476736L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage268G", 274877906944L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage1T", 1099511627776L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage4T", 4398046511104L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage17T", 17592186044416L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage68T", 70368744177664L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage274T", 281474976710656L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage1P", 1125899906842624L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage4P", 4503599627370496L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage17P", 1794402976530432L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage70P", 72057594037927936L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage281P", 288230376151711744L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage1E", 1152921504606846976L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage4E", 4611686018427387904L);
        BlockCustomCraftingStorage.registerCraftingStorage("blockCraftingStorage9E", Long.MAX_VALUE);
        BlockCustomCraftingStorage.registerBlocks();
    }

}
