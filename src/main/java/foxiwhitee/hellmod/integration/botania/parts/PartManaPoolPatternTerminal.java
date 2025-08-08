package foxiwhitee.hellmod.integration.botania.parts;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.helpers.ManaRecipeHelper;
import foxiwhitee.hellmod.integration.botania.items.ae.ItemManaDrop;
import foxiwhitee.hellmod.parts.PartTerminal;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.common.block.ModBlocks;

public class PartManaPoolPatternTerminal extends PartBotaniaPatternTerminal {
    private final AppEngInternalInventory crafting = new AppEngInternalInventory(this, 2);
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 2);
    private boolean isAlchemy;
    private boolean isConjuration;

    public PartManaPoolPatternTerminal(ItemStack is) {
        super(is);
    }

    @Override
    protected GuiBridge getThisGui() {
        return BotaniaIntegration.getGuiBridge(0);
    }

    @Override
    public CableBusTextures getFrontBright() {
        return BotaniaIntegration.getBusTextureBright(0);
    }

    @Override
    public CableBusTextures getFrontColored() {
        return BotaniaIntegration.getBusTextureColored(0);
    }

    @Override
    public CableBusTextures getFrontDark() {
        return BotaniaIntegration.getBusTextureDark(0);
    }

    @Override
    public AppEngInternalInventory getInventoryCrafting() {
        return this.crafting;
    }

    @Override
    public AppEngInternalInventory getInventoryOutput() {
        return output;
    }

    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setBoolean("isAlchemy", this.isAlchemy);
        data.setBoolean("isConjuration", this.isConjuration);
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.isAlchemy = data.getBoolean("isAlchemy");
        this.isConjuration = data.getBoolean("isConjuration");
    }

    private void updateManaPoolCatalyst() {
        ItemStack stack = this.getInventoryCrafting().getStackInSlot(1);
        if (stack != null) {
            Item item = stack.getItem();
            this.isAlchemy = item == Item.getItemFromBlock(ModBlocks.alchemyCatalyst);
            this.isConjuration = item == Item.getItemFromBlock(ModBlocks.conjurationCatalyst);
        }
    }

    public void onChangeInventory(IInventory inv, int slot, InvOperation mc, ItemStack removedStack, ItemStack newStack) {
        if (inv == this.getInventoryCrafting() && slot == 1 && mc != InvOperation.markDirty) {
            this.updateManaPoolCatalyst();
        }

        if (inv == getInventoryCrafting() && mc != InvOperation.markDirty) {
            this.updateRecipe();
        }
        if (inv == this.getInventoryPattern() && slot == 1) {
            boolean minusMana = false;
            ItemStack is = this.getInventoryPattern().getStackInSlot(1);
            if (is != null && is.getItem() instanceof ICraftingPatternItem) {
                ICraftingPatternItem pattern = (ICraftingPatternItem)is.getItem();
                ICraftingPatternDetails details = pattern.getPatternForItem(is, this.getHost().getTile().getWorldObj());
                if (details != null) {
                    IAEItemStack item;
                    for (int x = 0; x < this.getInventoryCrafting().getSizeInventory() && x < (details.getInputs()).length; x++) {
                        item = details.getInputs()[x];
                        if (item != null) {
                            if (item.getItemStack().getItem() instanceof ItemManaDrop && !minusMana) {
                                minusMana = true;
                                continue;
                            }
                        }
                        this.getInventoryCrafting().setInventorySlotContents(0, (item == null) ? null : item.getItemStack());
                    }
                }
            }
        }
        this.fixCraftingRecipes();
        this.getHost().markForSave();
    }

    @Override
    protected void updateRecipe() {
        ItemStack input = this.getInventoryCrafting().getStackInSlot(0);

        if (input != null) {
            ItemStack singleItem = input.copy();
            singleItem.stackSize = 1;

            ItemStack result = ManaRecipeHelper.findRecipeManaPool(singleItem, isConjuration, isAlchemy);

            this.getInventoryOutput().setInventorySlotContents(0, result);
        } else {
            this.getInventoryOutput().setInventorySlotContents(0, null);
        }
    }

    public boolean isAlchemy() {
        return isAlchemy;
    }

    public boolean isConjuration() {
        return isConjuration;
    }
}

