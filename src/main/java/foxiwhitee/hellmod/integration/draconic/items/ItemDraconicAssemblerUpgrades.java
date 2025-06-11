package foxiwhitee.hellmod.integration.draconic.items;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemDraconicAssemblerUpgrades extends Item {
    private static final String[] ITEMS = new String[] {"speed_card", "stack_card", "wyvern_module", "awakened_module", "chaotic_module", "arial_module"};
    IIcon[] icons;

    public ItemDraconicAssemblerUpgrades(String name) {
        this.setUnlocalizedName(name);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setMaxStackSize(1);
        hasSubtypes = true;
    }

    public void registerIcons(IIconRegister ir) {
        this.icons = new IIcon[ITEMS.length];
        for (int x = 0; x < ITEMS.length; x++)
            this.icons[x] = ir.registerIcon(HellCore.MODID + ":draconic/" + ITEMS[x]);
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
                list.add(String.format(LocalizationUtils.localize("tooltip.speed_card_ass.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name"), HellConfig.draconicAssemblerSpeedUpgrade));
                break;
            case 1:
                list.add(String.format(LocalizationUtils.localize("tooltip.stack_card_ass.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name")));
                break;
            case 2:
                list.add(String.format(LocalizationUtils.localize("tooltip.wyvern_module_ass.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name")));
                break;
            case 3:
                list.add(String.format(LocalizationUtils.localize("tooltip.awakened_module_ass.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name")));
                break;
            case 4:
                list.add(String.format(LocalizationUtils.localize("tooltip.chaotic_module_ass.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name")));
                break;
            case 5:
                list.add(String.format(LocalizationUtils.localize("tooltip.arial_module_ass.desc"), LocalizationUtils.localize(DraconicEvolutionIntegration.draconicAssembler.getUnlocalizedName() + ".name")));
                break;
        }
    }
}
