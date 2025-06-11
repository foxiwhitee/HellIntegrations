package foxiwhitee.hellmod.integration.botania.items.patterns;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.integration.botania.helpers.PetalsPatternHelper;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEncodedPetalsPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedPetalsPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new PetalsPatternHelper(is, w);
        } catch (Throwable var4) {
            return null;
        }
    }
}