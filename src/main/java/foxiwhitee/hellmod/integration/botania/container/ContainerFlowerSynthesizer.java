package foxiwhitee.hellmod.integration.botania.container;

import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotFake;
import foxiwhitee.hellmod.container.slots.CustomSlotRestrictedInput;
import foxiwhitee.hellmod.container.slots.SlotFakeAdv;
import foxiwhitee.hellmod.integration.botania.tile.ae.TileFlowerSynthesizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerFlowerSynthesizer extends AEBaseContainer {
    TileFlowerSynthesizer tile;
    public ContainerFlowerSynthesizer(EntityPlayer ip, TileFlowerSynthesizer myTile) {
        super(ip.inventory, myTile, null);
        tile = myTile;
        bindPlayerInventory(ip.inventory, 17, 148);

        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.FLOWER_SYNTHESIZER_UPGRADE, myTile.getInventoryUpgrade(), 0, 48, 72, ip.inventory));
        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.FLOWER_SYNTHESIZER_UPGRADE, myTile.getInventoryUpgrade(), 1, 146, 72, ip.inventory));

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.FLOWER_CORES_FUNC, tile.getInventory(), i, 80 + 17 * (i % 3), 55 + 17 * (i / 3), ip.inventory));
        }

    }

    public TileFlowerSynthesizer getTile() {
        return tile;
    }

}
