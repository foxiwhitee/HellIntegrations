package foxiwhitee.hellmod.integration.draconic.client.render.armor;

import com.brandon3055.draconicevolution.common.handler.ConfigHandler;
import com.brandon3055.draconicevolution.common.utills.LogHelper;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderArialArmor implements IItemRenderer {
    private ItemArmor armor;

    private ResourceLocation loc;

    public RenderArialArmor() {}

    public RenderArialArmor(ItemArmor armor) {
        this.armor = armor;
    }

    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        if (ConfigHandler.useOldArmorModel) {
            LogHelper.error("You must restart the game for armor model change to effect the armor items!!!");
        } else {
            GL11.glPushMatrix();
            DraconicEvolutionIntegration.bindResource(this.armor.getArmorTexture(stack, null, 0, null).replace(HellCore.MODID + ":", ""));
            if (type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.EQUIPPED) {
                GL11.glTranslated(0.5D, 0.5D, 0.5D);
                GL11.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
            }
            GL11.glTranslated(0.0D, (this.armor.armorType == 0) ? -0.25D : ((this.armor.armorType == 1) ? 0.42D : ((this.armor.armorType == 2) ? 1.05D : 1.5D)), 0.0D);
            GL11.glRotated(180.0D, -1.0D, 0.0D, 1.0D);
            this.armor.getArmorModel(null, stack, 0).render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
        }
    }
}

