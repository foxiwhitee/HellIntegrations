package foxiwhitee.hellmod.integration.waila;

import appeng.api.parts.IPart;
import appeng.core.localization.WailaText;
import appeng.integration.modules.waila.part.BasePartWailaDataProvider;
import foxiwhitee.hellmod.utils.cables.ICustomChannelCount;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.map.hash.TObjectShortHashMap;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public class ChannelWailaDataProvider extends BasePartWailaDataProvider {
  private static final String ID_USED_CHANNELS = "usedChannels";
  
  private final TObjectShortMap<IPart> cache = (TObjectShortMap<IPart>)new TObjectShortHashMap();
  
  public List<String> getWailaBody(IPart part, List<String> currentToolTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
    if (part instanceof ICustomChannelCount && part instanceof appeng.api.implementations.parts.IPartCable) {
      NBTTagCompound tag = accessor.getNBTData();
      short usedChannels = getUsedChannels(part, tag, this.cache);
      short maxChannels = (short)((ICustomChannelCount)part).getMaxChannelSize();
      boolean isInfinity = (((ICustomChannelCount)part).getMaxChannelSize() >= Integer.MAX_VALUE);
      String formattedToolTip = String.format(WailaText.Channels.getLocal(), new Object[] { Short.valueOf(usedChannels), isInfinity ? "Infinity" : Short.valueOf(maxChannels) });
      currentToolTip.add(formattedToolTip);
    } 
    return currentToolTip;
  }
  
  private short getUsedChannels(IPart part, NBTTagCompound tag, TObjectShortMap<IPart> cache) {
    short usedChannels;
    if (tag.hasKey("usedChannels")) {
      usedChannels = tag.getShort("usedChannels");
      this.cache.put(part, usedChannels);
    } else if (this.cache.containsKey(part)) {
      usedChannels = this.cache.get(part);
    } else {
      usedChannels = 0;
    } 
    return usedChannels;
  }
  
  public NBTTagCompound getNBTData(EntityPlayerMP player, IPart part, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
    if (part instanceof ICustomChannelCount && part instanceof appeng.api.implementations.parts.IPartCable) {
      NBTTagCompound tempTag = new NBTTagCompound();
      part.writeToNBT(tempTag);
      if (tempTag.hasKey("usedChannels")) {
        short usedChannels = tempTag.getShort("usedChannels");
        tag.setShort("usedChannels", usedChannels);
      } 
    } 
    return tag;
  }
}