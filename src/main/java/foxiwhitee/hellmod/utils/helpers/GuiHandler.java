package foxiwhitee.hellmod.utils.helpers;

import cpw.mods.fml.common.network.IGuiHandler;
import foxiwhitee.hellmod.utils.handler.GuiHandlerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
//        TileEntity tile = world.getTileEntity(x, y, z);
//        switch (ID) {
//            case 0:
//                if (tile instanceof TileCustomNeutronCollector)
//                    return new ContainerCustomNeutronCollector(player.inventory, (TileCustomNeutronCollector)tile);
//                break;
//            case 1:
//                if (tile instanceof TileCustomSolarPanel)
//                    return new ContainerCustomSolarPanel(player.inventory, (TileCustomSolarPanel)tile);
//                break;
//            case 2:
//                if (tile instanceof TileCustomMatterGen)
//                    return new ContainerCustomMatterGen(player, (TileCustomMatterGen)tile);
//                break;
//            case 3:
//                if (tile instanceof TileAdvancedScanner)
//                    return new ContainerAdvancedScanner(player, (TileAdvancedScanner)tile);
//                break;
//            case 4:
//                if (tile instanceof TileCobblestoneDuper)
//                    return new ContainerCobblestoneDuper(player.inventory, (TileCobblestoneDuper)tile);
//                break;
//            case 5:
//                if (tile instanceof TileAutomatedUpgradeableBlock)
//                    return new ContainerAutomatedUpgradeableBlock(player.inventory, (TileAutomatedUpgradeableBlock)tile);
//                break;
//            case 6:
//                if (tile instanceof TileDraconicAssembler)
//                    return new ContainerDraconicAssembler(player.inventory, (TileDraconicAssembler) tile);
//                break;
//            case 7:
//                if (tile instanceof TileFusionCraftingCore)
//                    return new ContainerFusionCraftingCore(player, (TileFusionCraftingCore)tile);
//                break;
//            case 8:
//                if (tile instanceof TileCustomUpgradeModifier)
//                    return new ContainerCustomUpgradeModifier(player.inventory, (TileCustomUpgradeModifier)tile);
//                break;
//        }
//        return null;
        return GuiHandlerUtils.getContainer(ID, player, world, x, y, z);

    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
//        TileEntity tile = world.getTileEntity(x, y, z);
//        switch (ID) {
//            case 0:
//                if (tile instanceof TileCustomNeutronCollector)
//                    return new GuiCustomNeutronCollector(player.inventory, (TileCustomNeutronCollector)tile);
//                break;
//            case 1:
//                if (tile instanceof TileCustomSolarPanel)
//                    return new GuiCustomSolarPanel(player.inventory, (TileCustomSolarPanel)tile);
//                break;
//            case 2:
//                if (tile instanceof TileCustomMatterGen)
//                    return new GuiCustomMatterGen(new ContainerCustomMatterGen(player, (TileCustomMatterGen) tile));
//                break;
//            case 3:
//                if (tile instanceof TileAdvancedScanner)
//                    return new GuiAdvancedScanner(new ContainerAdvancedScanner(player, (TileAdvancedScanner) tile));
//                break;
//            case 4:
//                if (tile instanceof TileCobblestoneDuper)
//                    return new GuiCobblestoneDuper(player.inventory, (TileCobblestoneDuper)tile);
//                break;
//            case 5:
//                if (tile instanceof TileAutomatedUpgradeableBlock)
//                    return new GuiAutomatedUpgradeableBlock(player.inventory, (TileAutomatedUpgradeableBlock)tile);
//                break;
//            case 6:
//                if (tile instanceof TileDraconicAssembler)
//                    return new GuiDraconicAssembler(new ContainerDraconicAssembler(player.inventory, (TileDraconicAssembler) tile));
//                break;
//            case 7:
//                if (tile instanceof TileFusionCraftingCore)
//                    return new GuiFusionCraftingCore(player, (TileFusionCraftingCore)tile);
//                break;
//            case 8:
//                if (tile instanceof TileCustomUpgradeModifier)
//                    return new GuiCustomUpgradeModifier(player.inventory, (TileCustomUpgradeModifier)tile, new ContainerCustomUpgradeModifier(player.inventory, (TileCustomUpgradeModifier)tile));
//                break;
//        }
//        return null;
        return GuiHandlerUtils.getGui(ID, player, world, x, y, z);
    }
}
