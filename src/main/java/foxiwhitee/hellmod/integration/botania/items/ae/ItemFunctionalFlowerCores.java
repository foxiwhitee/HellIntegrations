package foxiwhitee.hellmod.integration.botania.items.ae;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.flowers.ICoreFunctionalFlower;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.logic.functional.LogicAdedAmaranthus;
import foxiwhitee.hellmod.integration.botania.flowers.logic.functional.LogicLoonium;
import foxiwhitee.hellmod.integration.botania.flowers.logic.functional.LogicOrechid;
import foxiwhitee.hellmod.integration.botania.flowers.logic.functional.LogicOrechidIgnem;
import foxiwhitee.hellmod.integration.botania.flowers.logic.generating.*;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemFunctionalFlowerCores extends Item implements ICoreFunctionalFlower {
    private final static String[] names = {"core_adedAmaranthus", "core_loonium", "core_orechid", "core_orechidIgnem"};
    private final IIcon[] icons = new IIcon[names.length];
    private final String name;

    public ItemFunctionalFlowerCores(String name) {
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
            icons[i] = register.registerIcon(foxiwhitee.hellmod.HellCore.MODID + ":botania/cores/functional/" + names[i]);
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
        }
    }

    @Override
    public IFunctionalFlowerLogic getLogic(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 0: return new LogicAdedAmaranthus();
            case 1: return new LogicLoonium();
            case 2: return new LogicOrechid();
            case 3: return new LogicOrechidIgnem();
        }
        return null;
    }
}
