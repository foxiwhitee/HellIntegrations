package foxiwhitee.hellmod.parts.cables;

import appeng.api.networking.IGridNode;
import appeng.api.parts.BusSupport;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartRenderHelper;
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

public abstract class PartBaseDenseCable extends PartBaseCable{
    public PartBaseDenseCable(ItemStack is) {
        super(is);
    }

    public BusSupport supportsBuses() {
        return BusSupport.DENSE_CABLE;
    }

    public AECableType getCableConnectionType() {
        return AECableType.DENSE;
    }

    public void getBoxes(IPartCollisionHelper bch) {
        boolean noLadder = !bch.isBBCollision();
        double min = noLadder ? 3.0D : 4.9D;
        double max = noLadder ? 13.0D : 11.1D;
        bch.addBox(min, min, min, max, max, max);
        if (Platform.isServer()) {
            IGridNode n = getGridNode();
            if (n != null) {
                setConnections(n.getConnectedSides());
            } else {
                getConnections().clear();
            }
        }
        for (ForgeDirection of : getConnections()) {
            if (isDense(of)) {
                switch (of) {
                    case DOWN:
                        bch.addBox(min, 0.0D, min, max, min, max);
                        continue;
                    case EAST:
                        bch.addBox(max, min, min, 16.0D, max, max);
                        continue;
                    case NORTH:
                        bch.addBox(min, min, 0.0D, max, max, min);
                        continue;
                    case SOUTH:
                        bch.addBox(min, min, max, max, max, 16.0D);
                        continue;
                    case UP:
                        bch.addBox(min, max, min, max, 16.0D, max);
                        continue;
                    case WEST:
                        bch.addBox(0.0D, min, min, min, max, max);
                        continue;
                }
                continue;
            }
            switch (of) {
                case DOWN:
                    bch.addBox(5.0D, 0.0D, 5.0D, 11.0D, 5.0D, 11.0D);
                case EAST:
                    bch.addBox(11.0D, 5.0D, 5.0D, 16.0D, 11.0D, 11.0D);
                case NORTH:
                    bch.addBox(5.0D, 5.0D, 0.0D, 11.0D, 11.0D, 5.0D);
                case SOUTH:
                    bch.addBox(5.0D, 5.0D, 11.0D, 11.0D, 11.0D, 16.0D);
                case UP:
                    bch.addBox(5.0D, 11.0D, 5.0D, 11.0D, 16.0D, 11.0D);
                case WEST:
                    bch.addBox(0.0D, 5.0D, 5.0D, 5.0D, 11.0D, 11.0D);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void renderInventory(IPartRenderHelper rh, RenderBlocks renderer) {
        GL11.glTranslated(-0.0D, -0.0D, 0.3D);
        rh.setBounds(4.0F, 4.0F, 2.0F, 12.0F, 12.0F, 14.0F);
        float offU = 0.0F;
        float offV = 9.0F;
        OffsetIcon main = new OffsetIcon(getTexture(getCableColor()), offU, offV);
        OffsetIcon ch1 = new OffsetIcon(getChannelTex(4, false).getIcon(), offU, offV);
        OffsetIcon ch2 = new OffsetIcon(getChannelTex(4, true).getIcon(), offU, offV);
        for (ForgeDirection side : EnumSet.<ForgeDirection>of(ForgeDirection.UP, ForgeDirection.DOWN)) {
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
            rh.renderInventoryFace((IIcon)main, side, renderer);
            rh.renderInventoryFace((IIcon)ch1, side, renderer);
            rh.renderInventoryFace((IIcon)ch2, side, renderer);
        }
        main = new OffsetIcon(getTexture(getCableColor()), 0.0F, 0.0F);
        ch1 = new OffsetIcon(getChannelTex(4, false).getIcon(), 0.0F, 0.0F);
        ch2 = new OffsetIcon(getChannelTex(4, true).getIcon(), 0.0F, 0.0F);
        for (ForgeDirection side : EnumSet.<ForgeDirection>of(ForgeDirection.SOUTH, ForgeDirection.NORTH)) {
            rh.renderInventoryFace((IIcon)main, side, renderer);
            rh.renderInventoryFace((IIcon)ch1, side, renderer);
            rh.renderInventoryFace((IIcon)ch2, side, renderer);
        }
        rh.setTexture(null);
    }

    @SideOnly(Side.CLIENT)
    public void renderStatic(int x, int y, int z, IPartRenderHelper rh, RenderBlocks renderer) {
        this.setRenderCache(rh.useSimplifiedRendering(x, y, z, this, this.getRenderCache()));
        rh.setTexture(this.getTexture(this.getCableColor()));
        EnumSet<ForgeDirection> sides = this.getConnections().clone();
        boolean hasBuses = false;

        for(ForgeDirection of : this.getConnections()) {
            if (!this.isDense(of)) {
                hasBuses = true;
            }
        }

        if (sides.size() == 2 && this.nonLinear(sides) && !hasBuses) {
            ForgeDirection selectedSide = ForgeDirection.UNKNOWN;
            Iterator i$ = this.getConnections().iterator();
            if (i$.hasNext()) {
                ForgeDirection of = (ForgeDirection)i$.next();
                selectedSide = of;
            }

            int channels = this.getChannelsOnSide()[selectedSide.ordinal()];
            IIcon def = this.getTexture(this.getCableColor());
            IIcon off = new OffsetIcon(def, 0.0F, -12.0F);
            IIcon firstIcon = new TaughtIcon(this.getChannelTex(channels, false).getIcon(), -0.2F);
            IIcon firstOffset = new OffsetIcon(firstIcon, 0.0F, -12.0F);
            IIcon secondIcon = new TaughtIcon(this.getChannelTex(channels, true).getIcon(), -0.2F);
            IIcon secondOffset = new OffsetIcon(secondIcon, 0.0F, -12.0F);
            switch (selectedSide) {
                case DOWN:
                case UP:
                    renderer.setRenderBounds((double)0.1875F, (double)0.0F, (double)0.1875F, (double)0.8125F, (double)1.0F, (double)0.8125F);
                    rh.setTexture(def, def, off, off, off, off);
                    rh.renderBlockCurrentBounds(x, y, z, renderer);
                    renderer.uvRotateTop = 0;
                    renderer.uvRotateBottom = 0;
                    renderer.uvRotateSouth = 3;
                    renderer.uvRotateEast = 3;
                    Tessellator.instance.setBrightness(15728880);
                    Tessellator.instance.setColorOpaque_I(this.getCableColor().blackVariant);
                    rh.setTexture(firstIcon, firstIcon, firstOffset, firstOffset, firstOffset, firstOffset);
                    this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    Tessellator.instance.setColorOpaque_I(this.getCableColor().whiteVariant);
                    rh.setTexture(secondIcon, secondIcon, secondOffset, secondOffset, secondOffset, secondOffset);
                    this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    break;
                case EAST:
                case WEST:
                    rh.setTexture(off, off, off, off, def, def);
                    renderer.uvRotateEast = 2;
                    renderer.uvRotateWest = 1;
                    renderer.uvRotateBottom = 2;
                    renderer.uvRotateTop = 1;
                    renderer.uvRotateSouth = 0;
                    renderer.uvRotateNorth = 0;
                    AEBaseBlock blk = (AEBaseBlock)rh.getBlock();
                    FlippableIcon ico = blk.getRendererInstance().getTexture(ForgeDirection.EAST);
                    ico.setFlip(false, true);
                    renderer.setRenderBounds((double)0.0F, (double)0.1875F, (double)0.1875F, (double)1.0F, (double)0.8125F, (double)0.8125F);
                    rh.renderBlockCurrentBounds(x, y, z, renderer);
                    Tessellator.instance.setBrightness(15728880);
                    FlippableIcon fpA = new FlippableIcon(firstIcon);
                    FlippableIcon fpB = new FlippableIcon(secondIcon);
                    fpA.setFlip(true, false);
                    fpB.setFlip(true, false);
                    Tessellator.instance.setColorOpaque_I(this.getCableColor().blackVariant);
                    rh.setTexture(firstOffset, firstOffset, firstOffset, firstOffset, firstIcon, fpA);
                    this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    Tessellator.instance.setColorOpaque_I(this.getCableColor().whiteVariant);
                    rh.setTexture(secondOffset, secondOffset, secondOffset, secondOffset, secondIcon, fpB);
                    this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    break;
                case NORTH:
                case SOUTH:
                    rh.setTexture(off, off, def, def, off, off);
                    renderer.uvRotateTop = 3;
                    renderer.uvRotateBottom = 3;
                    renderer.uvRotateNorth = 1;
                    renderer.uvRotateSouth = 2;
                    renderer.uvRotateWest = 1;
                    renderer.setRenderBounds((double)0.1875F, (double)0.1875F, (double)0.0F, (double)0.8125F, (double)0.8125F, (double)1.0F);
                    rh.renderBlockCurrentBounds(x, y, z, renderer);
                    Tessellator.instance.setBrightness(15728880);
                    Tessellator.instance.setColorOpaque_I(this.getCableColor().blackVariant);
                    rh.setTexture(firstOffset, firstOffset, firstIcon, firstIcon, firstOffset, firstOffset);
                    this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
                    Tessellator.instance.setColorOpaque_I(this.getCableColor().whiteVariant);
                    rh.setTexture(secondOffset, secondOffset, secondIcon, secondIcon, secondOffset, secondOffset);
                    this.renderAllFaces((AEBaseBlock)rh.getBlock(), x, y, z, rh, renderer);
            }
        } else {
            for(ForgeDirection of : this.getConnections()) {
                if (this.isDense(of)) {
                    this.renderDenseConnection(x, y, z, rh, renderer, this.getChannelsOnSide()[of.ordinal()], of);
                } else if (this.isSmart(of)) {
                    this.renderSmartConnection(x, y, z, rh, renderer, this.getChannelsOnSide()[of.ordinal()], of);
                } else {
                    this.renderCoveredConnection(x, y, z, rh, renderer, this.getChannelsOnSide()[of.ordinal()], of);
                }
            }

            rh.setTexture(this.getTexture(this.getCableColor()));
            rh.setBounds(3.0F, 3.0F, 3.0F, 13.0F, 13.0F, 13.0F);
            rh.renderBlock(x, y, z, renderer);
        }

        renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth = renderer.uvRotateTop = renderer.uvRotateWest = 0;
        rh.setTexture((IIcon)null);
    }
}
