package foxiwhitee.hellmod.integration.blood.itemblock;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.blood.blocks.BlockIchorRune;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockRune extends ItemBlock {
    public ItemBlockRune(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        String name;
        switch (itemstack.getItemDamage()) {
            case 1:
                name = "capacity"; break;
            case 2:
                name = "betterCapacity"; break;
            case 3:
                name = "dislocation"; break;
            case 4:
                name = "efficiency"; break;
            case 5:
                name = "orbCapacity"; break;
            case 6:
                name = "sacrifice"; break;
            case 7:
                name = "selfSacrifice"; break;
            case 8:
                name = "speed"; break;
            case 9:
                name = "acceleration"; break;
            default:
                name = "blank";
        }

        return this.getUnlocalizedName() + "." + name;
    }
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
        if (HellConfig.enable_tooltips) {
            int value = ((BlockIchorRune)(Block.getBlockFromItem(stack.getItem()))).getRuneEffect(stack.getItemDamage());
            list.add(LocalizationUtils.localize("tooltip.blood.rune", value));
        }
    }

    public int getMetadata(int par1) {
        return par1;
    }
}