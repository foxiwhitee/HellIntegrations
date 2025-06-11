package foxiwhitee.hellmod.container;

import appeng.api.AEApi;
import appeng.api.storage.data.IAEItemStack;
import appeng.client.me.InternalSlotME;
import appeng.client.me.SlotME;
import appeng.container.slot.*;
import appeng.util.Platform;
import foxiwhitee.hellmod.container.slots.HellSlot;
import foxiwhitee.hellmod.container.slots.SlotPlayerHotBar;
import foxiwhitee.hellmod.container.slots.SlotPlayerInv;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class HellBaseContainer extends Container {
    private final InventoryPlayer invPlayer;
    private final TileEntity tileEntity;
    private boolean isProcessingShiftClick = false;

    public HellBaseContainer(InventoryPlayer ip, TileEntity myTile) {
        this.invPlayer = ip;
        this.tileEntity = myTile;
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int offsetX, int offsetY) {
        for(int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new SlotPlayerHotBar(inventoryPlayer, i, 8 + i * 18 + offsetX, 58 + offsetY));

        }
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new SlotPlayerInv(inventoryPlayer, j + i * 9 + 9, 8 + j * 18 + offsetX, offsetY + i * 18));

            }
        }
    }

    public InventoryPlayer getInventoryPlayer() {
        return this.invPlayer;
    }

    public TileEntity getTileEntity() {
        return this.tileEntity;
    }

    public boolean canInteractWith(EntityPlayer entityplayer) {
        return this.tileEntity instanceof IInventory ? ((IInventory)this.tileEntity).isUseableByPlayer(entityplayer) : true;
    }

}
