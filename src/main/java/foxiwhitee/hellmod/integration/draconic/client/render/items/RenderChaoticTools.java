package foxiwhitee.hellmod.integration.draconic.client.render.items;

import com.brandon3055.draconicevolution.client.render.IRenderTweak;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class RenderChaoticTools implements IItemRenderer {
    private IModelCustom toolModel;

    private String toolTexture;

    private IRenderTweak tool;

    public RenderChaoticTools(String model, String texture, IRenderTweak tool) {
        this.tool = tool;
        this.toolModel = AdvancedModelLoader.loadModel(DraconicEvolutionIntegration.getResource(model));
        this.toolTexture = texture;
    }

    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return (type == IItemRenderer.ItemRenderType.ENTITY && helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION);
    }

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        DraconicEvolutionIntegration.bindResource(this.toolTexture);
        this.tool.tweakRender(type);
        this.toolModel.renderAll();
        GL11.glPopMatrix();
    }
}
