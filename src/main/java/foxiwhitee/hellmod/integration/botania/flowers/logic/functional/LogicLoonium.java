package foxiwhitee.hellmod.integration.botania.flowers.logic.functional;

import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.GeneratingRandomEffects;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalMana;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ChestGenHooks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;

import java.util.Random;

public class LogicLoonium implements IFunctionalFlowerLogic {
    int ticks = 0;

    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicLoonuimTicks;
    }

    @Override
    public long getManaNeed() {
        return FlowerLogicConfig.flowerLogicLoonuimConsume;
    }

    @Override
    public void use(IFunctionalMana tile) {
        if (tile.getMaxStoredMana() >= getManaNeed()) {
            for (int i = 0; i < getDefaultNeedTicks(); i++) {
                ticks++;
                if (ticks >= getDefaultNeedTicks()) {
                    ticks = 0;
                    Random rand = new Random();

                    ItemStack stack;
                    do {
                        stack = ChestGenHooks.getOneItem("dungeonChest", rand);
                    } while (stack == null || BotaniaAPI.looniumBlacklist.contains(stack.getItem()));

                    if (stack != null) {
                        tile.consumeMana(getManaNeed());
                        tile.injectNewItems(AEItemStack.create(stack));
                    }
                }
            }
        }
    }
}
