package foxiwhitee.hellmod.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class TileEntitySpecialRendererObjWrapper<T extends TileEntity> extends TileEntitySpecialRenderer {
    private final Class<T> tileClass;

    private final HashMap<String, Integer> partLists = new HashMap<>();

    private final IModelCustom model;

    private final ResourceLocation texture;

    public TileEntitySpecialRendererObjWrapper(Class<T> tileClass, ResourceLocation obj, ResourceLocation texture) {
        this.tileClass = tileClass;
        this.model = AdvancedModelLoader.loadModel(obj);
        this.texture = texture;
    }

    public TileEntitySpecialRendererObjWrapper(Class<T> tileClass, String obj, String texture) {
        this(tileClass, new ResourceLocation(HellCore.MODID, obj), new ResourceLocation(HellCore.MODID, texture));
    }

    public TileEntitySpecialRendererObjWrapper(Class<T> tileClass, String modid, String obj, String texture) {
        this(tileClass, new ResourceLocation(modid, obj), new ResourceLocation(modid, texture));
    }

    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
        if (tile.getClass().isAssignableFrom(this.tileClass))
            renderAt(this.tileClass.cast(tile), x, y, z, f);
    }

    public abstract void renderAt(T paramT, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);

    protected void createList(String part) {
        int list = GL11.glGenLists(1);
        GL11.glNewList(list, 4864);
        if (part == null || part.equals("all")) {
            part = "all";
            this.model.renderAll();
        } else {
            this.model.renderPart(part);
        }
        GL11.glEndList();
        this.partLists.put(part, Integer.valueOf(list));
    }

    protected void renderPart(String part) {
        if (part == null)
            part = "all";
        if (!this.partLists.containsKey(part))
            return;
        GL11.glCallList(((Integer)this.partLists.get(part)).intValue());
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public IModelCustom getModel() {
        return this.model;
    }

    public void bindTexture() {
        bindTexture(this.texture);
    }

    public void bindTexture(ResourceLocation texture) {
        (Minecraft.getMinecraft()).renderEngine.bindTexture(texture);
    }
}