package foxiwhitee.hellmod.integration.thaumcraft.items;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import foxiwhitee.hellmod.integration.thaumcraft.helpers.AlchemicalConstructionPatternHelper;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEncodedAlchemicalConstructionPattern extends ItemUniversalEncodedPattern {
    public ItemEncodedAlchemicalConstructionPattern(String name) {
        super(name);
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new AlchemicalConstructionPatternHelper(is, w);
        } catch (Throwable var4) {
            return null;
        }
    }
}
