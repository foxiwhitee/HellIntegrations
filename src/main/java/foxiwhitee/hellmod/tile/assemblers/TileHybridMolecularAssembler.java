package foxiwhitee.hellmod.tile.assemblers;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModBlocks;
import net.minecraft.item.ItemStack;

public class TileHybridMolecularAssembler extends TileCustomMolecularAssembler{

    public TileHybridMolecularAssembler() {
        getProxy().setIdlePowerUsage(HellConfig.hybrid_molecular_assembler_power);
    }

    @Override
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(ModBlocks.hybridMolecularAssembler);
    }

    @Override
    public long getMaxCount() {
        return HellConfig.hybrid_molecular_assembler_speed - 1L;
    }

    @Override
    protected double getPower() {
        return HellConfig.hybrid_molecular_assembler_power;
    }

    @Override
    public String getName() {
        return ModBlocks.hybridMolecularAssembler.getUnlocalizedName();
    }
}
