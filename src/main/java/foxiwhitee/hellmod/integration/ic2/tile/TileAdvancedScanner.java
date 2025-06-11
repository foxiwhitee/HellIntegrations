package foxiwhitee.hellmod.integration.ic2.tile;

import foxiwhitee.hellmod.config.HellConfig;
import ic2.api.Direction;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.recipe.IPatternStorage;
import ic2.core.Ic2Items;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.invslot.InvSlotConsumableId;
import ic2.core.block.invslot.InvSlotScannable;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.item.ItemCrystalMemory;
import ic2.core.util.StackUtil;
import ic2.core.uu.UuGraph;
import ic2.core.uu.UuIndex;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileAdvancedScanner extends TileEntityElectricMachine implements INetworkClientTileEntityEventListener {
    private ItemStack currentStack = null;
    private ItemStack pattern = null;
    private final int energyusecycle = 256;
    public int progress = 0;
    public final InvSlotConsumable inputSlot;
    public final InvSlot diskSlot;
    private TileAdvancedScanner.State state;
    public double patternUu;
    public double patternEu;

    private int duration;
    public TileAdvancedScanner() {
        super(1024000, 3, 0);
        this.duration = HellConfig.advancedScannerDuration;
        this.state = TileAdvancedScanner.State.IDLE;
        this.inputSlot = new InvSlotScannable(this, "input", 0, 1);
        this.diskSlot = new InvSlotConsumableId(this, "disk", 0, InvSlot.Access.IO, 1, InvSlot.InvSide.ANY, new Item[]{Ic2Items.crystalmemory.getItem()});
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean newActive = false;
        if (this.progress < duration) {
            if (!this.inputSlot.isEmpty() && (this.currentStack == null || StackUtil.isStackEqual(this.currentStack, this.inputSlot.get()))) {
                if (this.getPatternStorage() == null && this.diskSlot.isEmpty()) {
                    this.state = TileAdvancedScanner.State.NO_STORAGE;
                    this.reset();
                } else if (this.energy >= (double)energyusecycle) {
                    if (this.currentStack == null) {
                        this.currentStack = StackUtil.copyWithSize(this.inputSlot.get(), 1);
                    }

                    this.pattern = UuGraph.find(this.currentStack);
                    if (this.pattern == null) {
                        this.state = TileAdvancedScanner.State.FAILED;
                    } else if (this.isPatternRecorded(this.pattern)) {
                        this.state = TileAdvancedScanner.State.ALREADY_RECORDED;
                        this.reset();
                    } else {
                        newActive = true;
                        this.state = TileAdvancedScanner.State.SCANNING;
                        this.energy -= (double)energyusecycle;
                        ++this.progress;
                        if (this.progress >= duration) {
                            this.refreshInfo();
                            if (this.patternUu != Double.POSITIVE_INFINITY) {
                                this.state = TileAdvancedScanner.State.COMPLETED;
                                this.inputSlot.consume(1, false, true);
                                this.markDirty();
                            } else {
                                this.state = TileAdvancedScanner.State.FAILED;
                            }
                        }
                    }
                } else {
                    this.state = TileAdvancedScanner.State.NO_ENERGY;
                }
            } else {
                this.state = TileAdvancedScanner.State.IDLE;
                this.reset();
            }
        } else if (this.pattern == null) {
            this.state = TileAdvancedScanner.State.IDLE;
            this.progress = 0;
        }

        this.setActive(newActive);
    }

    public void reset() {
        this.progress = 0;
        this.currentStack = null;
        this.pattern = null;
    }

    private boolean isPatternRecorded(ItemStack stack) {
        if (!this.diskSlot.isEmpty() && this.diskSlot.get().getItem() instanceof ItemCrystalMemory) {
            ItemStack crystalMemory = this.diskSlot.get();
            if (StackUtil.isStackEqual(((ItemCrystalMemory)crystalMemory.getItem()).readItemStack(crystalMemory), stack)) {
                return true;
            }
        }

        IPatternStorage storage = this.getPatternStorage();
        if (storage == null) {
            return false;
        } else {
            for(ItemStack stored : storage.getPatterns()) {
                if (StackUtil.isStackEqual(stored, stack)) {
                    return true;
                }
            }

            return false;
        }
    }

    private void record() {
        if (this.pattern != null && this.patternUu != Double.POSITIVE_INFINITY) {
            if (!this.savetoDisk(this.pattern)) {
                IPatternStorage storage = this.getPatternStorage();
                if (storage == null) {
                    this.state = TileAdvancedScanner.State.TRANSFER_ERROR;
                    return;
                }

                if (!storage.addPattern(this.pattern)) {
                    this.state = TileAdvancedScanner.State.TRANSFER_ERROR;
                    return;
                }
            }

            this.reset();
        } else {
            this.reset();
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInteger("progress");
        NBTTagCompound contentTag = nbttagcompound.getCompoundTag("currentStack");
        this.currentStack = ItemStack.loadItemStackFromNBT(contentTag);
        contentTag = nbttagcompound.getCompoundTag("pattern");
        this.pattern = ItemStack.loadItemStackFromNBT(contentTag);
        int stateIdx = nbttagcompound.getInteger("state");
        this.state = stateIdx < TileAdvancedScanner.State.values().length ? TileAdvancedScanner.State.values()[stateIdx] : TileAdvancedScanner.State.IDLE;
        this.refreshInfo();
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("progress", this.progress);
        if (this.currentStack != null) {
            NBTTagCompound contentTag = new NBTTagCompound();
            this.currentStack.writeToNBT(contentTag);
            nbttagcompound.setTag("currentStack", contentTag);
        }

        if (this.pattern != null) {
            NBTTagCompound contentTag = new NBTTagCompound();
            this.pattern.writeToNBT(contentTag);
            nbttagcompound.setTag("pattern", contentTag);
        }

        nbttagcompound.setInteger("state", this.state.ordinal());
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

    public String getInventoryName() {
        return "scanner";
    }

    public IPatternStorage getPatternStorage() {
        for(Direction direction : Direction.directions) {
            TileEntity target = direction.applyToTileEntity(this);
            if (target instanceof IPatternStorage) {
                return (IPatternStorage)target;
            }
        }

        return null;
    }

    public boolean savetoDisk(ItemStack stack) {
        if (!this.diskSlot.isEmpty() && stack != null) {
            if (this.diskSlot.get().getItem() instanceof ItemCrystalMemory) {
                ItemStack crystalMemory = this.diskSlot.get();
                ((ItemCrystalMemory)crystalMemory.getItem()).writecontentsTag(crystalMemory, stack);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void onNetworkEvent(EntityPlayer player, int event) {
        switch (event) {
            case 0:
                this.reset();
                break;
            case 1:
                if (this.progress >= duration) {
                    this.record();
                }
        }

    }

    private void refreshInfo() {
        if (this.pattern != null) {
            this.patternUu = UuIndex.instance.getInBuckets(this.pattern);
        }

    }

    public int getPercentageDone() {
        return 100 * this.progress / duration;
    }

    public int getProgress() {
        return progress;
    }

    public boolean isDone() {
        return this.progress >= duration;
    }

    public TileAdvancedScanner.State getState() {
        return this.state;
    }

    public static enum State {
        IDLE,
        SCANNING,
        COMPLETED,
        FAILED,
        NO_STORAGE,
        NO_ENERGY,
        TRANSFER_ERROR,
        ALREADY_RECORDED;

        private State() {
        }
    }
}
