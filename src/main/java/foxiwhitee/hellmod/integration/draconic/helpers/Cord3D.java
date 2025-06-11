package foxiwhitee.hellmod.integration.draconic.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Cord3D {
    public double x;

    public double y;

    public double z;

    public Cord3D() {
    }

    public Cord3D(Entity entity) {
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
    }

    public Cord3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Cord3D(Cord3D vec3I) {
        this.x = vec3I.x;
        this.y = vec3I.y;
        this.z = vec3I.z;
    }

    public Cord3D(Vec3 vector3) {
        this.x = vector3.xCoord;
        this.y = vector3.yCoord;
        this.z = vector3.zCoord;
    }

    public static Cord3D getDirectionVec(Cord3D vecFrom, Cord3D vecTo) {
        double distance = getDistanceAtoB(vecFrom, vecTo);
        if (distance == 0.0D)
            distance = 0.1D;
        Cord3D offset = vecTo.copy();
        offset.subtract(vecFrom);
        return new Cord3D(offset.x / distance, offset.y / distance, offset.z / distance);
    }

    public static double getDistanceAtoB(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static double getDistanceAtoB(Cord3D pos1, Cord3D pos2) {
        return getDistanceAtoB(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public static double getDistanceAtoB(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static double getDistanceSq(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }

    public static double getDistanceSq(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return dx * dx + dz * dz;
    }

    public Cord3D set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Cord3D set(Cord3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }

    public Cord3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Cord3D add(Cord3D vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Cord3D subtract(Cord3D vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public Cord3D subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Cord3D multiply(Cord3D vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }

    public Cord3D multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Cord3D copy() {
        return new Cord3D(this);
    }

    public Vec3 toVec3() {
        return Vec3.createVectorHelper(this.x, this.y, this.z);
    }

    public String toString() {
        return String.format("Cord3D: [x: %s, y: %s, z: %s]", new Object[]{Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z)});
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cord3D other = (Cord3D) obj;
        return (this.x == other.x && this.y == other.y && this.z == other.z);
    }

    public int hashCode() {
        return ((int) this.y + (int) this.z * 31) * 31 + (int) this.x;
    }

    public Cord3D offset(EnumFacing direction, double offsetDistance) {
        this.x += direction.getFrontOffsetX() * offsetDistance;
        this.y += direction.getFrontOffsetY() * offsetDistance;
        this.z += direction.getFrontOffsetZ() * offsetDistance;
        return this;
    }

    public Cord3D offset(Cord3D direction, double offsetDistance) {
        this.x += direction.x * offsetDistance;
        this.y += direction.y * offsetDistance;
        this.z += direction.z * offsetDistance;
        return this;
    }

    public double distXZ(Cord3D Cord3D) {
        return getDistanceAtoB(this.x, this.z, Cord3D.x, Cord3D.z);
    }

    public double distance(Cord3D Cord3D) {
        return getDistanceAtoB(this, Cord3D);
    }

    public double distanceSq(Cord3D v) {
        return getDistanceSq(this.x, this.y, this.z, v.x, v.y, v.z);
    }

    public int floorX() {
        return (int) Math.floor(this.x);
    }

    public int floorY() {
        return (int) Math.floor(this.y);
    }

    public int floorZ() {
        return (int) Math.floor(this.z);
    }
}