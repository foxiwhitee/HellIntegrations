package foxiwhitee.hellmod.asm.ae2.cables;


import foxiwhitee.hellmod.utils.asm.ASMClassTransformer;
import foxiwhitee.hellmod.utils.asm.SpecialClassNode;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class AEBasePartTransformer implements ASMClassTransformer {
  public ASMClassTransformer.TransformResult transformClass(String name, String transformedName, ClassReader reader, SpecialClassNode classNode) {
    MethodNode methodNode = classNode.getMethod("<init>");
    for (AbstractInsnNode node : methodNode.instructions.toArray()) {
      if (node.getType() == 3 && node.getOpcode() == 193) {
        TypeInsnNode typeInsnNode = (TypeInsnNode)node;
        if (typeInsnNode.desc.equals("appeng/parts/networking/PartCable"))
          typeInsnNode.desc = "appeng/api/implementations/parts/IPartCable"; 
      } 
    } 
    return ASMClassTransformer.TransformResult.MODIFIED;
  }
}