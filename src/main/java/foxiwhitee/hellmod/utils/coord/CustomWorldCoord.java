package foxiwhitee.hellmod.utils.coord;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class CustomWorldCoord extends appeng.api.util.WorldCoord{
    public int x;

    public int y;

    public int z;

    public CustomWorldCoord(TileEntity s) {
        this(s.xCoord, s.yCoord, s.zCoord);
    }

    public CustomWorldCoord(int _x, int _y, int _z) {
        super(_x, _y, _z);
        this.x = _x;
        this.y = _y;
        this.z = _z;
    }

    public CustomWorldCoord subtract(ForgeDirection direction, int length) {
        this.x -= direction.offsetX * length;
        this.y -= direction.offsetY * length;
        this.z -= direction.offsetZ * length;
        return this;
    }

    public CustomWorldCoord add(int _x, int _y, int _z) {
        this.x += _x;
        this.y += _y;
        this.z += _z;
        return this;
    }

    public CustomWorldCoord subtract(int _x, int _y, int _z) {
        this.x -= _x;
        this.y -= _y;
        this.z -= _z;
        return this;
    }

    public CustomWorldCoord multiple(int _x, int _y, int _z) {
        this.x *= _x;
        this.y *= _y;
        this.z *= _z;
        return this;
    }

    public CustomWorldCoord divide(int _x, int _y, int _z) {
        this.x /= _x;
        this.y /= _y;
        this.z /= _z;
        return this;
    }

    public ForgeDirection directionTo(CustomWorldCoord loc) {
        int ox = this.x - loc.x;
        int oy = this.y - loc.y;
        int oz = this.z - loc.z;
        int xlen = Math.abs(ox);
        int ylen = Math.abs(oy);
        int zlen = Math.abs(oz);
        if (loc.isEqual(copy().add(ForgeDirection.EAST, xlen)))
            return ForgeDirection.EAST;
        if (loc.isEqual(copy().add(ForgeDirection.WEST, xlen)))
            return ForgeDirection.WEST;
        if (loc.isEqual(copy().add(ForgeDirection.NORTH, zlen)))
            return ForgeDirection.NORTH;
        if (loc.isEqual(copy().add(ForgeDirection.SOUTH, zlen)))
            return ForgeDirection.SOUTH;
        if (loc.isEqual(copy().add(ForgeDirection.UP, ylen)))
            return ForgeDirection.UP;
        if (loc.isEqual(copy().add(ForgeDirection.DOWN, ylen)))
            return ForgeDirection.DOWN;
        return null;
    }

    public double distanceTo(CustomWorldCoord c) {
        double dx = (c.x - this.x);
        double dy = (c.y - this.y);
        double dz = (c.z - this.z);
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static long getTaxicabDistance(appeng.api.util.WorldCoord blockPos, appeng.api.util.WorldCoord blockPos2) {
        long dx = Math.abs(blockPos.x - blockPos2.x);
        long dy = Math.abs(blockPos.y - blockPos2.y);
        long dz = Math.abs(blockPos.z - blockPos2.z);
        return dx + dy + dz;
    }

    public boolean isEqual(CustomWorldCoord c) {
        return (this.x == c.x && this.y == c.y && this.z == c.z);
    }

    public CustomWorldCoord add(ForgeDirection direction, int length) {
        this.x += direction.offsetX * length;
        this.y += direction.offsetY * length;
        this.z += direction.offsetZ * length;
        return this;
    }

    public CustomWorldCoord copy() {
        return new CustomWorldCoord(this.x, this.y, this.z);
    }

    public int hashCode() {
        return this.y << 24 ^ this.x ^ this.z;
    }

    public boolean equals(Object obj) {
        return (obj instanceof CustomWorldCoord && isEqual((CustomWorldCoord)obj));
    }

    public String toString() {
        return "x=" + this.x + ", y=" + this.y + ", z=" + this.z;
    }
}
