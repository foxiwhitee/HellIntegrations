package foxiwhitee.hellmod.asm.ic2;

import foxiwhitee.hellmod.utils.asm.ASMUtils;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class IC2Transformer {
    public static final boolean IS_DEV = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    public static byte[] transformTileEntityPatternStorage(byte[] basicClass) {
        ClassNode node = ASMUtils.readClass(basicClass);
        if (!node.superName.equals("ic2/core/block/TileEntityInventory")) return basicClass;
        MethodNode method = ASMUtils.findMethod(node, "addPattern", "()V");
        if (method == null) return basicClass;
        InsnList hook = new InsnList();
        hook.add(new VarInsnNode(ALOAD, 0));
        hook.add(new MethodInsnNode(INVOKESTATIC, "foxiwhitee/hellmod/asm/ic2/IC2Hooks", "onPatternAdded", "(Lic2/core/block/machine/tileentity/TileEntityPatternStorage;)V", false));
        ASMUtils.findAllOpcodes(method.instructions, RETURN).forEach(p -> method.instructions.insertBefore(p, ASMUtils.copy(hook)));
        return ASMUtils.writeClass(node);
    }
}
