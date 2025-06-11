package foxiwhitee.hellmod.integration.botania.helpers;

import foxiwhitee.hellmod.helpers.UniversalMultiTypePatternHelper;
import foxiwhitee.hellmod.integration.botania.recipes.CustomRecipeManaInfusion;
import foxiwhitee.hellmod.recipes.*;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;

public class ManaPoolPatternHelper extends UniversalMultiTypePatternHelper {

    public ManaPoolPatternHelper(ItemStack is, World w) {
        super(is, w);
    }

    public static IHellRecipe findMatchingRecipe(InventoryCrafting matrix, String type) {
        ItemStack itemstack = matrix.getStackInSlot(0);

        if (itemstack == null) {
            return null;
        }

        CustomRecipeManaInfusion recipe;

        for (int i = 0; i < BotaniaAPI.manaInfusionRecipes.size(); i++) {
            recipe = new CustomRecipeManaInfusion(BotaniaAPI.manaInfusionRecipes.get(i));
            if (type.equals("ManaPool")) {
                if (recipe.isAlchemy() || recipe.isConjuration()) {
                    continue;
                } else {
                    if (recipe.matches(itemstack)) {
                        return recipe;
                    }
                }
            } else if (type.equals("ManaPool.Alchemy")) {
                if (!recipe.isAlchemy() || recipe.isConjuration()) {
                    continue;
                } else {
                    if (recipe.matches(itemstack)) {
                        return recipe;
                    }
                }
            } else if (type.equals("ManaPool.Conjuration")) {
                if (recipe.isAlchemy() || !recipe.isConjuration()) {
                    continue;
                } else {
                    if (recipe.matches(itemstack)) {
                        return recipe;
                    }
                }
            } else {
                continue;
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

