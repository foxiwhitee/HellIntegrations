package foxiwhitee.hellmod.integration.ic2.tile.replicators;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.ic2.IC2Integration;
import net.minecraft.item.ItemStack;

public class TileAutoNanoReplicator extends TileAutoReplicator{
    @Override
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(IC2Integration.nano_replicator);
    }

    @Override
    public String getName() {
        return "nano_replicator";
    }

    @Override
    public byte getDiscount() {
        return HellConfig.nano_replicator_discount;
    }

    @Override
    public long getItemsPerSec() {
        return HellConfig.nano_replicator_speed;
    }
}
