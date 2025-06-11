package foxiwhitee.hellmod.integration.draconic.tile;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModRecipes;
import foxiwhitee.hellmod.helpers.IAutomatedBlockUpgrade;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.items.ItemArialUpgrade;
import foxiwhitee.hellmod.integration.draconic.items.ItemChaosUpgrade;
import foxiwhitee.hellmod.integration.draconic.items.ItemDragonUpgrade;
import foxiwhitee.hellmod.recipes.BaseAutoBlockRecipe;
import foxiwhitee.hellmod.tile.TileAutomatedUpgradeableBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TileAutoAwakenedBlocks extends TileAutomatedUpgradeableBlock {

    @Override
    protected String[] getStatuses() {
        return new String[]{"dragon", "chaos", "arial"};
    }

    @Override
    protected Class<? extends IAutomatedBlockUpgrade>[] getUpgrades() {
        return new Class[]{ItemDragonUpgrade.class, ItemChaosUpgrade.class, ItemArialUpgrade.class};
    }

    @Override
    protected List<BaseAutoBlockRecipe>[] getCrafts() {
        return new List[]{ModRecipes.autoAwakenedBlocksDragonRecipes, ModRecipes.autoAwakenedBlocksChaosRecipes, ModRecipes.autoAwakenedBlocksArialRecipes};
    }

    @Override
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(DraconicEvolutionIntegration.autoAwakener);
    }

    @Override
    protected long getMaxCount() {
        return HellConfig.speedAutoAwakenedBlocks;
    }
}