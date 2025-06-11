package foxiwhitee.hellmod.parts;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public abstract class PartPatternTerminal extends PartTerminal {
    private final AppEngInternalInventory pattern = new AppEngInternalInventory(this, 2);

    public PartPatternTerminal(ItemStack is) {
        super(is);
    }

    public void getDrops(List<ItemStack> drops, boolean wrenched) {
        for(ItemStack is : this.getInventoryPattern()) {
            if (is != null) {
                drops.add(is);
            }
        }
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.getInventoryPattern().readFromNBT(data, "pattern");
        this.getInventoryCrafting().readFromNBT(data, "crafting");
        this.getInventoryOutput().readFromNBT(data, "output");
    }

    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        this.getInventoryPattern().writeToNBT(data, "pattern");
        this.getInventoryCrafting().writeToNBT(data, "crafting");
        this.getInventoryOutput().writeToNBT(data, "output");
    }

    public void onChangeInventory(IInventory inv, int slot, InvOperation mc, ItemStack removedStack, ItemStack newStack) {
        if (inv == getInventoryCrafting() && mc != InvOperation.markDirty) {
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
                        this.getInventoryCrafting().setInventorySlotContents(x, (item == null) ? null : item.getItemStack());
                    }
                }
            }
        }
        this.fixCraftingRecipes();
        this.getHost().markForSave();
    }

    protected void fixCraftingRecipes() {
        for (int x = 0; x < this.getInventoryCrafting().getSizeInventory(); x++) {
            ItemStack is = this.getInventoryCrafting().getStackInSlot(x);
            if (is != null)
                is.stackSize = 1;
        }
    }

    public IInventory getInventoryByName(String name) {
        return name.equals("crafting") ? this.getInventoryCrafting() : (name.equals("pattern") ? this.getInventoryPattern() : (name.equals("output") ? this.getInventoryOutput() : super.getInventoryByName(name)));
    }

    public AppEngInternalInventory getInventoryPattern() {
        return this.pattern;
    }

    abstract protected void updateRecipe();

    abstract public AppEngInternalInventory getInventoryCrafting();

    abstract public AppEngInternalInventory getInventoryOutput();
}
