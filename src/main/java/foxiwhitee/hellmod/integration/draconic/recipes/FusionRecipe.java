package foxiwhitee.hellmod.integration.draconic.recipes;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import appeng.util.Platform;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingInjector;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingInventory;
import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FusionRecipe implements IDraconicRecipe {
    protected ItemStack result;

    protected ItemStack catalyst;

    protected List<Object> ingredients;

    protected long energyCost;

    protected int craftingTier;

    public FusionRecipe(ItemStack result, ItemStack catalyst, long energyCost, int craftingTier, Object[] ingredients) {
        this.result = result;
        this.catalyst = catalyst;
        this.ingredients = new LinkedList();
        Collections.addAll(this.ingredients, ingredients);
        this.energyCost = energyCost;
        this.craftingTier = craftingTier;
        long maxCost = Long.MAX_VALUE / ingredients.length;
        if (energyCost > maxCost) {
            String r = "Result: " + result + "\nCatalyst: " + catalyst + "\nTier: " + craftingTier + "\nEnergy: (per ingredient) " + energyCost + ", (total) " + BigInteger.valueOf(energyCost).multiply(BigInteger.valueOf(ingredients.length));
            for (Object i : ingredients)
                r = r + "\n" + i;
            System.out.println("An error occurred while registering the following recipe. \n" + r);
            throw new RuntimeException("Invalid Recipe: The combined energy cost of your recipe exceeds Long.MAX_VALUE (9223372036854775807) WTF are you doing?");
        }
    }

    public ItemStack getOut() {
        return this.result;
    }

    public boolean isRecipeCatalyst(ItemStack catalyst) {
        return (catalyst != null && this.catalyst.isItemEqual(catalyst));
    }

    public ItemStack getRecipeCatalyst() {
        return this.catalyst;
    }

    public int getRecipeTier() {
        return this.craftingTier;
    }

    public List<Object> getInputs() {
        return this.ingredients;
    }

    public boolean matches(IFusionCraftingInventory inventory, World world, int x, int y, int z) {
        List<IFusionCraftingInjector> pedestals = new ArrayList<>();
        pedestals.addAll(inventory.getInjectors());
        if (OreDictUtil.isEmpty(inventory.getStackInCore(0)) || !itemMatches(inventory.getStackInCore(0), this.catalyst, false) || (inventory.getStackInCore(0)).stackSize < this.catalyst.stackSize)
            return false;
        if (this.catalyst.hasTagCompound() && !areItemStackTagsEqualForCrafting(this.catalyst, inventory.getStackInCore(0)))
            return false;
        for (Object ingredient : this.ingredients) {
            boolean foundIngredient = false;
            for (IFusionCraftingInjector pedestal : pedestals) {
                ItemStack pStack = pedestal.getStackInPedestal();
                if (!OreDictUtil.isEmpty(pStack) && itemMatches((ItemStack)ingredient, pStack, false) && areItemStackTagsEqualForCrafting(pStack, (ItemStack)ingredient)) {
                    ItemStack i = OreDictUtil.resolveObject(ingredient);
                    if (i.hasTagCompound() && !areItemStackTagsEqualForCrafting(pedestal.getStackInPedestal(), i))
                        continue;
                    foundIngredient = true;
                    pedestals.remove(pedestal);
                    break;
                }
            }
            if (!foundIngredient)
                return false;
        }
        for (IFusionCraftingInjector pedestal : pedestals) {
            if (!OreDictUtil.isEmpty(pedestal.getStackInPedestal()))
                return false;
        }
        return true;
    }

    public static boolean areItemStackTagsEqualForCrafting(ItemStack slotItem, ItemStack recipeItem) {
        if (recipeItem == null || slotItem == null)
            return false;
        if (recipeItem.stackTagCompound != null && slotItem.stackTagCompound == null)
            return false;
        if (recipeItem.stackTagCompound == null)
            return true;
        Iterator<String> iterator = recipeItem.stackTagCompound.func_150296_c().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (slotItem.stackTagCompound.hasKey(s)) {
                if (!slotItem.stackTagCompound.getTag(s).toString().equals(recipeItem.stackTagCompound
                        .getTag(s).toString()))
                    return false;
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isIngredient(List<Object> list, ItemStack itemStack) {
        if (itemStack == null)
            return false;
        for (Object next : list) {
            if (next instanceof ItemStack &&
                    itemMatches((ItemStack)next, itemStack, false) && areItemStackTagsEqualForCrafting(itemStack, (ItemStack)next))
                return true;
        }
        return false;
    }

    public static boolean itemMatches(ItemStack target, ItemStack input, boolean strict) {
        if ((input == null && target != null) || (input != null && target == null))
            return false;
        return (target.getItem() == input.getItem() && ((target
                .getItemDamage() == 32767 && !strict) || target.getItemDamage() == input.getItemDamage()));
    }

    public void craft(IFusionCraftingInventory inventory, World world, int x, int y, int z) {
        if (!matches(inventory, world, x, y, z))
            return;
        List<IFusionCraftingInjector> pedestals = new ArrayList<>(inventory.getInjectors());
        Iterator<IFusionCraftingInjector> iterator = pedestals.iterator();
        for (Object ingredient : this.ingredients) {
            while (iterator.hasNext()) {
                IFusionCraftingInjector pedestal = iterator.next();
                ItemStack pStack = pedestal.getStackInPedestal();
                if (!OreDictUtil.isEmpty(pStack) && itemMatches((ItemStack)ingredient, pStack, false) &&
                        areItemStackTagsEqualForCrafting(pStack, (ItemStack)ingredient) &&
                        pedestal.getPedestalTier() >= this.craftingTier) {
                    ItemStack stack = pedestal.getStackInPedestal();
                    stack.stackSize--;
                    if (stack.stackSize <= 0) {
                        stack = null;
                    }
                    pedestal.setStackInPedestal(stack);
                    iterator.remove();
                }
            }
            iterator = pedestals.iterator();
        }
        ItemStack catalyst = inventory.getStackInCore(0);
        ItemStack result = getOut();
        catalyst.stackSize -= this.catalyst.stackSize;
        if (catalyst.stackSize <= 0)
            catalyst = null;
        inventory.setStackInCore(0, catalyst);
        inventory.setStackInCore(1, result.copy());
    }

    public long getIngredientEnergyCost() {
        return this.energyCost;
    }

    public void onCraftingTick(IFusionCraftingInventory inventory, World world, int x, int y, int z) {}

    public String canCraft(IFusionCraftingInventory inventory, World world, int x, int y, int z) {
        if (!OreDictUtil.isEmpty(inventory.getStackInCore(1)))
            return "outputObstructed";
        List<IFusionCraftingInjector> pedestals = new ArrayList<>();
        pedestals.addAll(inventory.getInjectors());
        for (IFusionCraftingInjector pedestal : pedestals) {
            if (!OreDictUtil.isEmpty(pedestal.getStackInPedestal()) && pedestal.getPedestalTier() < this.craftingTier)
                return "tierLow";
        }
        return "true";
    }

    public String toString() {
        return String.format("FusionRecipe: {Result: %s, Catalyst: %s}", new Object[] { this.result, this.catalyst });
    }

    @Override
    public boolean matches(List<ItemStack> stacks) {
        return false;
    }
}
