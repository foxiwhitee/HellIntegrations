package foxiwhitee.hellmod.client.render;

import foxiwhitee.hellmod.utils.coord.CustomDimensionalCoord;

import java.util.ArrayList;
import java.util.List;

public class BlockPosHighlighter {
    private static final List<CustomDimensionalCoord> highlightedBlocks = new ArrayList<>();

    private static long expireHighlight;

    private static final int min = 3000;

    private static final int max = 30000;

    public static void highlightBlock(CustomDimensionalCoord c, long expireHighlight) {
        highlightBlock(c, expireHighlight, true);
    }

    public static void highlightBlock(CustomDimensionalCoord c, long expireHighlight, boolean clear) {
        if (clear)
            clear();
        highlightedBlocks.add(c);
        BlockPosHighlighter.expireHighlight = Math.max(BlockPosHighlighter.expireHighlight,

                Math.min(
                        System.currentTimeMillis() + 30000L,
                        Math.max(expireHighlight, System.currentTimeMillis() + 3000L)));
    }

    public static List<CustomDimensionalCoord> getHighlightedBlocks() {
        return highlightedBlocks;
    }

    public static CustomDimensionalCoord getHighlightedBlock() {
        return highlightedBlocks.isEmpty() ? null : highlightedBlocks.get(0);
    }

    public static void clear() {
        highlightedBlocks.clear();
        expireHighlight = -1L;
    }

    public static void remove(CustomDimensionalCoord c) {
        highlightedBlocks.remove(c);
    }

    public static long getExpireHighlight() {
        return expireHighlight;
    }
}