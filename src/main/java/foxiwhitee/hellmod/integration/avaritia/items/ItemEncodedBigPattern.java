package foxiwhitee.hellmod.integration.avaritia.items;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.items.misc.ItemEncodedPattern;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapelessRecipe;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.avaritia.recipes.CustomExtremeShapedOreRecipe;
import foxiwhitee.hellmod.integration.avaritia.recipes.CustomExtremeShapedRecipe;
import foxiwhitee.hellmod.integration.avaritia.recipes.CustomExtremeShapelessRecipe;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ItemEncodedBigPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedBigPattern(String name) {
        super(name);
    }

    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new UniversalPatternHelper(is, true, 9, 9, matrix -> {
                int i = 0;
                ItemStack itemstack = null;
                ItemStack itemstack1 = null;
                int j;
                for (j = 0; j < matrix.getSizeInventory(); j++) {
                    ItemStack itemstack2 = matrix.getStackInSlot(j);
                    if (itemstack2 != null) {
                        if (i == 0)
                            itemstack = itemstack2;
                        if (i == 1)
                            itemstack1 = itemstack2;
                        i++;
                    }
                }
                if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && itemstack.getItem()
                        .isRepairable())
                    return null;
                for (j = 0; j < ExtremeCraftingManager.getInstance().getRecipeList().size(); j++) {
                    IRecipe irecipe = (IRecipe) ExtremeCraftingManager.getInstance().getRecipeList().get(j);
                    if (irecipe.matches(matrix, w))
                        if (irecipe instanceof ExtremeShapelessRecipe) {
                            return new CustomExtremeShapelessRecipe((ExtremeShapelessRecipe) irecipe);
                        } else if (irecipe instanceof ExtremeShapedRecipe) {
                            return new CustomExtremeShapedRecipe((ExtremeShapedRecipe) irecipe);
                        } else if (irecipe instanceof ExtremeShapedOreRecipe) {
                            return new CustomExtremeShapedOreRecipe((ExtremeShapedOreRecipe) irecipe);
                        }
                }
                return null;
            });
        } catch (Throwable e) {
            return null;
        }
    }
}
