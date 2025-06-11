package foxiwhitee.hellmod.integration.botania.recipes;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.RecipeManaInfusion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomRecipeManaInfusion extends RecipeManaInfusion implements IBotaniaManaRecipe {
    public CustomRecipeManaInfusion(ItemStack output, Object input, int mana) {
        super(output, input, mana);
    }

    public CustomRecipeManaInfusion(RecipeManaInfusion oldRecipe) {
        super(oldRecipe.getOutput(), oldRecipe.getInput(), oldRecipe.getManaToConsume());
        this.setAlchemy(oldRecipe.isAlchemy());
        this.setConjuration(oldRecipe.isConjuration());
    }

    @Override
    public List<Object> getInputs() {
        return new ArrayList<>(Arrays.asList(this.getInput()));
    }

    @Override
    public ItemStack getOut() {
        return this.getOutput();
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        return this.matches(stacks.get(0));
    }

    @Override
    public int getManaUsage() {
        return this.getManaToConsume();
    }
}
