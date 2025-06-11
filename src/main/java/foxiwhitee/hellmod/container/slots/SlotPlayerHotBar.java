package foxiwhitee.hellmod.container.slots;

import net.minecraft.inventory.IInventory;

public class SlotPlayerHotBar extends HellSlot {
    public SlotPlayerHotBar(IInventory par1iInventory, int par2, int par3, int par4) {
        super(par1iInventory, par2, par3, par4);
        this.setPlayerSide(true);
    }
}
