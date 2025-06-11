package foxiwhitee.hellmod.parts.cables;

import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.parts.BusSupport;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import foxiwhitee.hellmod.ModItems;
import foxiwhitee.hellmod.client.render.CableBusTextures;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.parts.EnumParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class PartDenseCableNur extends PartBaseDenseCable {
    public PartDenseCableNur(ItemStack is) {
        super(is);
    }

    @Override
    public IIcon getTexture(AEColor c) {
        switch (c) {
            case Red:
                return CableBusTextures.DenseCableNur_Red.getIcon();
            case Blue:
                return CableBusTextures.DenseCableNur_Blue.getIcon();
            case Cyan:
                return CableBusTextures.DenseCableNur_Cyan.getIcon();
            case Gray:
                return CableBusTextures.DenseCableNur_Grey.getIcon();
            case Lime:
                return CableBusTextures.DenseCableNur_Lime.getIcon();
            case Pink:
                return CableBusTextures.DenseCableNur_Pink.getIcon();
            case Black:
                return CableBusTextures.DenseCableNur_Black.getIcon();
            case Brown:
                return CableBusTextures.DenseCableNur_Brown.getIcon();
            case Green:
                return CableBusTextures.DenseCableNur_Green.getIcon();
            case White:
                return CableBusTextures.DenseCableNur_White.getIcon();
            case Orange:
                return CableBusTextures.DenseCableNur_Orange.getIcon();
            case Purple:
                return CableBusTextures.DenseCableNur_Purple.getIcon();
            case Yellow:
                return CableBusTextures.DenseCableNur_Yellow.getIcon();
            case Magenta:
                return CableBusTextures.DenseCableNur_Magenta.getIcon();
            case LightBlue:
                return CableBusTextures.DenseCableNur_LightBlue.getIcon();
            case LightGray:
                return CableBusTextures.DenseCableNur_LightGrey.getIcon();
            default:
                return CableBusTextures.DenseCableNur.getIcon();
        }
    }

    @Override
    public ItemStack getPartItemStack(AEColor paramAEColor) {
        return ModItems.ITEM_PARTS.createPart(EnumParts.NUR_DENSE_CABLE, paramAEColor).stack(1);
    }

    @Override
    public int getMaxChannelSize() {
        return HellConfig.cableNurMaxChannelSize;
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
