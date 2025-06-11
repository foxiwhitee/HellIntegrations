package foxiwhitee.hellmod.integration.botania.entity;

import net.minecraft.world.World;

public class MidgardSpark extends CustomSpark{
    public MidgardSpark(World world) {
        super(world);
    }

    @Override
    public String getName() {
        return "midgardSpark";
    }
}
