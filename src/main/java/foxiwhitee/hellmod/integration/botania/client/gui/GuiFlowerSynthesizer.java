package foxiwhitee.hellmod.integration.botania.client.gui;

import appeng.client.gui.AEBaseGui;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.container.ContainerFlowerSynthesizer;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;

public class GuiFlowerSynthesizer extends AEBaseGui {
    ContainerFlowerSynthesizer container;

    public GuiFlowerSynthesizer(ContainerFlowerSynthesizer container) {
        super(container);
        this.container = container;
        this.xSize = 210;
        this.ySize = 230;
    }

    @Override
    public void drawFG(int i, int i1, int i2, int i3) {}

    @Override
    public void drawBG(int i, int i1, int i2, int i3) {
        bindTexture(getTexture());
        UtilGui.drawTexture(guiLeft, guiTop, 21, 4, xSize, ySize, xSize, ySize, 256, 256);

        if (this.container.getTile().getStoredMana() > 0) {
            double l = this.container.getTile().gaugeManaScaled(120);
            String storageString = LocalizationUtils.localize("tooltip.mana.generator.storage") + " ";
            String tooltipStorage = storageString + this.container.getTile().getStoredMana() + "/" + this.container.getTile().getMaxStoredMana();
            UtilGui.drawTexture(guiLeft + 45, guiTop + 131, 0, 250, (int)(l + 1.0D), 6, (int)(l + 1.0D), 6, 256, 256);
        }
    }

    private ResourceLocation getTexture() {
        return new ResourceLocation(HellCore.MODID, "textures/gui/gui_flower_synthesizer.png");
    }

    public void bindTexture(ResourceLocation file) {
        this.mc.getTextureManager().bindTexture(file);
    }

}
