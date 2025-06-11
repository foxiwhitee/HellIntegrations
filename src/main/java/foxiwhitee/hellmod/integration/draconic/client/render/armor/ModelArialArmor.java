package foxiwhitee.hellmod.integration.draconic.client.render.armor;

import com.brandon3055.draconicevolution.client.model.ModelRenderOBJ;
import foxiwhitee.hellmod.HellCore;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelArialArmor extends ModelBiped {
    public ModelRenderOBJ head;

    public ModelRenderOBJ body;

    public ModelRenderOBJ rightArm;

    public ModelRenderOBJ leftArm;

    public ModelRenderOBJ belt;

    public ModelRenderOBJ rightLeg;

    public ModelRenderOBJ leftLeg;

    public ModelRenderOBJ rightBoot;

    public ModelRenderOBJ leftBoot;

    public static final ResourceLocation arial_helm_M = new ResourceLocation(HellCore.MODID, "models/armor/ArialHelmet.obj");

    public static final ResourceLocation arial_BODY_M = new ResourceLocation(HellCore.MODID, "models/armor/ArialBody.obj");

    public static final ResourceLocation arial_L_ARM_M = new ResourceLocation(HellCore.MODID, "models/armor/ArialLeftArm.obj");

    public static final ResourceLocation arial_R_ARM_M = new ResourceLocation(HellCore.MODID, "models/armor/ArialRightArm.obj");

    public static final ResourceLocation arial_BELT_M = new ResourceLocation(HellCore.MODID, "models/armor/ArialBelt.obj");

    public static final ResourceLocation arial_L_LEG_M = new ResourceLocation(HellCore.MODID, "models/armor/ArialLeftLeg.obj");

    public static final ResourceLocation arial_R_LEG_M = new ResourceLocation(HellCore.MODID, "models/armor/ArialRightLeg.obj");

    public static final ResourceLocation arial_L_BOOT_M = new ResourceLocation(HellCore.MODID, "models/armor/ArialLeftBoot.obj");

    public static final ResourceLocation arial_R_BOOT_M = new ResourceLocation(HellCore.MODID, "models/armor/ArialRightBoot.obj");

    public static final ResourceLocation arial_helm_M_T = new ResourceLocation(HellCore.MODID, "textures/models/armor/ArialHelmet.png");

    public static final ResourceLocation arial_BODY_M_T = new ResourceLocation(HellCore.MODID, "textures/models/armor/ArialBody.png");

    public static final ResourceLocation arial_L_ARM_M_T = new ResourceLocation(HellCore.MODID, "textures/models/armor/ArialLeftArm.png");

    public static final ResourceLocation arial_R_ARM_M_T = new ResourceLocation(HellCore.MODID, "textures/models/armor/ArialRightArm.png");

    public static final ResourceLocation arial_BELT_M_T = new ResourceLocation(HellCore.MODID, "textures/models/armor/ArialBelt.png");

    public static final ResourceLocation arial_L_LEG_M_T = new ResourceLocation(HellCore.MODID, "textures/models/armor/ArialLeftLeg.png");

    public static final ResourceLocation arial_R_LEG_M_T = new ResourceLocation(HellCore.MODID, "textures/models/armor/ArialRightLeg.png");

    public static final ResourceLocation arial_L_BOOT_M_T = new ResourceLocation(HellCore.MODID, "textures/models/armor/ArialLeftBoot.png");

    public static final ResourceLocation arial_R_BOOT_M_T = new ResourceLocation(HellCore.MODID, "textures/models/armor/ArialRightBoot.png");

    public ModelArialArmor(float f, boolean isHelmet, boolean isChestPiece, boolean isLeggings, boolean isdBoots) {
        super(f, 0.0F, 128, 128);
        this.bipedHead = new ModelRenderer((ModelBase)this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.bipedBody = new ModelRenderer((ModelBase)this, 16, 16);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.bipedLeftArm = new ModelRenderer((ModelBase)this, 40, 16);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedRightArm = new ModelRenderer((ModelBase)this, 40, 16);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedLeftLeg = new ModelRenderer((ModelBase)this, 0, 16);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedRightLeg = new ModelRenderer((ModelBase)this, 0, 16);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.head = new ModelRenderOBJ((ModelBase)this, arial_helm_M, arial_helm_M_T);
        this.body = new ModelRenderOBJ((ModelBase)this, arial_BODY_M, arial_BODY_M_T);
        this.rightArm = new ModelRenderOBJ((ModelBase)this, arial_R_ARM_M, arial_R_ARM_M_T);
        this.leftArm = new ModelRenderOBJ((ModelBase)this, arial_L_ARM_M, arial_L_ARM_M_T);
        this.belt = new ModelRenderOBJ((ModelBase)this, arial_BELT_M, arial_BELT_M_T);
        this.rightLeg = new ModelRenderOBJ((ModelBase)this, arial_R_LEG_M, arial_R_LEG_M_T);
        this.leftLeg = new ModelRenderOBJ((ModelBase)this, arial_L_LEG_M, arial_L_LEG_M_T);
        this.rightBoot = new ModelRenderOBJ((ModelBase)this, arial_R_BOOT_M, arial_R_BOOT_M_T);
        this.leftBoot = new ModelRenderOBJ((ModelBase)this, arial_L_BOOT_M, arial_L_BOOT_M_T);
        this.bipedHead.cubeList.clear();
        this.bipedHeadwear.cubeList.clear();
        this.bipedBody.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
        this.bipedLeftLeg.cubeList.clear();
        this.bipedRightLeg.cubeList.clear();
        this.body.offsetY = 0.755F;
        this.rightArm.offsetY = 0.755F;
        this.leftArm.offsetY = 0.755F;
        this.head.offsetY = -0.1F;
        this.head.offsetX = -0.033F;
        this.head.offsetZ = 0.1F;
        this.body.offsetY = 0.755F;
        this.body.offsetZ = -0.03F;
        this.rightArm.offsetY = 0.72F;
        this.rightArm.offsetX = -0.18F;
        this.rightArm.offsetZ = -0.05F;
        this.leftArm.offsetY = 0.72F;
        this.leftArm.offsetX = 0.18F;
        this.leftArm.offsetZ = -0.06F;
        this.belt.offsetY = 0.756F;
        this.belt.offsetZ = -0.04F;
        this.rightLeg.offsetY = 0.6F;
        this.rightLeg.offsetX = -0.05F;
        this.leftLeg.offsetY = 0.6F;
        this.leftLeg.offsetX = 0.06F;
        this.rightBoot.offsetY = 0.76F;
        this.rightBoot.offsetX = -0.03F;
        this.leftBoot.offsetY = 0.76F;
        this.leftBoot.offsetX = 0.03F;
        this.leftLeg.scale = 0.06666667F;
        this.rightLeg.scale = 0.06666667F;
        this.leftBoot.scale = 0.06666667F;
        this.rightBoot.scale = 0.06666667F;
        if (isHelmet)
            this.bipedHead.addChild((ModelRenderer)this.head);
        if (isChestPiece) {
            this.bipedBody.addChild((ModelRenderer)this.body);
            this.bipedLeftArm.addChild((ModelRenderer)this.leftArm);
            this.bipedRightArm.addChild((ModelRenderer)this.rightArm);
        }
        if (isLeggings) {
            this.bipedLeftLeg.addChild((ModelRenderer)this.leftLeg);
            this.bipedRightLeg.addChild((ModelRenderer)this.rightLeg);
            this.bipedBody.addChild((ModelRenderer)this.belt);
        }
        if (isdBoots) {
            this.bipedLeftLeg.addChild((ModelRenderer)this.leftBoot);
            this.bipedRightLeg.addChild((ModelRenderer)this.rightBoot);
        }
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (entity == null) {
            this.isSneak = false;
            this.isRiding = false;
            this.isChild = false;
            this.aimedBow = false;
            this.bipedRightArm.rotateAngleX = 0.0F;
            this.bipedRightArm.rotateAngleY = 0.0F;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleX = 0.0F;
            this.bipedLeftArm.rotateAngleY = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedBody.rotateAngleX = 0.0F;
            this.bipedBody.rotateAngleY = 0.0F;
            this.bipedBody.rotateAngleZ = 0.0F;
            this.bipedHead.rotateAngleX = 0.0F;
            this.bipedHead.rotateAngleY = 0.0F;
            this.bipedHead.rotateAngleZ = 0.0F;
            this.bipedLeftLeg.rotateAngleX = 0.0F;
            this.bipedLeftLeg.rotateAngleY = 0.0F;
            this.bipedLeftLeg.rotateAngleZ = 0.0F;
            this.bipedRightLeg.rotateAngleX = 0.0F;
            this.bipedRightLeg.rotateAngleY = 0.0F;
            this.bipedRightLeg.rotateAngleZ = 0.0F;
            setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, (Entity)null);
        } else {
            super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        }
        this.bipedHead.render(0.07692308F);
        this.bipedRightArm.render(0.06666667F);
        this.bipedLeftArm.render(0.06666667F);
        this.bipedBody.render(0.06666667F);
        this.bipedRightLeg.render(0.0625F);
        this.bipedLeftLeg.render(0.0625F);
    }

    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightArm.rotationPointZ = 0.0F;
        this.bipedLeftArm.rotationPointZ = 0.0F;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;
        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        this.bipedBody.rotateAngleX = 0.0F;
        this.bipedRightLeg.rotationPointZ = 0.1F;
        this.bipedLeftLeg.rotationPointZ = 0.1F;
        this.bipedRightLeg.rotationPointY = 12.0F;
        this.bipedLeftLeg.rotationPointY = 12.0F;
        this.bipedHead.rotationPointY = 0.0F;
        this.bipedHeadwear.rotationPointY = 0.0F;
        this.leftLeg.rotationPointZ = 0.0F;
        this.rightLeg.rotationPointZ = 0.0F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
    }
}
