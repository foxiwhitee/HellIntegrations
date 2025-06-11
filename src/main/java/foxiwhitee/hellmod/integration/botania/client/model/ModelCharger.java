package foxiwhitee.hellmod.integration.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCharger extends ModelBase {
    public ModelRenderer Rot1;

    public ModelRenderer Rot2;

    public ModelRenderer Rot3;

    public ModelRenderer Rot4;

    public ModelRenderer PlateRot1;

    public ModelRenderer PlateRot2;

    public ModelRenderer PlateRot3;

    public ModelRenderer PlateRot4;

    public ModelRenderer Shape1;

    public ModelRenderer Shape2;

    public ModelRenderer Shape3;

    public ModelRenderer Shape4;

    public ModelCharger() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Rot1 = new ModelRenderer(this, 0, 11);
        this.Rot1.addBox(0.0F, -4.0F, 0.0F, 1, 8, 1);
        this.Rot1.setRotationPoint(5.0F, 12.0F, 5.0F);
        this.Rot1.setTextureSize(64, 32);
        this.Rot1.mirror = true;
        setRotation(this.Rot1, 0.0F, 0.0F, 0.0F);
        this.Rot2 = new ModelRenderer(this, 5, 11);
        this.Rot2.addBox(0.0F, -4.0F, 0.0F, 1, 8, 1);
        this.Rot2.setRotationPoint(5.0F, 12.0F, -6.0F);
        this.Rot2.setTextureSize(64, 32);
        this.Rot2.mirror = true;
        setRotation(this.Rot2, 0.0F, 0.0F, 0.0F);
        this.Rot3 = new ModelRenderer(this, 10, 11);
        this.Rot3.addBox(0.0F, -4.0F, 0.0F, 1, 8, 1);
        this.Rot3.setRotationPoint(-6.0F, 12.0F, 5.0F);
        this.Rot3.setTextureSize(64, 32);
        this.Rot3.mirror = true;
        setRotation(this.Rot3, 0.0F, 0.0F, 0.0F);
        this.Rot4 = new ModelRenderer(this, 15, 11);
        this.Rot4.addBox(0.0F, -4.0F, 0.0F, 1, 8, 1);
        this.Rot4.setRotationPoint(-6.0F, 12.0F, -6.0F);
        this.Rot4.setTextureSize(64, 32);
        this.Rot4.mirror = true;
        setRotation(this.Rot4, 0.0F, 0.0F, 0.0F);
        this.PlateRot1 = new ModelRenderer(this, 0, 21);
        this.PlateRot1.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
        this.PlateRot1.setRotationPoint(4.0F, 16.0F, -7.0F);
        this.PlateRot1.setTextureSize(64, 32);
        this.PlateRot1.mirror = true;
        setRotation(this.PlateRot1, 0.0F, 0.0F, 0.0F);
        this.PlateRot2 = new ModelRenderer(this, 0, 26);
        this.PlateRot2.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
        this.PlateRot2.setRotationPoint(4.0F, 16.0F, 4.0F);
        this.PlateRot2.setTextureSize(64, 32);
        this.PlateRot2.mirror = true;
        setRotation(this.PlateRot2, 0.0F, 0.0F, 0.0F);
        this.PlateRot3 = new ModelRenderer(this, 13, 21);
        this.PlateRot3.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
        this.PlateRot3.setRotationPoint(-7.0F, 16.0F, -7.0F);
        this.PlateRot3.setTextureSize(64, 32);
        this.PlateRot3.mirror = true;
        setRotation(this.PlateRot3, 0.0F, 0.0F, 0.0F);
        this.PlateRot4 = new ModelRenderer(this, 13, 26);
        this.PlateRot4.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
        this.PlateRot4.setRotationPoint(-7.0F, 16.0F, 4.0F);
        this.PlateRot4.setTextureSize(64, 32);
        this.PlateRot4.mirror = true;
        setRotation(this.PlateRot4, 0.0F, 0.0F, 0.0F);
        this.Shape1 = new ModelRenderer(this, 20, 11);
        this.Shape1.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1);
        this.Shape1.setRotationPoint(-3.0F, 8.0F, -4.0F);
        this.Shape1.setTextureSize(64, 32);
        this.Shape1.mirror = true;
        setRotation(this.Shape1, 0.0F, 0.0F, 0.0F);
        this.Shape2 = new ModelRenderer(this, 20, 14);
        this.Shape2.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1);
        this.Shape2.setRotationPoint(-3.0F, 8.0F, 3.0F);
        this.Shape2.setTextureSize(64, 32);
        this.Shape2.mirror = true;
        setRotation(this.Shape2, 0.0F, 0.0F, 0.0F);
        this.Shape3 = new ModelRenderer(this, 35, 11);
        this.Shape3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 6);
        this.Shape3.setRotationPoint(3.0F, 8.0F, -3.0F);
        this.Shape3.setTextureSize(64, 32);
        this.Shape3.mirror = true;
        setRotation(this.Shape3, 0.0F, 0.0F, 0.0F);
        this.Shape4 = new ModelRenderer(this, 35, 19);
        this.Shape4.addBox(0.0F, 0.0F, 0.0F, 1, 1, 6);
        this.Shape4.setRotationPoint(-4.0F, 8.0F, -3.0F);
        this.Shape4.setTextureSize(64, 32);
        this.Shape4.mirror = true;
        setRotation(this.Shape4, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float size) {
        super.render(entity, f, f1, f2, f3, f4, size);
        setRotationAngles(f, f1, f2, f3, f4, size, entity);
        this.Rot1.render(size);
        this.Rot2.render(size);
        this.Rot3.render(size);
        this.Rot4.render(size);
        this.PlateRot1.render(size);
        this.PlateRot2.render(size);
        this.PlateRot3.render(size);
        this.PlateRot4.render(size);
        this.Shape1.render(size);
        this.Shape2.render(size);
        this.Shape3.render(size);
        this.Shape4.render(size);
    }

    public void render(float size) {}

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float size, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, size, entity);
    }
}

