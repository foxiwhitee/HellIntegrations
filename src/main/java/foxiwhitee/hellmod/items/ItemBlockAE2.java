package foxiwhitee.hellmod.items;

import appeng.block.AEBaseItemBlock;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockAE2 extends AEBaseItemBlock {
    private final Block blockType;
    public ItemBlockAE2(Block id) {
        super(id);
        blockType = id;
    }

    @Override
    public void addCheckedInformation(ItemStack itemStack, EntityPlayer player, List<String> toolTip, boolean advancedToolTips) {
        if (HellConfig.enable_tooltips) {
            if (this.blockType.equals(ModBlocks.A_ADVANCED_INTERFACE)){
                toolTip.add(LocalizationUtils.localize("tooltip.interface", 18));
            } else if (this.blockType.equals(ModBlocks.A_HYBRID_INTERFACE)){
                toolTip.add(LocalizationUtils.localize("tooltip.interface", 27));
            } else if (this.blockType.equals(ModBlocks.A_ULTIMATE_INTERFACE)){
                toolTip.add(LocalizationUtils.localize("tooltip.interface", 36));
            } else if (this.blockType.equals(ModBlocks.autoCrystallizer)){
                toolTip.add(LocalizationUtils.localize("tooltip.autoCrystallizer"));
            } else if (this.blockType.equals(ModBlocks.autoPress)){
                toolTip.add(LocalizationUtils.localize("tooltip.autoPress"));
            } else if (this.blockType.equals(DraconicEvolutionIntegration.autoAwakener)){
                toolTip.add(LocalizationUtils.localize("tooltip.autoAwakener"));
            }
        }
    }
}
