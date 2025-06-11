package foxiwhitee.hellmod.integration.blood;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.integration.IIntegration;
import foxiwhitee.hellmod.integration.Integration;
import foxiwhitee.hellmod.integration.blood.blocks.BlockIchorRune;
import foxiwhitee.hellmod.integration.blood.blocks.BlockSingularRune;
import foxiwhitee.hellmod.integration.blood.itemblock.ItemBlockRune;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;

@Integration(modid = "AWWayofTime")
public class BloodMagicIntegration implements IIntegration {
    BlockIchorRune ichor_runes = new BlockIchorRune("ichor_runes");
    BlockSingularRune singular_runes = new BlockSingularRune("singular_runes");

    public void preInit(FMLPreInitializationEvent e) {
        RegisterUtils.registerBlocks(ItemBlockRune.class, ichor_runes, singular_runes);
    }

    public void init(FMLInitializationEvent e) {
        if (isClient())
            clientInit();
    }

    @SideOnly(Side.CLIENT)
    public void clientInit() {
    }

    public void postInit(FMLPostInitializationEvent e) {}
}
