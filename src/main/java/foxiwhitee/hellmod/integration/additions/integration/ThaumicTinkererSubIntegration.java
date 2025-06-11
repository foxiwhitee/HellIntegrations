package foxiwhitee.hellmod.integration.additions.integration;

import appeng.api.AEApi;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxiwhitee.additions.ModItems;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static foxiwhitee.hellmod.ModRecipes.registerAutoPressRecipe;

@Integration(modid = "HellAdditions", dependencies = {"ThaumicTinkerer"})
public class ThaumicTinkererSubIntegration implements IIntegration {

    public void preInit(FMLPreInitializationEvent e) {}

    public void init(FMLInitializationEvent e) {
        if (ModItems.ITEMS.containsKey("AE_CPU_Ihor") && ModItems.ITEMS.containsKey("AEContIhor")) {
            registerAutoPressRecipe(new ItemStack(ModItems.ITEMS.get("AE_CPU_Ihor")),
                    new ItemStack(ModItems.ITEMS.get("AEContIhor")),
                    AEApi.instance().definitions().materials().siliconPrint().maybeStack(1).get(),
                    new ItemStack(Items.redstone));
            registerAutoPressRecipe(new ItemStack(ModItems.ITEMS.get("AEContIhor")),
                    new ItemStack((Item) Item.itemRegistry.getObject("ThaumicTinkerer:kamiResourse")));
        }
    }

    public void postInit(FMLPostInitializationEvent e) {}
}
