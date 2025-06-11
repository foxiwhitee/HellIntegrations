package foxiwhitee.hellmod.integration.botania.flowers;


import java.util.*;

public final class GeneratingFlowersRegister {
    private static final Map<String, ICoreGeneratingFlower> flowers = new HashMap<>();

    public static void addFlower(String name, ICoreGeneratingFlower flower) {
        flowers.putIfAbsent(name, flower);
    }

    public static void removeFlower(String name) {
        if (flowers.containsKey(name)) {
            flowers.remove(name);
        }
    }

    public static void clearFlowers() {
        flowers.clear();
    }

    public static String getKeyByValue(ICoreGeneratingFlower flower) {
        for (Map.Entry<String, ICoreGeneratingFlower> entry : flowers.entrySet()) {
            if (Objects.equals(entry.getValue(), flower)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static ICoreGeneratingFlower getValueByKey(String name) {
        return flowers.get(name);
    }

    public static String getNameByIndex(int i) {
        int ii = 0;
        for (Map.Entry<String, ICoreGeneratingFlower> entry : flowers.entrySet()) {
            if (ii == i) return entry.getKey();
            ii++;
        }
        return null;
    }

    public static int getSize() {
        return flowers.size();
    }
}
