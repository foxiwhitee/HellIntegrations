package foxiwhitee.hellmod.integration.thaumcraft.helpers;

import foxiwhitee.hellmod.helpers.UniversalMultiPatternHelper;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.CustomAlchemicalConstructionRecipe;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.IThaumcraftRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlchemicalConstructionPatternHelper extends UniversalMultiPatternHelper {
    public AlchemicalConstructionPatternHelper(ItemStack is, World w) {
        super(is, w);
    }

    public static List<IHellRecipe> findMatchingRecipe(InventoryCrafting matrix) {
        ItemStack itemstack = matrix.getStackInSlot(0);

        if (itemstack == null) {
            return null;
        }
        List<IHellRecipe> recipes = new ArrayList<>();
        IHellRecipe recipe = null;
        for (int var7 = 0; var7 < ThaumcraftApi.getCraftingRecipes().size(); ++var7) {
            if (ThaumcraftApi.getCraftingRecipes().get(var7) instanceof CrucibleRecipe) {
                recipe = new CustomAlchemicalConstructionRecipe((CrucibleRecipe) ThaumcraftApi.getCraftingRecipes().get(var7));
                if (recipe.matches(new ArrayList<>(Arrays.asList(itemstack)))) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    public static IThaumcraftRecipe findMatchingRecipe(List<ItemStack> stacks, ItemStack out) {
        ItemStack itemstack = stacks.get(0);

        if (itemstack == null) {
            return null;
        }

        if (itemstack.stackSize == 1 && itemstack.getItem().isRepairable()) {
            return null;
        }
        IThaumcraftRecipe recipe = null;
        for (int var7 = 0; var7 < ThaumcraftApi.getCraftingRecipes().size(); ++var7) {
            if (ThaumcraftApi.getCraftingRecipes().get(var7) instanceof CrucibleRecipe) {
                recipe = new CustomAlchemicalConstructionRecipe((CrucibleRecipe) ThaumcraftApi.getCraftingRecipes().get(var7));
                if (recipe.matches(new ArrayList<>(Arrays.asList(itemstack)), out)) {
                    return recipe;
                }
            }
        }
        return null;
    }

    @Override
    protected int getWidthInventory() {
        return 1;
    }

    @Override
    protected int getHeightInventory() {
        return 1;
    }
}
