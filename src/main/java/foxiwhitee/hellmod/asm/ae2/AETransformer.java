package foxiwhitee.hellmod.asm.ae2;

import foxiwhitee.hellmod.utils.asm.*;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;

import static foxiwhitee.hellmod.utils.asm.ASMUtils.*;
import static org.objectweb.asm.Opcodes.*;

public class AETransformer {
    public static final String HOOKS = "foxiwhitee/hellmod/asm/ae2/AEHooks";
    public static final boolean IS_DEV = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
//        byte[] modifiedClass = writeClass(classNode);
//        try {
//            Path path = Paths.get("ModifiedCraftingCPUCluster.class");
//            Files.write(path, modifiedClass);
//            System.out.println("[SUCCESS] Файл ModifiedCraftingCPUCluster.class записано!");
//        } catch (Exception e) {
//            System.out.println("[NOT SUCCESS] Файл ModifiedCraftingCPUCluster.class не записано!");
//            e.printStackTrace();
//        }

    private static boolean isBytesAssignmentSequence(InsnList instructions, int index) {
        if (index + 7 >= instructions.size()) return false;
        AbstractInsnNode[] nodes = new AbstractInsnNode[8];
        for (int i = 0; i < 8; i++) {
            nodes[i] = instructions.get(index + i);
        }

        return nodes[0].getOpcode() == ALOAD && ((VarInsnNode) nodes[0]).var == 0
                && nodes[1].getOpcode() == GETFIELD && ((FieldInsnNode) nodes[1]).name.equals("bytes") && ((FieldInsnNode) nodes[1]).desc.equals("I")
                && nodes[2].getOpcode() == I2L
                && nodes[3].getOpcode() == ALOAD && ((VarInsnNode) nodes[3]).var == 4
                && nodes[4].getOpcode() == INVOKEINTERFACE && ((MethodInsnNode) nodes[4]).name.equals("getStackSize") && ((MethodInsnNode) nodes[4]).desc.equals("()J")
                && nodes[5].getOpcode() == LADD
                && nodes[6].getOpcode() == L2I
                && nodes[7].getOpcode() == PUTFIELD && ((FieldInsnNode) nodes[7]).name.equals("bytes") && ((FieldInsnNode) nodes[7]).desc.equals("I");
    }

    public static byte[] transformCraftingTreeNode(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        MethodNode targetMethod = findMethod(classNode, "<init>");
        for (int i = 0; i < targetMethod.instructions.size() - 8; i++) {
            AbstractInsnNode node = targetMethod.instructions.get(i);
            if (isBytesAssignmentSequence(targetMethod.instructions, i)) {
                for (int j = 0; j < 8; j++) {
                    targetMethod.instructions.remove(targetMethod.instructions.get(i));
                }
                InsnList newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(ALOAD, 0));
                newInstructions.add(new FieldInsnNode(GETFIELD, classNode.name, "bytes", "I"));
                newInstructions.add(new VarInsnNode(ALOAD, 4));
                newInstructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/api/storage/data/IAEItemStack", "getStackSize", "()J", true));
                newInstructions.add(new MethodInsnNode(INVOKESTATIC, "ua/serstarstory/be/asm/AEHooks", "skipBytes", "(IJ)I", false));
                newInstructions.add(new VarInsnNode(ALOAD, 0));
                newInstructions.add(new FieldInsnNode(PUTFIELD, classNode.name, "bytes", "I"));
                targetMethod.instructions.insertBefore(node, newInstructions);
                i += newInstructions.size() - 1;
            }
        }
        return writeClass(classNode);
    }

    public static byte[] transformCraftingCPUCalculator(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        MethodNode targetMethod = findMethod(classNode, "updateTiles");
        if (targetMethod != null) {
            AbstractInsnNode insertionPoint = null;
            for (AbstractInsnNode insn : targetMethod.instructions.toArray()) {
                if (insn.getOpcode() == ALOAD &&
                        insn.getNext() != null && insn.getNext().getOpcode() == ILOAD &&
                        insn.getNext().getNext() != null && insn.getNext().getNext().getOpcode() == ILOAD &&
                        insn.getNext().getNext().getNext() != null && insn.getNext().getNext().getNext().getOpcode() == ILOAD &&
                        insn.getNext().getNext().getNext().getNext() != null && insn.getNext().getNext().getNext().getNext().getOpcode() == INVOKEVIRTUAL &&
                        ((MethodInsnNode) insn.getNext().getNext().getNext().getNext()).name.equals("getTileEntity") &&
                        ((MethodInsnNode) insn.getNext().getNext().getNext().getNext()).desc.equals("(III)Lnet/minecraft/tileentity/TileEntity;") &&
                        insn.getNext().getNext().getNext().getNext().getNext() != null && insn.getNext().getNext().getNext().getNext().getNext().getOpcode() == CHECKCAST &&
                        ((TypeInsnNode) insn.getNext().getNext().getNext().getNext().getNext()).desc.equals("appeng/tile/crafting/TileCraftingTile") &&
                        insn.getNext().getNext().getNext().getNext().getNext().getNext() != null && insn.getNext().getNext().getNext().getNext().getNext().getNext().getOpcode() == ASTORE) {
                    insertionPoint = insn.getNext().getNext().getNext().getNext().getNext().getNext();
                    break;
                }
            }
            if (insertionPoint != null) {
                InsnList list = new InsnList();
                LabelNode label = new LabelNode();
                list.add(new VarInsnNode(ALOAD, 9));
                list.add(new TypeInsnNode(INSTANCEOF, "foxiwhitee/hellmod/tile/cpu/TileMEServer"));
                list.add(new JumpInsnNode(IFEQ, label));
                list.add(new InsnNode(RETURN));
                list.add(label);
                targetMethod.instructions.insert(insertionPoint, list);
            }
        }
        return writeClass(classNode);
    }

    public static byte[] transformCraftingGridCache(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        classNode.interfaces.add("foxiwhitee/hellmod/utils/craft/ICraftingGridCacheAddition");
        MethodNode targetMethod = findMethod(classNode, "updateCPUClusters");
        if (targetMethod != null) {
            AbstractInsnNode insertionPoint = locateOpcodeSequence(targetMethod.instructions, RETURN);
            if (insertionPoint != null) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "appeng/me/cache/CraftingGridCache", "grid", "Lappeng/api/networking/IGrid;"));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "updateCPUClusters", "(Lappeng/api/networking/IGrid;Lfoxiwhitee/hellmod/utils/craft/ICraftingGridCacheAddition;)V", false));

                targetMethod.instructions.insertBefore(insertionPoint, list);
            }
        }

        MethodNode method = new MethodNode(ACC_PUBLIC, "getCraftingCPUClusters", "()Ljava/util/Set;", "()Ljava/util/Set<Lappeng/me/cluster/implementations/CraftingCPUCluster;>;", null);
        method.instructions.add(new VarInsnNode(ALOAD, 0));
        method.instructions.add(new FieldInsnNode(GETFIELD, "appeng/me/cache/CraftingGridCache", "craftingCPUClusters", "Ljava/util/Set;"));
        method.instructions.add(new InsnNode(ARETURN));
        classNode.methods.add(method);
        return writeClass(classNode);
    }

    public static byte[] transformICraftingMedium(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        classNode.interfaces.add("foxiwhitee/hellmod/utils/craft/IPreCraftingMedium");
        return writeClass(classNode);
    }

    public static byte[] transformGuiMEMonitorable(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        classNode.interfaces.add("foxiwhitee/hellmod/utils/craft/IGuiMEMonitorableAccessor");
        MethodNode generated = new MethodNode(ACC_PUBLIC, "callSetReservedSpace", "(I)V", (String)null, (String[])null);
        InsnList list = generated.instructions;
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new VarInsnNode(ILOAD, 1));
        list.add(new MethodInsnNode(INVOKESPECIAL, "appeng/client/gui/implementations/GuiMEMonitorable", "setReservedSpace", "(I)V", false));
        list.add(new InsnNode(RETURN));
        classNode.methods.add(generated);
        generated = new MethodNode(ACC_PUBLIC, "callSetStandardSize", "(I)V", (String)null, (String[])null);
        list = generated.instructions;
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new VarInsnNode(ILOAD, 1));
        list.add(new MethodInsnNode(INVOKESPECIAL, "appeng/client/gui/implementations/GuiMEMonitorable", "setStandardSize", "(I)V", false));
        list.add(new InsnNode(RETURN));
        classNode.methods.add(generated);
        return writeClass(classNode);
    }

    public static byte[] transformGuiPriority(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        MethodNode targetMethod = findMethod(classNode, "initGui", "func_73866_w_");
        if (targetMethod != null) {
            AbstractInsnNode insertionPoint = locateOpcodeSequence(targetMethod.instructions, ALOAD, GETFIELD, IFNULL, ALOAD, IFNULL);
            if (insertionPoint != null) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 6));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "appeng/client/gui/implementations/GuiPriority", "OriginalGui", "Lappeng/core/sync/GuiBridge;"));
                list.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "getGui", "(Ljava/lang/Object;Lappeng/core/sync/GuiBridge;)Lappeng/core/sync/GuiBridge;", false));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new InsnNode(SWAP));
                list.add(new FieldInsnNode(PUTFIELD, "appeng/client/gui/implementations/GuiPriority", "OriginalGui", "Lappeng/core/sync/GuiBridge;"));
                list.add(new VarInsnNode(ALOAD, 6));
                list.add(new VarInsnNode(ALOAD, 5));
                list.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "getItem", "(Ljava/lang/Object;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false));
                list.add(new VarInsnNode(ASTORE, 5));

                targetMethod.instructions.insertBefore(insertionPoint, list);
            }
        }
        return ASMUtils.writeClass(classNode);
    } //

    public static byte[] transformGrid(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        MethodNode targetMethod = findMethod(classNode, "getMachines");
        if (targetMethod != null) {
            List<AbstractInsnNode> insertionPoint = findAllOpcodes(targetMethod.instructions, ARETURN);
            if (insertionPoint != null) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "getMachines", "(Lappeng/me/MachineSet;Lappeng/me/Grid;)Lappeng/api/networking/IMachineSet;", false));
                insertionPoint.forEach(p -> targetMethod.instructions.insertBefore(p, copy(list)));
            }
        }
        
        MethodNode newMethod = new MethodNode(ACC_PUBLIC, "getMachinesSafe", "(Ljava/lang/Class;)Lappeng/api/networking/IMachineSet;", null, null);
        InsnList instructions = newMethod.instructions;
        LabelNode label = new LabelNode();
        instructions.add(new VarInsnNode(ALOAD, 0));
        instructions.add(new FieldInsnNode(GETFIELD, "appeng/me/Grid", "machines", "Ljava/util/Map;"));
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
        instructions.add(new TypeInsnNode(CHECKCAST, "appeng/me/MachineSet"));
        instructions.add(new InsnNode(DUP));
        instructions.add(new VarInsnNode(ASTORE, 2));
        instructions.add(new JumpInsnNode(IFNULL, label));
        instructions.add(new VarInsnNode(ALOAD, 2));
        instructions.add(new InsnNode(ARETURN));
        instructions.add(label);
        instructions.add(new TypeInsnNode(NEW, "appeng/me/MachineSet"));
        instructions.add(new InsnNode(DUP));
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new MethodInsnNode(INVOKESPECIAL, "appeng/me/MachineSet", "<init>", "(Ljava/lang/Class;)V", false));
        instructions.add(new InsnNode(ARETURN));
        classNode.methods.add(newMethod);

        return writeClass(classNode);
    }

    public static byte[] transformCraftingCPUCluster(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        classNode.interfaces.add("foxiwhitee/hellmod/utils/craft/ICraftingCPUClusterAccessor");

        MethodNode targetMethod = findMethod(classNode, "executeCrafting");
        if (targetMethod != null) {
            AbstractInsnNode insertionPoint = locateLastOpcodeSequence(targetMethod.instructions, NEW, DUP, NEW, DUP, INVOKESPECIAL, ICONST_0, ICONST_0, INVOKESPECIAL, ASTORE);
            if (insertionPoint != null) {
                InsnList list = new InsnList();
                LabelNode label = new LabelNode();
                list.add(new VarInsnNode(ALOAD, 5));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/api/networking/crafting/ICraftingPatternDetails", "isCraftable", "()Z", true));
                list.add(new JumpInsnNode(IFNE, label));
                list.add(new TypeInsnNode(NEW, "net/minecraft/inventory/InventoryCrafting"));
                list.add(new InsnNode(DUP));
                list.add(new TypeInsnNode(NEW, "appeng/container/ContainerNull"));
                list.add(new InsnNode(DUP));
                list.add(new MethodInsnNode(INVOKESPECIAL, "appeng/container/ContainerNull", "<init>", "()V", false));
                list.add(new VarInsnNode(ALOAD, 5));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/api/networking/crafting/ICraftingPatternDetails", "getCondensedInputs", "()[Lappeng/api/storage/data/IAEItemStack;", true));
                list.add(new InsnNode(ARRAYLENGTH));
                list.add(new InsnNode(ICONST_1));
                list.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/inventory/InventoryCrafting", "<init>", "(Lnet/minecraft/inventory/Container;II)V", false));
                list.add(new VarInsnNode(ASTORE, 6));
                list.add(label);
                targetMethod.instructions.insert(insertionPoint, list);
            }
        }
        MethodInsnNode targetInvoke = null;
        for (AbstractInsnNode insertionPoint : targetMethod.instructions.toArray()) {
            if (insertionPoint.getType() == 5) {
                MethodInsnNode invoke = (MethodInsnNode) insertionPoint;
                if (invoke.name.equals("pushPattern")) {
                    targetInvoke = invoke;
                    invoke.desc = "(Lappeng/api/networking/crafting/ICraftingPatternDetails;Lnet/minecraft/inventory/InventoryCrafting;Lappeng/me/cluster/implementations/CraftingCPUCluster;)Z";
                    break;
                }
            }
        }
        if (targetInvoke != null) {
            targetMethod.instructions.insertBefore(targetInvoke, new VarInsnNode(ALOAD, 0));
        }
        MethodNode newMethod = new MethodNode(ACC_PUBLIC, "getWaitingFor", "(Lappeng/api/networking/crafting/ICraftingPatternDetails;)J", null, null);
        InsnList list = newMethod.instructions;
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, classNode.name, "tasks", "Ljava/util/Map;"));
        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
        list.add(new TypeInsnNode(CHECKCAST, "appeng/me/cluster/implementations/CraftingCPUCluster$TaskProgress"));
        list.add(new MethodInsnNode(INVOKESTATIC, "appeng/me/cluster/implementations/CraftingCPUCluster$TaskProgress", "access$000", "(Lappeng/me/cluster/implementations/CraftingCPUCluster$TaskProgress;)J", false));
        list.add(new InsnNode(LRETURN));
        classNode.methods.add(newMethod);

        newMethod = new MethodNode(ACC_PUBLIC, "setWaitingFor", "(Lappeng/api/networking/crafting/ICraftingPatternDetails;J)V", null, null);
        list = newMethod.instructions;
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, classNode.name, "tasks", "Ljava/util/Map;"));
        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
        list.add(new TypeInsnNode(CHECKCAST, "appeng/me/cluster/implementations/CraftingCPUCluster$TaskProgress"));
        list.add(new VarInsnNode(LLOAD, 2));
        list.add(new MethodInsnNode(INVOKESTATIC, "appeng/me/cluster/implementations/CraftingCPUCluster$TaskProgress", "access$002", "(Lappeng/me/cluster/implementations/CraftingCPUCluster$TaskProgress;J)J", false));
        list.add(new InsnNode(POP2));
        list.add(new InsnNode(RETURN));
        classNode.methods.add(newMethod);

        classNode.methods.add(createFieldAccessor(classNode.name, "waitingFor", Type.getType("Lappeng/api/storage/data/IItemList;"), false, null));

        classNode.methods.add(createMethodInvoker(classNode.name, "postChange", "V", new String[]{"Lappeng/api/storage/data/IAEItemStack;", "Lappeng/api/networking/security/BaseActionSource;"}, false, null));

        classNode.methods.add(createMethodInvoker(classNode.name, "postCraftingStatusChange", "V", new String[]{"Lappeng/api/storage/data/IAEItemStack;"}, false, null));
        targetMethod = ASMUtils.findMethod(classNode, "addTile");
        if (targetMethod != null) {
            AbstractInsnNode insertionPoint = ASMUtils.locateLastOpcodeSequence(targetMethod.instructions, ALOAD, DUP, GETFIELD, ICONST_1, IADD, PUTFIELD);
            if (insertionPoint != null) {
                list = new InsnList();
                LabelNode label = new LabelNode();
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new TypeInsnNode(INSTANCEOF, "foxiwhitee/hellmod/utils/craft/ICustomAccelerator"));
                list.add(new JumpInsnNode(IFEQ, label));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new TypeInsnNode(CHECKCAST, "foxiwhitee/hellmod/utils/craft/ICustomAccelerator"));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "foxiwhitee/hellmod/utils/craft/ICustomAccelerator", "getAcceleratorCount", "()I", true));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "appeng/me/cluster/implementations/CraftingCPUCluster", "accelerator", "I"));
                list.add(new InsnNode(IADD));
                list.add(new InsnNode(ICONST_M1));
                list.add(new InsnNode(IADD));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new InsnNode(SWAP));
                list.add(new FieldInsnNode(PUTFIELD, "appeng/me/cluster/implementations/CraftingCPUCluster", "accelerator", "I"));
                list.add(label);
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new InsnNode(DUP));
                list.add(new FieldInsnNode(GETFIELD, classNode.name, "availableStorage", "J"));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "getStorageBytes", "(JLappeng/tile/crafting/TileCraftingTile;)J", false));
                list.add(new FieldInsnNode(PUTFIELD, classNode.name, "availableStorage", "J"));
                targetMethod.instructions.insert(list);
            }
            insertionPoint = ASMUtils.locateLastOpcodeSequence(targetMethod.instructions, ALOAD, INVOKEVIRTUAL, ALOAD, GETFIELD, ALOAD, INVOKEVIRTUAL);
            if (insertionPoint != null) {
                LabelNode labelEnd = new LabelNode();
                list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new TypeInsnNode(INSTANCEOF, "foxiwhitee/hellmod/tile/cpu/TileMEServer"));
                list.add(new JumpInsnNode(IFEQ, labelEnd));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new TypeInsnNode(CHECKCAST, "foxiwhitee/hellmod/tile/cpu/TileMEServer"));
                list.add(new VarInsnNode(ASTORE, 10));
                list.add(new VarInsnNode(ALOAD, 10));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "foxiwhitee/hellmod/tile/cpu/TileMEServer", "getClusterIndex", "(Lappeng/me/cluster/implementations/CraftingCPUCluster;)I", false));
                list.add(new VarInsnNode(ISTORE, 11));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "appeng/me/cluster/implementations/CraftingCPUCluster", "availableStorage", "J"));
                list.add(new VarInsnNode(ALOAD, 10));
                list.add(new VarInsnNode(ILOAD, 11));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "foxiwhitee/hellmod/tile/cpu/TileMEServer", "getClusterStorageBytes", "(I)J", false));
                list.add(new InsnNode(LADD));
                list.add(new FieldInsnNode(PUTFIELD, "appeng/me/cluster/implementations/CraftingCPUCluster", "availableStorage", "J"));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "appeng/me/cluster/implementations/CraftingCPUCluster", "accelerator", "I"));
                list.add(new VarInsnNode(ALOAD, 10));
                list.add(new VarInsnNode(ILOAD, 11));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "foxiwhitee/hellmod/tile/cpu/TileMEServer", "getClusterAccelerator", "(I)I", false));
                list.add(new InsnNode(IADD));
                list.add(new FieldInsnNode(PUTFIELD, "appeng/me/cluster/implementations/CraftingCPUCluster", "accelerator", "I"));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "appeng/me/cluster/implementations/CraftingCPUCluster", "storage", "Ljava/util/List;"));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true));
                list.add(new InsnNode(POP));
                list.add(labelEnd);
                targetMethod.instructions.insertBefore(insertionPoint, list);
            }
        }

        byte[] modifiedClass = writeClass(classNode);
        try {
            Path path = Paths.get("ModifiedCraftingCPUCluster.class");
            Files.write(path, modifiedClass);
            System.out.println("[SUCCESS] Файл ModifiedCraftingCPUCluster.class записано!");
        } catch (Exception e) {
            System.out.println("[NOT SUCCESS] Файл ModifiedCraftingCPUCluster.class не записано!");
            e.printStackTrace();
        }
        return writeClass(classNode);
    }

    public static byte[] transformDualityInterface(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        MethodNode methodNode = findMethod(classNode, "updateCraftingList");
        if (methodNode != null) {

            classNode.interfaces.add("foxiwhitee/hellmod/utils/interfaces/ICustomDualityInterface");

            InsnList list = new InsnList();
            LabelNode label = new LabelNode();
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new TypeInsnNode(CHECKCAST, "foxiwhitee/hellmod/utils/interfaces/ICustomDualityInterface"));
            list.add(new MethodInsnNode(INVOKEINTERFACE, "foxiwhitee/hellmod/utils/interfaces/ICustomDualityInterface", "isOverrideDefault", "()Z", true));
            list.add(new JumpInsnNode(IFEQ, label));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new TypeInsnNode(CHECKCAST, "foxiwhitee/hellmod/utils/interfaces/ICustomDualityInterface"));
            list.add(new MethodInsnNode(INVOKEINTERFACE, "foxiwhitee/hellmod/utils/interfaces/ICustomDualityInterface", "updateCraftingListProxy", "()V", true));
            list.add(new InsnNode(RETURN));
            list.add(label);

            methodNode.instructions.insert(list);

            classNode.methods.add(createFieldGetter("getProxy", "Lappeng/me/helpers/AENetworkProxy;", GETFIELD, "appeng/helpers/DualityInterface", "gridProxy", ARETURN));

            classNode.methods.add(createFieldGetter("getCraftingList", "Ljava/util/List;", GETFIELD, "appeng/helpers/DualityInterface", "craftingList", ARETURN));

            classNode.methods.add(createFieldGetter("getIsWorking", "Z", GETFIELD, "appeng/helpers/DualityInterface", "isWorking", IRETURN));

            MethodNode generated = new MethodNode(ASM5, ACC_PUBLIC, "callAddToCraftingList", "(Lnet/minecraft/item/ItemStack;)V", null, null);
            list = generated.instructions;
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new MethodInsnNode(INVOKESPECIAL, "appeng/helpers/DualityInterface", "addToCraftingList", "(Lnet/minecraft/item/ItemStack;)V", false));
            list.add(new InsnNode(RETURN));
            classNode.methods.add(generated);

            generated = new MethodNode(ASM5, ACC_PUBLIC, "callReadConfig", "()V", null, null);
            list = generated.instructions;
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new MethodInsnNode(INVOKESPECIAL, "appeng/helpers/DualityInterface", "readConfig", "()V", false));
            list.add(new InsnNode(RETURN));
            classNode.methods.add(generated);

            generated = new MethodNode(ASM5, ACC_PUBLIC, "callUpdateCraftingList", "()V", null, null);
            list = generated.instructions;
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new MethodInsnNode(INVOKESPECIAL, "appeng/helpers/DualityInterface", "updateCraftingList", "()V", false));
            list.add(new InsnNode(RETURN));
            classNode.methods.add(generated);

            generated = new MethodNode(ASM5, ACC_PUBLIC, "callUpdatePlan", "(I)V", null, null);
            list = generated.instructions;
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new VarInsnNode(ILOAD, 1));
            list.add(new MethodInsnNode(INVOKESPECIAL, "appeng/helpers/DualityInterface", "updatePlan", "(I)V", false));
            list.add(new InsnNode(RETURN));
            classNode.methods.add(generated);

            generated = new MethodNode(ASM5, ACC_PUBLIC, "callHasWorkToDo", "()Z", null, null);
            list = generated.instructions;
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new MethodInsnNode(INVOKESPECIAL, "appeng/helpers/DualityInterface", "hasWorkToDo", "()Z", false));
            list.add(new InsnNode(IRETURN));
            classNode.methods.add(generated);
        }
        return writeClass(classNode);
    }

    public static byte[] transformTileInterface(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        classNode.interfaces.add("foxiwhitee/hellmod/utils/interfaces/ICustomTileInterface");
        MethodNode generated = new MethodNode(ASM5, ACC_PUBLIC, "setDuality", "(Lappeng/helpers/DualityInterface;)V", null, null);
        InsnList list = generated.instructions;
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new FieldInsnNode(PUTFIELD, "appeng/tile/misc/TileInterface", "duality", "Lappeng/helpers/DualityInterface;"));
        list.add(new InsnNode(RETURN));
        classNode.methods.add(generated);

        if (findMethod(classNode, "getPatternsConfigurations") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "getPatternsConfigurations", "()[Lfoxiwhitee/hellmod/helpers/IInterfaceTerminalSupport$PatternsConfiguration;", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new InsnNode(ICONST_1));
            instructions.add(new TypeInsnNode(ANEWARRAY, "foxiwhitee/hellmod/helpers/IInterfaceTerminalSupport$PatternsConfiguration"));
            instructions.add(new VarInsnNode(ASTORE, 1));
            instructions.add(new VarInsnNode(ALOAD, 1));
            instructions.add(new InsnNode(ICONST_0));
            instructions.add(new TypeInsnNode(NEW, "foxiwhitee/hellmod/helpers/IInterfaceTerminalSupport$PatternsConfiguration"));
            instructions.add(new InsnNode(DUP));
            instructions.add(new InsnNode(ICONST_0));
            instructions.add(new IntInsnNode(BIPUSH, 9));
            instructions.add(new MethodInsnNode(INVOKESPECIAL, "foxiwhitee/hellmod/helpers/IInterfaceTerminalSupport$PatternsConfiguration", "<init>", "(II)V", false));
            instructions.add(new InsnNode(AASTORE));
            instructions.add(new VarInsnNode(ALOAD, 1));
            instructions.add(new InsnNode(ARETURN));
            classNode.methods.add(methodNode);
        }
        if (ASMUtils.findMethod(classNode, "getPatterns") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "getPatterns", "(I)Lnet/minecraft/inventory/IInventory;", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new VarInsnNode(ALOAD, 0));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/helpers/IInterfaceHost", "getInterfaceDuality", "()Lappeng/helpers/DualityInterface;", true));
            instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "appeng/helpers/DualityInterface", "getPatterns", "()Lnet/minecraft/inventory/IInventory;", false));
            instructions.add(new InsnNode(ARETURN));
            classNode.methods.add(methodNode);
        }
        if (ASMUtils.findMethod(classNode, "shouldDisplay") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "shouldDisplay", "()Z", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new VarInsnNode(ALOAD, 0));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/helpers/IInterfaceHost", "getInterfaceDuality", "()Lappeng/helpers/DualityInterface;", true));
            instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "appeng/helpers/DualityInterface", "getConfigManager", "()Lappeng/api/util/IConfigManager;", false));
            instructions.add(new FieldInsnNode(GETSTATIC, "appeng/api/config/Settings", "INTERFACE_TERMINAL", "Lappeng/api/config/Settings;"));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/api/util/IConfigManager", "getSetting", "(Lappeng/api/config/Settings;)Ljava/lang/Enum;", true));
            instructions.add(new TypeInsnNode(CHECKCAST, "appeng/api/config/YesNo"));
            instructions.add(new FieldInsnNode(GETSTATIC, "appeng/api/config/YesNo", "YES", "Lappeng/api/config/YesNo;"));
            LabelNode labelFalse = new LabelNode();
            instructions.add(new JumpInsnNode(IF_ACMPNE, labelFalse));
            instructions.add(new InsnNode(ICONST_1));
            LabelNode labelEnd = new LabelNode();
            instructions.add(new JumpInsnNode(GOTO, labelEnd));
            instructions.add(labelFalse);
            instructions.add(new InsnNode(ICONST_0));
            instructions.add(labelEnd);
            instructions.add(new InsnNode(IRETURN));
            classNode.methods.add(methodNode);
        }
        if (ASMUtils.findMethod(classNode, "getName") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "getName", "()Ljava/lang/String;", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new VarInsnNode(ALOAD, 0));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/helpers/IInterfaceHost", "getInterfaceDuality", "()Lappeng/helpers/DualityInterface;", true));
            instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "appeng/helpers/DualityInterface", "getTermName", "()Ljava/lang/String;", false));
            instructions.add(new InsnNode(ARETURN));
            classNode.methods.add(methodNode);
        }
        if (ASMUtils.findMethod(classNode, "getSortValue") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "getSortValue", "()J", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new VarInsnNode(ALOAD, 0));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/helpers/IInterfaceHost", "getInterfaceDuality", "()Lappeng/helpers/DualityInterface;", true));
            instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "appeng/helpers/DualityInterface", "getSortValue", "()J", false));
            instructions.add(new InsnNode(LRETURN));
            classNode.methods.add(methodNode);
        }
        return writeClass(classNode);
    }

    public static byte[] transformPartInterface(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        if (findMethod(classNode, "getPatternsConfigurations") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "getPatternsConfigurations", "()[Lfoxiwhitee/hellmod/helpers/IInterfaceTerminalSupport$PatternsConfiguration;", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new InsnNode(ICONST_1));
            instructions.add(new TypeInsnNode(ANEWARRAY, "foxiwhitee/hellmod/helpers/IInterfaceTerminalSupport$PatternsConfiguration"));
            instructions.add(new VarInsnNode(ASTORE, 1));
            instructions.add(new VarInsnNode(ALOAD, 1));
            instructions.add(new InsnNode(ICONST_0));
            instructions.add(new TypeInsnNode(NEW, "foxiwhitee/hellmod/helpers/IInterfaceTerminalSupport$PatternsConfiguration"));
            instructions.add(new InsnNode(DUP));
            instructions.add(new InsnNode(ICONST_0));
            instructions.add(new IntInsnNode(BIPUSH, 9));
            instructions.add(new MethodInsnNode(INVOKESPECIAL, "foxiwhitee/hellmod/helpers/IInterfaceTerminalSupport$PatternsConfiguration", "<init>", "(II)V", false));
            instructions.add(new InsnNode(AASTORE));
            instructions.add(new VarInsnNode(ALOAD, 1));
            instructions.add(new InsnNode(ARETURN));
            classNode.methods.add(methodNode);
        }
        if (ASMUtils.findMethod(classNode, "getPatterns") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "getPatterns", "(I)Lnet/minecraft/inventory/IInventory;", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new VarInsnNode(ALOAD, 0));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/helpers/IInterfaceHost", "getInterfaceDuality", "()Lappeng/helpers/DualityInterface;", true));
            instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "appeng/helpers/DualityInterface", "getPatterns", "()Lnet/minecraft/inventory/IInventory;", false));
            instructions.add(new InsnNode(ARETURN));
            classNode.methods.add(methodNode);
        }
        if (ASMUtils.findMethod(classNode, "shouldDisplay") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "shouldDisplay", "()Z", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new VarInsnNode(ALOAD, 0));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/helpers/IInterfaceHost", "getInterfaceDuality", "()Lappeng/helpers/DualityInterface;", true));
            instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "appeng/helpers/DualityInterface", "getConfigManager", "()Lappeng/api/util/IConfigManager;", false));
            instructions.add(new FieldInsnNode(GETSTATIC, "appeng/api/config/Settings", "INTERFACE_TERMINAL", "Lappeng/api/config/Settings;"));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/api/util/IConfigManager", "getSetting", "(Lappeng/api/config/Settings;)Ljava/lang/Enum;", true));
            instructions.add(new TypeInsnNode(CHECKCAST, "appeng/api/config/YesNo"));
            instructions.add(new FieldInsnNode(GETSTATIC, "appeng/api/config/YesNo", "YES", "Lappeng/api/config/YesNo;"));
            LabelNode labelFalse = new LabelNode();
            instructions.add(new JumpInsnNode(IF_ACMPNE, labelFalse));
            instructions.add(new InsnNode(ICONST_1));
            LabelNode labelEnd = new LabelNode();
            instructions.add(new JumpInsnNode(GOTO, labelEnd));
            instructions.add(labelFalse);
            instructions.add(new InsnNode(ICONST_0));
            instructions.add(labelEnd);
            instructions.add(new InsnNode(IRETURN));
            classNode.methods.add(methodNode);
        }
        if (ASMUtils.findMethod(classNode, "getName") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "getName", "()Ljava/lang/String;", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new VarInsnNode(ALOAD, 0));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/helpers/IInterfaceHost", "getInterfaceDuality", "()Lappeng/helpers/DualityInterface;", true));
            instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "appeng/helpers/DualityInterface", "getTermName", "()Ljava/lang/String;", false));
            instructions.add(new InsnNode(ARETURN));
            classNode.methods.add(methodNode);
        }
        if (ASMUtils.findMethod(classNode, "getSortValue") == null) {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "getSortValue", "()J", null, null);
            InsnList instructions = methodNode.instructions;
            instructions.add(new VarInsnNode(ALOAD, 0));
            instructions.add(new MethodInsnNode(INVOKEINTERFACE, "appeng/helpers/IInterfaceHost", "getInterfaceDuality", "()Lappeng/helpers/DualityInterface;", true));
            instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "appeng/helpers/DualityInterface", "getSortValue", "()J", false));
            instructions.add(new InsnNode(LRETURN));
            classNode.methods.add(methodNode);
        }
        byte[] modifiedClass = writeClass(classNode);
        try {
            Path path = Paths.get("PartInterface.class");
            Files.write(path, modifiedClass);
            System.out.println("[SUCCESS] Файл PartInterface.class записано!");
        } catch (Exception e) {
            System.out.println("[NOT SUCCESS] Файл PartInterface.class не записано!");
            e.printStackTrace();
        }
        return writeClass(classNode);
    }

    public static byte[] transformNetworkEventBus(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        MethodNode methodNode = findMethod(classNode, "postEvent");
        if(methodNode == null) return basicClass;
        for (AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node.getType() == AbstractInsnNode.METHOD_INSN && node.getOpcode() == INVOKEVIRTUAL) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) node;
                if(methodInsnNode.owner.equals("appeng/me/Grid") && methodInsnNode.name.equals("getMachines")) {
                    methodInsnNode.name = "getMachinesSafe";
                }
            }
        }
        return writeClass(classNode);
    }

    public static byte[] transformMachineSet(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        classNode.interfaces.add("foxiwhitee/hellmod/utils/craft/IMachineSetAccessor");
        MethodNode generated = new MethodNode(ACC_PUBLIC, "create", "(Ljava/lang/Class;)Lappeng/me/MachineSet;", null, null);
        InsnList list = generated.instructions;
        list.add(new TypeInsnNode(NEW, "appeng/me/MachineSet"));
        list.add(new InsnNode(DUP));
        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new MethodInsnNode(INVOKESPECIAL, "appeng/me/MachineSet", "<init>", "(Ljava/lang/Class;)V", false));
        list.add(new InsnNode(ARETURN));
        classNode.methods.add(generated);
        return writeClass(classNode);
    }

    public static byte[] transformContainerCraftConfirm(byte[] basicClass) {
        ClassNode classNode = ASMUtils.readClass(basicClass);
        MethodNode methodNode = ASMUtils.findMethod(classNode, "startJob");
        if (methodNode != null) {
            AbstractInsnNode point = ASMUtils.locateOpcodeSequence(methodNode.instructions, ALOAD, GETFIELD, IFNULL, ALOAD, INVOKEVIRTUAL, IFNE);
            if (point != null) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "getContainerConfirm", "(Ljava/lang/Object;Lappeng/core/sync/GuiBridge;)Lappeng/core/sync/GuiBridge;", false));
                list.add(new VarInsnNode(ASTORE, 1));
                methodNode.instructions.insertBefore(point, list);
            }
        }

        return ASMUtils.writeClass(classNode);
    }

    public static byte[] transformGuiCraftConfirm(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        MethodNode methodNode = findMethod(classNode, new String[]{"<init>"});
        if (methodNode != null) {
            AbstractInsnNode point = locateOpcodeSequence(methodNode.instructions, 177);
            if (point != null) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "appeng/client/gui/implementations/GuiCraftConfirm", "OriginalGui", "Lappeng/core/sync/GuiBridge;"));
                list.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "getGuiConfirm", "(Ljava/lang/Object;Lappeng/core/sync/GuiBridge;)Lappeng/core/sync/GuiBridge;", false));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new InsnNode(SWAP));
                list.add(new FieldInsnNode(PUTFIELD, "appeng/client/gui/implementations/GuiCraftConfirm", "OriginalGui", "Lappeng/core/sync/GuiBridge;"));
                methodNode.instructions.insertBefore(point, list);
            }
        }

        return ASMUtils.writeClass(classNode);
    }

    public static byte[] transformPacketMEInventoryUpdate(byte[] basicClass) {
        ClassNode classNode = readClass(basicClass);
        MethodNode methodNode = findMethod(classNode, "clientPacketData");
        if (methodNode != null) {
            AbstractInsnNode point = locateOpcodeSequence(methodNode.instructions, ALOAD, INSTANCEOF, IFEQ);
            if (point != null) {
                InsnList list = new InsnList();
                LabelNode label = new LabelNode();
                list.add(new VarInsnNode(ALOAD, 4));
                list.add(new TypeInsnNode(INSTANCEOF, "foxiwhitee/hellmod/client/gui/terminals/GuiTerminal"));
                list.add(new JumpInsnNode(IFEQ, label));

                list.add(new VarInsnNode(ALOAD, 4));
                list.add(new TypeInsnNode(CHECKCAST, "foxiwhitee/hellmod/client/gui/terminals/GuiTerminal"));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "appeng/core/sync/packets/PacketMEInventoryUpdate", "list", "Ljava/util/List;"));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "foxiwhitee/hellmod/client/gui/terminals/GuiTerminal", "postUpdate", "(Ljava/util/List;)V", false));

                list.add(label);
                methodNode.instructions.insertBefore(point, list);
            }
        }

        return writeClass(classNode);
    }

    public static byte[] transformIInterfaceHost(byte[] classBytes) {
        ClassNode classNode = readClass(classBytes);
        classNode.interfaces.add("foxiwhitee/hellmod/helpers/IInterfaceTerminalSupport");
        return writeClass(classNode);
    }

}
