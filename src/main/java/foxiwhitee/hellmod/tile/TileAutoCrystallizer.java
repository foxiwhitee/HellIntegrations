package foxiwhitee.hellmod.tile;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.ModRecipes;
import foxiwhitee.hellmod.recipes.BaseAutoBlockRecipe;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TileAutoCrystallizer extends TileAutomatedBlock{
    @Override
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(ModBlocks.autoCrystallizer);
    }

    @Override
    protected List<BaseAutoBlockRecipe> getRecipes() {
        return ModRecipes.autoCrystallizerRecipes;
    }

    @Override
    protected long getMaxCount() {
        return HellConfig.speedAutoCrystallizer;
    }
}
