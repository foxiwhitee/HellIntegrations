package foxiwhitee.hellmod.integration.botania.helpers;

import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.botania.recipes.CustomRecipeElvenTrade;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.List;

public class ElvenTradePatternHelper extends UniversalPatternHelper {
    public ElvenTradePatternHelper(ItemStack is, World w) {
        super(is, w);
    }

    public static IHellRecipe findMatchingRecipe(InventoryCrafting matrix) {
        List<ItemStack> itemstacks = new ArrayList<>();
        ItemStack itemstack;
        for (int i = 0; i <  matrix.getSizeInventory(); i++) {
            itemstack = matrix.getStackInSlot(i);
            if (itemstack != null) {
                itemstacks.add(itemstack.copy());
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
        CustomRecipeElvenTrade recipe;

        for (int var7 = 0; var7 < BotaniaAPI.elvenTradeRecipes.size(); ++var7) {
            recipe = new CustomRecipeElvenTrade(BotaniaAPI.elvenTradeRecipes.get(var7));
            if (recipe.matches(itemstacks)) {
                return recipe;
            }
        }

        return null;
    }

    @Override
    protected int getWidthInventory() {
        return 3;
    }

    @Override
    protected int getHeightInventory() {
        return 1;
    }
}
