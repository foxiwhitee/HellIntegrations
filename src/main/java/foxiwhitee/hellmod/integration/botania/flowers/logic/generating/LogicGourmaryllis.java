package foxiwhitee.hellmod.integration.botania.flowers.logic.generating;

import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.IManaGenerator;
import foxiwhitee.hellmod.integration.botania.flowers.LevelTypes;
import net.minecraft.item.ItemFood;

import java.util.Arrays;
import java.util.List;

public class LogicGourmaryllis implements IGeneratingFlowerLogic {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicGourmaryllisTicks;
    }

    @Override
    public List<LevelTypes> getTypes() {
        return Arrays.asList(LevelTypes.YGGDRASIL, LevelTypes.CRYSTAL);
    }

    @Override
    public long getGenerating() {
        return 0;
    }

    @Override
    public int getLevel() {
        return FlowerLogicConfig.flowerLogicGourmaryllisLevel;
    }

    @Override
    public void generate(IManaGenerator generator, int slot) {
        if(generator.getStackInSLot(slot).getItem() instanceof ItemFood) {
            if (generator.consumeItem(slot)) {
                long food = ((ItemFood)generator.getStackInSLot(slot).getItem()).func_150905_g(generator.getStackInSLot(slot));
                generator.setStoredMana(Math.min(generator.getMaxStoredMana(), generator.getStoredMana() + generator.calculateEffectsGenerating(food * food * 64, slot)));
            }
        }

    }
}
