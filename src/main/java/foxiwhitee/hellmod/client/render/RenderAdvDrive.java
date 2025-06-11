package foxiwhitee.hellmod.client.render;

import appeng.client.render.BaseBlockRender;
import appeng.client.texture.ExtraBlockTextures;
import appeng.util.Platform;
import foxiwhitee.hellmod.blocks.BlockAdvancedDriver;
import foxiwhitee.hellmod.tile.TileAdvancedDrive;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

public class RenderAdvDrive  extends BaseBlockRender<BlockAdvancedDriver, TileAdvancedDrive> {
    public RenderAdvDrive() {
        super(false, 0.0D);
    }

    public void renderInventory(BlockAdvancedDriver block, ItemStack is, RenderBlocks renderer, IItemRenderer.ItemRenderType type, Object[] obj) {
        renderer.overrideBlockTexture = ExtraBlockTextures.getMissing();
        renderInvBlock(EnumSet.of(ForgeDirection.SOUTH), (BlockAdvancedDriver) block, is, Tessellator.instance, 0, renderer);
        renderer.overrideBlockTexture = null;
        super.renderInventory((BlockAdvancedDriver) block, is, renderer, type, obj);
    }

    public boolean renderInWorld(BlockAdvancedDriver imb, IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {
        TileAdvancedDrive sp = (TileAdvancedDrive)imb.getTileEntity(world, x, y, z);
        ForgeDirection up = sp.getUp();
        ForgeDirection forward = sp.getForward();
        ForgeDirection west = Platform.crossProduct(forward, up);
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        boolean result = super.renderInWorld((BlockAdvancedDriver) imb, world, x, y, z, renderer);
        Tessellator tess = Tessellator.instance;
        IIcon ico = ExtraBlockTextures.MEStorageCellTextures.getIcon();
        int b = world.getLightBrightnessForSkyBlocks(x + forward.offsetX, y + forward.offsetY, z + forward.offsetZ, 0);
        for (int yy = 0; yy < 6; yy++) {
            for (int xx = 0; xx < 5; xx++) {
                int stat = sp.getCellStatus(yy * 5 + xx);
                double yA = (yy == 0) ? 0.0D : (0.5D * yy);
                selectFaceOwn(renderer, west, up, forward, (4 + xx * 5), (8 + xx * 5), (4 + yy * 4), (7 + yy * 4));
                int spin = 0;
                int offset = forward.offsetX + forward.offsetY * 2 + forward.offsetZ * 3;
                switch (offset) {
                    case 1:
                        switch (up) {
                            case UP:
                                spin = 3;
                                break;
                            case DOWN:
                                spin = 1;
                                break;
                            case NORTH:
                                spin = 0;
                                break;
                            case SOUTH:
                                spin = 2;
                                break;
                        }
                        break;
                    case -1:
                        switch (up) {
                            case UP:
                                spin = 1;
                                break;
                            case DOWN:
                                spin = 3;
                                break;
                            case NORTH:
                                spin = 0;
                                break;
                            case SOUTH:
                                spin = 2;
                                break;
                        }
                        break;
                    case -2:
                        switch (up) {
                            case EAST:
                                spin = 1;
                                break;
                            case WEST:
                                spin = 3;
                                break;
                            case NORTH:
                                spin = 2;
                                break;
                            case SOUTH:
                                spin = 0;
                                break;
                        }
                        break;
                    case 2:
                        switch (up) {
                            case EAST:
                                spin = 1;
                                break;
                            case WEST:
                                spin = 3;
                                break;
                            case NORTH:
                                spin = 0;
                                break;
                            case SOUTH:
                                spin = 0;
                                break;
                        }
                        break;
                    case 3:
                        switch (up) {
                            case UP:
                                spin = 2;
                                break;
                            case DOWN:
                                spin = 0;
                                break;
                            case EAST:
                                spin = 3;
                                break;
                            case WEST:
                                spin = 1;
                                break;
                        }
                        break;
                    case -3:
                        switch (up) {
                            case UP:
                                spin = 2;
                                break;
                            case DOWN:
                                spin = 0;
                                break;
                            case EAST:
                                spin = 1;
                                break;
                            case WEST:
                                spin = 3;
                                break;
                        }
                        break;
                }
                double u1 = ico.getInterpolatedU((spin % 4 < 2) ? 1.0D : 5.0D);
                double u2 = ico.getInterpolatedU(((spin + 1) % 4 < 2) ? 1.0D : 5.0D);
                double u3 = ico.getInterpolatedU(((spin + 2) % 4 < 2) ? 1.0D : 5.0D);
                double u4 = ico.getInterpolatedU(((spin + 3) % 4 < 2) ? 1.0D : 5.0D);
                int m = 0;
                int mx = 3;
                if (stat == 0) {
                    m = 4;
                    mx = 5;
                }
                double v1 = ico.getInterpolatedV(((spin + 1) % 4 < 2) ? m : mx);
                double v2 = ico.getInterpolatedV(((spin + 2) % 4 < 2) ? m : mx);
                double v3 = ico.getInterpolatedV(((spin + 3) % 4 < 2) ? m : mx);
                double v4 = ico.getInterpolatedV((spin % 4 < 2) ? m : mx);
                tess.setBrightness(b);
                tess.setColorOpaque_I(16777215);
                switch (offset) {
                    case 1:
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMinZ, u1, v1);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMaxZ, u3, v3);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMinZ, u4, v4);
                        break;
                    case -1:
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMinZ, u1, v1);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMaxZ, u2, v2);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMinZ, u4, v4);
                        break;
                    case -2:
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMinZ, u1, v1);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                        tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                        tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMinZ, u4, v4);
                        break;
                    case 2:
                        tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMinZ, u1, v1);
                        tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMinZ, u4, v4);
                        break;
                    case 3:
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMaxZ, u1, v1);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                        tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                        tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMinY, z + renderer.renderMaxZ, u4, v4);
                        break;
                    case -3:
                        tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMinY, z + renderer.renderMaxZ, u1, v1);
                        tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                        tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMaxZ, u4, v4);
                        break;
                }
                if ((forward == ForgeDirection.UP && up == ForgeDirection.SOUTH) || forward == ForgeDirection.DOWN) {
                    selectFaceOwn(renderer, west, up, forward, (5 + xx * 5), (6 + xx * 5), (5 + yy * 4), (4 + yy * 4));
                } else {
                    selectFaceOwn(renderer, west, up, forward, (7 + xx * 5), (6 + xx * 5), (6 + yy * 4), (7 + yy * 4));
                }
                if (stat != 0) {
                    IIcon whiteIcon = ExtraBlockTextures.White.getIcon();
                    u1 = whiteIcon.getInterpolatedU((spin % 4 < 2) ? 1.0D : 6.0D);
                    u2 = whiteIcon.getInterpolatedU(((spin + 1) % 4 < 2) ? 1.0D : 6.0D);
                    u3 = whiteIcon.getInterpolatedU(((spin + 2) % 4 < 2) ? 1.0D : 6.0D);
                    u4 = whiteIcon.getInterpolatedU(((spin + 3) % 4 < 2) ? 1.0D : 6.0D);
                    v1 = whiteIcon.getInterpolatedV(((spin + 1) % 4 < 2) ? 1.0D : 3.0D);
                    v2 = whiteIcon.getInterpolatedV(((spin + 2) % 4 < 2) ? 1.0D : 3.0D);
                    v3 = whiteIcon.getInterpolatedV(((spin + 3) % 4 < 2) ? 1.0D : 3.0D);
                    v4 = whiteIcon.getInterpolatedV((spin % 4 < 2) ? 1.0D : 3.0D);
                    if (sp.isPowered()) {
                        tess.setBrightness(15728880);
                    } else {
                        tess.setBrightness(0);
                    }
                    if (stat == 1)
                        Tessellator.instance.setColorOpaque_I(65280);
                    if (stat == 2)
                        Tessellator.instance.setColorOpaque_I(16755200);
                    if (stat == 3)
                        Tessellator.instance.setColorOpaque_I(16711680);
                    switch (offset) {
                        case 1:
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMinZ, u1, v1);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMaxZ, u3, v3);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMinZ, u4, v4);
                            break;
                        case -1:
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMinZ, u1, v1);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMaxZ, u2, v2);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMinZ, u4, v4);
                            break;
                        case -2:
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMinZ, u1, v1);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                            tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                            tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMinZ, u4, v4);
                            break;
                        case 2:
                            tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMinZ, u1, v1);
                            tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMinZ, u4, v4);
                            break;
                        case 3:
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMaxZ, u1, v1);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                            tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                            tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMinY, z + renderer.renderMaxZ, u4, v4);
                            break;
                        case -3:
                            tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMinY, z + renderer.renderMaxZ, u1, v1);
                            tess.addVertexWithUV(x + renderer.renderMinX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u2, v2);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMaxY, z + renderer.renderMaxZ, u3, v3);
                            tess.addVertexWithUV(x + renderer.renderMaxX, y + renderer.renderMinY, z + renderer.renderMaxZ, u4, v4);
                            break;
                    }
                }
            }
        }
        renderer.overrideBlockTexture = null;
        return result;
    }

    public void selectFaceOwn(RenderBlocks renderer, ForgeDirection west, ForgeDirection up, ForgeDirection forward, double u1, double u2, double v1, double v2) {
        v1 = 32.0D - v1;
        v2 = 32.0D - v2;
        double minX = ((forward.offsetX > 0) ? 1 : 0) + mapFaceUV(west.offsetX, u1) + mapFaceUV(up.offsetX, v1);
        double minY = ((forward.offsetY > 0) ? 1 : 0) + mapFaceUV(west.offsetY, u1) + mapFaceUV(up.offsetY, v1);
        double minZ = ((forward.offsetZ > 0) ? 1 : 0) + mapFaceUV(west.offsetZ, u1) + mapFaceUV(up.offsetZ, v1);
        double maxX = ((forward.offsetX > 0) ? 1 : 0) + mapFaceUV(west.offsetX, u2) + mapFaceUV(up.offsetX, v2);
        double maxY = ((forward.offsetY > 0) ? 1 : 0) + mapFaceUV(west.offsetY, u2) + mapFaceUV(up.offsetY, v2);
        double maxZ = ((forward.offsetZ > 0) ? 1 : 0) + mapFaceUV(west.offsetZ, u2) + mapFaceUV(up.offsetZ, v2);
        renderer.renderMinX = Math.max(0.0D, Math.min(minX, maxX) - ((forward.offsetX != 0) ? 0.0D : 0.001D));
        renderer.renderMaxX = Math.min(1.0D, Math.max(minX, maxX) + ((forward.offsetX != 0) ? 0.0D : 0.001D));
        renderer.renderMinY = Math.max(0.0D, Math.min(minY, maxY) - ((forward.offsetY != 0) ? 0.0D : 0.001D));
        renderer.renderMaxY = Math.min(1.0D, Math.max(minY, maxY) + ((forward.offsetY != 0) ? 0.0D : 0.001D));
        renderer.renderMinZ = Math.max(0.0D, Math.min(minZ, maxZ) - ((forward.offsetZ != 0) ? 0.0D : 0.001D));
        renderer.renderMaxZ = Math.min(1.0D, Math.max(minZ, maxZ) + ((forward.offsetZ != 0) ? 0.0D : 0.001D));
    }

    private double mapFaceUV(int offset, double uv) {
        if (offset == 0)
            return 0.0D;
        return (offset > 0) ? (uv / 32.0D) : ((32.0D - uv) / 32.0D);
    }
}