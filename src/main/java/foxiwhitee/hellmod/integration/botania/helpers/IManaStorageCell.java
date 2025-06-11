package foxiwhitee.hellmod.integration.botania.helpers;

import net.minecraft.item.ItemStack;

public interface IManaStorageCell {
    boolean isManaCell(ItemStack paramItemStack);

    double getIdleDrain(ItemStack paramItemStack);

    long getBytes(ItemStack paramItemStack);
}

