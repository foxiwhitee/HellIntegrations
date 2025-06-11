package foxiwhitee.hellmod.integration.thaumcraft.recipes;

import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;

import java.util.List;

public interface IThaumcraftRecipe extends IHellRecipe {
    boolean matches(List<ItemStack> stacks, ItemStack out);

    AspectList getAspects();
}
