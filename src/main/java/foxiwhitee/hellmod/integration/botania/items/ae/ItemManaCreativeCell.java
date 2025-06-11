package foxiwhitee.hellmod.integration.botania.items.ae;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.item.Item;

public final class ItemManaCreativeCell extends Item {
    public ItemManaCreativeCell(String name) {
        setUnlocalizedName(name);
        setTextureName(HellCore.MODID + ":storageCells/" + name);
        this.maxStackSize = 1;
    }
}
