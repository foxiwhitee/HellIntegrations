package foxiwhitee.hellmod.integration.botania.flowers;

import net.minecraft.item.ItemStack;

public interface IItemWithEffect {

    String getName();

    PartTypes getType();

    LevelTypes getFlowerType(ItemStack stack);
}
