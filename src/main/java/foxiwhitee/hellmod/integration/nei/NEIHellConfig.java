package foxiwhitee.hellmod.integration.nei;


import codechicken.nei.api.API;
import codechicken.nei.api.GuiInfo;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import codechicken.nei.recipe.TemplateRecipeHandler;
import com.brandon3055.draconicevolution.integration.nei.DENEIGuiHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.client.gui.GuiDraconicAssembler;
import foxiwhitee.hellmod.integration.draconic.client.gui.GuiFusionCraftingCore;
import foxiwhitee.hellmod.integration.nei.draconic.DraconicNEIGuiHandler;
import foxiwhitee.hellmod.integration.nei.draconic.assembler.DrAssemblerRecipeOverlayHandler;
import foxiwhitee.hellmod.integration.nei.draconic.assembler.GuiDraconicAssemblerHandler;
import foxiwhitee.hellmod.integration.nei.draconic.fusion.GuiFusionCraftingHandler;
import net.minecraft.item.ItemStack;
import foxiwhitee.hellmod.HellCore;
import net.minecraft.nbt.NBTTagCompound;
import java.awt.Rectangle;

public class NEIHellConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {
        new ArrayList<TemplateRecipeHandler>(Arrays.asList(new GuiDraconicAssemblerHandler(), new GuiFusionCraftingHandler()))
                .forEach(handler -> {
                    API.registerRecipeHandler((ICraftingHandler)handler);
                    API.registerUsageHandler((IUsageHandler)handler);
                });

        API.registerGuiOverlay(GuiDraconicAssembler.class, "drassembler", 5, 11);
        GuiInfo.customSlotGuis.add(GuiDraconicAssembler.class);
        API.addRecipeCatalyst(new ItemStack(DraconicEvolutionIntegration.draconicAssembler), "drassembler");
        API.addRecipeCatalyst(new ItemStack(DraconicEvolutionIntegration.fusion_core), "fusioncrafting");
        API.addRecipeCatalyst(new ItemStack(DraconicEvolutionIntegration.fusion_injector, 1, 0), "fusioncrafting");
        API.addRecipeCatalyst(new ItemStack(DraconicEvolutionIntegration.fusion_injector, 1, 1), "fusioncrafting");
        API.addRecipeCatalyst(new ItemStack(DraconicEvolutionIntegration.fusion_injector, 1, 2), "fusioncrafting");
        API.addRecipeCatalyst(new ItemStack(DraconicEvolutionIntegration.fusion_injector, 1, 3), "fusioncrafting");
        API.addRecipeCatalyst(new ItemStack(DraconicEvolutionIntegration.fusion_injector, 1, 4), "fusioncrafting");
        API.registerGuiOverlayHandler(GuiDraconicAssembler.class, new DrAssemblerRecipeOverlayHandler(), "drassembler");
        sendHandlerInfo("foxiwhitee.hellmod.integration.nei.draconic.assembler.GuiDraconicAssemblerHandler", DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName().replace("tile.", ""), 112, 164, 2);
        sendHandlerInfo("foxiwhitee.hellmod.integration.nei.draconic.fusion.GuiFusionCraftingHandler", DraconicEvolutionIntegration.fusion_core.getUnlocalizedName().replace("tile.", ""), 112, 164, 2);
        TemplateRecipeHandler.RecipeTransferRectHandler.registerRectsToGuis(Arrays.asList(GuiFusionCraftingCore.class ), Collections.singletonList(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(98, 63, 11, 31), "fusioncrafting", new Object[0])));
        API.registerNEIGuiHandler(new DraconicNEIGuiHandler());
    }

    private void sendHandlerInfo(String handler, String nameItem, int height, int width, int perPage) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("handler", handler);
        tag.setString("modName", HellCore.MODNAME);
        tag.setString("modId", HellCore.MODID);
        tag.setString("itemName", HellCore.MODID + ":" + nameItem);
        tag.setInteger("handlerHeight", height);
        tag.setInteger("handlerWidth", width);
        tag.setInteger("maxRecipesPerPage", perPage);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", tag);
    }

    @Override
    public String getName() {
        return HellCore.MODNAME;
    }

    @Override
    public String getVersion() {
        return HellCore.VERSION;
    }
}
