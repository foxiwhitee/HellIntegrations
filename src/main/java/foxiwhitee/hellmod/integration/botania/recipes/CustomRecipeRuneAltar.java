package foxiwhitee.hellmod.integration.botania.recipes;

import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class CustomRecipeRuneAltar extends RecipeRuneAltar implements IBotaniaManaRecipe {
    public CustomRecipeRuneAltar(ItemStack output, int mana, Object... inputs) {
        super(output, mana, inputs);
    }

    public CustomRecipeRuneAltar(RecipeRuneAltar oldRecipe) {
        super(oldRecipe.getOutput(), oldRecipe.getManaUsage(), oldRecipe.getInputs().toArray());
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
            if(IBotaniaManaRecipe.super.simpleAreStacksEqual(stack, new ItemStack(ModBlocks.livingrock))) {
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
                } else if (input instanceof ItemStack && IBotaniaManaRecipe.super.simpleAreStacksEqual((ItemStack)input, stack)) {
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

}
