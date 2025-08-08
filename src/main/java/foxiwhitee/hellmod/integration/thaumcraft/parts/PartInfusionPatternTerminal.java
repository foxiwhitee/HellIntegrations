package foxiwhitee.hellmod.integration.thaumcraft.parts;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import foxiwhitee.hellmod.integration.thaumcraft.ThaumcraftIntegration;
import foxiwhitee.hellmod.integration.thaumcraft.helpers.ThaumcraftRecipeHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.crafting.InfusionRecipe;

import java.util.ArrayList;

public class PartInfusionPatternTerminal extends PartThaumcraftPatternTerminal {
    private final AppEngInternalInventory crafting = new AppEngInternalInventory(this, 16);
    private final AppEngInternalInventory core = new AppEngInternalInventory(this, 1);
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);

    public PartInfusionPatternTerminal(ItemStack is) {
        super(is);
    }

    @Override
    protected void updateRecipe() {
        this.getInventoryOutput().setInventorySlotContents(0, (ItemStack)null);
        ItemStack central = this.getInventoryCore().getStackInSlot(0);
        if (central != null) {
            ArrayList<ItemStack> inputList = new ArrayList<>();
            for (int i = 0; i < getInventoryCrafting().getSizeInventory(); i++) {
                if (getInventoryCrafting().getStackInSlot(i) != null) {
                    inputList.add(getInventoryCrafting().getStackInSlot(i));
                }
            }
            InfusionRecipe recipe = ThaumcraftRecipeHelper.findMatchingInfusionRecipe(inputList, central);
            if (recipe != null) {
                Object recipeOut = recipe.getRecipeOutput();
                if (recipeOut instanceof ItemStack) {
                    this.getInventoryOutput().setInventorySlotContents(0, (ItemStack)recipeOut);
                }
            }
        }
    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InvOperation mc, ItemStack removedStack, ItemStack newStack) {
        if ((inv == getInventoryCrafting() || inv == getInventoryCore()) && mc != InvOperation.markDirty) {
            this.updateRecipe();
        }
        if (inv == this.getInventoryPattern() && slot == 1) {
            ItemStack is = this.getInventoryPattern().getStackInSlot(1);
            if (is != null && is.getItem() instanceof ICraftingPatternItem) {
                ICraftingPatternItem pattern = (ICraftingPatternItem)is.getItem();
                ICraftingPatternDetails details = pattern.getPatternForItem(is, this.getHost().getTile().getWorldObj());
                if (details != null) {
                    IAEItemStack item;
                    for (int x = 0; x < this.getInventoryCrafting().getSizeInventory() && x < (details.getInputs()).length; x++) {
                        item = details.getInputs()[x];
                        if (x == 0) {
                            this.getInventoryCore().setInventorySlotContents(0, (item == null) ? null : item.getItemStack());
                        } else {
                            this.getInventoryCrafting().setInventorySlotContents(x - 1, (item == null) ? null : item.getItemStack());
                        }
                    }
                    this.getInventoryOutput().setInventorySlotContents(0, details.getCondensedOutputs()[0].getItemStack());
                }
            }
        }
        this.fixCraftingRecipes();
        this.getHost().markForSave();
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.core.readFromNBT(data, "core");
    }

    @Override
    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        this.core.writeToNBT(data, "core");
    }

    @Override
    public AppEngInternalInventory getInventoryCrafting() {
        return crafting;
    }

    @Override
    public AppEngInternalInventory getInventoryOutput() {
        return output;
    }

    public AppEngInternalInventory getInventoryCore() {
        return core;
    }

    @Override
    protected GuiBridge getThisGui() {
        return ThaumcraftIntegration.getGuiBridge(1);
    }

    @Override
    public CableBusTextures getFrontBright() {
        return ThaumcraftIntegration.getBusTextureBright(1);
    }

    @Override
    public CableBusTextures getFrontColored() {
        return ThaumcraftIntegration.getBusTextureColored(1);
    }

    @Override
    public CableBusTextures getFrontDark() {
        return ThaumcraftIntegration.getBusTextureDark(1);
    }

}

