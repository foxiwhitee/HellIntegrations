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

public class PartDenseCableXaur extends PartBaseDenseCable {
    public PartDenseCableXaur(ItemStack is) {
        super(is);
    }

    @Override
    public IIcon getTexture(AEColor c) {
        switch (c) {
            case Red: return CableBusTextures.DenseCableXaur_Red.getIcon();
            case Blue: return CableBusTextures.DenseCableXaur_Blue.getIcon();
            case Cyan: return CableBusTextures.DenseCableXaur_Cyan.getIcon();
            case Gray: return CableBusTextures.DenseCableXaur_Grey.getIcon();
            case Lime: return CableBusTextures.DenseCableXaur_Lime.getIcon();
            case Pink: return CableBusTextures.DenseCableXaur_Pink.getIcon();
            case Black: return CableBusTextures.DenseCableXaur_Black.getIcon();
            case Brown: return CableBusTextures.DenseCableXaur_Brown.getIcon();
            case Green: return CableBusTextures.DenseCableXaur_Green.getIcon();
            case White: return CableBusTextures.DenseCableXaur_White.getIcon();
            case Orange: return CableBusTextures.DenseCableXaur_Orange.getIcon();
            case Purple: return CableBusTextures.DenseCableXaur_Purple.getIcon();
            case Yellow: return CableBusTextures.DenseCableXaur_Yellow.getIcon();
            case Magenta: return CableBusTextures.DenseCableXaur_Magenta.getIcon();
            case LightBlue: return CableBusTextures.DenseCableXaur_LightBlue.getIcon();
            case LightGray: return CableBusTextures.DenseCableXaur_LightGrey.getIcon();
            default: return CableBusTextures.DenseCableXaur.getIcon();
        }
    }

    @Override
    public ItemStack getPartItemStack(AEColor paramAEColor) {
        return ModItems.ITEM_PARTS.createPart(EnumParts.XAUR_DENSE_CABLE, paramAEColor).stack(1);
    }

    @Override
    public int getMaxChannelSize() {
        return HellConfig.cableXaurMaxChannelSize;
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
