package foxiwhitee.hellmod.integration.botania.flowers.logic.generating;

import appeng.api.storage.data.IAEStack;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.LevelTypes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogicKekimurus implements IGeneratingFlowerLogic {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicKekimurusTicks;
    }

    @Override
    public List<LevelTypes> getTypes() {
        return Arrays.asList(LevelTypes.DESTRUCTIVE);
    }

    @Override
    public long getGenerating() {
        return FlowerLogicConfig.flowerLogicKekimurusGenerating;
    }

    @Override
    public List<IAEStack> getConsumed() {
        return new ArrayList<>(Arrays.asList(AEItemStack.create(new ItemStack(Items.cake))));
    }

    @Override
    public int getLevel() {
        return FlowerLogicConfig.flowerLogicKekimurusLevel;
    }
}
