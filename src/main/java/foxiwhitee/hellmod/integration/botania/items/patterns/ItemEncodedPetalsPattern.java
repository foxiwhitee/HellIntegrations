package foxiwhitee.hellmod.integration.botania.items.patterns;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.botania.recipes.CustomRecipePetals;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;

import java.util.ArrayList;
import java.util.List;

public class ItemEncodedPetalsPattern extends ItemUniversalEncodedPattern {
    private static final ItemStack seeds = new ItemStack(Items.wheat_seeds);
    public ItemEncodedPetalsPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new UniversalPatternHelper(is, false, 18, 1, matrix -> {
                boolean minusSeeds = false;
                List<ItemStack> itemstacks = new ArrayList<>();
                ItemStack itemstack;
                for (int i = 0; i <  matrix.getSizeInventory(); i++) {
                    itemstack = matrix.getStackInSlot(i);
                    if (itemstack != null) {
                        if (itemstack.getItem() == seeds.getItem() && itemstack.getItemDamage() == seeds.getItemDamage() && !minusSeeds) {
                            minusSeeds = true;
                            continue;
                        }
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
            });
        } catch (Throwable var4) {
            return null;
        }
    }
}