package foxiwhitee.hellmod.tile.assemblers;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModBlocks;
import net.minecraft.item.ItemStack;

public class TileUltimateMolecularAssembler extends TileCustomMolecularAssembler{

    public TileUltimateMolecularAssembler() {
        getProxy().setIdlePowerUsage(HellConfig.ultimate_molecular_assembler_power);
    }

    @Override
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(ModBlocks.ultimateMolecularAssembler);
    }

    @Override
    public long getMaxCount() {
        return HellConfig.ultimate_molecular_assembler_speed - 1L;
    }

    @Override
    protected double getPower() {
        return HellConfig.ultimate_molecular_assembler_power;
    }

    @Override
    public String getName() {
        return ModBlocks.ultimateMolecularAssembler.getUnlocalizedName();
    }
}
