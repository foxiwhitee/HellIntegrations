package foxiwhitee.hellmod.container.interfaces;

import appeng.container.slot.SlotFake;
import appeng.container.slot.SlotNormal;
import appeng.container.slot.SlotRestrictedInput;
import appeng.helpers.DualityInterface;
import appeng.helpers.IInterfaceHost;
import foxiwhitee.hellmod.utils.interfaces.CustomHybridDualityInterface;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerHybridInterface extends ContainerCustomInterface {
    public ContainerHybridInterface(final InventoryPlayer ip, final IInterfaceHost te) {
        super(ip, te);

        for (int x = 0; x < CustomHybridDualityInterface.getNumberOfPatternSlots(); x++) {
            int y = x / 9;
            this.addSlotToContainer(new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.ENCODED_PATTERN, myDuality.getPatterns(),
                    x, 8 + 18 * x - 162 * y, 97 + 19 * y, this.getInventoryPlayer()));
        }

        for (int x = 0; x < DualityInterface.NUMBER_OF_CONFIG_SLOTS; x++) {
            this.addSlotToContainer(new SlotFake(myDuality.getConfig(), x, 8 + 18 * x, 35));
        }

        for (int x = 0; x < DualityInterface.NUMBER_OF_STORAGE_SLOTS; x++) {
            this.addSlotToContainer(new SlotNormal(myDuality.getStorage(), x, 8 + 18 * x, 35 + 18));
        }
    }

    protected void bindPlayerInventory(final InventoryPlayer inventoryPlayer, final int offsetX, final int offsetY) {
        super.bindPlayerInventory(inventoryPlayer, offsetX, 211 - 44);
    }

    @Override
    protected int getHeight() {
        return 250;
    }

}
