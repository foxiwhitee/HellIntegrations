package foxiwhitee.hellmod.integration.thaumcraft.client.gui;


import appeng.api.storage.ITerminalHost;
import appeng.container.slot.SlotFakeCraftingMatrix;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.terminals.GuiPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.container.ContainerPartInfusionPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.IThaumcraftRecipe;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.client.lib.UtilsFX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GuiPartInfusionPatternTerminal extends GuiPatternTerminal {
    private Object[] tooltip = null;

    public GuiPartInfusionPatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, new ContainerPartInfusionPatternTerminal(inventoryPlayer, te), 511, 250);
        this.hasCycleItems = true;
        this.cycleItemsCordX = 403;
        this.cycleItemsCordY = 90;
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.bindTexture(this.getBackground());

        UtilGui.drawTexture(offsetX, offsetY, 0, 0, xSize, ySize, xSize, ySize, 768, 768);

        if (this.searchField != null) {
            this.searchField.drawTextBox();
        }
    }

    @Override
    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        super.drawFG(offsetX, offsetY, mouseX, mouseY);
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 1; i < getContainer().getTerminal().getInventoryCrafting().getSizeInventory(); i++) {
            if (getContainer().getTerminal().getInventoryCrafting().getStackInSlot(i) != null) {
                stacks.add(getContainer().getTerminal().getInventoryCrafting().getStackInSlot(i));
            }
        }
        this.renderItems(399, 87, stacks);
        this.drawRecipe(mouseX, mouseY);
        if (this.tooltip != null) {
            UtilGui.drawCustomTooltip(this, itemRender, this.fontRendererObj, (List)this.tooltip[0], (Integer)this.tooltip[1], (Integer)this.tooltip[2], (Integer)this.tooltip[3]);
            this.tooltip = null;
        }

    }

    @Override
    protected String getBackground() {
        return "gui/gui_terminal_thaumcraft_pattern_matrix.png";
    }

    private void drawAspects(InfusionRecipe recipe, int mouseX, int mouseY) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        AspectList aspectList = recipe.getAspects();
        Aspect[] aspects = aspectList.getAspectsSortedAmount();
        int aspectsSize = aspects.length;
        int rows = aspectsSize / 9;
        rows = rows == 0 ? 1 : rows;
        int lastRow = aspectsSize % 9;
        int lastRowToFill = 9 - lastRow;
        this.mc.renderEngine.bindTexture(new ResourceLocation(HellCore.MODID, "textures/" + this.getBackground()));
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);

        int yy;
        for (yy = 0; yy < rows; ++yy) {
            UtilGui.drawTexture(321, 221 + 31 * yy, 0.0F, 483.0F, 173, 29, 173, 29, 768, 768);
        }

        GL11.glDisable(3042);

        int xx;
        int xCord;
        int yCord;
        int aspect = 0;
        for (yy = 0; yy < rows; ++yy) {
            yCord = 227 + yy * 31;
            for (xx = 0; xx < 9; ++xx) {
                xCord = 328 + xx * 18;
                if (aspect < aspects.length) {
                    if (mouseX >= xCord && mouseX <= xCord + 18 && mouseY >= yCord && mouseY <= yCord + 18) {
                        this.drawTooltip(Arrays.asList(aspects[aspect].getName(), aspects[aspect].getLocalizedDescription()), mouseX, mouseY, 11);
                    }

                    UtilsFX.drawTag(xCord, yCord, aspects[aspect], (float) aspectList.getAmount(aspects[aspect]), 0, 0.0);
                }
                aspect++;
            }
        }

    }

    public void drawTooltip(List var4, int par2, int par3, int subTipColor) {
        this.tooltip = new Object[]{var4, par2, par3, subTipColor};
    }

    private void drawRecipe(int mouseX, int mouseY) {
        ItemStack out = this.getContainer().getTerminal().getInventoryOutput().getStackInSlot(0);
        if (out != null) {
            InfusionRecipe recipe = ThaumcraftApi.getInfusionRecipe(out);
            if (recipe != null) {
                this.drawAspects(recipe, mouseX, mouseY);
            }
        }
    }

    public void renderItems(int x, int y, List stacks) {
        int items = stacks.size();
        if (items > 0) {
            float degreePerInput = 360.0F / (float) (items + 1);
            float currentDegree = degreePerInput;
            GL11.glPushMatrix();
            GL11.glTranslated((double) x, (double) y, 0.0);

            for (Iterator var7 = stacks.iterator(); var7.hasNext(); currentDegree += degreePerInput) {
                ItemStack s = (ItemStack) var7.next();
                GL11.glPushMatrix();
                GL11.glTranslated(8.0, 8.0, 0.0);
                GL11.glRotated((double) currentDegree, 0.0, 0.0, 1.0);
                GL11.glTranslated(-8.0, -8.0, 0.0);
                RenderItem var10000 = itemRender;
                var10000.zLevel -= 50.0F;
                GL11.glEnable(2929);
                itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, s, 0, -43);
                GL11.glDisable(2929);
                var10000 = itemRender;
                var10000.zLevel += 50.0F;
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }
    }
}