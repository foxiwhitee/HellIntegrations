package foxiwhitee.hellmod.asm.ic2;

import ic2.core.block.machine.tileentity.TileEntityPatternStorage;

public class IC2Hooks {
    public static void getPattern(TileEntityPatternStorage tile) {
        if (tile.getWorldObj() == null)
            return;
        tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
        tile.getWorldObj().notifyBlockChange(tile.xCoord, tile.yCoord, tile.zCoord, tile.getWorldObj().getBlock(tile.xCoord, tile.yCoord, tile.zCoord));
        tile.markDirty();
    }
}
