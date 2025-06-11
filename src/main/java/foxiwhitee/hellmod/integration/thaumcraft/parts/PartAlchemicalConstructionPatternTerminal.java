package foxiwhitee.hellmod.integration.thaumcraft.parts;

import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.tile.inventory.AppEngInternalInventory;
import foxiwhitee.hellmod.integration.thaumcraft.ThaumcraftIntegration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.crafting.InfusionRecipe;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class PartAlchemicalConstructionPatternTerminal extends PartThaumcraftPatternTerminal {
    private final AppEngInternalInventory crafting = new AppEngInternalInventory(this, 1);
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);

    public PartAlchemicalConstructionPatternTerminal(ItemStack is) {
        super(is);
    }

    @Override
    protected void updateRecipe() {
        if (getInventoryCrafting().getStackInSlot(0) == null) {
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
        return ThaumcraftIntegration.getGuiBridge(0);
    }

    @Override
    public CableBusTextures getFrontBright() {
        return ThaumcraftIntegration.getBusTextureBright(0);
    }

    @Override
    public CableBusTextures getFrontColored() {
        return ThaumcraftIntegration.getBusTextureColored(0);
    }

    @Override
    public CableBusTextures getFrontDark() {
        return ThaumcraftIntegration.getBusTextureDark(0);
    }

}
