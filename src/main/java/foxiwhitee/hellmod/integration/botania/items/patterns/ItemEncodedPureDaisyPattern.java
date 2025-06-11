package foxiwhitee.hellmod.integration.botania.items.patterns;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.integration.botania.helpers.PureDaisyPatternHelper;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEncodedPureDaisyPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedPureDaisyPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new PureDaisyPatternHelper(is, w);
        } catch (Throwable var4) {
            return null;
        }
    }
}