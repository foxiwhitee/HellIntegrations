package foxiwhitee.hellmod.integration.botania.helpers;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.*;

import java.util.List;

public class ManaRecipeHelper {
    public static ItemStack findRecipePureDaisy(ItemStack stack) {
        if (stack == null) {
            return null;
        }

        for (RecipePureDaisy obj : BotaniaAPI.pureDaisyRecipes) {
            if (obj == null) continue;

            if (matchesPureDaisy(obj, stack)) {
                return new ItemStack(obj.getOutput());
            }
        }

        return null;
    }

    public static ItemStack findRecipePetals(IInventory inv) {
        if (inv == null) {
            return null;
        }

        for (RecipePetals recipe : BotaniaAPI.petalRecipes) {
            if (recipe.matches(inv)) {
                return recipe.getOutput().copy();
            }
        }

        return null;
    }

    public static ItemStack findRecipeRuneAltar(IInventory inv) {
        if (inv == null) {
            return null;
        }

        for (RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes) {
            if (recipe.matches(inv)) {
                return recipe.getOutput().copy();
            }
        }

        return null;
    }

    public static ItemStack findRecipeManaPool(ItemStack stack, boolean isConjuration, boolean isAlchemy) {
        if (stack == null) {
            return null;
        }

        for (RecipeManaInfusion obj : BotaniaAPI.manaInfusionRecipes) {
            if (obj == null) continue;

            if (obj.isAlchemy() == isAlchemy && obj.isConjuration() == isConjuration && obj.matches(stack)) {
                return obj.getOutput().copy();
            }
        }

        return null;
    }

    public static ItemStack findRecipeElvenTrade(List<ItemStack> stacks) {
        if (stacks.isEmpty()) {
            return null;
        }

        for (RecipeElvenTrade obj : BotaniaAPI.elvenTradeRecipes) {
            if (obj == null) continue;

            if (obj.matches(stacks, false) && obj.getInputs().size() == stacks.size()) {
                return obj.getOutput().copy();
            }
        }

        return null;
    }

    public static long getManaCostManaPool(ItemStack stack, boolean isConjuration, boolean isAlchemy) {
        if (stack == null) {
            return 0;
        }

        for (RecipeManaInfusion obj : BotaniaAPI.manaInfusionRecipes) {
            if (obj == null) continue;

            if (obj.isAlchemy() == isAlchemy && obj.isConjuration() == isConjuration && obj.matches(stack)) {
                return obj.getManaToConsume();
            }
        }

        return 0;
    }

    public static long getManaCostRuneAltar(IInventory inv) {
        if (inv == null) {
            return 0;
        }

        for (RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes) {
            if (recipe.matches(inv)) {
                return recipe.getManaUsage();
            }
        }

        return 0;
    }

    private static boolean matchesPureDaisy(RecipePureDaisy recipe, ItemStack stack) {
        Object input = recipe.getInput();

        if (input instanceof Block) {
            if (!(stack.getItem() instanceof ItemBlock)) {
                return false;
            }
            return ((ItemBlock) stack.getItem()).field_150939_a == input;
        }

        if (input instanceof String) {
            return recipe.isOreDict(stack, (String) input);
        }

        return false;
    }

}
