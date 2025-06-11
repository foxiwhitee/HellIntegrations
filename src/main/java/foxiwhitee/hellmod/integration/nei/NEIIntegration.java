package foxiwhitee.hellmod.integration.nei;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;

@Integration(modid = "NotEnoughItems")
public class NEIIntegration implements IIntegration {
    public void preInit(FMLPreInitializationEvent e) {}

    public void init(FMLInitializationEvent e) {
        if (isClient())
            clientInit();
    }

    @SideOnly(Side.CLIENT)
    public void clientInit() {
        (new NEIHellConfig()).loadConfig();
    }

    public void postInit(FMLPostInitializationEvent e) {}
}