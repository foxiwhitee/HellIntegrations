package foxiwhitee.hellmod.integration.avaritia.utils;

import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.item.ItemStack;

public enum NeutronCollectorsOutput {
    BASE(new ItemStack(LudicrousItems.resource, 1, 2)),
    ADVANCED(new ItemStack(LudicrousItems.resource, 2, 3)),
    HYBRID(new ItemStack(LudicrousItems.resource, 1, 4)),
    ULTIMATE(new ItemStack(LudicrousItems.resource, 1, 4)),
    QUANTUM(new ItemStack(LudicrousBlocks.resource_block, 1, 0));

    private ItemStack stack;
    NeutronCollectorsOutput(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }
}
