package foxiwhitee.hellmod.integration.avaritia.recipes;

import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CustomExtremeShapedOreRecipe extends ExtremeShapedOreRecipe implements IAvaritiaRecipe {
    public CustomExtremeShapedOreRecipe(ExtremeShapedOreRecipe old) {
        super(old.getRecipeOutput(), old.getInput());
    }

    @Override
    public ItemStack getOut() {
        return getRecipeOutput();
    }

    @Override
    public List<Object> getInputs() {
        return Arrays.asList(getInput());
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        return false;
    }
}
