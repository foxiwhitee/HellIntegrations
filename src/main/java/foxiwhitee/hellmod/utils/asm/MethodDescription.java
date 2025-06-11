package foxiwhitee.hellmod.utils.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

public class MethodDescription {
    private final List<Type> arguments;

    private final Type returnType;

    public MethodDescription(Type returnType, Type... arguments) {
        this.returnType = returnType;
        this.arguments = new ArrayList<>(Arrays.asList(arguments));
    }

    public MethodDescription(String returnType, String... arguments) {
        this.returnType = Type.getType(returnType);
        this.arguments = new ArrayList<>((Collection<? extends Type>)Arrays.<String>stream(arguments).map(Type::getType).collect(Collectors.toList()));
    }

    public MethodDescription(MethodNode methodNode) {
        this.returnType = Type.getReturnType(methodNode.desc);
        this.arguments = new ArrayList<>((Collection<? extends Type>)Arrays.<Type>stream(Type.getArgumentTypes(methodNode.desc)).collect(Collectors.toList()));
    }

    public String build() {
        StringBuilder desc = new StringBuilder("(");
        for (Type t : this.arguments)
            desc.append(t.getDescriptor());
        desc.append(")");
        desc.append(this.returnType.getDescriptor());
        return desc.toString();
    }

    public int getArgumentsSize() {
        return this.arguments.size();
    }

    public Type getArgument(int index) {
        return this.arguments.get(index);
    }

    public Type getReturnType() {
        return this.returnType;
    }

    public void addArgument(Type type) {
        this.arguments.add(type);
    }

    public void addArgument(String type) {
        addArgument(Type.getType(type));
    }

    public void addArguments(Type... types) {
        Arrays.<Type>stream(types).forEach(this::addArgument);
    }

    public void addArguments(String... types) {
        Arrays.<String>stream(types).forEach(this::addArgument);
    }

    public void addArguments(List<Type> types) {
        types.forEach(this::addArgument);
    }

    public void addArguments(MethodDescription desc) {
        addArguments(desc.arguments);
    }
}