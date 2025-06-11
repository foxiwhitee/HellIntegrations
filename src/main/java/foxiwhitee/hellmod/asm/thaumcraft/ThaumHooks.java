package foxiwhitee.hellmod.asm.thaumcraft;

import foxiwhitee.hellmod.integration.thaumcraft.tile.TileStabilizer;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.common.tiles.TileInfusionMatrix;

public class ThaumHooks {
  public static boolean readFromNBT(NBTTagCompound compound) {
    return (compound.hasKey("StabilizerPresent") && compound.getBoolean("StabilizerPresent"));
  }
  
  public static void writeToNBT(NBTTagCompound compound, boolean b) {
    compound.setBoolean("StabilizerPresent", b);
  }
  
  public static boolean onCraftingStartEnd(TileInfusionMatrix tileEntity) {
    int xCoord = tileEntity.xCoord;
    int yCoord = tileEntity.yCoord;
    int zCoord = tileEntity.zCoord;
    int dist = 6;
    int count = 0;
    for (int x = xCoord - dist; x <= xCoord + dist; x++) {
      for (int y = yCoord - dist; y <= yCoord + dist; y++) {
        for (int z = zCoord - dist; z <= zCoord + dist; z++) {
          count++;
          if (tileEntity.getWorldObj().getTileEntity(x, y, z) instanceof TileStabilizer && count == 4) {
            tileEntity.instability = 0;
            TileStabilizer tiles = new TileStabilizer();
            tiles.actives = true;
            return true;
          } 
        } 
      } 
    } 
    return false;
  }
}