package foxiwhitee.hellmod.integration.avaritia.client.gui;

import appeng.api.config.ActionItems;
import appeng.api.config.ItemSubstitution;
import appeng.api.config.Settings;
import appeng.api.storage.ITerminalHost;
import appeng.client.gui.implementations.GuiMEMonitorable;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.client.texture.ExtraBlockTextures;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.AppEngSlot;
import appeng.core.localization.GuiText;
import appeng.core.sync.AppEngPacket;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketSwitchGuis;
import java.io.IOException;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.terminals.GuiPatternTerminal;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerPartBigPatternTerminal;
import foxiwhitee.hellmod.network.BasePacket;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import foxiwhitee.hellmod.utils.craft.IGuiMEMonitorableAccessor;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiPartBigPatternTerminal extends GuiPatternTerminal {

    private GuiTabButton tabCraftButton;
    private GuiTabButton tabProcessButton;
    private GuiImgButton substitutionsEnabledBtn;
    private GuiImgButton substitutionsDisabledBtn;
    private GuiImgButton clearBtn;
    private GuiTabButton craftingStatusBtn;

    private static final ResourceLocation GUI = new ResourceLocation(HellCore.MODID, "textures/gui/gui_terminal_avaritia_pattern_big_1.png");
    private static final ResourceLocation GUI2 = new ResourceLocation(HellCore.MODID, "textures/gui/gui_terminal_avaritia_pattern_big_3.png");


    public GuiPartBigPatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, new ContainerPartBigPatternTerminal(inventoryPlayer, te), 550, 250);
    }

    protected void actionPerformed(GuiButton btn) {
        super.actionPerformed(btn);
        if (btn == this.craftingStatusBtn)
            NetworkHandler.instance.sendToServer((AppEngPacket)new PacketSwitchGuis(GuiBridge.GUI_CRAFTING_STATUS));
        try {
            if (this.tabCraftButton == btn || this.tabProcessButton == btn)
                NetworkManager.instance.sendToServer((BasePacket)new DefaultPacket("BigPatternTerminal.CraftMode", (this.tabProcessButton == btn) ? "1" : "0"));
            if (this.clearBtn == btn)
                NetworkManager.instance.sendToServer((BasePacket)new DefaultPacket("BigPatternTerminal.Clear", "1"));
            if (this.substitutionsEnabledBtn == btn || this.substitutionsDisabledBtn == btn)
                NetworkManager.instance.sendToServer((BasePacket)new DefaultPacket("BigPatternTerminal.Substitute", (this.substitutionsEnabledBtn == btn) ? "0" : "1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initGui() {
        super.initGui();
        this.tabCraftButton = new GuiTabButton(this.guiLeft + 173, this.guiTop + this.ySize - 177, new ItemStack(Blocks.crafting_table), GuiText.CraftingPattern.getLocal(), itemRender);
        this.buttonList.add(this.tabCraftButton);
        this.tabProcessButton = new GuiTabButton(this.guiLeft + 173, this.guiTop + this.ySize - 177, new ItemStack(Blocks.furnace), GuiText.ProcessingPattern.getLocal(), itemRender);
        this.buttonList.add(this.tabProcessButton);
        this.buttonList.add(this.craftingStatusBtn = new GuiTabButton(this.guiLeft + 170, this.guiTop - 4, 178, GuiText.CraftingStatus.getLocal(), itemRender));
        this.craftingStatusBtn.setHideEdge(13);
        this.substitutionsEnabledBtn = new GuiImgButton(this.guiLeft + 171 + 24, this.guiTop + 194, (Enum)Settings.ACTIONS, (Enum)ItemSubstitution.ENABLED);
        this.substitutionsEnabledBtn.setHalfSize(true);
        this.buttonList.add(this.substitutionsEnabledBtn);
        this.substitutionsDisabledBtn = new GuiImgButton(this.guiLeft + 171 + 24, this.guiTop + 194, (Enum)Settings.ACTIONS, (Enum)ItemSubstitution.DISABLED);
        this.substitutionsDisabledBtn.setHalfSize(true);
        this.buttonList.add(this.substitutionsDisabledBtn);
        this.clearBtn = new GuiImgButton(this.guiLeft + 171 + 24, this.guiTop + 182, (Enum)Settings.ACTIONS, (Enum)ActionItems.CLOSE);
        this.clearBtn.setHalfSize(true);
        this.buttonList.add(this.clearBtn);
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        if (!((ContainerPartBigPatternTerminal)getContainer()).isCraftingMode()) {
            this.tabCraftButton.visible = false;
            this.tabProcessButton.visible = true;
        } else {
            this.tabCraftButton.visible = true;
            this.tabProcessButton.visible = false;
        }

        if (((ContainerPartBigPatternTerminal)getContainer()).substitute) {
            this.substitutionsEnabledBtn.visible = true;
            this.substitutionsDisabledBtn.visible = false;
        } else {
            this.substitutionsEnabledBtn.visible = false;
            this.substitutionsDisabledBtn.visible = true;
        }
        this.fontRendererObj.drawString(GuiText.PatternTerminal.getLocal(), 8, this.ySize - 96 + 2 - 81, 4210752);
    }

    public void drawBG(int offsetX, int offsetY, int n3, int n4) {
        this.mc.renderEngine.bindTexture(((ContainerPartBigPatternTerminal)getContainer()).isCraftingMode() ? GUI : GUI2);
        UtilGui.drawTexture(offsetX, offsetY, 0, 0, xSize, ySize, xSize, ySize, 768, 768);

        if (this.searchField != null) {
            this.searchField.drawTextBox();
        }
    }

    protected String getBackground() {
        return "guis/terminal.png";
    }

    protected void repositionSlot(AppEngSlot s) {
        if (s.isPlayerSide()) {
            s.yDisplayPosition = s.getY() + this.ySize - 78 - 5;
        } else {
            s.yDisplayPosition = s.getY() + this.ySize - 78 - 3;
        }
    }
}
