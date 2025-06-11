package foxiwhitee.hellmod.integration.avaritia.tile.collectors;

import foxiwhitee.hellmod.integration.avaritia.helpers.INeutronCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileCustomNeutronCollector extends TileEntity implements IInventory {
    public TileCustomNeutronCollector() {}
    private ItemStack neutrons;

    private int facing = 2;

    private int progress;

    public void updateEntity() {
        if (++this.progress >= this.getTicks()) {
            if (this.neutrons == null) {
                this.neutrons = this.getStack();
            } else if (this.neutrons.getItemDamage() == this.getStack().getItemDamage() && this.neutrons.stackSize < 64) {
                this.neutrons.stackSize += this.getStack().stackSize;
            }
            this.progress = 0;
            markDirty();
        }
    }

    public int getFacing() {
        return this.facing;
    }

    public void setFacing(int dir) {
        this.facing = dir;
    }

    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        writeCustomNBT(tag);
    }

    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        readCustomNBT(tag);
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeCustomNBT(tag);
        return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -999, tag);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        readCustomNBT(packet.func_148857_g());
    }

    public void readCustomNBT(NBTTagCompound tag) {
        this.neutrons = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Neutrons"));
        this.progress = tag.getInteger("Progress");
        this.facing = tag.getShort("Facing");
    }

    public void writeCustomNBT(NBTTagCompound tag) {
        tag.setInteger("Progress", this.progress);
        tag.setShort("Facing", (short)this.facing);
        if (this.neutrons != null) {
            NBTTagCompound produce = new NBTTagCompound();
            this.neutrons.writeToNBT(produce);
            tag.setTag("Neutrons", (NBTBase)produce);
        } else {
            tag.removeTag("Neutrons");
        }
    }

    public int getSizeInventory() {
        return 1;
    }

    public ItemStack getStackInSlot(int slot) {
        return this.neutrons;
    }

    public ItemStack decrStackSize(int slot, int decrement) {
        if (this.neutrons == null)
            return null;
        if (decrement < this.neutrons.stackSize) {
            ItemStack itemStack = this.neutrons.splitStack(decrement);
            if (this.neutrons.stackSize <= 0)
                this.neutrons = null;
            return itemStack;
        }
        ItemStack take = this.neutrons;
        this.neutrons = null;
        return take;
    }

    public void openInventory() {}

    public void closeInventory() {}

    public boolean isUseableByPlayer(EntityPlayer player) {
        return (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D);
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return false;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.neutrons = stack;
    }

    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    public String getInventoryName() {
        return "container.custom_neutron_collector";
    }

    public abstract String getName();

    public abstract int getTicks();

    public abstract ItemStack getStack();
}
