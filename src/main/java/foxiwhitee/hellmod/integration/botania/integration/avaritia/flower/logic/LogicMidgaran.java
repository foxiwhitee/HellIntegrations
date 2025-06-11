package foxiwhitee.hellmod.integration.botania.integration.avaritia.flower.logic;

import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.IManaGenerator;
import foxiwhitee.hellmod.integration.botania.flowers.LevelTypes;

import java.util.Arrays;
import java.util.List;

public class LogicMidgaran implements IGeneratingFlowerLogic {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicMidgaranTicks;
    }

    @Override
    public List<LevelTypes> getTypes() {
        return Arrays.asList(LevelTypes.ASGARD);
    }

    @Override
    public long getGenerating() {
        return HellConfig.midgaranGeneration;
    }

    @Override
    public int getLevel() {
        return FlowerLogicConfig.flowerLogicMidgaranLevel;
    }

    @Override
    public void generate(IManaGenerator generator, int slot) {
        generator.setStoredMana(Math.min(generator.getMaxStoredMana(), generator.getStoredMana() + generator.calculateEffectsGenerating(getGenerating(), slot)));
    }
}
