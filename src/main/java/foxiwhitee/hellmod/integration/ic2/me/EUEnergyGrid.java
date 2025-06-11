package foxiwhitee.hellmod.integration.ic2.me;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridStorage;
import appeng.tile.grid.AENetworkInvTile;
import appeng.tile.grid.AENetworkTile;
import foxiwhitee.hellmod.integration.ic2.helpers.IEUEnergyGrid;
import foxiwhitee.hellmod.integration.ic2.helpers.IEUEnergyProvider;
import foxiwhitee.hellmod.integration.ic2.helpers.IEUEnergyReceiver;
import foxiwhitee.hellmod.tile.cpu.TileMEServer;

import java.util.ArrayList;
import java.util.List;

import static appeng.api.config.Actionable.*;

public final class EUEnergyGrid implements IEUEnergyGrid {

    private final List<IEUEnergyProvider> providers = new ArrayList<>();

    private final List<IEUEnergyReceiver> receivers = new ArrayList<>();

    public EUEnergyGrid(IGrid grid) {

    }

    public double extractPower(double energy) {
        double ret = 0;
        double test;
        for (IEUEnergyProvider provider : providers) {
            test = 0;
            test = provider.extractEnergy(energy, SIMULATE);
            if (test > 0) {
                ret += provider.extractEnergy(energy, MODULATE);
                if (ret >= energy) {
                    break;
                }
            }
        }
        return ret;
    }

    public void onUpdateTick() {
        double need;
        for (IEUEnergyReceiver receiver : receivers) {
            need = 0;
            need = receiver.receiveEnergy(Double.MAX_VALUE, SIMULATE);
            if (need > 0) {
                receiver.receiveEnergy(extractPower(need), MODULATE);
            }
        }
    }

    public void removeNode(IGridNode gridNode, IGridHost machine) {
        if (machine instanceof IEUEnergyProvider) {
            providers.remove(machine);
        }
        if (machine instanceof IEUEnergyReceiver) {
            receivers.remove(machine);
        }
    }

    public void addNode(IGridNode gridNode, IGridHost machine) {
        if (machine instanceof IEUEnergyProvider) {
            providers.add((IEUEnergyProvider) machine);
        }
        if (machine instanceof IEUEnergyReceiver) {
            receivers.add((IEUEnergyReceiver) machine);
        }
    }

    public void onSplit(IGridStorage gridStorage) {}

    public void onJoin(IGridStorage gridStorage) {}

    public void populateGridStorage(IGridStorage gridStorage) {}
}
