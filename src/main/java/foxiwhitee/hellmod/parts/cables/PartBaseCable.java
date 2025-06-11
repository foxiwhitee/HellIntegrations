package foxiwhitee.hellmod.parts.cables;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.api.implementations.parts.IPartCable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.parts.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.AEColoredItemDefinition;
import appeng.api.util.IReadOnlyCollection;
import appeng.block.AEBaseBlock;
import appeng.client.texture.CableBusTextures;
import appeng.client.texture.FlippableIcon;
import appeng.client.texture.TaughtIcon;
import appeng.me.GridAccessException;
import appeng.parts.AEBasePart;
import appeng.util.Platform;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.items.part.ItemParts;
import foxiwhitee.hellmod.utils.cables.ICustomChannelCount;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.EnumSet;

public abstract class PartBaseCable extends AEBasePart implements ICustomChannelCount, IPartCable {
  private final int[] channelsOnSide = new int[] { 0, 0, 0, 0, 0, 0 };

  private EnumSet<ForgeDirection> connections = EnumSet.noneOf(ForgeDirection.class);

  private boolean powered = false;

  public PartBaseCable(ItemStack is) {
    super(is);
    getProxy().setIdlePowerUsage(0.0D);
    getProxy().setFlags(new GridFlags[] { GridFlags.PREFERRED });
    getProxy().setColor(AEColor.values()[((ItemParts)is.getItem()).variantOf(is.getItemDamage())]);
  }

  public BusSupport supportsBuses() {
    return BusSupport.CABLE;
  }

  public AEColor getCableColor() {
    return getProxy().getColor();
  }

  public AECableType getCableConnectionType() {
    return AECableType.SMART;
  }

  public boolean changeColor(AEColor newColor, EntityPlayer who) {
    if (getCableColor() != newColor) {
      ItemStack newPart = getPartItemStack(newColor);
      boolean hasPermission = true;
      try {
        hasPermission = getProxy().getSecurity().hasPermission(who, SecurityPermissions.BUILD);
      } catch (GridAccessException gridAccessException) {}
      if (newPart != null && hasPermission) {
        if (Platform.isClient())
          return true;
        getHost().removePart(ForgeDirection.UNKNOWN, true);
        getHost().addPart(newPart, ForgeDirection.UNKNOWN, who);
        return true;
      }
    }
    return false;
  }

  public void setValidSides(EnumSet<ForgeDirection> sides) {
    getProxy().setValidSides(sides);
  }

  public boolean isConnected(ForgeDirection side) {
    return getConnections().contains(side);
  }

  public void markForUpdate() {
    getHost().markForUpdate();
  }

  boolean isSmart(ForgeDirection of) {
    TileEntity te = getTile().getWorldObj().getTileEntity((getTile()).xCoord + of.offsetX, (getTile()).yCoord + of.offsetY, (getTile()).zCoord + of.offsetZ);
    if (te instanceof IGridHost) {
      AECableType t = ((IGridHost)te).getCableConnectionType(of.getOpposite());
      return (t == AECableType.SMART);
    }
    return false;
  }

  public void writeToNBT(NBTTagCompound data) {
    super.writeToNBT(data);
    if (Platform.isServer()) {
      IGridNode node = getGridNode();
      if (node != null) {
        int howMany = 0;
        for (IGridConnection gc : node.getConnections())
          howMany = Math.max(gc.getUsedChannels(), howMany);
        data.setShort("usedChannels", (short)howMany);
      }
    }
  }

  public void writeToStream(ByteBuf data) {
    int cs = 0;
    int sideOut = 0;
    IGridNode n = getGridNode();
    if (n != null) {
      for (ForgeDirection thisSide : ForgeDirection.VALID_DIRECTIONS) {
        IPart part = getHost().getPart(thisSide);
        if (part != null && part.getGridNode() != null) {
          IReadOnlyCollection<IGridConnection> set = part.getGridNode().getConnections();
          for (IGridConnection gc : set) {
            if ((getProxy().getNode().getMachine() instanceof PartBaseDenseCable && gc.getOtherSide(getProxy().getNode()).getMachine() instanceof PartBaseDenseCable) || (gc
                    .getOtherSide(getProxy().getNode()).getMachine() != null && gc.getOtherSide(getProxy().getNode()).getMachine().getClass().getSimpleName().equals("TileController"))) {
              sideOut |= gc.getUsedChannels() / getMaxChannelSize() / (getMaxChannelSize() / 32) << 4 * thisSide.ordinal();
              continue;
            }
            sideOut |= gc.getUsedChannels() / (getMaxChannelSize() / 32) << 4 * thisSide.ordinal();
          }
        }
      }
      for (IGridConnection gc : n.getConnections()) {
        ForgeDirection side = gc.getDirection(n);
        if (side != ForgeDirection.UNKNOWN) {
          if ((getProxy().getNode().getMachine() instanceof PartBaseDenseCable && gc.getOtherSide(getProxy().getNode()).getMachine() instanceof PartBaseDenseCable) || (gc
                  .getOtherSide(getProxy().getNode()).getMachine() != null && gc.getOtherSide(getProxy().getNode()).getMachine().getClass().getSimpleName().equals("TileController"))) {
            sideOut |= gc.getUsedChannels() / getMaxChannelSize() / (getMaxChannelSize() / 32) << 4 * side.ordinal();
          } else {
            sideOut |= gc.getUsedChannels() / (getMaxChannelSize() / 32) << 4 * side.ordinal();
          }
          cs |= 1 << side.ordinal();
        }
      }
    }
    try {
      if (getProxy().getEnergy().isNetworkPowered())
        cs |= 1 << ForgeDirection.UNKNOWN.ordinal();
    } catch (GridAccessException gridAccessException) {}
    data.writeByte((byte)cs);
    data.writeInt(sideOut);
  }

  public boolean readFromStream(ByteBuf data) {
    int cs = data.readByte();
    int sideOut = data.readInt();
    EnumSet<ForgeDirection> myC = getConnections().clone();
    boolean wasPowered = this.powered;
    this.powered = false;
    boolean channelsChanged = false;
    for (ForgeDirection d : ForgeDirection.values()) {
      if (d != ForgeDirection.UNKNOWN) {
        int ch = sideOut >> d.ordinal() * 4 & 0xF;
        if (ch != getChannelsOnSide()[d.ordinal()]) {
          channelsChanged = true;
          getChannelsOnSide()[d.ordinal()] = ch;
        }
      }
      int id = 1 << d.ordinal();
      if (d == ForgeDirection.UNKNOWN) {
        if (id == (cs & id))
          this.powered = true;
      } else if (id == (cs & id)) {
        getConnections().add(d);
      } else {
        getConnections().remove(d);
      }
    }
    return (!myC.equals(getConnections()) || wasPowered != this.powered || channelsChanged);
  }

  public void getBoxes(IPartCollisionHelper bch) {
    bch.addBox(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    if (Platform.isServer()) {
      IGridNode n = getGridNode();
      if (n != null) {
        setConnections(n.getConnectedSides());
      } else {
        getConnections().clear();
      }
    }
    IPartHost ph = getHost();
    if (ph != null)
      for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
        IPart p = ph.getPart(dir);
        if (p instanceof IGridHost) {
          double dist = p.cableConnectionRenderTo();
          if (dist <= 8.0D)
            switch (dir) {
              case DOWN:
                bch.addBox(6.0D, dist, 6.0D, 10.0D, 6.0D, 10.0D);
                break;
              case UP:
                bch.addBox(10.0D, 6.0D, 6.0D, 16.0D - dist, 10.0D, 10.0D);
                break;
              case NORTH:
                bch.addBox(6.0D, 6.0D, dist, 10.0D, 10.0D, 6.0D);
                break;
              case SOUTH:
                bch.addBox(6.0D, 6.0D, 10.0D, 10.0D, 10.0D, 16.0D - dist);
                break;
              case WEST:
                bch.addBox(6.0D, 10.0D, 6.0D, 10.0D, 16.0D - dist, 10.0D);
                break;
              case EAST:
                bch.addBox(dist, 6.0D, 6.0D, 6.0D, 10.0D, 10.0D);
                break;
            }
        }
      }
    for (ForgeDirection of : getConnections()) {
      switch (of) {
        case DOWN:
          bch.addBox(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
          break;
        case UP:
          bch.addBox(10.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
          break;
        case NORTH:
          bch.addBox(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 6.0D);
          break;
        case SOUTH:
          bch.addBox(6.0D, 6.0D, 10.0D, 10.0D, 10.0D, 16.0D);
          break;
        case WEST:
          bch.addBox(6.0D, 10.0D, 6.0D, 10.0D, 16.0D, 10.0D);
          break;
        case EAST:
          bch.addBox(0.0D, 6.0D, 6.0D, 6.0D, 10.0D, 10.0D);
          break;
      }
    }
  }

  @SideOnly(Side.CLIENT)
  public void renderInventory(IPartRenderHelper rh, RenderBlocks renderer) {
    GL11.glTranslated(-0.0D, -0.0D, 0.3D);
    rh.setTexture(getTexture(getCableColor()));
    rh.setBounds(6.0F, 6.0F, 2.0F, 10.0F, 10.0F, 14.0F);
    rh.renderInventoryBox(renderer);
    rh.setTexture(null);
  }

  public abstract IIcon getTexture(AEColor c);

  @SideOnly(Side.CLIENT)
  public void renderStatic(int x, int y, int z, IPartRenderHelper rh, RenderBlocks renderer) {
    setRenderCache(rh.useSimplifiedRendering(x, y, z, (IBoxProvider)this, getRenderCache()));
    boolean useCovered = false;
    boolean requireDetailed = false;
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
      IPart p = getHost().getPart(dir);
      if (p instanceof IGridHost) {
        IGridHost igh = (IGridHost)p;
        AECableType type = igh.getCableConnectionType(dir.getOpposite());
        if (type == AECableType.COVERED || type == AECableType.SMART) {
          useCovered = true;
          break;
        }
      } else if (getConnections().contains(dir)) {
        TileEntity te = getTile().getWorldObj().getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
        IPartHost partHost = (te instanceof IPartHost) ? (IPartHost)te : null;
        IGridHost gh = (te instanceof IGridHost) ? (IGridHost)te : null;
        if (partHost == null && gh != null && gh.getCableConnectionType(dir) != AECableType.GLASS)
          requireDetailed = true;
      }
    }
    if (useCovered) {
      rh.setTexture(getTexture(getCableColor()));
    } else {
      rh.setTexture(getTexture(getCableColor()));
    }
    IPartHost ph = getHost();
    for (ForgeDirection of : EnumSet.<ForgeDirection>complementOf(getConnections())) {
      IPart bp = ph.getPart(of);
      if (bp instanceof IGridHost) {
        int len = bp.cableConnectionRenderTo();
        if (len < 8) {
          switch (of) {
            case DOWN:
              rh.setBounds(6.0F, len, 6.0F, 10.0F, 6.0F, 10.0F);
              break;
            case UP:
              rh.setBounds(10.0F, 6.0F, 6.0F, (16 - len), 10.0F, 10.0F);
              break;
            case NORTH:
              rh.setBounds(6.0F, 6.0F, len, 10.0F, 10.0F, 6.0F);
              break;
            case SOUTH:
              rh.setBounds(6.0F, 6.0F, 10.0F, 10.0F, 10.0F, (16 - len));
              break;
            case WEST:
              rh.setBounds(6.0F, 10.0F, 6.0F, 10.0F, (16 - len), 10.0F);
              break;
            case EAST:
              rh.setBounds(len, 6.0F, 6.0F, 6.0F, 10.0F, 10.0F);
              break;
            default:
              continue;
          }
          rh.renderBlock(x, y, z, renderer);
        }
      }
    }
    if (getConnections().size() != 2 || !nonLinear(getConnections()) || useCovered || requireDetailed) {
      if (useCovered) {
        rh.setBounds(5.0F, 5.0F, 5.0F, 11.0F, 11.0F, 11.0F);
        rh.renderBlock(x, y, z, renderer);
      } else {
        rh.setBounds(6.0F, 6.0F, 6.0F, 10.0F, 10.0F, 10.0F);
        rh.renderBlock(x, y, z, renderer);
      }
      for (ForgeDirection of : getConnections())
        renderGlassConnection(x, y, z, rh, renderer, of);
    } else {
      IIcon def = getTexture(getCableColor());
      rh.setTexture(def);
      for (ForgeDirection of : getConnections()) {
        rh.setFacesToRender(EnumSet.complementOf((EnumSet)EnumSet.of(of, of.getOpposite())));
        switch (of) {
          case DOWN:
          case WEST:
            renderer.setRenderBounds(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
            break;
          case UP:
          case EAST:
            renderer.uvRotateEast = renderer.uvRotateWest = 1;
            renderer.uvRotateBottom = renderer.uvRotateTop = 1;
            renderer.setRenderBounds(0.0D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);
            break;
          case NORTH:
          case SOUTH:
            renderer.uvRotateNorth = renderer.uvRotateSouth = 1;
            renderer.setRenderBounds(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 1.0D);
            break;
        }
      }
      rh.renderBlockCurrentBounds(x, y, z, renderer);
    }
    rh.setFacesToRender(EnumSet.allOf(ForgeDirection.class));
    rh.setTexture(null);
  }

  @SideOnly(Side.CLIENT)
  public IIcon getBreakingTexture() {
    return getTexture(getCableColor());
  }

  protected boolean nonLinear(EnumSet<ForgeDirection> sides) {
    return ((sides.contains(ForgeDirection.EAST) && sides.contains(ForgeDirection.WEST)) || (sides.contains(ForgeDirection.NORTH) && sides.contains(ForgeDirection.SOUTH)) || (sides.contains(ForgeDirection.UP) && sides.contains(ForgeDirection.DOWN)));
  }

  @SideOnly(Side.CLIENT)
  private void renderGlassConnection(int x, int y, int z, IPartRenderHelper rh, RenderBlocks renderer, ForgeDirection of) {
    TileEntity te = getTile().getWorldObj().getTileEntity(x + of.offsetX, y + of.offsetY, z + of.offsetZ);
    IPartHost partHost = (te instanceof IPartHost) ? (IPartHost)te : null;
    IGridHost gh = (te instanceof IGridHost) ? (IGridHost)te : null;
    rh.setFacesToRender(EnumSet.complementOf((EnumSet)EnumSet.of(of)));
    if (gh != null && partHost != null && gh.getCableConnectionType(of.getOpposite()) == AECableType.GLASS && partHost.getColor() != AEColor.Transparent && partHost.getPart(of.getOpposite()) == null) {
      rh.setTexture(getTexture(partHost.getColor()));
    } else if (partHost == null && gh != null && gh.getCableConnectionType(of.getOpposite()) != AECableType.GLASS) {
      rh.setTexture(getTexture(getCableColor()));
      switch (of) {
        case DOWN:
          rh.setBounds(5.0F, 0.0F, 5.0F, 11.0F, 4.0F, 11.0F);
          break;
        case UP:
          rh.setBounds(12.0F, 5.0F, 5.0F, 16.0F, 11.0F, 11.0F);
          break;
        case NORTH:
          rh.setBounds(5.0F, 5.0F, 0.0F, 11.0F, 11.0F, 4.0F);
          break;
        case SOUTH:
          rh.setBounds(5.0F, 5.0F, 12.0F, 11.0F, 11.0F, 16.0F);
          break;
        case WEST:
          rh.setBounds(5.0F, 12.0F, 5.0F, 11.0F, 16.0F, 11.0F);
          break;
        case EAST:
          rh.setBounds(0.0F, 5.0F, 5.0F, 4.0F, 11.0F, 11.0F);
          break;
        default:
          return;
      }
      rh.renderBlock(x, y, z, renderer);
      rh.setTexture(getTexture(getCableColor()));
    } else {
      rh.setTexture(getTexture(getCableColor()));
    }
    switch (of) {
      case DOWN:
        rh.setBounds(6.0F, 0.0F, 6.0F, 10.0F, 6.0F, 10.0F);
        break;
      case UP:
        rh.setBounds(10.0F, 6.0F, 6.0F, 16.0F, 10.0F, 10.0F);
        break;
      case NORTH:
        rh.setBounds(6.0F, 6.0F, 0.0F, 10.0F, 10.0F, 6.0F);
        break;
      case SOUTH:
        rh.setBounds(6.0F, 6.0F, 10.0F, 10.0F, 10.0F, 16.0F);
        break;
      case WEST:
        rh.setBounds(6.0F, 10.0F, 6.0F, 10.0F, 16.0F, 10.0F);
        break;
      case EAST:
        rh.setBounds(0.0F, 6.0F, 6.0F, 6.0F, 10.0F, 10.0F);
        break;
      default:
        return;
    }
    rh.renderBlock(x, y, z, renderer);
    rh.setFacesToRender(EnumSet.allOf(ForgeDirection.class));
  }

  @SideOnly(Side.CLIENT)
  protected void renderCoveredConnection(int x, int y, int z, IPartRenderHelper rh, RenderBlocks renderer, int channels, ForgeDirection of) {
    TileEntity te = getTile().getWorldObj().getTileEntity(x + of.offsetX, y + of.offsetY, z + of.offsetZ);
    IPartHost partHost = (te instanceof IPartHost) ? (IPartHost)te : null;
    IGridHost ghh = (te instanceof IGridHost) ? (IGridHost)te : null;
    rh.setFacesToRender(EnumSet.complementOf((EnumSet)EnumSet.of(of)));
    if (ghh != null && partHost != null && ghh.getCableConnectionType(of.getOpposite()) == AECableType.GLASS && partHost.getPart(of.getOpposite()) == null && partHost.getColor() != AEColor.Transparent) {
      rh.setTexture(getTexture(partHost.getColor()));
    } else if (partHost == null && ghh != null && ghh.getCableConnectionType(of.getOpposite()) != AECableType.GLASS) {
      rh.setTexture(getTexture(getCableColor()));
      switch (of) {
        case DOWN:
          rh.setBounds(5.0F, 0.0F, 5.0F, 11.0F, 4.0F, 11.0F);
          break;
        case UP:
          rh.setBounds(12.0F, 5.0F, 5.0F, 16.0F, 11.0F, 11.0F);
          break;
        case NORTH:
          rh.setBounds(5.0F, 5.0F, 0.0F, 11.0F, 11.0F, 4.0F);
          break;
        case SOUTH:
          rh.setBounds(5.0F, 5.0F, 12.0F, 11.0F, 11.0F, 16.0F);
          break;
        case WEST:
          rh.setBounds(5.0F, 12.0F, 5.0F, 11.0F, 16.0F, 11.0F);
          break;
        case EAST:
          rh.setBounds(0.0F, 5.0F, 5.0F, 4.0F, 11.0F, 11.0F);
          break;
        default:
          return;
      }
      rh.renderBlock(x, y, z, renderer);
      rh.setTexture(getTexture(getCableColor()));
    } else if (ghh != null && partHost != null && ghh.getCableConnectionType(of.getOpposite()) == AECableType.COVERED && partHost.getColor() != AEColor.Transparent && partHost.getPart(of.getOpposite()) == null) {
      rh.setTexture(getTexture(partHost.getColor()));
    } else {
      rh.setTexture(getTexture(getCableColor()));
    }
    switch (of) {
      case DOWN:
        rh.setBounds(6.0F, 0.0F, 6.0F, 10.0F, 5.0F, 10.0F);
        break;
      case UP:
        rh.setBounds(11.0F, 6.0F, 6.0F, 16.0F, 10.0F, 10.0F);
        break;
      case NORTH:
        rh.setBounds(6.0F, 6.0F, 0.0F, 10.0F, 10.0F, 5.0F);
        break;
      case SOUTH:
        rh.setBounds(6.0F, 6.0F, 11.0F, 10.0F, 10.0F, 16.0F);
        break;
      case WEST:
        rh.setBounds(6.0F, 11.0F, 6.0F, 10.0F, 16.0F, 10.0F);
        break;
      case EAST:
        rh.setBounds(0.0F, 6.0F, 6.0F, 5.0F, 10.0F, 10.0F);
        break;
      default:
        return;
    }
    rh.renderBlock(x, y, z, renderer);
    rh.setFacesToRender(EnumSet.allOf(ForgeDirection.class));
    rh.setFacesToRender(EnumSet.allOf(ForgeDirection.class));
  }

  @SideOnly(Side.CLIENT)
  protected void renderSmartConnection(int x, int y, int z, IPartRenderHelper rh, RenderBlocks renderer, int channels, ForgeDirection of) {
    TileEntity te = this.getTile().getWorldObj().getTileEntity(x + of.offsetX, y + of.offsetY, z + of.offsetZ);
    IPartHost partHost = te instanceof IPartHost ? (IPartHost)te : null;
    IGridHost ghh = te instanceof IGridHost ? (IGridHost)te : null;
    AEColor myColor = this.getCableColor();
    rh.setFacesToRender(EnumSet.complementOf(EnumSet.of(of)));
    boolean isGlass = false;
    if (ghh != null && partHost != null && ghh.getCableConnectionType(of.getOpposite()) == AECableType.GLASS && partHost.getPart(of.getOpposite()) == null && partHost.getColor() != AEColor.Transparent) {
      isGlass = true;
      rh.setTexture(this.getTexture(myColor = partHost.getColor()));
    } else if (partHost == null && ghh != null && ghh.getCableConnectionType(of.getOpposite()) != AECableType.GLASS) {
      rh.setTexture(this.getTexture(myColor));
      switch (of) {
        case DOWN:
          rh.setBounds(5.0F, 0.0F, 5.0F, 11.0F, 4.0F, 11.0F);
          break;
        case EAST:
          rh.setBounds(12.0F, 5.0F, 5.0F, 16.0F, 11.0F, 11.0F);
          break;
        case NORTH:
          rh.setBounds(5.0F, 5.0F, 0.0F, 11.0F, 11.0F, 4.0F);
          break;
        case SOUTH:
          rh.setBounds(5.0F, 5.0F, 12.0F, 11.0F, 11.0F, 16.0F);
          break;
        case UP:
          rh.setBounds(5.0F, 12.0F, 5.0F, 11.0F, 16.0F, 11.0F);
          break;
        case WEST:
          rh.setBounds(0.0F, 5.0F, 5.0F, 4.0F, 11.0F, 11.0F);
          break;
        default:
          return;
      }

      rh.renderBlock(x, y, z, renderer);
      this.setSmartConnectionRotations(of, renderer);
      IIcon firstIcon = new TaughtIcon(this.getChannelTex(channels, false).getIcon(), -0.2F);
      IIcon secondIcon = new TaughtIcon(this.getChannelTex(channels, true).getIcon(), -0.2F);
      if (of == ForgeDirection.EAST || of == ForgeDirection.WEST) {
        AEBaseBlock blk = (AEBaseBlock)rh.getBlock();
        FlippableIcon ico = blk.getRendererInstance().getTexture(ForgeDirection.EAST);
        ico.setFlip(false, true);
      }

      Tessellator.instance.setBrightness(15728880);
      Tessellator.instance.setColorOpaque_I(myColor.blackVariant);
      rh.setTexture(firstIcon, firstIcon, firstIcon, firstIcon, firstIcon, firstIcon);
      this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
      Tessellator.instance.setColorOpaque_I(myColor.whiteVariant);
      rh.setTexture(secondIcon, secondIcon, secondIcon, secondIcon, secondIcon, secondIcon);
      this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
      renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth = renderer.uvRotateTop = renderer.uvRotateWest = 0;
      rh.setTexture(this.getTexture(this.getCableColor()));
    } else if (ghh != null && partHost != null && ghh.getCableConnectionType(of.getOpposite()) != AECableType.GLASS && partHost.getColor() != AEColor.Transparent && partHost.getPart(of.getOpposite()) == null) {
      rh.setTexture(this.getTexture(myColor = partHost.getColor()));
    } else {
      rh.setTexture(this.getTexture(this.getCableColor()));
    }

    switch (of) {
      case DOWN:
        rh.setBounds(6.0F, 0.0F, 6.0F, 10.0F, 5.0F, 10.0F);
        break;
      case EAST:
        rh.setBounds(11.0F, 6.0F, 6.0F, 16.0F, 10.0F, 10.0F);
        break;
      case NORTH:
        rh.setBounds(6.0F, 6.0F, 0.0F, 10.0F, 10.0F, 5.0F);
        break;
      case SOUTH:
        rh.setBounds(6.0F, 6.0F, 11.0F, 10.0F, 10.0F, 16.0F);
        break;
      case UP:
        rh.setBounds(6.0F, 11.0F, 6.0F, 10.0F, 16.0F, 10.0F);
        break;
      case WEST:
        rh.setBounds(0.0F, 6.0F, 6.0F, 5.0F, 10.0F, 10.0F);
        break;
      default:
        return;
    }

    rh.renderBlock(x, y, z, renderer);
    rh.setFacesToRender(EnumSet.allOf(ForgeDirection.class));
    if (!isGlass) {
      this.setSmartConnectionRotations(of, renderer);
      IIcon firstIcon = new TaughtIcon(this.getChannelTex(channels, false).getIcon(), -0.2F);
      IIcon secondIcon = new TaughtIcon(this.getChannelTex(channels, true).getIcon(), -0.2F);
      Tessellator.instance.setBrightness(15728880);
      Tessellator.instance.setColorOpaque_I(myColor.blackVariant);
      rh.setTexture(firstIcon, firstIcon, firstIcon, firstIcon, firstIcon, firstIcon);
      this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
      Tessellator.instance.setColorOpaque_I(myColor.whiteVariant);
      rh.setTexture(secondIcon, secondIcon, secondIcon, secondIcon, secondIcon, secondIcon);
      this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
      renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth = renderer.uvRotateTop = renderer.uvRotateWest = 0;
    }

  }

  @SideOnly(Side.CLIENT)
  protected void renderDenseConnection(int x, int y, int z, IPartRenderHelper rh, RenderBlocks renderer, int channels, ForgeDirection of) {
    TileEntity te = this.getTile().getWorldObj().getTileEntity(x + of.offsetX, y + of.offsetY, z + of.offsetZ);
    IPartHost partHost = te instanceof IPartHost ? (IPartHost)te : null;
    IGridHost ghh = te instanceof IGridHost ? (IGridHost)te : null;
    AEColor myColor = this.getCableColor();
    rh.setFacesToRender(EnumSet.complementOf(EnumSet.of(of, of.getOpposite())));
    if (ghh != null && partHost != null && ghh.getCableConnectionType(of) != AECableType.GLASS && partHost.getColor() != AEColor.Transparent && partHost.getPart(of.getOpposite()) == null) {
      rh.setTexture(this.getTexture(myColor = partHost.getColor()));
    } else {
      rh.setTexture(this.getTexture(this.getCableColor()));
    }

    switch (of) {
      case DOWN:
        rh.setBounds(4.0F, 0.0F, 4.0F, 12.0F, 5.0F, 12.0F);
        break;
      case EAST:
        rh.setBounds(11.0F, 4.0F, 4.0F, 16.0F, 12.0F, 12.0F);
        break;
      case NORTH:
        rh.setBounds(4.0F, 4.0F, 0.0F, 12.0F, 12.0F, 5.0F);
        break;
      case SOUTH:
        rh.setBounds(4.0F, 4.0F, 11.0F, 12.0F, 12.0F, 16.0F);
        break;
      case UP:
        rh.setBounds(4.0F, 11.0F, 4.0F, 12.0F, 16.0F, 12.0F);
        break;
      case WEST:
        rh.setBounds(0.0F, 4.0F, 4.0F, 5.0F, 12.0F, 12.0F);
        break;
      default:
        return;
    }

    rh.renderBlock(x, y, z, renderer);
    rh.setFacesToRender(EnumSet.allOf(ForgeDirection.class));
    boolean isGlass = false;
    this.setSmartConnectionRotations(of, renderer);
    IIcon firstIcon = new TaughtIcon(this.getChannelTex(channels, false).getIcon(), -0.2F);
    IIcon secondIcon = new TaughtIcon(this.getChannelTex(channels, true).getIcon(), -0.2F);
    Tessellator.instance.setBrightness(15728880);
    Tessellator.instance.setColorOpaque_I(myColor.blackVariant);
    rh.setTexture(firstIcon, firstIcon, firstIcon, firstIcon, firstIcon, firstIcon);
    this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
    Tessellator.instance.setColorOpaque_I(myColor.whiteVariant);
    rh.setTexture(secondIcon, secondIcon, secondIcon, secondIcon, secondIcon, secondIcon);
    this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
    renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth = renderer.uvRotateTop = renderer.uvRotateWest = 0;
  }

  @SideOnly(Side.CLIENT)
  protected void setSmartConnectionRotations(ForgeDirection of, RenderBlocks renderer) {
    switch (of) {
      case DOWN:
      case UP:
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
        renderer.uvRotateSouth = 3;
        renderer.uvRotateEast = 3;
        break;
      case EAST:
      case WEST:
        renderer.uvRotateEast = 2;
        renderer.uvRotateWest = 1;
        renderer.uvRotateBottom = 2;
        renderer.uvRotateTop = 1;
        renderer.uvRotateSouth = 3;
        renderer.uvRotateNorth = 0;
        break;
      case NORTH:
      case SOUTH:
        renderer.uvRotateTop = 3;
        renderer.uvRotateBottom = 3;
        renderer.uvRotateNorth = 1;
        renderer.uvRotateSouth = 2;
        renderer.uvRotateWest = 1;
    }

  }

  protected CableBusTextures getChannelTex(int i, boolean b) {
    if (!this.powered)
      i = 0;
    if (b) {
      switch (i) {
        default:
          return CableBusTextures.Channels10;
        case 5:
          return CableBusTextures.Channels11;
        case 6:
          return CableBusTextures.Channels12;
        case 7:
          return CableBusTextures.Channels13;
        case 8:
          break;
      }
      return CableBusTextures.Channels14;
    }
    switch (i) {
      case 0:
        return CableBusTextures.Channels00;
      case 1:
        return CableBusTextures.Channels01;
      case 2:
        return CableBusTextures.Channels02;
      case 3:
        return CableBusTextures.Channels03;
    }
    return CableBusTextures.Channels04;
  }

  @SideOnly(Side.CLIENT)
  protected void renderAllFaces(AEBaseBlock blk, int x, int y, int z, IPartRenderHelper rh, RenderBlocks renderer) {
    rh.setBounds((float)renderer.renderMinX * 16.0F, (float)renderer.renderMinY * 16.0F, (float)renderer.renderMinZ * 16.0F, (float)renderer.renderMaxX * 16.0F, (float)renderer.renderMaxY * 16.0F, (float)renderer.renderMaxZ * 16.0F);
    rh.renderFace(x, y, z, (IIcon)blk.getRendererInstance().getTexture(ForgeDirection.WEST), ForgeDirection.WEST, renderer);
    rh.renderFace(x, y, z, (IIcon)blk.getRendererInstance().getTexture(ForgeDirection.EAST), ForgeDirection.EAST, renderer);
    rh.renderFace(x, y, z, (IIcon)blk.getRendererInstance().getTexture(ForgeDirection.NORTH), ForgeDirection.NORTH, renderer);
    rh.renderFace(x, y, z, (IIcon)blk.getRendererInstance().getTexture(ForgeDirection.SOUTH), ForgeDirection.SOUTH, renderer);
    rh.renderFace(x, y, z, (IIcon)blk.getRendererInstance().getTexture(ForgeDirection.DOWN), ForgeDirection.DOWN, renderer);
    rh.renderFace(x, y, z, (IIcon)blk.getRendererInstance().getTexture(ForgeDirection.UP), ForgeDirection.UP, renderer);
  }

  public int[] getChannelsOnSide() {
    return this.channelsOnSide;
  }

  public EnumSet<ForgeDirection> getConnections() {
    return this.connections;
  }

  public void setConnections(EnumSet<ForgeDirection> connections) {
    this.connections = connections;
  }

  public abstract ItemStack getPartItemStack(AEColor paramAEColor);

  boolean isDense(ForgeDirection of) {
    TileEntity te = getTile().getWorldObj().getTileEntity((getTile()).xCoord + of.offsetX, (getTile()).yCoord + of.offsetY, (getTile()).zCoord + of.offsetZ);
    if (te instanceof IGridHost) {
      AECableType t = ((IGridHost)te).getCableConnectionType(of.getOpposite());
      return (t == AECableType.DENSE);
    }
    return false;
  }

  public IIcon getCoveredTexture(AEColor c) {
    switch (c) {
      case Black:
        return CableBusTextures.MECable_Black.getIcon();
      case Blue:
        return CableBusTextures.MECable_Blue.getIcon();
      case Brown:
        return CableBusTextures.MECable_Brown.getIcon();
      case Cyan:
        return CableBusTextures.MECable_Cyan.getIcon();
      case Gray:
        return CableBusTextures.MECable_Grey.getIcon();
      case Green:
        return CableBusTextures.MECable_Green.getIcon();
      case LightBlue:
        return CableBusTextures.MECable_LightBlue.getIcon();
      case LightGray:
        return CableBusTextures.MECable_LightGrey.getIcon();
      case Lime:
        return CableBusTextures.MECovered_Lime.getIcon();
      case Magenta:
        return CableBusTextures.MECovered_Magenta.getIcon();
      case Orange:
        return CableBusTextures.MECovered_Orange.getIcon();
      case Pink:
        return CableBusTextures.MECovered_Pink.getIcon();
      case Purple:
        return CableBusTextures.MECovered_Purple.getIcon();
      case Red:
        return CableBusTextures.MECovered_Red.getIcon();
      case White:
        return CableBusTextures.MECovered_White.getIcon();
      case Yellow:
        return CableBusTextures.MECovered_Yellow.getIcon();
    }
    AEColoredItemDefinition coveredCable = AEApi.instance().definitions().parts().cableCovered();
    ItemStack coveredCableStack = coveredCable.stack(AEColor.Transparent, 1);
    return coveredCable.item(AEColor.Transparent).getIconIndex(coveredCableStack);
  }
}