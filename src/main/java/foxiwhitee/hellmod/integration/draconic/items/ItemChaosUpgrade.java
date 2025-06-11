package foxiwhitee.hellmod.integration.draconic.items;

import foxiwhitee.hellmod.helpers.IAutomatedBlockUpgrade;
import foxiwhitee.hellmod.items.DefaultItem;

public class ItemChaosUpgrade extends DefaultItem implements IAutomatedBlockUpgrade {
    public ItemChaosUpgrade(String name, String texture) {
        super(name, texture);
        this.setMaxStackSize(1);
    }
}
