package foxiwhitee.hellmod.integration.ic2.client.gui;

import appeng.client.gui.AEBaseGui;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.container.ContainerSynthesizer;
import foxiwhitee.hellmod.utils.helpers.EnergyUtility;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;

public class GuiSynthesizer extends AEBaseGui {
    private static final ResourceLocation texture = new ResourceLocation(HellCore.MODID, "textures/gui/gui_sintezator.png");
    ContainerSynthesizer container;
    public GuiSynthesizer(ContainerSynthesizer container) {
        super(container);
        this.container = container;
        this.xSize = 233;
        this.ySize = 234;
    }

    @Override
    public void drawFG(int i, int i1, int i2, int i3) {

    }

    public void bindTexture(ResourceLocation file) {
        this.mc.getTextureManager().bindTexture(file);
    }

    @Override
    public void drawBG(int i, int i1, int i2, int i3) {
        bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        double energyStored = container.getTile().getEnergy();
        double y = 64 * Math.min(energyStored / container.getTile().getMaxEnergy(), 1.0);
        UtilGui.drawTexture(guiLeft + 50.0, guiTop + 49.0 + 64.0 - y, 244.0, 0.0, 12.0, y, 12.0, y, 256.0, 256.0);
        GL11.glPushMatrix();
        GL11.glTranslated(guiLeft + 177.0, guiTop + 69 + 4.0, 0.0);
        GL11.glScaled(0.75, 0.75, 1.0);
        GL11.glPopMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float btn) {
        super.drawScreen(mouseX, mouseY, btn);
        double storedEnergy = container.getTile().getEnergy();
        if (storedEnergy >= 0.0) {
            double maxStoredEnergy = container.getTile().getMaxEnergy();
            String[] tooltip = {EnergyUtility.formatPower(storedEnergy) + " / " + EnergyUtility.formatPower(maxStoredEnergy) + " EU"};
            drawIfInMouse(mouseX, mouseY, 50, 49, 12, 64, tooltip);
        }
    }

    public final void drawIfInMouse(int mouseX, int mouseY, int x, int y, int w, int h, String... str) {
        if (mouseX >= this.guiLeft + x && mouseX <= this.guiLeft + x + w && mouseY >= this.guiTop + y && mouseY <= this.guiTop + y + h)
            drawHoveringText(new ArrayList<>(Arrays.asList(Arrays.copyOf((Object[]) str, str.length))), mouseX, mouseY, this.mc.fontRenderer);
    }
}
