package foxiwhitee.hellmod.client.gui.widgets;

import net.minecraft.item.ItemStack;

public interface IDropToFillTextField {
    boolean isOverTextField(int paramInt1, int paramInt2);

    void setTextFieldValue(String paramString, int paramInt1, int paramInt2, ItemStack paramItemStack);
}
