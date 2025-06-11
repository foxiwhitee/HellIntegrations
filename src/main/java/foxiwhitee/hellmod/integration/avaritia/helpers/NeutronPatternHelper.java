package foxiwhitee.hellmod.integration.avaritia.helpers;

import fox.spiteful.avaritia.crafting.CompressorManager;
import fox.spiteful.avaritia.crafting.CompressorRecipe;
import foxiwhitee.hellmod.helpers.UniversalLongPatternHelper;
import foxiwhitee.hellmod.integration.avaritia.recipes.CustomNeutronCompressorRecipe;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;

public class NeutronPatternHelper extends UniversalLongPatternHelper {
    public NeutronPatternHelper(ItemStack is, World w) {
        super(is, w);
    }

    public static IHellRecipe findMatchingRecipe(InventoryCrafting matrix) {
        ItemStack itemstack = matrix.getStackInSlot(0);

        if (itemstack == null) {
            return null;
        }
        CustomNeutronCompressorRecipe recipe;

        for (int var7 = 0; var7 < CompressorManager.getRecipes().size(); ++var7) {
            CompressorRecipe r = CompressorManager.getRecipes().get(var7);
            if (r.getIngredient() instanceof ItemStack) {
                recipe = new CustomNeutronCompressorRecipe(r);
                if (recipe.matches(new ArrayList<>(Arrays.asList(itemstack)))) {
                    return recipe;
                }
            }
        }

        return null;
    }

    public static int findCountInputCraft(ItemStack stack) {
        CustomNeutronCompressorRecipe recipe;

        for (int var7 = 0; var7 < CompressorManager.getRecipes().size(); ++var7) {
            CompressorRecipe r = CompressorManager.getRecipes().get(var7);
            if (r.getIngredient() instanceof ItemStack) {
                recipe = new CustomNeutronCompressorRecipe(r);
                if (recipe.matches(new ArrayList<>(Arrays.asList(stack)))) {
                    return recipe.getCost();
                }
            }
        }

        return 0;
    }

    @Override
    protected int getWidthInventory() {
        return 1;
    }

    @Override
    protected int getHeightInventory() {
        return 1;
    }
}
