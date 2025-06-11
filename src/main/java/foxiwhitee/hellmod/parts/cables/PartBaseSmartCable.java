package foxiwhitee.hellmod.parts.cables;

import appeng.api.networking.IGridNode;
import appeng.api.parts.*;
import appeng.api.util.AECableType;
import appeng.block.AEBaseBlock;
import appeng.client.texture.FlippableIcon;
import appeng.client.texture.OffsetIcon;
import appeng.client.texture.TaughtIcon;
import appeng.util.Platform;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.EnumSet;
import java.util.Iterator;

public abstract class PartBaseSmartCable extends PartBaseCable{

    public PartBaseSmartCable(ItemStack is) {
        super(is);
    }

    public AECableType getCableConnectionType() {
        return AECableType.SMART;
    }

    public void getBoxes(IPartCollisionHelper bch) {
        bch.addBox(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);
        if (Platform.isServer()) {
            IGridNode n = getGridNode();
            if (n != null) {
                setConnections(n.getConnectedSides());
            } else {
                getConnections().clear();
            }
        }
        for (ForgeDirection of : getConnections()) {
            switch (of) {
                case DOWN:
                    bch.addBox(5.0D, 0.0D, 5.0D, 11.0D, 5.0D, 11.0D);
                    break;
                case EAST:
                    bch.addBox(11.0D, 5.0D, 5.0D, 16.0D, 11.0D, 11.0D);
                    break;
                case NORTH:
                    bch.addBox(5.0D, 5.0D, 0.0D, 11.0D, 11.0D, 5.0D);
                    break;
                case SOUTH:
                    bch.addBox(5.0D, 5.0D, 11.0D, 11.0D, 11.0D, 16.0D);
                    break;
                case UP:
                    bch.addBox(5.0D, 11.0D, 5.0D, 11.0D, 16.0D, 11.0D);
                    break;
                case WEST:
                    bch.addBox(0.0D, 5.0D, 5.0D, 5.0D, 11.0D, 11.0D);
                    break;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void renderInventory(IPartRenderHelper rh, RenderBlocks renderer) {
        GL11.glTranslated(-0.0D, -0.0D, 0.3D);
        float offU = 0.0F;
        float offV = 9.0F;
        OffsetIcon main = new OffsetIcon(getTexture(getCableColor()), offU, offV);
        OffsetIcon ch1 = new OffsetIcon(getChannelTex(4, false).getIcon(), offU, offV);
        OffsetIcon ch2 = new OffsetIcon(getChannelTex(4, true).getIcon(), offU, offV);
        for (ForgeDirection side : EnumSet.<ForgeDirection>of(ForgeDirection.UP, ForgeDirection.DOWN)) {
            rh.setBounds(5.0F, 5.0F, 2.0F, 11.0F, 11.0F, 14.0F);
            rh.renderInventoryFace((IIcon)main, side, renderer);
            rh.renderInventoryFace((IIcon)ch1, side, renderer);
            rh.renderInventoryFace((IIcon)ch2, side, renderer);
        }
        offU = 9.0F;
        offV = 0.0F;
        main = new OffsetIcon(getTexture(getCableColor()), offU, offV);
        ch1 = new OffsetIcon(getChannelTex(4, false).getIcon(), offU, offV);
        ch2 = new OffsetIcon(getChannelTex(4, true).getIcon(), offU, offV);
        for (ForgeDirection side : EnumSet.<ForgeDirection>of(ForgeDirection.EAST, ForgeDirection.WEST)) {
            rh.setBounds(5.0F, 5.0F, 2.0F, 11.0F, 11.0F, 14.0F);
            rh.renderInventoryFace((IIcon)main, side, renderer);
            rh.renderInventoryFace((IIcon)ch1, side, renderer);
            rh.renderInventoryFace((IIcon)ch2, side, renderer);
        }
        main = new OffsetIcon(getTexture(getCableColor()), 0.0F, 0.0F);
        ch1 = new OffsetIcon(getChannelTex(4, false).getIcon(), 0.0F, 0.0F);
        ch2 = new OffsetIcon(getChannelTex(4, true).getIcon(), 0.0F, 0.0F);
        for (ForgeDirection side : EnumSet.<ForgeDirection>of(ForgeDirection.SOUTH, ForgeDirection.NORTH)) {
            rh.setBounds(5.0F, 5.0F, 2.0F, 11.0F, 11.0F, 14.0F);
            rh.renderInventoryFace((IIcon)main, side, renderer);
            rh.renderInventoryFace((IIcon)ch1, side, renderer);
            rh.renderInventoryFace((IIcon)ch2, side, renderer);
        }
        rh.setTexture(null);
    }

    @SideOnly(Side.CLIENT)
    public void renderStatic(int x, int y, int z, IPartRenderHelper rh, RenderBlocks renderer) {
        setRenderCache(rh.useSimplifiedRendering(x, y, z, (IBoxProvider)this, getRenderCache()));
        rh.setTexture(getTexture(getCableColor()));
        EnumSet<ForgeDirection> sides = getConnections().clone();
        boolean hasBuses = false;
        IPartHost ph = getHost();
        for (ForgeDirection of : EnumSet.<ForgeDirection>complementOf(getConnections())) {
            IPart bp = ph.getPart(of);
            if (bp instanceof appeng.api.networking.IGridHost) {
                if (of != ForgeDirection.UNKNOWN) {
                    sides.add(of);
                    hasBuses = true;
                }
                int len = bp.cableConnectionRenderTo();
                if (len < 8) {
                    switch (of) {
                        case DOWN:
                            rh.setBounds(6.0F, len, 6.0F, 10.0F, 5.0F, 10.0F);
                            break;
                        case EAST:
                            rh.setBounds(11.0F, 6.0F, 6.0F, (16 - len), 10.0F, 10.0F);
                            break;
                        case NORTH:
                            rh.setBounds(6.0F, 6.0F, len, 10.0F, 10.0F, 5.0F);
                            break;
                        case SOUTH:
                            rh.setBounds(6.0F, 6.0F, 11.0F, 10.0F, 10.0F, (16 - len));
                            break;
                        case UP:
                            rh.setBounds(6.0F, 11.0F, 6.0F, 10.0F, (16 - len), 10.0F);
                            break;
                        case WEST:
                            rh.setBounds(len, 6.0F, 6.0F, 5.0F, 10.0F, 10.0F);
                            break;
                        default:
                            continue;
                    }
                    rh.renderBlock(x, y, z, renderer);
                    setSmartConnectionRotations(of, renderer);
                    TaughtIcon taughtIcon1 = new TaughtIcon(getChannelTex(getChannelsOnSide()[of.ordinal()], false).getIcon(), -0.2F);
                    TaughtIcon taughtIcon2 = new TaughtIcon(getChannelTex(getChannelsOnSide()[of.ordinal()], true).getIcon(), -0.2F);
                    if (of == ForgeDirection.EAST || of == ForgeDirection.WEST) {
                        AEBaseBlock blk = (AEBaseBlock)rh.getBlock();
                        FlippableIcon ico = blk.getRendererInstance().getTexture(ForgeDirection.EAST);
                        ico.setFlip(false, true);
                    }
                    Tessellator.instance.setBrightness(15728880);
                    Tessellator.instance.setColorOpaque_I((getCableColor()).blackVariant);
                    rh.setTexture((IIcon)taughtIcon1, (IIcon)taughtIcon1, (IIcon)taughtIcon1, (IIcon)taughtIcon1, (IIcon)taughtIcon1, (IIcon)taughtIcon1);
                    renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    Tessellator.instance.setColorOpaque_I((getCableColor()).whiteVariant);
                    rh.setTexture((IIcon)taughtIcon2, (IIcon)taughtIcon2, (IIcon)taughtIcon2, (IIcon)taughtIcon2, (IIcon)taughtIcon2, (IIcon)taughtIcon2);
                    renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth = renderer.uvRotateTop = renderer.uvRotateWest = 0;
                    rh.setTexture(getTexture(getCableColor()));
                }
            }
        }
        if (sides.size() != 2 || !nonLinear(sides) || hasBuses) {
            for (ForgeDirection of : getConnections())
                renderSmartConnection(x, y, z, rh, renderer, getChannelsOnSide()[of.ordinal()], of);
            rh.setTexture(getCoveredTexture(getCableColor()));
            rh.setBounds(5.0F, 5.0F, 5.0F, 11.0F, 11.0F, 11.0F);
            rh.renderBlock(x, y, z, renderer);
        } else {
            AEBaseBlock blk;
            FlippableIcon ico, fpA, fpB;
            ForgeDirection selectedSide = ForgeDirection.UNKNOWN;
            Iterator<ForgeDirection> iterator = getConnections().iterator();
            if (iterator.hasNext()) {
                ForgeDirection of = iterator.next();
                selectedSide = of;
            }
            int channels = getChannelsOnSide()[selectedSide.ordinal()];
            IIcon def = getTexture(getCableColor());
            OffsetIcon offsetIcon1 = new OffsetIcon(def, 0.0F, -12.0F);
            TaughtIcon taughtIcon1 = new TaughtIcon(getChannelTex(channels, false).getIcon(), -0.2F);
            OffsetIcon offsetIcon2 = new OffsetIcon((IIcon)taughtIcon1, 0.0F, -12.0F);
            TaughtIcon taughtIcon2 = new TaughtIcon(getChannelTex(channels, true).getIcon(), -0.2F);
            OffsetIcon offsetIcon3 = new OffsetIcon((IIcon)taughtIcon2, 0.0F, -12.0F);
            switch (selectedSide) {
                case DOWN:
                case UP:
                    renderer.setRenderBounds(0.3125D, 0.0D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
                    rh.setTexture(def, def, (IIcon)offsetIcon1, (IIcon)offsetIcon1, (IIcon)offsetIcon1, (IIcon)offsetIcon1);
                    rh.renderBlockCurrentBounds(x, y, z, renderer);
                    renderer.uvRotateTop = 0;
                    renderer.uvRotateBottom = 0;
                    renderer.uvRotateSouth = 3;
                    renderer.uvRotateEast = 3;
                    Tessellator.instance.setBrightness(15728880);
                    Tessellator.instance.setColorOpaque_I((getCableColor()).blackVariant);
                    rh.setTexture((IIcon)taughtIcon1, (IIcon)taughtIcon1, (IIcon)offsetIcon2, (IIcon)offsetIcon2, (IIcon)offsetIcon2, (IIcon)offsetIcon2);
                    renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    Tessellator.instance.setColorOpaque_I((getCableColor()).whiteVariant);
                    rh.setTexture((IIcon)taughtIcon2, (IIcon)taughtIcon2, (IIcon)offsetIcon3, (IIcon)offsetIcon3, (IIcon)offsetIcon3, (IIcon)offsetIcon3);
                    renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    break;
                case EAST:
                case WEST:
                    rh.setTexture((IIcon)offsetIcon1, (IIcon)offsetIcon1, (IIcon)offsetIcon1, (IIcon)offsetIcon1, def, def);
                    renderer.uvRotateEast = 2;
                    renderer.uvRotateWest = 1;
                    renderer.uvRotateBottom = 2;
                    renderer.uvRotateTop = 1;
                    renderer.uvRotateSouth = 0;
                    renderer.uvRotateNorth = 0;
                    blk = (AEBaseBlock)rh.getBlock();
                    ico = blk.getRendererInstance().getTexture(ForgeDirection.EAST);
                    ico.setFlip(false, true);
                    renderer.setRenderBounds(0.0D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D);
                    rh.renderBlockCurrentBounds(x, y, z, renderer);
                    Tessellator.instance.setBrightness(15728880);
                    fpA = new FlippableIcon((IIcon)taughtIcon1);
                    fpB = new FlippableIcon((IIcon)taughtIcon2);
                    fpA = new FlippableIcon((IIcon)taughtIcon1);
                    fpB = new FlippableIcon((IIcon)taughtIcon2);
                    fpA.setFlip(true, false);
                    fpB.setFlip(true, false);
                    Tessellator.instance.setColorOpaque_I((getCableColor()).blackVariant);
                    rh.setTexture((IIcon)offsetIcon2, (IIcon)offsetIcon2, (IIcon)offsetIcon2, (IIcon)offsetIcon2, (IIcon)taughtIcon1, (IIcon)fpA);
                    renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    Tessellator.instance.setColorOpaque_I((getCableColor()).whiteVariant);
                    rh.setTexture((IIcon)offsetIcon3, (IIcon)offsetIcon3, (IIcon)offsetIcon3, (IIcon)offsetIcon3, (IIcon)taughtIcon2, (IIcon)fpB);
                    renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    break;
                case NORTH:
                case SOUTH:
                    rh.setTexture((IIcon)offsetIcon1, (IIcon)offsetIcon1, def, def, (IIcon)offsetIcon1, (IIcon)offsetIcon1);
                    renderer.uvRotateTop = 3;
                    renderer.uvRotateBottom = 3;
                    renderer.uvRotateNorth = 1;
                    renderer.uvRotateSouth = 2;
                    renderer.uvRotateWest = 1;
                    renderer.setRenderBounds(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 1.0D);
                    rh.renderBlockCurrentBounds(x, y, z, renderer);
                    Tessellator.instance.setBrightness(15728880);
                    Tessellator.instance.setColorOpaque_I((getCableColor()).blackVariant);
                    rh.setTexture((IIcon)offsetIcon2, (IIcon)offsetIcon2, (IIcon)taughtIcon1, (IIcon)taughtIcon1, (IIcon)offsetIcon2, (IIcon)offsetIcon2);
                    renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    Tessellator.instance.setColorOpaque_I((getCableColor()).whiteVariant);
                    rh.setTexture((IIcon)offsetIcon3, (IIcon)offsetIcon3, (IIcon)taughtIcon2, (IIcon)taughtIcon2, (IIcon)offsetIcon3, (IIcon)offsetIcon3);
                    renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    break;
            }
        }
        renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth = renderer.uvRotateTop = renderer.uvRotateWest = 0;
        rh.setTexture(null);
    }
}
