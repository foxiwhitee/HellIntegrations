package foxiwhitee.hellmod.integration.avaritia.recipes;

import fox.spiteful.avaritia.crafting.CompressorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomNeutronCompressorRecipe extends CompressorRecipe implements IAvaritiaRecipe{
    private int realAmount;
    public CustomNeutronCompressorRecipe(ItemStack output, int amount, ItemStack ingredient, boolean exact) {
        super(output, amount, ingredient, exact);
        this.realAmount = amount;
    }

    public CustomNeutronCompressorRecipe(CompressorRecipe oldRecipe) {
        super(oldRecipe.getOutput(), oldRecipe.getCost(), (ItemStack) oldRecipe.getIngredient(), false);
        this.realAmount = oldRecipe.getCost();
    }

    @Override
    public ItemStack getOut() {
        return this.getOutput();
    }

    @Override
    public int getCost() {
        return realAmount;
    }

    @Override
    public List<Object> getInputs() {
        Object o = this.getIngredient();
        if (o instanceof ItemStack) {
            ItemStack s = (ItemStack) o;
            s.stackSize = this.realAmount;
            return new ArrayList<>(Arrays.asList(s));
        }
        return new ArrayList<>(Arrays.asList(o));
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        Object o = this.getIngredient();
        ItemStack stack;
        if (o instanceof ItemStack) {
            stack = (ItemStack) o;
            return simpleAreStacksEqual(stack, stacks.get(0));
        } else if (o instanceof ArrayList) {
            ArrayList<?> al = (ArrayList<?>) o;
            if (al.size() > 0) {
                stack = (ItemStack) al.get(0);
                return simpleAreStacksEqual(stack, stacks.get(0));
            } else {
                return false;
            }
        } else if (o instanceof String) {
            List<ItemStack> al = OreDictionary.getOres((String)o);
            if (al.size() > 0) {
                stack = (ItemStack) al.get(0);
                return simpleAreStacksEqual(stack, stacks.get(0));
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }
}
