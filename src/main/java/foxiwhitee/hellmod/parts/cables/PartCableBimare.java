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

public class PartCableBimare extends PartBaseSmartCable {
    public PartCableBimare(ItemStack is) {
        super(is);
    }

    @Override
    public IIcon getTexture(AEColor c) {
        switch (c) {
            case Red: return CableBusTextures.CableBimare_Red.getIcon();
            case Blue: return CableBusTextures.CableBimare_Blue.getIcon();
            case Cyan: return CableBusTextures.CableBimare_Cyan.getIcon();
            case Gray: return CableBusTextures.CableBimare_Grey.getIcon();
            case Lime: return CableBusTextures.CableBimare_Lime.getIcon();
            case Pink: return CableBusTextures.CableBimare_Pink.getIcon();
            case Black: return CableBusTextures.CableBimare_Black.getIcon();
            case Brown: return CableBusTextures.CableBimare_Brown.getIcon();
            case Green: return CableBusTextures.CableBimare_Green.getIcon();
            case White: return CableBusTextures.CableBimare_White.getIcon();
            case Orange: return CableBusTextures.CableBimare_Orange.getIcon();
            case Purple: return CableBusTextures.CableBimare_Purple.getIcon();
            case Yellow: return CableBusTextures.CableBimare_Yellow.getIcon();
            case Magenta: return CableBusTextures.CableBimare_Magenta.getIcon();
            case LightBlue: return CableBusTextures.CableBimare_LightBlue.getIcon();
            case LightGray: return CableBusTextures.CableBimare_LightGrey.getIcon();
            default: return CableBusTextures.CableBimare.getIcon();
        }
    }

    @Override
    public ItemStack getPartItemStack(AEColor paramAEColor) {
        return ModItems.ITEM_PARTS.createPart(EnumParts.BIMARE_SMART_CABLE, paramAEColor).stack(1);
    }

    @Override
    public int getMaxChannelSize() {
        return HellConfig.cableBimareMaxChannelSize;
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
