package foxiwhitee.hellmod.tile.energycell;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.networking.events.MENetworkPowerStorage;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkTile;
import appeng.util.SettingsFrom;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileCustomEnergyCell extends AENetworkTile implements IAEPowerStorage {
    private double internalCurrentPower = (double)0.0F;
    private byte currentMeta = -1;

    public TileCustomEnergyCell() {
        this.getProxy().setIdlePowerUsage((double)0.0F);
    }

    public AECableType getCableConnectionType(ForgeDirection dir) {
        return AECableType.COVERED;
    }

    public void onReady() {
        super.onReady();
        this.currentMeta = (byte)this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        this.changePowerLevel();
    }

    private void changePowerLevel() {
        if (!this.notLoaded()) {
            byte boundMetadata = (byte)((int)((double)8.0F * (this.internalCurrentPower / this.getInternalMaxPower())));
            if (boundMetadata > 7) {
                boundMetadata = 7;
            }

            if (boundMetadata < 0) {
                boundMetadata = 0;
            }

            if (this.currentMeta != boundMetadata) {
                this.currentMeta = boundMetadata;
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.currentMeta, 2);
            }

        }
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_TileEnergyCell(NBTTagCompound data) {
        if (!this.worldObj.isRemote) {
            data.setDouble("internalCurrentPower", this.internalCurrentPower);
        }

    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_TileEnergyCell(NBTTagCompound data) {
        this.internalCurrentPower = data.getDouble("internalCurrentPower");
    }

    public boolean canBeRotated() {
        return false;
    }

    public void uploadSettings(SettingsFrom from, NBTTagCompound compound) {
        if (from == SettingsFrom.DISMANTLE_ITEM) {
            this.internalCurrentPower = compound.getDouble("internalCurrentPower");
        }

    }

    public NBTTagCompound downloadSettings(SettingsFrom from) {
        if (from == SettingsFrom.DISMANTLE_ITEM) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setDouble("internalCurrentPower", this.internalCurrentPower);
            tag.setDouble("internalMaxPower", this.getInternalMaxPower());
            return tag;
        } else {
            return null;
        }
    }

    public final double injectAEPower(double amt, Actionable mode) {
        if (mode == Actionable.SIMULATE) {
            double fakeBattery = this.internalCurrentPower + amt;
            return fakeBattery > this.getInternalMaxPower() ? fakeBattery - this.getInternalMaxPower() : (double)0.0F;
        } else {
            if (this.internalCurrentPower < 0.01 && amt > 0.01) {
                this.getProxy().getNode().getGrid().postEvent(new MENetworkPowerStorage(this, MENetworkPowerStorage.PowerEventType.PROVIDE_POWER));
            }

            this.internalCurrentPower += amt;
            if (this.internalCurrentPower > this.getInternalMaxPower()) {
                amt = this.internalCurrentPower - this.getInternalMaxPower();
                this.internalCurrentPower = this.getInternalMaxPower();
                this.changePowerLevel();
                return amt;
            } else {
                this.changePowerLevel();
                return (double)0.0F;
            }
        }
    }

    public double getAEMaxPower() {
        return this.getInternalMaxPower();
    }

    public double getAECurrentPower() {
        return this.internalCurrentPower;
    }

    public boolean isAEPublicPowerStorage() {
        return true;
    }

    public AccessRestriction getPowerFlow() {
        return AccessRestriction.READ_WRITE;
    }

    public final double extractAEPower(double amt, Actionable mode, PowerMultiplier pm) {
        return pm.divide(this.extractAEPower(pm.multiply(amt), mode));
    }

    private double extractAEPower(double amt, Actionable mode) {
        if (mode == Actionable.SIMULATE) {
            return Math.min(this.internalCurrentPower, amt);
        } else {
            boolean wasFull = this.internalCurrentPower >= this.getInternalMaxPower() - 0.001;
            if (wasFull && amt > 0.001) {
                try {
                    this.getProxy().getGrid().postEvent(new MENetworkPowerStorage(this, MENetworkPowerStorage.PowerEventType.REQUEST_POWER));
                } catch (GridAccessException var6) {
                }
            }

            if (this.internalCurrentPower > amt) {
                this.internalCurrentPower -= amt;
                this.changePowerLevel();
                return amt;
            } else {
                amt = this.internalCurrentPower;
                this.internalCurrentPower = (double)0.0F;
                this.changePowerLevel();
                return amt;
            }
        }
    }

    protected abstract double getInternalMaxPower();
}
