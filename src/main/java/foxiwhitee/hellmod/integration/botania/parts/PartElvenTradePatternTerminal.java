package foxiwhitee.hellmod.integration.botania.parts;

import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.tile.inventory.AppEngInternalInventory;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.helpers.ManaRecipeHelper;
import foxiwhitee.hellmod.parts.PartTerminal;
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

}

