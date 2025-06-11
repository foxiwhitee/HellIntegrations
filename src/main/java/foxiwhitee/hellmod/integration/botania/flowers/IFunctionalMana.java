package foxiwhitee.hellmod.integration.botania.flowers;

import appeng.util.item.AEItemStack;

public interface IFunctionalMana {
    long getMaxStoredMana();
    void setStoredMana(long storedMana);
    long getStoredMana();
    void injectNewItems(AEItemStack stack);
    boolean extractNeedItems(AEItemStack stack);


    default void consumeMana(long value) {
        setStoredMana(Math.max(0, getMaxStoredMana() - value));
    }
}
