package foxiwhitee.hellmod.integration.ic2.tile;

import appeng.api.config.Actionable;
import appeng.api.util.DimensionalCoord;
import appeng.tile.AEBaseInvTile;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.ic2.helpers.IEnergyUpdateTile;
import foxiwhitee.hellmod.integration.ic2.helpers.ISpecialSintezatorPanel;
import foxiwhitee.hellmod.integration.ic2.helpers.ISupportsEUEnergyProvider;
import foxiwhitee.hellmod.integration.ic2.items.ItemEUEnergyUpgrades;
import foxiwhitee.hellmod.integration.ic2.items.ItemSunUpgrade;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketUpdateEnergy;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TileSynthesizer extends AEBaseInvTile implements IEnergySource, IEnergyUpdateTile, ISupportsEUEnergyProvider, IEnergyTile {
    private double maxEnergy = 1.0E8;
    private boolean alwaysDay = false;
    public boolean sunIsUp;
    private double energy;
    private double dayGen;
    private double nightGen;
    private double boost = 0;
    public boolean addedToEnergyNet = false;
    public boolean loaded = false;
    private final AppEngInternalInventory inv = new AppEngInternalInventory(this, 21);
    private final AppEngInternalInventory upgradeFotonInv = new AppEngInternalInventory(this, 1);
    private final AppEngInternalInventory upgradeInv = new AppEngInternalInventory(this, 1);

    private static final double[] euEnergyUpgrades = {HellConfig.synthesizerUpgrade1, HellConfig.synthesizerUpgrade2, HellConfig.synthesizerUpgrade3, HellConfig.synthesizerUpgrade4, HellConfig.synthesizerUpgrade5, HellConfig.synthesizerUpgrade6, HellConfig.synthesizerUpgrade7, HellConfig.synthesizerUpgrade8, HellConfig.synthesizerUpgrade9, HellConfig.synthesizerUpgrade10};

    @TileEvent(TileEventType.TICK)
    public void updateTick() {
        if (Platform.isServer()) {
            checkSun();
            double generating = (sunIsUp || alwaysDay) ? dayGen : nightGen;
            generating += boost * generating;
            this.energy = Math.min(energy + generating, maxEnergy);
            NetworkManager.instance.sendToAll(new PacketUpdateEnergy(this));
        }
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack old, ItemStack add) {
        if (Platform.isServer()) {
            if (iInventory == upgradeFotonInv) {
                if (old == null && add != null && add.getItem() instanceof ItemSunUpgrade) {
                    alwaysDay = true;
                } else if (old != null && add == null) {
                    alwaysDay = false;
                }
            } else if (iInventory == upgradeInv) {
                maxEnergy = 1.0E8;
                if (upgradeInv.getStackInSlot(0) != null && upgradeInv.getStackInSlot(0).getItem() instanceof ItemEUEnergyUpgrades && upgradeInv.getStackInSlot(0).getItemDamage() >= 0 && upgradeInv.getStackInSlot(0).getItemDamage() <= 9) {
                    boost = euEnergyUpgrades[upgradeInv.getStackInSlot(0).getItemDamage()] / 100;
                    maxEnergy += maxEnergy * boost * 1000;
                } else {
                    boost = 0;
                }
            } else if (iInventory == inv) {
                dayGen = 0;
                nightGen = 0;
                if (i >= 0) {
                    for (int j = 0; j < inv.getSizeInventory(); j++) {
                        ItemStack stack = inv.getStackInSlot(j);
                        if (stack != null) {
                            if (stack.getItem() == Ic2Items.solarPanel.getItem()) {
                                dayGen += stack.stackSize;
                            } else if (Block.getBlockFromItem(stack.getItem()) instanceof ISpecialSintezatorPanel) {
                                dayGen += ((ISpecialSintezatorPanel) Block.getBlockFromItem(stack.getItem())).getDayGen(stack) * stack.stackSize;
                                nightGen += ((ISpecialSintezatorPanel) Block.getBlockFromItem(stack.getItem())).getNightGen(stack) * stack.stackSize;
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkSun() {
        Boolean rainWeather = super.worldObj.getWorldChunkManager().getBiomeGenAt(super.xCoord, super.zCoord).getIntRainfall() > 0 && (super.worldObj.isRaining() || super.worldObj.isThundering());
        if (super.worldObj.isDaytime() && !rainWeather) {
            this.sunIsUp = true;
        } else {
            this.sunIsUp = false;
        }
    }


    public final double getEnergy() {
        return this.energy;
    }

    public final void setEnergy(double value) {
        this.energy = value;
    }

    @Override
    public IInventory getInternalInventory() {
        return inv;
    }

    @Override
    public String getInventoryName() {
        return "Sintezator";
    }

    @Override
    public int[] getAccessibleSlotsBySide(ForgeDirection forgeDirection) {
        return new int[0];
    }


    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return true;
    }

    @Override
    public double getOfferedEnergy() {
        return this.energy;
    }

    @Override
    public void drawEnergy(double amount) {
        if (amount >= this.energy) {
            this.energy = 0.0;
        } else {
            this.energy -= amount;
        }
    }

    @Override
    public int getSourceTier() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 0;
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_(NBTTagCompound data) {
        inv.writeToNBT(data, "inv");
        upgradeFotonInv.writeToNBT(data, "upgradeFotonInv");
        upgradeInv.writeToNBT(data, "upgradeInv");
        data.setBoolean("day", alwaysDay);
        data.setDouble("maxEnergy", maxEnergy);
        data.setDouble("energy", energy);
        data.setDouble("dayGen", dayGen);
        data.setDouble("nightGen", nightGen);
        data.setDouble("boost", boost);
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_(NBTTagCompound data) {
        inv.readFromNBT(data, "inv");
        upgradeFotonInv.readFromNBT(data, "upgradeFotonInv");
        upgradeInv.readFromNBT(data, "upgradeInv");
        alwaysDay = data.getBoolean("day");
        maxEnergy = data.getDouble("maxEnergy");
        energy = data.getDouble("energy");
        dayGen = data.getDouble("dayGen");
        nightGen = data.getDouble("nightGen");
        boost = data.getDouble("boost");
    }

    public AppEngInternalInventory getInv() {
        return inv;
    }

    public AppEngInternalInventory getUpgradeFotonInv() {
        return upgradeFotonInv;
    }

    public AppEngInternalInventory getUpgradeInv() {
        return upgradeInv;
    }

    public double getDayGen() {
        return dayGen + boost * dayGen;
    }

    public double getNightGen() {
        return nightGen + boost * nightGen;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    public boolean isSunIsUp() {
        return sunIsUp;
    }

    @Override
    public DimensionalCoord getCoord() {
        return new DimensionalCoord((TileEntity)this);
    }

    @Override
    public void setEnergyStoredForUpdate(double paramDouble) {
        this.energy = paramDouble;
    }

    @Override
    public double getEnergyStoredForUpdate() {
        return this.energy;
    }

    @Override
    public void setMaxEnergyStoredForUpdate(double paramDouble) {
        this.maxEnergy = paramDouble;
    }

    @Override
    public double getMaxEnergyStoredForUpdate() {
        return this.maxEnergy;
    }

    @Override
    public double extractEU(double paramDouble, Actionable paramActionable) {
        double ret = 0;
        if (energy >= paramDouble) {
            ret = paramDouble;
        } else if (energy > 0 && energy < paramDouble) {
            ret = energy;
        }
        if (paramActionable == Actionable.MODULATE) {
            energy -= ret;
        }
        if (Platform.isServer()) {
            NetworkManager.instance.sendToAll(new PacketUpdateEnergy(this));
        }
        return ret;
    }

    public void validate() {
        super.validate();
        this.onLoaded();
    }

    public void invalidate() {
        if (this.loaded) {
            this.onUnloaded();
        }

        super.invalidate();
    }

    public void onLoaded() {
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        this.addedToEnergyNet = true;
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
}