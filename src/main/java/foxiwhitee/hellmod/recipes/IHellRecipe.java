package foxiwhitee.hellmod.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IHellRecipe {

    ItemStack getOut();

    List<Object> getInputs();

    boolean matches(List<ItemStack> stacks);

    default boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }
}
