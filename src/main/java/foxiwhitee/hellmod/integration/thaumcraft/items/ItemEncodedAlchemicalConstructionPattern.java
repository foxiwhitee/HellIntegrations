package foxiwhitee.hellmod.integration.thaumcraft.items;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.CustomAlchemicalConstructionRecipe;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemEncodedAlchemicalConstructionPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedAlchemicalConstructionPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new UniversalPatternHelper(is, false, 1, 1, matrix -> {
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
                            return recipe;//recipes.add(recipe);
                        }
                    }
                }
                return null;//recipes;
            });
        } catch (Throwable var4) {
            return null;
        }
    }
}
