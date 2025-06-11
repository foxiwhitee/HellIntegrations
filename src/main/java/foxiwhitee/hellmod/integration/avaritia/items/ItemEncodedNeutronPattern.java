package foxiwhitee.hellmod.integration.avaritia.items;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.integration.avaritia.helpers.NeutronPatternHelper;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEncodedNeutronPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedNeutronPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new NeutronPatternHelper(is, w);
        } catch (Throwable var4) {
            return null;
        }
    }
}
