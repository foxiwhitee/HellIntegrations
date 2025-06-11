package foxiwhitee.hellmod.items;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.item.Item;

public class DefaultItem extends Item {
    boolean mode = false;

    public DefaultItem(String name, String texture) {
        this.canRepair = false;
        this.setUnlocalizedName(name);
        this.setTextureName(HellCore.MODID + ":" + texture);
        this.setCreativeTab(HellCore.HELL_TAB);
        //this.setMaxDamage(800);
        this.maxStackSize = 64;
        RegisterUtils.registerItem(this);
    }
}
