package foxiwhitee.hellmod.integration.nei.draconic.fusion;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.Timer;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.ModRecipes;
import foxiwhitee.hellmod.integration.draconic.client.gui.GuiFusionCraftingCore;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.GuiEffectRenderer;
import foxiwhitee.hellmod.integration.draconic.helpers.gui.accessors.IMinecraftAccessor;
import foxiwhitee.hellmod.integration.draconic.recipes.FusionRecipe;
import foxiwhitee.hellmod.utils.helpers.EnergyUtility;
import foxiwhitee.hellmod.utils.helpers.OreDictUtil;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiFusionCraftingHandler extends TemplateRecipeHandler {
    @SideOnly(Side.CLIENT)
    private final FontRenderer fontRender = (Minecraft.getMinecraft()).fontRenderer;

    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(73, 45, 19, 21), "fusioncrafting", new Object[0]));
    }

    public int recipiesPerPage() {
        return 2;
    }

    public Class<? extends GuiContainer> getGuiClass() {
        return (Class) GuiFusionCraftingCore.class;
    }

    public String getRecipeName() {
        return LocalizationUtils.localize("crafting.fusioncrafting");
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("fusioncrafting") && getClass() == GuiFusionCraftingHandler.class) {
            for (FusionRecipe recipe : ModRecipes.fusionRecipes) {
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
        for (FusionRecipe recipe : ModRecipes.fusionRecipes) {
            if (safeOre(recipe) && NEIServerUtils.areStacksSameTypeCrafting(recipe.getOut(), result)) {
                CachedCompression r = new CachedCompression(recipe);
                r.computeVisuals();
                this.arecipes.add(r);
            }
        }
    }

    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("fusioncrafting") && getClass() == GuiFusionCraftingHandler.class) {
            loadCraftingRecipes("fusioncrafting", new Object[0]);
        } else {
            super.loadUsageRecipes(inputId, ingredients);
        }
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient == null)
            return;
        for (FusionRecipe recipe : ModRecipes.fusionRecipes) {
            if (safeOre(recipe) && ingredient.isItemEqual(recipe.getRecipeCatalyst())) {
                CachedCompression r = new CachedCompression(recipe);
                this.arecipes.add(r);
            }
        }
        for (FusionRecipe fusionRecipe : ModRecipes.fusionRecipes) {
            if (fusionRecipe.isRecipeCatalyst(ingredient) || fusionRecipe.isIngredient(fusionRecipe.getInputs(), ingredient))
                this.arecipes.add(new CachedCompression(fusionRecipe));
        }
    }

    private boolean safeOre(FusionRecipe recipe) {
        if (!(recipe instanceof FusionRecipe))
            return true;
        return (recipe.getRecipeCatalyst() != null);
    }

    public String getGuiTexture() {
        return HellCore.MODID + ":textures/gui/fusioncrafting_nei.png";
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        switch (((CachedCompression)this.arecipes.get(recipe)).getTier()) {
            case 0:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.fusion.tier") + ": " + LocalizationUtils.localize("tooltip.fusion.tier0"), 83, 5, 5263615);
                break;
            case 1:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.fusion.tier") + ": " + LocalizationUtils.localize("tooltip.fusion.tier1"), 83, 5, 8388863);
                break;
            case 2:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.fusion.tier") + ": " + LocalizationUtils.localize("tooltip.fusion.tier2"), 83, 5, 16737792);
                break;
            case 3:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.fusion.tier") + ": " + LocalizationUtils.localize("tooltip.fusion.tier3"), 83, 5, 5263440);
                break;
            case 4:
                GuiDraw.drawStringC(LocalizationUtils.localize("tooltip.fusion.tier") + ": " + LocalizationUtils.localize("tooltip.fusion.tier4"), 83, 5, 16392940);
                break;
        }
        long energy = ((CachedCompression)this.arecipes.get(recipe)).getCost();
        long energyAll = ((CachedCompression)this.arecipes.get(recipe)).getCost() * ((TemplateRecipeHandler.CachedRecipe)this.arecipes.get(recipe)).getIngredients().size();
        WorldClient world = (Minecraft.getMinecraft()).theWorld;
        GuiDraw.drawStringC(EnumChatFormatting.BLUE + LocalizationUtils.localize("tooltip.DraconicAssembler.info.energyCost"), 83, 90, 0);
        GuiDraw.drawStringC(EnumChatFormatting.AQUA + "" + EnergyUtility.formatNumber(energy) + "RF", 83, 100, 0);
    }

    public String getOverlayIdentifier() {
        return "fusioncrafting";
    }

    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 256, 208);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
        ((CachedCompression)this.arecipes.get(recipe)).drawAnimations(Minecraft.getMinecraft(), 0, 0);
    }

    public class CachedCompression extends TemplateRecipeHandler.CachedRecipe {
        private final int count;

        private final long cost;

        private final int tier;

        private int lastTick = 0;

        List<PositionedStack> ingredList = new ArrayList<>();

        PositionedStack ingred;

        PositionedStack result;

        private int xSize = 162;

        private int ySize = 94;

        FusionRecipe recipe;

        private GuiEffectRenderer effectRenderer;

        public CachedCompression(FusionRecipe recipe) {
            this.effectRenderer = new GuiEffectRenderer();
            this.recipe = recipe;
            this.ingred = new PositionedStack(recipe.getRecipeCatalyst(), 74, 27);
            this.result = new PositionedStack(recipe.getOut(), 74, 68);
            this.ingredList.add(new PositionedStack(recipe.getRecipeCatalyst(), 74, 27));
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
                    int ySize = 80 / Math.max(sideCount - (isOdd ? 0 : 1), 1);
                    int sideIndex = i / 2;
                    if (sideCount <= 1 && (!isOdd || ingredients.size() == 1)) {
                        sideIndex = 1;
                        ySize = 40;
                    }
                    yPos = centerY - 40 + sideIndex * ySize;
                } else {
                    xPos = centerX + 51;
                    int ySize = 80 / Math.max(sideCount - (isOdd ? 0 : 1), 1);
                    int sideIndex = i / 2;
                    if (isOdd)
                        sideCount++;
                    if (sideCount <= 1) {
                        sideIndex = 1;
                        ySize = 40;
                    }
                    yPos = centerY - 40 + sideIndex * ySize;
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
            this.cost = recipe.getIngredientEnergyCost();
            this.tier = recipe.getRecipeTier();
        }

        public List<PositionedStack> getIngredients() {
            return this.ingredList;
        }

        public PositionedStack getResult() {
            return this.result;
        }

        public PositionedStack getIngredient() {
            return this.ingred;
        }

        public List<PositionedStack> getOtherStacks() {
            return this.ingredList;
        }

        public void computeVisuals() {
            this.ingred.generatePermutations();
        }

        public long getCost() {
            return this.cost;
        }

        public int getTier() {
            return this.tier;
        }

        public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {
            if (ClientEventHandler.elapsedTicks != this.lastTick) {
                this.lastTick = ClientEventHandler.elapsedTicks;
                tick();
            }

            try {
                Field timerField = Minecraft.class.getDeclaredField("timer");
                timerField.setAccessible(true);
                Timer timer = (Timer) timerField.get(Minecraft.getMinecraft());
                this.effectRenderer.renderEffects(timer.renderPartialTicks);
            } catch (Exception e) {
                e.printStackTrace();
                this.effectRenderer.renderEffects(0);
            }
        }

        public void tick() {
            this.effectRenderer.updateEffects();
            WorldClient worldClient = (Minecraft.getMinecraft()).theWorld;
            if (worldClient != null) {
                List ingredients = this.recipe.getInputs();
                int centerX = this.xSize / 2;
                int centerY = this.ySize / 2;
                for (int i = 0; i < ingredients.size(); i++) {
                    int j, yPos;
                    boolean isLeft = (i % 2 == 0);
                    boolean isOdd = (ingredients.size() % 2 == 1);
                    int sideCount = ingredients.size() / 2;
                    if (isOdd && !isLeft)
                        sideCount--;
                    if (isLeft) {
                        j = centerX - 65;
                        int ySize = 80 / Math.max(sideCount - (isOdd ? 0 : 1), 1);
                        int sideIndex = i / 2;
                        if (sideCount <= 1 && (!isOdd || ingredients.size() == 1)) {
                            sideIndex = 1;
                            ySize = 40;
                        }
                        yPos = centerY - 40 + sideIndex * ySize;
                    } else {
                        j = centerX + 65;
                        int ySize = 80 / Math.max(sideCount - (isOdd ? 0 : 1), 1);
                        int sideIndex = i / 2;
                        if (isOdd)
                            sideCount++;
                        if (sideCount <= 1) {
                            sideIndex = 1;
                            ySize = 40;
                        }
                        yPos = centerY - 40 + sideIndex * ySize;
                    }
                    if (((World)worldClient).rand.nextInt(10) == 0) {
                        j = (int)(j + -8.0D + ((World)worldClient).rand.nextDouble() * 16.0D);
                        yPos = (int)(yPos + -8.0D + ((World)worldClient).rand.nextDouble() * 16.0D);
                        double ty = centerY + -20.0D + ((World)worldClient).rand.nextDouble() * 40.0D;
                        this.effectRenderer.addEffect(new GuiFusionCraftingCore.EnergyEffect((World)(Minecraft.getMinecraft()).theWorld, j, yPos, centerX, ty, 0));
                    }
                }
                double xPos = (centerX - 8) + ((World)worldClient).rand.nextDouble() * 16.0D;
                double yTop = 27.0D + ((World)worldClient).rand.nextDouble() * 16.0D;
                this.effectRenderer.addEffect(new GuiFusionCraftingCore.EnergyEffect((World)(Minecraft.getMinecraft()).theWorld, xPos, yTop, (centerX + 2), 78.0D, 1));
                this.effectRenderer.updateEffects();
            } else {
                this.effectRenderer.clearEffects();
            }
        }
    }
}
