package foxiwhitee.hellmod.integration.botania.helpers;

import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.botania.recipes.CustomRecipePetals;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;

import java.util.ArrayList;
import java.util.List;

public class PetalsPatternHelper extends UniversalPatternHelper {
    public PetalsPatternHelper(ItemStack is, World w) {
        super(is, w);
    }

    public static IHellRecipe findMatchingRecipe(InventoryCrafting matrix) {
        List<ItemStack> itemstacks = new ArrayList<>();
        ItemStack itemstack;
        for (int i = 0; i <  matrix.getSizeInventory(); i++) {
            itemstack = matrix.getStackInSlot(i);
            if (itemstack != null) {
                itemstacks.add(itemstack);
            }
        }
        if (itemstacks.isEmpty()) {
            return null;
        }
        for (ItemStack stack : itemstacks) {
            if (stack == null) {
                return null;
            }
        }
        CustomRecipePetals recipe;

        for (int var7 = 0; var7 < BotaniaAPI.petalRecipes.size(); ++var7) {
            RecipePetals r = BotaniaAPI.petalRecipes.get(var7);
            if (r != null) {
                recipe = new CustomRecipePetals(r);
                if (recipe.matches(itemstacks)) {
                    return recipe;
                }
            }
        }

        return null;
    }

    @Override
    protected int getWidthInventory() {
        return 17;
    }

    @Override
    protected int getHeightInventory() {
        return 1;
    }
}
