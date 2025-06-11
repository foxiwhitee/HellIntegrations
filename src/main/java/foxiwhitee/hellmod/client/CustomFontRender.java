package foxiwhitee.hellmod.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;
import java.util.List;

public class CustomFontRender extends FontRenderer {
    public CustomFontRender(GameSettings gameSettings, ResourceLocation fontTexture, TextureManager textureManager, boolean unicode) {
        super(gameSettings, fontTexture, textureManager, unicode);
    }

    @Override
    public int drawString(String text, int x, int y, int color) {
        if (text == null) return 0;
        if (text.contains("#") && text.length() >= 7) {
            try {
                int hexIndex = text.indexOf("#");
                String hex = text.substring(hexIndex, hexIndex + 7);
                int hexColor = hexToInt(hex);
                text = text.replace(hex, "");
                return super.drawString(text, x, y, hexColor, false);
            } catch (Exception e) {
                return super.drawString(text, x, y, color, false);
            }
        }
        return super.drawString(text, x, y, color, false);
    }

    public static int hexToInt(String hex) {
        hex = hex.replace("#", "");
        if (hex.length() != 6) return 0xFFFFFFFF;
        try {
            return (0xFF << 24) | Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            return 0xFFFFFFFF;
        }
    }

}
