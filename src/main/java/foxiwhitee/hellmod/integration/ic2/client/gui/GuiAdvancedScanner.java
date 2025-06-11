package foxiwhitee.hellmod.integration.ic2.client.gui;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.container.ContainerAdvancedScanner;
import foxiwhitee.hellmod.integration.ic2.tile.TileAdvancedScanner;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;
import ic2.core.util.Util;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedScanner extends GuiContainer {
    public ContainerAdvancedScanner container;
    public String[] info = new String[9];
    private final ResourceLocation background;
    private static final int infoXordX = 61;
    private static final int infoXordY = 122;
    public GuiAdvancedScanner(ContainerAdvancedScanner container1) {
        super(container1);
        this.container = container1;
        this.background = getBackground("advanced_scanner");
        this.xSize = 217;
        this.ySize = 234;
        this.info[0] = LocalizationUtils.localize("tooltip.customScanner.gui.name");
        this.info[1] = LocalizationUtils.localize("tooltip.customScanner.gui.info1");
        this.info[2] = LocalizationUtils.localize("tooltip.customScanner.gui.info2");
        this.info[3] = LocalizationUtils.localize("tooltip.customScanner.gui.info3");
        this.info[4] = LocalizationUtils.localize("tooltip.customScanner.gui.info4");
        this.info[5] = LocalizationUtils.localize("tooltip.customScanner.gui.info5");
        this.info[6] = LocalizationUtils.localize("tooltip.customScanner.gui.info6");
        this.info[7] = LocalizationUtils.localize("tooltip.customScanner.gui.info7");
        this.info[8] = LocalizationUtils.localize("tooltip.customScanner.gui.info8");
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        TileAdvancedScanner te = (TileAdvancedScanner)this.container.base;
        switch (te.getState()) {
            case IDLE:
                this.fontRendererObj.drawString(LocalizationUtils.localize("tooltip.customScanner.gui.idle"), infoXordX, infoXordY, 15461152);
                break;
            case NO_STORAGE:
                this.fontRendererObj.drawString(this.info[2], infoXordX, infoXordY, 15461152);
                break;
            case SCANNING:
                this.fontRendererObj.drawString(this.info[1], infoXordX, infoXordY, 2157374);
                this.fontRendererObj.drawString(te.getPercentageDone() + "%", infoXordX + 70, infoXordY, 2157374);
                break;
            case NO_ENERGY:
                this.fontRendererObj.drawString(this.info[3], infoXordX, infoXordY, 14094352);
                break;
            case ALREADY_RECORDED:
                this.fontRendererObj.drawString(this.info[8], infoXordX, infoXordY, 14094352);
                break;
            case FAILED:
                this.fontRendererObj.drawString(this.info[6], infoXordX, infoXordY, 14094352);
                break;
            case COMPLETED:
            case TRANSFER_ERROR:
                if (te.getState() == TileAdvancedScanner.State.COMPLETED) {
                    this.fontRendererObj.drawString(Util.toSiString(te.patternUu, 4) + "B UUM", infoXordX, infoXordY, 2157374);
                    this.fontRendererObj.drawString(Util.toSiString(te.patternEu, 4) + "EU", infoXordX + 70, infoXordY, 2157374);
                }

                if (te.getState() == TileAdvancedScanner.State.TRANSFER_ERROR) {
                    this.fontRendererObj.drawString(this.info[7], infoXordX, infoXordY, 14094352);
                }


        }

        if (te.getState() == TileAdvancedScanner.State.COMPLETED || te.getState() == TileAdvancedScanner.State.TRANSFER_ERROR || te.getState() == TileAdvancedScanner.State.FAILED) {
            UtilGui.drawAreaTooltip(par1 - this.guiLeft + 29, par2 - this.guiTop - 51, LocalizationUtils.localize("tooltip.customScanner.gui.button.delete"), 102, 49, 102+13, 49+13);
            if (te.getState() != TileAdvancedScanner.State.FAILED) {
                UtilGui.drawAreaTooltip(par1 - this.guiLeft + 13, par2 - this.guiTop - 51, LocalizationUtils.localize("tooltip.customScanner.gui.button.save"), 143, 49, 143+13, 49+13);
            }
        }

    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        TileAdvancedScanner te = (TileAdvancedScanner)this.container.base;

        if (te.isDone()) {
            if (x >= 73 && y >= 100 && x <= 86 && y <= 113) {
                ((NetworkManager) IC2.network.get()).initiateClientTileEntityEvent(te, 0);
            }

            if (x >= 130 && y >= 100 && x <= 143 && y <= 113) {
                ((NetworkManager)IC2.network.get()).initiateClientTileEntityEvent(te, 1);
            }
        }
    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        UtilGui.drawTexture(j, k, 0, 12, this.xSize, this.ySize, this.xSize, this.ySize);
        TileAdvancedScanner te = (TileAdvancedScanner)this.container.base;

        int chargeLevel = (int)(41.0F * te.getChargeLevel());
        int progress = (int)(6.0F * (te.getPercentageDone() / 100.0F));
        if (chargeLevel > 0) {
            UtilGui.drawTexture(j + 15, k + 124 - chargeLevel,  230, 41 - chargeLevel,  3, chargeLevel,3, chargeLevel);
        }

        if (progress > 0) {
            UtilGui.drawTexture(j + 107, k + 79, 226, 0, 4, progress, 4, progress);
        }

    }

    protected ResourceLocation getBackground(String name) {
        return new ResourceLocation(HellCore.MODID, "textures/gui/gui_" + name +  ".png");
    }
}
