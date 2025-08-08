package foxiwhitee.hellmod.integration.avaritia.container;


import appeng.api.storage.ITerminalHost;
import appeng.container.guisync.GuiSync;
import appeng.container.slot.OptionalSlotFake;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.container.slots.CustomSlotPatternTerm;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContainerPartBigProcessingPatternTerminal extends ContainerPatternTerminal {

    private final SlotFakeCraftingMatrix[] craftingSlots = new SlotFakeCraftingMatrix[81];
    private final SlotFakeCraftingMatrix[] outputSlots = new SlotFakeCraftingMatrix[3];

    public ContainerPartBigProcessingPatternTerminal(InventoryPlayer ip, ITerminalHost host) {
        super(ip, host);
        int y;
        for(y = 0; y < 9; ++y) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(this.craftingSlots[j + y * 9] = new SlotFakeCraftingMatrix(this.crafting, j + y * 9, 352 + j * 18, 26 + y * 18));
            }
        }

        for(y = 0; y < 3; ++y) {
            this.addSlotToContainer(this.outputSlots[y] = new SlotFakeCraftingMatrix(output, y, 406 + y * 18, 203));
        }

    }

    public boolean isSlotEnabled(int idx) {
        if (idx == 1) {
            return Platform.isServer() ? !this.getTerminal().isCraftingRecipe() : !this.isCraftingMode();
        } else if (idx == 2) {
            return Platform.isServer() ? this.getTerminal().isCraftingRecipe() : this.isCraftingMode();
        } else {
            return false;
        }
    }

    @Override
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
                        this.getPatternSlotOUT().putStack((ItemStack)null);
                    }

                    output = new ItemStack(getPattern(), 1);
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
                encodedValue.setBoolean("crafting", this.isCraftingMode());
                encodedValue.setBoolean("substitute", this.isSubstitute());
                output.setTagCompound(encodedValue);
            }
        }
    }

    @Override
    protected ItemStack[] getOutputs() {
        if (this.isCraftingMode()) {
            ItemStack out = this.getInventoryOut().getStackInSlot(0);
            if (out != null && out.stackSize > 0) {
                return new ItemStack[]{out};
            }
        } else {
            List list = new ArrayList(3);
            boolean hasValue = false;
            SlotFakeCraftingMatrix[] var3 = this.outputSlots;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                SlotFakeCraftingMatrix outputSlot = var3[var5];
                ItemStack out = outputSlot.getStack();
                if (out != null && out.stackSize > 0) {
                    list.add(out);
                    hasValue = true;
                }
            }

            if (hasValue) {
                return (ItemStack[])list.toArray(new ItemStack[list.size()]);
            }
        }

        return null;
    }

    @Override
    public void onSlotChange(Slot s) {
        if (s == this.getPatternSlotOUT() && Platform.isServer()) {
            ICrafting icrafting;
            label32:
            for(Iterator var2 = this.crafters.iterator(); var2.hasNext(); ((EntityPlayerMP)icrafting).isChangingQuantityOnly = false) {
                Object crafter = var2.next();
                icrafting = (ICrafting)crafter;
                Iterator var5 = this.inventorySlots.iterator();

                while(true) {
                    Object g;
                    do {
                        if (!var5.hasNext()) {
                            continue label32;
                        }

                        g = var5.next();
                    } while(!(g instanceof OptionalSlotFake) && !(g instanceof SlotFakeCraftingMatrix));

                    Slot sri = (Slot)g;
                    icrafting.sendSlotContents(this, sri.slotNumber, sri.getStack());
                }
            }

            this.detectAndSendChanges();
        }

    }

    @Override
    public void clear() {
        for (SlotFakeCraftingMatrix slotFakeCraftingMatrix : this.craftingSlots)
            slotFakeCraftingMatrix.putStack(null);
        for (SlotFakeCraftingMatrix optionalSlotFake : this.outputSlots)
            optionalSlotFake.putStack(null);
        detectAndSendChanges();
    }

    @Override
    protected Item getPattern() {
        return AvaritiaIntegration.BIG_PROCESSING_PATTERN;
    }

    @Override
    protected SlotFakeCraftingMatrix[] getInventoryCraftingSlots() {
        return this.craftingSlots;
    }

    public void toggleSubstitute() {
        this.substitute = !this.substitute;
        detectAndSendChanges();
    }

    public boolean isCraftingMode() {
        return this.craftingMode;
    }

    private void setCraftingMode(boolean craftingMode) {
        this.craftingMode = craftingMode;
    }

    private boolean isSubstitute() {
        return this.substitute;
    }

    public void setSubstitute(boolean substitute) {
        this.substitute = substitute;
    }

}
