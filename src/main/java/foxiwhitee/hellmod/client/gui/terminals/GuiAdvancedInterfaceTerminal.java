package foxiwhitee.hellmod.client.gui.terminals;

import appeng.api.AEApi;
import appeng.api.config.Settings;
import appeng.api.config.TerminalStyle;
import appeng.client.gui.AEBaseGui;
import appeng.client.gui.widgets.ITooltip;
import appeng.client.me.ClientDCInternalInv;
import appeng.client.me.SlotDisconnected;
import appeng.container.slot.AppEngSlot;
import appeng.core.AEConfig;
import appeng.core.CommonHelper;
import appeng.helpers.PatternHelper;
import appeng.integration.IntegrationRegistry;
import appeng.integration.IntegrationType;
import appeng.util.Platform;
import com.google.common.collect.HashMultimap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.client.gui.widgets.CustomGuiImgButton;
import foxiwhitee.hellmod.client.gui.widgets.CustomGuiScrollbar;
import foxiwhitee.hellmod.client.gui.widgets.IDropToFillTextField;
import foxiwhitee.hellmod.client.gui.widgets.MEGuiTextField;
import foxiwhitee.hellmod.localization.CustomButtonToolTips;
import foxiwhitee.hellmod.localization.CustomGuiColors;
import foxiwhitee.hellmod.container.terminals.ContainerAdvancedInterfaceTerminal;
import foxiwhitee.hellmod.localization.CustomGuiText;
import foxiwhitee.hellmod.localization.CustomPlayerMessages;
import foxiwhitee.hellmod.parts.PartAdvancedInterfaceTerminal;
import foxiwhitee.hellmod.client.render.BlockPosHighlighter;
import foxiwhitee.hellmod.api.config.ActionItems;
import foxiwhitee.hellmod.utils.coord.CustomDimensionalCoord;
import foxiwhitee.hellmod.utils.coord.CustomWorldCoord;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedInterfaceTerminal extends AEBaseGui implements IDropToFillTextField {
    private static final int MAGIC_HEIGHT_NUMBER = 151;

    private static final int offsetX = 21;

    private final HashMap<Long, ClientDCInternalInv> byId = new HashMap<>();

    private final HashMultimap<String, ClientDCInternalInv> byName = HashMultimap.create();

    private final HashMap<ClientDCInternalInv, CustomDimensionalCoord> blockPosHashMap = new HashMap<>();

    private final HashMap<GuiButton, ClientDCInternalInv> guiButtonHashMap = new HashMap<>();

    private final ArrayList<String> names = new ArrayList<>();

    private final ArrayList<Object> lines = new ArrayList();

    private final Set<Object> matchedStacks = new HashSet();

    private final Map<String, Set<Object>> cachedSearches = new WeakHashMap<>();

    private final MEGuiTextField searchFieldOutputs;

    private final MEGuiTextField searchFieldInputs;

    private final MEGuiTextField searchFieldNames;

    private final CustomGuiImgButton guiButtonHideFull;

    private final CustomGuiImgButton guiButtonAssemblersOnly;

    private final CustomGuiImgButton guiButtonBrokenRecipes;

    private final CustomGuiImgButton terminalStyleBox;

    private boolean refreshList = false;

    private boolean onlyMolecularAssemblers = false;

    private boolean onlyBrokenRecipes = false;

    private int rows = 3;

    private static final String MOLECULAR_ASSEMBLER = "tile.appliedenergistics2.BlockMolecularAssembler";

    private String getStates() { return "gui/advancedInterfaceTerminalStater.png"; }
    public GuiAdvancedInterfaceTerminal(InventoryPlayer inventoryPlayer, PartAdvancedInterfaceTerminal te) {
        super((Container)new ContainerAdvancedInterfaceTerminal(inventoryPlayer, te));
        String state = this.getStates();
        CustomGuiScrollbar scrollbar = new CustomGuiScrollbar() {
            public void draw(AEBaseGui g) {
                g.bindTexture("appliedenergistics2", state);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if (this.getRange() == 0) {
                    g.drawTexturedModalRect(this.displayX, this.displayY, 144 + this.width, 0, this.width, 15);
                } else {
                    int offset = (this.currentScroll - this.minScroll) * (this.height - 15) / this.getRange();
                    g.drawTexturedModalRect(this.displayX, offset + this.displayY, 144, 0, this.width, 15);
                }

            }
        };
        setScrollBar(scrollbar);
        this.xSize = 246;
        this.ySize = 273;
        this.searchFieldInputs = new MEGuiTextField(90, 12, CustomButtonToolTips.SearchFieldInputs.getLocal()) {
            public void onTextChange(String oldText) {
                GuiAdvancedInterfaceTerminal.this.refreshList();
            }
        };
        this.searchFieldOutputs = new MEGuiTextField(90, 12, CustomButtonToolTips.SearchFieldOutputs.getLocal()) {
            public void onTextChange(String oldText) {
                GuiAdvancedInterfaceTerminal.this.refreshList();
            }
        };
        this.searchFieldNames = new MEGuiTextField(90, 12, CustomButtonToolTips.SearchFieldNames.getLocal()) {
            public void onTextChange(String oldText) {
                GuiAdvancedInterfaceTerminal.this.refreshList();
            }
        };
        this.searchFieldNames.setFocused(true);
        this.guiButtonAssemblersOnly = new CustomGuiImgButton(0, 0, (Enum)Settings.ACTIONS, null) {
            public String getStates() {return state; }
        };
        this.guiButtonHideFull = new CustomGuiImgButton(0, 0, (Enum)Settings.ACTIONS, null) {
            public String getStates() {return state; }
        };
        this.guiButtonBrokenRecipes = new CustomGuiImgButton(0, 0, (Enum)Settings.ACTIONS, null) {
            public String getStates() {return state; }
        };
        this.terminalStyleBox = new CustomGuiImgButton(0, 0, (Enum)Settings.TERMINAL_STYLE, null) {
            public String getStates() {return state; }
        };
    }

    private void setScrollBar() {
        getScrollBar().setTop(52).setLeft(209).setHeight(this.rows * 18 - 4);
        getScrollBar().setRange(0, this.lines.size() - this.rows, 2);
    }

    public void initGui() {
        this.rows = calculateRowsCount();
        super.initGui();
        this.ySize = 151 + this.rows * 18;
        int unusedSpace = this.height - this.ySize;
        this.guiTop = (int)Math.floor((unusedSpace / ((unusedSpace < 0) ? 3.8F : 2.0F)));
        this.searchFieldInputs.x = this.guiLeft + Math.max(42, 21);
        this.searchFieldInputs.y = this.guiTop + 25;
        this.searchFieldOutputs.x = this.guiLeft + Math.max(42, 21);
        this.searchFieldOutputs.y = this.guiTop + 38;
        this.searchFieldNames.x = this.guiLeft + Math.max(33, 21) + 99;
        this.searchFieldNames.y = this.guiTop + 38;
        this.guiButtonAssemblersOnly.yPosition = this.guiTop + 173 + (Math.max(rows - 5, 1) - 1) * 18;
        this.guiButtonAssemblersOnly.xPosition = this.guiLeft + 16;
        this.guiButtonHideFull.yPosition = this.guiTop + 173 + 18 + (Math.max(rows - 5, 1) - 1) * 18;
        this.guiButtonHideFull.xPosition = this.guiLeft + 16;
        this.guiButtonBrokenRecipes.yPosition = this.guiTop + 173 + 36 + (Math.max(rows - 5, 1) - 1) * 18;
        this.guiButtonBrokenRecipes.xPosition = this.guiLeft + 16;
        this.terminalStyleBox.xPosition = this.guiLeft + 16;
        this.terminalStyleBox.yPosition = this.guiTop + 16;
        setScrollBar();
        repositionSlots();
    }

    protected void repositionSlots() {
        for (Object obj : this.inventorySlots.inventorySlots) {
            if (obj instanceof AppEngSlot) {
                AppEngSlot slot = (AppEngSlot)obj;
                slot.yDisplayPosition = this.ySize + slot.getY() - 78 - 7;
            }
        }
    }

    protected int calculateRowsCount() {
        int maxRows = getMaxRows();
        boolean hasNEI = IntegrationRegistry.INSTANCE.isEnabled(IntegrationType.NEI);
        int NEIPadding = hasNEI ? 40 : 0;
        int extraSpace = this.height - 151 - NEIPadding;
        return Math.max(3, Math.min(maxRows, extraSpace / 18));
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        int offset = 51;
        int ex = getScrollBar().getCurrentScroll();
        for (int x = 0; x < this.rows && ex + x < this.lines.size(); x++) {
            Object lineObj = this.lines.get(ex + x);
            if (lineObj instanceof ClientDCInternalInv) {
                ClientDCInternalInv inv = (ClientDCInternalInv)lineObj;
                for (int z = 0; z < inv.getInventory().getSizeInventory(); z++) {
                    if (this.matchedStacks.contains(inv.getInventory().getStackInSlot(z)))
                        drawRect(z * 18 + 22 + 21, 1 + offset, z * 18 + 22 + 21 + 16, 1 + offset + 16, CustomGuiColors.InterfaceTerminalMatch

                                .getColor());
                }
            } else if (lineObj instanceof String) {
                String name = (String)lineObj;
                int rows = this.byName.get(name).size();
                String postfix = "";
                if (rows > 1)
                    postfix = " (" + rows + ')';
                while (name.length() > 2 && this.fontRendererObj.getStringWidth(name + postfix) > 158)
                    name = name.substring(0, name.length() - 1);
                this.fontRendererObj.drawString(name + postfix, 24+21, 6 + offset, CustomGuiColors.InterfaceTerminalName

                        .getColor());
            }
            offset += 18;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float btn) {
        this.buttonList.clear();
        this.inventorySlots.inventorySlots.removeIf(slot -> slot instanceof SlotDisconnected);
        this.guiButtonAssemblersOnly.set(
                this.onlyMolecularAssemblers ? (Enum)ActionItems.MOLECULAR_ASSEMBLEERS_ON : (Enum)ActionItems.MOLECULAR_ASSEMBLEERS_OFF);
        this.guiButtonHideFull.set(
                HellConfig.showOnlyInterfacesWithFreeSlotsInInterfaceTerminal ?
                        (Enum)ActionItems.TOGGLE_SHOW_FULL_INTERFACES_OFF :
                        (Enum)ActionItems.TOGGLE_SHOW_FULL_INTERFACES_ON);
        this.guiButtonBrokenRecipes.set(
                this.onlyBrokenRecipes ? (Enum)ActionItems.TOGGLE_SHOW_ONLY_INVALID_PATTERN_OFF :
                        (Enum) ActionItems.TOGGLE_SHOW_ONLY_INVALID_PATTERN_ON);
        this.terminalStyleBox.set(AEConfig.instance.settings.getSetting(Settings.TERMINAL_STYLE));
        this.buttonList.add(this.guiButtonAssemblersOnly);
        this.buttonList.add(this.guiButtonHideFull);
        this.buttonList.add(this.guiButtonBrokenRecipes);
        this.buttonList.add(this.terminalStyleBox);
        this.guiButtonHashMap.clear();
        int offset = 51;
        int ex = getScrollBar().getCurrentScroll();
        String state = this.getStates();
        for (int x = 0; x < this.rows && ex + x < this.lines.size(); x++) {
            Object lineObj = this.lines.get(ex + x);
            if (lineObj instanceof ClientDCInternalInv) {
                ClientDCInternalInv inv = (ClientDCInternalInv)lineObj;
                for (int z = 0; z < inv.getInventory().getSizeInventory(); z++)
                    this.inventorySlots.inventorySlots.add(new SlotDisconnected(inv, z, z * 18 + 43, 1 + offset));
                CustomGuiImgButton guiImgButton = new CustomGuiImgButton(this.guiLeft + 17, this.guiTop + offset + 1, (Enum)Settings.ACTIONS, (Enum)ActionItems.HIGHLIGHT_INTERFACE) {
                    public String getStates() {return state; }
                };
                this.guiButtonHashMap.put(guiImgButton, inv);
                this.buttonList.add(guiImgButton);
            }
            offset += 18;
        }
        super.drawScreen(mouseX, mouseY, btn);
        handleTooltip(mouseX, mouseY, (ITooltip)this.searchFieldInputs);
        handleTooltip(mouseX, mouseY, (ITooltip)this.searchFieldOutputs);
        handleTooltip(mouseX, mouseY, (ITooltip)this.searchFieldNames);
    }

    protected void handleTooltip(int mouseX, int mouseY, ITooltip tooltip) {
        int x = tooltip.xPos();
        int y = tooltip.yPos();
        if (x < mouseX && x + tooltip.getWidth() > mouseX && tooltip.isVisible() &&
                y < mouseY && y + tooltip.getHeight() > mouseY) {
            if (y < 15)
                y = 15;
            String msg = tooltip.getMessage();
            if (msg != null && !"".equals(msg))
                drawTooltip(x + 11, y + 4, 0, msg);
        }
    }

    protected void mouseClicked(int xCoord, int yCoord, int btn) {
        this.searchFieldInputs.mouseClicked(xCoord, yCoord, btn);
        this.searchFieldOutputs.mouseClicked(xCoord, yCoord, btn);
        this.searchFieldNames.mouseClicked(xCoord, yCoord, btn);
        super.mouseClicked(xCoord, yCoord, btn);
    }

    protected void actionPerformed(GuiButton btn) {
        if (this.guiButtonHashMap.containsKey(btn)) {
            CustomDimensionalCoord blockPos = this.blockPosHashMap.get(this.guiButtonHashMap.get(btn));
            CustomWorldCoord blockPos2 = new CustomWorldCoord((int)this.mc.thePlayer.posX, (int)this.mc.thePlayer.posY, (int)this.mc.thePlayer.posZ);
            int q = this.mc.theWorld.provider.dimensionId;
            int w = blockPos.getDimension();
            if (this.mc.theWorld.provider.dimensionId != blockPos.getDimension()) {
                this.mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentTranslation(CustomPlayerMessages.InterfaceInOtherDim

                        .getName(), new Object[] { Integer.valueOf(blockPos.getDimension()) }));
            } else {
                BlockPosHighlighter.highlightBlock(blockPos, System.currentTimeMillis() + 500L * CustomWorldCoord.getTaxicabDistance((appeng.api.util.WorldCoord)blockPos, blockPos2));
                this.mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentTranslation(CustomPlayerMessages.InterfaceHighlighted

                        .getName(), new Object[] { Integer.valueOf(blockPos.x),
                        Integer.valueOf(blockPos.y),
                        Integer.valueOf(blockPos.z) }));
            }
            this.mc.thePlayer.closeScreen();
        } else if (btn == this.guiButtonHideFull) {
            HellConfig.showOnlyInterfacesWithFreeSlotsInInterfaceTerminal = !HellConfig.showOnlyInterfacesWithFreeSlotsInInterfaceTerminal;
            refreshList();
        } else if (btn == this.guiButtonAssemblersOnly) {
            this.onlyMolecularAssemblers = !this.onlyMolecularAssemblers;
            refreshList();
        } else if (btn == this.guiButtonBrokenRecipes) {
            this.onlyBrokenRecipes = !this.onlyBrokenRecipes;
            refreshList();
        } else if (btn instanceof CustomGuiImgButton) {
            CustomGuiImgButton iBtn = (CustomGuiImgButton) btn;
            if (iBtn.getSetting() != Settings.ACTIONS) {
                Enum cv = iBtn.getCurrentValue();
                boolean backwards = Mouse.isButtonDown(1);
                Enum next = Platform.rotateEnum(cv, backwards, iBtn.getSetting().getPossibleValues());
                if (btn == this.terminalStyleBox) {
                    AEConfig.instance.settings.putSetting(iBtn.getSetting(), next);
                    reinitialize();
                }
                iBtn.set(next);
            }
        }
    }

    private void reinitialize() {
        this.buttonList.clear();
        initGui();
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        bindTexture("gui/advanced_interface_terminal.png");
        UtilGui.drawTexture(offsetX, offsetY, 0, 0, this.xSize, 130, this.xSize, 130);
        int offset = 51;
        int ex = getScrollBar().getCurrentScroll();
        int x;
        int rowsAdditional = Math.max(rows - 5, 1);
        int rowOffsetY;
        for (x = 0; x < rowsAdditional; x++) {
            if (x != 0) {
                rowOffsetY = offsetY + 143 + (x - 1) * 18;
                UtilGui.drawTexture(offsetX + 4, rowOffsetY, 4, 130, 30, 8, 30, 8);
                UtilGui.drawTexture(offsetX + 4, rowOffsetY + 8, 4, 130, 30, 10, 30, 10);

                UtilGui.drawTexture(offsetX + 230, rowOffsetY, 230, 130, 12, 8, 12, 8);
                UtilGui.drawTexture(offsetX + 230, rowOffsetY + 8, 230, 130, 12, 10, 12, 10);

                UtilGui.drawTexture(offsetX + 34, rowOffsetY, 34, 126, 196, 18, 196, 18);
            } else {
                UtilGui.drawTexture(offsetX, offsetY + 130, 0, 130, 242, 13, 242, 13);
            }
        }
        UtilGui.drawTexture(offsetX, offsetY + 143 + (rowsAdditional - 1) * 18, 0, 143, this.xSize, 140, this.xSize, 140);
        for (x = 0; x < this.rows && ex + x < this.lines.size(); x++) {
            Object lineObj = this.lines.get(ex + x);
            if (lineObj instanceof ClientDCInternalInv) {
                ClientDCInternalInv inv = (ClientDCInternalInv)lineObj;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                int width = inv.getInventory().getSizeInventory() * 18;
                UtilGui.drawTexture(offsetX + 42, offsetY + offset, 42, 172, width, 18, width, 18);
            }
            offset += 18;
        }
        this.searchFieldInputs.drawTextBox();
        this.searchFieldOutputs.drawTextBox();
        this.searchFieldNames.drawTextBox();
    }

    protected void keyTyped(char character, int key) {
        if (!checkHotbarKeys(key)) {
            if (character == ' ') {
                if ((this.searchFieldInputs.getText().isEmpty() && this.searchFieldInputs.isFocused()) || (this.searchFieldOutputs
                        .getText().isEmpty() && this.searchFieldOutputs.isFocused()) || (this.searchFieldNames
                        .getText().isEmpty() && this.searchFieldNames.isFocused()))
                    return;
            } else if (character == '\t' &&
                    handleTab()) {
                return;
            }
            if (this.searchFieldInputs.textboxKeyTyped(character, key) || this.searchFieldOutputs.textboxKeyTyped(character, key) || this.searchFieldNames
                    .textboxKeyTyped(character, key)) {
                refreshList();
            } else {
                super.keyTyped(character, key);
            }
        }
    }

    private boolean handleTab() {
        if (this.searchFieldInputs.isFocused()) {
            this.searchFieldInputs.setFocused(false);
            if (isShiftKeyDown()) {
                this.searchFieldNames.setFocused(true);
            } else {
                this.searchFieldOutputs.setFocused(true);
            }
            return true;
        }
        if (this.searchFieldOutputs.isFocused()) {
            this.searchFieldOutputs.setFocused(false);
            if (isShiftKeyDown()) {
                this.searchFieldInputs.setFocused(true);
            } else {
                this.searchFieldNames.setFocused(true);
            }
            return true;
        }
        if (this.searchFieldNames.isFocused()) {
            this.searchFieldNames.setFocused(false);
            if (isShiftKeyDown()) {
                this.searchFieldOutputs.setFocused(true);
            } else {
                this.searchFieldInputs.setFocused(true);
            }
            return true;
        }
        return false;
    }

    public void postUpdate(NBTTagCompound in) {
        if (in.getBoolean("clear")) {
            this.byId.clear();
            this.refreshList = true;
        }
        for (Object oKey : in.func_150296_c()) {
            String key = (String)oKey;
            if (key.startsWith("="))
                try {
                    long id = Long.parseLong(key.substring(1), 36);
                    NBTTagCompound invData = in.getCompoundTag(key);
                    int size = invData.getInteger("size");
                    ClientDCInternalInv current = getById(id, invData.getLong("sortBy"), invData.getString("un"), size);
                    int X = invData.getInteger("x");
                    int Y = invData.getInteger("y");
                    int Z = invData.getInteger("z");
                    int dim = invData.getInteger("dim");
                    this.blockPosHashMap.put(current, new CustomDimensionalCoord(X, Y, Z, dim));
                    for (int x = 0; x < current.getInventory().getSizeInventory(); x++) {
                        String which = Integer.toString(x);
                        if (invData.hasKey(which))
                            current.getInventory().setInventorySlotContents(x, ItemStack.loadItemStackFromNBT(invData.getCompoundTag(which)));
                    }
                } catch (NumberFormatException numberFormatException) {}
        }
        if (this.refreshList) {
            this.refreshList = false;
            this.cachedSearches.clear();
            refreshList();
        }
    }

    private void refreshList() {
        this.byName.clear();
        this.buttonList.clear();
        this.matchedStacks.clear();
        String searchFieldInputs = this.searchFieldInputs.getText().toLowerCase();
        String searchFieldOutputs = this.searchFieldOutputs.getText().toLowerCase();
        String searchFieldNames = this.searchFieldNames.getText().toLowerCase();
        Set<Object> cachedSearch = getCacheForSearchTerm("IN:" + searchFieldInputs + "OUT:" + searchFieldOutputs + "NAME:" + searchFieldNames + HellConfig.showOnlyInterfacesWithFreeSlotsInInterfaceTerminal + this.onlyMolecularAssemblers + this.onlyBrokenRecipes);
        boolean rebuild = cachedSearch.isEmpty();
        for (ClientDCInternalInv entry : this.byId.values()) {
            if (!rebuild && !cachedSearch.contains(entry))
                continue;
            boolean found = (searchFieldInputs.isEmpty() && searchFieldOutputs.isEmpty());
            boolean interfaceHasFreeSlots = false;
            boolean interfaceHasBrokenRecipes = false;
            if (!found || HellConfig.showOnlyInterfacesWithFreeSlotsInInterfaceTerminal || this.onlyBrokenRecipes)
                for (ItemStack itemStack : entry.getInventory()) {
                    if (itemStack == null) {
                        interfaceHasFreeSlots = true;
                        continue;
                    }
                    if (this.onlyBrokenRecipes && recipeIsBroken(itemStack))
                        interfaceHasBrokenRecipes = true;
                    if ((!searchFieldInputs.isEmpty() && itemStackMatchesSearchTerm(itemStack, searchFieldInputs, 0)) || (
                            !searchFieldOutputs.isEmpty() &&
                                    itemStackMatchesSearchTerm(itemStack, searchFieldOutputs, 1))) {
                        found = true;
                        this.matchedStacks.add(itemStack);
                    }
                }
            boolean molecularAssembler = false;
            if (entry.getName().contains("tile.appliedenergistics2.BlockMolecularAssembler") ||
                entry.getName().contains("tile.baseMolecularAssembler") ||
                entry.getName().contains("tile.hybridMolecularAssembler") ||
                entry.getName().contains("tile.ultimateMolecularAssembler")) {
                molecularAssembler = true;
            } else if (entry.getName().contains(LocalizationUtils.localize("tile.appliedenergistics2.BlockMolecularAssembler.name")) ||
                       entry.getName().contains(LocalizationUtils.localize("tile.baseMolecularAssembler.name")) ||
                       entry.getName().contains(LocalizationUtils.localize("tile.hybridMolecularAssembler.name")) ||
                       entry.getName().contains(LocalizationUtils.localize("tile.ultimateMolecularAssembler.name"))) {
                molecularAssembler = true;
            }
            if (found && entry.getName().toLowerCase().contains(searchFieldNames) && (!this.onlyMolecularAssemblers || molecularAssembler) && (!HellConfig.showOnlyInterfacesWithFreeSlotsInInterfaceTerminal || interfaceHasFreeSlots) && (!this.onlyBrokenRecipes || interfaceHasBrokenRecipes)) {
                this.byName.put(entry.getName(), entry);
                cachedSearch.add(entry);
                continue;
            }
            cachedSearch.remove(entry);
        }
        this.names.clear();
        this.names.addAll(this.byName.keySet());
        Collections.sort(this.names);
        this.lines.clear();
        this.lines.ensureCapacity(this.names.size() + this.byId.size());
        for (String n : this.names) {
            this.lines.add(n);
            ArrayList<ClientDCInternalInv> clientInventories = new ArrayList<>(this.byName.get(n));
            Collections.sort(clientInventories);
            this.lines.addAll(clientInventories);
        }
        setScrollBar();
    }

    private boolean itemStackMatchesSearchTerm(ItemStack itemStack, String searchTerm, int pass) {
        if (itemStack == null)
            return false;
        NBTTagCompound encodedValue = itemStack.getTagCompound();
        if (encodedValue == null)
            return false;
        NBTTagList tags = encodedValue.getTagList((pass == 0) ? "in" : "out", 10);
        boolean containsInvalidDisplayName = CustomGuiText.UnknownItem.getLocal().toLowerCase().contains(searchTerm);
        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound tag = tags.getCompoundTagAt(i);
            ItemStack parsedItemStack = ItemStack.loadItemStackFromNBT(tag);
            if (parsedItemStack != null) {
                String displayName = Platform.getItemDisplayName(AEApi.instance().storage().createItemStack(parsedItemStack)).toLowerCase();
                if (displayName.contains(searchTerm))
                    return true;
            } else if (containsInvalidDisplayName && !tag.hasNoTags()) {
                return true;
            }
        }
        return false;
    }

    private boolean recipeIsBroken(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        NBTTagCompound encodedValue = itemStack.getTagCompound();
        if (encodedValue == null)
            return true;
        World w = CommonHelper.proxy.getWorld();
        if (w == null)
            return false;
        try {
            new PatternHelper(itemStack, w);
            return false;
        } catch (Throwable t) {
            return true;
        }
    }

    private Set<Object> getCacheForSearchTerm(String searchTerm) {
        if (!this.cachedSearches.containsKey(searchTerm))
            this.cachedSearches.put(searchTerm, new HashSet());
        Set<Object> cache = this.cachedSearches.get(searchTerm);
        if (cache.isEmpty() && searchTerm.length() > 1) {
            cache.addAll(getCacheForSearchTerm(searchTerm.substring(0, searchTerm.length() - 1)));
            return cache;
        }
        return cache;
    }

    private int getMaxRows() {
        return (AEConfig.instance.getConfigManager().getSetting(Settings.TERMINAL_STYLE) == TerminalStyle.SMALL) ?
                6 : Integer
                .MAX_VALUE;
    }

    private ClientDCInternalInv getById(long id, long sortBy, String unlocalizedName, int sizeInit) {
        ClientDCInternalInv o = this.byId.get(Long.valueOf(id));
        if (o == null) {
            this.byId.put(Long.valueOf(id), o = new ClientDCInternalInv(sizeInit, id, sortBy, unlocalizedName));
            this.refreshList = true;
        }
        return o;
    }

    public boolean isOverTextField(int mousex, int mousey) {
        return (this.searchFieldInputs.isMouseIn(mousex, mousey) || this.searchFieldOutputs.isMouseIn(mousex, mousey) || this.searchFieldNames
                .isMouseIn(mousex, mousey));
    }

    public void setTextFieldValue(String displayName, int mousex, int mousey, ItemStack stack) {
        if (this.searchFieldInputs.isMouseIn(mousex, mousey)) {
            this.searchFieldInputs.setText(displayName);
        } else if (this.searchFieldOutputs.isMouseIn(mousex, mousey)) {
            this.searchFieldOutputs.setText(displayName);
        } else if (this.searchFieldNames.isMouseIn(mousex, mousey)) {
            this.searchFieldNames.setText(displayName);
        }
    }
}
