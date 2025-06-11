package foxiwhitee.hellmod.integration.botania.flowers;

import net.minecraft.item.ItemStack;

public interface ICoreFunctionalFlower {
    IFunctionalFlowerLogic getLogic(ItemStack stack);
}
