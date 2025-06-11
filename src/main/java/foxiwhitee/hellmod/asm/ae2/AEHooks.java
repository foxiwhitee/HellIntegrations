package foxiwhitee.hellmod.asm.ae2;

import appeng.api.networking.*;
import appeng.api.storage.data.IAEItemStack;
import appeng.core.sync.GuiBridge;
import appeng.crafting.CraftingLink;
import appeng.me.*;
import appeng.me.cache.CraftingGridCache;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.helpers.AENetworkProxy;
import appeng.tile.crafting.TileCraftingStorageTile;
import appeng.tile.crafting.TileCraftingTile;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import foxiwhitee.hellmod.integration.avaritia.parts.PartBigPatternTerminal;
import foxiwhitee.hellmod.integration.avaritia.parts.PartCraftingTerminal9x9;
import foxiwhitee.hellmod.integration.avaritia.parts.PartNeutronCompressorPatternTerminal;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.parts.*;
import foxiwhitee.hellmod.integration.ic2.IC2Integration;
import foxiwhitee.hellmod.integration.thaumcraft.ThaumcraftIntegration;
import foxiwhitee.hellmod.integration.thaumcraft.parts.PartAlchemicalConstructionPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.parts.PartInfusionPatternTerminal;
import foxiwhitee.hellmod.integration.utility.ExtraUtilityIntegration;
import foxiwhitee.hellmod.items.ItemFluidDrop;
import foxiwhitee.hellmod.parts.*;
import foxiwhitee.hellmod.parts.cables.PartBaseCable;
import foxiwhitee.hellmod.proxy.CommonProxy;
import foxiwhitee.hellmod.tile.cpu.TileCustomCraftingStorage;
import foxiwhitee.hellmod.tile.cpu.TileMEServer;
import foxiwhitee.hellmod.tile.interfaces.TileAdvancedInterface;
import foxiwhitee.hellmod.tile.interfaces.TileHybridInterface;
import foxiwhitee.hellmod.tile.interfaces.TileUltimateInterface;
import foxiwhitee.hellmod.utils.cables.ICustomChannelCount;
import foxiwhitee.hellmod.utils.craft.ICraftingGridCacheAddition;
import foxiwhitee.hellmod.utils.craft.IMachineSetAccessor;
import net.minecraft.item.ItemStack;

public class AEHooks {

    public static long skipBytes(long old, IAEItemStack what) {
        if (what.getItem() == BotaniaIntegration.mana_drop || what.getItem() == ItemFluidDrop.DROP)
            return 0L;
        return old;
    }

    public static void onGridNotification(AENetworkProxy proxy) {
        if (proxy.getMachine() instanceof PartBaseCable)
            ((PartBaseCable)proxy.getMachine()).markForUpdate();
    }

    public static int getMaxChannelCount(GridNode node, int old) {
        if (node.getGridBlock().getMachine() instanceof ICustomChannelCount)
            return ((ICustomChannelCount)node.getGridBlock().getMachine()).getMaxChannelSize();
        return old;
    }

    public static long getStorageBytes(long currentBytes, TileCraftingTile tile) {
        if (tile instanceof TileCustomCraftingStorage) {
            long bytes = currentBytes + ((TileCustomCraftingStorage) tile).getStorageBytesLong() - 1;
            if (bytes < 0) return Long.MAX_VALUE - 1;
            return bytes;
        }
        return currentBytes;
    }

    public static ItemStack getItem(Object target, ItemStack old) {
        if (target instanceof TileAdvancedInterface) {
            return new ItemStack(ModBlocks.A_ADVANCED_INTERFACE);
        }
        if (target instanceof TileHybridInterface) {
            return new ItemStack(ModBlocks.A_HYBRID_INTERFACE);
        }
        if (target instanceof TileUltimateInterface) {
            return new ItemStack(ModBlocks.A_ULTIMATE_INTERFACE);
        }
        if (target instanceof PartAdvancedInterface) {
            return EnumParts.PART_ADV_INTERFACE.getStack();
        }
        if (target instanceof PartHybridInterface) {
            return EnumParts.PART_HYBRID_INTERFACE.getStack();
        }
        if (target instanceof PartUltimateInterface) {
            return EnumParts.PART_ULTIMATE_INTERFACE.getStack();
        }
        return old;
    }

    public static GuiBridge getGui(Object target, GuiBridge old) {
        if (target instanceof TileAdvancedInterface || target instanceof PartAdvancedInterface) {
            return CommonProxy.getGuiAdvInterface();
        }
        if (target instanceof TileHybridInterface || target instanceof PartHybridInterface) {
            return CommonProxy.getGuiHybridInterface();
        }
        if (target instanceof TileUltimateInterface || target instanceof PartUltimateInterface) {
            return CommonProxy.getGuiUltimateInterface();
        }
        if (target instanceof PartAdvancedInterfaceTerminal) {
            return CommonProxy.getGuiBridge(0);
        }
        return old;
    }

    public static void updateCPUClusters(IGrid grid, ICraftingGridCacheAddition cache) {
        for(IGridNode cst : grid.getMachines(TileMEServer.class)) {
            TileMEServer tile = (TileMEServer)cst.getMachine();
            for (int i = 0; i < tile.getVirtualClusters().size(); i++) {
                CraftingCPUCluster cluster = (CraftingCPUCluster)tile.getVirtualClusters().get(i);
                if (cluster != null) {
                    cache.getCraftingCPUClusters().add(cluster);
                    if (cluster.getLastCraftingLink() != null) {
                        ((CraftingGridCache)cache).addLink((CraftingLink)cluster.getLastCraftingLink());
                    }
                }
            }
        }
    }

    public static IMachineSet getMachines(MachineSet old, Grid grid) {
        if (old.getMachineClass() == TileCraftingStorageTile.class) {
            MachineSet n = ((IMachineSetAccessor)old).create(old.getMachineClass());
            n.addAll(old);
            old = n;
            MachineSet set = (MachineSet) grid.getMachines(TileCustomCraftingStorage.class);
            old.addAll(set);
        }
        return old;
    }

    public static GuiBridge getContainerConfirm(Object target, GuiBridge old) {
        if (target instanceof PartManaPoolPatternTerminal) {
            return BotaniaIntegration.getGuiBridge(0);
        } else if (target instanceof PartAlchemicalConstructionPatternTerminal) {
            return ThaumcraftIntegration.getGuiBridge(0);
        } else if (target instanceof PartBigPatternTerminal) {
            return AvaritiaIntegration.getGuiBridge(0);
        } else if (target instanceof PartCraftingTerminal9x9) {
            return AvaritiaIntegration.getGuiBridge(1);
        } else if (target instanceof PartElvenTradePatternTerminal) {
            return BotaniaIntegration.getGuiBridge(2);
        } else if (target instanceof PartInfusionPatternTerminal) {
            return ThaumcraftIntegration.getGuiBridge(1);
        } else if (target instanceof PartNeutronCompressorPatternTerminal) {
            return AvaritiaIntegration.getGuiBridge(2);
        } else if (target instanceof PartPetalsPatternTerminal) {
            return BotaniaIntegration.getGuiBridge(3);
        } else if (target instanceof PartPureDaisyPatternTerminal) {
            return BotaniaIntegration.getGuiBridge(4);
        } else if (target instanceof PartRuneAltarPatternTerminal) {
            return BotaniaIntegration.getGuiBridge(5);
        }
        return old;
    }

    public static GuiBridge getGuiConfirm(Object target, GuiBridge old) {
        if (target instanceof PartManaPoolPatternTerminal) {
            return BotaniaIntegration.getGuiBridge(0);
        } else if (target instanceof PartAlchemicalConstructionPatternTerminal) {
            return ThaumcraftIntegration.getGuiBridge(0);
        } else if (target instanceof PartBigPatternTerminal) {
            return AvaritiaIntegration.getGuiBridge(0);
        } else if (target instanceof PartCraftingTerminal9x9) {
            return AvaritiaIntegration.getGuiBridge(1);
        } else if (target instanceof PartElvenTradePatternTerminal) {
            return BotaniaIntegration.getGuiBridge(2);
        } else if (target instanceof PartInfusionPatternTerminal) {
            return ThaumcraftIntegration.getGuiBridge(1);
        } else if (target instanceof PartNeutronCompressorPatternTerminal) {
            return AvaritiaIntegration.getGuiBridge(2);
        } else if (target instanceof PartPetalsPatternTerminal) {
            return BotaniaIntegration.getGuiBridge(3);
        } else if (target instanceof PartPureDaisyPatternTerminal) {
            return BotaniaIntegration.getGuiBridge(4);
        } else if (target instanceof PartRuneAltarPatternTerminal) {
            return BotaniaIntegration.getGuiBridge(5);
        }
        return old;
    }

}
