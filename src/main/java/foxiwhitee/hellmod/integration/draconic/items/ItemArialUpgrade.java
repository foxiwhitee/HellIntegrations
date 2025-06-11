package foxiwhitee.hellmod.integration.draconic.items;

import foxiwhitee.hellmod.helpers.IAutomatedBlockUpgrade;
import foxiwhitee.hellmod.items.DefaultItem;

public class ItemArialUpgrade extends DefaultItem implements IAutomatedBlockUpgrade {

    public ItemArialUpgrade(String name, String texture) {
        super(name, texture);
        this.setMaxStackSize(1);
    }
}
