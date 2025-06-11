package foxiwhitee.hellmod.integration.botania.items.generating;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemUpgradeGeneratorMultiply extends Item {
    private final static String[] names = {"Tier1", "Tier2", "Tier3"};
    private final IIcon[] icons = new IIcon[names.length];
    private final String name;

    public ItemUpgradeGeneratorMultiply(String name) {
        this.name = name;
        setCreativeTab(HellCore.HELL_TAB);
        setUnlocalizedName(name);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        meta = Math.max(0, Math.min(meta, names.length - 1));
        return icons[meta];
    }

    @Override
    public void registerIcons(IIconRegister register) {
        for (int i = 0; i < names.length; i++) {
            icons[i] = register.registerIcon(foxiwhitee.hellmod.HellCore.MODID + ":botania/upgrades/" + name + names[i]);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int meta = (stack != null) ? stack.getItemDamage() : 0;
        return LocalizationUtils.localize(getUnlocalizedName() + names[meta] + ".name");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}