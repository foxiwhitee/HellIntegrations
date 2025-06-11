package foxiwhitee.hellmod.items.patterns;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import appeng.core.localization.GuiText;
import appeng.items.misc.ItemEncodedPattern;
import appeng.util.Platform;
import foxiwhitee.hellmod.HellCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public abstract class ItemUniversalEncodedPattern extends ItemEncodedPattern {
    public ItemUniversalEncodedPattern(String name) {
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setUnlocalizedName(name);
        this.setTextureName(HellCore.MODID + ":patterns/" + name);
    }

    @Override
    public void addCheckedInformation(ItemStack stack, EntityPlayer player, List<String> lines, boolean displayMoreInfo) {
        ICraftingPatternDetails details = this.getPatternForItem(stack, player.worldObj);
        if (details == null) {
            lines.add(EnumChatFormatting.RED + GuiText.InvalidPattern.getLocal());
        } else {
            IAEItemStack[] in = details.getCondensedInputs();
            IAEItemStack[] out = details.getCondensedOutputs();
            String label = (GuiText.Creates.getLocal()) + ": ";
            String and = ' ' + GuiText.And.getLocal() + ' ';
            String with = GuiText.With.getLocal() + ": ";
            boolean first = true;

            for(IAEItemStack anOut : out) {
                if (anOut != null) {
                    lines.add((first ? label : and) + anOut.getStackSize() + ' ' + Platform.getItemDisplayName(anOut));
                    first = false;
                }
            }

            first = true;

            for(IAEItemStack anIn : in) {
                if (anIn != null) {
                    lines.add((first ? with : and) + anIn.getStackSize() + ' ' + Platform.getItemDisplayName(anIn));
                    first = false;
                }
            }
        }
    }
}
