package foxiwhitee.hellmod.integration.avaritia.container;


import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.ITerminalHost;
import appeng.container.guisync.GuiSync;
import appeng.container.slot.*;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.container.slots.CustomSlotPatternOutputs;
import foxiwhitee.hellmod.container.slots.CustomSlotPatternTerm;
import foxiwhitee.hellmod.container.terminals.ContainerPatternTerminal;
import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import foxiwhitee.hellmod.integration.avaritia.helpers.BigPatternHelper;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.DefaultPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.lwjgl.Sys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContainerPartBigPatternTerminal extends ContainerPatternTerminal {

    private final SlotFakeCraftingMatrix[] craftingSlots = new SlotFakeCraftingMatrix[81];
    private final CustomSlotPatternTerm outputSlot;
    private final OptionalSlotFake[] outputSlots = new OptionalSlotFake[3];


    @GuiSync(97)
    public boolean craftingMode = true;

    @GuiSync(96)
    public boolean substitute = false;

    public ContainerPartBigPatternTerminal(InventoryPlayer ip, ITerminalHost host) {
        super(ip, host);
        int y;
        for(y = 0; y < 9; ++y) {
            for(int j = 0; j < 9; ++j) {
                //this.addSlotToContainer(this.craftingSlots[j + y * 9] = new SlotFakeCraftingMatrix(this.crafting, j + y * 9, 352 + j * 18, 26 + y * 18));
            }
        }

        this.addSlotToContainer(this.outputSlot = new CustomSlotPatternTerm(ip.player, this.getActionSource(), this.getPowerSource(), host, this.crafting, patternInv, this.getInventoryOut(), 424, 203, this, 2, this));
        this.outputSlot.setIIcon(-1);

        for(y = 0; y < 3; ++y) {
            this.addSlotToContainer(this.outputSlots[y] = new CustomSlotPatternOutputs(output, this, y, 406 + y * 18, 21, 0, 0, 1));
            this.outputSlots[y].setRenderDisabled(false);
        }

        this.updateOrderOfOutputSlots();

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

    public void updateOrderOfOutputSlots() {
        int y;
        if (!this.isCraftingMode()) {
            this.outputSlot.xDisplayPosition = -9000;

            for (y = 0; y < 3; ++y) {
                this.outputSlots[y].xDisplayPosition = this.outputSlots[y].getX();
                this.outputSlots[y].yDisplayPosition = this.outputSlots[y].getY() + 205;
                //((CustomSlotPatternOutputs) this.outputSlots[y]).display = true;
            }
        } else {
            this.outputSlot.xDisplayPosition = this.outputSlot.getX();

            for (y = 0; y < 3; ++y) {
                this.outputSlots[y].xDisplayPosition = -9000;
                //((CustomSlotPatternOutputs) this.outputSlots[y]).display = false;
            }
        }
        try {
            if (Platform.isServer()) {
                NetworkManager.instance.sendToAll(new DefaultPacket("BigPatternTerminal.CraftMode", isCraftingMode() ? "1" : "0"));
            }
        } catch (IOException ignore) {}
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
            OptionalSlotFake[] var3 = this.outputSlots;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                OptionalSlotFake outputSlot = var3[var5];
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
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (Platform.isServer()) {
            if (isCraftingMode() != getTerminal().isCraftingRecipe()) {
                setCraftingMode(getTerminal().isCraftingRecipe());
                updateOrderOfOutputSlots();
            }
            this.substitute = this.getTerminal().isSubstitution();
        }
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
        for (OptionalSlotFake optionalSlotFake : this.outputSlots)
            optionalSlotFake.putStack(null);
        detectAndSendChanges();
    }

    @Override
    protected Item getPattern() {
        return BotaniaIntegration.PURE_DAISY_PATTERN;
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
