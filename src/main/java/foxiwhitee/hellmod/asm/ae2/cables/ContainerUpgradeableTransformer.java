package foxiwhitee.hellmod.asm.ae2.cables;

import foxiwhitee.hellmod.utils.asm.ASMClassTransformer;
import foxiwhitee.hellmod.utils.asm.SpecialClassNode;
import org.objectweb.asm.ClassReader;

public class ContainerUpgradeableTransformer implements ASMClassTransformer {
  public ASMClassTransformer.TransformResult transformClass(String name, String transformedName, ClassReader reader, SpecialClassNode classNode) {
    classNode.addInterface("foxiwhitee/hellmod/utils/cables/IContainerUpgradeableAccessor").invoke("call").build();
    return ASMClassTransformer.TransformResult.MODIFIED_STACK;
  }
}
