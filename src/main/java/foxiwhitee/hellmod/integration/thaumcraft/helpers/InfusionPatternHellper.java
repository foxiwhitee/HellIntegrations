package foxiwhitee.hellmod.integration.thaumcraft.helpers;

import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.CustomInfusionRecipe;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.IThaumcraftRecipe;
import foxiwhitee.hellmod.recipes.*;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.lib.crafting.InfusionRunicAugmentRecipe;

import java.util.ArrayList;
import java.util.List;

public class InfusionPatternHellper extends UniversalPatternHelper {
    public InfusionPatternHellper(ItemStack is, World w) {
        super(is, w);
    }

    public static IHellRecipe findMatchingRecipe(InventoryCrafting matrix) {
        List<ItemStack> itemstacks = new ArrayList<>();
        ItemStack itemstack;
        for (int i = 0; i <  matrix.getSizeInventory(); i++) {
            itemstack = matrix.getStackInSlot(i);
            if (itemstack != null) {
                itemstacks.add(itemstack);
            }
        }
        if (itemstacks.isEmpty()) {
            return null;
        }
        for (ItemStack stack : itemstacks) {
            if (stack == null) {
                return null;
            }
        }
        IThaumcraftRecipe recipe = null;
        for (int var7 = 0; var7 < ThaumcraftApi.getCraftingRecipes().size(); ++var7) {
            if (ThaumcraftApi.getCraftingRecipes().get(var7) instanceof InfusionRecipe) {
                InfusionRecipe r = (InfusionRecipe) ThaumcraftApi.getCraftingRecipes().get(var7);
                try {
                    if (r != null && !(r instanceof InfusionRunicAugmentRecipe))
                    recipe = new CustomInfusionRecipe(r);
                } catch (NullPointerException e) {}
                if (recipe.matches(itemstacks)) {
                    return recipe;
                }
            }
        }
        return null;
    }

    public static IThaumcraftRecipe findMatchingRecipe(List<ItemStack> stacks, ItemStack out) {

        if (stacks == null) {
            return null;
        }
        for (ItemStack stack : stacks) {
            if (stack != null) {
                if (stack.stackSize == 1 && stack.getItem().isRepairable()) {
                    return null;
                }
            }
        }
        IThaumcraftRecipe recipe = null;
        for (int var7 = 0; var7 < ThaumcraftApi.getCraftingRecipes().size(); ++var7) {
            if (ThaumcraftApi.getCraftingRecipes().get(var7) instanceof InfusionRecipe) {
                InfusionRecipe ir = (InfusionRecipe) ThaumcraftApi.getCraftingRecipes().get(var7);
                if (ir != null && !(ir instanceof InfusionRunicAugmentRecipe)) {
                    recipe = new CustomInfusionRecipe(ir);
                    if (recipe.matches(stacks, out)) {
                        return recipe;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected int getWidthInventory() {
        return 17;
    }

    @Override
    protected int getHeightInventory() {
        return 1;
    }
}
