package foxiwhitee.hellmod.localization;

import appeng.core.AELog;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;

public enum CustomGuiColors {
    SearchboxFocused(1845493760),
    SearchboxUnfocused(0),
    ItemSlotOverlayUnpowered(1712394513),
    ItemSlotOverlayInvalid(1728013926),
    CraftConfirmMissingItem(452919296),
    CraftingCPUActive(1514512417),
    CraftingCPUInactive(1526724522),
    InterfaceTerminalMatch(704708352),
    SearchboxText(16777215),
    CraftingCPUTitle(4210752),
    CraftingCPUStored(4210752),
    CraftingCPUAmount(4210752),
    CraftingCPUScheduled(4210752),
    CraftingStatusCPUName(2105376),
    CraftingStatusCPUStorage(2105376),
    CraftingStatusCPUAmount(2105376),
    CraftAmountToCraft(16777215),
    CraftAmountSelectAmount(4210752),
    LevelEmitterValue(16777215),
    PriorityTitle(4210752),
    PriorityValue(16777215),
    ChestTitle(4210752),
    ChestInventory(4210752),
    CondenserTitle(4210752),
    CondenserInventory(4210752),
    CraftConfirmCraftingPlan(4210752),
    CraftConfirmSimulation(4210752),
    CraftConfirmFromStorage(4210752),
    CraftConfirmMissing(4210752),
    CraftConfirmToCraft(4210752),
    CraftingTerminalTitle(4210752),
    DriveTitle(4210752),
    DriveInventory(4210752),
    FormationPlaneTitle(4210752),
    FormationPlaneInventory(4210752),
    GrindStoneTitle(4210752),
    GrindStoneInventory(4210752),
    InscriberTitle(4210752),
    InscriberInventory(4210752),
    InterfaceTitle(4210752),
    InterfaceTerminalTitle(4210752),
    InterfaceTerminalInventory(4210752),
    InterfaceTerminalName(4210752),
    IOPortTitle(4210752),
    IOPortInventory(4210752),
    NetworkStatusDetails(4210752),
    NetworkStatusStoredPower(4210752),
    NetworkStatusMaxPower(4210752),
    NetworkStatusPowerInputRate(4210752),
    NetworkStatusPowerUsageRate(4210752),
    NetworkStatusItemCount(4210752),
    NetworkToolTitle(4210752),
    NetworkToolInventory(4210752),
    OreFilterLabel(4210752),
    PatternTerminalTitle(4210752),
    PatternTerminalEx(4210752),
    QuantumLinkChamberTitle(4210752),
    QuantumLinkChamberInventory(4210752),
    QuartzCuttingKnifeTitle(4210752),
    QuartzCuttingKnifeInventory(4210752),
    RenamerTitle(4210752),
    SecurityCardEditorTitle(4210752),
    SkyChestTitle(4210752),
    SkyChestInventory(4210752),
    SpatialIOTitle(4210752),
    SpatialIOInventory(4210752),
    SpatialIOStoredPower(4210752),
    SpatialIOMaxPower(4210752),
    SpatialIORequiredPower(4210752),
    SpatialIOEfficiency(4210752),
    StorageBusTitle(4210752),
    StorageBusInventory(4210752),
    UpgradableTitle(4210752),
    UpgradableInventory(4210752),
    VibrationChamberTitle(4210752),
    VibrationChamberInventory(4210752),
    WirelessTitle(4210752),
    WirelessInventory(4210752),
    WirelessRange(4210752),
    WirelessPowerUsageRate(4210752),
    NEIGrindstoneRecipeChance(0),
    NEIGrindstoneNoSecondOutput(0),
    MEMonitorableTitle(4210752),
    MEMonitorableInventory(4210752);

    private final String root;

    private final int color;

    CustomGuiColors() {
        this.root = "gui.color.appliedenergistics2";
        this.color = 0;
    }

    CustomGuiColors(int hex) {
        this.root = "gui.color.appliedenergistics2";
        this.color = hex;
    }

    public int getColor() {
        String hex = LocalizationUtils.localize(getUnlocalized());
        int color = this.color;
        if (hex.length() <= 8)
            try {
                color = Integer.parseUnsignedInt(hex, 16);
            } catch (NumberFormatException e) {
                AELog.warn("Couldn't format color correctly for: " + this.root + " -> " + hex, new Object[0]);
            }
        return color;
    }

    public String getUnlocalized() {
        return this.root + '.' + toString();
    }
}