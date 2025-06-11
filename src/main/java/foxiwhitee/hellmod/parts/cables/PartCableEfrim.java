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

public class PartCableEfrim extends PartBaseSmartCable {
    public PartCableEfrim(ItemStack is) {
        super(is);
    }

    @Override
    public IIcon getTexture(AEColor c) {
        switch (c) {
            case Red: return CableBusTextures.CableEfrim_Red.getIcon();
            case Blue: return CableBusTextures.CableEfrim_Blue.getIcon();
            case Cyan: return CableBusTextures.CableEfrim_Cyan.getIcon();
            case Gray: return CableBusTextures.CableEfrim_Grey.getIcon();
            case Lime: return CableBusTextures.CableEfrim_Lime.getIcon();
            case Pink: return CableBusTextures.CableEfrim_Pink.getIcon();
            case Black: return CableBusTextures.CableEfrim_Black.getIcon();
            case Brown: return CableBusTextures.CableEfrim_Brown.getIcon();
            case Green: return CableBusTextures.CableEfrim_Green.getIcon();
            case White: return CableBusTextures.CableEfrim_White.getIcon();
            case Orange: return CableBusTextures.CableEfrim_Orange.getIcon();
            case Purple: return CableBusTextures.CableEfrim_Purple.getIcon();
            case Yellow: return CableBusTextures.CableEfrim_Yellow.getIcon();
            case Magenta: return CableBusTextures.CableEfrim_Magenta.getIcon();
            case LightBlue: return CableBusTextures.CableEfrim_LightBlue.getIcon();
            case LightGray: return CableBusTextures.CableEfrim_LightGrey.getIcon();
            default: return CableBusTextures.CableEfrim.getIcon();
        }
    }

    @Override
    public ItemStack getPartItemStack(AEColor paramAEColor) {
        return ModItems.ITEM_PARTS.createPart(EnumParts.EFRIM_SMART_CABLE, paramAEColor).stack(1);
    }

    @Override
    public int getMaxChannelSize() {
        return HellConfig.cableEfrimMaxChannelSize;
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
