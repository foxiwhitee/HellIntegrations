package foxiwhitee.hellmod.integration.botania.parts;

import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.helpers.ManaRecipeHelper;
import foxiwhitee.hellmod.parts.PartTerminal;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class PartPureDaisyPatternTerminal extends PartBotaniaPatternTerminal {
    private final AppEngInternalInventory crafting = new AppEngInternalInventory(this, 1);
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);

    public PartPureDaisyPatternTerminal(ItemStack is) {
        super(is);
    }

    @Override
    protected GuiBridge getThisGui() {
        return BotaniaIntegration.getGuiBridge(4);
    }

    @Override
    public CableBusTextures getFrontBright() {
        return BotaniaIntegration.getBusTextureBright(4);
    }

    @Override
    public CableBusTextures getFrontColored() {
        return BotaniaIntegration.getBusTextureColored(4);
    }

    @Override
    public CableBusTextures getFrontDark() {
        return BotaniaIntegration.getBusTextureDark(4);
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
        ItemStack input = this.getInventoryCrafting().getStackInSlot(0);

        if (input != null) {
            ItemStack singleItem = input.copy();
            singleItem.stackSize = 1;

            ItemStack result = ManaRecipeHelper.findRecipePureDaisy(singleItem);

            this.getInventoryOutput().setInventorySlotContents(0, result);
        } else {
            this.getInventoryOutput().setInventorySlotContents(0, null);
        }
    }

}

