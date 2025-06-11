package foxiwhitee.hellmod.utils.asm;

import java.io.IOException;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class InterfaceAdderBuilder {
    private final SpecialClassNode classNode;

    private final String className;

    private String getterPrefix = "get";

    private String setterPrefix = "set";

    private String invokePrefix = "invoke";

    public InterfaceAdderBuilder(SpecialClassNode classNode, String className) {
        this.classNode = classNode;
        this.className = className;
    }

    public InterfaceAdderBuilder getter(String prefix) {
        this.getterPrefix = prefix;
        return this;
    }

    public InterfaceAdderBuilder setter(String prefix) {
        this.setterPrefix = prefix;
        return this;
    }

    public InterfaceAdderBuilder invoke(String prefix) {
        this.invokePrefix = prefix;
        return this;
    }

    public void build() {
        byte[] bytes = null;
        try {
            bytes = Launch.classLoader.getClassBytes(this.className);
        } catch (IOException e) {
            throw new IllegalArgumentException("Class " + this.className + " didn't found");
        }
        if (bytes == null)
            throw new IllegalArgumentException("Class " + this.className + " didn't found");
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept((ClassVisitor)classNode, 0);
        if ((classNode.access & 0x200) == 0)
            throw new IllegalArgumentException("Given class isn't interface");
        for (MethodNode node : classNode.methods) {
            String methodName = node.name;
            if (methodName.startsWith(this.getterPrefix)) {
                processGetter(node);
                continue;
            }
            if (methodName.startsWith(this.setterPrefix)) {
                processSetter(node);
                continue;
            }
            if (methodName.startsWith(this.invokePrefix))
                processInvoke(node);
        }
        this.classNode.interfaces.add(this.className.replace('.', '/'));
    }

    private void processGetter(MethodNode node) {
        String fieldName = node.name.substring(this.getterPrefix.length());
        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        Type[] args = Type.getArgumentTypes(node.desc);
        Type returnType = Type.getReturnType(node.desc);
        if (args.length != 0)
            throw new IllegalArgumentException("Method " + node.name + " mustn't has arguments");
        this.classNode.generateGetterForField(fieldName)
                .desc(returnType.getDescriptor())
                .getterMethodName(node.name)
                .setStatic(((node.access & 0x8) != 0))
                .build();
    }

    private void processSetter(MethodNode node) {
        String fieldName = node.name.substring(this.getterPrefix.length());
        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        Type[] args = Type.getArgumentTypes(node.desc);
        Type returnType = Type.getReturnType(node.desc);
        if (returnType != Type.VOID_TYPE || args.length != 1)
            throw new IllegalArgumentException("Method " + node.name + " must return void");
        this.classNode.generateSetterForField(fieldName)
                .desc(args[0].getDescriptor())
                .setterMethodName(node.name)
                .setStatic(((node.access & 0x8) != 0))
                .build();
    }

    private void processInvoke(MethodNode node) {
        String methodName = node.name.substring(this.invokePrefix.length());
        methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
        this.classNode.generateInvokerForMethod(methodName)
                .invokerName(node.name)
                .desc(new MethodDescription(node))
                .setStatic(((node.access & 0x8) != 0))
                .build();
    }
}
