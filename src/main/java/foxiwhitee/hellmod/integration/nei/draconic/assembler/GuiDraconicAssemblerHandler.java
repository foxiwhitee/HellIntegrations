package foxiwhitee.hellmod.integration.nei.draconic.assembler;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.ModRecipes;
import foxiwhitee.hellmod.integration.draconic.client.gui.GuiDraconicAssembler;
import foxiwhitee.hellmod.integration.draconic.recipes.DraconicAssemblerRecipe;
import foxiwhitee.hellmod.utils.helpers.EnergyUtility;
import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class GuiDraconicAssemblerHandler extends TemplateRecipeHandler {
    @SideOnly(Side.CLIENT)
    private final FontRenderer fontRender = (Minecraft.getMinecraft()).fontRenderer;

    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(40, 3, 84, 17), "drassembler", new Object[0]));
    }

    public int recipiesPerPage() {
        return 2;
    }

    public Class<? extends GuiContainer> getGuiClass() {
        return (Class) GuiDraconicAssembler.class;
    }

    public String getRecipeName() {
        return LocalizationUtils.localize("crafting.drassembler");
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("drassembler") && getClass() == GuiDraconicAssemblerHandler.class) {
            for (DraconicAssemblerRecipe recipe : ModRecipes.draconicAssemblerRecipes) {
                if (safeOre(recipe)) {
                    CachedCompression r = new CachedCompression(recipe);
                    r.computeVisuals();
                    this.arecipes.add(r);
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    public void loadCraftingRecipes(ItemStack result) {
        for (DraconicAssemblerRecipe recipe :  ModRecipes.draconicAssemblerRecipes) {
            if (safeOre(recipe) && NEIServerUtils.areStacksSameTypeCrafting(recipe.getOut(), result)) {
                CachedCompression r = new CachedCompression(recipe);
                r.computeVisuals();
                this.arecipes.add(r);
            }
        }
    }

    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("drassembler") && getClass() == GuiDraconicAssemblerHandler.class) {
            loadCraftingRecipes("drassembler", new Object[0]);
        } else {
            super.loadUsageRecipes(inputId, ingredients);
        }
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient == null)
            return;
        for (DraconicAssemblerRecipe recipe :  ModRecipes.draconicAssemblerRecipes) {
            if (safeOre(recipe)) {
                for (Object o : recipe.getInputs()) {
                    if (OreDictUtil.areStacksEqual(o, ingredient)) {
                        CachedCompression r = new CachedCompression(recipe);
                        this.arecipes.add(r);
                    }
                }
            }
        }
    }

    private boolean safeOre(DraconicAssemblerRecipe recipe) {
        if (!(recipe instanceof DraconicAssemblerRecipe))
            return true;
        return (recipe.getInputs() != null);
    }

    public String getGuiTexture() {
        return HellCore.MODID + ":textures/gui/gui_nei_draconic_assembler.png";
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        switch (((CachedCompression)this.arecipes.get(recipe)).getTier()) {
            case 0:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.DraconicAssembler.tier") + ": " + LocalizationUtils.localize("tooltip.DraconicAssembler.tier0"), 83, 5, 5263615);
                break;
            case 1:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.DraconicAssembler.tier") + ": " + LocalizationUtils.localize("tooltip.DraconicAssembler.tier1"), 83, 5, 8388863);
                break;
            case 2:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.DraconicAssembler.tier") + ": " + LocalizationUtils.localize("tooltip.DraconicAssembler.tier2"), 83, 5, 16737792);
                break;
            case 3:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.DraconicAssembler.tier") + ": " + LocalizationUtils.localize("tooltip.DraconicAssembler.tier3"), 83, 5, 5263440);
                break;
            case 4:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.DraconicAssembler.tier") + ": " + LocalizationUtils.localize("tooltip.DraconicAssembler.tier4"), 83, 5, 16392940);
                break;
        }
        long energy = ((CachedCompression)this.arecipes.get(recipe)).getCost();
        GuiDraw.drawStringC(EnumChatFormatting.BLUE + LocalizationUtils.localize("tooltip.DraconicAssembler.info.energyCost"), 83, 90, 0);
        GuiDraw.drawStringC(EnumChatFormatting.AQUA + "" + EnergyUtility.formatNumber(energy) + "RF", 83, 100, 0);
    }

    public String getOverlayIdentifier() {
        return "drassembler";
    }

    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 256, 208);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
    }

    public class CachedCompression extends TemplateRecipeHandler.CachedRecipe {
        private final int count;
        private final long cost;
        private final int tier;
        List<PositionedStack> ingredList = new ArrayList<>();
        PositionedStack result;
        private int xSize = 162;
        private int ySize = 94;
        DraconicAssemblerRecipe recipe;

        public CachedCompression(DraconicAssemblerRecipe recipe) {
            super();
            this.recipe = recipe;
            this.result = new PositionedStack(recipe.getOut(), 74, 49);
            int size = recipe.getInputs().size();
            int j = size % 2;
            if (j != 0) {
                this.count = size / 2 + 1;
            } else {
                this.count = size / 2;
            }
            int times = 0;
            this.xSize = 162;
            this.ySize = 94;
            int centerX = this.xSize / 2;
            int centerY = this.ySize / 2;
            for (int i = 0; i < recipe.getInputs().size(); i++) {
                int xPos, yPos;
                List ingredients = recipe.getInputs();
                Object o = recipe.getInputs().get(i);
                boolean isLeft = (i % 2 == 0);
                boolean isOdd = (ingredients.size() % 2 == 1);
                int sideCount = ingredients.size() / 2;
                if (isOdd && !isLeft)
                    sideCount--;
                if (isLeft) {
                    xPos = centerX - 65;
                    int ySize = 33 / Math.max(sideCount - (isOdd ? 0 : 1), 1);
                    int sideIndex = i / 2;
                    if (sideCount <= 1 && (!isOdd || ingredients.size() == 1)) {
                        sideIndex = 1;
                        ySize = 18;
                    }
                    yPos = centerY - 23 + sideIndex * ySize;
                } else {
                    xPos = centerX + 51;
                    int ySize = 33 / Math.max(sideCount - (isOdd ? 0 : 1), 1);
                    int sideIndex = i / 2;
                    if (isOdd)
                        sideCount++;
                    if (sideCount <= 1) {
                        sideIndex = 1;
                        ySize = 18;
                    }
                    yPos = centerY - 23 + sideIndex * ySize;
                }
                if (o != null) {
                    if (o instanceof Item) {
                        this.ingredList.add(new PositionedStack(new ItemStack((Item)o), xPos, yPos));
                    } else if (o instanceof Block) {
                        this.ingredList.add(new PositionedStack(new ItemStack((Block)o), xPos, yPos));
                    } else if (o instanceof ItemStack) {
                        this.ingredList.add(new PositionedStack(o, xPos, yPos));
                    } else if (o instanceof String) {
                        this.ingredList.add(new PositionedStack(Objects.requireNonNull(OreDictUtil.resolveObject(o)), xPos, yPos));
                    }
                    times++;
                }
            }
            this.cost = recipe.getEnergy();
            this.tier = recipe.getLevel();
        }

        public List<PositionedStack> getIngredients() {
            return this.ingredList;
        }

        public PositionedStack getResult() {
            return this.result;
        }

        public List<PositionedStack> getOtherStacks() {
            return this.ingredList;
        }

        public void computeVisuals() {
            for (PositionedStack stack : this.ingredList) {
                stack.generatePermutations();
            }
        }

        public long getCost() {
            return this.cost;
        }

        public int getTier() {
            return this.tier;
        }
    }
}
