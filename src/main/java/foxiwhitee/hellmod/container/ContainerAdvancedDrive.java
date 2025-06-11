package foxiwhitee.hellmod.container;

import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.container.slot.SlotRestrictedInput;
import foxiwhitee.hellmod.tile.TileAdvancedDrive;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public class ContainerAdvancedDrive extends AEBaseContainer {
  public ContainerAdvancedDrive(InventoryPlayer ip, TileAdvancedDrive drive) {
    super(ip, (TileEntity)drive, null);
    int c = 0;
    for (int a = 29; a > 24; a--) {
      c++;
      addSlotToContainer((Slot)new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.STORAGE_CELLS, (IInventory)drive, a, 48 + c * 18, 116, getInventoryPlayer()));
    }
    int b = 0;
    for (int i = 24; i > 19; i--) {
      b++;
      addSlotToContainer((Slot)new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.STORAGE_CELLS, (IInventory)drive, i, 48 + b * 18, 98, getInventoryPlayer()));
    }
    int n = 0;
    for (int j = 19; j > 14; j--) {
      n++;
      addSlotToContainer((Slot)new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.STORAGE_CELLS, (IInventory)drive, j, 48 + n * 18, 80, getInventoryPlayer()));
    }
    int m = 0;
    for (int k = 14; k > 9; k--) {
      m++;
      addSlotToContainer((Slot)new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.STORAGE_CELLS, (IInventory)drive, k, 48 + m * 18, 62, getInventoryPlayer()));
    }
    int q = 0;
    for (int i1 = 9; i1 > 4; i1--) {
      q++;
      addSlotToContainer((Slot)new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.STORAGE_CELLS, (IInventory)drive, i1, 48 + q * 18, 44, getInventoryPlayer()));
    }
    int w = 0;
    for (int i2 = 4; i2 > -1; i2--) {
      w++;
      addSlotToContainer((Slot)new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.STORAGE_CELLS, (IInventory)drive, i2, 48 + w * 18, 26, getInventoryPlayer()));
    }
    bindPlayerInventory(ip, 22, 147);
  }
}

