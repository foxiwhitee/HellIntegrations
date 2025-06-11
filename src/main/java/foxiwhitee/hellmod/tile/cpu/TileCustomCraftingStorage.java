package foxiwhitee.hellmod.tile.cpu;

import appeng.tile.crafting.TileCraftingStorageTile;
import foxiwhitee.hellmod.blocks.cpu.BlockCustomCraftingStorage;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TileCustomCraftingStorage extends TileCraftingStorageTile {
    @Override
    protected ItemStack getItemFromTile(Object obj) {
        TileCustomCraftingStorage tile = (TileCustomCraftingStorage) obj;
        return new ItemStack(BlockCustomCraftingStorage.getBlockFromBytes(tile.getStorageBytesLong()));
    }

    @Override
    public int getStorageBytes() {
        return 1;
    }

    public long getStorageBytesLong() {
        if (this.worldObj == null || this.notLoaded()) return 0;

        Block b = getBlockType();
        return BlockCustomCraftingStorage.getBytesFromBlock(b);
    }
}