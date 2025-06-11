package foxiwhitee.hellmod.integration.avaritia.tile.collectors;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import foxiwhitee.hellmod.integration.avaritia.utils.NeutronCollectorsOutput;
import net.minecraft.item.ItemStack;

public class TileUltimateNeutronCollector extends TileCustomNeutronCollector{
    @Override
    public String getName() {
        return AvaritiaIntegration.ultimateNeutronCollector.getUnlocalizedName().replace("tile.", "");
    }

    @Override
    public int getTicks() {
        return HellConfig.ultTicks;
    }

    @Override
    public ItemStack getStack() {
        return NeutronCollectorsOutput.ULTIMATE.getStack();
    }
}
