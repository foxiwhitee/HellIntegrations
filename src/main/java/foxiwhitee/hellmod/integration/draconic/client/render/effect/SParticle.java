package foxiwhitee.hellmod.integration.draconic.client.render.effect;

import foxiwhitee.hellmod.integration.draconic.helpers.Cord3D;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class SParticle extends EntityFX {
    protected float texturesPerRow = 16.0F;

    protected float airResistance = 0.0F;

    protected float baseScale = 1.0F;

    public SParticle(World worldIn, Cord3D pos) {
        super(worldIn, pos.x, pos.y, pos.z);
    }

    public SParticle(World worldIn, Cord3D pos, Cord3D speed) {
        super(worldIn, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
    }

    public int getFXLayer() {
        return super.getFXLayer();
    }

    public boolean isRawGLParticle() {
        return false;
    }

    public SParticle setScale(float scale) {
        this.particleScale = scale;
        this.baseScale = scale;
        return this;
    }

    public SParticle setColour(float red, float green, float blue) {
        setRBGColorF(red, green, blue);
        return this;
    }

    public SParticle setMaxAge(int age, int randAdditive) {
        this.particleMaxAge = age + this.rand.nextInt(randAdditive);
        return this;
    }

    public SParticle setGravity(double gravity) {
        this.particleGravity = (float)gravity;
        return this;
    }

    public SParticle setAirResistance(float airResistance) {
        this.airResistance = airResistance;
        return this;
    }

    public SParticle setSizeAndRandMotion(double scale, double xMotion, double yMotion, double zMotion) {
        this.particleScale = (float)scale;
        this.baseScale = (float)scale;
        this.motionX = (-0.5D + this.rand.nextDouble()) * xMotion;
        this.motionY = (-0.5D + this.rand.nextDouble()) * yMotion;
        this.motionZ = (-0.5D + this.rand.nextDouble()) * zMotion;
        return this;
    }

    public Cord3D getPos() {
        return new Cord3D(this.posX, this.posY, this.posZ);
    }

    public World getWorld() {
        return this.worldObj;
    }

    public SParticle setPosition(Cord3D pos) {
        setPosition(pos.x, pos.y, pos.z);
        return this;
    }

    public void moveEntityNoClip(double x, double y, double z) {}

    public void renderParticle(Tessellator tess, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float minU = this.particleTextureIndexX / this.texturesPerRow;
        float maxU = minU + 1.0F / this.texturesPerRow;
        float minV = this.particleTextureIndexY / this.texturesPerRow;
        float maxV = minV + 1.0F / this.texturesPerRow;
        float scale = 0.1F * this.particleScale;
        if (this.particleIcon != null) {
            minU = this.particleIcon.getMinU();
            maxU = this.particleIcon.getMaxU();
            minV = this.particleIcon.getMinV();
            maxV = this.particleIcon.getMaxV();
        }
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

