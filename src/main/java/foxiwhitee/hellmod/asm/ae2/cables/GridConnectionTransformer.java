package foxiwhitee.hellmod.asm.ae2.cables;

import foxiwhitee.hellmod.utils.asm.ASMClassTransformer;
import foxiwhitee.hellmod.utils.asm.SpecialClassNode;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ListIterator;

public class GridConnectionTransformer implements ASMClassTransformer {
  public ASMClassTransformer.TransformResult transformClass(String name, String transformedName, ClassReader reader, SpecialClassNode classNode) {
    MethodNode methodNode = classNode.getMethod("canSupportMoreChannels");
    for (AbstractInsnNode node : methodNode.instructions.toArray()) {
      if (node.getOpcode() == 16 && node.getType() == 1) {
        IntInsnNode i = (IntInsnNode)node;
        i.operand = 512;
        i.setOpcode(17);
      } 
    } 
    methodNode = classNode.getMethod("finalizeChannels");
    ListIterator<AbstractInsnNode> it;
    for (it = methodNode.instructions.iterator(); it.hasNext(); ) {
      AbstractInsnNode node = it.next();
      if (node.getType() == 1) {
        IntInsnNode i = (IntInsnNode)node;
        if (i.operand == 255) {
          methodNode.instructions.insertBefore((AbstractInsnNode)i, (AbstractInsnNode)new LdcInsnNode(Integer.valueOf(65535)));
          it.remove();
          continue;
        } 
        if (i.operand == 8)
          i.operand = 16; 
      } 
    } 
    methodNode = classNode.getMethod("getLastUsedChannels");
    for (it = methodNode.instructions.iterator(); it.hasNext(); ) {
      AbstractInsnNode node = it.next();
      if (node.getType() == 1) {
        IntInsnNode i = (IntInsnNode)node;
        if (i.operand == 255) {
          methodNode.instructions.insertBefore((AbstractInsnNode)i, (AbstractInsnNode)new LdcInsnNode(Integer.valueOf(65535)));
          it.remove();
        } 
      } 
    } 
    methodNode = classNode.getMethod("setControllerRoute");
    for (it = methodNode.instructions.iterator(); it.hasNext(); ) {
      AbstractInsnNode node = it.next();
      if (node.getType() == 1) {
        IntInsnNode i = (IntInsnNode)node;
        if (i.operand == -256) {
          methodNode.instructions.insertBefore((AbstractInsnNode)i, (AbstractInsnNode)new LdcInsnNode(Integer.valueOf(-65536)));
          it.remove();
        } 
      } 
    } 
    methodNode = classNode.getMethod("getUsedChannels");
    for (it = methodNode.instructions.iterator(); it.hasNext(); ) {
      AbstractInsnNode node = it.next();
      if (node.getType() == 1) {
        IntInsnNode i = (IntInsnNode)node;
        if (i.operand == 255) {
          methodNode.instructions.insertBefore((AbstractInsnNode)i, (AbstractInsnNode)new LdcInsnNode(Integer.valueOf(65535)));
          it.remove();
          continue;
        } 
        if (i.operand == 8)
          i.operand = 16; 
      } 
    } 
    return ASMClassTransformer.TransformResult.MODIFIED_STACK;
  }
}

