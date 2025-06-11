package foxiwhitee.hellmod.integration.draconic.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IFusionCraftingInjector {
    int getPedestalTier();

    ItemStack getStackInPedestal();

    void setStackInPedestal(ItemStack paramItemStack);

    void onCraft();

    boolean setCraftingInventory(IFusionCraftingInventory paramIFusionCraftingInventory);

    int getFace();

    void setFace(int paramInt);

    TileEntity getTile();
}