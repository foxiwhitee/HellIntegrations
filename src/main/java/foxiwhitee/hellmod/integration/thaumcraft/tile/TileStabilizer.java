package foxiwhitee.hellmod.integration.thaumcraft.tile;

import appeng.util.Platform;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSource;
import thaumcraft.common.tiles.TileInfusionMatrix;

import java.lang.reflect.Field;
import java.util.HashMap;

public class TileStabilizer extends TileEntity {
    public static int rad = 5;

    public static int limit = 10;

    public static HashMap<WorldCoordinates, Integer> limitMap = new HashMap<>();

    public static Field field;

    public int prevX;

    public int prevY;

    public int prevZ;

    public int prevValue;

    public boolean actives = false;

    public static AspectList getValue(TileInfusionMatrix matrix) {
        try {
            if (field == null)
                (field = matrix.getClass().getDeclaredField("recipeEssentia")).setAccessible(true);
            return (AspectList)field.get(matrix);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        if (this.prevX != 0 || this.prevY != 0 || this.prevZ != 0) {
            nbt.setInteger("xpos", this.prevX);
            nbt.setInteger("ypos", this.prevY);
            nbt.setInteger("zpos", this.prevZ);
        }
        nbt.setInteger("prevValue", this.prevValue);
        super.writeToNBT(nbt);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("xpos")) {
            this.prevX = nbt.getInteger("xpos");
            this.prevY = nbt.getInteger("ypos");
            this.prevZ = nbt.getInteger("zpos");
        }
    }

    public void updateEntity() {
        if (Platform.isServer()) {
            super.updateEntity();
            if (this.worldObj.isRemote)
                return;
            TileInfusionMatrix matrix = null;
            if (this.worldObj.getTileEntity(this.prevX, this.prevY, this.prevZ) instanceof TileInfusionMatrix) {
                matrix = (TileInfusionMatrix) this.worldObj.getTileEntity(this.prevX, this.prevY, this.prevZ);
            } else if (this.worldObj.getTotalWorldTime() % 40L == 0L) {
                for (int x = this.xCoord - rad; x <= this.xCoord + rad; x++) {
                    for (int y = this.yCoord - rad; y <= rad + this.yCoord; y++) {
                        for (int z = this.zCoord - rad; z <= rad + this.zCoord; z++) {
                            TileEntity tileEntity = this.worldObj.getTileEntity(x, y, z);
                            if (tileEntity instanceof TileInfusionMatrix) {
                                matrix = (TileInfusionMatrix) tileEntity;
                                WorldCoordinates coordinates = new WorldCoordinates(x, y, z, this.worldObj.provider.dimensionId);
                                int count = (limitMap.get(coordinates) == null) ? 0 : (((Integer) limitMap.get(coordinates)).intValue() + 1);
                                if (count < limit) {
                                    this.prevX = x;
                                    this.prevY = y;
                                    this.prevZ = z;
                                    limitMap.put(coordinates, Integer.valueOf(count));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (matrix != null && matrix.active && this.worldObj.getTotalWorldTime() % 1L == 0L && this.worldObj.rand.nextInt(1) == 0) {
                AspectList aspectList = getValue(matrix);
                if (aspectList != null && aspectList.visSize() > 0)
                    for (Aspect aspect : aspectList.getAspects()) {
                        if (aspectList.getAmount(aspect) > 0)
                            for (int x2 = this.xCoord - rad; x2 <= this.xCoord + rad; x2++) {
                                for (int y2 = this.yCoord - rad; y2 <= rad + this.yCoord; y2++) {
                                    for (int z2 = this.zCoord - rad; z2 <= rad + this.zCoord; z2++) {
                                        TileEntity tileEntity2 = this.worldObj.getTileEntity(x2, y2, z2);
                                        if (tileEntity2 instanceof IAspectSource && !(tileEntity2 instanceof thaumcraft.common.tiles.TileMirrorEssentia)) {
                                            IAspectSource as = (IAspectSource) tileEntity2;
                                            if (as.takeFromContainer(aspect, 1)) {
                                                SimpleNetworkWrapper pk = PacketHandler.INSTANCE;
                                                double xa = matrix.xCoord;
                                                double ya = matrix.yCoord;
                                                double za = matrix.zCoord;
                                                PacketFXEssentiaSource fx = new PacketFXEssentiaSource((int) xa, (int) ya, (int) za, (byte) (int) (xa - x2), (byte) (int) (ya - y2), (byte) (int) (za - z2), aspect.getColor());
                                                pk.sendToAllAround((IMessage) fx, new NetworkRegistry.TargetPoint((matrix.getWorldObj()).provider.dimensionId, xa, ya, za, 32.0D));
                                                aspectList.reduce(aspect, 1);
                                                this.worldObj.markBlockForUpdate((int) xa, (int) ya, (int) za);
                                                matrix.markDirty();
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                    }
            }
        }
    }

    public final boolean isInWorld() {
        return (this.worldObj != null);
    }
}