package foxiwhitee.hellmod.integration.botania.parts;

import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.parts.PartPatternTerminal;
import foxiwhitee.hellmod.parts.PartTerminal;
import net.minecraft.item.ItemStack;

public abstract class PartBotaniaPatternTerminal extends PartPatternTerminal {

    public PartBotaniaPatternTerminal(ItemStack is) {
        super(is);
    }

    @Override
    protected ItemStack getItemFromTile(Object obj) { return new ItemStack(BotaniaIntegration.ITEM_PARTS_TERMINALS); }
}
