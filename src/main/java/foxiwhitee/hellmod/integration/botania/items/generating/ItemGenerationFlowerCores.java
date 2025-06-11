package foxiwhitee.hellmod.integration.botania.items.generating;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.flowers.*;
import foxiwhitee.hellmod.integration.botania.flowers.logic.generating.*;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemGenerationFlowerCores extends Item implements ICoreGeneratingFlower {
    private final static String[] names = {"core_daybloom", "core_nightshade", "core_hydroangeas", "core_thermalily", "core_endoflame", "core_munchdew", "core_entropinnyum", "core_gourmaryllis", "core_kekimurus", "core_spectrolus", "core_rafflowsia"};
    private final IIcon[] icons = new IIcon[names.length];
    private final String name;

    public ItemGenerationFlowerCores(String name) {
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
            icons[i] = register.registerIcon(foxiwhitee.hellmod.HellCore.MODID + ":botania/cores/generation/" + names[i]);
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
            case "core_daybloom": return new LogicDaybloom();
            case "core_nightshade": return new LogicNightshade();
            case "core_endoflame": return new LogicEndoflame();
            case "core_entropinnyum": return new LogicEntropinnyum();
            case "core_munchdew": return new LogicMunchdew();
            case "core_gourmaryllis": return new LogicGourmaryllis();
            case "core_kekimurus": return new LogicKekimurus();
            case "core_spectrolus": return new LogicSpectrolus();
            case "core_rafflowsia": return new LogicRafflowsia();
            case "core_hydroangeas": return new LogicHydroangeas();
            case "core_thermalily": return new LogicThermalily();
        }
        return null;
    }

    @Override
    public String getName(ItemStack stack) {
        return names[stack.getItemDamage()];
    }
}
