package foxiwhitee.hellmod.items;

import appeng.me.helpers.IGridProxyable;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import foxiwhitee.hellmod.integration.avaritia.blocks.BlockCustomNeutronCollector;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.blocks.BlockCustomManaPool;
import foxiwhitee.hellmod.integration.botania.blocks.BlockCustomSpreader;
import foxiwhitee.hellmod.integration.ic2.IC2Integration;
import foxiwhitee.hellmod.integration.ic2.blocks.BlockAutoReplicator;
import foxiwhitee.hellmod.integration.ic2.blocks.BlockCustomMatterGen;
import foxiwhitee.hellmod.integration.ic2.blocks.BlockCustomSolarPanel;
import foxiwhitee.hellmod.integration.thaumcraft.ThaumcraftIntegration;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public class ModItemBlock extends ItemBlock {
    private final Block blockType;

    public ModItemBlock(Block b) {
        super(b);
        this.blockType = b;
    }

    public String getUnlocalizedName() {
        return this.blockType.getUnlocalizedName();
    }

    public String getUnlocalizedName(ItemStack i) {
        return this.blockType.getUnlocalizedName();
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
            return false;
        if (this.blockType instanceof net.minecraft.block.ITileEntityProvider) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof IGridProxyable)
                ((IGridProxyable) tile).getProxy().setOwner(player);
        }
        return true;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
        if (HellConfig.enable_tooltips) {
            if (this.blockType.equals(IC2Integration.panel1) ||
                this.blockType.equals(IC2Integration.panel2) ||
                this.blockType.equals(IC2Integration.panel3) ||
                this.blockType.equals(IC2Integration.panel4) ||
                this.blockType.equals(IC2Integration.panel5) ||
                this.blockType.equals(IC2Integration.panel6) ||
                this.blockType.equals(IC2Integration.panel7) ||
                this.blockType.equals(IC2Integration.panel8)) {

                list.add(LocalizationUtils.localize("tooltip.panel.generation.day") + ((BlockCustomSolarPanel) (this.blockType)).getGenDay());
                list.add(LocalizationUtils.localize("tooltip.panel.generation.night") + ((BlockCustomSolarPanel) (this.blockType)).getGenNight());
                list.add(LocalizationUtils.localize("tooltip.panel.output") + ((BlockCustomSolarPanel) (this.blockType)).getGenDay() * 2);
                list.add(LocalizationUtils.localize("tooltip.panel.storage") + ((BlockCustomSolarPanel) (this.blockType)).getGenDay() * 10);
            } else if (this.blockType.equals(ThaumcraftIntegration.stabilizer)) {
                list.add(LocalizationUtils.localize("tooltip.stabilizer"));
            } else if (this.blockType.equals(AvaritiaIntegration.basicNeutronCollector) ||
                    this.blockType.equals(AvaritiaIntegration.advancedNeutronCollector) ||
                    this.blockType.equals(AvaritiaIntegration.hybridNeutronCollector) ||
                    this.blockType.equals(AvaritiaIntegration.ultimateNeutronCollector) ||
                    this.blockType.equals(AvaritiaIntegration.quantiumNeutronCollector)) {
                list.add(LocalizationUtils.localize("tooltip.neutronCollector", ((BlockCustomNeutronCollector)blockType).getStack().stackSize, ((BlockCustomNeutronCollector)blockType).getStack().getDisplayName(), ((BlockCustomNeutronCollector)blockType).getTicks()));
            } else if (this.blockType.equals(BotaniaIntegration.asgardPool) ||
                    this.blockType.equals(BotaniaIntegration.helhelmPool) ||
                    this.blockType.equals(BotaniaIntegration.valhallaPool) ||
                    this.blockType.equals(BotaniaIntegration.midgardPool)) {
                list.add(LocalizationUtils.localize("tooltip.customManaPool", ((BlockCustomManaPool)blockType).getMaxMana() / 1000000));
            } else if (this.blockType.equals(BotaniaIntegration.asgardSpreader) ||
                    this.blockType.equals(BotaniaIntegration.helhelmSpreader) ||
                    this.blockType.equals(BotaniaIntegration.valhallaSpreader) ||
                    this.blockType.equals(BotaniaIntegration.midgardSpreader)) {
                list.add(LocalizationUtils.localize("tooltip.customManaSpreader", ((BlockCustomSpreader)blockType).getManaPerSec()));
            } else if (this.blockType.equals(IC2Integration.advanced_matter) ||
                    this.blockType.equals(IC2Integration.nano_matter) ||
                    this.blockType.equals(IC2Integration.quantum_matter)) {
                list.add(LocalizationUtils.localize("tooltip.customMatterGen.matter", ((BlockCustomMatterGen)blockType).getMatter()));
                list.add(LocalizationUtils.localize("tooltip.customMatterGen.tank", ((BlockCustomMatterGen)blockType).getTank()));
            } else if (this.blockType.equals(IC2Integration.advanced_scanner)) {
                list.add(LocalizationUtils.localize("tooltip.advancedScanner"));
            } else if (this.blockType.equals(IC2Integration.advanced_replicator) ||
                    this.blockType.equals(IC2Integration.nano_replicator) ||
                    this.blockType.equals(IC2Integration.quantum_replicator)) {
                list.add(LocalizationUtils.localize("tooltip.customReplicator.speed", ((BlockAutoReplicator)blockType).getItemsPerSec()));
                list.add(LocalizationUtils.localize("tooltip.customReplicator.discount", ((BlockAutoReplicator)blockType).getDiscount()).replace("PROS", "%"));
                list.add(LocalizationUtils.localize("tooltip.customReplicator.desc"));
            } else if (this.blockType.equals(ModBlocks.baseMolecularAssembler)) {
                list.add(LocalizationUtils.localize("tooltip.assembler.speed", HellConfig.basic_molecular_assembler_speed));
            } else if (this.blockType.equals(ModBlocks.hybridMolecularAssembler)) {
                list.add(LocalizationUtils.localize("tooltip.assembler.speed", HellConfig.hybrid_molecular_assembler_speed));
            } else if (this.blockType.equals(ModBlocks.ultimateMolecularAssembler)) {
                list.add(LocalizationUtils.localize("tooltip.assembler.speed", HellConfig.ultimate_molecular_assembler_speed));
            } else if (this.blockType.equals(ModBlocks.cobblestone_duper)) {
                list.add(LocalizationUtils.localize("tooltip.cobblestoneDuper"));
            }
        }
    }
}

