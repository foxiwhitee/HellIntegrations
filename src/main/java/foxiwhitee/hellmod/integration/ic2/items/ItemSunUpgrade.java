package foxiwhitee.hellmod.integration.ic2.items;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.utils.helpers.EnergyUtility;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemSunUpgrade extends Item {
    public ItemSunUpgrade(String name) {
        this.setUnlocalizedName(name);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setMaxStackSize(1);
        this.setTextureName(HellCore.MODID + ":ic2/upgrades/" + name);
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        list.add(String.format(LocalizationUtils.localize("tooltip.wyvern_storage_card.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name"), EnergyUtility.formatNumber(HellConfig.energyUpgradeWywern)));
    }
}
