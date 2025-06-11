package foxiwhitee.hellmod.client.render;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

public enum CableBusTextures {
    CableAlite("CableAlite"),
    CableAlite_Green("CableAlite_Green"),
    CableAlite_Grey("CableAlite_Grey"),
    CableAlite_LightBlue("CableAlite_LightBlue"),
    CableAlite_LightGrey("CableAlite_LightGrey"),
    CableAlite_Lime("CableAlite_Lime"),
    CableAlite_Magenta("CableAlite_Magenta"),
    CableAlite_Orange("CableAlite_Orange"),
    CableAlite_Pink("CableAlite_Pink"),
    CableAlite_Purple("CableAlite_Purple"),
    CableAlite_Red("CableAlite_Red"),
    CableAlite_White("CableAlite_White"),
    CableAlite_Yellow("CableAlite_Yellow"),
    CableAlite_Black("CableAlite_Black"),
    CableAlite_Blue("CableAlite_Blue"),
    CableAlite_Brown("CableAlite_Brown"),
    CableAlite_Cyan("CableAlite_Cyan"),
    CableBimare("CableBimare"),
    CableBimare_Green("CableBimare_Green"),
    CableBimare_Grey("CableBimare_Grey"),
    CableBimare_LightBlue("CableBimare_LightBlue"),
    CableBimare_LightGrey("CableBimare_LightGrey"),
    CableBimare_Lime("CableBimare_Lime"),
    CableBimare_Magenta("CableBimare_Magenta"),
    CableBimare_Orange("CableBimare_Orange"),
    CableBimare_Pink("CableBimare_Pink"),
    CableBimare_Purple("CableBimare_Purple"),
    CableBimare_Red("CableBimare_Red"),
    CableBimare_White("CableBimare_White"),
    CableBimare_Yellow("CableBimare_Yellow"),
    CableBimare_Black("CableBimare_Black"),
    CableBimare_Blue("CableBimare_Blue"),
    CableBimare_Brown("CableBimare_Brown"),
    CableBimare_Cyan("CableBimare_Cyan"),
    CableDefit("CableDefit"),
    CableDefit_Green("CableDefit_Green"),
    CableDefit_Grey("CableDefit_Grey"),
    CableDefit_LightBlue("CableDefit_LightBlue"),
    CableDefit_LightGrey("CableDefit_LightGrey"),
    CableDefit_Lime("CableDefit_Lime"),
    CableDefit_Magenta("CableDefit_Magenta"),
    CableDefit_Orange("CableDefit_Orange"),
    CableDefit_Pink("CableDefit_Pink"),
    CableDefit_Purple("CableDefit_Purple"),
    CableDefit_Red("CableDefit_Red"),
    CableDefit_White("CableDefit_White"),
    CableDefit_Yellow("CableDefit_Yellow"),
    CableDefit_Black("CableDefit_Black"),
    CableDefit_Blue("CableDefit_Blue"),
    CableDefit_Brown("CableDefit_Brown"),
    CableDefit_Cyan("CableDefit_Cyan"),
    CableEfrim("CableEfrim"),
    CableEfrim_Green("CableEfrim_Green"),
    CableEfrim_Grey("CableEfrim_Grey"),
    CableEfrim_LightBlue("CableEfrim_LightBlue"),
    CableEfrim_LightGrey("CableEfrim_LightGrey"),
    CableEfrim_Lime("CableEfrim_Lime"),
    CableEfrim_Magenta("CableEfrim_Magenta"),
    CableEfrim_Orange("CableEfrim_Orange"),
    CableEfrim_Pink("CableEfrim_Pink"),
    CableEfrim_Purple("CableEfrim_Purple"),
    CableEfrim_Red("CableEfrim_Red"),
    CableEfrim_White("CableEfrim_White"),
    CableEfrim_Yellow("CableEfrim_Yellow"),
    CableEfrim_Black("CableEfrim_Black"),
    CableEfrim_Blue("CableEfrim_Blue"),
    CableEfrim_Brown("CableEfrim_Brown"),
    CableEfrim_Cyan("CableEfrim_Cyan"),
    DenseCableNur("DenseCableNur"),
    DenseCableNur_Green("DenseCableNur_Green"),
    DenseCableNur_Grey("DenseCableNur_Grey"),
    DenseCableNur_LightBlue("DenseCableNur_LightBlue"),
    DenseCableNur_LightGrey("DenseCableNur_LightGrey"),
    DenseCableNur_Lime("DenseCableNur_Lime"),
    DenseCableNur_Magenta("DenseCableNur_Magenta"),
    DenseCableNur_Orange("DenseCableNur_Orange"),
    DenseCableNur_Pink("DenseCableNur_Pink"),
    DenseCableNur_Purple("DenseCableNur_Purple"),
    DenseCableNur_Red("DenseCableNur_Red"),
    DenseCableNur_White("DenseCableNur_White"),
    DenseCableNur_Yellow("DenseCableNur_Yellow"),
    DenseCableNur_Black("DenseCableNur_Black"),
    DenseCableNur_Blue("DenseCableNur_Blue"),
    DenseCableNur_Brown("DenseCableNur_Brown"),
    DenseCableNur_Cyan("DenseCableNur_Cyan"),
    DenseCableXaur("DenseCableXaur"),
    DenseCableXaur_Green("DenseCableXaur_Green"),
    DenseCableXaur_Grey("DenseCableXaur_Grey"),
    DenseCableXaur_LightBlue("DenseCableXaur_LightBlue"),
    DenseCableXaur_LightGrey("DenseCableXaur_LightGrey"),
    DenseCableXaur_Lime("DenseCableXaur_Lime"),
    DenseCableXaur_Magenta("DenseCableXaur_Magenta"),
    DenseCableXaur_Orange("DenseCableXaur_Orange"),
    DenseCableXaur_Pink("DenseCableXaur_Pink"),
    DenseCableXaur_Purple("DenseCableXaur_Purple"),
    DenseCableXaur_Red("DenseCableXaur_Red"),
    DenseCableXaur_White("DenseCableXaur_White"),
    DenseCableXaur_Yellow("DenseCableXaur_Yellow"),
    DenseCableXaur_Black("DenseCableXaur_Black"),
    DenseCableXaur_Blue("DenseCableXaur_Blue"),
    DenseCableXaur_Brown("DenseCableXaur_Brown"),
    DenseCableXaur_Cyan("DenseCableXaur_Cyan");

    private final String name;

    public net.minecraft.util.IIcon IIcon;

    CableBusTextures(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public IIcon getIcon() {
        return this.IIcon;
    }

    public void registerIcon(TextureMap map) {
        this.IIcon = map.registerIcon(HellCore.MODID + ":ae2/cables/" + this.name);
    }
}
