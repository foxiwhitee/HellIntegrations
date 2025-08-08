package foxiwhitee.hellmod.integration.botania.container;


import appeng.api.storage.ITerminalHost;
import appeng.container.slot.SlotDisabled;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.container.slot.SlotRestrictedInput;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.util.Platform;
import foxiwhitee.hellmod.container.slots.CustomSlotPatternTerm;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.helpers.ManaRecipeHelper;
import foxiwhitee.hellmod.integration.botania.parts.PartManaPoolPatternTerminal;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vazkii.botania.common.block.ModBlocks;

import java.io.IOException;

public class ContainerPartManaPoolPatternTerminal extends ContainerManaPatternTerminal {
    private final SlotFakeCraftingMatrix[] craftingSlots = new SlotFakeCraftingMatrix[1];
    private final Slot[] disabledSlot = new Slot[1];
    private final CustomSlotPatternTerm outputSlot;
    private static final ItemStack[] stacks = new ItemStack[]{new ItemStack(ModBlocks.pool), new ItemStack(ModBlocks.alchemyCatalyst), new ItemStack(ModBlocks.conjurationCatalyst)};

    public ContainerPartManaPoolPatternTerminal(InventoryPlayer ip, ITerminalHost host) {
        super(ip, host);

        this.addSlotToContainer(this.craftingSlots[0] = new SlotFakeCraftingMatrix(this.crafting, 0, 403, 87));
        this.addSlotToContainer(this.disabledSlot[0] = new SlotDisabled(this.crafting, 1, 403, 128));
        this.disabledSlot[0].putStack(stacks[0].copy());

        this.addSlotToContainer(this.outputSlot = new CustomSlotPatternTerm(ip.player, this.getActionSource(), this.getPowerSource(), host, this.crafting, patternInv, this.getInventoryOut(), 403, 168, this, 1, this));
        this.outputSlot.setIIcon(-1);

    }

    @Override
    protected long getManaCost() {
        return ManaRecipeHelper.getManaCostManaPool(getInventoryCrafting().getStackInSlot(0), ((PartManaPoolPatternTerminal)getTerminal()).isConjuration(), ((PartManaPoolPatternTerminal)getTerminal()).isAlchemy());
    }

    public ItemStack slotClick(int slotIndex, int dragType, int clickType, EntityPlayer player) {
        if (slotIndex == 39) {
            Slot slot = slotIndex < this.inventorySlots.size() ? this.disabledSlot[0] : null;

            if (slot != null && slot.getHasStack()) {
                for (int i = 0; i < 3; ++i) {
                    if (stacks[i].getItem() == slot.getStack().getItem()) {
                        int nextIndex = (i + 1) % stacks.length;
                        slot.putStack(stacks[nextIndex]);
                        this.detectAndSendChanges();
                        return super.slotClick(slotIndex, dragType, clickType, player);
                    }
                }
            }
        }

        return super.slotClick(slotIndex, dragType, clickType, player);
    }

    @Override
    protected Item getPattern() {
        return BotaniaIntegration.MANA_POOL_PATTERN;
    }

    @Override
    protected SlotFakeCraftingMatrix[] getInventoryCraftingSlots() {
        return this.craftingSlots;
    }

}
