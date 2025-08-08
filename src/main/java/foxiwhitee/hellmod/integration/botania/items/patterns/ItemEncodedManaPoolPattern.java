package foxiwhitee.hellmod.integration.botania.items.patterns;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import appeng.core.localization.GuiText;
import appeng.util.Platform;
import foxiwhitee.hellmod.helpers.UniversalPatternHelper;
import foxiwhitee.hellmod.integration.botania.items.ae.ItemManaDrop;
import foxiwhitee.hellmod.integration.botania.recipes.CustomRecipeManaInfusion;
import foxiwhitee.hellmod.items.patterns.ItemUniversalEncodedPattern;
import foxiwhitee.hellmod.localization.CustomGuiText;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;

import java.util.List;

public class ItemEncodedManaPoolPattern extends ItemUniversalEncodedPattern {

    public ItemEncodedManaPoolPattern(String name) {
        super(name);
    }

    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        try {
            return new UniversalPatternHelper(is, false, 2, 1, matrix -> {
                ItemStack itemstack = matrix.getStackInSlot(0);
                if (itemstack.getItem() instanceof ItemManaDrop) {
                    itemstack = matrix.getStackInSlot(1);
                }

                if (itemstack == null) {
                    return null;
                }

                CustomRecipeManaInfusion recipe;

                for (int i = 0; i < BotaniaAPI.manaInfusionRecipes.size(); i++) {
                    recipe = new CustomRecipeManaInfusion(BotaniaAPI.manaInfusionRecipes.get(i));
                    if (recipe.matches(itemstack)) {
                        return recipe;
                    }
                }
                return null;
            });
        } catch (Throwable var4) {
            return null;
        }
    }


}
