package foxiwhitee.hellmod.integration.botania.integration.avaritia.items;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.flowers.*;
import foxiwhitee.hellmod.integration.botania.integration.avaritia.flower.logic.LogicAsgardandelion;
import foxiwhitee.hellmod.integration.botania.integration.avaritia.flower.logic.LogicHelibrium;
import foxiwhitee.hellmod.integration.botania.integration.avaritia.flower.logic.LogicMidgaran;
import foxiwhitee.hellmod.integration.botania.integration.avaritia.flower.logic.LogicValharin;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemGenerationFlowerCoresAvaritia extends Item implements ICoreGeneratingFlower {
    private final static String[] names = {"core_asgardandelion", "core_helibrium", "core_valharin", "core_midgaran"};
    private final IIcon[] icons = new IIcon[names.length];
    private final String name;

    public ItemGenerationFlowerCoresAvaritia(String name) {
        this.name = name;
        setCreativeTab(HellCore.HELL_TAB);
        setUnlocalizedName(name);
        hasSubtypes = true;
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        meta = Math.max(0, Math.min(meta, names.length - 1));
        return icons[meta];
    }

    @Override
    public void registerIcons(IIconRegister register) {
        for (int i = 0; i < names.length; i++) {
            icons[i] = register.registerIcon(HellCore.MODID + ":botania/cores/generation/" + names[i]);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int meta = (stack != null) ? stack.getItemDamage() : 0;
        return LocalizationUtils.localize(getUnlocalizedName()) + "." + names[meta] + ".name";
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(item, 1, i));
            GeneratingFlowersRegister.addFlower(names[i], new CoreGeneratingFlowerWrapper(getLogic(names[i]), names[i]));
        }
    }

    @Override
    public IGeneratingFlowerLogic getLogic(String name) {
        switch (name) {
            case "core_asgardandelion": return new LogicAsgardandelion();
            case "core_helibrium": return new LogicHelibrium();
            case "core_valharin": return new LogicValharin();
            case "core_midgaran": return new LogicMidgaran();
        }
        return null;
    }

    @Override
    public String getName(ItemStack stack) {
        return names[stack.getItemDamage()];
    }
}
