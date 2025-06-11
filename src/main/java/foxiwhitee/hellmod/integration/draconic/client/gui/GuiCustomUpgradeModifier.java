package foxiwhitee.hellmod.integration.draconic.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.brandon3055.brandonscore.client.utills.GuiHelper;
import com.brandon3055.draconicevolution.client.handler.ResourceHandler;
import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.container.ContainerUpgradeModifier;
import com.brandon3055.draconicevolution.common.tileentities.TileObjectSync;
import com.brandon3055.draconicevolution.common.utills.IUpgradableItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.container.ContainerCustomUpgradeModifier;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.integration.draconic.tile.TileCustomUpgradeModifier;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class GuiCustomUpgradeModifier extends GuiContainer {
    public EntityPlayer player;
    private TileCustomUpgradeModifier tile;
    public boolean inUse = false;
    private IUpgradableItem upgradableItemO = null;
    private ICustomUpgradableItem upgradableItem = null;
    private ItemStack stack = null;
    private List<IUpgradableItem.EnumUpgrade> itemUpgradesO = new ArrayList();
    private List<ICustomUpgradableItem.EnumUpgrade> itemUpgrades = new ArrayList();
    private ContainerCustomUpgradeModifier containerEM;
    private int coreSlots = 0;
    private int coreTier = 0;
    private int usedSlots = 0;
    private boolean[] coreInInventory = new boolean[5];

    public GuiCustomUpgradeModifier(ContainerCustomUpgradeModifier containerEM) {
        super(containerEM);
        this.containerEM = containerEM;
        this.xSize = 176;
        this.ySize = 190;
        this.tile = (TileCustomUpgradeModifier) containerEM.getTile();
        this.player = containerEM.getPlayer();
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        DraconicEvolutionIntegration.bindResource("textures/gui/gui_CustomUpgradeModifier.png");
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 70, this.guiTop + 6, 60, 106, 100, 50);
        this.drawTexturedModalRect(this.guiLeft + 70, this.guiTop + 56, 60, 106, 100, 50);
        GL11.glPushMatrix();
        GL11.glTranslated((double)(this.guiLeft + 70), (double)(this.guiTop + 6), (double)0.0F);
        GL11.glTranslatef(50.0F, 50.0F, 0.0F);
        GL11.glRotatef(this.tile.rotation + f * this.tile.rotationSpeed, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-50.0F, -50.0F, 0.0F);
        this.drawTexturedModalRect(0, 0, 70, 6, 100, 100);
        GL11.glPopMatrix();
        if (!this.inUse) {
            this.drawTexturedModalRect(this.guiLeft + 3, this.guiTop + 77, 60, 106, 56, 55);
            this.drawTexturedModalRect(this.guiLeft + 3, this.guiTop + 132, 60, 106, 56, 55);
        } else {
            this.drawFlippedTexturedModalRect(this.guiLeft + 59, this.guiTop + 77, 3, 77, 56, 110);
            this.drawTexturedModalRect(this.guiLeft + 115, this.guiTop + 77, 3, 77, 56, 110);
            this.drawFlippedTexturedModalRect(this.guiLeft + 171, this.guiTop + 77, 57, 77, 2, 110);
        }

        if (!this.inUse) {
            this.drawSlots();
        } else {
            this.renderUpgrades(x, y);
        }

        if (this.inUse) {
            if (upgradableItem != null) {
                this.drawHoveringText(this.upgradableItem.getUpgradeStats(this.stack), this.guiLeft + this.xSize - 9, this.guiTop + 17, this.fontRendererObj);
            } else {
                this.drawHoveringText(this.upgradableItemO.getUpgradeStats(this.stack), this.guiLeft + this.xSize - 9, this.guiTop + 17, this.fontRendererObj);
            }
        }

    }

    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.drawCenteredString(this.fontRendererObj, this.tile.getBlockType().getLocalizedName(), this.xSize / 2, -9, 65535);
    }

    public void initGui() {
        super.initGui();
    }

    protected void actionPerformed(GuiButton button) {
    }

    public void updateScreen() {
        super.updateScreen();
        if (this.tile.getStackInSlot(0) != null && this.tile.getStackInSlot(0).getItem() instanceof ICustomUpgradableItem) {
            this.stack = this.tile.getStackInSlot(0);
            this.upgradableItem = (ICustomUpgradableItem)this.stack.getItem();
            this.itemUpgrades = this.upgradableItem.getCUpgrades(this.stack);
            this.inUse = true;
            this.coreSlots = this.upgradableItem.getUpgradeCap(this.stack);
            this.coreTier = this.upgradableItem.getMaxTier(this.stack);
            this.usedSlots = 0;
            this.coreInInventory[0] = this.player.inventory.hasItem(ModItems.draconicCore);
            this.coreInInventory[1] = this.player.inventory.hasItem(ModItems.wyvernCore);
            this.coreInInventory[2] = this.player.inventory.hasItem(ModItems.awakenedCore);
            this.coreInInventory[3] = this.player.inventory.hasItem(ModItems.chaoticCore);
            this.coreInInventory[4] = this.player.inventory.hasItem(DraconicEvolutionIntegration.arialCore);

            for(ICustomUpgradableItem.EnumUpgrade upgrade : this.upgradableItem.getCUpgrades(this.stack)) {
                int[] arr$ = upgrade.getCoresApplied(this.stack);
                int len$ = arr$.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Integer i = arr$[i$];
                    this.usedSlots += i;
                }
            }
        } else if (this.tile.getStackInSlot(0) != null && this.tile.getStackInSlot(0).getItem() instanceof IUpgradableItem) {
            this.stack = this.tile.getStackInSlot(0);
            this.upgradableItemO = (IUpgradableItem)this.stack.getItem();
            this.itemUpgradesO = this.upgradableItemO.getUpgrades(this.stack);
            this.inUse = true;
            this.coreSlots = this.upgradableItemO.getUpgradeCap(this.stack);
            this.coreTier = this.upgradableItemO.getMaxTier(this.stack);
            this.usedSlots = 0;
            this.coreInInventory[0] = this.player.inventory.hasItem(ModItems.draconicCore);
            this.coreInInventory[1] = this.player.inventory.hasItem(ModItems.wyvernCore);
            this.coreInInventory[2] = this.player.inventory.hasItem(ModItems.awakenedCore);
            this.coreInInventory[3] = this.player.inventory.hasItem(ModItems.chaoticCore);

            for(IUpgradableItem.EnumUpgrade upgrade : this.upgradableItemO.getUpgrades(this.stack)) {
                int[] arr$ = upgrade.getCoresApplied(this.stack);
                int len$ = arr$.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Integer i = arr$[i$];
                    this.usedSlots += i;
                }
            }
        } else {
            this.inUse = false;
        }

    }

    private void drawSlots() {
        ResourceHandler.bindResource("textures/gui/Widgets.png");
        int xPos = this.guiLeft + (this.xSize - 162) / 2;
        int yPos = this.guiTop + 110;

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                this.drawTexturedModalRect(xPos + x * 18, yPos + y * 18, 138, 0, 18, 18);
            }
        }

        for(int x = 0; x < 9; ++x) {
            this.drawTexturedModalRect(xPos + x * 18, yPos + 56, 138, 0, 18, 18);
        }

        this.drawTexturedModalRect(this.guiLeft + 111, this.guiTop + 47, 138, 0, 18, 18);
    }

    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);

        for(ICustomUpgradableItem.EnumUpgrade upgrade : this.itemUpgrades) {
            int xIndex = this.itemUpgrades.indexOf(upgrade);
            int spacing = (this.xSize - 6) / this.itemUpgrades.size();
            int xPos = this.guiLeft + xIndex * spacing + (spacing - 23) / 2 + 4;
            int yPos = this.guiTop + 90;
            int[] appliedCores = upgrade.getCoresApplied(this.tile.getStackInSlot(0));

            for(int i = 0; i <= this.coreTier; ++i) {
                if (this.coreInInventory[i] &&
                        this.coreSlots > this.usedSlots &&
                        GuiHelper.isInRect(xPos, yPos + 33 + i * 18, 8, 8, x, y) &&
                        upgrade.getUpgradePoints(this.stack) < this.upgradableItem.getMaxUpgradePoints(upgrade.index, this.stack)) {
                    this.containerEM.sendObjectToServer((TileObjectSync)null, upgrade.index, i * 2);
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(ResourceHandler.getResourceWOP("gui.button.press"), 1.0F));
                }

                if (appliedCores[i] > 0 && GuiHelper.isInRect(xPos + 16, yPos + 33 + i * 18, 8, 8, x, y)) {
                    this.containerEM.sendObjectToServer((TileObjectSync)null, upgrade.index, 1 + i * 2);
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(ResourceHandler.getResourceWOP("gui.button.press"), 1.0F));
                }
            }
        }

        for(IUpgradableItem.EnumUpgrade upgrade : this.itemUpgradesO) {
            int xIndex = this.itemUpgradesO.indexOf(upgrade);
            int spacing = (this.xSize - 6) / this.itemUpgradesO.size();
            int xPos = this.guiLeft + xIndex * spacing + (spacing - 23) / 2 + 4;
            int yPos = this.guiTop + 90;
            int[] appliedCores = upgrade.getCoresApplied(this.tile.getStackInSlot(0));

            for(int i = 0; i <= this.coreTier; ++i) {
                if (this.coreInInventory[i] &&
                        this.coreSlots > this.usedSlots &&
                        GuiHelper.isInRect(xPos, yPos + 33 + i * 18, 8, 8, x, y) &&
                        upgrade.getUpgradePoints(this.stack) < this.upgradableItemO.getMaxUpgradePoints(upgrade.index, this.stack)) {
                    this.containerEM.sendObjectToServer((TileObjectSync)null, upgrade.index, i * 2);
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(ResourceHandler.getResourceWOP("gui.button.press"), 1.0F));
                }

                if (appliedCores[i] > 0 && GuiHelper.isInRect(xPos + 16, yPos + 33 + i * 18, 8, 8, x, y)) {
                    this.containerEM.sendObjectToServer((TileObjectSync)null, upgrade.index, 1 + i * 2);
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(ResourceHandler.getResourceWOP("gui.button.press"), 1.0F));
                }
            }
        }


    }

    private void renderUpgrades(int x, int y) {
        DraconicEvolutionIntegration.bindResource("textures/gui/gui_CustomUpgradeModifier.png");
        if (coreTier == 4) {
            UtilGui.drawTexture(this.guiLeft, this.guiTop + 187, 0, 169, 176, 21, 176, 21, 256, 256);
            UtilGui.drawTexture(this.guiLeft + 3, this.guiTop + 187, 3, 169, 170, 21, 56, 21, 256, 256);
        }
        for(ICustomUpgradableItem.EnumUpgrade upgrade : this.itemUpgrades) {
            int xIndex = this.itemUpgrades.indexOf(upgrade);
            int spacing = (this.xSize - 6) / this.itemUpgrades.size();
            int xPos = this.guiLeft + xIndex * spacing + (spacing - 23) / 2 + 4;
            int yPos = this.guiTop + 90;
            DraconicEvolutionIntegration.bindResource("textures/gui/gui_CustomUpgradeModifier.png");
            this.drawTexturedModalRect(xPos, yPos, 0, 190, 24, 24);
            this.drawTexturedModalRect(xPos + 3, yPos + 3, upgrade.index * 18, 220, 18, 18);
            this.drawTexturedModalRect(xPos + 2, yPos - 10, 126, 190, 20, 11);
            int[] appliedCores = upgrade.getCoresApplied(this.tile.getStackInSlot(0));
            for(int i = 0; i <= this.coreTier; ++i) {
                int j = i == 4 ? 7 : i;
                this.drawTexturedModalRect(xPos + 3, yPos + 24 + i * 18, 24 + j * 18, 190, 18, 18);
                this.drawTexturedModalRect(xPos + 3, yPos + 24 + i * 18, 24 + j * 18, 190, 18, 18);
                GL11.glEnable(3042);
                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.9F);
                if (appliedCores[i] < 10) {
                    this.drawTexturedModalRect(xPos + 8, yPos + 28 + i * 18, 3, 3, 7, 9);
                } else {
                    this.drawTexturedModalRect(xPos + 5, yPos + 28 + i * 18, 3, 3, 13, 9);
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(3042);
                if (this.coreSlots > this.usedSlots && upgrade.getUpgradePoints(this.stack) < this.upgradableItem.getMaxUpgradePoints(upgrade.index, this.stack)) {
                    boolean hovering = GuiHelper.isInRect(xPos, yPos + 33 + i * 18, 8, 8, x, y);
                    if (!this.coreInInventory[i]) {
                        this.drawTexturedModalRect(xPos, yPos + 33 + i * 18, 24, 208, 8, 8);
                    } else {
                        this.drawTexturedModalRect(xPos, yPos + 33 + i * 18, 32 + (hovering ? 8 : 0), 208, 8, 8);
                    }
                }

                if (appliedCores[i] > 0) {
                    boolean hovering = GuiHelper.isInRect(xPos + 16, yPos + 33 + i * 18, 8, 8, x, y);
                    this.drawTexturedModalRect(xPos + 16, yPos + 33 + i * 18, 56 + (hovering ? 8 : 0), 208, 8, 8);
                }
            }

            for(int i = 0; i <= this.coreTier; ++i) {
                this.fontRendererObj.drawString(String.valueOf(appliedCores[i]), xPos + 12 - this.fontRendererObj.getStringWidth(String.valueOf(appliedCores[i])) / 2, yPos + 29 + i * 18, 16777215);
            }

            this.fontRendererObj.drawString(String.valueOf(upgrade.getUpgradePoints(this.stack)), xPos + 12 - this.fontRendererObj.getStringWidth(String.valueOf(upgrade.getUpgradePoints(this.stack))) / 2, yPos - 8, 16777215);
        }

        for(IUpgradableItem.EnumUpgrade upgrade : this.itemUpgradesO) {
            int xIndex = this.itemUpgradesO.indexOf(upgrade);
            int spacing = (this.xSize - 6) / this.itemUpgradesO.size();
            int xPos = this.guiLeft + xIndex * spacing + (spacing - 23) / 2 + 4;
            int yPos = this.guiTop + 90;
            DraconicEvolutionIntegration.bindResource("textures/gui/gui_CustomUpgradeModifier.png");
            this.drawTexturedModalRect(xPos, yPos, 0, 190, 24, 24);
            this.drawTexturedModalRect(xPos + 3, yPos + 3, upgrade.index * 18, 220, 18, 18);
            this.drawTexturedModalRect(xPos + 2, yPos - 10, 126, 190, 20, 11);
            int[] appliedCores = upgrade.getCoresApplied(this.tile.getStackInSlot(0));
            int falseCore = Math.min(coreTier, 3);
            for(int i = 0; i <= falseCore; ++i) {
                this.drawTexturedModalRect(xPos + 3, yPos + 24 + i * 18, 24 + i * 18, 190, 18, 18);
                this.drawTexturedModalRect(xPos + 3, yPos + 24 + i * 18, 24 + i * 18, 190, 18, 18);
                GL11.glEnable(3042);
                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.9F);
                if (appliedCores[i] < 10) {
                    this.drawTexturedModalRect(xPos + 8, yPos + 28 + i * 18, 3, 3, 7, 9);
                } else {
                    this.drawTexturedModalRect(xPos + 5, yPos + 28 + i * 18, 3, 3, 13, 9);
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(3042);
                if (this.coreSlots > this.usedSlots && upgrade.getUpgradePoints(this.stack) < this.upgradableItemO.getMaxUpgradePoints(upgrade.index, this.stack)) {
                    boolean hovering = GuiHelper.isInRect(xPos, yPos + 33 + i * 18, 8, 8, x, y);
                    if (!this.coreInInventory[i]) {
                        this.drawTexturedModalRect(xPos, yPos + 33 + i * 18, 24, 208, 8, 8);
                    } else {
                        this.drawTexturedModalRect(xPos, yPos + 33 + i * 18, 32 + (hovering ? 8 : 0), 208, 8, 8);
                    }
                }

                if (appliedCores[i] > 0) {
                    boolean hovering = GuiHelper.isInRect(xPos + 16, yPos + 33 + i * 18, 8, 8, x, y);
                    this.drawTexturedModalRect(xPos + 16, yPos + 33 + i * 18, 56 + (hovering ? 8 : 0), 208, 8, 8);
                }
            }

            for(int i = 0; i <= falseCore; ++i) {
                this.fontRendererObj.drawString(String.valueOf(appliedCores[i]), xPos + 12 - this.fontRendererObj.getStringWidth(String.valueOf(appliedCores[i])) / 2, yPos + 29 + i * 18, 16777215);
            }

            this.fontRendererObj.drawString(String.valueOf(upgrade.getUpgradePoints(this.stack)), xPos + 12 - this.fontRendererObj.getStringWidth(String.valueOf(upgrade.getUpgradePoints(this.stack))) / 2, yPos - 8, 16777215);
        }

        this.fontRendererObj.drawStringWithShadow(LocalizationUtils.localize("gui.de.cores.txt"), this.guiLeft + 4, this.guiTop + 4, 65280);
        this.fontRendererObj.drawString(LocalizationUtils.localize("gui.de.cap.txt"), this.guiLeft + 4, this.guiTop + 16, 0);
        this.fontRendererObj.drawString(">" + this.coreSlots, this.guiLeft + 4, this.guiTop + 25, 0);
        this.fontRendererObj.drawString(LocalizationUtils.localize("gui.de.installed.txt"), this.guiLeft + 4, this.guiTop + 37, 0);
        this.fontRendererObj.drawString(">" + this.usedSlots, this.guiLeft + 4, this.guiTop + 46, 0);
        this.fontRendererObj.drawString(LocalizationUtils.localize("gui.de.free.txt"), this.guiLeft + 4, this.guiTop + 58, 0);
        this.fontRendererObj.drawString(">" + (this.coreSlots - this.usedSlots), this.guiLeft + 4, this.guiTop + 67, 0);
    }

    public void drawScreen(int x, int y, float f) {
        super.drawScreen(x, y, f);
        if (this.inUse) {
            for(ICustomUpgradableItem.EnumUpgrade upgrade : this.itemUpgrades) {
                int xIndex = this.itemUpgrades.indexOf(upgrade);
                int spacing = (this.xSize - 6) / this.itemUpgrades.size();
                int xPos = this.guiLeft + xIndex * spacing + (spacing - 23) / 2 + 4;
                int yPos = this.guiTop + 90;
                int[] appliedCores = upgrade.getCoresApplied(this.tile.getStackInSlot(0));
                if (GuiHelper.isInRect(xPos, yPos, 24, 24, x, y)) {
                    List list = new ArrayList();
                    list.add(upgrade.getLocalizedName());
                    this.drawHoveringText(list, x, y, this.fontRendererObj);
                }

                if (GuiHelper.isInRect(xPos + 3, yPos - 9, 18, 8, x, y)) {
                    List list = new ArrayList();
                    list.add(LocalizationUtils.localize("gui.de.basePoints.txt") + ": " + this.upgradableItem.getBaseUpgradePoints(upgrade.index));
                    list.add(LocalizationUtils.localize("gui.de.maxPoints.txt") + ": " + this.upgradableItem.getMaxUpgradePoints(upgrade.index, this.stack));
                    list.add(LocalizationUtils.localize("gui.de.pointCost.txt") + ": " + upgrade.pointConversion);
                    this.drawHoveringText(list, x, y, this.fontRendererObj);
                }

                for(int i = 0; i <= this.coreTier; ++i) {
                    if (GuiHelper.isInRect(xPos + 9, yPos + 25 + i * 18, 6, 15, x, y)) {
                        List list = new ArrayList();
                        double value = Math.pow((double)2.0F, (double)i) / (double)upgrade.pointConversion;
                        String string = LocalizationUtils.localize("gui.de.value.txt") + ": " + value + " " + (value == (double)1.0F ? LocalizationUtils.localize("gui.de.point.txt") : LocalizationUtils.localize("gui.de.points.txt"));
                        list.add(string.replace(".0", ""));
                        this.drawHoveringText(list, x, y, this.fontRendererObj);
                    }

                    if (this.coreSlots > this.usedSlots && GuiHelper.isInRect(xPos, yPos + 33 + i * 18, 8, 8, x, y)) {
                        List list = new ArrayList();
                        if (this.coreInInventory[i]) {
                            list.add(LocalizationUtils.localize("gui.de.addCore.txt"));
                        } else {
                            list.add(LocalizationUtils.localize("gui.de.noCoresInInventory" + i + ".txt"));
                        }

                        this.drawHoveringText(list, x, y, this.fontRendererObj);
                    }

                    if (appliedCores[i] > 0 && GuiHelper.isInRect(xPos + 16, yPos + 33 + i * 18, 8, 8, x, y)) {
                        List list = new ArrayList();
                        if (this.coreInInventory[i]) {
                            list.add(LocalizationUtils.localize("gui.de.removeCore.txt"));
                        }

                        this.drawHoveringText(list, x, y, this.fontRendererObj);
                    }
                }
            }

            for(IUpgradableItem.EnumUpgrade upgrade : this.itemUpgradesO) {
                int xIndex = this.itemUpgradesO.indexOf(upgrade);
                int spacing = (this.xSize - 6) / this.itemUpgradesO.size();
                int xPos = this.guiLeft + xIndex * spacing + (spacing - 23) / 2 + 4;
                int yPos = this.guiTop + 90;
                int[] appliedCores = upgrade.getCoresApplied(this.tile.getStackInSlot(0));
                if (GuiHelper.isInRect(xPos, yPos, 24, 24, x, y)) {
                    List list = new ArrayList();
                    list.add(upgrade.getLocalizedName());
                    this.drawHoveringText(list, x, y, this.fontRendererObj);
                }

                if (GuiHelper.isInRect(xPos + 3, yPos - 9, 18, 8, x, y)) {
                    List list = new ArrayList();
                    list.add(LocalizationUtils.localize("gui.de.basePoints.txt") + ": " + this.upgradableItemO.getBaseUpgradePoints(upgrade.index));
                    list.add(LocalizationUtils.localize("gui.de.maxPoints.txt") + ": " + this.upgradableItemO.getMaxUpgradePoints(upgrade.index, this.stack));
                    list.add(LocalizationUtils.localize("gui.de.pointCost.txt") + ": " + upgrade.pointConversion);
                    this.drawHoveringText(list, x, y, this.fontRendererObj);
                }
                int falseCore = Math.min(coreTier, 3);
                for(int i = 0; i <= falseCore; ++i) {
                    if (GuiHelper.isInRect(xPos + 9, yPos + 25 + i * 18, 6, 15, x, y)) {
                        List list = new ArrayList();
                        double value = Math.pow((double)2.0F, (double)i) / (double)upgrade.pointConversion;
                        String string = LocalizationUtils.localize("gui.de.value.txt") + ": " + value + " " + (value == (double)1.0F ? LocalizationUtils.localize("gui.de.point.txt") : LocalizationUtils.localize("gui.de.points.txt"));
                        list.add(string.replace(".0", ""));
                        this.drawHoveringText(list, x, y, this.fontRendererObj);
                    }

                    if (this.coreSlots > this.usedSlots && GuiHelper.isInRect(xPos, yPos + 33 + i * 18, 8, 8, x, y)) {
                        List list = new ArrayList();
                        if (this.coreInInventory[i]) {
                            list.add(LocalizationUtils.localize("gui.de.addCore.txt"));
                        } else {
                            list.add(LocalizationUtils.localize("gui.de.noCoresInInventory" + i + ".txt"));
                        }

                        this.drawHoveringText(list, x, y, this.fontRendererObj);
                    }

                    if (appliedCores[i] > 0 && GuiHelper.isInRect(xPos + 16, yPos + 33 + i * 18, 8, 8, x, y)) {
                        List list = new ArrayList();
                        if (this.coreInInventory[i]) {
                            list.add(LocalizationUtils.localize("gui.de.removeCore.txt"));
                        }

                        this.drawHoveringText(list, x, y, this.fontRendererObj);
                    }
                }
            }


        }
    }

    public void drawFlippedTexturedModalRect(int xPos, int yPos, int texXPos, int texYPos, int xSize, int ySize) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)xPos, (double)(yPos + ySize), (double)this.zLevel, (double)((float)(texXPos + xSize) * f), (double)((float)(texYPos + ySize) * f1));
        tessellator.addVertexWithUV((double)(xPos + xSize), (double)(yPos + ySize), (double)this.zLevel, (double)((float)texXPos * f), (double)((float)(texYPos + ySize) * f1));
        tessellator.addVertexWithUV((double)(xPos + xSize), (double)yPos, (double)this.zLevel, (double)((float)texXPos * f), (double)((float)texYPos * f1));
        tessellator.addVertexWithUV((double)xPos, (double)yPos, (double)this.zLevel, (double)((float)(texXPos + xSize) * f), (double)((float)texYPos * f1));
        tessellator.draw();
    }
}
