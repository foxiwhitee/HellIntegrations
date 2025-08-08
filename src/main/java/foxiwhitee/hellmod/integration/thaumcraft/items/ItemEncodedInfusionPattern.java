package foxiwhitee.hellmod.integration.thaumcraft.items;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.CustomInfusionRecipe;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.IThaumcraftRecipe;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.lib.crafting.InfusionRunicAugmentRecipe;

import java.util.ArrayList;
import java.util.List;

public class ItemEncodedInfusionPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedInfusionPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new UniversalPatternHelper(is, false, 17, 1, matrix -> {
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
                IThaumcraftRecipe recipe = null;
                for (int var7 = 0; var7 < ThaumcraftApi.getCraftingRecipes().size(); ++var7) {
                    if (ThaumcraftApi.getCraftingRecipes().get(var7) instanceof InfusionRecipe) {
                        InfusionRecipe r = (InfusionRecipe) ThaumcraftApi.getCraftingRecipes().get(var7);
                        try {
                            if (r != null && !(r instanceof InfusionRunicAugmentRecipe))
                                recipe = new CustomInfusionRecipe(r);
                        } catch (NullPointerException ignored) {}
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
