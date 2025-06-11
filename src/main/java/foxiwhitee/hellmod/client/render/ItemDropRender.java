package foxiwhitee.hellmod.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Map;

import foxiwhitee.hellmod.items.ItemFluidDrop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemDropRender implements IItemRenderer {
    private final Map<String, Integer> colourCache = new HashMap<>();

    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return (type != IItemRenderer.ItemRenderType.FIRST_PERSON_MAP);
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return (type == IItemRenderer.ItemRenderType.ENTITY);
    }

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        IIcon shape = ItemFluidDrop.DROP.shape;
        GL11.glEnable(3042);
        GL11.glEnable(3008);
        if (type.equals(IItemRenderer.ItemRenderType.ENTITY)) {
            GL11.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
            GL11.glTranslated(-0.5D, -0.6D, 0.0D);
        }
        (Minecraft.getMinecraft()).renderEngine.bindTexture(TextureMap.locationItemsTexture);
        GL11.glBlendFunc(770, 771);
        FluidStack fluid = ItemFluidDrop.getFluidStack(item);
        if (fluid != null) {
            int RGB = getColour(fluid);
            GL11.glColor3f((RGB >> 16 & 0xFF) / 255.0F, (RGB >> 8 & 0xFF) / 255.0F, (RGB & 0xFF) / 255.0F);
        }
        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            renderItemIcon(shape, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
        } else {
            ItemRenderer.renderItemIn2D(Tessellator.instance, shape.getMaxU(), shape.getMinV(), shape.getMinU(), shape.getMaxV(), shape.getIconWidth(), shape.getIconHeight(), 0.0625F);
        }
        GL11.glDisable(3008);
        GL11.glDisable(3042);
    }

    public int getColour(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        int colour = fluid.getColor(fluidStack);
        return (colour != 16777215) ? colour : getColour(fluid);
    }

    public int getColour(Fluid fluid) {
        Integer cached = this.colourCache.get(fluid.getName());
        if (cached != null)
            return cached.intValue();
        int colour = fluid.getColor();
        if (colour == 16777215) {
            TextureAtlasSprite sprite;
            try {
                sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getStillIcon().getIconName());
            } catch (NullPointerException e) {
                this.colourCache.put(fluid.getName(), Integer.valueOf(colour));
                return colour;
            }
            if (sprite != null && sprite.getFrameCount() > 0) {
                int[][] image = sprite.getFrameTextureData(0);
                int r = 0, g = 0, b = 0, count = 0;
                for (int[] row : image) {
                    for (int pixel : row) {
                        if ((pixel >> 24 & 0xFF) > 127) {
                            r += pixel >> 16 & 0xFF;
                            g += pixel >> 8 & 0xFF;
                            b += pixel & 0xFF;
                            count++;
                        }
                    }
                }
                if (count > 0)
                    colour = r / count << 16 | g / count << 8 | b / count;
            }
        }
        this.colourCache.put(fluid.getName(), Integer.valueOf(colour));
        return colour;
    }

    public static void renderItemIcon(IIcon icon, double size, double z, float nx, float ny, float nz) {
        renderItemIcon(icon, 0.0D, 0.0D, size, size, z, nx, ny, nz);
    }

    public static void renderItemIcon(IIcon icon, double xStart, double yStart, double xEnd, double yEnd, double z, float nx, float ny, float nz) {
        if (icon == null)
            return;
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setNormal(nx, ny, nz);
        if (nz > 0.0F) {
            Tessellator.instance.addVertexWithUV(xStart, yStart, z, icon.getMinU(), icon.getMinV());
            Tessellator.instance.addVertexWithUV(xEnd, yStart, z, icon.getMaxU(), icon.getMinV());
            Tessellator.instance.addVertexWithUV(xEnd, yEnd, z, icon.getMaxU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(xStart, yEnd, z, icon.getMinU(), icon.getMaxV());
        } else {
            Tessellator.instance.addVertexWithUV(xStart, yEnd, z, icon.getMinU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(xEnd, yEnd, z, icon.getMaxU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(xEnd, yStart, z, icon.getMaxU(), icon.getMinV());
            Tessellator.instance.addVertexWithUV(xStart, yStart, z, icon.getMinU(), icon.getMinV());
        }
        Tessellator.instance.draw();
    }
}
