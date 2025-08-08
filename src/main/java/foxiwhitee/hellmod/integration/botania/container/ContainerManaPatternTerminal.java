package foxiwhitee.hellmod.integration.botania.container;

import appeng.api.storage.ITerminalHost;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public abstract class ContainerManaPatternTerminal extends ContainerPatternTerminal {
    public ContainerManaPatternTerminal(InventoryPlayer ip, ITerminalHost host) {
        super(ip, host);
    }

    @Override
    public AEItemStack[] getInputs() {
        AEItemStack[] input = new AEItemStack[this.getInventoryCraftingSlots().length + 1];
        boolean hasValue = false;
        AEItemStack mana = AEItemStack.create(new ItemStack(BotaniaIntegration.mana_drop));
        mana.setStackSize(getManaCost());
        input[0] = mana;
        for(int x = 1; x <= this.getInventoryCraftingSlots().length; ++x) {
            input[x] = AEItemStack.create(this.getInventoryCraftingSlots()[x - 1].getStack());
            if (input[x] != null) {
                hasValue = true;
            }
        }

        if (hasValue) {
            return input;
        } else {
            return null;
        }
    }

    abstract protected long getManaCost();
}
