package foxiwhitee.hellmod.client.gui;

import appeng.client.gui.AEBaseGui;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.container.ContainerCobblestoneDuper;
import foxiwhitee.hellmod.container.ContainerFluidReceiver;
import net.minecraft.client.gui.GuiButton;

public class GuiFluidReceiver extends AEBaseGui {
    public GuiFluidReceiver(ContainerFluidReceiver container) {
        super(container);
        this.ySize = 199;
        this.xSize = 210;
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        super.actionPerformed(par1GuiButton);
    }

    public void initGui() {
        super.initGui();
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {}

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.bindTexture(HellCore.MODID, this.getBackground());
        drawTexturedModalRect(offsetX, offsetY, 23, 28, this.xSize, this.ySize);
    }

    protected String getBackground() {
        return "gui/gui_fluid_receiver.png";
    }

}
