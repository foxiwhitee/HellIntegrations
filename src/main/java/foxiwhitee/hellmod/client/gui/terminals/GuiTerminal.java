package foxiwhitee.hellmod.client.gui.terminals;

import appeng.api.config.*;
import appeng.api.implementations.guiobjects.IPortableCell;
import appeng.api.implementations.tiles.IMEChest;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.client.gui.AEBaseGui;
import appeng.client.gui.AEBaseMEGui;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.client.gui.widgets.ISortSource;
import appeng.client.gui.widgets.MEGuiTextField;
import appeng.client.me.InternalSlotME;
import appeng.client.me.ItemRepo;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.AppEngSlot;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.core.AEConfig;
import appeng.core.AELog;
import appeng.core.localization.GuiText;
import appeng.core.sync.AppEngPacket;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketSwitchGuis;
import appeng.core.sync.packets.PacketValueConfig;
import appeng.helpers.WirelessTerminalGuiObject;
import appeng.integration.IntegrationRegistry;
import appeng.integration.IntegrationType;
import appeng.parts.reporting.AbstractPartTerminal;
import appeng.tile.misc.TileSecurity;
import appeng.util.IConfigManagerHost;
import appeng.util.Platform;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.buttons.CustomGuiTabButton;
import foxiwhitee.hellmod.client.gui.widgets.CustomGuiImgButton;
import foxiwhitee.hellmod.client.gui.widgets.CustomGuiScrollbar;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.avaritia.container.ContainerPartBigProcessingPatternTerminal;
import foxiwhitee.hellmod.network.BasePacket;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import foxiwhitee.hellmod.utils.craft.IGuiMEMonitorableAccessor;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class GuiTerminal extends AEBaseMEGui implements ISortSource, IConfigManagerHost, IGuiMEMonitorableAccessor {
    public static int craftingGridOffsetX;
    public static int craftingGridOffsetY;
    private static String memoryText = "";
    private final ItemRepo repo;
    protected final IConfigManager configSrc;
    private final ContainerMEMonitorable container;
    protected MEGuiTextField searchField;
    private GuiText myName;
    private int perRow;
    private int reservedSpace;
    private boolean customSortOrder;
    private int standardSize;
    private CustomGuiImgButton ViewBox;
    private CustomGuiImgButton SortByBox;
    private CustomGuiImgButton SortDirBox;
    private CustomGuiImgButton searchBoxSettings;
    protected int offset;
    boolean hasFG = true;
    private CustomGuiImgButton substitutionsEnabledBtn;
    private CustomGuiImgButton substitutionsDisabledBtn;
    private CustomGuiImgButton clearBtn;
    private CustomGuiTabButton craftingStatusBtn;
    protected boolean hasSubstitutions = false;

    public GuiTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        this(inventoryPlayer, te, new ContainerMEMonitorable(inventoryPlayer, te), 185, 204);
    }

    public GuiTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te, ContainerMEMonitorable c) {
        this(inventoryPlayer, te, new ContainerMEMonitorable(inventoryPlayer, te), 185, 204);
    }

    public GuiTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te, ContainerMEMonitorable c, int xSize, int ySize) {
        super(c);
        this.xSize = xSize;
        this.ySize = ySize;
        this.perRow = 9;
        this.reservedSpace = 0;
        this.customSortOrder = true;
        CustomGuiScrollbar scrollbar = getCustomGuiScrollbar();
        this.setScrollBar(scrollbar);
        this.repo = new ItemRepo(scrollbar, this);
        this.standardSize = this.xSize;
        this.configSrc = ((IConfigurableObject) this.inventorySlots).getConfigManager();
        (this.container = (ContainerMEMonitorable) this.inventorySlots).setGui(this);
        if (te instanceof TileSecurity) {
            this.myName = GuiText.Security;
        } else if (te instanceof WirelessTerminalGuiObject) {
            this.myName = GuiText.WirelessTerminal;
        } else if (te instanceof IPortableCell) {
            this.myName = GuiText.PortableCell;
        } else if (te instanceof IMEChest) {
            this.myName = GuiText.Chest;
        } else if (te instanceof AbstractPartTerminal) {
            this.myName = GuiText.Terminal;
        }

    }

    private CustomGuiScrollbar getCustomGuiScrollbar() {
        String state = this.getStates();
        return new CustomGuiScrollbar() {
            public void draw(AEBaseGui g) {
                g.bindTexture("appliedenergistics2", state);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if (this.getRange() == 0) {
                    g.drawTexturedModalRect(this.displayX, this.displayY, 144 + this.width, 0, this.width, 15);
                } else {
                    int offset1 = (this.currentScroll - this.minScroll) * (this.height - 15) / this.getRange();
                    g.drawTexturedModalRect(this.displayX, offset1 + this.displayY, 144, 0, this.width, 15);
                }

            }
        };
    }

    public void postUpdate(List<IAEItemStack> list) {
        for (IAEItemStack is : list) {
            this.repo.postUpdate(is);
        }
        this.repo.updateView();
        this.setScrollBar();
    }

    private void setScrollBar() {
        this.getScrollBar().setTop(18 + 15 + 6).setLeft(175 + 16).setHeight(5 * 18 - 2);
        this.getScrollBar().setRange(0, (this.repo.size() + this.perRow - 1) / this.perRow - 5, Math.max(1, 5 / 6));
    }

    protected void actionPerformed(GuiButton btn) {
        if (btn == this.craftingStatusBtn)
            NetworkHandler.instance.sendToServer((AppEngPacket)new PacketSwitchGuis(GuiBridge.GUI_CRAFTING_STATUS));
        try {
            if (this.clearBtn == btn)
                NetworkManager.instance.sendToServer((BasePacket)new DefaultPacket("PatternTerminal.Clear", "1"));
            if (this.substitutionsEnabledBtn == btn || this.substitutionsDisabledBtn == btn)
                NetworkManager.instance.sendToServer((BasePacket)new DefaultPacket("PatternTerminal.Substitute", (this.substitutionsEnabledBtn == btn) ? "0" : "1"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (btn instanceof CustomGuiImgButton) {
            boolean backwards = Mouse.isButtonDown(1);
            CustomGuiImgButton iBtn = (CustomGuiImgButton) btn;
            if (iBtn.getSetting() != Settings.ACTIONS) {
                Enum cv = iBtn.getCurrentValue();
                Enum next = Platform.rotateEnum(cv, backwards, iBtn.getSetting().getPossibleValues());
                if (btn == this.searchBoxSettings) {
                    AEConfig.instance.settings.putSetting(iBtn.getSetting(), next);
                } else {
                    try {
                        NetworkHandler.instance.sendToServer(new PacketValueConfig(iBtn.getSetting().name(), next.name()));
                    } catch (IOException e) {
                        AELog.debug(e);
                    }
                }

                iBtn.set(next);
                if (next.getClass() == SearchBoxMode.class || next.getClass() == TerminalStyle.class) {
                    this.reinitialize();
                }
            }
        }

    }

    private void reinitialize() {
        this.buttonList.clear();
        this.initGui();
    }

    protected int getBtnShiftX() {
        return 0;
    }

    protected int getBtnShiftY() {
        return 0;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.perRow = AEConfig.instance.getConfigManager().getSetting(Settings.TERMINAL_STYLE) != TerminalStyle.FULL ? 9 : 9 + (this.width - this.standardSize) / 18;
        boolean hasNEI = IntegrationRegistry.INSTANCE.isEnabled(IntegrationType.NEI);
        this.getMeSlots().clear();
        List<InternalSlotME> slotsME;
        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < this.perRow; ++x) {
                slotsME = this.getMeSlots();
                slotsME.add(new InternalSlotME(this.repo, x + y * this.perRow, 26 + x * 18, 39 + y * 18));
            }
        }
        if (AEConfig.instance.getConfigManager().getSetting(Settings.TERMINAL_STYLE) != TerminalStyle.FULL) {
            this.xSize = this.standardSize + (this.perRow - 9) * 18;
        } else {
            this.xSize = this.standardSize;
        }
        super.initGui();
        this.ySize = 115 + 5 * 18 + this.reservedSpace;
        int unusedSpace = this.height - this.ySize;
        this.guiTop = (int) Math.floor((double) ((float) unusedSpace / (unusedSpace < 0 ? 3.8F : 2.0F)));
        this.offset = this.guiTop + 49 + this.getBtnShiftY();
        if (this.customSortOrder) {
            this.buttonList.add(this.SortByBox = new CustomGuiImgButton(this.getStates(), this.guiLeft + 216 + this.getBtnShiftX(), this.offset, Settings.SORT_BY, this.configSrc.getSetting(Settings.SORT_BY)));
            this.offset += 16;
        }

        this.buttonList.add(this.ViewBox = new CustomGuiImgButton(this.getStates(), this.guiLeft + 216 + this.getBtnShiftX(), offset, Settings.VIEW_MODE, this.configSrc.getSetting(Settings.VIEW_MODE)));
        this.offset += 16;
        this.buttonList.add(this.SortDirBox = new CustomGuiImgButton(this.getStates(), this.guiLeft + 216 + this.getBtnShiftX(), this.offset, Settings.SORT_DIRECTION, this.configSrc.getSetting(Settings.SORT_DIRECTION)));
        this.offset += 16;
        this.buttonList.add(this.searchBoxSettings = new CustomGuiImgButton(this.getStates(), this.guiLeft + 216 + this.getBtnShiftX(), this.offset, Settings.SEARCH_MODE, AEConfig.instance.settings.getSetting(Settings.SEARCH_MODE)) );
        this.offset += 16;
        this.searchField = new MEGuiTextField(this.fontRendererObj, this.guiLeft + 26, this.guiTop + 26, 160, 12);
        this.searchField.setEnableBackgroundDrawing(false);
        this.searchField.setMaxStringLength(100);
        this.searchField.setTextColor(16777215);
        this.searchField.setVisible(true);

        this.buttonList.add(this.craftingStatusBtn = new CustomGuiTabButton(this.getStates(), this.guiLeft + 237, this.guiTop + 50, 178, GuiText.CraftingStatus.getLocal(), itemRender));
        this.craftingStatusBtn.setHideEdge(13);
        if (hasSubstitutions) {
            this.substitutionsEnabledBtn = new CustomGuiImgButton(this.getStates(), this.guiLeft + 249, this.guiTop + 78, (Enum) Settings.ACTIONS, (Enum) ItemSubstitution.ENABLED);
            this.substitutionsEnabledBtn.setHalfSize(true);
            this.buttonList.add(this.substitutionsEnabledBtn);
            this.substitutionsDisabledBtn = new CustomGuiImgButton(this.getStates(), this.guiLeft + 249, this.guiTop + 78, (Enum) Settings.ACTIONS, (Enum) ItemSubstitution.DISABLED);
            this.substitutionsDisabledBtn.setHalfSize(true);
            this.buttonList.add(this.substitutionsDisabledBtn);
        }
        this.clearBtn = new CustomGuiImgButton(this.getStates(), this.guiLeft + 239, this.guiTop + 78, (Enum)Settings.ACTIONS, (Enum) ActionItems.CLOSE);
        this.clearBtn.setHalfSize(true);

        this.buttonList.add(this.clearBtn);
        Enum<?> setting = AEConfig.instance.settings.getSetting(Settings.SEARCH_MODE);
        this.searchField.setFocused(SearchBoxMode.AUTOSEARCH == setting || SearchBoxMode.NEI_AUTOSEARCH == setting);
        if (this.isSubGui()) {
            this.searchField.setText(memoryText);
            this.repo.setSearchString(memoryText);
            this.repo.updateView();
            this.setScrollBar();
        }

        craftingGridOffsetX = Integer.MAX_VALUE;
        craftingGridOffsetY = Integer.MAX_VALUE;
        Slot g;
        for (Object s : this.inventorySlots.inventorySlots) {
            if (s instanceof AppEngSlot && ((Slot) s).xDisplayPosition < 197) {
                this.repositionSlot((AppEngSlot) s);
            }

            if (s instanceof SlotCraftingMatrix || s instanceof SlotFakeCraftingMatrix) {
                g = (Slot) s;
                if (g.xDisplayPosition > 0 && g.yDisplayPosition > 0) {
                    craftingGridOffsetX = Math.min(craftingGridOffsetX, g.xDisplayPosition);
                    craftingGridOffsetY = Math.min(craftingGridOffsetY, g.yDisplayPosition);
                }
            }
        }

        craftingGridOffsetX -= 25;
        craftingGridOffsetY -= 6;
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        if (hasFG) {
            this.fontRendererObj.drawString(this.getGuiDisplayName(this.myName.getLocal()), 8, 6, 4210752);
            this.fontRendererObj.drawString(GuiText.inventory.getLocal(), 8, this.ySize - 96 + 3, 4210752);
        }
        if (hasSubstitutions) {
            if (((ContainerPatternTerminal) getContainer()).substitute) {
                this.substitutionsEnabledBtn.visible = true;
                this.substitutionsDisabledBtn.visible = false;
            } else {
                this.substitutionsEnabledBtn.visible = false;
                this.substitutionsDisabledBtn.visible = true;
            }
        }
    }

    protected void mouseClicked(int xCord, int yCord, int btn) {
        Enum<?> searchMode = AEConfig.instance.settings.getSetting(Settings.SEARCH_MODE);
        if (searchMode != SearchBoxMode.AUTOSEARCH && searchMode != SearchBoxMode.NEI_AUTOSEARCH) {
            this.searchField.mouseClicked(xCord, yCord, btn);
        }

        if (btn == 1 && this.searchField.isMouseIn(xCord, yCord)) {
            this.searchField.setText("");
            this.repo.setSearchString("");
            this.repo.updateView();
            this.setScrollBar();
        }

        super.mouseClicked(xCord, yCord, btn);
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        memoryText = this.searchField.getText();
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.bindTexture(this.getBackground());

        UtilGui.drawTexture(offsetX, offsetY, 0, 0, xSize, ySize, xSize, ySize, 768, 768);

        if (this.searchField != null) {
            this.searchField.drawTextBox();
        }
    }

    protected String getBackground() {
        return "guis/terminal.png";
    }

    protected ResourceLocation getBackgroundLocation() {
        return new ResourceLocation(HellCore.MODID, "textures/" + getBackground());
    }

    protected boolean isPowered() {
        return this.repo.hasPower();
    }

    int getMaxRows() {
        return 6;
    }

    protected void repositionSlot(AppEngSlot s) {
        s.yDisplayPosition = s.getY() + this.ySize - 78 - 5;
    }

    protected void keyTyped(char character, int key) {
        if (!this.checkHotbarKeys(key)) {
            if (character == ' ' && this.searchField.getText().isEmpty()) {
                return;
            }

            if (this.searchField.textboxKeyTyped(character, key)) {
                this.repo.setSearchString(this.searchField.getText());
                this.repo.updateView();
                this.setScrollBar();
            } else {
                super.keyTyped(character, key);
            }
        }

    }

    public void updateScreen() {
        this.repo.setPower(this.container.isPowered());
        super.updateScreen();
    }

    public Enum getSortBy() {
        return this.configSrc.getSetting(Settings.SORT_BY);
    }

    public Enum getSortDir() {
        return this.configSrc.getSetting(Settings.SORT_DIRECTION);
    }

    public Enum getSortDisplay() {
        return this.configSrc.getSetting(Settings.VIEW_MODE);
    }

    public void updateSetting(IConfigManager manager, Enum settingName, Enum newValue) {
        if (this.SortByBox != null) {
            this.SortByBox.set(this.configSrc.getSetting(Settings.SORT_BY));
        }

        if (this.SortDirBox != null) {
            this.SortDirBox.set(this.configSrc.getSetting(Settings.SORT_DIRECTION));
        }

        if (this.ViewBox != null) {
            this.ViewBox.set(this.configSrc.getSetting(Settings.VIEW_MODE));
        }

        this.repo.updateView();
    }

    int getReservedSpace() {
        return this.reservedSpace;
    }

    void setReservedSpace(int reservedSpace) {
        this.reservedSpace = reservedSpace;
    }

    public boolean isCustomSortOrder() {
        return this.customSortOrder;
    }

    void setCustomSortOrder(boolean customSortOrder) {
        this.customSortOrder = customSortOrder;
    }

    public int getStandardSize() {
        return this.standardSize;
    }

    void setStandardSize(int standardSize) {
        this.standardSize = standardSize;
    }

    @Override
    public void callSetReservedSpace(int space) {
        setReservedSpace(space);
    }

    @Override
    public void callSetStandardSize(int size) {
        setStandardSize(size);
    }

    protected String getStates() {
        return "gui/states.png";
    }

    protected ContainerMEMonitorable getContainer() {
        return container;
    }

    public void bindTexture(String file) {
        ResourceLocation loc = new ResourceLocation(HellCore.MODID, "textures/" + file);
        this.mc.getTextureManager().bindTexture(loc);
    }

    public void bindTexture(ResourceLocation loc) {
        this.mc.getTextureManager().bindTexture(loc);
    }
}