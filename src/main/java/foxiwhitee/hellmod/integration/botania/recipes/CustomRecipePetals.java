package foxiwhitee.hellmod.integration.botania.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipePetals;

import java.util.ArrayList;
import java.util.List;

public class CustomRecipePetals extends RecipePetals implements IBotaniaRecipe {
    public CustomRecipePetals(ItemStack output, Object... inputs) {
        super(output, inputs);
    }

    public CustomRecipePetals(RecipePetals oldRecipe) {
        super(oldRecipe.getOutput(), oldRecipe.getInputs().toArray());
    }

    @Override
    public ItemStack getOut() {
        return this.getOutput();
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        List<Object> inputsMissing = new ArrayList(this.getInputs());

        for(int i = 0; i < stacks.size(); ++i) {
            ItemStack stack = stacks.get(i);
            if (stack == null) {
                break;
            }
            if(IBotaniaRecipe.super.simpleAreStacksEqual(stack, new ItemStack(Items.wheat_seeds))) {
                continue;
            }

            int stackIndex = -1;
            int oredictIndex = -1;

            for(int j = 0; j < inputsMissing.size(); ++j) {
                Object input = inputsMissing.get(j);
                if (input instanceof String) {
                    List<ItemStack> validStacks = OreDictionary.getOres((String)input);
                    boolean found = false;

                    for(ItemStack ostack : validStacks) {
                        ItemStack cstack = ostack.copy();
                        if (cstack.getItemDamage() == 32767) {
                            cstack.setItemDamage(stack.getItemDamage());
                        }

                        if (stack.isItemEqual(cstack)) {
                            oredictIndex = j;
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        break;
                    }
                } else if (input instanceof ItemStack && IBotaniaRecipe.super.simpleAreStacksEqual((ItemStack)input, stack)) {
                    stackIndex = j;
                    break;
                }
            }

            if (stackIndex != -1) {
                inputsMissing.remove(stackIndex);
            } else {
                if (oredictIndex == -1) {
                    return false;
                }

                inputsMissing.remove(oredictIndex);
            }
        }

        return inputsMissing.isEmpty();
    }

    public boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }
}
