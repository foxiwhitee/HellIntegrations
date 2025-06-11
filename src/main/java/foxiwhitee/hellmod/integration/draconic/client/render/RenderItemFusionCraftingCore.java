package foxiwhitee.hellmod.integration.draconic.client.render;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class RenderItemFusionCraftingCore implements IItemRenderer {
    private final ResourceLocation texture = new ResourceLocation(HellCore.MODID, "textures/models/fusion_crafting_core.png");

    private final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(HellCore.MODID, "models/fusion_crafting_core.obj"));

    public boolean handleRenderType(ItemStack is, IItemRenderer.ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack is, IItemRenderer.ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack is, Object... data) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5D, 0.5D, 0.5D);
        GL11.glScaled(0.6D, 0.6D, 0.6D);
        switch (type) {
            case ENTITY:
                GL11.glScaled(1.35D, 1.35D, 1.35D);
                GL11.glTranslated(-0.4D, 0.0D, -0.4D);
                break;
        }
        (Minecraft.getMinecraft()).renderEngine.bindTexture(this.texture);
        this.model.renderAll();
        GL11.glPopMatrix();
    }
}
