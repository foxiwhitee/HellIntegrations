package foxiwhitee.hellmod.integration.botania.items.patterns;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.items.ae.ItemManaDrop;
import foxiwhitee.hellmod.integration.botania.recipes.CustomRecipeRuneAltar;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class ItemEncodedRuneAltarPattern extends ItemUniversalEncodedPattern {
    private static final ItemStack rock = new ItemStack(ModBlocks.livingrock);

    public ItemEncodedRuneAltarPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new UniversalPatternHelper(is, false, 19, 1, matrix -> {
                boolean minusMana = false, minusLivingRock = false;
                List<ItemStack> itemstacks = new ArrayList<>();
                ItemStack itemstack;
                for (int i = 0; i <  matrix.getSizeInventory(); i++) {
                    itemstack = matrix.getStackInSlot(i);
                    if (itemstack != null) {
                        if (itemstack.getItem() instanceof ItemManaDrop && !minusMana) {
                            minusMana = true;
                            continue;
                        } else if (itemstack.getItem() == rock.getItem() && itemstack.getItemDamage() == rock.getItemDamage() && !minusLivingRock) {
                            minusLivingRock = true;
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
                CustomRecipeRuneAltar recipe;

                for (int var7 = 0; var7 < BotaniaAPI.runeAltarRecipes.size(); ++var7) {
                    RecipeRuneAltar r = BotaniaAPI.runeAltarRecipes.get(var7);
                    if (r != null) {
                        recipe = new CustomRecipeRuneAltar(r);
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
