package foxiwhitee.hellmod.integration.thaumcraft.helpers;

import foxiwhitee.hellmod.integration.thaumcraft.recipes.CustomAlchemicalConstructionRecipe;
import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ThaumcraftRecipeHelper {

    public static List findMatchingCrucibleRecipe(ItemStack catalyst) {
        List r = new ArrayList<>();
        for (int var7 = 0; var7 < ThaumcraftApi.getCraftingRecipes().size(); ++var7) {
            if (ThaumcraftApi.getCraftingRecipes().get(var7) instanceof CrucibleRecipe) {
                if (catalystMatches((CrucibleRecipe) ThaumcraftApi.getCraftingRecipes().get(var7), catalyst)) {
                    r.add(ThaumcraftApi.getCraftingRecipes().get(var7));
                }
            }
        }
        return r;
    }

    protected static boolean catalystMatches(CrucibleRecipe recipe, ItemStack cat) {
        if (recipe.catalyst instanceof ItemStack && ThaumcraftApiHelper.itemMatches((ItemStack)recipe.catalyst, cat, false)) {
            return true;
        } else {
            if (recipe.catalyst instanceof ArrayList && !((ArrayList<?>) recipe.catalyst).isEmpty()) {
                for (Object o : (ArrayList)recipe.catalyst) {
                    if (OreDictUtil.areStacksEqual(o, cat)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static InfusionRecipe findMatchingInfusionRecipe(ArrayList items, ItemStack input) {
        return (InfusionRecipe)ThaumcraftApi.getCraftingRecipes().stream().filter((i) -> i instanceof InfusionRecipe).filter((i) -> {
            return matchesInfusion((InfusionRecipe)i, items, input);
        }).findFirst().orElse((Object)null);
    }

    private static boolean matchesInfusion(InfusionRecipe recipe, ArrayList input, ItemStack central) {
        ItemStack i2 = central.copy();
        if (recipe.getRecipeInput() == null) {
            return false;
        } else {
            if (recipe.getRecipeInput().getItemDamage() == 32767) {
                i2.setItemDamage(32767);
            }

            if (!areItemStacksEqual(i2, recipe.getRecipeInput(), true)) {
                return false;
            } else {
                ArrayList<ItemStack> ii = new ArrayList();

                for (Object o : input) {
                    ItemStack is = (ItemStack) o;
                    ii.add(is.copy());
                }

                ItemStack[] var11 = recipe.getComponents();
                int var12 = var11.length;

                for (ItemStack comp : var11) {
                    boolean b = false;

                    for (int a = 0; a < ii.size(); ++a) {
                        i2 = ((ItemStack) ii.get(a)).copy();
                        if (comp.getItemDamage() == 32767) {
                            i2.setItemDamage(32767);
                        }

                        if (areItemStacksEqual(i2, comp, true)) {
                            ii.remove(a);
                            b = true;
                            break;
                        }
                    }

                    if (!b) {
                        return false;
                    }
                }

                return ii.isEmpty();
            }
        }
    }

    private static boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1, boolean fuzzy) {
        if (stack0 == null && stack1 != null) {
            return false;
        } else if (stack0 != null && stack1 == null) {
            return false;
        } else if (stack0 == null && stack1 == null) {
            return true;
        } else {
            boolean t1 = ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(stack0, stack1);
            if (!t1) {
                return false;
            } else {
                if (fuzzy) {
                    int od = OreDictionary.getOreID(stack0);
                    if (od != -1) {
                        ItemStack[] ores = (ItemStack[])OreDictionary.getOres(od).toArray(new ItemStack[0]);
                        if (ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{stack1}, ores)) {
                            return true;
                        }
                    }
                }

                boolean damage = stack0.getItemDamage() == stack1.getItemDamage() || stack1.getItemDamage() == 32767;
                return stack0.getItem() == stack1.getItem() && damage && stack0.stackSize <= stack0.getMaxStackSize();
            }
        }
    }

}
