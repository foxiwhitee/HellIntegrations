package foxiwhitee.hellmod.integration.thaumcraft.container;

import appeng.api.storage.ITerminalHost;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.container.slot.SlotRestrictedInput;
import appeng.helpers.InventoryAction;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import foxiwhitee.hellmod.container.slots.CustomSlotPatternTerm;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.thaumcraft.helpers.InfusionPatternHellper;
import foxiwhitee.hellmod.integration.thaumcraft.ThaumcraftIntegration;
import foxiwhitee.hellmod.integration.thaumcraft.parts.PartInfusionPatternTerminal;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ContainerPartInfusionPatternTerminal extends ContainerPatternTerminal {

    private final SlotFakeCraftingMatrix[] craftingSlots = new SlotFakeCraftingMatrix[16];
    private final SlotFakeCraftingMatrix[] coreSlots = new SlotFakeCraftingMatrix[1];
    private final CustomSlotPatternTerm outputSlot;

    public ContainerPartInfusionPatternTerminal(InventoryPlayer ip, ITerminalHost host) {
        super(ip, host);

        this.addSlotToContainer(coreSlots[0] = new SlotFakeCraftingMatrix(((PartInfusionPatternTerminal)getTerminal()).getInventoryCore(), 0, 403, 87));

        for (int i = 0; i < 16; ++i) {
            SlotFakeCraftingMatrix slot;
            if (i == 0) {
                slot = new SlotFakeCraftingMatrix(this.crafting, i, 403, 50);
            } else {
                slot = new SlotFakeCraftingMatrix(this.crafting, i, -9000, -9000);
            }
            this.addSlotToContainer(slot);
            craftingSlots[i] = slot;
        }

        this.addSlotToContainer(this.outputSlot = new CustomSlotPatternTerm(ip.player, this.getActionSource(), this.getPowerSource(), host, this.getInventoryCrafting(), patternInv, this.getInventoryOut(), 403, 168, this, 1, this));
        this.outputSlot.setIIcon(-1);

    }

    public void doAction(EntityPlayerMP player, InventoryAction action, int slot, long id) {
        if (slot >= 0 && slot < this.inventorySlots.size()) {
            Slot s = this.getSlot(slot);
            if (s instanceof SlotFakeCraftingMatrix) {
                ItemStack hand = player != null ? player.inventory.getItemStack() : null;
                switch (action) {
                    case PICKUP_OR_SET_DOWN:
                        if (hand == null) {
                            s.putStack(this.geLastItemAndSetItToNull());
                        } else {
                            this.addLastStack(s.getStack());
                            ItemStack copy = hand.copy();
                            copy.stackSize = 1;
                            s.putStack(copy);
                        }
                        break;
                    case SHIFT_CLICK:
                        if (hand != null) {
                            ItemStack copy = hand.copy();
                            copy.stackSize = 1;
                            this.addLastStack(s.getStack());
                            s.putStack(copy);
                        }
                        break;
                }
            } else {
                super.doAction(player, action, slot, id);
            }
        } else {
            super.doAction(player, action, slot, id);
        }
    }

    private ItemStack geLastItemAndSetItToNull() {
        for (int i = craftingSlots.length - 1; i >= 1; --i) {
            if (craftingSlots[i].getHasStack()) {
                ItemStack stack = craftingSlots[i].getStack();
                if (i == 1) {
                    return null;
                }
                craftingSlots[i].putStack(null);
                return stack;
            }
        }
        craftingSlots[1].putStack(null);
        return null;
    }

    private void addLastStack(ItemStack stack) {
        if (stack != null) {
            for (int i = 2; i < craftingSlots.length; ++i) {
                if (!craftingSlots[i].getHasStack()) {
                    craftingSlots[i].putStack(stack);
                    return;
                }
            }
        }
    }

    @Override
    protected SlotFakeCraftingMatrix[] getInventoryCraftingSlots() {
        return craftingSlots;
    }

    @Override
    protected Item getPattern() {
        return ThaumcraftIntegration.INFUSION_PATTERN;
    }

}
