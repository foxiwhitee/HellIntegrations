package foxiwhitee.hellmod.integration.botania.flowers.logic.functional;

import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.config.FlowerLogicConfig;
import foxiwhitee.hellmod.integration.botania.flowers.GeneratingRandomEffects;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalMana;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;

public class LogicAdedAmaranthus implements IFunctionalFlowerLogic {
    int ticks = 0;

    @Override
    public int getDefaultNeedTicks() {
        return FlowerLogicConfig.flowerLogicAdedAmaranthusTicks;
    }

    @Override
    public long getManaNeed() {
        return FlowerLogicConfig.flowerLogicAdedAmaranthusConsume;
    }

    @Override
    public void use(IFunctionalMana tile) {
        if (tile.getMaxStoredMana() >= getManaNeed()) {
            for (int i = 0; i < getDefaultNeedTicks(); i++) {
                ticks++;
                if (ticks >= getDefaultNeedTicks()) {
                    ticks = 0;
                    ItemStack out = new ItemStack(ModBlocks.flower, 1, GeneratingRandomEffects.randomBetween(0, 16));
                    if (out != null) {
                        tile.consumeMana(getManaNeed());
                        tile.injectNewItems(AEItemStack.create(out));
                    }
                }
            }
        }
    }
}
