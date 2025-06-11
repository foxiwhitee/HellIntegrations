package foxiwhitee.hellmod.integration.botania.container;

import appeng.api.parts.IPart;
import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotFake;
import foxiwhitee.hellmod.container.slots.CustomSlotRestrictedInput;
import foxiwhitee.hellmod.container.slots.SlotFakeAdv;
import foxiwhitee.hellmod.integration.botania.tile.ae.TileManaGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerManaGenerator extends AEBaseContainer {
    TileManaGenerator tile;
    public ContainerManaGenerator(EntityPlayer ip, TileManaGenerator myTile) {
        super(ip.inventory, myTile, null);
        tile = myTile;
        bindPlayerInventory(ip.inventory, 17, 148);

        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ESSENCE_LIFE, myTile.getInventoryEssence(), 0, 169, 43, ip.inventory));
        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.CORE_LIFE, myTile.getInventoryCores(), 0, 169, 100, ip.inventory));

        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.MANA_GENERATOR_UPGRADES, myTile.getInventoryUpgrade(), 0, 10, 45, ip.inventory));
        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.MANA_GENERATOR_UPGRADES, myTile.getInventoryUpgrade(), 1, 10, 63, ip.inventory));
        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.MANA_GENERATOR_UPGRADES, myTile.getInventoryUpgrade(), 2, 10, 81, ip.inventory));
        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.MANA_GENERATOR_UPGRADES, myTile.getInventoryUpgrade(), 3, 10, 99, ip.inventory));

        int y = getYForCoresSlots(myTile.getInventory().getSizeInventory());
        for (int i = 0; i < myTile.getInventory().getSizeInventory(); i++) {
            addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.FLOWER_CORES, myTile.getInventory(), i, 49, y + i * 18, ip.inventory));
        }
        y = getYForCoresSlots(myTile.getInventoryConsumeFake().getSizeInventory());
        for (int i = 0; i < myTile.getInventoryConsumeFake().getSizeInventory(); i++) {
            addSlotToContainer(new SlotFakeAdv(myTile.getInventoryConsumeFake(), i, 67, y + i * 18));
        }

    }

    private int getYForCoresSlots(int i) {
        switch (i) {
            case 1: return 72;
            case 2: return 63;
            case 3: return 54;
            case 4: return 45;
            case 5: return 36;
            default: return 0;
        }
    }

    public TileManaGenerator getTile() {
        return tile;
    }

    public ItemStack slotClick(int slotNum, int button, int modifiers, EntityPlayer player) {
        if (slotNum >= 0) {
            Object object = this.inventorySlots.get(slotNum);
            if (object instanceof SlotFake) {
                ((SlotFake) object).putStack(player.inventory.getItemStack());
            }
        }
        return super.slotClick(slotNum, button, modifiers, player);
    }
}
