package foxiwhitee.hellmod.integration.botania.items.patterns;

import appeng.api.storage.data.IAEItemStack;
import appeng.core.localization.GuiText;
import appeng.util.Platform;
import foxiwhitee.hellmod.integration.botania.helpers.ManaPoolPatternHelper;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import foxiwhitee.hellmod.localization.CustomGuiText;
import foxiwhitee.hellmod.utils.craft.ICraftingPatternDetailsExt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemEncodedManaPoolPattern extends ItemUniversalEncodedPattern {

    public ItemEncodedManaPoolPattern(String name) {
        super(name);
    }

    @Override
    public void addCheckedInformation(ItemStack stack, EntityPlayer player, List<String> lines, boolean displayMoreInfo) {
        ICraftingPatternDetailsExt details = this.getPatternForItemExt(stack, player.worldObj);
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

            String substitutionLabel = CustomGuiText.TypeManaPool.getLocal() + " " + details.getType();
            lines.add(substitutionLabel);
        }
    }

    public ICraftingPatternDetailsExt getPatternForItemExt(ItemStack is, World w) {
        try {
            return new ManaPoolPatternHelper(is, w);
        } catch (Throwable var4) {
            return null;
        }
    }


}
