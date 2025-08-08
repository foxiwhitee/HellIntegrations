package foxiwhitee.hellmod.integration.avaritia.items;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import fox.spiteful.avaritia.crafting.CompressorManager;
import fox.spiteful.avaritia.crafting.CompressorRecipe;
import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.avaritia.recipes.CustomNeutronCompressorRecipe;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemEncodedNeutronPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedNeutronPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new UniversalPatternHelper(is, false, 1, 1, matrix -> {
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
            });
        } catch (Throwable var4) {
            return null;
        }
    }
}
