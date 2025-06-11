package foxiwhitee.hellmod.integration.draconic.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileCustomUpgradeModifier extends TileEntity implements ISidedInventory {
    ItemStack[] items = new ItemStack[1];
    public float rotation = 0.0F;
    public float rotationSpeed = 0.0F;
    private float targetSpeed = 0.0F;

    public TileCustomUpgradeModifier() {}

    public void updateEntity() {
        if (this.items[0] != null) {
            this.targetSpeed = 5.0F;
        } else {
            this.targetSpeed = 0.0F;
        }

        if (this.rotationSpeed < this.targetSpeed) {
            this.rotationSpeed += 0.05F;
        } else if (this.rotationSpeed > this.targetSpeed) {
            this.rotationSpeed -= 0.05F;
        }

        if (this.targetSpeed == 0.0F && this.rotationSpeed < 0.0F) {
            this.rotationSpeed = 0.0F;
        }

        this.rotation += this.rotationSpeed;
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagCompound);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    public int getSizeInventory() {
        return this.items.length;
    }

    public ItemStack getStackInSlot(int i) {
        return this.items[i];
    }

    public ItemStack decrStackSize(int i, int count) {
        ItemStack itemstack = this.getStackInSlot(i);
        if (itemstack != null) {
            if (itemstack.stackSize <= count) {
                this.setInventorySlotContents(i, (ItemStack)null);
            } else {
                itemstack = itemstack.splitStack(count);
                if (itemstack.stackSize == 0) {
                    this.setInventorySlotContents(i, (ItemStack)null);
                }
            }
        }

        return itemstack;
    }

    public ItemStack getStackInSlotOnClosing(int i) {
        ItemStack item = this.getStackInSlot(i);
        if (item != null) {
            this.setInventorySlotContents(i, (ItemStack)null);
        }

        return item;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }

    }

    public String getInventoryName() {
        return "";
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        if (this.worldObj == null) {
            return true;
        } else if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + 0.4) < (double)64.0F;
        }
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public int[] getAccessibleSlotsFromSide(int var1) {
        return new int[]{0};
    }

    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return true;
    }

    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return true;
    }

    public void writeToNBT(NBTTagCompound compound) {
        NBTTagCompound[] tag = new NBTTagCompound[this.items.length];

        for(int i = 0; i < this.items.length; ++i) {
            tag[i] = new NBTTagCompound();
            if (this.items[i] != null) {
                tag[i] = this.items[i].writeToNBT(tag[i]);
            }

            compound.setTag("Item" + i, tag[i]);
        }

        super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagCompound[] tag = new NBTTagCompound[this.items.length];

        for(int i = 0; i < this.items.length; ++i) {
            tag[i] = compound.getCompoundTag("Item" + i);
            this.items[i] = ItemStack.loadItemStackFromNBT(tag[i]);
        }

        super.readFromNBT(compound);
    }
}
