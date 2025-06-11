package foxiwhitee.hellmod.client.gui.widgets;

import appeng.client.gui.widgets.ITooltip;
import foxiwhitee.hellmod.localization.CustomGuiColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public class MEGuiTextField implements ITooltip {
    protected GuiTextField field;

    private static final int PADDING = 2;

    private static boolean previousKeyboardRepeatEnabled;

    private static MEGuiTextField previousKeyboardRepeatEnabledField;

    private String tooltip;

    private int fontPad;

    public int x;

    public int y;

    public int w;

    public int h;

    public MEGuiTextField(int width, int height, String tooltip) {
        FontRenderer fontRenderer = (Minecraft.getMinecraft()).fontRenderer;
        this.field = new GuiTextField(fontRenderer, 0, 0, 0, 0);
        this.w = width;
        this.h = height;
        this.field.setEnableBackgroundDrawing(false);
        this.field.setMaxStringLength(32);
        this.field.setTextColor(CustomGuiColors.SearchboxText.getColor());
        this.field.setCursorPositionZero();
        setMessage(tooltip);
        this.fontPad = fontRenderer.getCharWidth('_');
        setDimensionsAndColor();
    }

    public MEGuiTextField(int width, int height) {
        this(width, height, "");
    }

    public MEGuiTextField() {
        this(0, 0);
    }

    protected void setDimensionsAndColor() {
        this.field.xPosition = this.x + 2;
        this.field.yPosition = this.y + 2;
        this.field.width = this.w - 4 - this.fontPad;
        this.field.height = this.h - 4;
    }

    public void onTextChange(String oldText) {}

    public void mouseClicked(int xPos, int yPos, int button) {
        if (!isMouseIn(xPos, yPos)) {
            setFocused(false);
            return;
        }
        this.field.setCanLoseFocus(false);
        setFocused(true);
        if (button == 1) {
            setText("");
        } else {
            this.field.mouseClicked(xPos, yPos, button);
        }
        this.field.setCanLoseFocus(true);
    }

    public boolean isMouseIn(int xCoord, int yCoord) {
        boolean withinXRange = (this.x <= xCoord && xCoord < this.x + this.w);
        boolean withinYRange = (this.y <= yCoord && yCoord < this.y + this.h);
        return (withinXRange && withinYRange);
    }

    public boolean textboxKeyTyped(char keyChar, int keyID) {
        if (!isFocused())
            return false;
        String oldText = getText();
        boolean handled = this.field.textboxKeyTyped(keyChar, keyID);
        if (!handled && (keyID == 28 || keyID == 156 || keyID == 1))
            setFocused(false);
        if (handled)
            onTextChange(oldText);
        return handled;
    }

    public void drawTextBox() {
        if (this.field.getVisible()) {
            setDimensionsAndColor();
            GuiTextField.drawRect(this.x + 1, this.y + 1, this.x + this.w - 1, this.y + this.h - 1,

                    isFocused() ? CustomGuiColors.SearchboxFocused.getColor() : CustomGuiColors.SearchboxUnfocused.getColor());
            this.field.drawTextBox();
        }
    }

    public void setText(String text, boolean ignoreTrigger) {
        String oldText = getText();
        int currentCursorPos = this.field.getCursorPosition();
        this.field.setText(text);
        this.field.setCursorPosition(currentCursorPos);
        if (!ignoreTrigger)
            onTextChange(oldText);
    }

    public void setText(String text) {
        setText(text, false);
    }

    public void setCursorPositionEnd() {
        this.field.setCursorPositionEnd();
    }

    public void setFocused(boolean focus) {
        if (this.field.isFocused() == focus)
            return;
        this.field.setFocused(focus);
        if (focus) {
            if (previousKeyboardRepeatEnabledField == null)
                previousKeyboardRepeatEnabled = Keyboard.areRepeatEventsEnabled();
            previousKeyboardRepeatEnabledField = this;
            Keyboard.enableRepeatEvents(true);
        } else if (previousKeyboardRepeatEnabledField == this) {
            previousKeyboardRepeatEnabledField = null;
            Keyboard.enableRepeatEvents(previousKeyboardRepeatEnabled);
        }
    }

    public void setMaxStringLength(int size) {
        this.field.setMaxStringLength(size);
    }

    public boolean isFocused() {
        return this.field.isFocused();
    }

    public String getText() {
        return this.field.getText();
    }

    public void setMessage(String t) {
        this.tooltip = t;
    }

    public String getMessage() {
        return this.tooltip;
    }

    public boolean isVisible() {
        return this.field.getVisible();
    }

    public int xPos() {
        return this.x;
    }

    public int yPos() {
        return this.y;
    }

    public int getWidth() {
        return this.w;
    }

    public int getHeight() {
        return this.h;
    }
}
