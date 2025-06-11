package foxiwhitee.hellmod.integration.botania.container;


import appeng.api.storage.ITerminalHost;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.helpers.InventoryAction;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.ModItems;
import foxiwhitee.hellmod.container.slots.CustomSlotPatternTerm;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.helpers.ManaRecipeHelper;
import foxiwhitee.hellmod.integration.botania.helpers.RuneAltalPatternHelper;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;

public class ContainerPartRuneAltarPatternTerminal extends ContainerManaPatternTerminal {

    private final SlotFakeCraftingMatrix[] craftingSlots = new SlotFakeCraftingMatrix[16];
    private final CustomSlotPatternTerm outputSlot;

    public ContainerPartRuneAltarPatternTerminal(InventoryPlayer ip, ITerminalHost host) {
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

    @Override
    public AEItemStack[] getInputs() {
        AEItemStack[] input = new AEItemStack[this.getInventoryCraftingSlots().length + 2];
        boolean hasValue = false;
        AEItemStack mana = AEItemStack.create(new ItemStack(BotaniaIntegration.mana_drop));
        mana.setStackSize(getManaCost());
        input[0] = mana;
        AEItemStack add = AEItemStack.create(new ItemStack(ModBlocks.livingrock));
        mana.setStackSize(getManaCost());
        input[1] = add;
        for(int x = 2; x <= this.getInventoryCraftingSlots().length + 1; ++x) {
            input[x] = AEItemStack.create(this.getInventoryCraftingSlots()[x].getStack());
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
    protected long getManaCost() {
        return ManaRecipeHelper.getManaCostRuneAltar(getInventoryCrafting());
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
    protected Item getPattern() {
        return BotaniaIntegration.RUNE_ALTAR_PATTERN;
    }

    @Override
    protected SlotFakeCraftingMatrix[] getInventoryCraftingSlots() {
        return this.craftingSlots;
    }

}
