package foxiwhitee.hellmod.integration.thaumcraft.recipes;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomAlchemicalConstructionRecipe extends CrucibleRecipe implements IThaumcraftRecipe {
    public CustomAlchemicalConstructionRecipe(String researchKey, ItemStack result, Object cat, AspectList tags) {
        super(researchKey, result, cat, tags);
    }

    public CustomAlchemicalConstructionRecipe(CrucibleRecipe oldRecipe) {
        super(oldRecipe.key, oldRecipe.getRecipeOutput(), oldRecipe.catalyst, oldRecipe.aspects);
    }

    @Override
    public ItemStack getOut() {
        return this.getRecipeOutput();
    }

    @Override
    public List<Object> getInputs() {
        return new ArrayList<>(Arrays.asList(this.catalyst));
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        if (this.catalyst instanceof ItemStack) {
            if (catalyst.toString().equals(stacks.get(0).toString())) {
                return true;
            }
        } else if (this.catalyst instanceof ArrayList && ((ArrayList)this.catalyst).size() > 0) {
            ItemStack[] ores = (ItemStack[])((ArrayList)this.catalyst).toArray(new ItemStack[0]);
            if (ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{stacks.get(0)}, ores)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matches(List<ItemStack> stacks, ItemStack out) {
        if (this.catalyst instanceof ItemStack) {
            if (catalyst.toString().equals(stacks.get(0).toString()) && getRecipeOutput().toString().equals(out.toString())) {
                return true;
            }
        } else if (this.catalyst instanceof ArrayList && ((ArrayList)this.catalyst).size() > 0) {
            ItemStack[] ores = (ItemStack[])((ArrayList)this.catalyst).toArray(new ItemStack[0]);
            if (ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{stacks.get(0)}, ores) && getRecipeOutput().toString().equals(out.toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AspectList getAspects() {
        return this.aspects;
    }
}
