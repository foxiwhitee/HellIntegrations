package foxiwhitee.hellmod.integration.avaritia.parts;

import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.tile.inventory.AppEngInternalInventory;
import fox.spiteful.avaritia.crafting.CompressorManager;
import fox.spiteful.avaritia.crafting.CompressorRecipe;
import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import foxiwhitee.hellmod.integration.avaritia.recipes.CustomNeutronCompressorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartNeutronCompressorPatternTerminal extends PartAvaritiaPatternTerminal {
    private final AppEngInternalInventory crafting = new AppEngInternalInventory(this, 1);
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);

    public PartNeutronCompressorPatternTerminal(ItemStack is) {
        super(is);
    }

    @Override
    protected void updateRecipe() {
        ItemStack stack = getInventoryCrafting().getStackInSlot(0);
        if (stack != null) {
            for (int i = 0; i < CompressorManager.getRecipes().size(); ++i) {
                CompressorRecipe r = CompressorManager.getRecipes().get(i);
                if (r != null) {
                    if (matches(stack, r.getIngredient())) {
                        getInventoryOutput().setInventorySlotContents(0, r.getOutput().copy());
                        return;
                    }
                }
            }
            getInventoryOutput().setInventorySlotContents(0, null);
        } else {
            getInventoryOutput().setInventorySlotContents(0, null);
        }
    }

    @Override
    public AppEngInternalInventory getInventoryCrafting() {
        return crafting;
    }

    @Override
    public AppEngInternalInventory getInventoryOutput() {
        return output;
    }

    @Override
    protected GuiBridge getThisGui() {
        return AvaritiaIntegration.getGuiBridge(2);
    }

    @Override
    public CableBusTextures getFrontBright() {
        return AvaritiaIntegration.getBusTextureBright(2);
    }

    @Override
    public CableBusTextures getFrontColored() {
        return AvaritiaIntegration.getBusTextureColored(2);
    }

    @Override
    public CableBusTextures getFrontDark() {
        return AvaritiaIntegration.getBusTextureDark(2);
    }

    private boolean matches(ItemStack in, Object o) {
        ItemStack stack;
        if (o instanceof ItemStack) {
            stack = (ItemStack) o;
            stack = stack.copy();
            stack.stackSize = 1;
            return simpleAreStacksEqual(stack, in);
        } else if (o instanceof ArrayList) {
            ArrayList<?> al = (ArrayList<?>) o;
            if (!al.isEmpty()) {
                stack = (ItemStack) al.get(0);
                stack = stack.copy();
                stack.stackSize = 1;
                return simpleAreStacksEqual(stack, in);
            } else {
                return false;
            }
        } else if (o instanceof String) {
            List<ItemStack> al = OreDictionary.getOres((String)o);
            if (!al.isEmpty()) {
                stack = (ItemStack) al.get(0);
                stack = stack.copy();
                stack.stackSize = 1;
                return simpleAreStacksEqual(stack, in);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }
}

