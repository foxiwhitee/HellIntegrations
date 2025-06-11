package foxiwhitee.hellmod.client.render.assemblers;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.util.ResourceLocation;

public class RenderItemUltimateMolecularAssembler extends RenderItemCustomMolecularAssembler{
    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(HellCore.MODID, "textures/blocks/ae2/ultimate_molecular_assembler.png");
    }
}
