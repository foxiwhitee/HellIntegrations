package foxiwhitee.hellmod.integration.botania.integration.open;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import foxiwhitee.hellmod.integration.botania.integration.open.items.ItemGenerationFlowerCoresOpenBLocks;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.item.Item;

@Integration(modid = "Botania", dependencies = {"OpenBlocks"})
public class OpenBlocksSubIntegration implements IIntegration {

    public static Item generationFlowerCoresOpenBlocks = new ItemGenerationFlowerCoresOpenBLocks("generationFlowerCoresOpenBlocks");

    public void preInit(FMLPreInitializationEvent e) {
        RegisterUtils.registerItem(generationFlowerCoresOpenBlocks);
    }

    public void init(FMLInitializationEvent e) {}

    public void postInit(FMLPostInitializationEvent e) {}

}
