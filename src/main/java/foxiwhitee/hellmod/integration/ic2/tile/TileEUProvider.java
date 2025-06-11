package foxiwhitee.hellmod.integration.ic2.tile;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridHost;
import appeng.api.util.AECableType;
import appeng.tile.grid.AENetworkTile;
import foxiwhitee.hellmod.integration.ic2.helpers.IEUEnergyProvider;
import foxiwhitee.hellmod.integration.ic2.helpers.ISupportsEUEnergyProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.ref.WeakReference;

public class TileEUProvider extends AENetworkTile implements IEUEnergyProvider {

    public TileEUProvider() {
        getProxy().setIdlePowerUsage(1000.0D);
        getProxy().setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    public double extractEnergy(double energy, Actionable actionable) {
        TileEntity tile = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (tile instanceof ISupportsEUEnergyProvider) {
            return ((ISupportsEUEnergyProvider) tile).extractEU(energy, actionable);
        }
        return 0.0D;
    }

    public AECableType getCableConnectionType(ForgeDirection dir) {
        return dir == ForgeDirection.UP ? AECableType.SMART : AECableType.NONE;
    }

}
