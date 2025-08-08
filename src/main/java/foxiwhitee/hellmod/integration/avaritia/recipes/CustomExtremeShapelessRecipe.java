package foxiwhitee.hellmod.integration.avaritia.recipes;

import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CustomExtremeShapelessRecipe extends ExtremeShapelessRecipe implements IAvaritiaRecipe {
    public CustomExtremeShapelessRecipe(ExtremeShapelessRecipe old) {
        super(old.getRecipeOutput(), old.recipeItems);
    }
    public CustomExtremeShapelessRecipe(ItemStack out, List items) {
        super(out, items);
    }

    @Override
    public ItemStack getOut() {
        return getRecipeOutput();
    }

    @Override
    public List<Object> getInputs() {
        return recipeItems;
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        return false;
    }
}
