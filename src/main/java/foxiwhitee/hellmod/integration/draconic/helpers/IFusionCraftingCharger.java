package foxiwhitee.hellmod.integration.draconic.helpers;

public interface IFusionCraftingCharger {
    int getPedestalTier();

    default long getInjectorCharge() {
        return 0L;
    }

    void onCraft();

    boolean setCraftingInventory(IFusionCraftingInventory paramIFusionCraftingInventory);

    int getFace();

    void setFace(int paramInt);
}

