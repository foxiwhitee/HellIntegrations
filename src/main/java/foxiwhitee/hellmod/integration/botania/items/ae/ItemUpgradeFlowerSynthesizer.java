package foxiwhitee.hellmod.integration.botania.items.ae;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemUpgradeFlowerSynthesizer extends Item {
    public ItemUpgradeFlowerSynthesizer(String name) {
        setCreativeTab(HellCore.HELL_TAB);
        setUnlocalizedName(name);
        setTextureName(HellCore.MODID + ":botania/upgrades/" + name);
    }
}