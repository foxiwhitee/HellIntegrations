package foxiwhitee.hellmod.utils.helpers;

import cofh.api.energy.IEnergyContainerItem;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;
import java.util.List;

public class EnergyUtility {
    private static final String[] postfix = {"", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q"};
    private static final DecimalFormat df = new DecimalFormat("#.#");

    public static String formatNumber(double value) {
        if (value < 1000.0D)
            return String.valueOf(value);
        if (value < 1000000.0D)
            return String.valueOf(Math.round(value) / 1000.0D) + "K";
        if (value < 1.0E9D)
            return String.valueOf(Math.round(value / 1000.0D) / 1000.0D) + "M";
        if (value < 1.0E12D)
            return String.valueOf(Math.round(value / 1000000.0D) / 1000.0D) + "B";
        return String.valueOf(Math.round(value / 1.0E9D) / 1000.0D) + "T";
    }

    public static final String formatPower(double e) {
        double energy = e;
        int offset = 0;
        while (energy >= 999.99D) {
            offset++;
            energy /= 1000.0D;
        }
        if (offset >= postfix.length) {
            double exponent = Math.floor(Math.log10(e));
            return df.format(e / Math.pow(10.0D, exponent)).replace(",", ".") + 'E' + (int)exponent;
        }
        return df.format(energy).replace(",", ".") + postfix[offset] ;
    }

    public static String formatNumber(long value) {
        if (value < 1000L)
            return String.valueOf(value);
        if (value < 1000000L)
            return String.valueOf(Math.round((float)value) / 1000.0D) + "K";
        if (value < 1000000000L)
            return String.valueOf(Math.round((float)(value / 1000L)) / 1000.0D) + "M";
        if (value < 1000000000000L)
            return String.valueOf(Math.round((float)(value / 1000000L)) / 1000.0D) + "G";
        if (value < 1000000000000000L)
            return String.valueOf(Math.round((float)(value / 1000000000L)) / 1000.0D) + "T";
        if (value < 1000000000000000000L)
            return String.valueOf(Math.round((float)(value / 1000000000000L)) / 1000.0D) + "P";
        if (value <= Long.MAX_VALUE)
            return String.valueOf(Math.round((float)(value / 1000000000000000L)) / 1000.0D) + "E";
        return "Something is very broken!!!!";
    }

    public static String getString(double value) {
        String[] preFixes = { "", "k", "M", "G", "T", "P", "E", "Z", "Y" };
        int offset = 0;
        while (value > 1000.0D && offset < preFixes.length) {
            value /= 1000.0D;
            offset++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value) + preFixes[offset];
    }

    public static void addLore(ItemStack stack, List<String> list, boolean addLeadingLine) {
        String[] lore = getLore(stack);
        if (addLeadingLine)
            list.add("");
        if (lore == null) {
            list.add("" + EnumChatFormatting.ITALIC + "" + EnumChatFormatting.DARK_PURPLE + "Invalid lore localization! (something is broken)");
            return;
        }
        for (String s : lore)
            list.add("" + EnumChatFormatting.ITALIC + "" + EnumChatFormatting.DARK_PURPLE + s);
    }

    public static void addLore(ItemStack stack, List list) {
        addLore(stack, list, true);
    }

    public static void addEnergyAndLore(ItemStack stack, List<String> list) {
        if (!isShiftKeyDown()) {
            list.add(LocalizationUtils.localize("tooltip.info.hold") + " " + EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + LocalizationUtils.localize("tooltip.info.shift") + EnumChatFormatting.RESET + " " + EnumChatFormatting.GRAY + LocalizationUtils.localize("tooltip.info.forDetails"));
        } else {
            addEnergyInfo(stack, list);
            addLore(stack, list);
        }
    }

    public static boolean isShiftKeyDown() {
        return (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
    }

    public static String[] getLore(ItemStack stack) {
        String unlocalizeLore = stack.getItem().getUnlocalizedName() + ".lore";
        String rawLore = LocalizationUtils.localize(unlocalizeLore);
        if (rawLore.contains(unlocalizeLore))
            return null;
        String lineCountS = rawLore.substring(0, 1);
        rawLore = rawLore.substring(1);
        int lineCount = 0;
        try {
            lineCount = Integer.parseInt(lineCountS);
        } catch (NumberFormatException e) {

        }
        String[] loreLines = new String[lineCount];
        for (int i = 0; i < lineCount; i++) {
            if (rawLore.contains("\\n")) {
                loreLines[i] = rawLore.substring(0, rawLore.indexOf("\\n"));
            } else {
                loreLines[i] = rawLore;
            }
            if (rawLore.contains("\\n"))
                rawLore = rawLore.substring(rawLore.indexOf("\\n") + 2);
        }
        return loreLines;
    }

    public static void addEnergyInfo(ItemStack stack, List<String> list) {
        IEnergyContainerItem item = (IEnergyContainerItem)stack.getItem();
        int energy = item.getEnergyStored(stack);
        int maxEnergy = item.getMaxEnergyStored(stack);
        String eS = "";
        String eM = "";
        if (energy < 1000) {
            eS = String.valueOf(energy);
        } else if (energy < 1000000) {
            eS = String.valueOf(energy);
        } else {
            eS = String.valueOf(Math.round(energy / 1000.0F) / 1000.0F) + "m";
        }
        if (maxEnergy < 1000) {
            eM = String.valueOf(maxEnergy);
        } else if (maxEnergy < 1000000) {
            eM = String.valueOf(Math.round(maxEnergy / 100.0F) / 10.0F) + "k";
        } else {
            eM = String.valueOf(Math.round(maxEnergy / 10000.0F) / 100.0F) + "m";
        }
        list.add(LocalizationUtils.localize("tooltip.info.charge") + ": " + eS + " / " + eM + " RF");
    }
}
