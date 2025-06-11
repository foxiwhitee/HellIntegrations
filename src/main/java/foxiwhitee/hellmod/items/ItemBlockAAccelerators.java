package foxiwhitee.hellmod.items;

import appeng.block.AEBaseItemBlock;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.blocks.cpu.BlockCustomAccelerators;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockAAccelerators extends AEBaseItemBlock {
    private final Block blockType;
    public ItemBlockAAccelerators(Block block) {
        super(block);
        blockType = block;
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return "tile." + BlockCustomAccelerators.names[itemStack.getItemDamage() % BlockCustomAccelerators.names.length] + "_crafting_accelerator";
    }

    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public void addCheckedInformation(ItemStack itemStack, EntityPlayer player, List<String> toolTip, boolean advancedToolTips) {
        if (HellConfig.enable_tooltips) {
            if (this.blockType.equals(ModBlocks.A_ACCELERATORS)){
                int count = 0;
                switch (itemStack.getItemDamage()) {
                    case 0: count = HellConfig.advanced_accelerator; break;
                    case 1: count = HellConfig.hybrid_accelerator; break;
                    case 2: count = HellConfig.ultimate_accelerator; break;
                    case 3: count = HellConfig.quantum_accelerator; break;
                }
                toolTip.add(LocalizationUtils.localize("tooltip.accelerators", count));
            }
        }
    }
}
