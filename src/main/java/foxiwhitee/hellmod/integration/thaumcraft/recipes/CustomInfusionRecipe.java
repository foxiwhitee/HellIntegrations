package foxiwhitee.hellmod.integration.thaumcraft.recipes;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

import java.util.ArrayList;
import java.util.List;

public class CustomInfusionRecipe extends InfusionRecipe implements IThaumcraftRecipe {
    List<Object> inputs = new ArrayList<>();
    public CustomInfusionRecipe(String research, Object output, int inst, AspectList aspects2, ItemStack input, ItemStack[] recipe) {
        super(research, output, inst, aspects2, input, recipe);
        inputs.add(input);
        for (ItemStack stack : recipe) {
            inputs.add(stack);
        }
    }

    public CustomInfusionRecipe(InfusionRecipe oldRecipe) {
        super(oldRecipe.getResearch(), oldRecipe.getRecipeOutput(), oldRecipe.getInstability(), oldRecipe.getAspects(), oldRecipe.getRecipeInput(), oldRecipe.getComponents());
        inputs.add(oldRecipe.getRecipeInput());
        for (ItemStack stack : oldRecipe.getComponents()) {
            inputs.add(stack);
        }
    }

    @Override
    public boolean matches(List<ItemStack> stacks, ItemStack out) {
        if (this.getRecipeOutput() instanceof ItemStack) {
            return simpleAreStacksEqual((ItemStack) this.getRecipeOutput(), out) && this.matches(stacks);
        } else {
            return false;
        }

    }

    @Override
    public ItemStack getOut() {
        if (this.getRecipeOutput() instanceof ItemStack) {
            return (ItemStack) this.getRecipeOutput();
        } else {
            return null;
        }
    }

    @Override
    public List<Object> getInputs() {
        return inputs;
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        ArrayList<ItemStack> stacksCopy = new ArrayList<>();
        ItemStack base = null;
        ItemStack st;
        for (int i = 0; i < stacks.size(); i++) {
            st = stacks.get(i);
            if (st != null) {
                if (i != 0) {
                    stacksCopy.add(st);
                } else {
                    base = st;
                }
            }
        }
        ItemStack recipeInput = this.getRecipeInput();
        if (this.getRecipeInput() == null) {
            return false;
        } else if (base != null && !stacksCopy.isEmpty()) {
            ItemStack i2 = base;

            if (!areItemStacksEqual(i2, this.getRecipeInput(), false)) {
                return false;
            } else {
                ArrayList<ItemStack> ii = new ArrayList();

                for(ItemStack is : stacksCopy) {
                    ii.add(is.copy());
                }

                for(ItemStack comp : this.getComponents()) {
                    boolean b = false;

                    for(int a = 0; a < ii.size(); ++a) {
                        i2 = ((ItemStack)ii.get(a)).copy();

                        if (areItemStacksEqual(i2, comp, true)) {
                            ii.remove(a);
                            b = true;
                            break;
                        }
                    }

                    if (!b) {
                        return false;
                    }
                }

                return ii.size() == 0;
            }
        }
        return false;
    }

}
