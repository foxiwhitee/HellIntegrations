package foxiwhitee.hellmod.integration.thaumcraft.items;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.integration.thaumcraft.helpers.InfusionPatternHellper;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEncodedInfusionPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedInfusionPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new InfusionPatternHellper(is, w);
        } catch (Throwable var4) {
            return null;
        }
    }
}
