package foxiwhitee.hellmod.integration.draconic.client.render;

import com.brandon3055.draconicevolution.common.tileentities.TileUpgradeModifier;
import foxiwhitee.hellmod.integration.draconic.tile.TileCustomUpgradeModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemCustomUpgradeModifier implements IItemRenderer {
    private final TileCustomUpgradeModifier tile;

    public RenderItemCustomUpgradeModifier() {
        tile = new TileCustomUpgradeModifier();
        this.tile.rotation = 0.0F;
    }

    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        if (type == IItemRenderer.ItemRenderType.ENTITY)
            GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tile, 0.0D, 0.0D, 0.0D, 0.0F);
    }
}
