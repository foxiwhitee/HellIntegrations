package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui;

import java.util.Random;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderEnergyBolt {
    public static void renderBoltBetween(Vec3 point1, Vec3 point2, double scale, double maxDeflection, int maxSegments, long boltSeed, boolean corona) {
        Tessellator tessellator = Tessellator.instance;
        Random random = new Random(boltSeed);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0F, 200.0F);
        GL11.glBlendFunc(770, 1);
        double distance = point1.distanceTo(point2);
        Vec3 dirVec = getDirectionVec(point1, point2);
        Vec3 invDir = Vec3.createVectorHelper(1.0D, 1.0D, 1.0D).subtract(dirVec);
        Vec3[] vectors = new Vec3[maxSegments / 2 + random.nextInt(maxSegments / 2)];
        vectors[0] = point1;
        vectors[vectors.length - 1] = point2;
        for (int i = 1; i < vectors.length - 1; i++) {
            double pos = i / vectors.length * distance;
            Vec3 point = point1.addVector(0.0D, 0.0D, 0.0D);
            point.addVector(dirVec.xCoord * pos, dirVec.yCoord * pos, dirVec.zCoord * pos);
            double randX = (-0.5D + random.nextDouble()) * maxDeflection * invDir.xCoord;
            double randY = (-0.5D + random.nextDouble()) * maxDeflection * invDir.yCoord;
            double randZ = (-0.5D + random.nextDouble()) * maxDeflection * invDir.zCoord;
            point.addVector(randX, randY, randZ);
            vectors[i] = point;
        }
        double rScale = scale * (0.5D + random.nextDouble() * 0.5D);
        for (int j = 1; j < vectors.length; j++)
            drawBoltSegment(tessellator, vectors[j - 1], vectors[j], (float)rScale);
        if (corona) {
            Vec3[][] coronaVecs = new Vec3[2 + random.nextInt(4)][2 + random.nextInt(3)];
            int k;
            for (k = 0; k < coronaVecs.length; k++) {
                coronaVecs[k][0] = point1;
                double d = distance / (2.0D + random.nextDouble() * 2.0D);
                for (int v = 1; v < (coronaVecs[k]).length; v++) {
                    double pos = v / (coronaVecs[k]).length * d;
                    Vec3 point = point1.addVector(0.0D, 0.0D, 0.0D);
                    point.addVector(dirVec.xCoord * pos, dirVec.yCoord * pos, dirVec.zCoord * pos);
                    double randX = (-0.5D + random.nextDouble()) * maxDeflection * invDir.xCoord * 0.5D;
                    double randY = (-0.5D + random.nextDouble()) * maxDeflection * invDir.yCoord * 0.5D;
                    double randZ = (-0.5D + random.nextDouble()) * maxDeflection * invDir.zCoord * 0.5D;
                    point.addVector(randX, randY, randZ);
                    coronaVecs[k][v] = point;
                }
            }
            for (k = 0; k < coronaVecs.length; k++) {
                float f = 0.1F + random.nextFloat() * 0.5F;
                for (int v = 1; v < (coronaVecs[k]).length; v++)
                    drawBoltSegment(tessellator, coronaVecs[k][v - 1], coronaVecs[k][v], (float)scale * f);
            }
        }
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }

    private static void drawBoltSegment(Tessellator tessellator, Vec3 p1, Vec3 p2, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslated(p1.xCoord, p1.yCoord, p1.zCoord);
        double dist = p1.distanceTo(p2);
        float xd = (float)(p1.xCoord - p2.xCoord);
        float yd = (float)(p1.yCoord - p2.yCoord);
        float zd = (float)(p1.zCoord - p2.zCoord);
        double var7 = MathHelper.sqrt_double((xd * xd + zd * zd));
        float rotYaw = (float)(Math.atan2(xd, zd) * 180.0D / Math.PI);
        float rotPitch = (float)(Math.atan2(yd, var7) * 180.0D / Math.PI);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(180.0F + rotYaw, 0.0F, 0.0F, -1.0F);
        GL11.glRotatef(rotPitch, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawing(5);
        for (int i = 0; i <= 9; i++) {
            float f = (i + 1.0F) / 9.0F;
            float verX = MathHelper.sin((i % 3) * 3.1415927F * 2.0F / 3.0F) * f * scale;
            float verZ = MathHelper.cos((i % 3) * 3.1415927F * 2.0F / 3.0F) * f * scale;
            tessellator.setColorRGBA_F(0.35F, 0.65F, 0.9F, 0.3F);
            tessellator.addVertex(verX, dist, verZ);
            tessellator.addVertex(verX, 0.0D, verZ);
        }
        tessellator.draw();
        GL11.glPopMatrix();
    }

    private static Vec3 getDirectionVec(Vec3 from, Vec3 to) {
        double distance = from.distanceTo(to);
        if (distance == 0.0D)
            distance = 0.1D;
        Vec3 offset = Vec3.createVectorHelper(to.xCoord, to.yCoord, to.zCoord);
        offset = offset.subtract(from);
        return Vec3.createVectorHelper(offset.xCoord / distance, offset.yCoord / distance, offset.zCoord / distance);
    }
}

