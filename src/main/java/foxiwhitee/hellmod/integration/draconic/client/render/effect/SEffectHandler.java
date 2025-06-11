package foxiwhitee.hellmod.integration.draconic.client.render.effect;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.LinkedHashMap;
import java.util.Map;

import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.helpers.Cord3D;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketSpawnParticle;
import foxiwhitee.hellmod.utils.PairKV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

public class SEffectHandler {
    private static final SEffectHandler instance = new SEffectHandler();

    @SideOnly(Side.CLIENT)
    public static SEffectRenderer effectRenderer;

    public static Map<Integer, PairKV<ISParticleFactory, ResourceLocation>> particleRegistry = new LinkedHashMap<>();

    private static int lastIndex = -1;

    private static World currentWorld = null;

    @SideOnly(Side.CLIENT)
    public static void iniEffectRenderer() {
        effectRenderer = new SEffectRenderer(null);
        MinecraftForge.EVENT_BUS.register(instance);
        FMLCommonHandler.instance().bus().register(instance);
    }

    @SideOnly(Side.CLIENT)
    public static int registerFX(ResourceLocation particleSheet, ISParticleFactory factory) {
        lastIndex++;
        particleRegistry.put(Integer.valueOf(lastIndex), new PairKV(factory, particleSheet));
        return lastIndex;
    }

    public static int registerFXServer() {
        lastIndex++;
        return lastIndex;
    }

    public static void spawnFX(int particleID, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, double viewRange, int... args) {
        spawnFX(particleID, world, new Cord3D(xCoord, yCoord, zCoord), new Cord3D(xSpeed, ySpeed, zSpeed), viewRange, args);
    }

    public static void spawnFX(int particleID, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... args) {
        spawnFX(particleID, world, new Cord3D(xCoord, yCoord, zCoord), new Cord3D(xSpeed, ySpeed, zSpeed), 32.0D, args);
    }

    public static void spawnFX(int particleID, World world, Cord3D pos, Cord3D speed, int... args) {
        spawnFX(particleID, world, pos, speed, 32.0D, args);
    }

    public static void spawnFX(int particleID, World world, Cord3D pos, Cord3D speed, double viewRange, int... args) {
        if (!world.isRemote) {
            NetworkManager.instance.sendToAllAround(new PacketSpawnParticle(particleID, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z, viewRange, args), new NetworkRegistry.TargetPoint(world.provider.dimensionId, pos.x, pos.y, pos.z, viewRange));
        } else if (isInRange(pos.x, pos.y, pos.z, viewRange) && effectRenderer != null) {
            if (!particleRegistry.containsKey(Integer.valueOf(particleID))) {
                System.out.printf("Attempted to spawn an unregistered particle ID (%s)", new Object[] { Integer.valueOf(particleID) });
                return;
            }
            Minecraft mc = Minecraft.getMinecraft();
            int particleSetting = mc.gameSettings.particleSetting;
            if (particleSetting == 2 || (particleSetting == 1 && world.rand.nextInt(3) != 0))
                return;
            PairKV<ISParticleFactory, ResourceLocation> pair = particleRegistry.get(Integer.valueOf(particleID));
            EntityFX particle = ((ISParticleFactory)pair.getKey()).getEntityFX(particleID, world, pos, speed, args);
            effectRenderer.addEffect((ResourceLocation)pair.getValue(), particle);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void spawnFXDirect(ResourceLocation texture, SParticle particle) {
        spawnFXDirect(texture, particle, 32.0D, true);
    }

    @SideOnly(Side.CLIENT)
    public static void spawnFXDirect(ResourceLocation texture, SParticle particle, double viewRange, boolean respectParticleSetting) {
        Cord3D pos = particle.getPos();
        if (isInRange(pos.x, pos.y, pos.z, viewRange) && effectRenderer != null) {
            Minecraft mc = Minecraft.getMinecraft();
            int particleSetting = mc.gameSettings.particleSetting;
            if (respectParticleSetting && (particleSetting == 2 || (particleSetting == 1 && (particle.getWorld()).rand.nextInt(3) != 0)))
                return;
            effectRenderer.addEffect(texture, particle);
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInRange(double x, double y, double z, double vewRange) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.renderViewEntity == null || mc.effectRenderer == null)
            return false;
        double var15 = mc.renderViewEntity.posX - x;
        double var17 = mc.renderViewEntity.posY - y;
        double var19 = mc.renderViewEntity.posZ - z;
        return (var15 * var15 + var17 * var17 + var19 * var19 <= vewRange * vewRange);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase != TickEvent.Phase.END || mc.isGamePaused())
            return;
        if (currentWorld != mc.theWorld && mc.theWorld != null) {
            currentWorld = (World)mc.theWorld;

        }
        if (effectRenderer.world != null) {
            mc.mcProfiler.startSection("SParticlesUpdate");
            effectRenderer.updateEffects();
            mc.mcProfiler.endSection();
        }
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderWorld(RenderWorldLastEvent event) {
        (Minecraft.getMinecraft()).mcProfiler.startSection("SParticles");
        effectRenderer.renderParticles((Entity)(Minecraft.getMinecraft()).thePlayer, event.partialTicks);
        (Minecraft.getMinecraft()).mcProfiler.endSection();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void debugOverlay(RenderGameOverlayEvent.Text event) {
        if (event.left.size() >= 5 && effectRenderer != null) {
            String particleTxt = event.left.get(4);
            particleTxt = particleTxt + "." + EnumChatFormatting.GOLD + " BC-P: " + effectRenderer.getStatistics();
            event.left.set(4, particleTxt);
        }
    }
}
