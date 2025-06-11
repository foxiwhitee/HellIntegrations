package foxiwhitee.hellmod.utils.asm;

import java.util.Locale;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class MethodInvokerBuilder {
    private final SpecialClassNode classNode;

    private final String name;

    private String invokerName = "";

    private MethodDescription desc = null;

    private boolean isStatic = false;

    private boolean isSpecial = false;

    public MethodInvokerBuilder(SpecialClassNode classNode, String name) {
        this.classNode = classNode;
        this.name = name;
    }

    public MethodInvokerBuilder desc(MethodDescription desc) {
        this.desc = desc;
        return this;
    }

    public MethodInvokerBuilder setStatic(boolean aStatic) {
        this.isStatic = aStatic;
        return this;
    }

    public MethodInvokerBuilder setSpecial(boolean special) {
        if (this.isStatic && special)
            throw new IllegalArgumentException("Method cannot be both and static and special");
        this.isSpecial = special;
        return this;
    }

    public MethodInvokerBuilder invokerName(String name) {
        this.invokerName = name;
        return this;
    }

    public void build() {
        MethodNode methodNode = (this.desc == null) ? this.classNode.getMethod(this.name) : this.classNode.getMethod(this.name, this.desc);
        if (this.invokerName.isEmpty())
            this.invokerName = "invoke" + this.name.substring(0, 1).toUpperCase(Locale.ROOT) + this.name.substring(1);
        if (this.desc == null)
            this.desc = new MethodDescription(methodNode);
        MethodNode generated = new MethodNode(0x1 | (this.isStatic ? 8 : 0), this.invokerName, methodNode.desc, methodNode.signature, (String[])methodNode.exceptions.toArray((Object[])new String[0]));
        InsnList list = generated.instructions;
        if (!this.isStatic)
            list.add((AbstractInsnNode)new VarInsnNode(25, 0));
        for (int i = 0; i < this.desc.getArgumentsSize(); i++)
            list.add((AbstractInsnNode)new VarInsnNode(ASMUtils.getLoadOpcodeFromType(this.desc.getArgument(i)), this.isStatic ? i : (i + 1)));
        list.add((AbstractInsnNode)new MethodInsnNode(this.isStatic ? 184 : (this.isSpecial ? 183 : 182), this.classNode.name, methodNode.name, methodNode.desc, false));
        list.add((AbstractInsnNode)new InsnNode(ASMUtils.getReturnOpcodeFromType(this.desc.getReturnType())));
        this.classNode.methods.add(generated);
    }
}
