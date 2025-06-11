package foxiwhitee.hellmod.me;

import appeng.api.implementations.tiles.IChestOrDrive;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEStack;
import appeng.client.texture.ExtraBlockTextures;
import appeng.core.sync.GuiBridge;
import appeng.util.Platform;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class FluidCellHandler implements ICellHandler {
    public boolean isCell(ItemStack stack) {
        return FluidCellInventory.isCell(stack);
    }

    public IMEInventoryHandler<? extends IAEStack<?>> getCellInventory(ItemStack stack, ISaveProvider save, StorageChannel storageChannel) {
        return storageChannel == StorageChannel.ITEMS ? FluidCellInventory.getCell(stack, save) : null;
    }

    public IIcon getTopTexture_Light() {
        return ExtraBlockTextures.BlockMEChestItems_Light.getIcon();
    }

    public IIcon getTopTexture_Medium() {
        return ExtraBlockTextures.BlockMEChestItems_Medium.getIcon();
    }

    public IIcon getTopTexture_Dark() {
        return ExtraBlockTextures.BlockMEChestItems_Dark.getIcon();
    }

    public void openChestGui(EntityPlayer player, IChestOrDrive chest, ICellHandler p2, IMEInventoryHandler p3, ItemStack p4, StorageChannel p5) {
        Platform.openGUI(player, (TileEntity) chest, chest.getUp(), GuiBridge.GUI_ME);
    }

    public int getStatusForCell(ItemStack stack, IMEInventory handler) {
        if (handler instanceof FluidCellInventoryHandler) {
            FluidCellInventory cellInv = (FluidCellInventory) ((FluidCellInventoryHandler) handler).getCellInv();
            return cellInv != null ? cellInv.getStatusForCell() : 0;
        }
        return 0;
    }

    public double cellIdleDrain(ItemStack p0, IMEInventory handler) {
        if (handler instanceof FluidCellInventoryHandler) {
            FluidCellInventory cellInv = (FluidCellInventory) ((FluidCellInventoryHandler) handler).getCellInv();
            return cellInv != null ? cellInv.getIdleDrain() : 0.0;
        }
        return 0.0;
    }
}