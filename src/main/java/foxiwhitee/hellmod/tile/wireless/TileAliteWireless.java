package foxiwhitee.hellmod.tile.wireless;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.config.HellConfig;
import net.minecraft.item.ItemStack;

public class TileAliteWireless extends TileCustomWireless{

    @Override
    public int getMaxChannelSize() {
        return HellConfig.wirelessAliteMaxChannelSize;
    }

    @Override
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(ModBlocks.wirelessAlite);
    }
}
