package foxiwhitee.hellmod.utils.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.common.patcher.ClassPatchManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import foxiwhitee.hellmod.asm.ASMCoreMod;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ComputeFramesClassWriter extends ClassWriter {
    private static final LaunchClassLoader CLASSLOADER = (LaunchClassLoader)ComputeFramesClassWriter.class.getClassLoader();

    private static final String JAVA_LANG_OBJECT = "java/lang/Object";

    public ComputeFramesClassWriter() {
        super(2);
    }

    private static ClassReader getClassNode(String name) {
        try {
            byte[] classBytes = ClassPatchManager.INSTANCE.getPatchedResource(ASMCoreMod.isObfEnv ? FMLDeobfuscatingRemapper.INSTANCE.unmap(name) : name, FMLDeobfuscatingRemapper.INSTANCE
                    .map(name), CLASSLOADER);
            if (classBytes != null) {
                ClassReader reader = new ClassReader(classBytes);
                return reader;
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getSuperTypesStack(String type, ClassReader node) {
        try {
            if (node == null) {
                Class<?> cls1 = Class.forName(type.replace('/', '.'), false, (ClassLoader)CLASSLOADER);
                if (cls1.isInterface())
                    return null;
                return getSuperTypesStack(cls1);
            }
            return getSuperTypesStack(node);
        } catch (ClassNotFoundException e) {
            return Collections.emptyList();
        }
    }

    private static List<String> getSuperTypesStack(ClassReader node) {
        String superName = FMLDeobfuscatingRemapper.INSTANCE.map(node.getSuperName());
        if (superName.equals("java/lang/Object"))
            return Collections.emptyList();
        List<String> list = new ArrayList<>(4);
        list.add(superName);
        getSuperTypesStack(list, superName);
        return list;
    }

    private static List<String> getSuperTypesStack(Class<?> cls) {
        if (cls.getSuperclass() == Object.class)
            return Collections.emptyList();
        List<String> list = new ArrayList<>(4);
        while ((cls = cls.getSuperclass()) != Object.class)
            list.add(cls.getName().replace('.', '/'));
        return list;
    }

    private static void getSuperTypesStack(List<String> list, String name) {
        ClassReader node = getClassNode(name);
        if (node != null) {
            String superName = FMLDeobfuscatingRemapper.INSTANCE.map(node.getSuperName());
            if (!superName.equals("java/lang/Object")) {
                list.add(superName);
                getSuperTypesStack(list, superName);
            }
        } else {
            try {
                Class<?> cls = Class.forName(name.replace('/', '.'), false, (ClassLoader)CLASSLOADER);
                for (; cls != Object.class; cls = cls.getSuperclass())
                    list.add(cls.getName().replace('.', '/'));
            } catch (ClassNotFoundException classNotFoundException) {}
        }
    }

    protected String getCommonSuperClass(String type1, String type2) {
        if (type1.equals(type2))
            return type1;
        if (type1.equals("java/lang/Object") || type2.equals("java/lang/Object"))
            return "java/lang/Object";
        ClassReader node1 = getClassNode(type1);
        ClassReader node2 = getClassNode(type2);
        if ((node1 != null && (node1.getAccess() & 0x200) != 0) || (node2 != null && (node2.getAccess() & 0x200) != 0))
            return "java/lang/Object";
        List<String> sup1 = getSuperTypesStack(type1, node1);
        if (sup1 == null)
            return "java/lang/Object";
        List<String> sup2 = getSuperTypesStack(type2, node2);
        if (sup2 == null)
            return "java/lang/Object";
        if (sup2.contains(type1))
            return type1;
        if (sup1.contains(type2))
            return type2;
        if (sup1.isEmpty() || sup2.isEmpty())
            return "java/lang/Object";
        for (int i = 0; i < sup1.size(); i++) {
            String s1 = sup1.get(i);
            if (sup2.contains(s1))
                return s1;
        }
        return "java/lang/Object";
    }
}