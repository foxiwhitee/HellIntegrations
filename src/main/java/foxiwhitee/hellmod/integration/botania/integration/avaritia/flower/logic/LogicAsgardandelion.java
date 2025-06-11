package foxiwhitee.hellmod.integration.botania.integration.avaritia.flower.logic;

import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.IManaGenerator;
import foxiwhitee.hellmod.integration.botania.flowers.LevelTypes;

import java.util.Arrays;
import java.util.List;

public class LogicAsgardandelion implements IGeneratingFlowerLogic {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicAsgardandelionTicks;
    }

    @Override
    public List<LevelTypes> getTypes() {
        return Arrays.asList(LevelTypes.ASGARD);
    }

    @Override
    public long getGenerating() {
        return FlowerLogicConfig.flowerLogicAsgardandelionGenerating;
    }

    @Override
    public int getLevel() {
        return FlowerLogicConfig.flowerLogicAsgardandelionLevel;
    }

    @Override
    public void generate(IManaGenerator generator, int slot) {
        generator.setStoredMana(Math.min(generator.getMaxStoredMana(), generator.getStoredMana() + generator.calculateEffectsGenerating(getGenerating(), slot)));
    }
}
