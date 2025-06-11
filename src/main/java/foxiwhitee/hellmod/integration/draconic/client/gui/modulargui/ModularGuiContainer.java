package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui;

import foxiwhitee.hellmod.integration.draconic.helpers.gui.accessors.IMinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class ModularGuiContainer<T extends Container> extends GuiContainer implements IModularGui<ModularGuiContainer>, IModularGuiAccessor {
    protected ModuleManager manager = new ModuleManager(this);
    protected int zLevel = 0;
    protected T container;
    protected boolean slotsHidden = false;
    protected boolean drawDefaultBackground = true;

    public ModularGuiContainer(T container) {
        super((Container) container);
        this.container = container;
    }


    public ModularGuiContainer getScreen() {
        return this;
    }


    public int xSize() {
        return this.xSize;
    }


    public int ySize() {
        return this.ySize;
    }


    public int guiLeft() {
        return this.guiLeft;
    }


    public int guiTop() {
        return this.guiTop;
    }


    public int screenWidth() {
        return this.width;
    }


    public int screenHeight() {
        return this.height;
    }


    public Minecraft getMinecraft() {
        return this.mc;
    }

    public ModuleManager getManager() {
        return this.manager;
    }


    public int getZLevel() {
        return this.zLevel;
    }


    public void setZLevel(int zLevel) {
        this.zLevel = zLevel;
    }


    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.manager.mouseClicked(mouseX, mouseY, mouseButton)) {
            return;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
        if (this.manager.mouseReleased(mouseX, mouseY, state)) {
            return;
        }

        super.mouseMovedOrUp(mouseX, mouseY, state);
    }


    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (this.manager.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)) {
            return;
        }

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }


    protected void keyTyped(char typedChar, int keyCode) {
        if (this.manager.keyTyped(typedChar, keyCode)) {
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }


    public void handleMouseInput() {
        if (this.manager.handleMouseInput()) {
            return;
        }

        super.handleMouseInput();
    }

    public int getMouseX() {
        return Mouse.getEventX() * this.width / this.mc.displayWidth;
    }

    public int getMouseY() {
        return this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
    }


    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        renderBackgroundLayer(mouseX, mouseY, partialTicks);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-guiLeft(), -guiTop(), 0.0F);

        renderForegroundLayer(mouseX, mouseY, (((IMinecraftAccessor) this.mc).getTimer()).renderPartialTicks);

        GL11.glPopMatrix();
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.drawDefaultBackground) {
            drawDefaultBackground();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderOverlayLayer(mouseX, mouseY, partialTicks);
    }


    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        Slot slot = getSlotAtPosition(mouseX, mouseY);
        if (this.mc.thePlayer.inventory.getItemStack() == null && slot != null && slot.getHasStack()) {
            renderToolTip(slot.getStack(), mouseX, mouseY);
        }
    }


    public void renderBackgroundLayer(int mouseX, int mouseY, float partialTicks) {
        this.manager.renderBackgroundLayer(this.mc, mouseX, mouseY, partialTicks);
    }

    public void renderForegroundLayer(int mouseX, int mouseY, float partialTicks) {
        this.manager.renderForegroundLayer(this.mc, mouseX, mouseY, partialTicks);
    }

    public void renderOverlayLayer(int mouseX, int mouseY, float partialTicks) {
        this.manager.renderOverlayLayer(this.mc, mouseX, mouseY, partialTicks);
    }


    public void updateScreen() {
        super.updateScreen();
        this.manager.onUpdate();
    }


    public boolean isMouseOverSlotAcc(Slot slotIn, int mouseX, int mouseY) {
        return (isMouseOverSlotShadow(slotIn, mouseX, mouseY) && !this.manager.isAreaUnderElement(slotIn.xDisplayPosition + guiLeft(), slotIn.yDisplayPosition + guiTop(), 16, 16, 100));
    }

    private boolean isMouseOverSlotShadow(Slot slotIn, int mouseX, int mouseY) {
        return func_146978_c(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
    }


    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        this.manager.setWorldAndResolution(mc, width, height);
    }


    public boolean overrideDefaultIsMouseOverSlot() {
        return true;
    }


    private Slot getSlotAtPosition(int p_146975_1_, int p_146975_2_) {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); k++) {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(k);

            if (isMouseOverSlot(slot, p_146975_1_, p_146975_2_)) {
                return slot;
            }
        }

        return null;
    }

    private boolean isMouseOverSlot(Slot p_146981_1_, int p_146981_2_, int p_146981_3_) {
        return func_146978_c(p_146981_1_.xDisplayPosition, p_146981_1_.yDisplayPosition, 16, 16, p_146981_2_, p_146981_3_);
    }
}
