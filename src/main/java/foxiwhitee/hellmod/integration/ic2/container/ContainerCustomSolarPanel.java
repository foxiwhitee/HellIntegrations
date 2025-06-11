package foxiwhitee.hellmod.integration.ic2.container;

import foxiwhitee.hellmod.integration.ic2.tile.generators.panels.TileCustomSolarPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerCustomSolarPanel  extends Container {
    private final TileCustomSolarPanel tileentity;
    private int storage = 0;
    private int fuel = 0;
    private boolean sunIsUp;
    private boolean skyIsVisible;
    private int generating;

    public ContainerCustomSolarPanel(EntityPlayer inventoryplayer, TileCustomSolarPanel tileentitysolarpanel) {
        this.tileentity = tileentitysolarpanel;

        for(int j = 0; j < 4; ++j) {
            this.addSlotToContainer(new Slot(this.tileentity, j, 17 + j * 18, 59));
        }

        for(int i = 0; i < 3; ++i) {
            for(int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryplayer.inventory, k + i * 9 + 9, 17 + k * 18, 86 + i * 18));
            }
        }

        for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryplayer.inventory, j, 17 + j * 18, 144));
        }

    }

    public void addCraftingToCrafters(ICrafting icrafting) {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate(this, 0, this.tileentity.sunIsUp ? 1 : 0);
        icrafting.sendProgressBarUpdate(this, 1, this.tileentity.skyIsVisible ? 1 : 0);
        icrafting.sendProgressBarUpdate(this, 2, this.tileentity.generating);
        icrafting.sendProgressBarUpdate(this, 3, this.tileentity.storage & '\uffff');
        icrafting.sendProgressBarUpdate(this, 4, this.tileentity.storage >>> 16);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < super.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);
            icrafting.sendProgressBarUpdate(this, 0, this.tileentity.sunIsUp ? 1 : 0);
            icrafting.sendProgressBarUpdate(this, 1, this.tileentity.skyIsVisible ? 1 : 0);
            icrafting.sendProgressBarUpdate(this, 2, this.tileentity.generating);
            icrafting.sendProgressBarUpdate(this, 3, this.tileentity.storage & '\uffff');
            icrafting.sendProgressBarUpdate(this, 4, this.tileentity.storage >>> 16);
        }

        this.sunIsUp = this.tileentity.sunIsUp;
        this.skyIsVisible = this.tileentity.skyIsVisible;
        this.generating = this.tileentity.generating;
        this.storage = this.tileentity.storage;
    }

    public void updateProgressBar(int i, int j) {
        if (i == 0) {
            this.tileentity.sunIsUp = j != 0;
        }

        if (i == 1) {
            this.tileentity.skyIsVisible = j != 0;
        }

        if (i == 2) {
            this.tileentity.generating = j;
        }

        if (i == 3) {
            this.tileentity.storage = this.tileentity.storage & -65536 | j;
        }

        if (i == 4) {
            this.tileentity.storage = this.tileentity.storage & '\uffff' | j << 16;
        }

    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack stack = null;
        Slot slotObject = (Slot)super.inventorySlots.get(par2);
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (par2 >= 0 && par2 <= 3) {
                if (!this.mergeItemStack(stackInSlot, 4, 40, true)) {
                    return null;
                }
            } else if (par2 >= 4 && par2 < 31) {
                if (!this.mergeItemStack(stackInSlot, 0, 4, false) && !this.mergeItemStack(stackInSlot, 31, 40, false)) {
                    return null;
                }
            } else if (par2 >= 31 && par2 < 39) {
                if (!this.mergeItemStack(stackInSlot, 0, 30, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(stackInSlot, 0, 30, false)) {
                return null;
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack((ItemStack)null);
            } else {
                slotObject.onSlotChanged();
            }

            if (stack.stackSize == stackInSlot.stackSize) {
                return null;
            }

            slotObject.onPickupFromSlot(par1EntityPlayer, stackInSlot);
        }

        return stack;
    }

    public boolean canInteractWith(EntityPlayer entityplayer) {
        return this.tileentity.isUseableByPlayer(entityplayer);
    }

    public TileEntity getTile() {
        return tileentity;
    }
}
