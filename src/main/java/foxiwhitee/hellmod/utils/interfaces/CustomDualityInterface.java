package foxiwhitee.hellmod.utils.interfaces;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.helpers.DualityInterface;
import appeng.helpers.IInterfaceHost;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class CustomDualityInterface extends DualityInterface implements ICustomDualityInterface {
    private final AppEngInternalInventory patternInventory = new AppEngInternalInventory(this, getNumberOfSlots());

    public int getNumberOfSlots() {
        switch (getLevel()) {
            case "advanced": return CustomAdvancedDualityInterface.getNumberOfPatternSlots();
            case "hybrid": return CustomHybridDualityInterface.getNumberOfPatternSlots();
            case "ultimate": return CustomUltimateDualityInterface.getNumberOfPatternSlots();
            default: return  NUMBER_OF_PATTERN_SLOTS;
        }
    }

    protected abstract String getLevel();

    public CustomDualityInterface(AENetworkProxy proxy, IInterfaceHost host) {
        super(proxy, host);
    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InvOperation op, ItemStack removed, ItemStack added) {
        if (getIsWorking()) return;
        if (inv == getConfig()) {
            this.callReadConfig();
        } else if (inv == patternInventory && (removed != null || added != null)) {
            this.callUpdateCraftingList();
        } else if (inv == getStorage() && slot >= 0) {
            boolean hadWork = this.callHasWorkToDo();
            this.callUpdatePlan(slot);
            boolean hasWork = this.callHasWorkToDo();
            if (hadWork != hasWork) {
                try {
                    if (hasWork) {
                        getProxy().getTick().alertDevice(getProxy().getNode());
                    } else {
                        getProxy().getTick().sleepDevice(getProxy().getNode());
                    }
                } catch (GridAccessException ignored) {}
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        patternInventory.writeToNBT(data, "customPatterns");
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        patternInventory.readFromNBT(data, "customPatterns");
    }

    @Override
    public AppEngInternalInventory getPatterns() {
        return patternInventory;
    }

    @Override
    public IInventory getInventoryByName(String name) {
        return name.equals("patterns") ? patternInventory : super.getInventoryByName(name);
    }

    @Override
    public void addDrops(List<ItemStack> drops) {
        for (ItemStack stack : patternInventory) {
            if (stack != null) {
                drops.add(stack);
            }
        }
        super.addDrops(drops);
    }

    @Override
    public boolean isOverrideDefault() {
        return true;
    }

    @Override
    public void updateCraftingListProxy() {
        if (!getProxy().isReady()) return;
        Boolean[] tracked = new Boolean[getNumberOfSlots()];
        Arrays.fill(tracked, false);
        if (getCraftingList() != null) {
            getCraftingList().removeIf(pattern -> {
                for (int i = 0; i < getNumberOfSlots(); i++) {
                    if (pattern.getPattern() == patternInventory.getStackInSlot(i)) {
                        tracked[i] = true;
                        return false;
                    }
                }
                return true;
            });
        }
        for (int i = 0; i < getNumberOfSlots(); i++) {
            if (!tracked[i]) {
                this.callAddToCraftingList(patternInventory.getStackInSlot(i));
            }
        }
        try {
            getProxy().getGrid().postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
        } catch (GridAccessException ignored) {}
    }
}
