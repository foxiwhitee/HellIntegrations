package foxiwhitee.hellmod.recipes;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseAutoBlockRecipe implements IHellRecipe{
    List<Object> inputs;
    ItemStack output;
    public BaseAutoBlockRecipe(ItemStack output, List<Object> inputs) {
        this.output = output;
        this.inputs = inputs;
    }
    @Override
    public ItemStack getOut() {
        return output;
    }

    @Override
    public List<Object> getInputs() {
        return inputs;
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        return false;
    }

}
