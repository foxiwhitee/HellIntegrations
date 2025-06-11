package foxiwhitee.hellmod.localization;

import foxiwhitee.hellmod.utils.localization.LocalizationUtils;

public enum CustomButtonToolTips {
    PastBtnTitle,
    PastBtnDesc,
    NextBtnTitle,
    NextBtnDesc,
    ChangeTypeBtnTitle,
    ChangeTypeBtnDesc,
    SearchFieldInputs,
    SearchFieldOutputs,
    SearchFieldNames,
    ToggleShowOnlyInvalidInterface,
    ToggleShowOnlyInvalidInterfaceOnDesc,
    ToggleShowOnlyInvalidInterfaceOffDesc,
    HighlightInterface,
    HighlightInterfaceDesc,
    ToggleMolecularAssemblers,
    ToggleMolecularAssemblersDescOn,
    ToggleMolecularAssemblersDescOff,
    ToggleShowFullInterfaces,
    ToggleShowFullInterfacesOnDesc,
    ToggleShowFullInterfacesOffDesc;

    private final String root;

    private CustomButtonToolTips() {
        this.root = "gui.tooltips.appliedenergistics2";
    }

    private CustomButtonToolTips(String r) {
        this.root = r;
    }

    public String getLocal() {
        return LocalizationUtils.localize(this.getUnlocalized());
    }

    public String getUnlocalized() {
        return this.root + '.' + this.toString();
    }
}
