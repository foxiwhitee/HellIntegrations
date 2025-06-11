package foxiwhitee.hellmod.client.gui.interfaces;

import appeng.api.config.Settings;
import appeng.api.config.YesNo;
import appeng.client.gui.implementations.GuiUpgradeable;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.client.gui.widgets.GuiToggleButton;
import appeng.core.localization.GuiText;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketConfigButton;
import appeng.core.sync.packets.PacketSwitchGuis;
import appeng.helpers.IInterfaceHost;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.container.interfaces.ContainerAdvancedInterface;
import foxiwhitee.hellmod.container.interfaces.ContainerCustomInterface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.input.Mouse;


public class GuiAdvancedInterface extends GuiCustomInterface {
    public GuiAdvancedInterface(final InventoryPlayer inventoryPlayer, final IInterfaceHost te) {
        super(new ContainerAdvancedInterface(inventoryPlayer, te));
        this.ySize = 229;
    }

    @Override
    protected String getBackground() {
        return "gui/advanced_interface.png";
    }

}
