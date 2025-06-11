package foxiwhitee.hellmod.integration.ic2.tile.replicators;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingMedium;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.events.MENetworkEvent;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.me.GridAccessException;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.util.item.AEFluidStack;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.tile.TileNetwork;
import foxiwhitee.hellmod.utils.craft.ICraftingCPUClusterAccessor;
import foxiwhitee.hellmod.utils.craft.IPreCraftingMedium;
import ic2.api.recipe.IPatternStorage;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import ic2.core.uu.UuIndex;
import io.netty.buffer.ByteBuf;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class TileAutoReplicator extends TileNetwork implements IPreCraftingMedium {
    private boolean needUpdate = true;
    private ICraftingPatternDetails details;
    private long count;
    private SingularPattern patternTest;
    private IPatternStorage patternStorage;

    public TileAutoReplicator() {
        getProxy().setFlags(new GridFlags[] { GridFlags.REQUIRE_CHANNEL });
    }

    public void provideCrafting(ICraftingProviderHelper iCraftingProviderHelper) {
        if (this.patternStorage != null) {
            List<ItemStack> patterns = this.patternStorage.getPatterns();
            for (ItemStack stack : patterns) {
                ItemStack stack1 = stack.copy();
                stack1.stackSize = 10;
                iCraftingProviderHelper.addCraftingOption((ICraftingMedium)this, new SingularPattern(AEItemStack.create(stack1)));
            }
        }
    }

    public void updateCraftingList() {
        try {
            getProxy().getGrid().postEvent((MENetworkEvent)new MENetworkCraftingPatternChange(this, getProxy().getNode()));
            this.needUpdate = false;
        } catch (GridAccessException e) {
            e.printStackTrace();
        }
    }

    public TickRateModulation tickingRequest(IGridNode iGridNode, int i) {
        if (this.worldObj.isRemote)
            return TickRateModulation.IDLE;
        if (this.needUpdate) {
            this.patternStorage = getPatternStorage();
            updateCraftingList();
            totalUpdate();
            return TickRateModulation.URGENT;
        }
        if (this.patternStorage == null)
            return TickRateModulation.SLEEP;

        if (this.details != null && this.patternTest != null) {
            List<IAEItemStack> outputs = new ArrayList<>();
            Objects.requireNonNull(outputs);
            Arrays.<IAEItemStack>stream(this.details.getCondensedOutputs()).map(e -> {
                IAEItemStack stack = e.copy();
                stack.setStackSize(stack.getStackSize() * this.count);
                return stack;
            }).forEach(outputs::add);
            boolean can = true;
            for (IAEItemStack output : outputs) {
                IAEItemStack out = output.copy();
                out.setStackSize(out.getStackSize() / 10L);
                try {
                    IAEItemStack is = (IAEItemStack)getProxy().getStorage().getItemInventory().injectItems((IAEItemStack) out, Actionable.SIMULATE, (BaseActionSource)new MachineSource((IActionHost)this));
                    if (is != null && is.getStackSize() > 0L) {
                        can = false;
                        break;
                    }
                } catch (GridAccessException e) {
                    can = false;
                    break;
                }
            }

            if (can) {
                for (IAEItemStack output : outputs) {
                    IAEItemStack out = output.copy();
                    out.setStackSize(out.getStackSize() / 10L);
                    try {
                        IStorageGrid storageGrid = getProxy().getStorage();
                        Fluid fluid = BlocksItems.getFluid(InternalName.fluidUuMatter);
                        double amount = getAmount();
                        IAEFluidStack extracted = (IAEFluidStack)storageGrid.getFluidInventory().extractItems((IAEFluidStack) AEFluidStack.create(new FluidStack(fluid, (int)amount)), Actionable.SIMULATE, (BaseActionSource)new MachineSource((IActionHost)this));
                        if (extracted == null || extracted.getStackSize() < (int)amount)
                            return TickRateModulation.IDLE;
                        getProxy().getStorage().getItemInventory().injectItems((IAEItemStack) out, Actionable.MODULATE, (BaseActionSource)new MachineSource((IActionHost)this));
                        storageGrid.getFluidInventory().extractItems((IAEFluidStack)AEFluidStack.create(new FluidStack(fluid, (int)amount)), Actionable.MODULATE, (BaseActionSource)new MachineSource((IActionHost)this));
                    } catch (GridAccessException gridAccessException) {}
                }
                this.details = null;
                this.count = 0L;
                this.isBusy = false;
            }
        }
        markForUpdate();
        return TickRateModulation.IDLE;
    }

    private double getAmount() {
        double amount = count * UuIndex.instance.getInBuckets(this.patternTest.getOutputs()[0].getItemStack()) * 1000.0D;
        amount -= (this.getDiscount() * amount / 100.0D);
        if (amount < 0) {
            amount = 0;
        }
        return amount;
    }

    public boolean pushPattern(ICraftingPatternDetails iCraftingPatternDetails, InventoryCrafting inventoryCrafting) {
        return false;
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails details, InventoryCrafting ic, CraftingCPUCluster cluster) {
        this.patternTest = (SingularPattern)details;
        if (this.patternTest == null || this.patternTest != details || details.isCraftable() || isBusy() || !(details instanceof SingularPattern))
            return false;
        this.isBusy = true;
        markForUpdate();
        totalUpdate();
        wakeDevice();
        ICraftingCPUClusterAccessor accessor = (ICraftingCPUClusterAccessor)((Object)cluster);
        long need = accessor.getWaitingFor(details) - 1L;
        long realNeed = need + 1L;
        need = Math.min(need, getMaxCount());
        IMEInventory<IAEItemStack> inventory = cluster.getInventory();
        for (IAEItemStack stack : details.getCondensedInputs()) {
            IAEItemStack copy = stack.copy();
            long nSize = copy.getStackSize() * need;
            copy.setStackSize(nSize);
            IAEItemStack extracted = (IAEItemStack)inventory.extractItems((IAEItemStack) copy, Actionable.SIMULATE, cluster.getActionSource());
            long size = (extracted == null) ? 0L : extracted.getStackSize();
            if (nSize > size)
                need = size / stack.getStackSize();
        }
        if (need < 0L)
            return false;
        if (need >= 1L) {
            for (IAEItemStack stack : details.getCondensedInputs()) {
                IAEItemStack copy = stack.copy();
                long nSize = copy.getStackSize() * need;
                copy.setStackSize(nSize);
                inventory.extractItems((IAEItemStack) copy, Actionable.MODULATE, cluster.getActionSource());
            }
            for (IAEItemStack stack : details.getCondensedOutputs()) {
                IAEItemStack copy = stack.copy();
                copy.setStackSize(copy.getStackSize() * need);
                accessor.callPostChange(copy, cluster.getActionSource());
                accessor.getWaitingFor().add((IAEItemStack) copy.copy());
                accessor.callPostCraftingStatusChange(copy.copy());
            }
            accessor.setWaitingFor(details, realNeed - need);
        }
        this.details = details;

        this.count = need + 1L;
        return true;
    }

    public long getMaxCount() {
        long i = this.getItemsPerSec();
        return this.getItemsPerSec() - 1L;
    }

    protected abstract ItemStack getItemFromTile(Object obj);

    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(1, 1, false, false);
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeNBT(NBTTagCompound tag) {
        tag.setBoolean("isBusy", this.isBusy);
        if (this.isBusy && this.patternTest != null) {
            AEItemStack stack = (AEItemStack)this.patternTest.getCondensedOutputs()[0];
            NBTTagCompound tagCompound = new NBTTagCompound();
            stack.writeToNBT(tagCompound);
            tag.setTag("stack", (NBTBase)tagCompound);
        }
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readNBT(NBTTagCompound tag) {
        this.isBusy = tag.getBoolean("isBusy");
        if (tag.hasKey("stack")) {
            AEItemStack itemStack = (AEItemStack)AEItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack"));
            this.patternTest = new SingularPattern(itemStack);
            this.isBusy = true;
        }
    }

    protected void totalUpdate() {
        saveChanges();
        markDirty();
        markForUpdate();
    }

    public IPatternStorage getPatternStorage() {
        for (EnumFacing facing : EnumFacing.values()) {
            TileEntity tileEntity = this.worldObj.getTileEntity(this.xCoord + facing.getFrontOffsetX(), this.yCoord + facing
                    .getFrontOffsetY(), this.zCoord + facing
                    .getFrontOffsetZ());
            if (tileEntity instanceof IPatternStorage)
                return (IPatternStorage)tileEntity;
        }
        return null;
    }

    public void wakeDevice() {
        try {
            getProxy().getTick().wakeDevice(getProxy().getNode());
        } catch (GridAccessException gridAccessException) {}
    }

    public void sleepDevice() {
        try {
            getProxy().getTick().sleepDevice(getProxy().getNode());
        } catch (GridAccessException gridAccessException) {}
    }

    public void setUpdate(boolean bool) {
        this.needUpdate = bool;
        wakeDevice();
    }

    @TileEvent(TileEventType.NETWORK_WRITE)
    public void writeToClient(ByteBuf data) {
        try {
            data.writeBoolean(isBusy);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @TileEvent(TileEventType.NETWORK_READ)
    public boolean readFromClient(ByteBuf data) {
        boolean result = false;
        try {
            boolean oldBusy = isBusy;
            isBusy = data.readBoolean();
            result = oldBusy != isBusy ? true : false;
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return result;
    }

    public abstract String getName();

    public abstract byte getDiscount();

    public abstract long getItemsPerSec();

    public static class SingularPattern implements ICraftingPatternDetails {
        private final AEItemStack output;

        public SingularPattern(AEItemStack output) {
            this.output = output;
        }

        public ItemStack getPattern() {
            return null;
        }

        public boolean isValidItemForSlot(int i, ItemStack itemStack, World world) {
            return false;
        }

        public boolean isCraftable() {
            return false;
        }

        public IAEItemStack[] getInputs() {
            return new IAEItemStack[] { (IAEItemStack)AEItemStack.create(new ItemStack(Blocks.cobblestone)) };
        }

        public IAEItemStack[] getCondensedInputs() {
            return new IAEItemStack[] { (IAEItemStack)AEItemStack.create(new ItemStack(Blocks.cobblestone)) };
        }

        public IAEItemStack[] getCondensedOutputs() {
            return new IAEItemStack[] { (IAEItemStack)this.output };
        }

        public IAEItemStack[] getOutputs() {
            return new IAEItemStack[] { (IAEItemStack)this.output };
        }

        public boolean canSubstitute() {
            return false;
        }

        public ItemStack getOutput(InventoryCrafting inventoryCrafting, World world) {
            return this.output.getItemStack();
        }

        public int getPriority() {
            return -9999;
        }

        public void setPriority(int i) {}
    }
}
