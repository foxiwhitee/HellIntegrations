package foxiwhitee.hellmod.integration.botania.container;


import appeng.api.storage.ITerminalHost;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.container.slot.SlotRestrictedInput;
import appeng.helpers.InventoryAction;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.container.slots.CustomSlotPatternTerm;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vazkii.botania.common.item.ModItems;

public class ContainerPartPetalsPatternTerminal extends ContainerPatternTerminal {

    private final SlotFakeCraftingMatrix[] craftingSlots = new SlotFakeCraftingMatrix[16];
    private final CustomSlotPatternTerm outputSlot;

    public ContainerPartPetalsPatternTerminal(InventoryPlayer ip, ITerminalHost host) {
        super(ip, host);
        for (int i = 0; i < 16; ++i) {
            SlotFakeCraftingMatrix slot;
            if (i == 0) {
                slot = new SlotFakeCraftingMatrix(this.crafting, i, 403, 53);
            } else {
                slot = new SlotFakeCraftingMatrix(this.crafting, i, -9000, -9000);
            }
            this.addSlotToContainer(slot);
            craftingSlots[i] = slot;
        }

        this.addSlotToContainer(this.outputSlot = new CustomSlotPatternTerm(ip.player, this.getActionSource(), this.getPowerSource(), host, this.crafting, patternInv, this.getInventoryOut(), 403, 168, this, 1, this));
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
        for (int i = craftingSlots.length - 1; i >= 0; --i) {
            if (craftingSlots[i].getHasStack()) {
                ItemStack stack = craftingSlots[i].getStack();
                if (i == 0) {
                    return null;
                }
                craftingSlots[i].putStack(null);
                return stack;
            }
        }
        craftingSlots[0].putStack(null);
        return null;
    }

    private void addLastStack(ItemStack stack) {
        if (stack != null) {
            for (int i = 1; i < craftingSlots.length; ++i) {
                if (!craftingSlots[i].getHasStack()) {
                    craftingSlots[i].putStack(stack);
                    return;
                }
            }
        }
    }

    @Override
    public AEItemStack[] getInputs() {
        AEItemStack[] input = new AEItemStack[this.getInventoryCraftingSlots().length + 1];
        boolean hasValue = false;
        AEItemStack add = AEItemStack.create(new ItemStack(Items.wheat_seeds));
        input[0] = add;
        for(int x = 1; x <= this.getInventoryCraftingSlots().length; ++x) {
            input[x] = AEItemStack.create(this.getInventoryCraftingSlots()[x - 1].getStack());
            if (input[x] != null) {
                hasValue = true;
            }
        }

        if (hasValue) {
            return input;
        } else {
            return null;
        }
    }

    @Override
    protected Item getPattern() {
        return BotaniaIntegration.PETALS_PATTERN;
    }

    @Override
    protected SlotFakeCraftingMatrix[] getInventoryCraftingSlots() {
        return this.craftingSlots;
    }

}
