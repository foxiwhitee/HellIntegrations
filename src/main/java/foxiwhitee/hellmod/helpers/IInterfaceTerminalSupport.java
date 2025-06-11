package foxiwhitee.hellmod.helpers;

import appeng.api.networking.IGridHost;
import appeng.api.util.DimensionalCoord;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public interface IInterfaceTerminalSupport extends IGridHost {
    DimensionalCoord getLocation();
    PatternsConfiguration[] getPatternsConfigurations();
    IInventory getPatterns(int paramInt);
    String getName();
    TileEntity getTileEntity();

    public static class PatternsConfiguration {
        public int offset;

        public int size;

        public PatternsConfiguration(int offset, int size) {
            this.offset = offset;
            this.size = size;
        }
    }

    default long getSortValue() {
        TileEntity te = getTileEntity();
        return te.zCoord << 24L ^ te.xCoord << 8L ^ te.yCoord;
    }

    default boolean shouldDisplay() {
        return true;
    }
}
