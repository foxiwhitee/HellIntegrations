package foxiwhitee.hellmod.parts;

import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.parts.reporting.AbstractPartDisplay;
import appeng.util.Platform;
import foxiwhitee.hellmod.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class PartAdvancedInterfaceTerminal extends AbstractPartDisplay {
    public PartAdvancedInterfaceTerminal(ItemStack is) {
        super(is);
    }

    public boolean onPartActivate(EntityPlayer player, Vec3 pos) {
        if (!super.onPartActivate(player, pos) && !player.isSneaking()) {
            if (Platform.isClient()) {
                return true;
            } else {
                Platform.openGUI(player, this.getHost().getTile(), this.getSide(), getGui());
                return true;
            }
        } else {
            return false;
        }
    }

    public CableBusTextures getFrontBright() { return CommonProxy.getBusTextureBright(0); }
    public CableBusTextures getFrontColored() { return CommonProxy.getBusTextureColored(0); }
    public CableBusTextures getFrontDark() { return CommonProxy.getBusTextureDark(0); }

    public GuiBridge getGui() { return CommonProxy.getGuiBridge(0); }
}
