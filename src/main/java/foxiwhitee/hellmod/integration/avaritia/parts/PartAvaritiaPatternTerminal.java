package foxiwhitee.hellmod.integration.avaritia.parts;

import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import foxiwhitee.hellmod.parts.PartPatternTerminal;
import foxiwhitee.hellmod.parts.PartTerminal;
import net.minecraft.item.ItemStack;

public abstract class PartAvaritiaPatternTerminal extends PartPatternTerminal {

    public PartAvaritiaPatternTerminal(ItemStack is) {
        super(is);
    }

    @Override
    protected ItemStack getItemFromTile(Object obj) { return new ItemStack(AvaritiaIntegration.ITEM_PARTS_TERMINALS); }
}
