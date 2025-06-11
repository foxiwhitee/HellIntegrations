package foxiwhitee.hellmod.me;

import net.minecraft.item.Item;

import java.util.HashSet;
import java.util.Set;

public final class ItemDisplayBlackList {
    private static final boolean isOn = true;

    private static final Set<Class<? extends Item>> blacklistedItemClasses = new HashSet<>();

    private static final Set<Item> blacklistedItems = new HashSet<>();

    public static void blacklistItemDisplay(Item item) {
        blacklistedItems.add(item);
    }

    public static void blacklistItemDisplay(Class<? extends Item> item) {
        blacklistedItemClasses.add(item);
    }

    public static boolean isBlacklisted(Item item) {
        return isOn && blacklistedItems.contains(item);
    }

    public static boolean isBlacklisted(Class<? extends Item> item) {
        return isOn && blacklistedItemClasses.contains(item);
    }

}
