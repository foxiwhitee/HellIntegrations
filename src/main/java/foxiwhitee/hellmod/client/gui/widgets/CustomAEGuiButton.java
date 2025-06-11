package foxiwhitee.hellmod.client.gui.widgets;

import appeng.client.gui.widgets.ITooltip;
import appeng.client.texture.ExtraBlockTextures;
import foxiwhitee.hellmod.api.config.Buttons;
import foxiwhitee.hellmod.api.config.CustomAESetings;
import foxiwhitee.hellmod.localization.CustomButtonToolTips;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CustomAEGuiButton extends GuiButton implements ITooltip {
    private static final Pattern COMPILE = Pattern.compile("%s");
    private static final Pattern PATTERN_NEW_LINE = Pattern.compile("\\n", 16);
    private static Map<EnumPair, ButtonAppearance> appearances;
    private final Enum buttonSetting;
    private boolean halfSize = false;
    private String fillVar;
    private Enum currentValue;

    public CustomAEGuiButton(int x, int y, Enum idx, Enum val) {
        super(0, 0, 16, "");
        this.buttonSetting = idx;
        this.currentValue = val;
        this.xPosition = x;
        this.yPosition = y;
        this.width = 16;
        this.height = 16;
        if (appearances == null) {
            appearances = new HashMap();
            this.registerApp(255, CustomAESetings.CRAFT_DIRECTION, Buttons.PAST, CustomButtonToolTips.PastBtnTitle, CustomButtonToolTips.PastBtnDesc);
            this.registerApp(255, CustomAESetings.CRAFT_DIRECTION, Buttons.NEXT, CustomButtonToolTips.NextBtnTitle, CustomButtonToolTips.NextBtnDesc);
            this.registerApp(255, CustomAESetings.TYPE_CRAFT, Buttons.CHANGE_TYPE, CustomButtonToolTips.ChangeTypeBtnTitle, CustomButtonToolTips.ChangeTypeBtnDesc);
        }

    }

    private void registerApp(int iconIndex, CustomAESetings setting, Enum val, CustomButtonToolTips title, Object hint) {
        ButtonAppearance a = new ButtonAppearance();
        a.displayName = title.getUnlocalized();
        a.displayValue = (String)(hint instanceof String ? hint : ((CustomButtonToolTips)hint).getUnlocalized());
        a.index = iconIndex;
        appearances.put(new EnumPair(setting, val), a);
    }

    public void setVisibility(boolean vis) {
        this.visible = vis;
        this.enabled = vis;
    }

    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            int iconIndex = this.getIconIndex();
            if (this.halfSize) {
                this.width = 8;
                this.height = 8;
                GL11.glPushMatrix();
                GL11.glTranslatef((float)this.xPosition, (float)this.yPosition, 0.0F);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                if (this.enabled) {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                } else {
                    GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
                }

                par1Minecraft.renderEngine.bindTexture(ExtraBlockTextures.GuiTexture("guis/states.png"));
                this.field_146123_n = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
                int uv_y = (int)Math.floor((double)(iconIndex / 16));
                int uv_x = iconIndex - uv_y * 16;
                this.drawTexturedModalRect(0, 0, 240, 240, 16, 16);
                this.drawTexturedModalRect(0, 0, uv_x * 16, uv_y * 16, 16, 16);
                this.mouseDragged(par1Minecraft, par2, par3);
                GL11.glPopMatrix();
            } else {
                if (this.enabled) {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                } else {
                    GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
                }

                par1Minecraft.renderEngine.bindTexture(ExtraBlockTextures.GuiTexture("guis/states.png"));
                this.field_146123_n = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
                int uv_y = (int)Math.floor((double)(iconIndex / 16));
                int uv_x = iconIndex - uv_y * 16;
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 240, 240, 16, 16);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, uv_x * 16, uv_y * 16, 16, 16);
                this.mouseDragged(par1Minecraft, par2, par3);
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private int getIconIndex() {
        if (this.buttonSetting != null && this.currentValue != null) {
            ButtonAppearance app = (ButtonAppearance)appearances.get(new EnumPair(this.buttonSetting, this.currentValue));
            return app == null ? 255 : app.index;
        } else {
            return 255;
        }
    }

    public CustomAESetings getSetting() {
        return (CustomAESetings)this.buttonSetting;
    }

    public Enum getCurrentValue() {
        return this.currentValue;
    }

    public String getMessage() {
        String displayName = null;
        String displayValue = null;
        if (this.buttonSetting != null && this.currentValue != null) {
            ButtonAppearance buttonAppearance = (ButtonAppearance)appearances.get(new EnumPair(this.buttonSetting, this.currentValue));
            if (buttonAppearance == null) {
                return "No Such Message";
            }

            displayName = buttonAppearance.displayName;
            displayValue = buttonAppearance.displayValue;
        }

        if (displayName == null) {
            return null;
        } else {
            String name = LocalizationUtils.localize(displayName);
            String value = LocalizationUtils.localize(displayValue);
            if (name == null || name.isEmpty()) {
                name = displayName;
            }

            if (value == null || value.isEmpty()) {
                value = displayValue;
            }

            if (this.fillVar != null) {
                value = COMPILE.matcher(value).replaceFirst(this.fillVar);
            }

            value = PATTERN_NEW_LINE.matcher(value).replaceAll("\n");
            StringBuilder sb = new StringBuilder(value);
            int i = sb.lastIndexOf("\n");
            if (i <= 0) {
                i = 0;
            }

            while(i + 30 < sb.length() && (i = sb.lastIndexOf(" ", i + 30)) != -1) {
                sb.replace(i, i + 1, "\n");
            }

            return name + '\n' + sb;
        }
    }

    public int xPos() {
        return this.xPosition;
    }

    public int yPos() {
        return this.yPosition;
    }

    public int getWidth() {
        return this.halfSize ? 8 : 16;
    }

    public int getHeight() {
        return this.halfSize ? 8 : 16;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void set(Enum e) {
        if (this.currentValue != e) {
            this.currentValue = e;
        }

    }

    public boolean isHalfSize() {
        return this.halfSize;
    }

    public void setHalfSize(boolean halfSize) {
        this.halfSize = halfSize;
    }

    public String getFillVar() {
        return this.fillVar;
    }

    public void setFillVar(String fillVar) {
        this.fillVar = fillVar;
    }

    private static final class EnumPair {
        final Enum setting;
        final Enum value;

        EnumPair(Enum a, Enum b) {
            this.setting = a;
            this.value = b;
        }

        public int hashCode() {
            return this.setting.hashCode() ^ this.value.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (this.getClass() != obj.getClass()) {
                return false;
            } else {
                EnumPair other = (EnumPair)obj;
                return other.setting == this.setting && other.value == this.value;
            }
        }
    }

    private static class ButtonAppearance {
        public int index;
        public String displayName;
        public String displayValue;

        private ButtonAppearance() {
        }
    }
}