package foxiwhitee.hellmod.tile.cpu;

import appeng.tile.crafting.TileCraftingTile;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.utils.craft.ICustomAccelerator;
import net.minecraft.item.ItemStack;

public class TileCustomAccelerators extends TileCraftingTile implements ICustomAccelerator {

    public TileCustomAccelerators() {
        super();
    }
    protected ItemStack getItemFromTile(Object obj) {

        int storage = ((TileCustomAccelerators) obj).getAcceleratorCount() / 4;
        if (storage == HellConfig.advanced_accelerator) {
            return new ItemStack(ModBlocks.A_ACCELERATORS, 1, 1);
        } else if (storage == HellConfig.hybrid_accelerator) {
            return new ItemStack(ModBlocks.A_ACCELERATORS, 1, 2);
        } else if (storage == HellConfig.ultimate_accelerator) {
            return new ItemStack(ModBlocks.A_ACCELERATORS, 1, 3);
        } else if (storage == HellConfig.quantum_accelerator) {
            return new ItemStack(ModBlocks.A_ACCELERATORS, 1, 4);
        }
        return super.getItemFromTile(obj);
    }

    @Override
    public int getAcceleratorCount() {
        if (worldObj == null || this.notLoaded() || this.isInvalid()) return 0;
        switch (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) & 3) {
            case 1:
                return HellConfig.hybrid_accelerator;
            case 2:
                return HellConfig.ultimate_accelerator;
            case 3:
                return HellConfig.quantum_accelerator;
            default:
                return HellConfig.advanced_accelerator;
        }
    }

    @Override
    public boolean isAccelerator() {
        return true;
    }

    @Override
    public boolean isStorage() {
        return false;
    }

    @Override
    public boolean isStatus() {
        return false;
    }
}
