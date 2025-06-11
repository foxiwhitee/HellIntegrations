package foxiwhitee.hellmod.integration.avaritia.parts;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;

import java.util.List;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

public class PartBigPatternTerminal extends PartAvaritiaPatternTerminal {
    private final AppEngInternalInventory crafting = new AppEngInternalInventory(this, 81);

    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 3);

    private boolean craftingMode = true;

    private boolean substitute = false;

    public PartBigPatternTerminal(ItemStack is) {
        super(is);
    }

    @Override
    protected void updateRecipe() {
        if (craftingMode) {
            InventoryCrafting matrix = new InventoryCrafting(null, 9, 9);
            for (int x = 0; x < matrix.getSizeInventory(); x++)
                matrix.setInventorySlotContents(x, this.crafting.getStackInSlot(x));
            int i = 0;
            ItemStack itemstack = null;
            ItemStack itemstack1 = null;
            int j;
            for (j = 0; j < matrix.getSizeInventory(); j++) {
                ItemStack itemstack2 = matrix.getStackInSlot(j);
                if (itemstack2 != null) {
                    if (i == 0)
                        itemstack = itemstack2;
                    if (i == 1)
                        itemstack1 = itemstack2;
                    i++;
                }
            }
            if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && itemstack.getItem().isRepairable()) {
                getInventoryOutput().setInventorySlotContents(0, null);
                return;
            }
            for (j = 0; j < ExtremeCraftingManager.getInstance().getRecipeList().size(); j++) {
                IRecipe irecipe = (IRecipe) ExtremeCraftingManager.getInstance().getRecipeList().get(j);
                if (irecipe.matches(matrix, null)) {
                    getInventoryOutput().setInventorySlotContents(0, irecipe.getRecipeOutput().copy());
                    return;
                }
            }
            getInventoryOutput().setInventorySlotContents(0, null);
        }
    }

    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack((Item) AvaritiaIntegration.ITEM_PARTS_TERMINALS);
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        setCraftingRecipe(data.getBoolean("craftingMode"));
        setSubstitution(data.getBoolean("substitute"));
    }

    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setBoolean("craftingMode", this.craftingMode);
        data.setBoolean("substitute", this.substitute);
    }

    public void onChangeInventory(IInventory inv, int slot, InvOperation mc, ItemStack removedStack, ItemStack newStack) {
        if (inv == this.getInventoryPattern() && slot == 1) {
            ItemStack is = this.getInventoryPattern().getStackInSlot(1);
            if (is != null && is.getItem() instanceof ICraftingPatternItem) {
                ICraftingPatternItem pattern = (ICraftingPatternItem)is.getItem();
                ICraftingPatternDetails details = pattern.getPatternForItem(is, getHost().getTile().getWorldObj());
                if (details != null) {
                    setCraftingRecipe(details.isCraftable());
                    setSubstitution(details.canSubstitute());
                    int x;
                    for (x = 0; x < this.crafting.getSizeInventory() && x < (details.getInputs()).length; x++) {
                        IAEItemStack item = details.getInputs()[x];
                        this.crafting.setInventorySlotContents(x, (item == null) ? null : item.getItemStack());
                    }
                    for (x = 0; x < this.output.getSizeInventory() && x < (details.getOutputs()).length; x++) {
                        IAEItemStack item = details.getOutputs()[x];
                        this.output.setInventorySlotContents(x, (item == null) ? null : item.getItemStack());
                    }
                }
            }
        } else if (inv == this.crafting) {
            fixCraftingRecipes();
        }
        getHost().markForSave();
    }

    protected void fixCraftingRecipes() {
        for (int x = 0; x < this.crafting.getSizeInventory(); x++) {
            ItemStack is = this.crafting.getStackInSlot(x);
            if (is != null)
                is.stackSize = 1;
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

    public void setSubstitution(boolean canSubstitute) {
        this.substitute = canSubstitute;
    }

    public void setCraftingRecipe(boolean craftingMode) {
        this.craftingMode = craftingMode;
        fixCraftingRecipes();
    }

    public boolean isCraftingRecipe() {
        return this.craftingMode;
    }

    public boolean isSubstitution() {
        return this.substitute;
    }

    public CableBusTextures getFrontBright() {
        return AvaritiaIntegration.getBusTextureBright(0);
    }

    public CableBusTextures getFrontColored() {
        return AvaritiaIntegration.getBusTextureColored(0);
    }

    public CableBusTextures getFrontDark() {
        return AvaritiaIntegration.getBusTextureDark(0);
    }

    protected GuiBridge getThisGui() {
        return AvaritiaIntegration.getGuiBridge(0);
    }
}
