package foxiwhitee.hellmod.integration.botania.helpers;

import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.botania.recipes.CustomRecipeRuneAltar;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;

import java.util.ArrayList;
import java.util.List;

public class RuneAltalPatternHelper extends UniversalPatternHelper {
    public RuneAltalPatternHelper(ItemStack is, World w) {
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
        CustomRecipeRuneAltar recipe;

        for (int var7 = 0; var7 < BotaniaAPI.runeAltarRecipes.size(); ++var7) {
            RecipeRuneAltar r = BotaniaAPI.runeAltarRecipes.get(var7);
            if (r != null) {
                recipe = new CustomRecipeRuneAltar(r);
                if (recipe.matches(itemstacks)) {
                    return recipe;
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
