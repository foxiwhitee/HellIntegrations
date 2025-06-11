package foxiwhitee.hellmod.integration.botania.flowers.logic.generating;

import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.IManaGenerator;
import foxiwhitee.hellmod.integration.botania.flowers.LevelTypes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.Arrays;
import java.util.List;

public class LogicMunchdew implements IGeneratingFlowerLogic {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicMunchdewTicks;
    }

    @Override
    public List<LevelTypes> getTypes() {
        return Arrays.asList(LevelTypes.YGGDRASIL, LevelTypes.DESTRUCTIVE);
    }

    @Override
    public long getGenerating() {
        return FlowerLogicConfig.flowerLogicMunchdewGenerating;
    }

    @Override
    public int getLevel() {
        return FlowerLogicConfig.flowerLogicMunchdewLevel;
    }

    @Override
    public void generate(IManaGenerator generator, int slot) {
        if (Block.getBlockFromItem(generator.getStackInSLot(slot).getItem()).getMaterial() == Material.leaves) {
            IGeneratingFlowerLogic.super.generate(generator, slot);
        }
    }
}
