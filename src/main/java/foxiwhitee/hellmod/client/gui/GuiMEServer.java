package foxiwhitee.hellmod.client.gui;

import foxiwhitee.hellmod.container.ContainerMEServer;
import net.minecraft.inventory.Container;

public class GuiMEServer extends HellBaseGui{
    public GuiMEServer(ContainerMEServer container) {
        super(container, 316, 274);
    }

    @Override
    protected String getBackground() {
        return "gui/gui_me_server.png";
    }
}
