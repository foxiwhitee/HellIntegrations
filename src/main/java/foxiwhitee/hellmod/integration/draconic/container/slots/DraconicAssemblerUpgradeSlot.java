package foxiwhitee.hellmod.integration.draconic.container.slots;

import foxiwhitee.hellmod.container.slots.HellSlot;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.items.ItemDraconicEnergyUpgrades;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class DraconicAssemblerUpgradeSlot extends HellSlot {
    public DraconicAssemblerUpgradeSlot(IInventory inv, int idx, int x, int y) {
        super(inv, idx, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return (itemstack == null ||
                (itemstack.getItem() == DraconicEvolutionIntegration.draconicAssemblerUpgrades && itemstack.getItemDamage() >= 0 && itemstack.getItemDamage() <= 1) ||
                itemstack.getItem() instanceof ItemDraconicEnergyUpgrades);
    }
}
