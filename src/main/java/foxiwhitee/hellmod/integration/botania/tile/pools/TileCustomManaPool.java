package foxiwhitee.hellmod.integration.botania.tile.pools;

import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;

import java.lang.reflect.Field;
import java.util.HashMap;

import static vazkii.botania.client.core.helper.RenderHelper.drawTexturedModalRect;
public abstract class TileCustomManaPool extends TilePool {
    public static HashMap<String, Field> map = new HashMap<>();

    public TileCustomManaPool() {}

    public void renderHUD(Minecraft mc, ScaledResolution res) {
        ItemStack pool = new ItemStack(getBlockType());
        String name = LocalizationUtils.localize(pool.getUnlocalizedName() + ".name");
        int color = 4474111;
        HUDHandler.drawSimpleManaHUD(color, ((Integer)getFieldValue("knownMana", Integer.valueOf(0))).intValue(), getMaxMana(), name, res);
        int x = res.getScaledWidth() / 2 - 11;
        int y = res.getScaledHeight() / 2 + 30;
        int u = isOutputtingPower() ? 22 : 0;
        int v = 38;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        mc.renderEngine.bindTexture(HUDHandler.manaBar);
        drawTexturedModalRect(x, y, 0.0F, u, v, 22, 15);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ItemStack tablet = new ItemStack(ModItems.manaTablet);
        ItemManaTablet.setStackCreative(tablet);
        RenderHelper.enableGUIStandardItemLighting();
        RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, tablet, x - 20, y);
        RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, pool, x + 26, y);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(3042);
    }

    public void setFieldValue(String name, Object value) {
        try {
            if (!map.containsKey(name)) {
                Field field = TilePool.class.getDeclaredField(name);
                field.setAccessible(true);
                map.put(name, field);
            }
            ((Field)map.get(name)).set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T getFieldValue(String name, T baseValue) {
        try {
            if (!map.containsKey(name)) {
                Field field = TilePool.class.getDeclaredField(name);
                field.setAccessible(true);
                map.put(name, field);
            }
            return (T)((Field)map.get(name)).get(this);
        } catch (Exception e) {
            e.printStackTrace();
            return baseValue;
        }
    }

    public abstract int getMaxMana();

    public abstract String getName();
}
