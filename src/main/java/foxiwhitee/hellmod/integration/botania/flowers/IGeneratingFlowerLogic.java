package foxiwhitee.hellmod.integration.botania.flowers;

import appeng.api.storage.data.IAEStack;

import java.util.ArrayList;
import java.util.List;

public interface IGeneratingFlowerLogic {
    int getDefaultNeedTicks();

    List<LevelTypes> getTypes();

    default void generate(IManaGenerator generator, int slot) {
        if (generator.consumeItem(slot)) {
            generator.setStoredMana(Math.min(generator.getMaxStoredMana(), generator.getStoredMana() + generator.calculateEffectsGenerating(getGenerating(), slot)));
        }
    }

    long getGenerating();

    default List<IAEStack> getConsumed() {
        return new ArrayList<>();
    }

    default int getLevel() {
        return 1;
    }

}