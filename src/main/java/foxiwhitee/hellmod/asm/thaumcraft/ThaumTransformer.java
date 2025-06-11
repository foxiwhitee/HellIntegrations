package foxiwhitee.hellmod.asm.thaumcraft;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Optional;

import static foxiwhitee.hellmod.utils.asm.ASMUtils.*;
import static org.objectweb.asm.Opcodes.*;

public class ThaumTransformer {
    public static final boolean IS_DEV = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    public static byte[] transformInfusionMatrix(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        String owner = "thaumcraft/common/tiles/TileInfusionMatrix";
        String thisFile = "foxiwhitee/hellmod/asm/thaumcraft/asm/ThaumHooks";
        classNode.fields.add(new FieldNode(ACC_PUBLIC, "StabilizerPresent", "Z", null, Boolean.valueOf(false)));

        MethodNode targetMethod = findMethod(classNode, "craftingStart");
        if (targetMethod != null) {
            InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new InsnNode(ICONST_0));
            list.add(new FieldInsnNode(PUTFIELD, owner, "StabilizerPresent", "Z"));
            targetMethod.instructions.insert(list);

            List<AbstractInsnNode> points = findAllOpcodes(targetMethod.instructions, RETURN);
            if (!points.isEmpty()) {
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new InsnNode(DUP));
                list.add(new MethodInsnNode(INVOKESTATIC, thisFile, "onCraftingStartEnd", "(L" + owner + ";)Z", false));
                list.add(new FieldInsnNode(PUTFIELD, owner, "StabilizerPresent", "Z"));
                for (AbstractInsnNode point : points)
                    targetMethod.instructions.insertBefore(point, copy(list));
                list.clear();
            }
        }
        
        targetMethod = findMethod(classNode, "writeToNBT", "func_145841_b" );
        if (targetMethod != null) {
            InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(GETFIELD, owner, "StabilizerPresent", "Z"));
            list.add(new MethodInsnNode(INVOKESTATIC, thisFile, "writeToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;Z)V", false));
            targetMethod.instructions.insert(list);
        }

        targetMethod = findMethod(classNode, "readFromNBT", "func_145839_a" );
        if (targetMethod != null) {
            InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new MethodInsnNode(INVOKESTATIC, thisFile, "readFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)Z", false));
            list.add(new FieldInsnNode(PUTFIELD, owner, "StabilizerPresent", "Z"));
            targetMethod.instructions.insert(list);
        }
        targetMethod = findMethod(classNode, "craftCycle" );
        targetMethod = findMethod(classNode, "readFromNBT", "func_145839_a" );
        if (targetMethod != null) {
            InsnList list = new InsnList();
            LabelNode labelN = new LabelNode();
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(GETFIELD, owner, "StabilizerPresent", "Z"));
            list.add(new JumpInsnNode(IFEQ, labelN));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new InsnNode(ICONST_0));
            list.add(new FieldInsnNode(PUTFIELD, owner, "instability", "I"));
            list.add(labelN);
            targetMethod.instructions.insert(list);

            Optional<MethodInsnNode> callDrainEssentia = findAllOpcodes(targetMethod.instructions, INVOKESTATIC).stream().filter(i -> i instanceof MethodInsnNode).map(i -> (MethodInsnNode) i).filter(i -> i.name.equals("drainEssentia")).findFirst();
            if (callDrainEssentia.isPresent()) {
                AbstractInsnNode nextReturn = null;
                AbstractInsnNode next = ((MethodInsnNode) callDrainEssentia.get()).getNext();
                while (next != null) {
                    if (next.getOpcode() == RETURN) {
                        nextReturn = next;
                        break;
                    }
                    next = next.getNext();
                }
                if (nextReturn != null) {
                    LabelNode labelNode = new LabelNode();
                    list.add(new VarInsnNode(ALOAD, 0));
                    list.add(new FieldInsnNode(GETFIELD, owner, "StabilizerPresent", "Z"));
                    list.add(new JumpInsnNode(IFNE, labelNode));
                    targetMethod.instructions.insertBefore(nextReturn, list);
                    targetMethod.instructions.insert(nextReturn, labelNode);
                }
            }
            List<AbstractInsnNode> points = findAllOpcodes(targetMethod.instructions, RETURN);
            if (!points.isEmpty()) {
                for (AbstractInsnNode point : points) {
                    LabelNode labelNode = new LabelNode();
                    list.add(new VarInsnNode(ALOAD, 0));
                    list.add(new FieldInsnNode(GETFIELD, owner, "StabilizerPresent", "Z"));
                    list.add(new JumpInsnNode(IFEQ, labelNode));
                    list.add(new VarInsnNode(ALOAD, 0));
                    list.add(new InsnNode(ICONST_1));
                    list.add(new FieldInsnNode(PUTFIELD, owner, "countDelay", "I"));
                    list.add(labelNode);
                    targetMethod.instructions.insertBefore(point, list);
                }
            }
            list.clear();
        }
        return writeClass(classNode);
    }
}
