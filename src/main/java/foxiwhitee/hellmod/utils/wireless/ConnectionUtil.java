package foxiwhitee.hellmod.utils.wireless;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import java.util.Objects;

public class ConnectionUtil {
    public int x;

    public int y;

    public int z;

    public ConnectionUtil(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static ConnectionUtil getCoord(TileEntity tileEntity) {
        return new ConnectionUtil(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }

    public static ConnectionUtil getTags(NBTTagCompound nbtTagCompound) {
        return new ConnectionUtil(nbtTagCompound.getInteger("x"), nbtTagCompound.getInteger("y"), nbtTagCompound.getInteger("z"));
    }

    public boolean getY(IBlockAccess blockAccess) {
        return (this.y >= 0 && this.y < 256);
    }

    public String toString() {
        return "(x=" + this.x + " y=" + this.y + " z=" + this.z + ")";
    }

    public TileEntity getTile(IBlockAccess blockAccess) {
        if (getY(blockAccess))
            return blockAccess.getTileEntity(this.x, this.y, this.z);
        return null;
    }

    public NBTTagCompound writeToNBTWireless(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("x", this.x);
        nbtTagCompound.setInteger("y", this.y);
        nbtTagCompound.setInteger("z", this.z);
        return nbtTagCompound;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ConnectionUtil))
            return false;
        ConnectionUtil that = (ConnectionUtil)o;
        return (this.x == that.x && this.y == that.y && this.z == that.z);
    }

    public int hashCode() {
        return Objects.hash(new Object[] { Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z) });
    }
}
