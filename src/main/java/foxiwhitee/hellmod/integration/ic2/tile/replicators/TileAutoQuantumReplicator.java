package foxiwhitee.hellmod.integration.ic2.tile.replicators;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.ic2.IC2Integration;
import net.minecraft.item.ItemStack;

public class TileAutoQuantumReplicator extends TileAutoReplicator{
    @Override
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(IC2Integration.quantum_replicator);
    }

    @Override
    public String getName() {
        return "quantum_replicator";
    }

    @Override
    public byte getDiscount() {
        return HellConfig.quantum_replicator_discount;
    }

    @Override
    public long getItemsPerSec() {
        return HellConfig.quantum_replicator_speed;
    }
}
