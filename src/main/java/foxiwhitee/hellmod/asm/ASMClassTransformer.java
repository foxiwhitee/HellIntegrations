package foxiwhitee.hellmod.asm;

import appeng.container.ContainerNull;
import foxiwhitee.hellmod.asm.ae2.AETransformer;
import foxiwhitee.hellmod.asm.blood.BloodHooks;
import foxiwhitee.hellmod.asm.blood.BloodTransformer;
import foxiwhitee.hellmod.asm.draconic.DraconicTransformer;
import foxiwhitee.hellmod.asm.ic2.IC2Transformer;
import foxiwhitee.hellmod.asm.thaumcraft.ThaumTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class ASMClassTransformer  implements IClassTransformer {
    public ASMClassTransformer() {
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        switch (name) {
            case "appeng.client.gui.implementations.GuiPriority":
                return AETransformer.transformGuiPriority(basicClass);
            case "appeng.me.Grid":
                return AETransformer.transformGrid(basicClass);
            case "appeng.me.cluster.implementations.CraftingCPUCluster":
                return AETransformer.transformCraftingCPUCluster(basicClass);
            case "appeng.helpers.DualityInterface":
                return AETransformer.transformDualityInterface(basicClass);
            case "appeng.tile.misc.TileInterface":
                return AETransformer.transformTileInterface(basicClass);
            case "appeng.parts.misc.PartInterface":
                return AETransformer.transformPartInterface(basicClass);
            case "appeng.me.NetworkEventBus":
                return AETransformer.transformNetworkEventBus(basicClass);
            case "appeng.me.MachineSet":
                return AETransformer.transformMachineSet(basicClass);
            case "appeng.api.networking.crafting.ICraftingMedium":
                return AETransformer.transformICraftingMedium(basicClass);
            case "appeng.client.gui.implementations.GuiMEMonitorable":
                return AETransformer.transformGuiMEMonitorable(basicClass);
            case "appeng.client.gui.implementations.GuiCraftConfirm":
                return AETransformer.transformGuiCraftConfirm(basicClass);
            case "appeng.container.implementations.ContainerCraftConfirm":
                return AETransformer.transformContainerCraftConfirm(basicClass);
            case "appeng.core.sync.packets.PacketMEInventoryUpdate":
                return AETransformer.transformPacketMEInventoryUpdate(basicClass);
            case "thaumcraft.common.tiles.TileInfusionMatrix":
                return ThaumTransformer.transformInfusionMatrix(basicClass);
            case "ic2.core.block.machine.tileentity.TileEntityPatternStorage":
                return IC2Transformer.transformTileEntityPatternStorage(basicClass);
            case "WayofTime.alchemicalWizardry.common.bloodAltarUpgrade.UpgradedAltars":
                return BloodTransformer.transformUpgradedAltars(basicClass);
            case "appeng.helpers.IInterfaceHost":
                return AETransformer.transformIInterfaceHost(basicClass);
            case "com.brandon3055.draconicevolution.common.blocks.DraconicBlock":
                return DraconicTransformer.transformDraconicBlock(basicClass);
            case "net.minecraft.client.Minecraft":
                return DraconicTransformer.transformMinecraft(basicClass);
            case "appeng.me.cache.CraftingGridCache":
                return AETransformer.transformCraftingGridCache(basicClass);
            case "appeng.me.cluster.implementations.CraftingCPUCalculator":
                return AETransformer.transformCraftingCPUCalculator(basicClass);
            case "appeng.crafting.CraftingTreeNode":
                return AETransformer.transformCraftingTreeNode(basicClass);
            case "appeng.client.me.ItemRepo":
                return basicClass;//AETransformer.transformItemRepo(basicClass);
            case "net.minecraft.client.gui.FontRenderer":
                return MinecraftTransformer.transformFontRenderer(basicClass);
        }
        return basicClass;
    }
}

