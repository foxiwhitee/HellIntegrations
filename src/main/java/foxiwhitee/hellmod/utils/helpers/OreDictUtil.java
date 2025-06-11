package foxiwhitee.hellmod.utils.helpers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreDictUtil {
    public static boolean areStacksEqual(Object oreStack, ItemStack compare) {
        if (oreStack instanceof String) {
            if (!OreDictionary.doesOreNameExist((String)oreStack))
                return false;
            int[] ids = OreDictionary.getOreIDs(compare);
            for (int id : ids) {
                if (OreDictionary.getOreName(id).equals(oreStack))
                    return true;
            }
        } else {
            if (oreStack instanceof ItemStack)
                return OreDictionary.itemMatches(compare, (ItemStack)oreStack, false);
            if (oreStack instanceof Item)
                return areStacksEqual(new ItemStack((Item)oreStack), compare);
            if (oreStack instanceof Block)
                return areStacksEqual(new ItemStack((Block)oreStack), compare);
        }
        return false;
    }

    public static ItemStack findFirstOreMatch(String oreName) {
        if (!OreDictionary.doesOreNameExist(oreName))
            return null;
        List<ItemStack> stacks = OreDictionary.getOres(oreName);
        if (stacks.isEmpty())
            return null;
        return stacks.get(0);
    }

    public static ItemStack resolveObject(Object object) {
        if (object instanceof String)
            return findFirstOreMatch((String)object);
        if (object instanceof ItemStack)
            return (ItemStack)object;
        if (object instanceof Item)
            return new ItemStack((Item)object);
        if (object instanceof Block)
            return new ItemStack((Block)object);
        return null;
    }

    public static boolean isEmpty(ItemStack stack) {
        return (stack == null || stack.stackSize <= 0);
    }

}