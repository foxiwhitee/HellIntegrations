package foxiwhitee.hellmod.helpers;

import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.inventory.InventoryCrafting;

@FunctionalInterface
public interface IFindMatchingRecipe {
    IHellRecipe findMatchingRecipe(InventoryCrafting matrix);
}
