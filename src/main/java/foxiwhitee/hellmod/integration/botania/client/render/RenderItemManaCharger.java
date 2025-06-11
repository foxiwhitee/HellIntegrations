package foxiwhitee.hellmod.integration.botania.client.render;

import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;


public class RenderItemManaCharger implements IItemRenderer {
    public boolean handleRenderType(ItemStack is, ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack is, ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(ItemRenderType type, ItemStack is, Object... data) {
        double time = Minecraft.getSystemTime();
        GL11.glPushMatrix();
        if (is.getItem() == Item.getItemFromBlock((Block) BotaniaIntegration.manaCharger)) {
            switch (type) {
                case EQUIPPED:
                    GL11.glTranslated(0.5D, 1.5D, 0.5D);
                    GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
                    GL11.glScaled(1.2D, 1.2D, 1.2D);
                    break;
                case INVENTORY:
                    GL11.glTranslated(0.0D, 0.5D, 0.0D);
                    GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
                    break;
                case EQUIPPED_FIRST_PERSON:
                    GL11.glTranslated(0.5D, 1.5D, 0.5D);
                    GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
                    break;
                case ENTITY:
                    GL11.glTranslated(0.0D, 1.5D, 0.0D);
                    GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
                    break;
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(RenderManaCharger.TEXTURE_CHARGER);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0D, Math.cos(time / 750.0D) * 0.2D, 0.0D);
            GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
            RenderManaCharger.MODEl_CHARGER.Shape1.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.Shape2.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.Shape3.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.Shape4.render(1.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
            GL11.glRotated(time / 50.0D, 0.0D, 1.0D, 0.0D);
            GL11.glTranslated(Math.sin(time / 100.0D) / 20.0D, 0.0D, Math.cos(time / 100.0D) / 20.0D);
            RenderManaCharger.MODEl_CHARGER.Rot1.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.Rot2.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.Rot3.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.Rot4.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.PlateRot1.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.PlateRot2.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.PlateRot3.render(1.0F);
            RenderManaCharger.MODEl_CHARGER.PlateRot4.render(1.0F);
            GL11.glPopMatrix();
            GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
            RenderManaCharger.MODEl_CHARGER.render(1.0F);
            GL11.glPopMatrix();
        }
    }
}

