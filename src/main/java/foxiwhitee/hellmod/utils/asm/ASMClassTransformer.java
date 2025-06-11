package foxiwhitee.hellmod.utils.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

public interface ASMClassTransformer extends Opcodes {
    TransformResult transformClass(String paramString1, String paramString2, ClassReader paramClassReader, SpecialClassNode paramSpecialClassNode);

    public enum TransformResult {
        NOT_MODIFIED, MODIFIED, MODIFIED_STACK;
    }
}
