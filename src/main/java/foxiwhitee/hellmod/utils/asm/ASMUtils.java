package foxiwhitee.hellmod.utils.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class ASMUtils {

    public static ClassNode readClass(byte[] classBytes) {
        return readClass(classBytes, 0);
    }

    public static ClassNode readClass(byte[] classBytes, int flags) {
        if (classBytes == null || classBytes.length == 0) {
            throw new IllegalArgumentException("Class bytes cannot be null or empty");
        }

        ClassReader classReader = new ClassReader(classBytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, flags);
        return classNode;
    }

    public static MethodNode findMethod(ClassNode classNode, String... names) {
        if (classNode == null) {
            throw new IllegalArgumentException("ClassNode cannot be null");
        }
        if (names == null || names.length == 0) {
            throw new IllegalArgumentException("Method names cannot be null or empty");
        }

        for (MethodNode method : classNode.methods) {
            for (String name : names) {
                if (method.name.equals(name)) {
                    return method;
                }
            }
        }

        return null;
    }

    public static byte[] writeClass(ClassNode node, int flags) {
        if (node == null) {
            throw new IllegalArgumentException("ClassNode cannot be null");
        }

        ClassWriter writer = new ClassWriter(flags);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static byte[] writeClass(ClassNode node) {
        return writeClass(node, ClassWriter.COMPUTE_FRAMES);
    }

    public static void log(ClassNode classNode, PrintWriter writer) {
        TraceClassVisitor visitor = new TraceClassVisitor(writer);
        classNode.accept(visitor);
    }

    public static void log(ClassNode classNode) {
        log(classNode, new PrintWriter(System.out));
    }

    public static InsnList copy(InsnList list) {
        MethodNode node = new MethodNode();
        list.accept(node);
        return node.instructions;
    }

    public static MethodNode createFieldGetter(String methodName, String fieldType, int fieldOpcode, String className, String fieldName, int returnOpcode) {
        MethodNode getter = new MethodNode(ASM5, ACC_PUBLIC, methodName, "()" + fieldType, null, null);
        InsnList instructions = new InsnList();
        if (fieldOpcode == GETFIELD) {
            instructions.add(new VarInsnNode(ALOAD, 0));
        }

        instructions.add(new FieldInsnNode(fieldOpcode, className, fieldName, fieldType));
        instructions.add(new InsnNode(returnOpcode));
        getter.instructions = instructions;
        return getter;
    }

    public static AbstractInsnNode locateOpcodeSequence(InsnList insnList, int... opcodes) {
        return locateOpcodeSequence(insnList, true, opcodes);
    }

    public static AbstractInsnNode locateOpcodeSequence(InsnList insnList, boolean ignoreNonOps, int... opcodes) {
        if (insnList == null || opcodes == null || opcodes.length == 0) {
            return null;
        }

        int currentSeqPos = 0;
        AbstractInsnNode startNode = null;

        for (int i = 0; i < insnList.size(); i++) {
            AbstractInsnNode current = insnList.get(i);
            int currentOpcode = current.getOpcode();

            if (ignoreNonOps && currentOpcode == -1) {
                continue;
            }

            if (currentOpcode == opcodes[currentSeqPos]) {
                if (currentSeqPos == 0) {
                    startNode = current;
                }
                currentSeqPos++;
                if (currentSeqPos == opcodes.length) {
                    return startNode;
                }
            } else {
                if (currentOpcode == opcodes[0]) {
                    startNode = current;
                    currentSeqPos = 1;
                } else {
                    currentSeqPos = 0;
                    startNode = null;
                }
            }
        }

        return null;
    }

    public static AbstractInsnNode locateLastOpcodeSequence(InsnList insnList, int... opcodes) {
        return locateLastOpcodeSequence(insnList, true, opcodes);
    }

    public static AbstractInsnNode locateLastOpcodeSequence(InsnList insnList, boolean ignoreNonOps, int... opcodes) {
        if (insnList == null || opcodes == null || opcodes.length == 0) {
            return null;
        }

        int currentSeqPos = opcodes.length - 1;
        AbstractInsnNode startNode = null;
        for (int i = insnList.size() - 1; i >= 0; i--) {
            AbstractInsnNode current = insnList.get(i);
            int currentOpcode = current.getOpcode();
            if (ignoreNonOps && currentOpcode == -1) {
                continue;
            }

            if (currentOpcode == opcodes[currentSeqPos]) {
                if (currentSeqPos == opcodes.length - 1) {
                    startNode = current;
                }
                currentSeqPos--;
                if (currentSeqPos < 0) {
                    return insnList.get(i + 1);
                }
            } else {
                if (currentOpcode == opcodes[opcodes.length - 1]) {
                    startNode = current;
                    currentSeqPos = opcodes.length - 2;
                } else {

                    currentSeqPos = opcodes.length - 1;
                    startNode = null;
                }
            }
        }

        return null;
    }

    public static List<AbstractInsnNode> findAllOpcodes(InsnList list, int opcode) {
        List<AbstractInsnNode> nodes = new ArrayList<>();
        for (AbstractInsnNode node : list.toArray()) {
            if (node.getOpcode() == opcode) nodes.add(node);
        }
        return nodes;
    }

    public static MethodNode createFieldAccessor(String ownerClass, String fieldName, Type fieldType, boolean isStatic, String methodName) {
        if (methodName == null || methodName.isEmpty()) {
            methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }

        int access = ACC_PUBLIC | (isStatic ? ACC_STATIC : 0);
        String descriptor = Type.getMethodDescriptor(fieldType);
        MethodNode accessor = new MethodNode(ASM5, access, methodName, descriptor, null, null);
        InsnList instructions = new InsnList();
        if (!isStatic) {
            instructions.add(new VarInsnNode(ALOAD, 0));
        }

        instructions.add(new FieldInsnNode(isStatic ? GETSTATIC : GETFIELD, ownerClass, fieldName, fieldType.getDescriptor()));
        instructions.add(new InsnNode(getReturnOpcodeFromType(fieldType)));
        accessor.instructions = instructions;
        return accessor;
    }

    public static String buildMethodDescriptor(Object returnType, Object... arguments) {
        Type retType = returnType instanceof Type ? (Type) returnType : Type.getType((String) returnType);
        Type[] argTypes = new Type[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            argTypes[i] = arguments[i] instanceof Type ? (Type) arguments[i] : Type.getType((String) arguments[i]);
        }
        StringBuilder descriptor = new StringBuilder("(");
        for (Type arg : argTypes) {
            descriptor.append(arg.getDescriptor());
        }
        descriptor.append(")").append(retType.getDescriptor());
        return descriptor.toString();
    }

    public static MethodNode createMethodInvoker(String ownerClass, String methodName, Object returnType, Object[] argTypes, boolean isStatic, String wrapperName) {
        String descriptor = buildMethodDescriptor(returnType, argTypes);
        if (wrapperName == null || wrapperName.isEmpty()) {
            wrapperName = "call" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        }

        int access = ACC_PUBLIC | (isStatic ? ACC_STATIC : 0);
        MethodNode invoker = new MethodNode(ASM5, access, wrapperName, descriptor, null, null);
        InsnList instructions = new InsnList();
        if (!isStatic) {
            instructions.add(new VarInsnNode(ALOAD, 0));
        }

        int varIndex = isStatic ? 0 : 1;
        for (Object arg : argTypes) {
            Type argType = arg instanceof Type ? (Type) arg : Type.getType((String) arg);
            instructions.add(new VarInsnNode(getLoadOpcodeFromType(argType), varIndex));
            varIndex += argType.getSize();
        }

        int invokeOpcode = isStatic ? INVOKESTATIC : INVOKESPECIAL;
        instructions.add(new MethodInsnNode(invokeOpcode, ownerClass, methodName, descriptor, false));
        Type retType = returnType instanceof Type ? (Type) returnType : Type.getType((String) returnType);
        instructions.add(new InsnNode(getReturnOpcodeFromType(retType)));
        invoker.instructions = instructions;
        return invoker;
    }

    public static int getReturnOpcodeFromType(Type type) {
        switch (type.getSort()) {
            case Type.INT:
            case Type.BYTE:
            case Type.CHAR:
            case Type.SHORT:
            case Type.BOOLEAN:
                return IRETURN;
            case Type.VOID:
                return RETURN;
            case Type.FLOAT:
                return FRETURN;
            case Type.LONG:
                return LRETURN;
            case Type.DOUBLE:
                return DRETURN;
            default:
                return ARETURN;
        }
    }

    public static int getLoadOpcodeFromType(Type type) {
        switch (type.getSort()) {
            case Type.INT:
            case Type.BYTE:
            case Type.CHAR:
            case Type.SHORT:
            case Type.BOOLEAN:
                return ILOAD;
            case Type.FLOAT:
                return FLOAD;
            case Type.LONG:
                return LLOAD;
            case Type.DOUBLE:
                return DLOAD;
            default:
                return ALOAD;
        }
    }

    public static int getStoreOpcodeFromType(Type type) {
        switch (type.getSort()) {
            case Type.INT:
            case Type.BYTE:
            case Type.CHAR:
            case Type.SHORT:
            case Type.BOOLEAN:
                return ISTORE;
            case Type.FLOAT:
                return FSTORE;
            case Type.LONG:
                return LSTORE;
            case Type.DOUBLE:
                return DSTORE;
            default:
                return ASTORE;
        }
    }
}
