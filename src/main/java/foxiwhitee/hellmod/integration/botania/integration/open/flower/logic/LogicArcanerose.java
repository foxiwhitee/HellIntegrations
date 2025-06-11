package foxiwhitee.hellmod.integration.botania.integration.open.flower.logic;

import appeng.api.storage.data.IAEStack;
import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.LevelTypes;
import foxiwhitee.hellmod.items.ItemFluidDrop;
import net.minecraftforge.fluids.FluidStack;
import openblocks.OpenBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogicArcanerose implements IGeneratingFlowerLogic {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicArcaneroseTicks;
    }

    @Override
    public List<LevelTypes> getTypes() {
        return Arrays.asList(LevelTypes.YGGDRASIL);
    }

    @Override
    public long getGenerating() {
        return FlowerLogicConfig.flowerLogicArcaneroseGenerating;
    }

    @Override
    public int getLevel() {
        return FlowerLogicConfig.flowerLogicArcaneroseLevel;
    }

    @Override
    public List<IAEStack> getConsumed() {
        return new ArrayList<>(Arrays.asList(ItemFluidDrop.newAeStack(new FluidStack(OpenBlocks.Fluids.xpJuice, 1))));
    }
}
