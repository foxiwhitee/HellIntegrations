package foxiwhitee.hellmod.tile;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.ModRecipes;
import foxiwhitee.hellmod.recipes.BaseAutoBlockRecipe;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TileAutoPress extends TileAutomatedBlock{
    @Override
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(ModBlocks.autoPress);
    }

    @Override
    protected List<BaseAutoBlockRecipe> getRecipes() {
        return ModRecipes.autoPressRecipes;
    }

    @Override
    protected long getMaxCount() {
        return HellConfig.speedAutoPress;
    }
}
