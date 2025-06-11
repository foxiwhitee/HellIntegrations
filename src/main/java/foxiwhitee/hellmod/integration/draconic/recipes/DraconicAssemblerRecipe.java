package foxiwhitee.hellmod.integration.draconic.recipes;

import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DraconicAssemblerRecipe implements IDraconicAssemblerRecipe {
    private final ItemStack output;
    private final List<Object> inputs;
    private final int level;
    private final long energy;

    public DraconicAssemblerRecipe(ItemStack output, int level, long energy, Object... inputs) {
        this.output = output;
        this.level = level;
        this.energy = energy;
        this.inputs = Arrays.asList(inputs);
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
    public boolean matches(List<Object> stacks, int[] levels, int count) {
        if (count <= 0) return false;

        for (int level : levels) {
            if (!(level >= this.getLevel())) return false;
        }

        List<Object> ins = new ArrayList<>();
        for (Object input : this.getInputs()) {
            ins.add(input);
        }
        List<ItemStack> stacksCopy = new ArrayList<>();
        for (Object stack : stacks) {
            if (stack instanceof ItemStack && stack != null) stacksCopy.add((ItemStack) stack);
        }

        for (ItemStack stack : new ArrayList<>(stacksCopy)) {
            for (int i = 0; i < ins.size(); i++) {
                Object input = ins.get(i);
                if (input instanceof ItemStack) {
                    ItemStack inputStack = (ItemStack) input;
                    if (inputStack.getItem() == stack.getItem() &&
                            inputStack.getItemDamage() == stack.getItemDamage() &&
                            stack.stackSize >= inputStack.stackSize * count) {
                        ins.remove(i);
                        stacksCopy.remove(stack);
                        break;
                    }
                } else if (OreDictUtil.areStacksEqual(input, stack)) {
                    ins.remove(i);
                    stacksCopy.remove(stack);
                    break;
                }
            }
        }

        return ins.isEmpty() && stacksCopy.isEmpty();
    }

    public int getLevel() {
        return level;
    }

    public long getEnergy() {
        return energy;
    }
}
