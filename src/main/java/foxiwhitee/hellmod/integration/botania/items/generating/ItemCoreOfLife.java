package foxiwhitee.hellmod.integration.botania.items.generating;


import appeng.util.Platform;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.flowers.*;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemCoreOfLife extends Item implements IItemWithEffect {
    private static final String[] items = {"Burning", "Crystal", "Destructive", "EternalFog", "eternalSolstice", "fullMoon", "Thunder", "WildHunt", "Yggdrasil", "Asgard"};
    private final IIcon[] icons = new IIcon[items.length];
    private final String name;

    public ItemCoreOfLife(String name) {
        this.name = name;
        setUnlocalizedName(name);
        this.hasSubtypes = true;
        this.maxStackSize = 1;
    }

    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "_" + (stack != null ? stack.getItemDamage() : 0);
    }

    public void getSubItems(Item item, CreativeTabs tab, List list) {
        if (list != null) {
            for (int i = 0; i < items.length; i++) {
                ItemStack stack = new ItemStack(item, 1, i);
            }
        }
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (Platform.isClient()) {
            if (stack == null || player == null || list == null) {
                throw new IllegalArgumentException("stack, player, and list must not be null");
            }
            NBTTagCompound tag = stack.getTagCompound();
            List<String> desc = new ArrayList<>();
            if (tag != null) {
                if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
                    GeneratingRandomEffects.generateDescription(stack, desc);
                    Collections.sort(desc);
                    list.addAll(desc);
                } else {
                    list.add(LocalizationUtils.localize("botania.shift_for_add_info"));
                }
                PartRarity.generateDescription(stack.getTagCompound(), list);
            }
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity player, int p_77663_4_, boolean p_77663_5_) {
        super.onUpdate(stack, world, player, p_77663_4_, p_77663_5_);
        if (stack != null) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) {
                GeneratingRandomEffects.generateNBT(stack, getFlowerType(stack));
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public PartTypes getType() {
        return PartTypes.CoreOfLife;
    }

    @Override
    public LevelTypes getFlowerType(ItemStack stack) {
        return (stack.getItemDamage() >= 0 && stack.getItemDamage() < icons.length) ? LevelTypes.getType(items[stack.getItemDamage()]) : LevelTypes.BURNING;
    }

    public void registerIcons(IIconRegister register) {
        if (register == null) {
            throw new IllegalArgumentException("register must not be null");
        }
        for (int i = 0; i < icons.length; i++) {
            this.icons[i] = register.registerIcon(HellCore.MODID + ":botania/corelife/" + name + items[i]);
        }
    }

    public IIcon getIconFromDamage(int meta) {
        if (meta >= 0 && meta < this.icons.length) {
            return this.icons[meta];
        } else {
            return null;
        }
    }
}