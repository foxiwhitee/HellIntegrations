package foxiwhitee.hellmod.integration.botania.integration.avaritia;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import foxiwhitee.hellmod.integration.botania.integration.avaritia.items.ItemGenerationFlowerCoresAvaritia;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.item.Item;

@Integration(modid = "Botania", dependencies = {"Avaritia"})
public class AvaritiaSubIntegration implements IIntegration {

    public static Item generationFlowerCoresAvaritia = new ItemGenerationFlowerCoresAvaritia("generationFlowerCoresAvaritia");

    public void preInit(FMLPreInitializationEvent e) {
        RegisterUtils.registerItem(generationFlowerCoresAvaritia);
    }

    public void init(FMLInitializationEvent e) {}

    public void postInit(FMLPostInitializationEvent e) {}

}
