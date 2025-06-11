package foxiwhitee.hellmod.asm;

import net.minecraft.client.gui.FontRenderer;

public class MinecraftHook {

    public static int hexToInt(String hex, boolean shadow) {
        hex = hex.replace("#", "");
        if (hex.length() != 6) return 0xFFFFFFFF;
        try {
            int i = Integer.parseInt(hex, 16);
            if (shadow) {
                int r = (i >> 16) & 0xFF;
                int g = (i >> 8) & 0xFF;
                int b = i & 0xFF;

                r = r / 4;
                g = g / 4;
                b = b / 4;

                i = (r << 16) | (g << 8) | b;
            }
            return i;
        } catch (NumberFormatException e) {
            return 0xFFFFFFFF;
        }
    }

    public static int getStringWidth(String text, FontRenderer renderer) {
        if (text == null) return 0;
        int i = 0;
        boolean flag = false;
        for (int j = 0; j < text.length(); j++) {
            char c0 = text.charAt(j);
            if (c0 == '#' && j + 7 < text.length()) {
                j += 6;
                continue;
            }
            int k = renderer.getCharWidth(c0);
            if (k < 0 && j < text.length() - 1) {
                j++;
                c0 = text.charAt(j);
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') flag = false;
                } else {
                    flag = true;
                }
                k = 0;
            }
            i += k;
            if (flag && k > 0) i++;
        }
        return i;
    }

}
