package foxiwhitee.hellmod.utils.helpers;

import com.google.common.reflect.ClassPath;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import foxiwhitee.hellmod.HellCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterUtils {
    public static CreativeTabs tab = HellCore.HELL_TAB;
    public static final List<Block> blocks = new ArrayList<>();
    public static final List<Item> items = new ArrayList<>();

    public static <T> List<Class<? extends T>> findClasses(String pkg, Class<T> find) {
        try {
            return ClassPath.from(RegisterUtils.class.getClassLoader())
                    .getTopLevelClassesRecursive(pkg)
                    .stream()
                    .map(ClassPath.ClassInfo::load)
                    .filter(find::isAssignableFrom)
                    .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                    .filter(clazz -> !clazz.isAnnotationPresent(Deprecated.class))
                    .filter(clazz -> !clazz.isInterface())
                    .sorted((a, b) -> a.getSimpleName().compareTo(b.getSimpleName()))
                    .map(clazz -> {
                        @SuppressWarnings("unchecked")
                        Class<? extends T> safeClass = (Class<? extends T>) clazz;
                        return safeClass;
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static <T> List<Class<? extends T>> findClasses(String pkg, Class<T> find, String filter) {
        try {
            return ClassPath.from(RegisterUtils.class.getClassLoader())
                    .getTopLevelClassesRecursive(pkg)
                    .stream()
                    .filter(i -> i.getName().contains(filter))
                    .map(ClassPath.ClassInfo::load)
                    .filter(find::isAssignableFrom)
                    .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                    .filter(clazz -> !clazz.isAnnotationPresent(Deprecated.class))
                    .filter(clazz -> !clazz.isInterface())
                    .sorted((a, b) -> a.getSimpleName().compareTo(b.getSimpleName()))
                    .map(clazz -> {
                        @SuppressWarnings("unchecked")
                        Class<? extends T> safeClass = (Class<? extends T>) clazz;
                        return safeClass;
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void registerTile(Class<? extends TileEntity> tile) {
        GameRegistry.registerTileEntity(tile, tile.getSimpleName());
    }

    public static void registerBlock(Block block) {
        block.setCreativeTab(tab);
        GameRegistry.registerBlock(block, block.getUnlocalizedName().replace("tile.", ""));
        blocks.add(block);

    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlock) {
        block.setCreativeTab(tab);
        GameRegistry.registerBlock(block, itemBlock, block.getUnlocalizedName().replace("tile.", ""));
        blocks.add(block);
    }

    public static void registerBlocks(Block... blocks) {
        Arrays.stream(blocks).forEach(RegisterUtils::registerBlock);
    }

    public static void registerBlocks(Class<? extends ItemBlock> itemBlock, Block... blocks) {
        Arrays.stream(blocks).forEach(block -> registerBlock(block, itemBlock));
    }

    public static void registerItem(Item item) {
        item.setCreativeTab(tab);
        GameRegistry.registerItem(item, item.getUnlocalizedName());
        items.add(item);
    }

    public static void registerItems(Item... items) {
        Arrays.stream(items).forEach(RegisterUtils::registerItem);
    }

    public static void registerItemRenderer(Item item, IItemRenderer render) {
        MinecraftForgeClient.registerItemRenderer(item, render);
    }

    public static void registerItemRendererFromBlock(Block block, IItemRenderer render) {
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), render);
    }

    public static void registerTileRenderer(Class<? extends TileEntity> tile, TileEntitySpecialRenderer render) {
        ClientRegistry.bindTileEntitySpecialRenderer(tile, render);
    }

    public static void registerEntityRender(Class<? extends Entity> entity, Render render) {
        RenderingRegistry.registerEntityRenderingHandler(entity, render);
    }
}
