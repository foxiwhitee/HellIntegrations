package foxiwhitee.hellmod.integration.waila;

import appeng.api.parts.IPart;
import appeng.integration.modules.waila.part.IPartWailaDataProvider;
import appeng.integration.modules.waila.part.PartAccessor;
import appeng.integration.modules.waila.part.Tracer;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class PartWailaDataProvider implements IWailaDataProvider {
  private final List<IPartWailaDataProvider> providers;
  
  private final PartAccessor accessor = new PartAccessor();
  
  private final Tracer tracer = new Tracer();
  
  public PartWailaDataProvider() {
    ChannelWailaDataProvider tMChannelWailaDataProvider = new ChannelWailaDataProvider();
    this.providers = Lists.newArrayList(tMChannelWailaDataProvider);
  }
  
  public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
    TileEntity te = accessor.getTileEntity();
    MovingObjectPosition mop = accessor.getPosition();
    Optional<IPart> maybePart = this.accessor.getMaybePart(te, mop);
    if (!maybePart.isPresent())
      return null; 
    IPart part = (IPart)maybePart.get();
    ItemStack wailaStack = null;
    for (IPartWailaDataProvider provider : this.providers)
      wailaStack = provider.getWailaStack(part, config, wailaStack); 
    return wailaStack;
  }
  
  public List<String> getWailaHead(ItemStack itemStack, List<String> currentToolTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
    TileEntity te = accessor.getTileEntity();
    MovingObjectPosition mop = accessor.getPosition();
    Optional<IPart> maybePart = this.accessor.getMaybePart(te, mop);
    if (maybePart.isPresent()) {
      IPart part = (IPart)maybePart.get();
      Iterator<IPartWailaDataProvider> i$ = this.providers.iterator();
      while (i$.hasNext()) {
        IPartWailaDataProvider provider = i$.next();
        provider.getWailaHead(part, currentToolTip, accessor, config);
      } 
    } 
    return currentToolTip;
  }
  
  public List<String> getWailaBody(ItemStack itemStack, List<String> currentToolTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
    TileEntity te = accessor.getTileEntity();
    MovingObjectPosition mop = accessor.getPosition();
    Optional<IPart> maybePart = this.accessor.getMaybePart(te, mop);
    if (maybePart.isPresent()) {
      IPart part = (IPart)maybePart.get();
      Iterator<IPartWailaDataProvider> i$ = this.providers.iterator();
      while (i$.hasNext()) {
        IPartWailaDataProvider provider = i$.next();
        provider.getWailaBody(part, currentToolTip, accessor, config);
      } 
    } 
    return currentToolTip;
  }
  
  public List<String> getWailaTail(ItemStack itemStack, List<String> currentToolTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
    TileEntity te = accessor.getTileEntity();
    MovingObjectPosition mop = accessor.getPosition();
    Optional<IPart> maybePart = this.accessor.getMaybePart(te, mop);
    if (maybePart.isPresent()) {
      IPart part = (IPart)maybePart.get();
      Iterator<IPartWailaDataProvider> i$ = this.providers.iterator();
      while (i$.hasNext()) {
        IPartWailaDataProvider provider = i$.next();
        provider.getWailaTail(part, currentToolTip, accessor, config);
      } 
    } 
    return currentToolTip;
  }
  
  public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
    MovingObjectPosition mop = this.tracer.retraceBlock(world, player, x, y, z);
    if (mop != null) {
      Optional<IPart> maybePart = this.accessor.getMaybePart(te, mop);
      if (maybePart.isPresent()) {
        IPart part = (IPart)maybePart.get();
        Iterator<IPartWailaDataProvider> i$ = this.providers.iterator();
        while (i$.hasNext()) {
          IPartWailaDataProvider provider = i$.next();
          provider.getNBTData(player, part, te, tag, world, x, y, z);
        } 
      } 
    } 
    return tag;
  }
}