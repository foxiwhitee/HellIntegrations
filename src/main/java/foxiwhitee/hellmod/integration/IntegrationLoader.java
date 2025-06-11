package foxiwhitee.hellmod.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegrationLoader {
    private static final Map<String, IIntegration> modIntegrations = new HashMap<>();

    public static boolean hasIntegration(String modId) {
        if (modIntegrations.containsKey(modId)) {
            return true;
        }
        return modIntegrations.keySet().stream().anyMatch(key -> key.startsWith(modId + "[") || key.equals(modId));
    }

    public static void preInit(FMLPreInitializationEvent event) {
        ASMDataTable asmData = event.getAsmData();
        for (ASMDataTable.ASMData entry : asmData.getAll(Integration.class.getName())) {
            try {
                if (verifyDependencies(entry)) {
                    Class<?> integrationClass = Class.forName(entry.getClassName());
                    Integration annotation = integrationClass.getAnnotation(Integration.class);
                    if (IIntegration.class.isAssignableFrom(integrationClass)) {
                        IIntegration instance = (IIntegration) integrationClass.newInstance();
                        registerIntegration(annotation, instance);
                    } else {
                        System.err.println("Class " + entry.getClassName() + " does not implement IIntegration");
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to load integration for " + entry.getClassName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        modIntegrations.values().forEach(integration -> integration.preInit(event));
    }

    public static void init(FMLInitializationEvent event) {
        modIntegrations.values().forEach(integration -> integration.init(event));
    }

    public static void postInit(FMLPostInitializationEvent event) {
        modIntegrations.values().forEach(integration -> integration.postInit(event));
    }

    private static boolean verifyDependencies(ASMDataTable.ASMData data) {
        String modId = (String) data.getAnnotationInfo().getOrDefault("modid", "");
        if (!Loader.isModLoaded(modId)) {
            return false;
        }
        List<String> dependencies = (List<String>) data.getAnnotationInfo().getOrDefault("dependencies", new ArrayList<>());
        for (String dep : dependencies) {
            if (!Loader.isModLoaded(dep)) {
                return false;
            }
        }
        return true;
    }

    private static void registerIntegration(Integration annotation, IIntegration integration) {
        if (annotation == null) {
            return;
        }
        String key = annotation.modid();
        String[] deps = annotation.dependencies();
        if (deps.length > 0) {
            key += "[" + String.join(",", deps) + "]";
        }
        modIntegrations.put(key, integration);
    }
}
