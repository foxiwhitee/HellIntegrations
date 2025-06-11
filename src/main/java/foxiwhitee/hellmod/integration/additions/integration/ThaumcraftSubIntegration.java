package foxiwhitee.hellmod.integration.additions.integration;

import appeng.api.AEApi;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxiwhitee.additions.ModItems;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static foxiwhitee.hellmod.ModRecipes.registerAutoPressRecipe;
import static thaumcraft.common.config.ConfigItems.itemResource;

@Integration(modid = "HellAdditions", dependencies = {"Thaumcraft"})
public class ThaumcraftSubIntegration implements IIntegration {

    public void preInit(FMLPreInitializationEvent e) {}

    public void init(FMLInitializationEvent e) {
        if (ModItems.ITEMS.containsKey("AE_CPU_Thaum") && ModItems.ITEMS.containsKey("AEContThaum")) {
            registerAutoPressRecipe(new ItemStack(ModItems.ITEMS.get("AE_CPU_Thaum")),
                    new ItemStack(ModItems.ITEMS.get("AEContThaum")),
                    AEApi.instance().definitions().materials().siliconPrint().maybeStack(1).get(),
                    new ItemStack(Items.redstone));
            registerAutoPressRecipe(new ItemStack(ModItems.ITEMS.get("AEContThaum")),
                    new ItemStack(itemResource, 1, 2));
        }
        if (ModItems.ITEMS.containsKey("AE_CPU_Void") && ModItems.ITEMS.containsKey("AEContVoid")) {
            registerAutoPressRecipe(new ItemStack(ModItems.ITEMS.get("AE_CPU_Void")),
                    new ItemStack(ModItems.ITEMS.get("AEContVoid")),
                    AEApi.instance().definitions().materials().siliconPrint().maybeStack(1).get(),
                    new ItemStack(Items.redstone));
            registerAutoPressRecipe(new ItemStack(ModItems.ITEMS.get("AEContVoid")),
                    new ItemStack(itemResource, 1, 16));
        }

    }

    public void postInit(FMLPostInitializationEvent e) {}
}
