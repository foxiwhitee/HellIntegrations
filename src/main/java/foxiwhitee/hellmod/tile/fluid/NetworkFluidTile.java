package foxiwhitee.hellmod.tile.fluid;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkInvTile;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.items.ItemFluidDrop;
import foxiwhitee.hellmod.tile.TileNetworkInv;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public abstract class NetworkFluidTile extends AENetworkInvTile implements IFluidHandler {
    private final MachineSource source = new MachineSource((IActionHost)this);
    private final FluidTank tank = new FluidTank(10000000);

    public long injectFluid(FluidStack resource, Actionable actionable) {
        long filled = tank.fill(resource, actionable == Actionable.MODULATE);
        if (actionable == Actionable.MODULATE && filled > 0) {
            markDirty();
        }
        return filled;
    }

    public FluidStack extractFluid(FluidStack resource, Actionable actionable) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
            return null;
        }
        FluidStack drained = tank.drain(resource.amount, actionable == Actionable.MODULATE);
        if (actionable == Actionable.MODULATE && drained != null) {
            markDirty();
        }
        return drained;
    }

    public FluidStack extractFluid(int maxDrain, Actionable actionable) {
        FluidStack drained = tank.drain(maxDrain, actionable == Actionable.MODULATE);
        if (actionable == Actionable.MODULATE && drained != null) {
            markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        return drained;
    }

    public long extractFluidDrop(long amount) {
        if (Platform.isServer()) {
            try {
                if (getInternalInventory().getStackInSlot(0) != null) {
                    AEItemStack mana = (AEItemStack) AEItemStack.create(getInternalInventory().getStackInSlot(0));
                    mana.setStackSize(amount);
                    IAEItemStack remainder = getProxy().getStorage().getItemInventory().extractItems(mana.copy(), Actionable.SIMULATE, source);
                    if (remainder != null && remainder.getStackSize() > 0) {
                        IAEItemStack items = getProxy().getStorage().getItemInventory().extractItems(remainder.copy(), Actionable.MODULATE, source);
                        injectFluid(ItemFluidDrop.getFluidStack(remainder.getItemStack()), Actionable.MODULATE);
                        return Math.min(tank.getCapacity(), tank.getFluid().amount + items.getStackSize());
                    }
                }
            } catch (GridAccessException e) {}
            finally {
                markDirty();
                markForUpdate();
            }
        }
        return 0;
    }

    public void injectFluidDrop(long amount) {
        if (Platform.isServer()) {
            try {
                AEItemStack mana = (AEItemStack) ItemFluidDrop.newAeStack(tank.getFluid());
                mana.setStackSize(amount);
                IAEItemStack remainder = getProxy().getStorage().getItemInventory().injectItems(mana.copy(), Actionable.SIMULATE, source);
                if (remainder != null && remainder.getStackSize() > 0) {
                    extractFluid((int) Math.min(Integer.MAX_VALUE, remainder.getStackSize()), Actionable.MODULATE);
                    getProxy().getStorage().getItemInventory().injectItems(remainder.copy(), Actionable.MODULATE, source);
                }
            } catch (GridAccessException e) {}
            finally {
                markDirty();
                markForUpdate();
            }
        }
    }

    @Override
    public final int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return (int) Math.min(Integer.MAX_VALUE, injectFluid(resource, doFill ? Actionable.MODULATE : Actionable.SIMULATE));
    }

    @Override
    public final FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return extractFluid(resource, doDrain ? Actionable.MODULATE : Actionable.SIMULATE);
    }

    @Override
    public final FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return extractFluid(maxDrain, doDrain ? Actionable.MODULATE : Actionable.SIMULATE);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return tank.getFluid() == null || tank.getFluid().getFluid() == fluid && from != ForgeDirection.DOWN && from != ForgeDirection.UP;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return tank.getFluid() != null && tank.getFluid().getFluid() == fluid && from != ForgeDirection.DOWN && from != ForgeDirection.UP;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_(NBTTagCompound nbt) {
        tank.writeToNBT(nbt);
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_(NBTTagCompound nbt) {
        tank.readFromNBT(nbt);
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }

    @Override
    public int[] getAccessibleSlotsBySide(ForgeDirection forgeDirection) {
        return new int[0];
    }

    public MachineSource getSource() {
        return source;
    }
}
