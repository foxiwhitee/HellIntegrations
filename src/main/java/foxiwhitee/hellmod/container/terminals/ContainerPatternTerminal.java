package foxiwhitee.hellmod.container.terminals;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.storage.ITerminalHost;
import appeng.container.slot.OptionalSlotFake;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.container.slot.SlotRestrictedInput;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.parts.PartPatternTerminal;
import foxiwhitee.hellmod.parts.PartTerminal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class ContainerPatternTerminal extends ContainerTerminal {
    private final SlotRestrictedInput patternSlotIN;
    private final SlotRestrictedInput patternSlotOUT;
    protected final IInventory crafting;
    protected final IInventory patternInv;
    protected final IInventory output;

    public ContainerPatternTerminal(InventoryPlayer ip, ITerminalHost host) {
        super(ip, host);
        this.patternInv = this.getTerminal().getInventoryByName("pattern");
        this.crafting = this.getTerminal().getInventoryByName("crafting");
        this.output = this.getTerminal().getInventoryByName("output");
        this.addSlotToContainer((Slot)(patternSlotIN = new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.BLANK_PATTERN, patternInv, 0, 242, 146, ip)));
        this.addSlotToContainer((Slot)(patternSlotOUT = new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.ENCODED_PATTERN, patternInv, 1, 242, 203, ip)));
        this.patternSlotOUT.setStackLimit(1);
    }

    public void encode() {
        ItemStack output = this.getPatternSlotOUT().getStack();
        AEItemStack[] in = this.getInputs();
        ItemStack[] out = this.getOutputs();
        if (in != null && out != null) {
            if (output == null || this.isPattern(output)) {
                if (output == null) {
                    output = this.getPatternSlotIN().getStack();
                    if (output == null || !this.isPattern(output)) {
                        return;
                    }

                    --output.stackSize;
                    if (output.stackSize == 0) {
                        this.getPatternSlotIN().putStack((ItemStack)null);
                    }

                    output = new ItemStack(this.getPattern(), 1);
                    this.getPatternSlotOUT().putStack(output);
                }

                NBTTagCompound encodedValue = new NBTTagCompound();
                NBTTagList tagIn = new NBTTagList();
                NBTTagList tagOut = new NBTTagList();

                for(AEItemStack i : in) {
                    tagIn.appendTag(this.createItemTag(i));
                }

                for(ItemStack i : out) {
                    tagOut.appendTag(this.createItemTag(i));
                }

                encodedValue.setTag("in", tagIn);
                encodedValue.setTag("out", tagOut);
                output.setTagCompound(encodedValue);
            }
        }
    }

    public AEItemStack[] getInputs() {
        AEItemStack[] input = new AEItemStack[this.getInventoryCraftingSlots().length];
        boolean hasValue = false;

        for(int x = 0; x < this.getInventoryCraftingSlots().length; ++x) {
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

    protected ItemStack[] getOutputs() {
        ItemStack out = this.getInventoryOut().getStackInSlot(0);
        if (out != null && out.stackSize > 0) {
            return new ItemStack[]{out};
        }
        return null;
    }

    protected boolean isPattern(ItemStack output) {
        if (output == null) {
            return false;
        } else {
            IDefinitions definitions = AEApi.instance().definitions();
            boolean isPattern = definitions.items().encodedPattern().isSameAs(output);
            isPattern |= definitions.materials().blankPattern().isSameAs(output);
            return isPattern;
        }
    }

    protected NBTBase createItemTag(ItemStack i) {
        NBTTagCompound c = new NBTTagCompound();
        if (i != null) {
            i.writeToNBT(c);
        }
        return c;
    }

    protected NBTBase createItemTag(AEItemStack i) {
        NBTTagCompound c = new NBTTagCompound();
        if (i != null) {
            i.writeToNBT(c);
        }
        return c;
    }

    public void onSlotChange(Slot s) {
        if (s == this.getPatternSlotOUT() && Platform.isServer()) {
            for(Object crafter : this.crafters) {
                ICrafting icrafting = (ICrafting)crafter;

                for(Object g : this.inventorySlots) {
                    if (g instanceof OptionalSlotFake || g instanceof SlotFakeCraftingMatrix) {
                        Slot sri = (Slot)g;
                        icrafting.sendSlotContents(this, sri.slotNumber, sri.getStack());
                    }
                }
                ((EntityPlayerMP)icrafting).isChangingQuantityOnly = false;
            }
        }
    }

    public void clear() {
        for(Slot s : this.getInventoryCraftingSlots()) {
            s.putStack((ItemStack)null);
        }
    }

    @Override
    public PartPatternTerminal getTerminal() {
        return (PartPatternTerminal) super.getTerminal();
    }

    public SlotRestrictedInput getPatternSlotIN() {
        return patternSlotIN;
    }

    public SlotRestrictedInput getPatternSlotOUT() {
        return patternSlotOUT;
    }

    abstract protected SlotFakeCraftingMatrix[] getInventoryCraftingSlots();


    protected IInventory getInventoryOut() {
        return output;
    }

    public IInventory getInventoryCrafting() {
        return crafting;
    }

    protected IInventory getInventoryPattern() {
        return patternInv;
    }

    abstract protected Item getPattern();

}
