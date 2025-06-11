package foxiwhitee.hellmod.integration.botania.flowers.logic.functional;

import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalMana;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

public class LogicOrechidIgnem extends LogicOrechid {
    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicOrechidIgnemTicks;
    }

    @Override
    public long getManaNeed() {
        return FlowerLogicConfig.flowerLogicOrechidIgnemConsume;
    }

    protected Map<String, Integer> getOreMap() {
        return BotaniaAPI.oreWeightsNether;
    }

    protected ItemStack getConsumeItem() {
        return new ItemStack(Blocks.netherrack);
    }
}
