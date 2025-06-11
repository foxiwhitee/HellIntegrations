package foxiwhitee.hellmod.integration.draconic.client.render;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.render.AnimatedHeightTexture;
import foxiwhitee.hellmod.client.render.TileEntitySpecialRendererObjWrapper;
import foxiwhitee.hellmod.integration.draconic.tile.TileFusionInjector;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTileFusionInjector extends TileEntitySpecialRendererObjWrapper<TileFusionInjector> {
    private final ResourceLocation texture_active;

    private final ResourceLocation[] texturesActive;

    private final ResourceLocation texture_wyvern_injector = new ResourceLocation(HellCore.MODID, "textures/models/wyvern_fusion_injector.png");

    private final ResourceLocation texture_draconic_injector = new ResourceLocation(HellCore.MODID, "textures/models/draconic_fusion_injector.png");

    private final ResourceLocation texture_chaotic_injector = new ResourceLocation(HellCore.MODID, "textures/models/chaotic_fusion_injector.png");

    private final ResourceLocation texture_infinity_injector = new ResourceLocation(HellCore.MODID, "textures/models/infinity_fusion_injector.png");

    private boolean init = false;

    public RenderTileFusionInjector() {
        super(TileFusionInjector.class, "models/crafting_injector.obj", "textures/models/base_fusion_injector.png");
        this.texture_active = new ResourceLocation(HellCore.MODID, "textures/models/infinity_fusion_injector_lights.png");
        this.texturesActive = new ResourceLocation[8];
        for (int i = 0; i < 8; i++)
            this.texturesActive[i] = new ResourceLocation(HellCore.MODID, "textures/models/infinity_fusion_injector_lights" + i + ".png");
        createList("all");
    }

    public void renderAt(TileFusionInjector tileEntity, double x, double y, double z, double f) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glPushMatrix();
        bindTexture();
        GL11.glScaled(0.75D, 0.75D, 0.75D);
        GL11.glTranslated(0.66D, 0.0D, 0.66D);
        int meta = tileEntity.getFace();
        switch (meta) {
            case 0:
                GL11.glRotated(180.0D, 1.0D, 0.0D, 0.0D);
                GL11.glTranslated(0.0D, -1.33D, 0.0D);
                break;
            case 2:
                GL11.glRotated(-90.0D, 1.0D, 0.0D, 0.0D);
                GL11.glTranslated(0.0D, -0.68D, 0.66D);
                break;
            case 3:
                GL11.glRotated(90.0D, 1.0D, 0.0D, 0.0D);
                GL11.glTranslated(0.0D, -0.68D, -0.66D);
                break;
            case 4:
                GL11.glRotated(90.0D, 0.0D, 0.0D, 1.0D);
                GL11.glTranslated(0.66D, -0.68D, 0.0D);
                break;
            case 5:
                GL11.glRotated(-90.0D, 0.0D, 0.0D, 1.0D);
                GL11.glTranslated(-0.68D, -0.66D, 0.0D);
                break;
        }
        int tier = tileEntity.getPedestalTier();
        switch (tier) {
            case 1:
                bindTexture(this.texture_wyvern_injector);
                break;
            case 2:
                bindTexture(this.texture_draconic_injector);
                break;
            case 3:
                bindTexture(this.texture_chaotic_injector);
                break;
            case 4:
                bindTexture(this.texturesActive[(int)(System.currentTimeMillis() / 480L % 8L)]);
                break;
        }
        renderPart("all");
        if (tileEntity.getStackInSlot(0) != null) {
            GL11.glPushMatrix();
            ItemStack stack = tileEntity.getStackInSlot(0);
            EntityItem itemEntity = new EntityItem(tileEntity.getWorldObj(), 0.0D, 0.0D, 0.0D, tileEntity.getStackInSlot(0));
            itemEntity.hoverStart = 0.0F;
            GL11.glTranslatef(0.0F, 1.3F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            float rotation = (float)(720.0D * (System.currentTimeMillis() & 0x3FFFL) / 16383.0D);
            GL11.glRotatef(rotation, 0.0F, 0.5F, 0.0F);
            if (stack.getItem() instanceof net.minecraft.item.ItemBlock)
                GL11.glScalef(1.0F, 1.0F, 1.0F);
            RenderItem.renderInFrame = true;
            RenderManager.instance.renderEntityWithPosYaw((Entity)itemEntity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            RenderItem.renderInFrame = false;
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void init() {
        if (this.init)
            return;
        TextureManager texturemanager = this.field_147501_a.field_147553_e;
        if (texturemanager != null) {
            for (int i = 0; i < 8; i++)
                texturemanager.loadTexture(this.texturesActive[i], (ITextureObject)new AnimatedHeightTexture(this.texture_active, i));
            this.init = true;
        }
    }

    public void bindTexture(ResourceLocation p_147499_1_) {
        init();
        super.bindTexture(p_147499_1_);
    }
}
