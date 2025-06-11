package foxiwhitee.hellmod.integration.draconic.client.render.effect;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;

import foxiwhitee.hellmod.utils.PairKV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class SEffectRenderer {
    private final Map<ResourceLocation, ArrayDeque<EntityFX>[][]> renderQueue = (Map)new HashMap<>();

    private final Queue<PairKV<ResourceLocation, EntityFX>> newParticleQueue = new ArrayDeque<>();

    public World world;

    public SEffectRenderer(World world) {
        this.world = world;
    }

    public void addEffect(ResourceLocation resourceLocation, EntityFX particle) {
        if (resourceLocation == null || particle == null)
            return;
        if (particle instanceof SParticle && ((SParticle)particle).isRawGLParticle())
            throw new RuntimeException("Attempted to spawn a Raw GL particle using the default spawn call! This is not allowed!");
        this.newParticleQueue.add(new PairKV(resourceLocation, particle));
    }

    public void updateEffects() {
        for (int i = 0; i < 4; i++)
            updateEffectLayer(i);
        if (!this.newParticleQueue.isEmpty())
            for (PairKV<ResourceLocation, EntityFX> entry = this.newParticleQueue.poll(); entry != null; entry = this.newParticleQueue.poll()) {
                if (!this.renderQueue.containsKey(entry.getKey())) {
                    ArrayDeque[][] array = new ArrayDeque[4][];
                    for (int j = 0; j < 4; j++) {
                        array[j] = new ArrayDeque[2];
                        for (int k = 0; k < 2; k++)
                            array[j][k] = new ArrayDeque();
                    }
                    this.renderQueue.put(entry.getKey(), array);
                }
                ArrayDeque[][] arrayOfArrayDeque = (ArrayDeque[][])this.renderQueue.get(entry.getKey());
                EntityFX particle = (EntityFX)entry.getValue();
                int layer = particle.getFXLayer();
                int mask = 0;
                if (arrayOfArrayDeque[layer][mask].size() >= 6000)
                    ((EntityFX)arrayOfArrayDeque[layer][mask].removeFirst()).setDead();
                arrayOfArrayDeque[layer][mask].add(particle);
            }
    }

    private void updateEffectLayer(int layer) {
        for (int i = 0; i < 2; i++) {
            for (ArrayDeque[][] arrayOfArrayDeque : this.renderQueue.values())
                tickAndRemoveDead(arrayOfArrayDeque[layer][i]);
        }
    }

    private void tickAndRemoveDead(Queue<EntityFX> queue) {
        if (!queue.isEmpty()) {
            Iterator<EntityFX> iterator = queue.iterator();
            while (iterator.hasNext()) {
                EntityFX particle = iterator.next();
                tickParticle(particle);
                if (particle.isDead)
                    iterator.remove();
            }
        }
    }

    public void clearEffects(World worldIn) {
        this.world = worldIn;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 2; k++) {
                for (ArrayDeque[][] arrayOfArrayDeque : this.renderQueue.values()) {
                    for (Object particle : arrayOfArrayDeque[j][k])
                        ((EntityFX)particle).setDead();
                }
            }
        }
    }

    private void tickParticle(EntityFX particle) {
        try {
            particle.onUpdate();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            int i = particle.getFXLayer();
            crashreportcategory.addCrashSection("Particle", particle.toString());
            crashreportcategory.addCrashSection("Particle Type", (i == 0) ? "MISC_TEXTURE" : ((i == 1) ? "TERRAIN_TEXTURE" : ((i == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + i))));
            throw new ReportedException(crashreport);
        }
    }

    public void renderParticles(Entity entityIn, float partialTicks) {
        float rotationX = ActiveRenderInfo.rotationX;
        float rotationZ = ActiveRenderInfo.rotationZ;
        float rotationYZ = ActiveRenderInfo.rotationYZ;
        float rotationXY = ActiveRenderInfo.rotationXY;
        float rotationXZ = ActiveRenderInfo.rotationXZ;
        EntityFX.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        EntityFX.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        EntityFX.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.0F);
        Tessellator tessellator = Tessellator.instance;
        Profiler profiler = (Minecraft.getMinecraft()).mcProfiler;
        GL11.glDisable(2896);
        for (int layer = 0; layer < 4; layer++) {
            profiler.startSection("Tex_Particles");
            renderTexturedParticlesInLayer(layer, tessellator, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            profiler.endSection();
        }
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glAlphaFunc(516, 0.1F);
    }

    private void renderTexturedParticlesInLayer(int layer, Tessellator tessellator, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        for (ResourceLocation resourceLocation : this.renderQueue.keySet()) {
            (Minecraft.getMinecraft()).renderEngine.bindTexture(resourceLocation);
            ArrayDeque[][] arrayOfArrayDeque = (ArrayDeque[][])this.renderQueue.get(resourceLocation);
            for (int j = 0; j < 2; j++) {
                final int i_f = layer;
                if (!arrayOfArrayDeque[layer][j].isEmpty()) {
                    switch (j) {
                        case 0:
                            GL11.glDepthMask(false);
                            GL11.glAlphaFunc(516, 0.0F);
                            break;
                        case 1:
                            GL11.glDepthMask(true);
                            break;
                    }
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    tessellator.startDrawingQuads();
                    for (Object particle : arrayOfArrayDeque[layer][j]) {
                        try {
                            ((EntityFX)particle).renderParticle(tessellator, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addCrashSection("Particle", new Callable<String>() {
                                public String call() throws Exception {
                                    return particle.toString();
                                }
                            });
                            crashreportcategory.addCrashSection("Particle Type", new Callable<String>() {
                                public String call() throws Exception {
                                    return (i_f == 0) ? "MISC_TEXTURE" : ((i_f == 1) ? "TERRAIN_TEXTURE" : ((i_f == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + i_f)));
                                }
                            });
                            throw new ReportedException(crashreport);
                        }
                    }
                    tessellator.draw();
                }
            }
        }
    }

    public String getStatistics() {
        int i = 0;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 2; k++) {
                for (ArrayDeque[][] arrayOfArrayDeque : this.renderQueue.values())
                    i += arrayOfArrayDeque[j][k].size();
            }
        }
        int g = 0;
        return "" + i + " GLFX: " + g;
    }

    public void clear() {
        this.renderQueue.clear();
        this.newParticleQueue.forEach(pairKV -> ((EntityFX)pairKV.getValue()).setDead());
        this.newParticleQueue.clear();
    }
}
