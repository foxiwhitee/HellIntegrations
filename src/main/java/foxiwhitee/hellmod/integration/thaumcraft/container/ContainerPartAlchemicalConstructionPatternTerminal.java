package foxiwhitee.hellmod.integration.thaumcraft.container;

import appeng.api.storage.ITerminalHost;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.container.slot.SlotRestrictedInput;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.util.Platform;
import foxiwhitee.hellmod.container.slots.CustomSlotPatternTerm;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.helpers.AlchemicalConstructionPatternHelper;
import foxiwhitee.hellmod.integration.thaumcraft.ThaumcraftIntegration;
import foxiwhitee.hellmod.integration.thaumcraft.helpers.ThaumcraftRecipeHelper;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContainerPartAlchemicalConstructionPatternTerminal extends ContainerPatternTerminal {
    private final SlotFakeCraftingMatrix[] craftingSlots = new SlotFakeCraftingMatrix[1];
    private final CustomSlotPatternTerm outputSlot;
    public int craftingIndex = 0;


    public ContainerPartAlchemicalConstructionPatternTerminal(InventoryPlayer ip, ITerminalHost host) {
        super(ip, host);
        this.addSlotToContainer(this.craftingSlots[0] = new SlotFakeCraftingMatrix(this.crafting, 0, 403, 87));

        this.addSlotToContainer(this.outputSlot = new CustomSlotPatternTerm(ip.player, this.getActionSource(), this.getPowerSource(), host, this.crafting, patternInv, this.getInventoryOut(), 403, 168, this, 1, this));
        this.outputSlot.setIIcon(-1);

    }

    public CustomSlotPatternTerm getOutputSlot() {
        return outputSlot;
    }

    @Override
    protected ItemStack[] getOutputs() {
        List crucibleRecipeList = this.getRecipes(this.getInventoryCrafting().getStackInSlot(0));
        return crucibleRecipeList.isEmpty() ? new ItemStack[0] : new ItemStack[]{((CrucibleRecipe)crucibleRecipeList.get(this.craftingIndex)).getRecipeOutput()};
    }

    public List getRecipes(ItemStack centralStack) {
        return ThaumcraftRecipeHelper.findMatchingCrucibleRecipe(centralStack);
    }

    public void receiveEventId(byte id) {
        this.craftingIndex = id;
        List crucibleRecipeList = this.getRecipes(this.getInventoryCrafting().getStackInSlot(0));
        if (!crucibleRecipeList.isEmpty()) {
            this.getOutputSlot().putStack(((CrucibleRecipe)crucibleRecipeList.get(this.craftingIndex)).getRecipeOutput());
        }
    }

    @Override
    protected Item getPattern() {
        return ThaumcraftIntegration.ALCHEMICAL_CONSTRUCTION_PATTERN;
    }

    @Override
    protected SlotFakeCraftingMatrix[] getInventoryCraftingSlots() {
        return this.craftingSlots;
    }

}
