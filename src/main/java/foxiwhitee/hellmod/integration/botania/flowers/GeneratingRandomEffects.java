package foxiwhitee.hellmod.integration.botania.flowers;

import foxiwhitee.hellmod.config.EffectConfig;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;


import java.util.*;
import java.util.stream.Collectors;

public final class GeneratingRandomEffects {
    public static final GeneratingRandomEffects INSTANCE = new GeneratingRandomEffects();

    private static final Random random = new Random(System.currentTimeMillis());

    public static Random getRandom() {
        return random;
    }

    public static String toSignedFormattedString(double value) {
        int num = (int) (value * 100);
        String color;
        if (num >= EffectConfig.thresholdFox) {
            return LocalizationUtils.makeFox("+" + num, EffectConfig.rarityFoxColorSpeed, 1, true) + EffectConfig.percentColor;
        } else if (num >= EffectConfig.thresholdDivine) {
            return LocalizationUtils.makeDivine("+" + num, EffectConfig.rarityDivineColorSpeed, 1, true) + EffectConfig.percentColor;
        } else if (num >= EffectConfig.thresholdLegendary) {
            return LocalizationUtils.makeRainbow("+" + num, EffectConfig.rarityLegendaryColorSpeed, 0, true) + EffectConfig.percentColor;
        } else if (num >= EffectConfig.threshold3) {
            color = EffectConfig.descColor3;
        } else if (num >= EffectConfig.threshold2) {
            color = EffectConfig.descColor2;
        } else if (num >= EffectConfig.threshold1) {
            color = EffectConfig.descColor1;
        } else  {
            color = EffectConfig.descColorMin;
        }
        return color + EnumChatFormatting.BOLD + (num >= 0 ? "+" : "") + num + EffectConfig.percentColor;
    }

    public static String toSignedFormattedStringFalse(double value) {
        int num = (int) (value * 100);
        String color;
        if (num >= 95) {
            return LocalizationUtils.makeFox("+" + num, EffectConfig.rarityFoxColorSpeed, 1, true) + EffectConfig.percentColor;
        } else if (num >= 75) {
            return LocalizationUtils.makeDivine("+" + num, EffectConfig.rarityDivineColorSpeed, 1, true) + EffectConfig.percentColor;
        } else if (num >= 50) {
            return LocalizationUtils.makeRainbow("+" + num, EffectConfig.rarityLegendaryColorSpeed, 0, true) + EffectConfig.percentColor;
        } else if (num >= 25) {
            color = EffectConfig.descColor3;
        } else if (num >= 0) {
            color = EffectConfig.descColor2;
        } else  {
            color = EffectConfig.descColorMin;
        }
        return color + EnumChatFormatting.BOLD + (num >= 0 ? "+" : "") + num + EffectConfig.percentColor;
    }

    private static void addEffectDescription(List<String> list, String entry, double value) {
        String part1 = EnumChatFormatting.RESET.toString();
        String part2 = EffectConfig.bracketColor;
        String part3 = EnumChatFormatting.BLUE.toString();
        String part4 = LocalizationUtils.localize("tooltip.botania.effect.desc." + getFlowerName(entry));
        String addName = part4.contains("basic") ? part1 + part2 : part1 + part2 + "(" + part3 + part4 + part1 + part2 + ")";
        list.add(LocalizationUtils.localize("tooltip.botania.effect.desc." + entry.replaceAll("[+\\-\\.0-9]", "") + "." + (value > 0 ? "positive" : "negative")) + addName + EffectConfig.colonColor + ": " + (!entry.contains("chance_to_free_item_ise") ? toSignedFormattedString(value) : toSignedFormattedStringFalse(value)) + "%");
    }

    private static String getFlowerName(String entry) {
        if (entry.contains(".0")) return "basic";
        return GeneratingFlowersRegister.getNameByIndex(Integer.parseInt(entry.split("\\.")[1]) - 1);
    }

    public static double positiveEffectBonus(double mult, double lowerBound, double upperBound) {
        return Math.min(random.nextDouble() * (upperBound + 0.1 - lowerBound) + lowerBound, upperBound) * mult;
    }

    public static double negativeEffectBonus(double mult, double lowerBound, double upperBound) {
        return -Math.min(random.nextDouble() * (upperBound + 0.1 - lowerBound) + lowerBound, upperBound) / mult;
    }

    public static void generateNBT(ItemStack stack, LevelTypes flowerType) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            PartRarity rarity = PartRarity.generateRarity();
            tag.setString("rarity", rarity.toString().toLowerCase());
            int count = randomBetween(rarity.getMaxEffects() / 2, rarity.getMaxEffects());
            int positive = rarity.getMaxPositive();
            int negative = rarity.getMaxNegative();
            Map<EffectTypes, List<Integer>> flowers = new HashMap<>();
            try {
                flowers = generateRandomEffects(count, flowerType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Integer> fl;
            double bonus;
            boolean isPositive;
            String key;
            for (Map.Entry<EffectTypes, List<Integer>> effect : flowers.entrySet()) {
                fl = effect.getValue();
                for (int i : fl) {
                    isPositive = random.nextBoolean();
                    if (isPositive && positive > 0) {
                        positive--;
                    } else if (isPositive && positive == 0) {
                        negative--;
                        isPositive = false;
                    } else if (!isPositive && negative > 0) {
                        negative--;
                    } else if (!isPositive && negative == 0) {
                        positive--;
                        isPositive = true;
                    }
                    bonus = isPositive ? positiveEffectBonus(rarity.getMultiplier(), effect.getKey().minPositive, effect.getKey().maxPositive) : negativeEffectBonus(rarity.getMultiplier(), effect.getKey().minNegative, effect.getKey().maxNegative);
                    if (effect.getKey() == EffectTypes.CHANCE_TO_FREE_ITEM_USE && bonus >= 1) {
                        bonus = 0.9;
                    }
                    key = (isPositive ? "+" : "-") + effect.getKey().name().toLowerCase() + "." + i;
                    tag.setDouble(key, bonus);
                }
            }
            stack.setTagCompound(tag);
        }
    }

    public static void generateDescription(ItemStack stack, List<String> list) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            Set<String> keys = tag.func_150296_c();
            for (String entry : keys) {
                if (Objects.equals(entry, "rarity")) {
                    continue;
                }
                if ((int) (tag.getDouble(entry) * 100) != 0.0 && (int) (tag.getDouble(entry) * 100) != -0.0) {
                    addEffectDescription(list, entry, tag.getDouble(entry));
                }
            }
        }
    }

    public static int randomBetween(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private static Map<EffectTypes, List<Integer>> generateRandomEffects(int count, LevelTypes flowerType) {
        Map<EffectTypes, Integer> effects = new HashMap<>();
        Map<EffectTypes, List<Integer>> flowers = new HashMap<>();
        List<EffectTypes> validEffects = new ArrayList<>();

        for (EffectTypes type : EffectTypes.values()) {
            if (type.isAllowed(flowerType)) {
                validEffects.add(type);
            }
        }

        if (validEffects.isEmpty()) {
            return flowers;
        }

        int used = 0;
        while (used < count) {
            EffectTypes chosen = validEffects.get(random.nextInt(validEffects.size()));
            effects.merge(chosen, 1, Integer::sum);
            used++;
        }

        effects = effects.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        for (Map.Entry<EffectTypes, Integer> entry : effects.entrySet()) {
            EffectTypes effectType = entry.getKey();
            int requiredCount = entry.getValue();
            List<Integer> list = new ArrayList<>();

            List<Integer> validFlowerIndices = new ArrayList<>();
            for (int i = 0; i < GeneratingFlowersRegister.getSize(); i++) {
                List<LevelTypes> types = i != 0 ? GeneratingFlowersRegister.getValueByKey(GeneratingFlowersRegister.getNameByIndex(i - 1)).getLogic("").getTypes() : new ArrayList<>();
                if (i == 0 || types.contains(flowerType)) {
                    validFlowerIndices.add(i);
                }
            }

            int availableCount = Math.min(requiredCount, validFlowerIndices.size());
            if (availableCount == 0) {
                continue;
            }

            Collections.shuffle(validFlowerIndices, random);
            for (int j = 0; j < availableCount; j++) {
                list.add(validFlowerIndices.get(j));
            }

            flowers.put(effectType, list);
        }

        return flowers;
    }
}