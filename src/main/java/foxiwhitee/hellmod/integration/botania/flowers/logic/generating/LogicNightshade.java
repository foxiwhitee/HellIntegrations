package foxiwhitee.hellmod.integration.botania.flowers.logic.generating;

import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.LevelTypes;

import java.util.Arrays;
import java.util.List;

public class LogicNightshade implements IGeneratingFlowerLogic {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicNightshadeTicks;
    }

    @Override
    public List<LevelTypes> getTypes() {
        return Arrays.asList(LevelTypes.ETERNAL_FOG, LevelTypes.FULL_MOON);
    }

    @Override
    public long getGenerating() {
        return FlowerLogicConfig.flowerLogicNightshadeGenerating;
    }

    @Override
    public int getLevel() {
        return FlowerLogicConfig.flowerLogicNightshadeLevel;
    }
}
