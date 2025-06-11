package foxiwhitee.hellmod.integration.botania.helpers;

import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.botania.recipes.CustomRecipePureDaisy;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.Arrays;

public class PureDaisyPatternHelper extends UniversalPatternHelper {
    public PureDaisyPatternHelper(ItemStack is, World w) {
        super(is, w);
    }

    public static IHellRecipe findMatchingRecipe(InventoryCrafting matrix) {
        ItemStack itemstack = matrix.getStackInSlot(0);

        if (itemstack == null) {
            return null;
        }
        CustomRecipePureDaisy recipe;

        for (int var7 = 0; var7 < BotaniaAPI.pureDaisyRecipes.size(); ++var7) {
            recipe = new CustomRecipePureDaisy(BotaniaAPI.pureDaisyRecipes.get(var7));
            if (recipe.matches(new ArrayList<>(Arrays.asList(itemstack)))) {
                return recipe;
            }
        }

        return null;
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
