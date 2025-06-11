package foxiwhitee.hellmod.integration.draconic.helpers;


import java.util.List;
import net.minecraft.item.ItemStack;

public interface IFusionCraftingInventory {
    ItemStack getStackInCore(int paramInt);

    void setStackInCore(int paramInt, ItemStack paramItemStack);

    List<IFusionCraftingInjector> getInjectors();

    List<IFusionCraftingCharger> getChargers();

    default long getIngredientEnergyCost() {
        return 0L;
    }

    boolean craftingInProgress();

    int getCraftingStage();
}
