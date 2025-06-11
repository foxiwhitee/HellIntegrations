package foxiwhitee.hellmod.integration;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IIntegration {
    void preInit(FMLPreInitializationEvent paramFMLPreInitializationEvent);

    void init(FMLInitializationEvent paramFMLInitializationEvent);

    void postInit(FMLPostInitializationEvent paramFMLPostInitializationEvent);

    default boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }
}
