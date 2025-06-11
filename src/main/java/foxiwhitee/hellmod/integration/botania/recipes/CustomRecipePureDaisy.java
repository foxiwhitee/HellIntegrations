package foxiwhitee.hellmod.integration.botania.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.RecipePureDaisy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomRecipePureDaisy extends RecipePureDaisy implements IBotaniaRecipe{
    public CustomRecipePureDaisy(Object input, Block output, int outputMeta) {
        super(input, output, outputMeta);
    }

    public CustomRecipePureDaisy(RecipePureDaisy oldRecipe) {
        super(oldRecipe.getInput(), oldRecipe.getOutput(), oldRecipe.getOutputMeta());
    }

    @Override
    public List<Object> getInputs() {
        return new ArrayList<>(Arrays.asList(this.getInput()));
    }

    @Override
    public ItemStack getOut() {
        return new ItemStack(this.getOutput());
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        return this.matches(null, 0, 0, 0, null, Block.getBlockFromItem(stacks.get(0).getItem()), stacks.get(0).getItemDamage());
    }

}
