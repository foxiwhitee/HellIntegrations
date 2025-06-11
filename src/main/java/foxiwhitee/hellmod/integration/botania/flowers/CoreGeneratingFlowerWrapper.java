package foxiwhitee.hellmod.integration.botania.flowers;

import net.minecraft.item.ItemStack;

public final class CoreGeneratingFlowerWrapper implements ICoreGeneratingFlower {
    private final IGeneratingFlowerLogic logic;
    public final String name;
    public CoreGeneratingFlowerWrapper(IGeneratingFlowerLogic logic, String name) {
        this.logic = logic;
        this.name = name;
    }

    @Override
    public IGeneratingFlowerLogic getLogic(String name) {
        return logic;
    }

    @Override
    public String getName(ItemStack stack) {
        return name;
    }
}
