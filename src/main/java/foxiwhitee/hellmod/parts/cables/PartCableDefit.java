package foxiwhitee.hellmod.parts.cables;

import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import foxiwhitee.hellmod.ModItems;
import foxiwhitee.hellmod.client.render.CableBusTextures;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.parts.EnumParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class PartCableDefit extends PartBaseSmartCable {
    public PartCableDefit(ItemStack is) {
        super(is);
    }

    @Override
    public IIcon getTexture(AEColor c) {
        switch (c) {
            case Red: return CableBusTextures.CableDefit_Red.getIcon();
            case Blue: return CableBusTextures.CableDefit_Blue.getIcon();
            case Cyan: return CableBusTextures.CableDefit_Cyan.getIcon();
            case Gray: return CableBusTextures.CableDefit_Grey.getIcon();
            case Lime: return CableBusTextures.CableDefit_Lime.getIcon();
            case Pink: return CableBusTextures.CableDefit_Pink.getIcon();
            case Black: return CableBusTextures.CableDefit_Black.getIcon();
            case Brown: return CableBusTextures.CableDefit_Brown.getIcon();
            case Green: return CableBusTextures.CableDefit_Green.getIcon();
            case White: return CableBusTextures.CableDefit_White.getIcon();
            case Orange: return CableBusTextures.CableDefit_Orange.getIcon();
            case Purple: return CableBusTextures.CableDefit_Purple.getIcon();
            case Yellow: return CableBusTextures.CableDefit_Yellow.getIcon();
            case Magenta: return CableBusTextures.CableDefit_Magenta.getIcon();
            case LightBlue: return CableBusTextures.CableDefit_LightBlue.getIcon();
            case LightGray: return CableBusTextures.CableDefit_LightGrey.getIcon();
            default: return CableBusTextures.CableDefit.getIcon();
        }
    }

    @Override
    public ItemStack getPartItemStack(AEColor paramAEColor) {
        return ModItems.ITEM_PARTS.createPart(EnumParts.DEFIT_SMART_CABLE, paramAEColor).stack(1);
    }

    @Override
    public int getMaxChannelSize() {
        return HellConfig.cableDefitMaxChannelSize;
    }

    @MENetworkEventSubscribe
    public void channelUpdated(MENetworkChannelsChanged c) {
        getHost().markForUpdate();
    }

    @MENetworkEventSubscribe
    public void powerRender(MENetworkPowerStatusChange c) {
        getHost().markForUpdate();
    }

}
