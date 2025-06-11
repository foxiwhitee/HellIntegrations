package foxiwhitee.hellmod.utils.localization;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LocalizationUtils {
    private static final List<String> local = new ArrayList<>();
    private static final String MODID = HellCore.MODID;
    private static Path output;

    public static void findUnlocalizedNames() {
        for (LocalizationFiles lang : LocalizationFiles.values()) {
            try {
                Set<String> existingKeys = loadLangKeys(lang.filename);
                List<String> missingKeysOutput = getStrings(existingKeys);

                if (!missingKeysOutput.isEmpty()) {
                    output = Paths.get(lang.outfilename + ".txt");
                    Files.write(output, missingKeysOutput, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    System.out.println("Missing keys written to: " + output.toAbsolutePath());
                } else {
                    System.out.println("No missing keys found.");
                }
            } catch (IOException e) {
                System.err.println("Exception in findUnlocalizedNames(): " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static List<String> getStrings(Set<String> existingKeys) {
        List<String> missingKeysOutput = new ArrayList<>();

        String string;
        for (Block block : RegisterUtils.blocks) {
            string = block.getUnlocalizedName() + ".name";
            if (!existingKeys.contains(string)) {
                missingKeysOutput.add(string);
            }
        }
        for (Item item : RegisterUtils.items) {
            string = item.getUnlocalizedName() + ".name";
            if (!existingKeys.contains(string)) {
                missingKeysOutput.add(string);
            }
        }
        for (String s : local) {
            if (!existingKeys.contains(s)) {
                missingKeysOutput.add(s);
            }
        }

        return missingKeysOutput;
    }

    public static Set<String> loadLangKeys(String lang) throws IOException {
        Set<String> existingKeys = new HashSet<>();
        URL codeSourceUrl = RegisterUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPathStr = codeSourceUrl.toString();
        if (jarPathStr.startsWith("jar:")) {
            jarPathStr = jarPathStr.substring(4);
        }
        if (jarPathStr.startsWith("file:")) {
            jarPathStr = jarPathStr.substring(5);
        }
        if (jarPathStr.contains("!")) {
            jarPathStr = jarPathStr.substring(0, jarPathStr.indexOf("!"));
        }
        jarPathStr = URLDecoder.decode(jarPathStr, StandardCharsets.UTF_8.name());
        File jarFile = new File(jarPathStr);

        String langPath = "assets/" + MODID.toLowerCase() + "/lang/" + lang + ".lang";
        try (ZipFile zipFile = new ZipFile(jarFile)) {
            ZipEntry entry = zipFile.getEntry(langPath);
            if (entry == null) {
                return existingKeys;
            }
            try (InputStream inputStream = zipFile.getInputStream(entry);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        String[] parts = line.split("=", 2);
                        if (parts.length > 0) {
                            String key = parts[0].trim();
                            if (!key.isEmpty()) {
                                existingKeys.add(key);
                            }
                        }
                    }
                }
            }
        }
        return existingKeys;
    }

    public static String localize(String string, Object... objects) {
        boolean hasArguments = objects != null && objects.length != 0;
        String out = !hasArguments ? StatCollector.translateToLocal(string) : String.format(StatCollector.translateToLocal(string), objects);
        if (!local.contains(string)) local.add(string);
        return out;
    }

    public static String makeRainbow(String string, double delay, int step, boolean isBold) {
        return ludicrousFormatting(string, new String[]{EnumChatFormatting.RED.toString(), EnumChatFormatting.GOLD.toString(), EnumChatFormatting.YELLOW.toString(), EnumChatFormatting.GREEN.toString(), EnumChatFormatting.AQUA.toString(), EnumChatFormatting.BLUE.toString(), EnumChatFormatting.LIGHT_PURPLE.toString()}, delay, step, isBold);
    }

    public static String makeDivine(String string, double delay, int step, boolean isBold) {
        return ludicrousFormatting(string, new String[]{"#F30000", "#FF0000", "#E80000", "#DC0000", "#D00000", "#C50101", "#B90101", "#AD0101", "#A10101", "#AD0101", "#B90101", "#C50101", "#D00000", "#E80000", "#FF0000"}, delay, step, isBold);
    }

    public static String makeFox(String string, double delay, int step, boolean isBold) {
        return ludicrousFormatting(string, new String[]{"#E86A00", "#E56600", "#EB6D00", "#EE7100", "#F17400", "#F37800", "#F67B00", "#F97F00", "#FC8200", "#FF8600", "#FC8200", "#F97F00", "#F67B00", "#F37800", "#F17400", "#EE7100", "#EB6D00", "#E56600"}, delay, step, isBold);
    }

    private static String ludicrousFormatting(String input, String[] colours, double delay, int step, boolean isBold) {
        StringBuilder sb = new StringBuilder(input.length() * (isBold ? 5 : 3));
        double d = delay;
        if (d <= 0.0D)
            d = 0.001D;
        int offset =
                (int)Math.floor(Minecraft.getSystemTime() / d) % colours.length;
        for (int i = 0, j = input.length(); i < j; i++) {
            char c = input.charAt(i);
            int col = (i * step + colours.length - offset) % colours.length;
            sb.append(colours[col].toString());
            if (isBold)
                sb.append(String.valueOf(EnumChatFormatting.BOLD));
            sb.append(c);
        }
        return sb.toString();
    }
}
