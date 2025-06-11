package foxiwhitee.hellmod.tile.assemblers;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.events.MENetworkEvent;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.container.ContainerNull;
import appeng.me.GridAccessException;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import foxiwhitee.hellmod.helpers.IInterfaceTerminalSupport;
import foxiwhitee.hellmod.tile.TileNetworkInvA;
import foxiwhitee.hellmod.utils.craft.ICraftingCPUClusterAccessor;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileCustomMolecularAssembler extends TileNetworkInvA implements IPowerChannelState, IInterfaceTerminalSupport {
    private static final int[] SIDES = {0};
    private final AppEngInternalInventory patternInventory = new AppEngInternalInventory(this, 36);
    private ICraftingPatternDetails activePattern;
    private long craftCount;
    private InventoryCrafting craftingGrid;
    private List<ICraftingPatternDetails> patternList;
    private boolean isPowered;

    protected abstract ItemStack getItemFromTile(Object obj);

    public TileCustomMolecularAssembler() {
        getProxy().setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(1, 1, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        if (activePattern == null) return TickRateModulation.IDLE;
        List<IAEItemStack> outputs = new ArrayList<>();
        Arrays.stream(activePattern.getCondensedOutputs())
                .map(stack -> {
                    IAEItemStack copy = stack.copy();
                    copy.setStackSize(copy.getStackSize() * craftCount);
                    return copy;
                }).forEach(outputs::add);
        for (int i = 0; i < craftingGrid.getSizeInventory(); i++) {
            ItemStack item = Platform.getContainerItem(craftingGrid.getStackInSlot(i));
            if (item != null) {
                outputs.add(AEItemStack.create(item));
            }
        }
        boolean canCraft = true;
        for (IAEItemStack output : outputs) {
            try {
                IAEItemStack remainder = getProxy().getStorage().getItemInventory()
                        .injectItems(output.copy(), Actionable.SIMULATE, new MachineSource(this));
                if (remainder != null && remainder.getStackSize() > 0) {
                    canCraft = false;
                    break;
                }
            } catch (GridAccessException e) {
                canCraft = false;
                break;
            }
        }
        if (canCraft) {
            for (IAEItemStack output : outputs) {
                try {
                    getProxy().getStorage().getItemInventory()
                            .injectItems(output.copy(), Actionable.MODULATE, new MachineSource(this));
                } catch (GridAccessException ignored) {}
            }
            activePattern = null;
            craftCount = 0;
        }
        return TickRateModulation.IDLE;
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper helper) {
        if (getProxy().isActive() && patternList != null) {
            patternList.stream()
                    .filter(ICraftingPatternDetails::isCraftable)
                    .forEach(pattern -> helper.addCraftingOption(this, pattern));
        }
    }

    public boolean pushPattern(ICraftingPatternDetails pattern, InventoryCrafting grid, CraftingCPUCluster cluster) {
        if (patternList == null || !patternList.contains(pattern) || !pattern.isCraftable()) return false;
        ICraftingCPUClusterAccessor accessor = (ICraftingCPUClusterAccessor)((Object) cluster);
        long required = accessor.getWaitingFor(pattern) - 1;
        long actualRequired = required + 1;
        required = Math.min(required, getMaxCount());
        IMEInventory<IAEItemStack> inventory = cluster.getInventory();
        for (IAEItemStack input : pattern.getCondensedInputs()) {
            IAEItemStack copy = input.copy();
            copy.setStackSize(copy.getStackSize() * required);
            IAEItemStack extracted = inventory.extractItems(copy, Actionable.SIMULATE, cluster.getActionSource());
            long available = extracted == null ? 0 : extracted.getStackSize();
            if (copy.getStackSize() > available) {
                required = available / input.getStackSize();
            }
        }
        if (required < 0) return false;
        if (required >= 1) {
            for (IAEItemStack input : pattern.getCondensedInputs()) {
                IAEItemStack copy = input.copy();
                copy.setStackSize(copy.getStackSize() * required);
                inventory.extractItems(copy, Actionable.MODULATE, cluster.getActionSource());
            }
            for (IAEItemStack output : pattern.getCondensedOutputs()) {
                IAEItemStack copy = output.copy();
                copy.setStackSize(copy.getStackSize() * required);
                accessor.callPostChange(copy, cluster.getActionSource());
                accessor.getWaitingFor().add(copy.copy());
                accessor.callPostCraftingStatusChange(copy.copy());
            }
            accessor.setWaitingFor(pattern, actualRequired - required);
        }
        activePattern = pattern;
        craftCount = required + 1;
        craftingGrid = grid;
        return true;
    }

    @Override
    public void onReady() {
        super.onReady();
        updatePatternList();
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_MA(NBTTagCompound compound) {
        if (this.craftingGrid != null) {
            compound.setInteger("invC_size", this.craftingGrid.getSizeInventory());
            for (int i = 0; i < this.craftingGrid.getSizeInventory(); i++) {
                NBTTagCompound tag = new NBTTagCompound();
                ItemStack is = this.craftingGrid.getStackInSlot(i);
                if (is != null)
                    is.writeToNBT(tag);
                compound.setTag("invC_" + i, (NBTBase)tag);
            }
        }
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_MA(NBTTagCompound compound) {
        if (compound.hasKey("invC")) {
            int size = compound.getInteger("invC_size");
            this.craftingGrid = new InventoryCrafting((Container)new ContainerNull(), size, 1);
            for (int i = 0; i < size; i++) {
                NBTTagCompound tag = compound.getCompoundTag("invC_" + i);
                if (!tag.hasNoTags())
                    try {
                        this.craftingGrid.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tag));
                    } catch (Exception exception) {}
            }
        }
        updatePatternList();
    }

    private void addPattern(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ICraftingPatternItem)) return;
        ICraftingPatternDetails pattern = ((ICraftingPatternItem)stack.getItem()).getPatternForItem(stack, worldObj);
        if (pattern != null) {
            if (patternList == null) {
                patternList = new LinkedList<>();
            }
            patternList.add(pattern);
        }
    }

    private void updatePatternList() {
        if (!getProxy().isReady()) return;
        Boolean[] tracked = new Boolean[36];
        Arrays.fill(tracked, false);
        if (patternList != null) {
            patternList.removeIf(pattern -> {
                for (int i = 0; i < patternInventory.getSizeInventory(); i++) {
                    if (pattern.getPattern() == patternInventory.getStackInSlot(i)) {
                        tracked[i] = true;
                        return false;
                    }
                }
                return true;
            });
        }
        for (int i = 0; i < tracked.length; i++) {
            if (!tracked[i]) {
                addPattern(patternInventory.getStackInSlot(i));
            }
        }
        try {
            getProxy().getGrid().postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
        } catch (GridAccessException ignored) {}
    }

    @Override
    public boolean isBusy() {
        return activePattern != null;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection dir) {
        return AECableType.SMART;
    }

    @Override
    public IInventory getInternalInventory() {
        return patternInventory;
    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InvOperation op, ItemStack removed, ItemStack added) {
        if (inv == patternInventory) {
            updatePatternList();
        }
    }

    @Override
    public int[] getAccessibleSlotsBySide(ForgeDirection dir) {
        return SIDES;
    }

    protected abstract long getMaxCount();

    @MENetworkEventSubscribe
    public void onNetworkChange(MENetworkEvent event) {
        try {
            getProxy().getGrid().postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
        } catch (GridAccessException ignored) {}
    }

    @MENetworkEventSubscribe
    public void onPowerStatusChange(MENetworkPowerStatusChange event) {
        boolean newState;
        try {
            newState = getProxy().isActive() &&
                    getProxy().getEnergy().extractAEPower(getPower(), Actionable.SIMULATE, PowerMultiplier.CONFIG) > 0.0001;
        } catch (GridAccessException e) {
            newState = false;
        }
        if (newState != isPowered) {
            isPowered = newState;
            markForUpdate();
        }
    }

    @Override
    public boolean isPowered() {
        return isPowered;
    }

    @Override
    public boolean isActive() {
        return isPowered;
    }

    protected abstract double getPower();

    @Override
    public boolean pushPattern(ICraftingPatternDetails details, InventoryCrafting ic, ForgeDirection direction) {
        return false;
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails iCraftingPatternDetails, InventoryCrafting inventoryCrafting) {
        return false;
    }

    public IInterfaceTerminalSupport.PatternsConfiguration[] getPatternsConfigurations() {
        return new IInterfaceTerminalSupport.PatternsConfiguration[] { new IInterfaceTerminalSupport.PatternsConfiguration(0, 9), new IInterfaceTerminalSupport.PatternsConfiguration(9, 9), new IInterfaceTerminalSupport.PatternsConfiguration(18, 9), new IInterfaceTerminalSupport.PatternsConfiguration(27, 9) };
    }

    public IInventory getPatterns(int paramInt) {
        return this.patternInventory;
    }

    public TileEntity getTileEntity() {
        return this;
    }

}