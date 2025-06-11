package foxiwhitee.hellmod.client.render.assemblers;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.util.ResourceLocation;

public class RenderItemHybridMolecularAssembler extends RenderItemCustomMolecularAssembler{
    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(HellCore.MODID, "textures/blocks/ae2/hybrid_molecular_assembler.png");
    }
}
