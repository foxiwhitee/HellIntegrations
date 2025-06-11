package foxiwhitee.hellmod.tile.wireless;

import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.config.HellConfig;
import net.minecraft.item.ItemStack;

public class TileEfrimWireless extends TileCustomWireless{

    @Override
    public int getMaxChannelSize() {
        return HellConfig.wirelessEfrimMaxChannelSize;
    }

    @Override
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(ModBlocks.wirelessEfrim);
    }
}
