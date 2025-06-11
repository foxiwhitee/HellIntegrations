package foxiwhitee.hellmod.integration.ic2.tile.generators.infinity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class TileInfinityGenerator extends TileEntity implements IEnergyTile, IWrenchable, IEnergySource, IInventory, INetworkDataProvider, INetworkUpdateListener, INetworkTileEntityEventListener {
    public static Random randomizer = new Random();
    public int ticker;
    public int generating;
    public boolean initialized;
    private short facing = 2;
    private boolean noSunWorld;
    private boolean wetBiome;
    private int machineTire;
    public boolean addedToEnergyNet;
    private boolean created = false;
    private ItemStack[] chargeSlots;
    public int fuel;
    private int lastX;
    private int lastY;
    private int lastZ;
    public int storage;
    public boolean loaded = false;
    private static List<String> fields = Arrays.asList();
    public boolean active = false;


    public TileInfinityGenerator() {
        this.storage = 0;
        this.chargeSlots = new ItemStack[4];
        this.initialized = false;
        this.ticker = randomizer.nextInt(this.tickRate());
        this.lastX = super.xCoord;
        this.lastY = super.yCoord;
        this.lastZ = super.zCoord;
        this.machineTire = Integer.MAX_VALUE;
    }

    public void validate() {
        super.validate();
        if (!this.isInvalid() && super.worldObj.blockExists(super.xCoord, super.yCoord, super.zCoord)) {
            this.onLoaded();
        }
    }

    public void invalidate() {
        if (this.loaded) {
            this.onUnloaded();
        }

        super.invalidate();
    }

    public void onLoaded() {
        if (!super.worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }

        this.loaded = true;
    }

    public void onChunkUnload() {
        if (this.loaded) {
            this.onUnloaded();
        }

        super.onChunkUnload();
    }

    public void onUnloaded() {
        if (!super.worldObj.isRemote && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        this.loaded = false;
    }

    public void intialize() {
        this.wetBiome = super.worldObj.getWorldChunkManager().getBiomeGenAt(super.xCoord, super.zCoord).getIntRainfall() > 0;
        this.noSunWorld = super.worldObj.provider.hasNoSky;
        this.initialized = true;
        if (!this.addedToEnergyNet) {
            this.onLoaded();
        }

    }

    public void updateEntity() {
        super.updateEntity();
        if (!this.initialized && super.worldObj != null) {
            this.intialize();
        }

        if (!super.worldObj.isRemote) {
            if (this.lastX != super.xCoord || this.lastZ != super.zCoord || this.lastY != super.yCoord) {
                this.lastX = super.xCoord;
                this.lastY = super.yCoord;
                this.lastZ = super.zCoord;
                this.onUnloaded();
                this.intialize();
            }
            generating = getGen();
            long canReceive = Math.min(generating, getMaxStorage() - this.storage);
            if (canReceive > 0) {
                this.storage += (int)Math.min(Integer.MAX_VALUE, canReceive);
            }

            boolean needInvUpdate = false;
            double sentPacket = (double)0.0F;

            for(int i = 0; i < this.chargeSlots.length; ++i) {
                if (this.chargeSlots[i] != null && this.chargeSlots[i].getItem() instanceof IElectricItem && this.storage > 0) {
                    sentPacket = ElectricItem.manager.charge(this.chargeSlots[i], (double)this.storage, Integer.MAX_VALUE, false, false);
                    if (sentPacket > (double)0.0F) {
                        needInvUpdate = true;
                    }

                    this.storage = (int)((double)this.storage - sentPacket);
                }
            }

            if (needInvUpdate) {
                super.markDirty();
            }

        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.storage = nbttagcompound.getInteger("storage");
        this.lastX = nbttagcompound.getInteger("lastX");
        this.lastY = nbttagcompound.getInteger("lastY");
        this.lastZ = nbttagcompound.getInteger("lastZ");
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.chargeSlots = new ItemStack[this.getSizeInventory()];

        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;
            if (j >= 0 && j < this.chargeSlots.length) {
                this.chargeSlots[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        nbttagcompound.setInteger("storage", this.storage);
        nbttagcompound.setInteger("lastX", this.lastX);
        nbttagcompound.setInteger("lastY", this.lastY);
        nbttagcompound.setInteger("lastZ", this.lastZ);

        for(int i = 0; i < this.chargeSlots.length; ++i) {
            if (this.chargeSlots[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.chargeSlots[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public boolean isAddedToEnergyNet() {
        return this.addedToEnergyNet;
    }

    public int getMaxEnergyOutput() {
        return this.getProduction();
    }

    public int gaugeEnergyScaled(int i) {
        return this.storage * i / this.getMaxStorage();
    }

    public int gaugeFuelScaled(int i) {
        return i;
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return entityplayer.getDistance((double)super.xCoord + (double)0.5F, (double)super.yCoord + (double)0.5F, (double)super.zCoord + (double)0.5F) <= (double)64.0F;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    public int tickRate() {
        return 128;
    }

    public short getFacing() {
        return this.facing;
    }

    public void setFacing(short facing) {
        this.facing = facing;
    }

    public boolean wrenchCanSetFacing(EntityPlayer entityplayer, int i) {
        return false;
    }

    public boolean wrenchCanRemove(EntityPlayer entityplayer) {
        return true;
    }

    public float getWrenchDropRate() {
        return 1.0F;
    }

    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(super.worldObj.getBlock(super.xCoord, super.yCoord, super.zCoord), 1, super.worldObj.getBlockMetadata(super.xCoord, super.yCoord, super.zCoord));
    }

    public ItemStack[] getContents() {
        return this.chargeSlots;
    }

    public int getSizeInventory() {
        return 4;
    }

    public ItemStack getStackInSlot(int i) {
        return this.chargeSlots[i];
    }

    public ItemStack decrStackSize(int i, int j) {
        if (this.chargeSlots[i] != null) {
            if (this.chargeSlots[i].stackSize <= j) {
                ItemStack itemstack = this.chargeSlots[i];
                this.chargeSlots[i] = null;
                return itemstack;
            } else {
                ItemStack itemstack1 = this.chargeSlots[i].splitStack(j);
                if (this.chargeSlots[i].stackSize == 0) {
                    this.chargeSlots[i] = null;
                }

                return itemstack1;
            }
        } else {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.chargeSlots[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }

    }

    public String getInventoryName() {
        return "Advanced Solar Panel";
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public String getInvName() {
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.chargeSlots[var1] != null) {
            ItemStack var2 = this.chargeSlots[var1];
            this.chargeSlots[var1] = null;
            return var2;
        } else {
            return null;
        }
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public void onNetworkUpdate(String field) {
    }

    public List<String> getNetworkedFields() {
        return fields;
    }

    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return true;
    }

    public double getOfferedEnergy() {
        return (double)Math.min(this.getProduction(), this.storage);
    }

    public void drawEnergy(double amount) {
        this.storage = (int)((double)this.storage - amount);
    }

    public int getSourceTier() {
        return this.machineTire;
    }

    public boolean getActive() {
        return this.active;
    }

    @Override
    public void onNetworkEvent(int i) {}

    public abstract int getGen();

    public abstract int getMaxStorage();

    public abstract int getProduction();

    public abstract String getName();
}
