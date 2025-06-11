package foxiwhitee.hellmod.integration.draconic.recipes;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IDraconicAssemblerRecipe extends IDraconicRecipe {
    boolean matches(List<Object> stacks, int[] levels, int count);

    default boolean matches(List<Object> stacks, int[] levels) {
        return matches(stacks, levels, 1);
    };

    default boolean advancedAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage() && stack.stackSize == stack2.stackSize;
    }

    @Override
    default boolean matches(List<ItemStack> stacks) {
        return false;
    };

    default int findMaxCraftCount(List<Object> stacks, int[] levels) {
        if (!matches(stacks, levels, 1)) return 0;

        ItemStack result = getOut();
        if (result == null || result.stackSize <= 0) return 0;

        int maxOutputCount = 64 / result.stackSize;

        for (int i = maxOutputCount; i >= 1; i--) {
            if (matches(stacks, levels, i)) return i;
        }

        return 0;
    }
}
