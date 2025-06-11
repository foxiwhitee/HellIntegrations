package foxiwhitee.hellmod.utils.asm;

import java.util.Locale;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class FieldSetterBuilder {
    private final SpecialClassNode classNode;

    private final String name;

    private String desc = "";

    private boolean isStatic = false;

    private String setterMethodName = "";

    public FieldSetterBuilder(SpecialClassNode classNode, String name) {
        this.classNode = classNode;
        this.name = name;
    }

    public FieldSetterBuilder desc(String desc) {
        this.desc = desc;
        return this;
    }

    public FieldSetterBuilder setStatic(boolean aStatic) {
        this.isStatic = aStatic;
        return this;
    }

    public FieldSetterBuilder setterMethodName(String name) {
        this.setterMethodName = name;
        return this;
    }

    public void build() {
        FieldNode fieldNode = this.desc.isEmpty() ? this.classNode.getField(this.name) : this.classNode.getField(this.name, this.desc);
        if (this.setterMethodName.isEmpty())
            this.setterMethodName = "set" + this.name.substring(0, 1).toUpperCase(Locale.ROOT) + this.name.substring(1);
        MethodNode node = new MethodNode(0x1 | (this.isStatic ? 8 : 0), this.setterMethodName, String.format("(%s)V", new Object[] { fieldNode.desc }), null, null);
        InsnList list = node.instructions;
        if (!this.isStatic)
            list.add((AbstractInsnNode)new VarInsnNode(25, 0));
        list.add((AbstractInsnNode)new VarInsnNode(ASMUtils.getLoadOpcodeFromType(Type.getType(fieldNode.desc)), this.isStatic ? 0 : 1));
        list.add((AbstractInsnNode)new FieldInsnNode(this.isStatic ? 179 : 181, this.classNode.name, fieldNode.name, fieldNode.desc));
        list.add((AbstractInsnNode)new InsnNode(177));
        this.classNode.methods.add(node);
    }
}
