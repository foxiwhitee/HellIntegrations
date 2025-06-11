package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui;

import java.util.*;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class ModuleManager {
    private static final Comparator<MGuiElementBase> renderSorter = new Comparator<MGuiElementBase>() {
        public int compare(MGuiElementBase o1, MGuiElementBase o2) {
            return (o1.displayLevel < o2.displayLevel) ? -1 : ((o1.displayLevel > o2.displayLevel) ? 1 : 0);
        }
    };
    private static final Comparator<MGuiElementBase> actionSorter = new Comparator<MGuiElementBase>() {
        public int compare(MGuiElementBase o1, MGuiElementBase o2) {
            return (o1.displayLevel < o2.displayLevel) ? 1 : ((o1.displayLevel > o2.displayLevel) ? -1 : 0);
        }
    };

    private final IModularGui parentGui;
    private final List<MGuiElementBase> toRemove = new ArrayList<>();
    protected LinkedList<MGuiElementBase> elements = new LinkedList<>();
    protected LinkedList<MGuiElementBase> actionList = new LinkedList<>();

    private boolean requiresReSort = false;


    public ModuleManager(IModularGui parentGui) {
        this.parentGui = parentGui;
    }

    public void initElements() {
        for (MGuiElementBase element : this.elements) {
            element.initElement();
        }
    }


    public MGuiElementBase add(MGuiElementBase element, int displayLevel) {
        if (displayLevel > 4) {
            System.out.println("ModularGui Display Level Out Of Bounds! Max is 4, someone is using " + displayLevel);
        }
        if (element.mc == null || element.fontRenderer == null) {
            element.setWorldAndResolution(this.parentGui.getMinecraft(), this.parentGui.screenWidth(), this.parentGui.screenHeight());
        }
        element.displayLevel = displayLevel;
        this.elements.add(element);
        this.requiresReSort = true;
        return element;
    }


    public MGuiElementBase add(MGuiElementBase element) {
        return add(element, 0);
    }

    public void remove(MGuiElementBase element) {
        if (this.elements.contains(element)) {
            this.toRemove.add(element);
            this.requiresReSort = true;
        }
    }

    public void removeByID(String id) {
        Iterator<MGuiElementBase> i = this.elements.iterator();
        while (i.hasNext()) {
            MGuiElementBase element = i.next();
            if (element.id != null && element.id.equals(id)) {
                this.toRemove.add(element);
                this.requiresReSort = true;
                return;
            }
        }
    }

    public void removeByGroup(String group) {
        Iterator<MGuiElementBase> i = this.elements.iterator();
        while (i.hasNext()) {
            MGuiElementBase element = i.next();
            if (element.isInGroup(group)) {
                this.toRemove.add(element);
                this.requiresReSort = true;
            }
        }
    }

    public void setIDEnabled(String id, boolean enabled) {
        Iterator<MGuiElementBase> i = this.elements.iterator();
        while (i.hasNext()) {
            MGuiElementBase element = i.next();
            if (element.id != null && element.id.equals(id)) {
                element.setEnabled(enabled);
                return;
            }
        }
    }

    public void setGroupEnabled(String group, boolean enabled) {
        Iterator<MGuiElementBase> i = this.elements.iterator();
        while (i.hasNext()) {
            MGuiElementBase element = i.next();
            if (element.isInGroup(group)) {
                element.setEnabled(enabled);
            }
        }
    }


    public List<MGuiElementBase> getElements() {
        return this.elements;
    }

    public void clear() {
        this.elements.clear();
        this.requiresReSort = true;
    }

    protected boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int clickedDisplay = -100;
        for (MGuiElementBase element : this.actionList) {
            if (element.isEnabled() && clickedDisplay > -100 && element.displayLevel < clickedDisplay) {
                return true;
            }

            if (element.isEnabled() && element.isMouseOver(mouseX, mouseY)) {
                clickedDisplay = element.displayLevel;
            }

            if (element.isEnabled() && element.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        return false;
    }

    protected boolean mouseReleased(int mouseX, int mouseY, int state) {
        for (MGuiElementBase element : this.actionList) {
            if (element.isEnabled() && element.mouseReleased(mouseX, mouseY, state)) {
                return true;
            }
        }
        return false;
    }

    protected boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for (MGuiElementBase element : this.actionList) {
            if (element.isEnabled() && element.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)) {
                return true;
            }
        }
        return false;
    }


    public boolean keyTyped(char typedChar, int keyCode) {
        for (MGuiElementBase element : this.actionList) {
            if (element.isEnabled() && element.keyTyped(typedChar, keyCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean handleMouseInput() {
        for (MGuiElementBase element : this.actionList) {
            if (element.isEnabled() && element.handleMouseInput()) {
                return true;
            }
        }
        return false;
    }

    public void renderBackgroundLayer(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        for (MGuiElementBase element : this.elements) {
            if (element.isEnabled()) {
                this.parentGui.setZLevel(element.displayLevel * 200);
                element.renderBackgroundLayer(mc, mouseX, mouseY, partialTicks);
            }
        }
    }

    public void renderForegroundLayer(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        for (MGuiElementBase element : this.elements) {
            if (element.isEnabled()) {
                this.parentGui.setZLevel(element.displayLevel * 200);
                element.renderForegroundLayer(mc, mouseX, mouseY, partialTicks);
            }
        }
    }


    public boolean renderOverlayLayer(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        int renderDisplay = -100;
        for (MGuiElementBase element : this.actionList) {
            if (element.isEnabled() && renderDisplay > -100 && element.displayLevel < renderDisplay) {
                return true;
            }

            if (element.isEnabled() && element.isMouseOver(mouseX, mouseY)) {
                renderDisplay = element.displayLevel;
            }

            if (element.isEnabled() && element.renderOverlayLayer(mc, mouseX, mouseY, partialTicks)) {
                return true;
            }
        }

        return false;
    }


    public boolean isAreaUnderElement(int posX, int posY, int xSize, int ySize, int zLevel) {
        for (MGuiElementBase element : this.elements) {
            if (element.isEnabled() && element.displayLevel * 200 >= zLevel && element.getRectangle().intersects(posX, posY, xSize, ySize)) {
                return true;
            }
        }
        return false;
    }


    public void onUpdate() {
        if (!this.toRemove.isEmpty()) {
            this.elements.removeAll(this.toRemove);
            this.toRemove.clear();
        }

        for (MGuiElementBase element : this.elements) {
            if (element.onUpdate()) {
                break;
            }
        }

        if (this.requiresReSort) {
            sort();
        }
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        for (MGuiElementBase element : this.elements) {
            element.setWorldAndResolution(mc, width, height);
        }
        initElements();
    }

    private void sort() {
        Collections.sort(this.elements, renderSorter);
        this.actionList.clear();
        this.actionList.addAll(this.elements);
        Collections.sort(this.actionList, actionSorter);
    }
}
