package foxiwhitee.hellmod.client.gui.buttons;

import appeng.client.gui.widgets.GuiTabButton;
import appeng.client.texture.ExtraBlockTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class CustomGuiTabButton extends GuiTabButton {
    private final String states;
    private int hideEdge = 0;
    private int myIcon = -1;
    private ItemStack myItem;
    private final RenderItem itemRenderer;

    public CustomGuiTabButton(String states, int x, int y, int ico, String message, RenderItem ir) {
        super(x, y, ico, message, ir);
        this.states = states;
        this.myIcon = ico;
        this.itemRenderer = ir;
    }

    public CustomGuiTabButton(String states, int x, int y, ItemStack ico, String message, RenderItem ir) {
        super(x, y, ico, message, ir);
        this.states = states;
        this.myItem = ico;
        this.itemRenderer = ir;
    }

    public CustomGuiTabButton(int x, int y, int ico, String message, RenderItem ir) {
        super(x, y, ico, message, ir);
        this.states = "guis/states.png";
        this.myIcon = ico;
        this.itemRenderer = ir;
    }

    public CustomGuiTabButton(int x, int y, ItemStack ico, String message, RenderItem ir) {
        super(x, y, ico, message, ir);
        this.states = "guis/states.png";
        this.myItem = ico;
        this.itemRenderer = ir;
    }

    public void drawButton(Minecraft minecraft, int x, int y) {
        if (this.visible) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            minecraft.renderEngine.bindTexture(ExtraBlockTextures.GuiTexture(states));
            this.field_146123_n = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
            int uv_x = this.hideEdge > 0 ? 11 : 13;
            int offsetX = this.hideEdge > 0 ? 1 : 0;
            this.drawTexturedModalRect(this.xPosition, this.yPosition, uv_x * 16, 0, 25, 22);
            if (this.myIcon >= 0) {
                int uv_y = (int)Math.floor((double)(this.myIcon / 16));
                uv_x = this.myIcon - uv_y * 16;
                this.drawTexturedModalRect(offsetX + this.xPosition + 3, this.yPosition + 3, uv_x * 16, uv_y * 16, 16, 16);
            }

            this.mouseDragged(minecraft, x, y);
            if (this.myItem != null) {
                this.zLevel = 100.0F;
                this.itemRenderer.zLevel = 100.0F;
                GL11.glEnable(2896);
                GL11.glEnable(32826);
                RenderHelper.enableGUIStandardItemLighting();
                FontRenderer fontrenderer = minecraft.fontRenderer;
                this.itemRenderer.renderItemAndEffectIntoGUI(fontrenderer, minecraft.renderEngine, this.myItem, offsetX + this.xPosition + 3, this.yPosition + 3);
                GL11.glDisable(2896);
                this.itemRenderer.zLevel = 0.0F;
                this.zLevel = 0.0F;
            }
        }

    }

}
