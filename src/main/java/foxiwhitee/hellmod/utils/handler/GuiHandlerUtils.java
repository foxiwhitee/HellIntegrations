package foxiwhitee.hellmod.utils.handler;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;

public class GuiHandlerUtils {
    public static Container getContainer(int index, EntityPlayer player, World world, int x, int y, int z) {
        SimpleGuiHandler handler = GuiHandlerRegistry.getGuiHandler(index);
        if (handler == null) {
            return null;
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !handler.tile().isInstance(tile)) {
            return null;
        }

        try {
            Class<? extends Container> containerClass = handler.container();
            Constructor<? extends Container> constructor = containerClass.getConstructor(EntityPlayer.class, handler.tile());
            return constructor.newInstance(player, tile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GuiContainer getGui(int index, EntityPlayer player, World world, int x, int y, int z) {
        SimpleGuiHandler handler = GuiHandlerRegistry.getGuiHandler(index);
        if (handler == null) {
            return null;
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !handler.tile().isInstance(tile)) {
            return null;
        }

        try {
            Class<? extends Container> containerClass = handler.container();
            Constructor<? extends Container> containerConstructor = containerClass.getConstructor(EntityPlayer.class, handler.tile());
            Container container = containerConstructor.newInstance(player, tile);

            Class<? extends GuiContainer> guiClass = handler.gui();
            Constructor<? extends GuiContainer> guiConstructor = guiClass.getConstructor(containerClass);
            return guiConstructor.newInstance(container);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}