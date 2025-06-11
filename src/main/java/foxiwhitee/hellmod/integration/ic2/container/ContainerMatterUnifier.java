package foxiwhitee.hellmod.integration.ic2.container;

import foxiwhitee.hellmod.integration.ic2.client.gui.GuiMatterUnifier;
import foxiwhitee.hellmod.integration.ic2.container.slot.SlotInvValid;
import foxiwhitee.hellmod.integration.ic2.helpers.IMatter;
import foxiwhitee.hellmod.integration.ic2.tile.TileMatterUnifier;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMatterUnifier extends Container {
    TileMatterUnifier tile;

    public double energy;

    private int clientProgress;

    public ContainerMatterUnifier(EntityPlayer inventory, TileMatterUnifier tile) {
        this.energy = 0.0D;
        this.clientProgress = 0;
        this.tile = tile;
        int i;
        for (i = 0; i < 9; i++)
            addSlotToContainer(new Slot((IInventory)inventory.inventory, i, 25 + 18 * i, 191));
        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new Slot((IInventory)inventory.inventory, j + i * 9 + 9, 25 + 18 * j, 133 + i * 18));
        }

        for (i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++)
                addSlotToContainer((Slot)new SlotInvValid((IInventory)tile, j + i * 9, 25 + 18 * j, 22 + i * 18));
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        if (this.tile != null)
            return this.tile.isUseableByPlayer(player);
        return false;
    }

    public TileMatterUnifier getTile() {
        return this.tile;
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        if (slot < 36) {
            ItemStack playerInventoryItemStack = player.inventory.getStackInSlot(slot);
            if (playerInventoryItemStack == null)
                return null;
            boolean validMatterStack = (Block.getBlockFromItem(playerInventoryItemStack.getItem()) instanceof IMatter || playerInventoryItemStack.getItem() == Ic2Items.massFabricator.getItem());
            if (validMatterStack) {
                int internalInventorySize = this.tile.getSizeInventory();
                for (int slotIndex = 0; slotIndex < internalInventorySize; slotIndex++) {
                    ItemStack hasStack = this.tile.getInternalInventory().getStackInSlot(slotIndex);
                    if (hasStack == null) {
                        player.inventory.setInventorySlotContents(slot, null);
                        this.tile.getInternalInventory().setInventorySlotContents(slotIndex, playerInventoryItemStack);
                        break;
                    }
                    if (hasStack.getItem() == playerInventoryItemStack.getItem() && hasStack.getItemDamage() == playerInventoryItemStack.getItemDamage() && hasStack.stackSize < hasStack.getMaxStackSize()) {
                        int count = hasStack.getMaxStackSize() - hasStack.stackSize;
                        if (count > playerInventoryItemStack.stackSize) {
                            int adding = playerInventoryItemStack.stackSize;
                            hasStack.stackSize += adding;
                            playerInventoryItemStack.stackSize -= adding;
                            player.inventory.setInventorySlotContents(slot, null);
                            break;
                        }
                        if (count < playerInventoryItemStack.stackSize) {
                            hasStack.stackSize += count;
                            playerInventoryItemStack.stackSize -= count;
                        }
                    }
                }
            }
        } else {
            ItemStack tileEntityItemStack = this.tile.getStackInSlot(slot - 36);
            if (tileEntityItemStack != null) {
                this.tile.getInternalInventory().setInventorySlotContents(slot - 36, null);
                player.inventory.addItemStackToInventory(tileEntityItemStack);
            }
        }
        return null;
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int progress = (int)this.tile.getEnergyStored();
        if (this.clientProgress != progress) {
            this.clientProgress = progress;
            for (Object o : this.crafters) {
                if (o instanceof ICrafting)
                    ((ICrafting)o).sendProgressBarUpdate(this, 0, progress);
            }
        }
    }

    public void updateProgressBar(int id, int val) {
        if ((Minecraft.getMinecraft()).currentScreen instanceof GuiMatterUnifier)
            ((GuiMatterUnifier)(Minecraft.getMinecraft()).currentScreen).receiveData(id, val);
    }

    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
    }
}
