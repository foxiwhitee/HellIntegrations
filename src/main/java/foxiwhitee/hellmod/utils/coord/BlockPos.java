package foxiwhitee.hellmod.utils.coord;


import java.util.Objects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class BlockPos {
    private final int x;

    private final int y;

    private final int z;

    private final int world;

    public BlockPos(World world, int x, int y, int z) {
        this.world = world.provider.dimensionId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(int world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static BlockPos fromNBT(NBTTagCompound tagCompound) {
        int world = tagCompound.getInteger("worldID");
        int x = tagCompound.getInteger("xPosition");
        int y = tagCompound.getInteger("yPosition");
        int z = tagCompound.getInteger("zPosition");
        return new BlockPos(world, x, y, z);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public World getWorld() {
        return (World)DimensionManager.getWorld(this.world);
    }

    public String toString() {
        return "BlockPos{x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", world=" + this.world + '}';
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BlockPos blockPos = (BlockPos)o;
        return (this.x == blockPos.x && this.y == blockPos.y && this.z == blockPos.z && this.world == blockPos.world);
    }

    public int hashCode() {
        return Objects.hash(new Object[] { Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z), Integer.valueOf(this.world) });
    }

    public NBTTagCompound toNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("worldID", this.world);
        tagCompound.setInteger("xPosition", this.x);
        tagCompound.setInteger("yPosition", this.y);
        tagCompound.setInteger("zPosition", this.z);
        return tagCompound;
    }
}
