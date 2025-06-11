package foxiwhitee.hellmod.container.slots;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.definitions.IItems;
import appeng.api.definitions.IMaterials;
import appeng.block.crafting.BlockCraftingStorage;
import appeng.block.crafting.BlockCraftingUnit;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import foxiwhitee.hellmod.blocks.cpu.BlockCustomAccelerators;
import foxiwhitee.hellmod.blocks.cpu.BlockCustomCraftingStorage;
import foxiwhitee.hellmod.helpers.IAutomatedBlockUpgrade;
import foxiwhitee.hellmod.integration.avaritia.helpers.INeutronCollector;
import foxiwhitee.hellmod.integration.botania.flowers.ICoreFunctionalFlower;
import foxiwhitee.hellmod.integration.botania.flowers.ICoreGeneratingFlower;
import foxiwhitee.hellmod.integration.botania.items.ae.ItemUpgradeFlowerSynthesizer;
import foxiwhitee.hellmod.integration.botania.items.generating.ItemCoreOfLife;
import foxiwhitee.hellmod.integration.botania.items.generating.ItemEssenceOfLife;
import foxiwhitee.hellmod.integration.botania.items.generating.ItemUpgradeGeneratorMultiply;
import foxiwhitee.hellmod.integration.botania.items.generating.ItemUpgradeGeneratorSpeed;
import foxiwhitee.hellmod.integration.ic2.helpers.ISpecialSintezatorPanel;
import foxiwhitee.hellmod.integration.ic2.items.ItemEUEnergyUpgrades;
import foxiwhitee.hellmod.integration.ic2.items.ItemSunUpgrade;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CustomSlotRestrictedInput extends CustomAppEngSlot {
    private final PlacableItemType which;
    private final InventoryPlayer p;
    private boolean allowEdit = true;
    private int stackLimit = -1;

    public CustomSlotRestrictedInput(PlacableItemType valid, IInventory i, int slotIndex, int x, int y, InventoryPlayer p) {
        super(i, slotIndex, x, y);
        this.which = valid;
        this.p = p;
    }

    public int getSlotStackLimit() {
        return this.stackLimit != -1 ? this.stackLimit : super.getSlotStackLimit();
    }

    public Slot setStackLimit(int i) {
        this.stackLimit = i;
        return this;
    }

    public boolean isItemValid(ItemStack i) {
        if (!this.getContainer().isValidForSlot(this, i)) {
            return false;
        } else if (i == null) {
            return false;
        } else if (i.getItem() == null) {
            return false;
        } else if (!this.inventory.isItemValidForSlot(this.getSlotIndex(), i)) {
            return false;
        } else if (!this.isAllowEdit()) {
            return false;
        } else {
            IDefinitions definitions = AEApi.instance().definitions();
            IMaterials materials = definitions.materials();
            IItems items = definitions.items();
            switch (this.which) {
                case AUTOMATED_BLOCK_UPGRADE:
                    return i.getItem() instanceof IAutomatedBlockUpgrade;
                case STORAGE:
                    return Block.getBlockFromItem(i.getItem()) instanceof BlockCustomCraftingStorage ||
                            Block.getBlockFromItem(i.getItem()) instanceof BlockCraftingStorage;
                case ACCELERATOR:
                    return Block.getBlockFromItem(i.getItem()) instanceof BlockCustomAccelerators
                            || ((Block.getBlockFromItem(i.getItem()) instanceof BlockCraftingUnit && !(Block.getBlockFromItem(i.getItem()) instanceof BlockCustomCraftingStorage))
                            && (Block.getBlockFromItem(i.getItem()) instanceof BlockCraftingUnit && !(Block.getBlockFromItem(i.getItem()) instanceof BlockCraftingStorage)));
                case NEUTRON:
                    return Block.getBlockFromItem(i.getItem()) instanceof INeutronCollector ||
                            Block.getBlockFromItem(i.getItem()) == LudicrousBlocks.neutron_collector;
                case SUN_UPGRADE:
                    return i.getItem() instanceof ItemSunUpgrade;
                case SYNTHESIZER_UPGRADE:
                    return i.getItem() instanceof ItemEUEnergyUpgrades;
                case SOLAR_PANEL:
                    return Block.getBlockFromItem(i.getItem()) instanceof ISpecialSintezatorPanel || i.getItem() == Ic2Items.solarPanel.getItem();
                case ESSENCE_LIFE:
                    return i.getItem() instanceof ItemEssenceOfLife;
                case CORE_LIFE:
                    return i.getItem() instanceof ItemCoreOfLife;
                case FLOWER_CORES:
                    return i.getItem() instanceof ICoreGeneratingFlower;
                case FLOWER_CORES_FUNC:
                    return i.getItem() instanceof ICoreFunctionalFlower;
                case FLOWER_SYNTHESIZER_UPGRADE:
                    return i.getItem() instanceof ItemUpgradeFlowerSynthesizer;
                case MANA_GENERATOR_UPGRADES:
                    return i.getItem() instanceof ItemUpgradeGeneratorSpeed || i.getItem() instanceof ItemUpgradeGeneratorMultiply;
                default:
                    return false;
            }
        }
    }

    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return this.isAllowEdit();
    }

    public ItemStack getDisplayStack() {
        return super.getStack();
    }

    private boolean isAllowEdit() {
        return this.allowEdit;
    }


    public static enum PlacableItemType {
        AUTOMATED_BLOCK_UPGRADE,
        STORAGE,
        ACCELERATOR,
        NEUTRON,
        SUN_UPGRADE,
        SYNTHESIZER_UPGRADE,
        SOLAR_PANEL,
        ESSENCE_LIFE,
        CORE_LIFE,
        MANA_GENERATOR_UPGRADES,
        FLOWER_CORES,
        FLOWER_CORES_FUNC,
        FLOWER_SYNTHESIZER_UPGRADE;
    }
}
