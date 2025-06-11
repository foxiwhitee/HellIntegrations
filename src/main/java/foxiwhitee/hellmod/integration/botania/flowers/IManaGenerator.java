package foxiwhitee.hellmod.integration.botania.flowers;

import net.minecraft.item.ItemStack;

public interface IManaGenerator {
    boolean consumeItem(int slot);
    long getMaxStoredMana();
    void setStoredMana(long storedMana);
    long getStoredMana();
    long calculateEffectsGenerating(long generating, int slot);
    ItemStack getStackInSLot(int slot);
}
