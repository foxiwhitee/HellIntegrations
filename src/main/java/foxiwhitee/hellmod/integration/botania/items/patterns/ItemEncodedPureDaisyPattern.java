package foxiwhitee.hellmod.integration.botania.items.patterns;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.botania.recipes.CustomRecipePureDaisy;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemEncodedPureDaisyPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedPureDaisyPattern(String name) {
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
                CustomRecipePureDaisy recipe;

                for (int var7 = 0; var7 < BotaniaAPI.pureDaisyRecipes.size(); ++var7) {
                    recipe = new CustomRecipePureDaisy(BotaniaAPI.pureDaisyRecipes.get(var7));
                    if (recipe.matches(new ArrayList<>(Arrays.asList(itemstack)))) {
                        return recipe;
                    }
                }

                return null;
            });
        } catch (Throwable var4) {
            return null;
        }
    }
}