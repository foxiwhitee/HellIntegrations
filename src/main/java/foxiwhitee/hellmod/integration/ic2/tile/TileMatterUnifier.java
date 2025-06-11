package foxiwhitee.hellmod.integration.ic2.tile;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.tile.AEBaseInvTile;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkInvTile;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import appeng.util.item.AEFluidStack;
import cpw.mods.fml.common.eventhandler.Event;
import foxiwhitee.hellmod.integration.ic2.IC2Integration;
import foxiwhitee.hellmod.integration.ic2.helpers.IMatter;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class TileMatterUnifier extends AENetworkInvTile implements ISaveProvider, IGridTickable, IEnergySink {
    public static final int[] slots = new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
            30, 31, 32, 33, 34, 35 };


    private final AppEngInternalInventory inventory = new AppEngInternalInventory((IAEAppEngInventory)this, 36);

    private final BaseActionSource source = (BaseActionSource)new MachineSource((IActionHost)this);

    private boolean addedToEnergyNetwork = false;

    private double energyStored = 0.0D;

    private double maxEnergyStored = 100000000;

    private int matterGeneration = 0;

    private double needEnergyForOperation = 0.0D;

    public TileMatterUnifier() {
        getProxy().setFlags(new GridFlags[] { GridFlags.REQUIRE_CHANNEL });
        getProxy().setIdlePowerUsage(1.0D);
    }

    @Nullable
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(IC2Integration.matterUnifier);
    }

    public IInventory getInternalInventory() {
        return (IInventory)this.inventory;
    }

    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {
        if (Platform.isServer()) {
            if (iInventory == inventory) {
                needEnergyForOperation = 0;
                for (int j = 0; j < inventory.getSizeInventory(); j++) {
                    if (inventory.getStackInSlot(j) != null) {
                        ItemStack stack = inventory.getStackInSlot(j);
                        Block b = Block.getBlockFromItem(stack.getItem());
                        if (stack.getItem() == Ic2Items.massFabricator.getItem()) {
                            matterGeneration += stack.stackSize;
                        } else if (b instanceof IMatter) {
                            matterGeneration += ((IMatter)b).getFluid().amount;
                        }
                        needEnergyForOperation += 1024 * stack.stackSize;
                    }
                }
            }
        }
    }

    public int[] getAccessibleSlotsBySide(ForgeDirection forgeDirection) {
        return slots;
    }

    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(1, 1, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode iGridNode, int i) {
        if (this.energyStored >= needEnergyForOperation) {
            insertMatterToME(matterGeneration);
            this.energyStored -= needEnergyForOperation;
        }
        return TickRateModulation.IDLE;
    }

    private long insertMatterToME(long count) {
        AEFluidStack aEFluidStack = AEFluidStack.create(new FluidStack(BlocksItems.getFluid(InternalName.fluidUuMatter), 1));
        aEFluidStack.setStackSize(count);
        try {
            IMEMonitor<IAEFluidStack> monitor = getProxy().getStorage().getFluidInventory();
            if (monitor == null)
                return count;
            IAEFluidStack rejected = (IAEFluidStack)monitor.injectItems((IAEFluidStack) aEFluidStack, Actionable.MODULATE, this.source);
            if (rejected == null)
                return 0L;
            return rejected.getStackSize();
        } catch (GridAccessException gridAccessException) {
            return count;
        }
    }

    private void addToEnergyNetwork() {
        if (!this.addedToEnergyNetwork && IC2.platform.isSimulating())
            MinecraftForge.EVENT_BUS.post((Event)new EnergyTileLoadEvent((IEnergyTile)this));
    }

    private void removeFromEnergyNetwork() {
        if (this.addedToEnergyNetwork && IC2.platform.isSimulating())
            MinecraftForge.EVENT_BUS.post((Event)new EnergyTileUnloadEvent((IEnergyTile)this));
    }

    public void onReady() {
        super.onReady();
        addToEnergyNetwork();
    }

    public void invalidate() {
        super.invalidate();
        removeFromEnergyNetwork();
    }

    public void validate() {
        super.validate();
        addToEnergyNetwork();
    }

    public DimensionalCoord getLocation() {
        return new DimensionalCoord((TileEntity)this);
    }

    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }

    public void saveChanges(IMEInventory imeInventory) {
        markDirty();
    }

    public double getDemandedEnergy() {
        return this.maxEnergyStored - this.energyStored;
    }

    public int getSinkTier() {
        return 16;
    }

    public double injectEnergy(ForgeDirection forgeDirection, double v, double v1) {
        double canInsert = Math.min(v, this.maxEnergyStored - this.energyStored);
        this.energyStored += canInsert;
        return v - canInsert;
    }

    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        return true;
    }

    public boolean isItemValidForSlot(int i, ItemStack stack) {
        if (stack == null)
            return true;
        Block b = Block.getBlockFromItem(stack.getItem());
        return (b instanceof IMatter || stack.getItem() == Ic2Items.massFabricator.getItem());
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_TMU(NBTTagCompound tag) {
        this.energyStored = tag.getDouble("es");
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_TMU(NBTTagCompound tag) {
        tag.setDouble("es", this.energyStored);
    }

    public double getMaxStorage() {
        return this.maxEnergyStored;
    }

    public double getEnergyStored() {
        return this.energyStored;
    }
}
