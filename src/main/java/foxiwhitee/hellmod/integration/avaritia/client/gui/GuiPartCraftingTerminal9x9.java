package foxiwhitee.hellmod.integration.avaritia.client.gui;

import appeng.api.config.ActionItems;
import appeng.api.config.ItemSubstitution;
import appeng.api.config.Settings;
import appeng.api.storage.ITerminalHost;
import appeng.client.gui.implementations.GuiMEMonitorable;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.core.localization.GuiText;
import appeng.core.sync.AppEngPacket;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketInventoryAction;
import appeng.core.sync.packets.PacketSwitchGuis;
import appeng.helpers.InventoryAction;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.terminals.GuiTerminal;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerPartCraftingTerminal9x9;
import foxiwhitee.hellmod.utils.craft.IGuiMEMonitorableAccessor;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiPartCraftingTerminal9x9 extends GuiTerminal {
    private final int maxResize = 3;

    private int resize = 3;

    private GuiImgButton clearBtn;

    private GuiTabButton craftingStatusBtn;

    private GuiImgButton resizeBtn;

    public GuiPartCraftingTerminal9x9(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, (ContainerMEMonitorable)new ContainerPartCraftingTerminal9x9(inventoryPlayer, te));
        ((IGuiMEMonitorableAccessor)this).callSetStandardSize(360);
        this.xSize = 360;
        this.ySize = 208;
    }

    protected void actionPerformed(GuiButton btn) {
        super.actionPerformed(btn);
        if (btn == this.craftingStatusBtn)
            NetworkHandler.instance.sendToServer((AppEngPacket)new PacketSwitchGuis(GuiBridge.GUI_CRAFTING_STATUS));
        if (this.clearBtn == btn) {
            Slot s = null;
            Container c = this.inventorySlots;
            for (Object j : c.inventorySlots) {
                if (j instanceof appeng.container.slot.SlotCraftingMatrix)
                    s = (Slot)j;
            }
            if (s != null) {
                PacketInventoryAction p = new PacketInventoryAction(InventoryAction.MOVE_REGION, s.slotNumber, 0L);
                NetworkHandler.instance.sendToServer((AppEngPacket)p);
            }
        }
        if (this.resizeBtn == btn) {
            this.resize--;
            if (this.resize <= 0)
                this.resize = 3;
        }
    }

    public void initGui() {
        super.initGui();
        this.buttonList.add(this.craftingStatusBtn = new GuiTabButton(this.guiLeft + 170, this.guiTop - 4, 178, GuiText.CraftingStatus.getLocal(), itemRender));
        this.craftingStatusBtn.setHideEdge(13);
        this.buttonList.add(this.clearBtn = new GuiImgButton(this.guiLeft + 188 + 108 - 99, this.guiTop + this.ySize - 206, (Enum)Settings.ACTIONS, (Enum)ActionItems.STASH));
        this.clearBtn.setHalfSize(true);
        this.buttonList.add(this.resizeBtn = new GuiImgButton(this.guiLeft + 188 + 108 - 99, this.guiTop + this.ySize - 199, (Enum)Settings.ACTIONS, (Enum)ItemSubstitution.DISABLED));
        this.resizeBtn.setHalfSize(true);
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.fontRendererObj.drawString(GuiText.CraftingTerminal.getLocal(), 8, this.ySize - 96 + 1, 4210752);
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        (Minecraft.getMinecraft()).renderEngine.bindTexture(new ResourceLocation(HellCore.MODID, "textures/gui/gui_terminal_avaritia_craft_big.png"));
        UtilGui.drawTexture(offsetX, offsetY, 0, 0, xSize, ySize, xSize, ySize, 768, 768);

        if (this.searchField != null) {
            this.searchField.drawTextBox();
        }
    }

    protected String getBackground() {
        return "guis/terminal.png";
    }
}
