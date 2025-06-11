package foxiwhitee.hellmod.integration.avaritia.container;

import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.ITerminalHost;
import appeng.container.ContainerNull;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.helpers.IContainerCraftingPacket;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.tile.inventory.InvOperation;
import foxiwhitee.hellmod.container.slots.SlotCraftingTermExtended;
import foxiwhitee.hellmod.container.terminals.ContainerTerminal;
import foxiwhitee.hellmod.integration.avaritia.helpers.BigPatternHelper;
import foxiwhitee.hellmod.integration.avaritia.parts.PartCraftingTerminal9x9;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ContainerPartCraftingTerminal9x9 extends ContainerTerminal {
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);

    private final SlotCraftingMatrix[] craftingSlots = new SlotCraftingMatrix[81];

    private final SlotCraftingTermExtended outputSlot;

    public ContainerPartCraftingTerminal9x9(InventoryPlayer ip, ITerminalHost monitorable) {
        super(ip, monitorable);
        IInventory crafting = this.getTerminal().getInventoryByName("crafting");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                addSlotToContainer((Slot)(this.craftingSlots[j + i * 9] = new SlotCraftingMatrix((Container)this, crafting, j + i * 9, 206 + j * 18, 18 + i * 18)));
        }
        addSlotToContainer((Slot)(this.outputSlot = new SlotCraftingTermExtended((getPlayerInv()).player, getActionSource(), getPowerSource(), (IStorageMonitorable)monitorable, crafting, crafting, (IInventory)this.output, 403, 168, this, 9, 9)));
        onCraftMatrixChanged(crafting);
    }

    public void onCraftMatrixChanged(IInventory par1IInventory) {
        ContainerNull cn = new ContainerNull();
        InventoryCrafting ic = new InventoryCrafting((Container)cn, 9, 9);
        for (int x = 0; x < ic.getSizeInventory(); x++)
            ic.setInventorySlotContents(x, this.craftingSlots[x].getStack());
        IRecipe recipe = null;//BigPatternHelper.findMatchingRecipe(ic, (getPlayerInv()).player.worldObj);
        ItemStack is = (recipe == null) ? null : recipe.getRecipeOutput();
        this.outputSlot.putStack(is);
    }

    public IInventory getInventoryByName(String name) {
        if (name.equals("player"))
            return (IInventory)getInventoryPlayer();
        return this.getTerminal().getInventoryByName(name);
    }

    public boolean useRealItems() {
        return true;
    }
}
