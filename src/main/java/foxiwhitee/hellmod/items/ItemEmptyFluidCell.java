package foxiwhitee.hellmod.items;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.item.Item;

public final class ItemEmptyFluidCell extends Item {
    public ItemEmptyFluidCell(String name) {
        setUnlocalizedName(name);
        setTextureName(HellCore.MODID + ":storageCells/" + name);
    }
}

