package foxiwhitee.hellmod.integration.botania.flowers.logic.generating;

import appeng.api.storage.data.IAEStack;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IGeneratingFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.LevelTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.common.block.ModBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogicRafflowsia implements IGeneratingFlowerLogic {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicRafflowsiaTicks;
    }

    @Override
    public List<LevelTypes> getTypes() {
        return Arrays.asList(LevelTypes.DESTRUCTIVE);
    }

    @Override
    public long getGenerating() {
        return FlowerLogicConfig.flowerLogicRafflowsiaGenerating;
    }

    @Override
    public List<IAEStack> getConsumed() {
        ItemStack day = new ItemStack(ModBlocks.specialFlower);
        ItemStack night = new ItemStack(ModBlocks.specialFlower);
        ItemStack water = new ItemStack(ModBlocks.specialFlower);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", "daybloom");
        day.setTagCompound(tag);
        tag = new NBTTagCompound();
        tag.setString("type", "nightshade");
        night.setTagCompound(tag);
        tag = new NBTTagCompound();
        tag.setString("type", "hydroangreas");
        water.setTagCompound(tag);
        return new ArrayList<>(Arrays.asList(
                AEItemStack.create(day),
                AEItemStack.create(night),
                AEItemStack.create(water)
        ));
    }

    @Override
    public int getLevel() {
        return FlowerLogicConfig.flowerLogicRafflowsiaLevel;
    }
}
