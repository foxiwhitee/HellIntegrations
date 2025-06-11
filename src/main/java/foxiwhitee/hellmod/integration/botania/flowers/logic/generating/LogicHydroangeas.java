package foxiwhitee.hellmod.integration.botania.flowers.logic.generating;

import appeng.api.storage.data.IAEStack;
import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.LevelTypes;
import foxiwhitee.hellmod.items.ItemFluidDrop;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogicHydroangeas implements IGeneratingFlowerLogic {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicHydroangeasTicks;
    }

    @Override
    public List<LevelTypes> getTypes() {
        return Arrays.asList(LevelTypes.CRYSTAL, LevelTypes.ETERNAL_FOG);
    }

    @Override
    public long getGenerating() {
        return FlowerLogicConfig.flowerLogicHydroangeasGenerating;
    }

    @Override
    public List<IAEStack> getConsumed() {
        return new ArrayList<>(Arrays.asList(ItemFluidDrop.newAeStack(new FluidStack(FluidRegistry.WATER, 1))));
    }

    @Override
    public int getLevel() {
        return FlowerLogicConfig.flowerLogicHydroangeasLevel;
    }
}
