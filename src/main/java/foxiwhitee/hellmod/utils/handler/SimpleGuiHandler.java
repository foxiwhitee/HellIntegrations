package foxiwhitee.hellmod.utils.handler;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SimpleGuiHandler {
    int index();
    Class<? extends TileEntity> tile();
    Class<? extends GuiContainer> gui();
    Class<? extends Container> container();
    String integration() default "";
}
