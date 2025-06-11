package foxiwhitee.hellmod.container;

import appeng.api.parts.IPart;
import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotRestrictedInput;
import foxiwhitee.hellmod.container.slots.CustomSlotRestrictedInput;
import foxiwhitee.hellmod.tile.cpu.TileMEServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerMEServer extends AEBaseContainer {
    public ContainerMEServer(EntityPlayer ip, TileMEServer myTile) {
        super(ip.inventory, myTile, null);
        bindPlayerInventory(ip.inventory, 70, 184);

        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 0, 60, 37, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 1, 141, 37, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 2, 222, 37, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 3, 60, 71, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 4, 141, 71, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 5, 222, 71, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 6, 60, 105, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 7, 141, 105, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 8, 222, 105, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 9, 60, 139, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 10, 141, 139, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.STORAGE, myTile.getStorage(), 11, 222, 139, this.getInventoryPlayer()));

        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 0, 60 + 18, 37, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 1, 141 + 18, 37, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 2, 222 + 18, 37, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 3, 60 + 18, 71, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 4, 141 + 18, 71, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 5, 222 + 18, 71, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 6, 60 + 18, 105, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 7, 141 + 18, 105, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 8, 222 + 18, 105, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 9, 60 + 18, 139, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 10, 141 + 18, 139, this.getInventoryPlayer()));
        this.addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.ACCELERATOR, myTile.getAccelerators(), 11, 222 + 18, 139, this.getInventoryPlayer()));


    }
}
