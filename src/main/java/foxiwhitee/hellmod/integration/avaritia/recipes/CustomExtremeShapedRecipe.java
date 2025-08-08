package foxiwhitee.hellmod.integration.avaritia.recipes;

import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomExtremeShapedRecipe extends ExtremeShapedRecipe implements IAvaritiaRecipe {
    public CustomExtremeShapedRecipe(ExtremeShapedRecipe old) {
        super(old.recipeWidth, old.recipeHeight, old.recipeItems, old.getRecipeOutput());
    }

    @Override
    public ItemStack getOut() {
        return getRecipeOutput();
    }

    @Override
    public List<Object> getInputs() {
        return Arrays.asList(recipeItems);
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        return false;
    }
}
