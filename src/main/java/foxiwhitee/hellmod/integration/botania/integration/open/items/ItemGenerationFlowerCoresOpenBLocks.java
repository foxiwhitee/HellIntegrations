package foxiwhitee.hellmod.integration.botania.integration.open.items;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.flowers.CoreGeneratingFlowerWrapper;
import foxiwhitee.hellmod.integration.botania.flowers.GeneratingFlowersRegister;
import foxiwhitee.hellmod.integration.botania.flowers.ICoreGeneratingFlower;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.integration.open.flower.logic.LogicArcanerose;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemGenerationFlowerCoresOpenBLocks extends Item implements ICoreGeneratingFlower {
    private final static String[] names = {"core_arcanerose"};
    private final IIcon[] icons = new IIcon[names.length];
    private final String name;

    public ItemGenerationFlowerCoresOpenBLocks(String name) {
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
        return new LogicArcanerose();
    }

    @Override
    public String getName(ItemStack stack) {
        return names[stack.getItemDamage()];
    }
}
