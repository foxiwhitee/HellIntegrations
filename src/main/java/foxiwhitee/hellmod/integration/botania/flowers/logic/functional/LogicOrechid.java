package foxiwhitee.hellmod.integration.botania.flowers.logic.functional;

import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalMana;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

public class LogicOrechid implements IFunctionalFlowerLogic {
    int ticks = 0;

    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicOrechidTicks;
    }

    @Override
    public long getManaNeed() {
        return FlowerLogicConfig.flowerLogicOrechidConsume;
    }

    @Override
    public void use(IFunctionalMana tile) {
        if (tile.getMaxStoredMana() >= getManaNeed()) {
            for (int i = 0; i < getDefaultNeedTicks(); i++) {
                ticks++;
                if (ticks >= getDefaultNeedTicks()) {
                    ticks = 0;
                    if (tile.extractNeedItems(AEItemStack.create(getConsumeItem()))) {
                        Random rand = new Random();

                        ItemStack stack = this.getOreToPut(rand);

                        if (stack != null) {
                            tile.consumeMana(getManaNeed());
                            tile.injectNewItems(AEItemStack.create(stack));
                        }
                    }
                }
            }
        }
    }

    protected ItemStack getConsumeItem() {
        return new ItemStack(Blocks.stone);
    }

    private ItemStack getOreToPut(Random random) {
        Collection<WeightedRandom.Item> values = new ArrayList();
        Map<String, Integer> map = getOreMap();

        for(String s : map.keySet()) {
            values.add(new StringRandomItem((Integer)map.get(s), s));
        }

        String ore = ((StringRandomItem)WeightedRandom.getRandomItem(random, values)).s;

        for(ItemStack stack : OreDictionary.getOres(ore)) {
            Item item = stack.getItem();
            String clname = item.getClass().getName();
            if (!clname.startsWith("gregtech") && !clname.startsWith("gregapi")) {
                return stack;
            }
        }

        return this.getOreToPut(random);
    }

    private static class StringRandomItem extends WeightedRandom.Item {
        public String s;

        public StringRandomItem(int par1, String s) {
            super(par1);
            this.s = s;
        }
    }

    protected Map<String, Integer> getOreMap() {
        return BotaniaAPI.oreWeights;
    }
}
