package foxiwhitee.hellmod.asm.draconic;

import foxiwhitee.hellmod.asm.ASMClassTransformer;
import foxiwhitee.hellmod.utils.asm.ASMUtils;
import foxiwhitee.hellmod.utils.asm.ComputeFramesClassWriter;
import foxiwhitee.hellmod.utils.asm.SpecialClassNode;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.Collections;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class DraconicTransformer {
    public static final boolean IS_DEV = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        try {
            byte[] buffer = new byte[inputStream.available()];
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != buffer.length) {
                throw new IOException("Failed to read all bytes from InputStream");
            }
            return buffer;
        } finally {
            inputStream.close();
        }
    }

    private static void adaptBytecode(ClassNode classNode, String oldType, String newType) {
        for (FieldNode field : classNode.fields) {
            field.desc = field.desc.replace("L" + oldType + ";", "L" + newType + ";");
        }

        for (MethodNode method : classNode.methods) {
            method.desc = method.desc.replace("L" + oldType + ";", "L" + newType + ";");

            for (AbstractInsnNode insn : method.instructions.toArray()) {
                if (insn instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                    if (fieldInsn.owner.equals(oldType)) {
                        fieldInsn.owner = newType;
                    }
                    fieldInsn.desc = fieldInsn.desc.replace("L" + oldType + ";", "L" + newType + ";");
                } else if (insn instanceof MethodInsnNode) {
                    MethodInsnNode methodInsn = (MethodInsnNode) insn;
                    if (methodInsn.owner.equals(oldType)) {
                        methodInsn.owner = newType;
                    }
                    methodInsn.desc = methodInsn.desc.replace("L" + oldType + ";", "L" + newType + ";");
                } else if (insn instanceof TypeInsnNode) {
                    TypeInsnNode typeInsn = (TypeInsnNode) insn;
                    if (typeInsn.desc.equals(oldType)) {
                        typeInsn.desc = newType;
                    }
                }
            }

            if (method.localVariables != null) {
                for (LocalVariableNode local : method.localVariables) {
                    local.desc = local.desc.replace("L" + oldType + ";", "L" + newType + ";");
                }
            }

            if (method.exceptions != null) {
                for (int i = 0; i < method.exceptions.size(); i++) {
                    if (method.exceptions.get(i).equals(oldType)) {
                        method.exceptions.set(i, newType);
                    }
                }
            }
        }
    }

    public static byte[] transformDraconicBlock(byte[] basicClass) {
        try {
            ClassNode originalClass = ASMUtils.readClass(basicClass);

            String customClassPath = "foxiwhitee/hellmod/integration/draconic/blocks/DraconicBlock.class";
            InputStream customClassStream = DraconicTransformer.class.getClassLoader().getResourceAsStream(customClassPath);
            if (customClassStream == null) {
                System.err.println("Failed to find custom class: " + customClassPath);
                return basicClass;
            }
            byte[] customClassBytes = readInputStream(customClassStream);
            ClassNode customClass = ASMUtils.readClass(customClassBytes);

            String originalName = originalClass.name;
            String originalSuperName = originalClass.superName;
            String[] originalInterfaces = originalClass.interfaces.toArray(new String[0]);

            originalClass.fields.clear();
            originalClass.methods.clear();
            originalClass.innerClasses.clear();

            originalClass.fields.addAll(customClass.fields);
            originalClass.methods.addAll(customClass.methods);
            originalClass.innerClasses.addAll(customClass.innerClasses);
            originalClass.version = customClass.version;
            originalClass.access = customClass.access;
            originalClass.signature = customClass.signature;
            originalClass.sourceFile = customClass.sourceFile;

            originalClass.name = originalName;
            originalClass.superName = originalSuperName;
            originalClass.interfaces.clear();
            for (String iface : originalInterfaces) {
                originalClass.interfaces.add(iface);
            }

            adaptBytecode(originalClass, customClass.name, originalName);
            byte[] modifiedClass = ASMUtils.writeClass(originalClass);
            try {
                Path path = Paths.get("DraconicBlock.class");
                Files.write(path, modifiedClass);
                System.out.println("[SUCCESS] Файл DraconicBlock.class записано!");
            } catch (Exception e) {
                System.out.println("[NOT SUCCESS] Файл DraconicBlock.class не записано!");
                e.printStackTrace();
            }
            return ASMUtils.writeClass(originalClass);
        } catch (Exception e) {
            System.err.println("Failed to transform DraconicBlock: " + e.getMessage());
            e.printStackTrace();
            return basicClass;
        }
    }

    public static byte[] transformMinecraft(byte[] basicClass) {
        if (basicClass == null) return null;

        long start = System.currentTimeMillis();

        SpecialClassNode classNode = new SpecialClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        boolean modified = false;
        classNode.interfaces.add("foxiwhitee/hellmod/integration/draconic/helpers/gui/accessors/IMinecraftAccessor");
        classNode.generateGetterForField(IS_DEV ? "timer" : "field_71428_T")
                .getterMethodName("getTimer")
                .build();
        modified = true;

        boolean shouldComputeFrames = (classNode.version & 0xFFFF) > 50;
        if (!modified && !shouldComputeFrames) return basicClass;

        ClassWriter writer = shouldComputeFrames ? new ComputeFramesClassWriter() : new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        byte[] transformedClass = writer.toByteArray();

        return transformedClass;
    }

}
