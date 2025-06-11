package foxiwhitee.hellmod.container.slots;

import appeng.container.AEBaseContainer;
import appeng.container.slot.AppEngSlot;
import appeng.tile.inventory.AppEngInternalInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CustomAppEngSlot extends AppEngSlot {
    private final int defX;
    private final int defY;
    private boolean isDraggable = true;
    private boolean isPlayerSide = false;
    private AEBaseContainer myContainer = null;
    private int IIcon = -1;
    private hasCalculatedValidness isValid;
    private boolean isDisplay = false;

    public CustomAppEngSlot(IInventory inv, int idx, int x, int y) {
        super(inv, idx, x, y);
        this.defX = x;
        this.defY = y;
        this.setIsValid(hasCalculatedValidness.NotAvailable);
    }

    public Slot setNotDraggable() {
        this.setDraggable(false);
        return this;
    }

    public Slot setPlayerSide() {
        this.isPlayerSide = true;
        return this;
    }

    public String getTooltip() {
        return null;
    }

    public void clearStack() {
        super.putStack((ItemStack)null);
    }

    public boolean isItemValid(ItemStack par1ItemStack) {
        return this.isEnabled() ? super.isItemValid(par1ItemStack) : false;
    }

    public ItemStack getStack() {
        if (!this.isEnabled()) {
            return null;
        } else if (this.inventory.getSizeInventory() <= this.getSlotIndex()) {
            return null;
        } else if (this.isDisplay()) {
            this.setDisplay(false);
            return this.getDisplayStack();
        } else {
            return super.getStack();
        }
    }

    public void onSlotChanged() {
        if (this.inventory instanceof AppEngInternalInventory) {
            ((AppEngInternalInventory)this.inventory).markDirty(this.getSlotIndex());
        } else {
            super.onSlotChanged();
        }

        this.setIsValid(hasCalculatedValidness.NotAvailable);
    }

    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return this.isEnabled() ? super.canTakeStack(par1EntityPlayer) : false;
    }

    public boolean func_111238_b() {
        return this.isEnabled();
    }

    public ItemStack getDisplayStack() {
        return super.getStack();
    }

    public boolean isEnabled() {
        return true;
    }

    public float getOpacityOfIcon() {
        return 0.4F;
    }

    public boolean renderIconWithItem() {
        return false;
    }

    public int getIcon() {
        return this.getIIcon();
    }

    public boolean isPlayerSide() {
        return this.isPlayerSide;
    }

    public boolean shouldDisplay() {
        return this.isEnabled();
    }

    public int getX() {
        return this.defX;
    }

    public int getY() {
        return this.defY;
    }

    private int getIIcon() {
        return this.IIcon;
    }

    public void setIIcon(int iIcon) {
        this.IIcon = iIcon;
    }

    private boolean isDisplay() {
        return this.isDisplay;
    }

    public void setDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public boolean isDraggable() {
        return this.isDraggable;
    }

    private void setDraggable(boolean isDraggable) {
        this.isDraggable = isDraggable;
    }

    void setPlayerSide(boolean isPlayerSide) {
        this.isPlayerSide = isPlayerSide;
    }

    public hasCalculatedValidness getIsValid() {
        return this.isValid;
    }

    public void setIsValid(hasCalculatedValidness isValid) {
        this.isValid = isValid;
    }

    public AEBaseContainer getContainer() {
        return this.myContainer;
    }

    public void setContainer(AEBaseContainer myContainer) {
        this.myContainer = myContainer;
    }

}
