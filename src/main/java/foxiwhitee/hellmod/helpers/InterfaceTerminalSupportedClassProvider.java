package foxiwhitee.hellmod.helpers;


import appeng.parts.misc.PartInterface;
import appeng.tile.misc.TileInterface;
import java.util.HashSet;
import java.util.Set;

public class InterfaceTerminalSupportedClassProvider {
    private static final Set<Class<? extends IInterfaceTerminalSupport>> supportedClasses = new HashSet<>();

    //static {
    //    supportedClasses.add((Class<? extends IInterfaceTerminalSupport>)(TileInterface.class));
    //    supportedClasses.add((Class<? extends IInterfaceTerminalSupport>)(PartInterface.class));
    //}

    public static Set<Class<? extends IInterfaceTerminalSupport>> getSupportedClasses() {
        return supportedClasses;
    }

    public static void register(Class<? extends IInterfaceTerminalSupport> clazz) {
        if (!supportedClasses.contains(clazz)) {
            supportedClasses.add(clazz);
        }
    }
}
