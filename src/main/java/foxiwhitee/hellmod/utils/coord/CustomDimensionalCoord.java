package foxiwhitee.hellmod.utils.coord;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CustomDimensionalCoord extends CustomWorldCoord {
    private final World w;

    private final int dimId;

    public CustomDimensionalCoord(CustomDimensionalCoord s) {
        super(s.x, s.y, s.z);
        this.w = s.w;
        this.dimId = s.dimId;
    }

    public CustomDimensionalCoord(TileEntity s) {
        super(s);
        this.w = s.getWorldObj();
        this.dimId = this.w.provider.dimensionId;
    }

    public CustomDimensionalCoord(World _w, int _x, int _y, int _z) {
        super(_x, _y, _z);
        this.w = _w;
        this.dimId = _w.provider.dimensionId;
    }

    public CustomDimensionalCoord(int _x, int _y, int _z, int _dim) {
        super(_x, _y, _z);
        this.w = null;
        this.dimId = _dim;
    }

    public CustomDimensionalCoord copy() {
        return new CustomDimensionalCoord(this);
    }

    public int hashCode() {
        return super.hashCode() ^ this.dimId;
    }

    public boolean equals(Object obj) {
        return (obj instanceof CustomDimensionalCoord && isEqual((CustomDimensionalCoord)obj));
    }

    public boolean isEqual(CustomDimensionalCoord c) {
        return (this.x == c.x && this.y == c.y && this.z == c.z && c.w == this.w);
    }

    private static void writeToNBT(NBTTagCompound data, int x, int y, int z, int dimId) {
        data.setInteger("dim", dimId);
        data.setInteger("x", x);
        data.setInteger("y", y);
        data.setInteger("z", z);
    }

    public void writeToNBT(NBTTagCompound data) {
        writeToNBT(data, this.x, this.y, this.z, this.dimId);
    }

    public static void writeListToNBT(NBTTagCompound tag, List<CustomDimensionalCoord> list) {
        int i = 0;
        for (CustomDimensionalCoord d : list) {
            NBTTagCompound data = new NBTTagCompound();
            writeToNBT(data, d.x, d.y, d.z, d.dimId);
            tag.setTag("pos#" + i, (NBTBase)data);
            i++;
        }
    }

    public static CustomDimensionalCoord readFromNBT(NBTTagCompound data) {
        return new CustomDimensionalCoord(data
                .getInteger("x"), data
                .getInteger("y"), data
                .getInteger("z"), data
                .getInteger("dim"));
    }

    public static List<CustomDimensionalCoord> readAsListFromNBT(NBTTagCompound tag) {
        List<CustomDimensionalCoord> list = new ArrayList<>();
        int i = 0;
        while (tag.hasKey("pos#" + i)) {
            NBTTagCompound data = tag.getCompoundTag("pos#" + i);
            list.add(readFromNBT(data));
            i++;
        }
        return list;
    }

    public String toString() {
        return "dimension=" + this.dimId + ", " + super.toString();
    }

    public boolean isInWorld(World world) {
        return (this.w == world);
    }

    public World getWorld() {
        return this.w;
    }

    public int getDimension() {
        return this.dimId;
    }
}
