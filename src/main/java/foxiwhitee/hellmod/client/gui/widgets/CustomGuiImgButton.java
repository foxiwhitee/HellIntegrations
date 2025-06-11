
package foxiwhitee.hellmod.client.gui.widgets;

import appeng.api.config.AccessRestriction;
import appeng.api.config.ActionItems;
import appeng.api.config.CondenserOutput;
import appeng.api.config.FullnessMode;
import appeng.api.config.FuzzyMode;
import appeng.api.config.ItemSubstitution;
import appeng.api.config.LevelType;
import appeng.api.config.OperationMode;
import appeng.api.config.PowerUnits;
import appeng.api.config.RedstoneMode;
import appeng.api.config.RelativeDirection;
import appeng.api.config.SchedulingMode;
import appeng.api.config.SearchBoxMode;
import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.StorageFilter;
import appeng.api.config.TerminalStyle;
import appeng.api.config.ViewItems;
import appeng.api.config.YesNo;
import appeng.client.gui.widgets.ITooltip;
import appeng.client.texture.ExtraBlockTextures;
import appeng.core.localization.ButtonToolTips;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import foxiwhitee.hellmod.localization.CustomButtonToolTips;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class CustomGuiImgButton extends GuiButton implements ITooltip {
    private static final Pattern COMPILE = Pattern.compile("%s");
    private static final Pattern PATTERN_NEW_LINE = Pattern.compile("\\n", 16);
    private static Map<EnumPair, ButtonAppearance> appearances;
    private final Enum buttonSetting;
    private boolean halfSize = false;
    private String fillVar;
    private Enum currentValue;
    private final String states;

    public CustomGuiImgButton(int x, int y, Enum idx, Enum val) {
        this("guis/states.png", x, y, idx, val);
    }

    public CustomGuiImgButton(String states, int x, int y, Enum idx, Enum val) {
        super(0, 0, 16, "");
        this.states = states;
        this.buttonSetting = idx;
        this.currentValue = val;
        this.xPosition = x;
        this.yPosition = y;
        this.width = 16;
        this.height = 16;
        if (appearances == null) {
            appearances = new HashMap();
            this.registerApp(112, Settings.CONDENSER_OUTPUT, CondenserOutput.TRASH, ButtonToolTips.CondenserOutput, ButtonToolTips.Trash);
            this.registerApp(113, Settings.CONDENSER_OUTPUT, CondenserOutput.MATTER_BALLS, ButtonToolTips.CondenserOutput, ButtonToolTips.MatterBalls);
            this.registerApp(114, Settings.CONDENSER_OUTPUT, CondenserOutput.SINGULARITY, ButtonToolTips.CondenserOutput, ButtonToolTips.Singularity);
            this.registerApp(145, Settings.ACCESS, AccessRestriction.READ, ButtonToolTips.IOMode, ButtonToolTips.Read);
            this.registerApp(144, Settings.ACCESS, AccessRestriction.WRITE, ButtonToolTips.IOMode, ButtonToolTips.Write);
            this.registerApp(146, Settings.ACCESS, AccessRestriction.READ_WRITE, ButtonToolTips.IOMode, ButtonToolTips.ReadWrite);
            this.registerApp(160, Settings.POWER_UNITS, PowerUnits.AE, ButtonToolTips.PowerUnits, PowerUnits.AE.unlocalizedName);
            this.registerApp(161, Settings.POWER_UNITS, PowerUnits.EU, ButtonToolTips.PowerUnits, PowerUnits.EU.unlocalizedName);
            this.registerApp(162, Settings.POWER_UNITS, PowerUnits.MK, ButtonToolTips.PowerUnits, PowerUnits.MK.unlocalizedName);
            this.registerApp(163, Settings.POWER_UNITS, PowerUnits.WA, ButtonToolTips.PowerUnits, PowerUnits.WA.unlocalizedName);
            this.registerApp(164, Settings.POWER_UNITS, PowerUnits.RF, ButtonToolTips.PowerUnits, PowerUnits.RF.unlocalizedName);
            this.registerApp(3, Settings.REDSTONE_CONTROLLED, RedstoneMode.IGNORE, ButtonToolTips.RedstoneMode, ButtonToolTips.AlwaysActive);
            this.registerApp(0, Settings.REDSTONE_CONTROLLED, RedstoneMode.LOW_SIGNAL, ButtonToolTips.RedstoneMode, ButtonToolTips.ActiveWithoutSignal);
            this.registerApp(1, Settings.REDSTONE_CONTROLLED, RedstoneMode.HIGH_SIGNAL, ButtonToolTips.RedstoneMode, ButtonToolTips.ActiveWithSignal);
            this.registerApp(2, Settings.REDSTONE_CONTROLLED, RedstoneMode.SIGNAL_PULSE, ButtonToolTips.RedstoneMode, ButtonToolTips.ActiveOnPulse);
            this.registerApp(0, Settings.REDSTONE_EMITTER, RedstoneMode.LOW_SIGNAL, ButtonToolTips.RedstoneMode, ButtonToolTips.EmitLevelsBelow);
            this.registerApp(1, Settings.REDSTONE_EMITTER, RedstoneMode.HIGH_SIGNAL, ButtonToolTips.RedstoneMode, ButtonToolTips.EmitLevelAbove);
            this.registerApp(51, Settings.OPERATION_MODE, OperationMode.FILL, ButtonToolTips.TransferDirection, ButtonToolTips.TransferToStorageCell);
            this.registerApp(50, Settings.OPERATION_MODE, OperationMode.EMPTY, ButtonToolTips.TransferDirection, ButtonToolTips.TransferToNetwork);
            this.registerApp(51, Settings.IO_DIRECTION, RelativeDirection.LEFT, ButtonToolTips.TransferDirection, ButtonToolTips.TransferToStorageCell);
            this.registerApp(50, Settings.IO_DIRECTION, RelativeDirection.RIGHT, ButtonToolTips.TransferDirection, ButtonToolTips.TransferToNetwork);
            this.registerApp(48, Settings.SORT_DIRECTION, SortDir.ASCENDING, ButtonToolTips.SortOrder, ButtonToolTips.ToggleSortDirection);
            this.registerApp(49, Settings.SORT_DIRECTION, SortDir.DESCENDING, ButtonToolTips.SortOrder, ButtonToolTips.ToggleSortDirection);
            this.registerApp(35, Settings.SEARCH_MODE, SearchBoxMode.AUTOSEARCH, ButtonToolTips.SearchMode, ButtonToolTips.SearchMode_Auto);
            this.registerApp(36, Settings.SEARCH_MODE, SearchBoxMode.MANUAL_SEARCH, ButtonToolTips.SearchMode, ButtonToolTips.SearchMode_Standard);
            this.registerApp(37, Settings.SEARCH_MODE, SearchBoxMode.NEI_AUTOSEARCH, ButtonToolTips.SearchMode, ButtonToolTips.SearchMode_NEIAuto);
            this.registerApp(38, Settings.SEARCH_MODE, SearchBoxMode.NEI_MANUAL_SEARCH, ButtonToolTips.SearchMode, ButtonToolTips.SearchMode_NEIStandard);
            this.registerApp(83, Settings.LEVEL_TYPE, LevelType.ENERGY_LEVEL, ButtonToolTips.LevelType, ButtonToolTips.LevelType_Energy);
            this.registerApp(67, Settings.LEVEL_TYPE, LevelType.ITEM_LEVEL, ButtonToolTips.LevelType, ButtonToolTips.LevelType_Item);
            this.registerApp(208, Settings.TERMINAL_STYLE, TerminalStyle.TALL, ButtonToolTips.TerminalStyle, ButtonToolTips.TerminalStyle_Tall);
            this.registerApp(209, Settings.TERMINAL_STYLE, TerminalStyle.SMALL, ButtonToolTips.TerminalStyle, ButtonToolTips.TerminalStyle_Small);
            this.registerApp(210, Settings.TERMINAL_STYLE, TerminalStyle.FULL, ButtonToolTips.TerminalStyle, ButtonToolTips.TerminalStyle_Full);
            this.registerApp(64, Settings.SORT_BY, SortOrder.NAME, ButtonToolTips.SortBy, ButtonToolTips.ItemName);
            this.registerApp(65, Settings.SORT_BY, SortOrder.AMOUNT, ButtonToolTips.SortBy, ButtonToolTips.NumberOfItems);
            this.registerApp(68, Settings.SORT_BY, SortOrder.INVTWEAKS, ButtonToolTips.SortBy, ButtonToolTips.InventoryTweaks);
            this.registerApp(69, Settings.SORT_BY, SortOrder.MOD, ButtonToolTips.SortBy, ButtonToolTips.Mod);
            this.registerApp(66, Settings.ACTIONS, ActionItems.WRENCH, ButtonToolTips.PartitionStorage, ButtonToolTips.PartitionStorageHint);
            this.registerApp(6, Settings.ACTIONS, ActionItems.CLOSE, ButtonToolTips.Clear, ButtonToolTips.ClearSettings);
            this.registerApp(6, Settings.ACTIONS, ActionItems.STASH, ButtonToolTips.Stash, ButtonToolTips.StashDesc);
            this.registerApp(8, Settings.ACTIONS, ActionItems.ENCODE, ButtonToolTips.Encode, ButtonToolTips.EncodeDescription);
            this.registerApp(52, Settings.ACTIONS, ItemSubstitution.ENABLED, ButtonToolTips.Substitutions, ButtonToolTips.SubstitutionsDescEnabled);
            this.registerApp(55, Settings.ACTIONS, ItemSubstitution.DISABLED, ButtonToolTips.Substitutions, ButtonToolTips.SubstitutionsDescDisabled);
            this.registerApp(16, Settings.VIEW_MODE, ViewItems.STORED, ButtonToolTips.View, ButtonToolTips.StoredItems);
            this.registerApp(18, Settings.VIEW_MODE, ViewItems.ALL, ButtonToolTips.View, ButtonToolTips.StoredCraftable);
            this.registerApp(19, Settings.VIEW_MODE, ViewItems.CRAFTABLE, ButtonToolTips.View, ButtonToolTips.Craftable);
            this.registerApp(96, Settings.FUZZY_MODE, FuzzyMode.PERCENT_25, ButtonToolTips.FuzzyMode, ButtonToolTips.FZPercent_25);
            this.registerApp(97, Settings.FUZZY_MODE, FuzzyMode.PERCENT_50, ButtonToolTips.FuzzyMode, ButtonToolTips.FZPercent_50);
            this.registerApp(98, Settings.FUZZY_MODE, FuzzyMode.PERCENT_75, ButtonToolTips.FuzzyMode, ButtonToolTips.FZPercent_75);
            this.registerApp(99, Settings.FUZZY_MODE, FuzzyMode.PERCENT_99, ButtonToolTips.FuzzyMode, ButtonToolTips.FZPercent_99);
            this.registerApp(100, Settings.FUZZY_MODE, FuzzyMode.IGNORE_ALL, ButtonToolTips.FuzzyMode, ButtonToolTips.FZIgnoreAll);
            this.registerApp(80, Settings.FULLNESS_MODE, FullnessMode.EMPTY, ButtonToolTips.OperationMode, ButtonToolTips.MoveWhenEmpty);
            this.registerApp(81, Settings.FULLNESS_MODE, FullnessMode.HALF, ButtonToolTips.OperationMode, ButtonToolTips.MoveWhenWorkIsDone);
            this.registerApp(82, Settings.FULLNESS_MODE, FullnessMode.FULL, ButtonToolTips.OperationMode, ButtonToolTips.MoveWhenFull);
            this.registerApp(21, Settings.BLOCK, YesNo.YES, ButtonToolTips.InterfaceBlockingMode, ButtonToolTips.Blocking);
            this.registerApp(20, Settings.BLOCK, YesNo.NO, ButtonToolTips.InterfaceBlockingMode, ButtonToolTips.NonBlocking);
            this.registerApp(19, Settings.CRAFT_ONLY, YesNo.YES, ButtonToolTips.Craft, ButtonToolTips.CraftOnly);
            this.registerApp(18, Settings.CRAFT_ONLY, YesNo.NO, ButtonToolTips.Craft, ButtonToolTips.CraftEither);
            this.registerApp(178, Settings.CRAFT_VIA_REDSTONE, YesNo.YES, ButtonToolTips.EmitterMode, ButtonToolTips.CraftViaRedstone);
            this.registerApp(177, Settings.CRAFT_VIA_REDSTONE, YesNo.NO, ButtonToolTips.EmitterMode, ButtonToolTips.EmitWhenCrafting);
            this.registerApp(53, Settings.STORAGE_FILTER, StorageFilter.EXTRACTABLE_ONLY, ButtonToolTips.ReportInaccessibleItems, ButtonToolTips.ReportInaccessibleItemsNo);
            this.registerApp(54, Settings.STORAGE_FILTER, StorageFilter.NONE, ButtonToolTips.ReportInaccessibleItems, ButtonToolTips.ReportInaccessibleItemsYes);
            this.registerApp(224, Settings.PLACE_BLOCK, YesNo.YES, ButtonToolTips.BlockPlacement, ButtonToolTips.BlockPlacementYes);
            this.registerApp(225, Settings.PLACE_BLOCK, YesNo.NO, ButtonToolTips.BlockPlacement, ButtonToolTips.BlockPlacementNo);
            this.registerApp(240, Settings.SCHEDULING_MODE, SchedulingMode.DEFAULT, ButtonToolTips.SchedulingMode, ButtonToolTips.SchedulingModeDefault);
            this.registerApp(241, Settings.SCHEDULING_MODE, SchedulingMode.ROUNDROBIN, ButtonToolTips.SchedulingMode, ButtonToolTips.SchedulingModeRoundRobin);
            this.registerApp(242, Settings.SCHEDULING_MODE, SchedulingMode.RANDOM, ButtonToolTips.SchedulingMode, ButtonToolTips.SchedulingModeRandom);
            this.registerApp(84, Settings.ACTIONS, (Enum) foxiwhitee.hellmod.api.config.ActionItems.TOGGLE_SHOW_ONLY_INVALID_PATTERN_ON, CustomButtonToolTips.ToggleShowOnlyInvalidInterface, CustomButtonToolTips.ToggleShowOnlyInvalidInterfaceOnDesc);
            this.registerApp(85, Settings.ACTIONS, (Enum) foxiwhitee.hellmod.api.config.ActionItems.TOGGLE_SHOW_ONLY_INVALID_PATTERN_OFF, CustomButtonToolTips.ToggleShowOnlyInvalidInterface, CustomButtonToolTips.ToggleShowOnlyInvalidInterfaceOffDesc);
            this.registerApp(86, Settings.ACTIONS, (Enum) foxiwhitee.hellmod.api.config.ActionItems.MOLECULAR_ASSEMBLEERS_ON, CustomButtonToolTips.ToggleMolecularAssemblers, CustomButtonToolTips.ToggleMolecularAssemblersDescOn);
            this.registerApp(87, Settings.ACTIONS, (Enum) foxiwhitee.hellmod.api.config.ActionItems.TOGGLE_SHOW_FULL_INTERFACES_ON, CustomButtonToolTips.ToggleShowFullInterfaces, CustomButtonToolTips.ToggleShowFullInterfacesOnDesc);
            this.registerApp(88, Settings.ACTIONS, (Enum) foxiwhitee.hellmod.api.config.ActionItems.TOGGLE_SHOW_FULL_INTERFACES_OFF, CustomButtonToolTips.ToggleShowFullInterfaces, CustomButtonToolTips.ToggleShowFullInterfacesOffDesc);
            this.registerApp(89, Settings.ACTIONS, (Enum) foxiwhitee.hellmod.api.config.ActionItems.MOLECULAR_ASSEMBLEERS_OFF, CustomButtonToolTips.ToggleMolecularAssemblers, CustomButtonToolTips.ToggleMolecularAssemblersDescOff);
            this.registerApp(102, Settings.ACTIONS, (Enum) foxiwhitee.hellmod.api.config.ActionItems.HIGHLIGHT_INTERFACE, CustomButtonToolTips.HighlightInterface, "");
        }

    }

    private void registerApp(int iconIndex, Settings setting, Enum val, ButtonToolTips title, Object hint) {
        ButtonAppearance a = new ButtonAppearance();
        a.displayName = title.getUnlocalized();
        a.displayValue = (String)(hint instanceof String ? hint : ((ButtonToolTips)hint).getUnlocalized());
        a.index = iconIndex;
        appearances.put(new EnumPair(setting, val), a);
    }
    private void registerApp(int iconIndex, Settings setting, Enum val, CustomButtonToolTips title, Object hint) {
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

                par1Minecraft.renderEngine.bindTexture(ExtraBlockTextures.GuiTexture(getStates()));
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

                par1Minecraft.renderEngine.bindTexture(ExtraBlockTextures.GuiTexture(getStates()));
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

    public String getStates() { return states; }

    public int getIconIndex() {
        if (this.buttonSetting != null && this.currentValue != null) {
            ButtonAppearance app = (ButtonAppearance)appearances.get(new EnumPair(this.buttonSetting, this.currentValue));
            return app == null ? 255 : app.index;
        } else {
            return 255;
        }
    }

    public Settings getSetting() {
        return (Settings)this.buttonSetting;
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
