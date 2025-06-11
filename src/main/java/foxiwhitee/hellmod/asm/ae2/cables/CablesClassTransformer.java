package foxiwhitee.hellmod.asm.ae2.cables;


import foxiwhitee.hellmod.utils.asm.ASMClassTransformer;
import foxiwhitee.hellmod.utils.asm.ComputeFramesClassWriter;
import foxiwhitee.hellmod.utils.asm.SpecialClassNode;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.*;

public class CablesClassTransformer implements IClassTransformer {
  private final List<ASMClassTransformer> globalTransformers = new ArrayList<>();
  
  private final Map<String, List<ASMClassTransformer>> specialTransformers = new HashMap<>();
  
  public CablesClassTransformer() {
    registerSpecialTransformer((ASMClassTransformer)new GridNodeTransformer(), "appeng.me.GridNode");
    registerSpecialTransformer((ASMClassTransformer)new GridConnectionTransformer(), "appeng.me.GridConnection");
    registerSpecialTransformer((ASMClassTransformer)new AEBasePartTransformer(), "appeng.parts.AEBasePart");
    registerSpecialTransformer((ASMClassTransformer)new AENetworkProxyTransformer(), "appeng.me.helpers.AENetworkProxy");
    registerSpecialTransformer((ASMClassTransformer)new ContainerUpgradeableTransformer(), "appeng.container.implementations.ContainerUpgradeable");
  }
  
  public void registerGlobalTransformer(ASMClassTransformer transformer) {
    this.globalTransformers.add(transformer);
  }
  
  public void registerSpecialTransformer(ASMClassTransformer transformer, String className) {
    ((List<ASMClassTransformer>)this.specialTransformers.computeIfAbsent(className, k -> new ArrayList(1))).add(transformer);
  }
  
  public void registerSpecialTransformer(ASMClassTransformer transformer, String... classNames) {
    for (String name : classNames)
      registerSpecialTransformer(transformer, name); 
  }
  
  public byte[] transform(String name, String transformedName, byte[] basicClass) {
    if (basicClass == null)
      return null; 
    SpecialClassNode classNode = new SpecialClassNode();
    ClassReader classReader = new ClassReader(basicClass);
    classReader.accept((ClassVisitor)classNode, 0);
    int flags = 0;
    for (ASMClassTransformer transformer : this.specialTransformers.getOrDefault(transformedName, Collections.emptyList()))
      flags |= transformer.transformClass(name, transformedName, classReader, classNode).ordinal(); 
    for (ASMClassTransformer transformer : this.globalTransformers)
      flags |= transformer.transformClass(name, transformedName, classReader, classNode).ordinal(); 
    boolean shouldComputeFrames = ((classNode.version & 0xFFFF) > 50);
    if (flags == 0 && !shouldComputeFrames)
      return basicClass; 
    ClassWriter writer = shouldComputeFrames ? (ClassWriter)new ComputeFramesClassWriter() : new ClassWriter((flags == 1) ? 0 : 1);
    classNode.accept(writer);
    return writer.toByteArray();
  }
}

