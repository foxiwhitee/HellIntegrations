package foxiwhitee.hellmod.integration.draconic.client.gui;

import cpw.mods.fml.common.Loader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.*;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.modularelements.*;
import foxiwhitee.hellmod.integration.draconic.container.ContainerFusionCraftingCore;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingCharger;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingInventory;
import foxiwhitee.hellmod.integration.draconic.helpers.gui.GuiHelper;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.RenderEnergyBolt;
import foxiwhitee.hellmod.integration.draconic.recipes.FusionRecipe;
import foxiwhitee.hellmod.integration.draconic.tile.TileFusionCraftingCore;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketFusionCore;
import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiFusionCraftingCore extends ModularGuiContainer<ContainerFusionCraftingCore> {
    private final EntityPlayer player;

    private final TileFusionCraftingCore tile;

    private final GuiEffectRenderer guiEffectRenderer = new GuiEffectRenderer();

    private final Random rand = new Random();

    private final int[] boltStats = new int[] { 0, 0, 0, 0, 0, 0 };

    private FusionRecipe currentRecipe = null;

    private FusionRecipe lastRecipe = null;

    private String canCraft = "";

    private GuiButton startCrafting;

    public GuiFusionCraftingCore(ContainerFusionCraftingCore container) {
        super(container);
        this.player = container.getPlayer();
        this.tile = container.getTile();
        this.xSize = 180;
        this.ySize = 200;
    }

    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(this.startCrafting = new StartButton(0, this.width / 2 - 40, this.guiTop + 93, 80, 14, LocalizationUtils.localize("tooltip.gui.button.start")));
        this.startCrafting.visible = false;
        initRecipeComponents();
    }

    public void initRecipeComponents() {
        this.manager.removeByGroup("RECIPE_ELEMENTS");
        if (this.currentRecipe == null)
            return;
        List ingredients = this.currentRecipe.getInputs();
        int nColumns = (ingredients.size() > 16) ? 4 : 2;
        LinkedList<MGuiList> iColumns = new LinkedList<>();
        int i;
        for (i = 0; i < nColumns; i++) {
            int x = (nColumns == 2) ? (15 + i * 130) : (6 + i % 2 * 19 + i / 2 * 129);
            MGuiList list = (new MGuiList((IModularGui)this, guiLeft() + x, guiTop() + 8, 20, 98)).setScrollingEnabled(false);
            list.addChild((MGuiElementBase)(new MGuiBorderedRect((IModularGui)this, list.xPos, list.yPos - 1, list.xSize, list.ySize + 2)).setBorderColour(-5635841).setFillColour(0));
            list.topPadding = list.bottomPadding = 0;
            iColumns.add((MGuiList)addElement((MGuiElementBase)list));
        }
        i = 0;
        for (Object ingredient : ingredients) {
            ItemStack ingredStack = OreDictUtil.resolveObject(ingredient);
            MGuiList column = iColumns.get((iColumns.size() == 4) ? (i % 4) : (i % 2));
            column.addEntry((MGuiListEntry)new MGuiListEntryWrapper((IModularGui)this, (MGuiElementBase)(new MGuiStackIcon((IModularGui)this, 0, 5, 16, 16, ingredStack)).setDrawHoverHighlight(true)));
            column.sortEvenSpacing(true);
            i++;
        }
        this.manager.initElements();
    }

    private MGuiElementBase addElement(MGuiElementBase elementBase) {
        return this.manager.add(elementBase.addToGroup("RECIPE_ELEMENTS"));
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GuiHelper.drawGuiBaseBackground((Gui)this, this.guiLeft, this.guiTop, this.xSize, this.ySize);
        GuiHelper.drawColouredRect(this.guiLeft + 3, this.guiTop + 3, this.xSize - 6, 110, -16711681);
        GuiHelper.drawColouredRect(this.guiLeft + 4, this.guiTop + 4, this.xSize - 8, 108, -16777216);
        if (this.currentRecipe != null && this.canCraft != null && this.canCraft.equals("true")) {
            GuiHelper.drawColouredRect(this.guiLeft + this.xSize / 2 - 10, this.guiTop + 24, 20, 64, -16711681);
            GuiHelper.drawColouredRect(this.guiLeft + this.xSize / 2 - 9, this.guiTop + 25, 18, 62, -16777216);
        }
        drawCenteredString(this.fontRendererObj, LocalizationUtils.localize("tooltip.gui.fusionCraftingCore.name"), this.guiLeft + this.xSize / 2, this.guiTop + 5, 65535);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.bindTexture("textures/gui/fusion_gui.png");
        GuiHelper.drawPlayerSlots((Gui)this, this.guiLeft + this.xSize / 2, this.guiTop + 115, true);
        if (this.currentRecipe == null || this.canCraft == null || !this.canCraft.equals("true")) {
            drawTexturedModalRect(this.guiLeft + this.xSize / 2 - 9, this.guiTop + 25, 138, 0, 18, 18);
            if (this.tile.getStackInSlot(1) != null)
                drawTexturedModalRect(this.guiLeft + this.xSize / 2 - 9, this.guiTop + 69, 138, 0, 18, 18);
        }
        if (this.currentRecipe != null) {
            GuiHelper.drawStack2D(this.currentRecipe.getOut(), this.mc, this.guiLeft + this.xSize / 2 - 8, this.guiTop + 70, 16.0F);
            if (this.tile.isCrafting() && this.tile.getCraftingStage() > 0) {
                GL11.glDisable(3008);
                double charge = this.tile.getCraftingStage() / 1000.0D;
                if (charge > 1.0D)
                    charge = 1.0D;
                int size = (int)((1.0D - charge) * 98.0D);
                RenderEnergyBolt.renderBoltBetween(Vec3.createVectorHelper((this.guiLeft + 16 + this.boltStats[0]), (this.guiTop + 106), 0.0D), Vec3.createVectorHelper((this.guiLeft + 16 + this.boltStats[1]), (this.guiTop + 8 + size), 0.0D), 1.0D, charge * 10.0D, 10, this.boltStats[2], true);
                RenderEnergyBolt.renderBoltBetween(Vec3.createVectorHelper((this.guiLeft + 16 + this.boltStats[3]), (this.guiTop + 106), 0.0D), Vec3.createVectorHelper((this.guiLeft + 16 + this.boltStats[4]), (this.guiTop + 8 + size), 0.0D), 1.0D, charge * 10.0D, 10, this.boltStats[5], true);
                RenderEnergyBolt.renderBoltBetween(Vec3.createVectorHelper((this.guiLeft + this.xSize - 34 + this.boltStats[0]), (this.guiTop + 106), 0.0D), Vec3.createVectorHelper((this.guiLeft + this.xSize - 34 + this.boltStats[1]), (this.guiTop + 8 + size), 0.0D), 1.0D, charge * 10.0D, 10, this.boltStats[2], true);
                RenderEnergyBolt.renderBoltBetween(Vec3.createVectorHelper((this.guiLeft + this.xSize - 34 + this.boltStats[3]), (this.guiTop + 106), 0.0D), Vec3.createVectorHelper((this.guiLeft + this.xSize - 34 + this.boltStats[4]), (this.guiTop + 8 + size), 0.0D), 1.0D, charge * 10.0D, 10, this.boltStats[5], true);
                GL11.glEnable(3008);
            }
            if (this.tile.isCrafting() && this.tile.getCraftingStage() >= 0) {
                int state = this.tile.getCraftingStage();
                String status = (state > 1000) ? LocalizationUtils.localize("tooltip.info.fusionCrafting.status.crafting") : LocalizationUtils.localize("tooltip.info.fusionCrafting.status.charging");
                double d = (state > 1000) ? ((state - 1000.0F) / 1000.0D) : (state / 1000.0D);
                String progress = (int)(d * 100.0D) + "%";
                if (state < 1000 && isShiftKeyDown()) {
                    long totalCharge = 0L;
                    for (IFusionCraftingCharger pedestal : this.tile.getChargers())
                        totalCharge += pedestal.getInjectorCharge();
                    long averageCharge = totalCharge / this.currentRecipe.getInputs().size();
                    double percentage = averageCharge / this.currentRecipe.getIngredientEnergyCost();
                    progress = ((int)(percentage * 100000.0D) / 1000.0D) + "%";
                }
                drawCenteredString(this.mc.fontRenderer, status + ": " + EnumChatFormatting.GOLD + progress, this.width / 2, this.guiTop + 95, (state < 1000) ? 65280 : 65535);
            }
        }
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    public void updateScreen() {
        super.updateScreen();
        boolean hasNoRecipe = (this.currentRecipe == null);
        this.currentRecipe = DraconicEvolutionIntegration.findRecipeFusion((IFusionCraftingInventory)this.tile, this.player.worldObj, this.tile.xCoord, this.tile.yCoord, this.tile.zCoord);
        if (this.currentRecipe != null) {
            this.canCraft = this.currentRecipe.canCraft((IFusionCraftingInventory)this.tile, this.player.worldObj, this.tile.xCoord, this.tile.yCoord, this.tile.zCoord);
            if (hasNoRecipe || this.currentRecipe != this.lastRecipe) {
                this.lastRecipe = this.currentRecipe;
                initRecipeComponents();
            }
        } else if (!hasNoRecipe) {
            this.manager.removeByGroup("RECIPE_ELEMENTS");
        }
        this.startCrafting.enabled = this.startCrafting.visible = (this.currentRecipe != null && this.canCraft != null && this.canCraft.equals("true") && !this.tile.craftingInProgress());
        if (this.currentRecipe != null && this.canCraft != null && this.canCraft.equals("true")) {
            int centerX = this.guiLeft + this.xSize / 2;
            int centerY = this.guiTop + this.ySize / 2 - 45;
            for (MGuiElementBase element : this.manager.getElements()) {
                if (element instanceof MGuiList)
                    for (MGuiElementBase item : ((MGuiList)element).listEntries) {
                        if (this.rand.nextInt(10) == 0) {
                            double xPos = item.xPos + this.rand.nextDouble() * 16.0D;
                            double yPos = item.yPos + this.rand.nextDouble() * 16.0D;
                            double ty = centerY + -20.0D + this.rand.nextDouble() * 40.0D;
                            this.guiEffectRenderer.addEffect(new EnergyEffect(this.player.worldObj, xPos, yPos, centerX, ty, 0));
                        }
                    }
            }
            if (this.tile.getCraftingStage() > 1000) {
                double xPos = (centerX - 8) + this.rand.nextDouble() * 16.0D;
                double yTop = (this.guiTop + 35 - 8) + this.rand.nextDouble() * 16.0D;
                this.guiEffectRenderer.addEffect(new EnergyEffect(this.player.worldObj, xPos, yTop, centerX, (this.guiTop + 78), 1));
            }
            this.guiEffectRenderer.updateEffects();
        } else {
            this.guiEffectRenderer.clearEffects();
        }
        this.boltStats[0] = (int)(this.rand.nextDouble() * 18.0D);
        this.boltStats[1] = (int)(this.rand.nextDouble() * 18.0D);
        this.boltStats[2] = this.rand.nextInt();
        this.boltStats[3] = (int)(this.rand.nextDouble() * 18.0D);
        this.boltStats[4] = (int)(this.rand.nextDouble() * 18.0D);
        this.boltStats[5] = this.rand.nextInt();
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        if (this.currentRecipe != null) {
            if (this.canCraft != null && !this.canCraft.equals("true"))
                if (this.canCraft.equals("tierLow")) {
                    drawCenteredString(this.fontRendererObj, LocalizationUtils.localize("tooltip.gui.fusionCrafting.tierLow.info"), this.xSize / 2, 95, 11141375);
                } else if (this.canCraft.equals("outputObstructed")) {
                    drawCenteredString(this.fontRendererObj, LocalizationUtils.localize("tooltip.gui.fusionCrafting.outputObstructed.info"), this.xSize / 2, 95, 11141375);
                } else {
                    GL11.glTranslatef(0.0F, 0.0F, 600.0F);
                    GuiHelper.drawColouredRect(5, 88, this.xSize - 10, 20, -65536);
                    GuiHelper.drawColouredRect(6, 89, this.xSize - 12, 18, -16777216);
                    GuiHelper.drawCenteredSplitString(this.mc.fontRenderer, LocalizationUtils.localize(this.canCraft), this.xSize / 2, 90, this.xSize - 10, 11141375, false);
                    GL11.glTranslatef(0.0F, 0.0F, -600.0F);
                }
        } else if (Loader.isModLoaded("NotEnoughItems")) {
            GuiHelper.drawBorderedRect(81, 45, 18, 22, 1, -15724528, -13619152);
            this.mc.fontRenderer.drawString("R", 87, 52, 10526880, false);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.guiEffectRenderer.renderEffects(partialTicks);
    }

    protected void actionPerformed(GuiButton button) {
        NetworkManager.instance.sendToServer(new PacketFusionCore(0));
    }

    public static class EnergyEffect extends GuiEffect {
        private final double xTarget;

        private final double yTarget;

        private final int type;

        public EnergyEffect(World world, double xCoord, double yCoord, double xTarget, double yTarget, int type) {
            super(world, xCoord, yCoord, 0.0D, 0.0D);
            this.xTarget = xTarget;
            this.yTarget = yTarget;
            this.type = type;
            this.motionX = this.motionY = 0.0D;
            this.particleMaxAge = 12;
            this.particleScale = 1.0F;
            this.particleTextureIndexY = 1;
            if (type == 1) {
                this.particleRed = 0.0F;
                this.particleGreen = 0.8F;
                this.particleBlue = 1.0F;
                this.particleMaxAge = 21;
            }
        }

        public boolean isTransparent() {
            return true;
        }

        public void onUpdate() {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            double x = this.xTarget - this.posX;
            double y = this.yTarget - this.posY;
            double dist = Math.sqrt(x * x + y * y);
            if (this.particleAge++ >= this.particleMaxAge || dist < -10.0D)
                setExpired();
            if (this.particleMaxAge - this.particleAge < 10) {
                float d = (this.particleMaxAge - this.particleAge) / 10.0F;
                this.particleScale = d * 1.0F;
            }
            if (this.type == 1 && this.particleMaxAge - this.particleAge < 2)
                this.particleScale = 3.0F;
            this.particleTextureIndexX = this.rand.nextInt(5);
            dist = (dist == 0.0D) ? 0.1D : dist;
            x /= dist;
            y /= dist;
            double speed = (this.type == 0) ? 5.0D : 3.0D;
            this.motionX = x * speed;
            this.motionY = y * speed;
            moveEntity(this.motionX, this.motionY);
        }

        public void renderParticle(float partialTicks) {
            GuiHelper.bindTexture("textures/particle/particles.png");
            float minU = this.particleTextureIndexX / 8.0F;
            float maxU = minU + 0.125F;
            float minV = this.particleTextureIndexY / 8.0F;
            float maxV = minV + 0.125F;
            float scale = 8.0F * this.particleScale;
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            float renderX = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks);
            float renderY = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks);
            tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
            tessellator.addVertexWithUV((renderX - 1.0F * scale), (renderY - 1.0F * scale), this.zLevel, maxU, maxV);
            tessellator.addVertexWithUV((renderX - 1.0F * scale), (renderY + 1.0F * scale), this.zLevel, maxU, minV);
            tessellator.addVertexWithUV((renderX + 1.0F * scale), (renderY + 1.0F * scale), this.zLevel, minU, minV);
            tessellator.addVertexWithUV((renderX + 1.0F * scale), (renderY - 1.0F * scale), this.zLevel, minU, maxV);
            tessellator.draw();
        }
    }

    public class StartButton extends GuiButton {
        private boolean hovered = false;

        public StartButton(int buttonId, int x, int y, int width, int height, String text) {
            super(buttonId, x, y, width, height, text);
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
                int back = -16777216;
                GuiHelper.drawColouredRect(this.xPosition + 1, this.yPosition + 1, this.width - 2, this.height - 2, back);
                int border = this.hovered ? -16737895 : -7864133;
                GuiHelper.drawColouredRect(this.xPosition, this.yPosition, this.width, 1, border);
                GuiHelper.drawColouredRect(this.xPosition, this.yPosition + this.height - 1, this.width, 1, border);
                GuiHelper.drawColouredRect(this.xPosition, this.yPosition, 1, this.height, border);
                GuiHelper.drawColouredRect(this.xPosition + this.width - 1, this.yPosition, 1, this.height, border);
                GuiHelper.drawCenteredString(mc.fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + this.height / 2 - mc.fontRenderer.FONT_HEIGHT / 2, 16777215, false);
            }
        }
    }
}
