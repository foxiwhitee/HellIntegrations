package foxiwhitee.hellmod.integration.botania.client;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.awt.Color;
import java.util.HashMap;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.blocks.BlockCustomManaPool;
import foxiwhitee.hellmod.utils.coord.BlockPos;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import vazkii.botania.common.block.tile.mana.TilePool;

public class ManaPoolInfoRenderer {
    public int ticks = 0;

    public static void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0.0D, y + height, zLevel, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, 1.0D, 1.0D);
        tessellator.addVertexWithUV(x + width, y + 0.0D, zLevel, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + 0.0D, y + 0.0D, zLevel, 0.0D, 0.0D);
        tessellator.draw();
    }

    public static HashMap<Integer, String> name = new HashMap<>();

    static {
        name.put(Integer.valueOf(0), LocalizationUtils.localize("tile.botania:pool0"));
        name.put(Integer.valueOf(1), LocalizationUtils.localize("tile.botania:pool1"));
        name.put(Integer.valueOf(2), LocalizationUtils.localize("tile.botania:pool2"));
    }

    public static final ResourceLocation info_texture = new ResourceLocation(HellCore.MODID, "textures/gui/info.png");

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent renderWorldLastEvent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP thePlayer = minecraft.thePlayer;
        double n = thePlayer.lastTickPosX + (thePlayer.posX - thePlayer.lastTickPosX) * renderWorldLastEvent.partialTicks;
        double n2 = thePlayer.lastTickPosY + (thePlayer.posY - thePlayer.lastTickPosY) * renderWorldLastEvent.partialTicks;
        double n3 = thePlayer.lastTickPosZ + (thePlayer.posZ - thePlayer.lastTickPosZ) * renderWorldLastEvent.partialTicks;
        GL11.glPushMatrix();
        GL11.glTranslated(-n, -n2, -n3);
        MovingObjectPosition objectMouseOver = minecraft.objectMouseOver;
        if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos loc = new BlockPos((World)minecraft.theWorld, objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ);
            TileEntity tileEntity = minecraft.theWorld.getTileEntity(objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ);
            if (Loader.isModLoaded("Botania") &&
                    tileEntity instanceof TilePool) {
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDepthMask(false);
                GL11.glTranslated((loc.getX() + 0.5F), (loc.getY() + 0.3F), (loc.getZ() + 0.5F));
                GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(180.0F + (Minecraft.getMinecraft()).thePlayer.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-0.05D, -0.75D, 0.0D);
                Minecraft.getMinecraft().getTextureManager().bindTexture(info_texture);
                drawTexturedQuadFit(-0.6D, -0.03D, 1.4D, 0.3D, 0.0D);
                GL11.glScalef(0.008F, 0.008F, 0.008F);
                GL11.glTranslatef(-12.0F, 6.0F, 0.0F);
                String pool = LocalizationUtils.localize(tileEntity.getWorldObj().getBlock(loc.getX(), loc.getY(), loc.getZ()).getUnlocalizedName() + ".name");
                if (tileEntity.getWorldObj().getBlock(loc.getX(), loc.getY(), loc.getZ()) instanceof vazkii.botania.common.block.mana.BlockPool)
                    switch (tileEntity.getBlockMetadata()) {
                        case 0:
                            pool = LocalizationUtils.localize("tile.botania:pool0.name");
                            break;
                        case 1:
                            pool = LocalizationUtils.localize("tile.botania:pool1.name");
                            break;
                        case 2:
                            pool = LocalizationUtils.localize("tile.botania:pool2.name");
                            break;
                        case 3:
                            pool = LocalizationUtils.localize("tile.botania:pool3.name");
                            break;
                    }
                if (tileEntity.getWorldObj().getBlock(loc.getX(), loc.getY(), loc.getZ()) instanceof BlockCustomManaPool)
                    GL11.glTranslated(-12.0D, 0.0D, 0.0D);
                int manaStorage = ((TilePool)tileEntity).manaCap;
                int currentMana = ((TilePool)tileEntity).getCurrentMana();
                String mana = "§7Mana: §6"+ currentMana + "/" + manaStorage;
                minecraft.fontRenderer.drawString("§6"+ pool, -45, 0, 245);
                minecraft.fontRenderer.drawString(mana, -45, 10, Color.ORANGE.getRGB());
                GL11.glDepthMask(true);
                GL11.glPopMatrix();
            }
        }
        GL11.glPopMatrix();
    }
}
