package foxiwhitee.hellmod.integration.draconic.client.render.effect;

import codechicken.lib.render.CCModelLibrary;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Vector3;
import java.util.Random;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.draconic.client.shaders.AlphaShader;
import foxiwhitee.hellmod.integration.draconic.helpers.Cord3D;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingCharger;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingInventory;
import foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.RenderEnergyBolt;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class EffectTrackerFusionCrafting {
    private static final AlphaShader shader = new AlphaShader();

    private static final long initTime = System.currentTimeMillis();

    public static double interpPosX = 0.0D;

    public static double interpPosY = 0.0D;

    public static double interpPosZ = 0.0D;

    public final IFusionCraftingInventory craftingInventory;

    private final Random rand = new Random();

    private final Cord3D corePos;

    private final int effectCount;

    private final World world;

    public boolean positionLocked = false;

    public Cord3D startPos;

    public Cord3D pos;

    public Cord3D prevPos = new Cord3D();

    public Cord3D circlePosition = new Cord3D();

    public float alpha = 0.0F;

    public float scale = 1.0F;

    public float red = 0.0F;

    public float green = 1.0F;

    public float blue = 1.0F;

    private int renderBolt = 0;

    private float rotation;

    private float rotationSpeed = 1.0F;

    private float aRandomFloat = 0.0F;

    private long boltSeed = 0L;

    public EffectTrackerFusionCrafting(World world, Cord3D pos, Cord3D corePos, IFusionCraftingInventory craftingInventory, int effectCount) {
        this.world = world;
        this.corePos = corePos;
        this.craftingInventory = craftingInventory;
        this.effectCount = effectCount;
        this.rotation = this.rand.nextInt(1000);
        this.aRandomFloat = this.rand.nextFloat();
        this.pos = pos.copy();
        this.startPos = pos.copy();
        this.prevPos.set(pos);
    }

    public void onUpdate(boolean isMoving) {
        this.prevPos.set(this.pos);
        if (isMoving) {
            double distance = Cord3D.getDistanceAtoB(this.circlePosition, this.pos);
            if (distance > 0.1D && !this.positionLocked) {
                if (this.scale > 1.0F)
                    this.scale -= 0.05F;
                Cord3D dir = Cord3D.getDirectionVec(this.pos, this.circlePosition);
                double speed = 0.1D + this.aRandomFloat * 0.1D;
                dir.multiply(speed, speed, speed);
                this.pos.add(dir.x, dir.y, dir.z);
            } else {
                this.positionLocked = true;
                this.pos.set(this.circlePosition);
            }
        } else {
            this.scale = 1.5F;
        }
        int chance = 22 - (int)(this.craftingInventory.getCraftingStage() / 2000.0D * 22.0D);
        if (chance < 1)
            chance = 1;
        if (this.rand.nextInt(chance) == 0)
            SEffectHandler.spawnFXDirect(new ResourceLocation(HellCore.MODID, "textures/particle/particles.png"), new SubParticle(this.world, this.pos));
        if (this.renderBolt > 0)
            this.renderBolt--;
        if (this.rand.nextInt(chance * 2 + (int)(this.effectCount * 1.5D)) == 0) {
            this.renderBolt = 1;
            this.boltSeed = this.rand.nextLong();
            Cord3D pos = this.corePos.copy().add(0.5D, 0.5D, 0.5D);
            SEffectHandler.spawnFXDirect(new ResourceLocation(HellCore.MODID, "textures/particle/particles.png"), new SubParticle(this.world, pos));
        }
        if (this.craftingInventory.getCraftingStage() < 1000) {
            TileEntity tile = this.world.getTileEntity(this.pos.floorX(), this.pos.floorY(), this.pos.floorZ());
            if (tile instanceof IFusionCraftingCharger && this.craftingInventory.getIngredientEnergyCost() > 0L)
                this.alpha = (float)(((IFusionCraftingCharger)tile).getInjectorCharge() / this.craftingInventory.getIngredientEnergyCost());
        } else {
            this.alpha = 1.0F;
        }
        this.rotationSpeed = 1.0F + this.craftingInventory.getCraftingStage() / 1000.0F * 10.0F;
        if (this.alpha > 1.0F)
            this.alpha = 1.0F;
        this.rotation += this.rotationSpeed;
    }

    public void renderEffect(Tessellator tessellator, float partialTicks) {
        float relativeX = (float)(this.prevPos.x + (this.pos.x - this.prevPos.x) * partialTicks - interpPosX);
        float relativeY = (float)(this.prevPos.y + (this.pos.y - this.prevPos.y) * partialTicks - interpPosY);
        float relativeZ = (float)(this.prevPos.z + (this.pos.z - this.prevPos.z) * partialTicks - interpPosZ);
        float correctX = (float)(this.prevPos.x + (this.pos.x - this.prevPos.x) * partialTicks);
        float correctY = (float)(this.prevPos.y + (this.pos.y - this.prevPos.y) * partialTicks);
        float correctZ = (float)(this.prevPos.z + (this.pos.z - this.prevPos.z) * partialTicks);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0F, 200.0F);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslatef(relativeX, relativeY, relativeZ);
        GL11.glRotatef(this.rotation + partialTicks * this.rotationSpeed, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-relativeX, -relativeY, -relativeZ);
        GL11.glColor4f(this.red, this.green, this.blue, this.alpha);
        CCRenderState.reset();
        CCRenderState.startDrawing();
        CCRenderState.alphaOverride = (int)(this.alpha * 255.0F);
        Matrix4 mat4 = (new Matrix4()).translate(new Vector3(relativeX, relativeY, relativeZ)).apply((Transformation)new Scale(0.15D * this.scale)).apply((Transformation)new Rotation(0.0D, 0.0D, 1.0D, 0.0D));
        CCModelLibrary.icosahedron7.render(new CCRenderState.IVertexOperation[] { (CCRenderState.IVertexOperation)mat4 });
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glTranslatef(relativeX, relativeY, relativeZ);
        if (this.renderBolt > 0)
            RenderEnergyBolt.renderBoltBetween(Vec3.createVectorHelper(0.0D, 0.0D, 0.0D), Vec3.createVectorHelper(this.corePos.x - correctX + 0.5D, this.corePos.y - correctY + 0.5D, this.corePos.z - correctZ + 0.5D), 0.05D, 1.0D, 10, this.boltSeed, true);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glPopMatrix();
    }

    public static class SubParticle extends SParticle {
        public SubParticle(World worldIn, Cord3D pos) {
            super(worldIn, pos);
            double speed = 0.1D;
            this.motionX = (-0.5D + this.rand.nextDouble()) * speed;
            this.motionY = (-0.5D + this.rand.nextDouble()) * speed;
            this.motionZ = (-0.5D + this.rand.nextDouble()) * speed;
            this.particleMaxAge = 10 + this.rand.nextInt(10);
            this.particleScale = 1.0F;
            this.particleTextureIndexY = 1;
            this.particleRed = 0.0F;
        }

        public void onUpdate() {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.particleTextureIndexX = this.rand.nextInt(5);
            int ttd = this.particleMaxAge - this.particleAge;
            if (ttd < 10)
                this.particleScale = ttd / 10.0F;
            moveEntityNoClip(this.motionX, this.motionY, this.motionZ);
            if (this.particleAge++ > this.particleMaxAge)
                setDead();
        }

        public void renderParticle(Tessellator tess, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
            float minU = this.particleTextureIndexX / 8.0F;
            float maxU = minU + 0.125F;
            float minV = this.particleTextureIndexY / 8.0F;
            float maxV = minV + 0.125F;
            float scale = 0.1F * this.particleScale;
            float renderX = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
            float renderY = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
            float renderZ = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
            int brightnessForRender = getBrightnessForRender(partialTicks);
            int j = brightnessForRender >> 16 & 0xFFFF;
            int k = brightnessForRender & 0xFFFF;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
            tess.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
            tess.addVertexWithUV((renderX - rotationX * scale - rotationXY * scale), (renderY - rotationZ * scale), (renderZ - rotationYZ * scale - rotationXZ * scale), maxU, maxV);
            tess.addVertexWithUV((renderX - rotationX * scale + rotationXY * scale), (renderY + rotationZ * scale), (renderZ - rotationYZ * scale + rotationXZ * scale), maxU, minV);
            tess.addVertexWithUV((renderX + rotationX * scale + rotationXY * scale), (renderY + rotationZ * scale), (renderZ + rotationYZ * scale + rotationXZ * scale), minU, minV);
            tess.addVertexWithUV((renderX + rotationX * scale - rotationXY * scale), (renderY - rotationZ * scale), (renderZ + rotationYZ * scale - rotationXZ * scale), minU, maxV);
        }
    }
}
