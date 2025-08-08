package foxiwhitee.hellmod.integration.botania.container;


import appeng.api.storage.ITerminalHost;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.container.slot.SlotRestrictedInput;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import foxiwhitee.hellmod.ModItems;
import foxiwhitee.hellmod.container.slots.CustomSlotPatternTerm;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerPartElvenTradePatternTerminal extends ContainerManaPatternTerminal {
    private final SlotFakeCraftingMatrix[] craftingSlots = new SlotFakeCraftingMatrix[3];
    private final CustomSlotPatternTerm outputSlot;

    public ContainerPartElvenTradePatternTerminal(InventoryPlayer ip, ITerminalHost host) {
        super(ip, host);

        this.addSlotToContainer(this.craftingSlots[0] = new SlotFakeCraftingMatrix(this.crafting, 0, 367, 105));
        this.addSlotToContainer(this.craftingSlots[1] = new SlotFakeCraftingMatrix(this.crafting, 1, 403, 105));
        this.addSlotToContainer(this.craftingSlots[2] = new SlotFakeCraftingMatrix(this.crafting, 2, 439, 105));

        this.addSlotToContainer(this.outputSlot = new CustomSlotPatternTerm(ip.player, this.getActionSource(), this.getPowerSource(), host, this.crafting, patternInv, this.getInventoryOut(), 403, 168, this, 1, this));
        this.outputSlot.setIIcon(-1);

    }

    @Override
    protected long getManaCost() {
        return 10000;
    }

    @Override
    protected Item getPattern() {
        return BotaniaIntegration.ELVEN_TRADE_PATTERN;
    }

    @Override
    protected SlotFakeCraftingMatrix[] getInventoryCraftingSlots() {
        return this.craftingSlots;
    }
}
