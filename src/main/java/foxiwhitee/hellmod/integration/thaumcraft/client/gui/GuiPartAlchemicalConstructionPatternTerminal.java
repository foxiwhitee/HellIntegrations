package foxiwhitee.hellmod.integration.thaumcraft.client.gui;

import appeng.api.config.ActionItems;
import appeng.api.config.Settings;
import appeng.api.storage.ITerminalHost;
import appeng.client.gui.widgets.GuiImgButton;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.api.config.Buttons;
import foxiwhitee.hellmod.client.gui.buttons.NoTextureAEButton;
import foxiwhitee.hellmod.client.gui.terminals.GuiPatternTerminal;
import foxiwhitee.hellmod.client.gui.widgets.CustomAEGuiButton;
import foxiwhitee.hellmod.api.config.CustomAESetings;
import foxiwhitee.hellmod.integration.thaumcraft.container.ContainerPartAlchemicalConstructionPatternTerminal;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import foxiwhitee.hellmod.integration.thaumcraft.recipes.IThaumcraftRecipe;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.client.lib.UtilsFX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiPartAlchemicalConstructionPatternTerminal extends GuiPatternTerminal {
    private NoTextureAEButton prevBtn;
    private NoTextureAEButton nextBtn;
    private List crucibleRecipeList;
    private int crucibleRecipeIndex = -1;
    private Object[] tooltip = null;
    private ItemStack lastInputStack;
    private byte recipeStatus = -1;


    public GuiPartAlchemicalConstructionPatternTerminal(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te, new ContainerPartAlchemicalConstructionPatternTerminal(inventoryPlayer, te), 511, 250);
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.bindTexture(this.getBackground());

        UtilGui.drawTexture(offsetX, offsetY, 0, 0, xSize, ySize, xSize, ySize, 768, 768);

        if (this.searchField != null) {
            this.searchField.drawTextBox();
        }
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
        super.actionPerformed(btn);
        if (this.prevBtn == btn) {
            if (this.crucibleRecipeList == null) {
                return;
            }

            if (this.crucibleRecipeIndex > 0 && !this.crucibleRecipeList.isEmpty()) {
                --this.crucibleRecipeIndex;

                try {
                    NetworkManager.instance.sendToServer(new DefaultPacket("PatternTerminal.Index", String.valueOf(this.crucibleRecipeIndex)));
                } catch (IOException var6) {
                    throw new RuntimeException(var6);
                }
            }
        } else if (this.nextBtn == btn) {
            if (this.crucibleRecipeList == null) {
                return;
            }

            if (!this.crucibleRecipeList.isEmpty()) {
                int currentRecipeIndex = this.crucibleRecipeIndex;
                int maximumRecipeIndex = this.crucibleRecipeList.size() - 1;
                if (currentRecipeIndex < maximumRecipeIndex) {
                    ++this.crucibleRecipeIndex;

                    try {
                        NetworkManager.instance.sendToServer(new DefaultPacket("PatternTerminal.Index", String.valueOf(this.crucibleRecipeIndex)));
                    } catch (IOException var5) {
                        throw new RuntimeException(var5);
                    }
                }
            }
        }
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        if (this.lastInputStack == null && this.getContainer().getInventoryCrafting().getStackInSlot(0) != null) {
            List crucibleRecipes = ((ContainerPartAlchemicalConstructionPatternTerminal)getContainer()).getRecipes(this.getContainer().getInventoryCrafting().getStackInSlot(0));
            if (crucibleRecipes.isEmpty()) {
                this.recipeStatus = 0;
            } else {
                this.lastInputStack = this.getContainer().getInventoryCrafting().getStackInSlot(0);
                this.crucibleRecipeList = crucibleRecipes;
                this.recipeStatus = 1;
                this.crucibleRecipeIndex = 0;

                try {
                    NetworkManager.instance.sendToServer(new DefaultPacket("PatternTerminal.Index", String.valueOf(this.crucibleRecipeIndex)));
                } catch (IOException var7) {
                    throw new RuntimeException(var7);
                }
            }
        } else if (this.getContainer().getInventoryCrafting().getStackInSlot(0) == null || this.getContainer().getInventoryCrafting().getStackInSlot(0) != this.lastInputStack) {
            this.lastInputStack = null;
        }

        if (this.lastInputStack != null && !this.crucibleRecipeList.isEmpty()) {
            CrucibleRecipe crucibleRecipe = (CrucibleRecipe)this.crucibleRecipeList.get(this.crucibleRecipeIndex);
            if (crucibleRecipe == null) {
                return;
            }

            this.drawRecipe(crucibleRecipe/*.getRecipeOutput()*/, mouseX, mouseY);
        } else {
            this.getContainer().getTerminal().getInventoryOutput().setInventorySlotContents(0, null);
        }

        if (this.tooltip != null) {
            UtilGui.drawCustomTooltip(this, new RenderItem(), this.fontRendererObj, (List)this.tooltip[0], (Integer)this.tooltip[1], (Integer)this.tooltip[2], (Integer)this.tooltip[3]);
            this.tooltip = null;
        }

    }


    public void initGui() {
        super.initGui();
        this.prevBtn = new NoTextureAEButton(this.guiLeft + 395, this.guiTop + 118, CustomAESetings.CRAFT_DIRECTION, Buttons.PAST);
        this.buttonList.add(this.prevBtn);
        this.nextBtn = new NoTextureAEButton(this.guiLeft + 411, this.guiTop + 121, CustomAESetings.CRAFT_DIRECTION, Buttons.NEXT);
        this.buttonList.add(this.nextBtn);

    }

    private void drawVis(CrucibleRecipe recipe, int mouseX, int mouseY) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        AspectList aspectList = recipe.aspects;
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

    private void drawRecipe(CrucibleRecipe recipe, int mouseX, int mouseY) {
        //CrucibleRecipe recipe = (CrucibleRecipe) ((ContainerPartAlchemicalConstructionPatternTerminal)getContainer()).getRecipes(getContainer().getInventoryCrafting().getStackInSlot(0)).get(0);
        if (recipe != null) {
            //if (simpleAreStacksEqual(stack, recipe.getRecipeOutput())) {
                if (((ContainerPartAlchemicalConstructionPatternTerminal) getContainer()).getOutputSlot().getStack() != null) {
                    this.drawVis(recipe, mouseX, mouseY);
                }
            //}
        }
    }

    private boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }

    @Override
    protected String getBackground() {
        return "gui/gui_terminal_thaumcraft_pattern_alchemy.png";
    }

}
