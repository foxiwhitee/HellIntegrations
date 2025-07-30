package foxiwhitee.hellmod.utils.handler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GuiHandlerRegistry {
    private static final Map<Integer, SimpleGuiHandler> guiHandlers = new HashMap<>();

    public static void registerGuiHandlers(FMLPreInitializationEvent event) {
        ASMDataTable asmData = event.getAsmData();
        Set<ASMDataTable.ASMData> annotatedClasses = asmData.getAll(SimpleGuiHandler.class.getName());

        for (ASMDataTable.ASMData entry : annotatedClasses) {
            try {
                Class<?> clazz = Class.forName(entry.getClassName());
                if (!Block.class.isAssignableFrom(clazz)) {
                    throw new SimpleGuiHandlerException("The class " + clazz.getName() + " must inherit from Block");
                }

                SimpleGuiHandler annotation = clazz.getAnnotation(SimpleGuiHandler.class);
                if (annotation == null) {
                    continue;
                }

                String integration = annotation.integration();
                if (!integration.isEmpty() && !Loader.isModLoaded(integration)) {
                    continue;
                }

                // Só valida GUI se estivermos no lado CLIENTE
                if (cpw.mods.fml.common.FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                    if (!validateGuiConstructor(annotation.gui(), annotation.container())) {
                        throw new SimpleGuiHandlerException("Invalid GUI constructor for " + annotation.gui().getName() + " in " + clazz.getName());
                    }
                }

                if (!validateContainerConstructor(annotation.container(), annotation.tile())) {
                    throw new SimpleGuiHandlerException("Invalid Container constructor for " + annotation.gui().getName() + " in " + clazz.getName() + " must inherit from Block. Container must accept EntityPlayer and TileEntity");
                }

                if (guiHandlers.containsKey(annotation.index())) {
                    throw new SimpleGuiHandlerException("Duplicate GUI index " + annotation.index() + " for " + clazz.getName());
                }

                guiHandlers.put(annotation.index(), annotation);
            } catch (ClassNotFoundException e) {
                System.err.println("Failed to load class " + entry.getClassName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static boolean validateGuiConstructor(Class<? extends GuiContainer> guiClass, Class<? extends Container> containerClass) {
        for (Constructor<?> constructor : guiClass.getConstructors()) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            if (paramTypes.length == 1 && paramTypes[0].isAssignableFrom(containerClass)) {
                return true;
            }
        }
        return false;
    }

    private static boolean validateContainerConstructor(Class<? extends Container> containerClass, Class<? extends TileEntity> tileClass) {
        for (Constructor<?> constructor : containerClass.getConstructors()) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            if (paramTypes.length == 2 &&
                    paramTypes[0].isAssignableFrom(EntityPlayer.class) &&
                    paramTypes[1].isAssignableFrom(tileClass)) {
                return true;
            }
        }
        return false;
    }

    public static SimpleGuiHandler getGuiHandler(int index) {
        return guiHandlers.get(index);
    }

    public static Map<Integer, SimpleGuiHandler> getAllGuiHandlers() {
        return new HashMap<>(guiHandlers);
    }
}