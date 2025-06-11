package foxiwhitee.hellmod.utils.cells;

import appeng.api.storage.ICellWorkbenchItem;
import appeng.api.storage.data.IAEItemStack;
import net.minecraft.item.ItemStack;

public interface ICustomStorageCell extends ICellWorkbenchItem {
    int getTotalTypes(ItemStack var1);
    boolean isStorageCell(ItemStack var1);
    boolean storableInStorageCell();
    boolean isBlackListed(ItemStack var1, IAEItemStack var2);
    double getIdleDrain();
    int getBytesPerType(ItemStack var1);
    long getBytes(ItemStack var1);
}
