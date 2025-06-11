package foxiwhitee.hellmod.config;

import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigHandler {
    private static final Map<String, Configuration> configCache = new HashMap<>();

    public static void loadConfigs(FMLPreInitializationEvent event) {
        ASMDataTable asmData = event.getAsmData();
        Set<ASMDataTable.ASMData> configClasses = asmData.getAll(Config.class.getName());

        for (ASMDataTable.ASMData entry : configClasses) {
            try {
                Class<?> configClass = Class.forName(entry.getClassName());
                Config configAnnotation = configClass.getAnnotation(Config.class);
                if (configAnnotation != null) {
                    loadConfig(configClass, configAnnotation, event);
                }
            } catch (Exception e) {
                System.err.println("Failed to load config for " + entry.getClassName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void loadConfig(Class<?> configClass, Config configAnnotation, FMLPreInitializationEvent event) {
        String folder = configAnnotation.folder();
        String name = configAnnotation.name();
        String configKey = folder + "/" + name;
        File configDir = new File(event.getModConfigurationDirectory(), folder);
        File configFile = new File(configDir, name + ".cfg");

        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        Configuration config = new Configuration(configFile);
        config.load();

        for (Field field : configClass.getDeclaredFields()) {
            ConfigValue configValue = field.getAnnotation(ConfigValue.class);
            if (configValue == null) continue;

            field.setAccessible(true);
            String category = configValue.category().isEmpty() ? "general" : configValue.category();
            String key = configValue.name().isEmpty() ? field.getName() : configValue.name();
            String desc = configValue.desc();
            String min = configValue.min();
            String max = configValue.max();

            try {
                Object defaultValue = field.get(null);
                Class<?> type = field.getType();

                double minVal = min.isEmpty() && (type == int.class || type == long.class || type == double.class) ? 1 : Double.NEGATIVE_INFINITY;
                double maxVal = Double.POSITIVE_INFINITY;
                if (type == int.class) {
                    maxVal = max.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(max);
                    minVal = min.isEmpty() ? 1 : Integer.parseInt(min);
                } else if (type == long.class) {
                    maxVal = max.isEmpty() ? Long.MAX_VALUE : Long.parseLong(max);
                    minVal = min.isEmpty() ? 1L : Long.parseLong(min);
                } else if (type == double.class) {
                    maxVal = max.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(max);
                    minVal = min.isEmpty() ? 1.0 : Double.parseDouble(min);
                } else if (type == boolean.class) {
                    if (!max.isEmpty()) {
                        throw new ConfigExcaption("Boolean type can't have a max value");
                    }
                    if (!min.isEmpty()) {
                        throw new ConfigExcaption("Boolean type can't have a min value");
                    }
                } else if (type == String.class) {
                    if (!max.isEmpty()) {
                        throw new ConfigExcaption("String type can't have a max value");
                    }
                    if (!min.isEmpty()) {
                        throw new ConfigExcaption("String type can't have a min value");
                    }
                }

                if (type == int.class) {
                    int value = config.getInt(key, category, (Integer) defaultValue, (int) minVal, (int) maxVal, desc);
                    if (value < minVal) {
                        value = (int) minVal;
                    }
                    if (value > maxVal) {
                        value = (int) maxVal;
                    }
                    field.setInt(null, value);
                } else if (type == long.class) {
                    long value = Long.valueOf(config.getString(key, category, String.valueOf((Long) defaultValue), desc + " [range: " + (long)minVal + " ~ " + (long)maxVal + "]"));
                    if (value < minVal) {
                        value = (long) minVal;
                    }
                    if (value > maxVal) {
                        value = (long) maxVal;
                    }
                    field.setLong(null, value);
                } else if (type == double.class) {
                    double value = config.get(category, key, (Double) defaultValue, desc, minVal, maxVal).getDouble();
                    if (value < minVal) {
                        value = minVal;
                    }
                    if (value > maxVal) {
                        value = maxVal;
                    }
                    field.setDouble(null, value);
                } else if (type == boolean.class) {
                    boolean value = config.getBoolean(key, category, (Boolean) defaultValue, desc);
                    field.setBoolean(null, value);
                } else if (type == String.class) {
                    String value = config.getString(key, category, (String) defaultValue, desc);
                    field.set(null, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                if (config.hasChanged()) {
                    config.save();
                }
            }
        }

        configCache.put(configKey, config);


    }
}