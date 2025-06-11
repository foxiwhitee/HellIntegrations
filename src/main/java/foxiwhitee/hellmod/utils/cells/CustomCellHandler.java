package foxiwhitee.hellmod.utils.cells;

import appeng.api.implementations.tiles.IChestOrDrive;
import appeng.api.storage.*;
import appeng.client.texture.ExtraBlockTextures;
import appeng.core.sync.GuiBridge;
import appeng.util.Platform;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class CustomCellHandler implements ICellHandler {
    public boolean isCell(ItemStack paramItemStack) {
        return CustomCellInventory.isCell(paramItemStack);
    }

    public IMEInventoryHandler getCellInventory(ItemStack paramItemStack, ISaveProvider paramISaveProvider, StorageChannel paramStorageChannel) {
        return (paramStorageChannel == StorageChannel.ITEMS) ? CustomCellInventory.getCell(paramItemStack, paramISaveProvider) : null;
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

    public void openChestGui(EntityPlayer paramEntityPlayer, IChestOrDrive paramIChestOrDrive, ICellHandler paramICellHandler, IMEInventoryHandler paramIMEInventoryHandler, ItemStack paramItemStack, StorageChannel paramStorageChannel) {
        Platform.openGUI(paramEntityPlayer, (TileEntity)paramIChestOrDrive, paramIChestOrDrive.getUp(), GuiBridge.GUI_ME);
    }

    public int getStatusForCell(ItemStack paramItemStack, IMEInventory paramIMEInventory) {
        if (paramIMEInventory instanceof CustomCellInventoryHandler) {
            CustomCellInventoryHandler modCellInventoryHandler = (CustomCellInventoryHandler)paramIMEInventory;
            return modCellInventoryHandler.getStatusForCell();
        }
        return 0;
    }

    public double cellIdleDrain(ItemStack paramItemStack, IMEInventory paramIMEInventory) {
        ICellInventory iCellInventory = ((ICellInventoryHandler)paramIMEInventory).getCellInv();
        return iCellInventory.getIdleDrain();
    }
}
