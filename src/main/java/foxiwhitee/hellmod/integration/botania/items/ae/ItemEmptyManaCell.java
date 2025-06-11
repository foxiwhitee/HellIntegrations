package foxiwhitee.hellmod.integration.botania.items.ae;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.item.Item;

public final class ItemEmptyManaCell extends Item {
    public ItemEmptyManaCell(String name) {
        setUnlocalizedName(name);
        setTextureName(HellCore.MODID + ":storageCells/" + name);
    }
}

