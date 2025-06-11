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

import java.util.List;

public class ItemEUEnergyUpgrades extends Item {
    private static final String[] ITEMS = new String[] {"upgrade1", "upgrade2", "upgrade3", "upgrade4", "upgrade5", "upgrade6", "upgrade7", "upgrade8", "upgrade9", "upgrade10"};
    IIcon[] icons;

    public ItemEUEnergyUpgrades(String name) {
        this.setUnlocalizedName(name);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setMaxStackSize(1);
    }

    public void registerIcons(IIconRegister ir) {
        this.icons = new IIcon[ITEMS.length];
        for (int x = 0; x < ITEMS.length; x++)
            this.icons[x] = ir.registerIcon(HellCore.MODID + ":ic2/upgrades/" + ITEMS[x]);
    }

    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta % this.icons.length];
    }

    public String getUnlocalizedName(ItemStack stack) {
        int i = stack.getItemDamage();
        return getUnlocalizedName() + "." + ITEMS[i];
    }

    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int j = 0; j < this.icons.length; j++)
            list.add(new ItemStack(item, 1, j));
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        int meta = stack.getItemDamage();
        switch (meta) {
            case 0:
                list.add(String.format(LocalizationUtils.localize("tooltip.wyvern_storage_card.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name"), EnergyUtility.formatNumber(HellConfig.energyUpgradeWywern)));
                break;
            case 1:
                list.add(String.format(LocalizationUtils.localize("tooltip.awakened_storage_card.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name"), EnergyUtility.formatNumber(HellConfig.energyUpgradeAwakened)));
                break;
            case 2:
                list.add(String.format(LocalizationUtils.localize("tooltip.chaotic_storage_card.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name"), EnergyUtility.formatNumber(HellConfig.energyUpgradeChaotic)));
                break;
            case 3:
                list.add(String.format(LocalizationUtils.localize("tooltip.arial_storage_card.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name"), EnergyUtility.formatNumber(HellConfig.energyUpgradeArial)));
                break;
        }
    }
}
