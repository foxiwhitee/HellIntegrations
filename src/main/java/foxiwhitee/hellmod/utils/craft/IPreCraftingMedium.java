package foxiwhitee.hellmod.utils.craft;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import net.minecraft.inventory.InventoryCrafting;

public interface IPreCraftingMedium {
    boolean pushPattern(ICraftingPatternDetails var1, InventoryCrafting var2);

    boolean isBusy();

    default boolean pushPattern(ICraftingPatternDetails details, InventoryCrafting ic, CraftingCPUCluster cluster) {
        return this.pushPattern(details, ic);
    }
}
