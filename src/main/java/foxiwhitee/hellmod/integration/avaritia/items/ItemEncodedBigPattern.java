package foxiwhitee.hellmod.integration.avaritia.items;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.items.misc.ItemEncodedPattern;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.avaritia.helpers.BigPatternHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEncodedBigPattern extends ItemEncodedPattern {
    public ItemEncodedBigPattern(String name) {
        setCreativeTab(HellCore.HELL_TAB);
        setUnlocalizedName(name);
        setTextureName(HellCore.MODID + ":patterns/" + name);
    }

    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return (ICraftingPatternDetails)new BigPatternHelper(is, w);
        } catch (Throwable e) {
            return null;
        }
    }
}
