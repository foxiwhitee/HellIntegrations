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
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PartElvenTradePatternTerminal extends PartBotaniaPatternTerminal {
    private final AppEngInternalInventory crafting = new AppEngInternalInventory(this, 3);
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);

    public PartElvenTradePatternTerminal(ItemStack is) {
        super(is);
    }

    @Override
    protected GuiBridge getThisGui() {
        return BotaniaIntegration.getGuiBridge(2);
    }

    @Override
    public CableBusTextures getFrontBright() {
        return BotaniaIntegration.getBusTextureBright(2);
    }

    @Override
    public CableBusTextures getFrontColored() {
        return BotaniaIntegration.getBusTextureColored(2);
    }

    @Override
    public CableBusTextures getFrontDark() {
        return BotaniaIntegration.getBusTextureDark(2);
    }

    @Override
    public AppEngInternalInventory getInventoryCrafting() {
        return this.crafting;
    }

    @Override
    public AppEngInternalInventory getInventoryOutput() {
        return output;
    }

    protected void updateRecipe() {
        List<ItemStack> inputs = new ArrayList<>();
        ItemStack stack;
        for (int i = 0; i < getInventoryCrafting().getSizeInventory(); i++) {
            stack = getInventoryCrafting().getStackInSlot(i);
            if (stack != null) {
                stack.stackSize = 1;
                inputs.add(stack);
            }
        }

        if (!inputs.isEmpty()) {
            ItemStack result = ManaRecipeHelper.findRecipeElvenTrade(inputs);

            this.getInventoryOutput().setInventorySlotContents(0, result);
        } else {
            this.getInventoryOutput().setInventorySlotContents(0, null);
        }
    }

    public void onChangeInventory(IInventory inv, int slot, InvOperation mc, ItemStack removedStack, ItemStack newStack) {
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
                        this.getInventoryCrafting().setInventorySlotContents(x - (minusMana ? 1 : 0), (item == null) ? null : item.getItemStack());
                    }
                }
            }
        }
        this.fixCraftingRecipes();
        this.getHost().markForSave();
    }

}

