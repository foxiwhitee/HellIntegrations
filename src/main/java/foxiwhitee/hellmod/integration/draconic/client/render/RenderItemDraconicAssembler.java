package foxiwhitee.hellmod.integration.draconic.client.render;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.render.assemblers.RenderItemCustomMolecularAssembler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class RenderItemDraconicAssembler implements IItemRenderer {
    protected ResourceLocation getTexture() {
        return new ResourceLocation(HellCore.MODID, "textures/blocks/draconic/dragon_assembler.png");
    }

    private final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(HellCore.MODID, "models/dragon_assembler.obj"));

    public boolean handleRenderType(ItemStack is, IItemRenderer.ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack is, IItemRenderer.ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack is, Object... data) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(0.0D, -0.5D, 0.0D);
        GL11.glScaled(1.0D, 1.0D, 1.0D);
        switch (type) {
            case ENTITY:
                GL11.glScaled(1.35D, 1.35D, 1.35D);
                GL11.glTranslated(0.0D, 0.0D, 0.0D);
                break;
        }
        (Minecraft.getMinecraft()).renderEngine.bindTexture(this.getTexture());
        this.model.renderAll();
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}
