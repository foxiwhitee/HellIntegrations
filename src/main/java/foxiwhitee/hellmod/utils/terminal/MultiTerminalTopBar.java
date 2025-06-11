package foxiwhitee.hellmod.utils.terminal;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.util.ResourceLocation;

public enum MultiTerminalTopBar {
    Vanilla_Crafting_Workbench(0, 0, 0, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Vanilla_Patterns_Workbench(1, 0, 0, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Avaritia_Crafting_Big(2, 0, 56, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Avaritia_Patterns_Big(3, 0, 56, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Avaritia_Patterns_Neutron(4, 0, 112, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Botania_Patterns_Petals(5, 0, 168, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Botania_Patterns_Mana_Pool(6, 0, 224, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Botania_Patterns_Elven_Trade(7, 0, 280, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Botania_Patterns_Rune_Altar(8, 0, 336, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Botania_Patterns_Pure_Daisy(9, 0, 392, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Draconic_Patterns_Fusion(10, 0, 448, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Draconic_Patterns_Assembler(11, 176, 0, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Thaumcraft_Patterns_Matrix(12, 176, 56, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png"),
    Thaumcraft_Patterns_Alchemy(13, 176, 112, 176, 56, "textures/gui/gui_multi_terminal_top_bar.png");

    private final int index;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final ResourceLocation texture;

    MultiTerminalTopBar(int index, int x, int y, int width, int height, String texture) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = new ResourceLocation(HellCore.MODID, texture);
    }

    public int getHeight() {
        return height;
    }

    public int getIndex() {
        return index;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ResourceLocation getTexture() {
        return texture;
    }
}
