package foxiwhitee.hellmod.integration.botania.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipeElvenTrade;

import java.util.ArrayList;
import java.util.List;

public class CustomRecipeElvenTrade extends RecipeElvenTrade implements IBotaniaManaRecipe{
    public CustomRecipeElvenTrade(ItemStack output, Object... inputs) {
        super(output, inputs);
    }

    public CustomRecipeElvenTrade(RecipeElvenTrade oldRecipe) {
        super(oldRecipe.getOutput(), oldRecipe.getInputs().toArray());
    }

    @Override
    public int getManaUsage() {
        return 5000;
    }

    @Override
    public ItemStack getOut() {
        return this.getOutput();
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        List<Object> ins = new ArrayList<>(this.getInputs());
        List<ItemStack> stacksCopy = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack != null) {
                stacksCopy.add(stack);
            }
        }
        for (ItemStack stack : stacks) {
            if (stack == null) continue;

            for (int i = 0; i < ins.size(); i++) {
                Object input = ins.get(i);

                if (input instanceof ItemStack && simpleAreStacksEqual((ItemStack) input, stack)) {
                    ins.remove(i);
                    stacksCopy.remove(stack);
                    break;
                } else if (input instanceof String) {
                    for (ItemStack itemStack : OreDictionary.getOres((String) input)) {
                        if (simpleAreStacksEqual(itemStack, stack)) {
                            ins.remove(i);
                            stacksCopy.remove(stack);
                            break;
                        }
                    }
                }
            }
        }

        return ins.isEmpty() && stacksCopy.isEmpty();
    }


    public boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }

}
