package foxiwhitee.hellmod.utils.asm;

import java.io.PrintWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.TraceClassVisitor;

public class SpecialClassNode extends ClassNode {
    public SpecialClassNode() {
        super(327680);
    }

    public FieldGetterBuilder generateGetterForField(String name) {
        return new FieldGetterBuilder(this, name);
    }

    public FieldSetterBuilder generateSetterForField(String name) {
        return new FieldSetterBuilder(this, name);
    }

    public MethodInvokerBuilder generateInvokerForMethod(String name) {
        return new MethodInvokerBuilder(this, name);
    }

    public InterfaceAdderBuilder addInterface(String className) {
        return new InterfaceAdderBuilder(this, className);
    }

    public FieldNode getField(String name) {
        for (FieldNode node : this.fields) {
            if (node.name.equals(name))
                return node;
        }
        throw new IllegalArgumentException("Field not found");
    }

    public FieldNode getField(String name, String desc) {
        for (FieldNode node : this.fields) {
            if (node.name.equals(name) && node.desc.equals(desc))
                return node;
        }
        throw new IllegalArgumentException("Field not found");
    }

    public MethodNode getMethod(String name) {
        for (MethodNode node : this.methods) {
            if (node.name.equals(name))
                return node;
        }
        throw new IllegalArgumentException("Method not found");
    }

    public MethodNode getMethod(String name, String desc) {
        for (MethodNode node : this.methods) {
            if (node.name.equals(name) && node.desc.equals(desc))
                return node;
        }
        throw new IllegalArgumentException("Method not found");
    }

    public MethodNode getMethod(String name, MethodDescription desc) {
        return getMethod(name, desc.build());
    }

    public void trace(PrintWriter writer) {
        TraceClassVisitor visitor = new TraceClassVisitor(writer);
        accept((ClassVisitor)visitor);
    }

    public void trace() {
        trace(new PrintWriter(System.out));
    }
}
