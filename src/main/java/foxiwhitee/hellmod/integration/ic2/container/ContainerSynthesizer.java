package foxiwhitee.hellmod.integration.ic2.container;

import java.util.List;

import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotDisabled;
import appeng.container.slot.SlotPlayerHotBar;
import appeng.container.slot.SlotPlayerInv;
import foxiwhitee.hellmod.container.slots.CustomSlotRestrictedInput;
import foxiwhitee.hellmod.integration.ic2.tile.TileSynthesizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSynthesizer extends AEBaseContainer {
    private final TileSynthesizer tile;
    private final Slot[] playerSlots;

    public ContainerSynthesizer(EntityPlayer ip, TileSynthesizer tile) {
        super(ip.inventory, tile);
        this.tile = tile;
        this.playerSlots = new Slot[36];
        bindPlayerInventory(ip.inventory, 38, 152);
        int index = 0;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if ((y == 0 || y == 4) && (x == 0 || x == 4)) continue;
                addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.SOLAR_PANEL, tile.getInv(), index++, 80 + x * 19, 37 + y * 19, ip.inventory));
            }
        }
        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.SYNTHESIZER_UPGRADE, tile.getUpgradeInv(), 0, 10, 67, ip.inventory));
        addSlotToContainer(new CustomSlotRestrictedInput(CustomSlotRestrictedInput.PlacableItemType.SUN_UPGRADE, tile.getUpgradeFotonInv(), 0, 10, 86, ip.inventory));
    }

    @Override
    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int offsetX, int offsetY) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.playerSlots[j + i * 9 + 9] = this.addSlotToContainer(new SlotPlayerInv(inventoryPlayer, j + i * 9 + 9, 8 + j * 18 + offsetX, offsetY + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.playerSlots[i] = this.addSlotToContainer(new SlotPlayerHotBar(inventoryPlayer, i, 8 + i * 18 + offsetX, 58 + offsetY));
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        return tile.isUseableByPlayer(player);
    }

    public boolean clickWasInPlayerInventory(Slot slot) {
        return playerSlots[0] != null && slot != null && playerSlots[0].slotNumber <= slot.slotNumber && playerSlots[35] != null && slot.slotNumber <= playerSlots[35].slotNumber + 9;
    }

    public boolean mergeStackToPlayerInv(ItemStack stack) {
        return mergeItemStack(stack, playerSlots[0].slotNumber, playerSlots[35].slotNumber + 10, false);
    }

    public boolean mergeStackToTileInv(ItemStack stack) {
        return mergeItemStack(stack, playerSlots[35].slotNumber + 10, inventorySlots.size(), false);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        Slot slot = (Slot) inventorySlots.get(slotId);
        if (slot == null || !slot.getHasStack()) return null;
        ItemStack stack = slot.getStack();
        boolean didMerge = clickWasInPlayerInventory(slot) ? mergeStackToTileInv(stack) : mergeStackToPlayerInv(stack);
        if (didMerge) {
            if (stack.stackSize == 0) slot.putStack(null);
            slot.onSlotChanged();
            return slot.getStack();
        }
        return null;
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    public TileSynthesizer getTile() {
        return tile;
    }
}