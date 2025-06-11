package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui;

import java.util.Random;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GuiEffect {
    public static double interpPosX;
    public static double interpPosY;
    private final World world;
    public double zLevel = 0.0D;
    protected double prevPosX;
    protected double prevPosY;
    protected double posX;
    protected double posY;
    protected double motionX;
    protected double motionY;
    protected boolean isExpired;
    protected Random rand;
    protected int particleTextureIndexX;
    protected int particleTextureIndexY;
    protected int particleAge;
    protected int particleMaxAge;
    protected float particleScale;
    protected float particleGravity;
    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;
    protected float particleAlpha;

    protected GuiEffect(World world, double posX, double posY) {
        this.world = world;
        this.rand = new Random();
        this.particleAlpha = 1.0F;
        setPosition(posX, posY);
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
        this.particleMaxAge = (int) (4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
        this.particleAge = 0;
    }

    public GuiEffect(World world, double xCoord, double yCoord, double xSpeed, double ySpeed) {
        this(world, xCoord, yCoord);
        this.motionX = xSpeed + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionY = ySpeed + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        float f = (float) (Math.random() + Math.random() + 1.0D) * 0.15F;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY);
        this.motionX = this.motionX / f1 * f * 0.4000000059604645D;
        this.motionY = this.motionY / f1 * f * 0.4000000059604645D + 0.10000000149011612D;
    }

    public GuiEffect multiplyVelocity(float multiplier) {
        this.motionX *= multiplier;
        this.motionY = (this.motionY - 0.10000000149011612D) * multiplier + 0.10000000149011612D;
        return this;
    }

    public GuiEffect setScale(float scale) {
        this.particleScale = scale;
        return this;
    }

    public void setRBGColorF(float particleRedIn, float particleGreenIn, float particleBlueIn) {
        this.particleRed = particleRedIn;
        this.particleGreen = particleGreenIn;
        this.particleBlue = particleBlueIn;
    }

    public void setAlphaF(float alpha) {
        this.particleAlpha = alpha;
    }

    public boolean isTransparent() {
        return true;
    }

    public void setMaxAge(int p_187114_1_) {
        this.particleMaxAge = p_187114_1_;
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;

        if (this.particleAge++ >= this.particleMaxAge) {
            setExpired();
        }

        this.motionY -= 0.04D * this.particleGravity;
        moveEntity(this.motionX, this.motionY);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
    }

    public void renderParticle(float partialTicks) {
        float minU = this.particleTextureIndexX / 8.0F;
        float maxU = minU + 0.125F;
        float minV = this.particleTextureIndexY / 8.0F;
        float maxV = minV + 0.125F;
        float scale = 8.0F * this.particleScale;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        float renderX = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks);
        float renderY = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks);
        tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tessellator.addVertexWithUV((renderX - 1.0F * scale), (renderY - 1.0F * scale), this.zLevel, maxU, maxV);
        tessellator.addVertexWithUV((renderX - 1.0F * scale), (renderY + 1.0F * scale), this.zLevel, maxU, minV);
        tessellator.addVertexWithUV((renderX + 1.0F * scale), (renderY + 1.0F * scale), this.zLevel, minU, minV);
        tessellator.addVertexWithUV((renderX + 1.0F * scale), (renderY - 1.0F * scale), this.zLevel, minU, maxV);
        tessellator.draw();
    }

    public int getFXLayer() {
        return 0;
    }


    public void setParticleTextureIndex(int particleTextureIndex) {
        if (getFXLayer() != 0) {
            throw new RuntimeException("Invalid call to Particle.setMiscTex");
        }
        this.particleTextureIndexX = particleTextureIndex % 16;
        this.particleTextureIndexY = particleTextureIndex / 16;
    }


    public void nextTextureIndexX() {
        this.particleTextureIndexX++;
    }

    public String toString() {
        return getClass().getSimpleName() + ", Pos (" + this.posX + "," + this.posY + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.particleAge;
    }

    public void setExpired() {
        this.isExpired = true;
    }


    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    public void moveEntity(double x, double y) {
        this.posX += x;
        this.posY += y;
    }


    public boolean isAlive() {
        return !this.isExpired;
    }
}
