package foxiwhitee.hellmod.client.gui.terminals;

import appeng.api.config.ActionItems;
import appeng.api.config.Settings;
import appeng.api.storage.ITerminalHost;
import appeng.client.gui.AEBaseMEGui;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.implementations.ContainerPatternTerm;
import appeng.container.slot.AppEngSlot;
import foxiwhitee.hellmod.client.gui.buttons.NoTextureAEButton;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import foxiwhitee.hellmod.utils.craft.IGuiMEMonitorableAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class GuiPatternTerminal extends GuiTerminal {
    private NoTextureAEButton encodeBtn;
    protected boolean hasCycleItems = false;
    protected double cycleItemsCordX = 0;
    protected double cycleItemsCordY = 0;

    public GuiPatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te, ContainerPatternTerminal c, int xSize, int ySize) {
        super(inventoryPlayer, te, c, xSize, ySize);
        this.callSetStandardSize(this.xSize);
        this.callSetReservedSpace(81);
        this.hasFG = false;
    }

    @Override
    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        super.drawFG(offsetX, offsetY, mouseX, mouseY);
        if (hasCycleItems) {
            List<ItemStack> stacks = new ArrayList<>();
            Iterable<ItemStack> craftingGrid = getContainer().getTerminal().getInventoryCrafting();

            for (ItemStack itemStack : craftingGrid) {
                if (itemStack != null) {
                    stacks.add(itemStack);
                }
            }

            if (!stacks.isEmpty()) {
                double degreePerInput = 360.0 / stacks.size();
                double currentDegree = degreePerInput;

                if (!stacks.isEmpty()) {
                    stacks.remove(0);
                }

                if (!stacks.isEmpty()) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(cycleItemsCordX, cycleItemsCordY, 0.0);

                    Iterator<ItemStack> iterator = stacks.iterator();
                    while (iterator.hasNext()) {
                        ItemStack itemStack = iterator.next();

                        GL11.glPushMatrix();
                        GL11.glTranslated(8.0, 8.0, 0.0);
                        GL11.glRotated(currentDegree, 0.0, 0.0, 1.0);
                        GL11.glTranslated(-8.0, -8.0, 0.0);

                        AEBaseMEGui.itemRender.zLevel = -50.0F;
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        AEBaseMEGui.itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, itemStack, 0, -43);

                        GL11.glPopMatrix();

                        currentDegree += degreePerInput;
                    }

                    GL11.glPopMatrix();
                }
            }
        }
    }

    protected void actionPerformed(GuiButton btn) {
        super.actionPerformed(btn);
        try {
            if (this.encodeBtn == btn) {
                NetworkManager.instance.sendToServer(new DefaultPacket("PatternTerminal.Encode", "1"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initGui() {
        super.initGui();
        this.encodeBtn = new NoTextureAEButton(this.guiLeft + 243, this.guiTop + 173, Settings.ACTIONS, ActionItems.ENCODE);
        this.buttonList.add(this.encodeBtn);
        getContainer().getPatternSlotIN().setIIcon(-1);
        getContainer().getPatternSlotOUT().setIIcon(-1);
    }

    @Override
    protected ContainerPatternTerminal getContainer() {
        return (ContainerPatternTerminal) super.getContainer();
    }

    abstract protected String getBackground();

    protected void repositionSlot(AppEngSlot s) {
        if (s.isPlayerSide()) {
            s.yDisplayPosition = s.getY() + this.ySize - 78 - 5;
        } else {
            s.yDisplayPosition = s.getY() + this.ySize - 78 - 3;
        }
    }

}
