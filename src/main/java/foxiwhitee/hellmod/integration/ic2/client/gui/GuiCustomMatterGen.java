package foxiwhitee.hellmod.integration.ic2.client.gui;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.container.ContainerCustomMatterGen;
import foxiwhitee.hellmod.integration.ic2.tile.matter.TileCustomMatterGen;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import ic2.core.ContainerBase;
import ic2.core.GuiIC2;
import ic2.core.util.DrawUtil;
import ic2.core.util.GuiTooltipHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class GuiCustomMatterGen extends GuiIC2 {
    public ContainerCustomMatterGen container;
    private TileCustomMatterGen tile;

    public String progressLabel;

    public GuiCustomMatterGen(ContainerCustomMatterGen container1) {
        super((ContainerBase)container1);
        this.container = container1;
        this.tile = container.getTile();
        this.xSize = 213;
        this.ySize = 233;
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRendererObj.drawString(((TileCustomMatterGen)this.container.base).getProgressAsString(), 106-11, 87+35, 12566463);
        FluidStack fluidstack = ((TileCustomMatterGen)this.container.base).getFluidStackfromTank();
        if (fluidstack != null) {
            String tooltip = fluidstack.getFluid().getName() + ": " + fluidstack.amount + LocalizationUtils.localize("ic2.generic.text.mb");
            GuiTooltipHelper.drawAreaTooltip(par1 - this.guiLeft - 30, par2 - this.guiTop, tooltip, 99, 25+40, 112, 73+40);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.getResourceLocation());
        this.xoffset = (this.width - this.xSize) / 2;
        this.yoffset = (this.height - this.ySize) / 2;
        UtilGui.drawTexture(this.xoffset, this.yoffset, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
        if (((TileCustomMatterGen)this.container.base).getTankAmount() > 0) {
            IIcon fluidIcon = ((TileCustomMatterGen)this.container.base).getFluidTank().getFluid().getFluid().getIcon();
            if (fluidIcon != null) {
                UtilGui.drawTexture(this.xoffset + 129, this.yoffset + 66, 214, 0, 20, 55, 20, 55);
                this.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                int liquidHeight = ((TileCustomMatterGen)this.container.base).gaugeLiquidScaled(47);
                DrawUtil.drawRepeated(fluidIcon, (this.xoffset + 130), (this.yoffset + 66 + 47 - liquidHeight), 12.0D, liquidHeight, this.zLevel);
                this.mc.renderEngine.bindTexture(getResourceLocation());
                UtilGui.drawTexture(this.xoffset + 130, this.yoffset + 65, 214, 48, 12, 47, 12, 47);
            }
        }
    }

    public String getName() {
        return ((TileCustomMatterGen)this.container.base).getInventoryName();
    }

    public ResourceLocation getResourceLocation() {
        String textureName = "gui_advanced_matter";
        switch (tile.getName()) {
            case "nano_matter": textureName = "gui_nano_matter"; break;
            case "quantum_matter": textureName = "gui_quantum_matter"; break;
        }
        return new ResourceLocation(HellCore.MODID, "textures/gui/" + textureName +  ".png");
    }

    
}
