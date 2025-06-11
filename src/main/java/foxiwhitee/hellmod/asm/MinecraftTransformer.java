package foxiwhitee.hellmod.asm;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static foxiwhitee.hellmod.utils.asm.ASMUtils.*;
import static org.objectweb.asm.Opcodes.*;

public class MinecraftTransformer {
    public static final boolean IS_DEV = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    public static byte[] transformFontRenderer(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("renderStringAtPos") && method.desc.equals("(Ljava/lang/String;Z)V")) {
                AbstractInsnNode targetInsn = null;
                for (int i = 0; i < method.instructions.size() - 3; i++) {
                    AbstractInsnNode insn1 = method.instructions.get(i);
                    AbstractInsnNode insn2 = method.instructions.get(i + 1);
                    AbstractInsnNode insn3 = method.instructions.get(i + 2);
                    AbstractInsnNode insn4 = method.instructions.get(i + 3);

                    if (insn1 instanceof VarInsnNode &&
                            ((VarInsnNode) insn1).getOpcode() == ALOAD && ((VarInsnNode) insn1).var == 1 &&
                            insn2 instanceof VarInsnNode &&
                            ((VarInsnNode) insn2).getOpcode() == ILOAD && ((VarInsnNode) insn2).var == 3 &&
                            insn3 instanceof MethodInsnNode &&
                            ((MethodInsnNode) insn3).owner.equals("java/lang/String") &&
                            ((MethodInsnNode) insn3).name.equals("charAt") &&
                            ((MethodInsnNode) insn3).desc.equals("(I)C") &&
                            insn4 instanceof VarInsnNode &&
                            ((VarInsnNode) insn4).getOpcode() == ISTORE && ((VarInsnNode) insn4).var == 4) {
                        targetInsn = insn4;
                        break;
                    }
                }

                if (targetInsn != null) {
                    InsnList list = new InsnList();
                    LabelNode lNew = new LabelNode();
                    list.add(lNew);
                    list.add(new LineNumberNode(308, lNew));
                    list.add(new VarInsnNode(ILOAD, 4));
                    list.add(new IntInsnNode(BIPUSH, 35));
                    LabelNode elseLabel = new LabelNode();
                    list.add(new JumpInsnNode(IF_ICMPNE, elseLabel));
                    list.add(new VarInsnNode(ILOAD, 3));
                    list.add(new IntInsnNode(BIPUSH, 6));
                    list.add(new InsnNode(IADD));
                    list.add(new VarInsnNode(ALOAD, 1));
                    list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "length", "()I", false));
                    list.add(new JumpInsnNode(IF_ICMPGE, elseLabel));

                    list.add(new VarInsnNode(ALOAD, 1));
                    list.add(new VarInsnNode(ILOAD, 3));
                    list.add(new VarInsnNode(ILOAD, 3));
                    list.add(new IntInsnNode(BIPUSH, 7));
                    list.add(new InsnNode(IADD));
                    list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "substring", "(II)Ljava/lang/String;", false));
                    list.add(new VarInsnNode(ILOAD, 2));
                    list.add(new MethodInsnNode(INVOKESTATIC, "foxiwhitee/hellmod/asm/MinecraftHook", "hexToInt", "(Ljava/lang/String;Z)I", false));
                    list.add(new VarInsnNode(ALOAD, 0));
                    list.add(new InsnNode(SWAP));
                    list.add(new FieldInsnNode(PUTFIELD, "net/minecraft/client/gui/FontRenderer", "textColor", "I"));
                    
                    list.add(new VarInsnNode(ALOAD, 0)); 
                    list.add(new VarInsnNode(ALOAD, 0)); 
                    list.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/gui/FontRenderer", "textColor", "I"));
                    list.add(new IntInsnNode(BIPUSH, 16)); 
                    list.add(new InsnNode(ISHR)); 
                    list.add(new InsnNode(I2F)); 
                    list.add(new LdcInsnNode(255.0F)); 
                    list.add(new InsnNode(FDIV));
                    list.add(new VarInsnNode(ALOAD, 0));
                    list.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/gui/FontRenderer", "textColor", "I"));
                    list.add(new IntInsnNode(BIPUSH, 8)); 
                    list.add(new InsnNode(ISHR)); 
                    list.add(new IntInsnNode(SIPUSH, 255)); 
                    list.add(new InsnNode(IAND)); 
                    list.add(new InsnNode(I2F)); 
                    list.add(new LdcInsnNode(255.0F)); 
                    list.add(new InsnNode(FDIV)); 
                    list.add(new VarInsnNode(ALOAD, 0)); 
                    list.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/gui/FontRenderer", "textColor", "I"));
                    list.add(new IntInsnNode(SIPUSH, 255)); 
                    list.add(new InsnNode(IAND));
                    list.add(new InsnNode(I2F)); 
                    list.add(new LdcInsnNode(255.0F)); 
                    list.add(new InsnNode(FDIV));
                    list.add(new VarInsnNode(ALOAD, 0)); 
                    list.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/gui/FontRenderer", "alpha", "F"));
                    list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/gui/FontRenderer", "setColor", "(FFFF)V", false));
                    list.add(new IincInsnNode(3, 6));

                    LabelNode cycleEndLabel = null;
                    for (AbstractInsnNode insn : method.instructions.toArray()) {
                        if (insn instanceof IincInsnNode &&
                                ((IincInsnNode) insn).var == 3 && ((IincInsnNode) insn).incr == 1) {
                            AbstractInsnNode nextInsn = insn.getNext();
                            if (nextInsn instanceof JumpInsnNode &&
                                    ((JumpInsnNode) nextInsn).getOpcode() == GOTO) {
                                cycleEndLabel = ((JumpInsnNode) nextInsn).label;
                                break;
                            }
                        }
                    }
                    if (cycleEndLabel != null) {
                        list.add(new JumpInsnNode(GOTO, cycleEndLabel)); 
                    }

                    list.add(elseLabel);

                    method.instructions.insert(targetInsn, list);
                }
            }

            if ((method.name.equals("getStringWidth") || method.name.equals("func_78256_a")) &&
                    method.desc.equals("(Ljava/lang/String;)I")) {

                method.instructions.clear();
                method.tryCatchBlocks.clear();
                method.localVariables.clear();

                InsnList newInstructions = new InsnList();
                LabelNode start = new LabelNode();
                LabelNode end = new LabelNode();
                newInstructions.add(start);
                newInstructions.add(new VarInsnNode(0x19, 1));
                newInstructions.add(new VarInsnNode(0x19, 0));
                newInstructions.add(new MethodInsnNode(0xb8, "foxiwhitee/hellmod/asm/MinecraftHook", "getStringWidth", "(Ljava/lang/String;Lnet/minecraft/client/gui/FontRenderer;)I", false));
                newInstructions.add(new InsnNode(0xac));
                newInstructions.add(end);

                method.instructions = newInstructions;

                method.localVariables.add(new LocalVariableNode("this", "Lnet/minecraft/client/gui/FontRenderer;", null, start, end, 0));
                method.localVariables.add(new LocalVariableNode("p_78256_1_", "Ljava/lang/String;", null, start, end, 1));

                method.maxLocals = 2;
                method.maxStack = 2;
            }
        }

        byte[] modifiedClass = writeClass(classNode);
        try {
            Path path = Paths.get("transformFontRenderer.class");
            Files.write(path, modifiedClass);
            System.out.println("[SUCCESS] Файл transformFontRenderer.class записано!");
        } catch (Exception e) {
            System.out.println("[NOT SUCCESS] Файл transformFontRenderer.class не записано!");
            e.printStackTrace();
        }
        return writeClass(classNode);
    }

    private static boolean isTargetLabel(LabelNode label, MethodNode method) {
        // Шукаємо мітку L0 за номером рядка 305
        AbstractInsnNode next = label;
        while (next != null) {
            if (next instanceof LineNumberNode) {
                LineNumberNode line = (LineNumberNode) next;
                if (line.line == 305) { // LINENUMBER 305 L0
                    return true;
                }
            }
            next = next.getNext();
        }
        return false;
    }

    // Допоміжний метод для пошуку мітки L38
    private static LabelNode findLabelNode(MethodNode method, String labelName) {
        for (AbstractInsnNode insn : method.instructions.toArray()) {
            if (insn instanceof LineNumberNode) {
                LineNumberNode line = (LineNumberNode) insn;
                if (labelName.equals("L38") && line.line == 305) { // LINENUMBER 305 L38
                    return line.start;
                }
            }
        }
        return null;
    }

}
