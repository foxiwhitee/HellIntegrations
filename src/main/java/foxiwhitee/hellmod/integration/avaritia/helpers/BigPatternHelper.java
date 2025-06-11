package foxiwhitee.hellmod.integration.avaritia.helpers;

import appeng.api.AEApi;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import appeng.container.ContainerNull;
import appeng.util.ItemSorters;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import foxiwhitee.hellmod.utils.craft.IExtendedPatternDetails;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class BigPatternHelper implements ICraftingPatternDetails, Comparable<BigPatternHelper>, IExtendedPatternDetails {
    private final ItemStack patternItem;

    private final InventoryCrafting crafting = new InventoryCrafting((Container)new ContainerNull(), 9, 9);

    private final InventoryCrafting testFrame = new InventoryCrafting((Container)new ContainerNull(), 9, 9);

    private final ItemStack correctOutput;

    private final IRecipe standardRecipe;

    private final IAEItemStack[] condensedInputs;

    private final IAEItemStack[] condensedOutputs;

    private final IAEItemStack[] inputs;

    private final IAEItemStack[] outputs;

    private final boolean isCrafting;

    private final boolean canSubstitute;

    private final Set<TestLookup> failCache = new HashSet<>();

    private final Set<TestLookup> passCache = new HashSet<>();

    private final IAEItemStack pattern;

    private int priority = 0;

    public BigPatternHelper(ItemStack is, World w) {
        NBTTagCompound encodedValue = is.getTagCompound();
        if (encodedValue == null)
            throw new IllegalArgumentException("No pattern here!");
        NBTTagList inTag = encodedValue.getTagList("in", 10);
        NBTTagList outTag = encodedValue.getTagList("out", 10);
        this.isCrafting = encodedValue.getBoolean("crafting");
        this.canSubstitute = encodedValue.getBoolean("substitute");
        this.patternItem = is;
        this.pattern = (IAEItemStack)AEItemStack.create(is);
        List<IAEItemStack> in = new ArrayList<>();
        List<IAEItemStack> out = new ArrayList<>();
        List<IAEItemStack> containerOut = new ArrayList<>();
        int x;
        for (x = 0; x < inTag.tagCount(); x++) {
            ItemStack gs = ItemStack.loadItemStackFromNBT(inTag.getCompoundTagAt(x));
            this.crafting.setInventorySlotContents(x, gs);
            if (gs != null && (!this.isCrafting || !gs.hasTagCompound()))
                markItemAs(x, gs, TestStatus.ACCEPT);
            in.add(AEApi.instance().storage().createItemStack(gs));
            if (gs != null && gs.getItem().hasContainerItem(gs))
                containerOut.add(AEItemStack.create(gs.getItem().getContainerItem(gs)));
            this.testFrame.setInventorySlotContents(x, gs);
        }
        if (this.isCrafting) {
            this.standardRecipe = findMatchingRecipe(this.crafting, w);
            if (this.standardRecipe != null) {
                this.correctOutput = this.standardRecipe.getCraftingResult(this.crafting);
                out.add(AEApi.instance().storage().createItemStack(this.correctOutput));
            } else {
                throw new IllegalStateException("No pattern here!");
            }
        } else {
            this.standardRecipe = null;
            this.correctOutput = null;
            for (x = 0; x < outTag.tagCount(); x++) {
                ItemStack gs = ItemStack.loadItemStackFromNBT(outTag.getCompoundTagAt(x));
                if (gs != null)
                    out.add(AEApi.instance().storage().createItemStack(gs));
            }
        }
        Objects.requireNonNull(out);
        containerOut.stream().filter(item -> (item.getStackSize() > 0L)).forEach(out::add);
        this.outputs = out.<IAEItemStack>toArray(new IAEItemStack[out.size()]);
        this.inputs = in.<IAEItemStack>toArray(new IAEItemStack[in.size()]);
        Map<IAEItemStack, IAEItemStack> tmpOutputs = new HashMap<>();
        for (IAEItemStack io : this.outputs) {
            if (io != null) {
                IAEItemStack g = tmpOutputs.get(io);
                if (g == null) {
                    tmpOutputs.put(io, io.copy());
                } else {
                    g.add(io);
                }
            }
        }
        Map<IAEItemStack, IAEItemStack> tmpInputs = new HashMap<>();
        for (IAEItemStack io : this.inputs) {
            if (io != null) {
                IAEItemStack g = tmpInputs.get(io);
                if (g == null) {
                    tmpInputs.put(io, io.copy());
                } else {
                    g.add(io);
                }
            }
        }
        if (tmpOutputs.isEmpty() || tmpInputs.isEmpty())
            throw new IllegalStateException("No pattern here!");
        this.condensedInputs = new IAEItemStack[tmpInputs.size()];
        int offset = 0;
        for (IAEItemStack io : tmpInputs.values()) {
            this.condensedInputs[offset] = io;
            offset++;
        }
        offset = 0;
        this.condensedOutputs = new IAEItemStack[tmpOutputs.size()];
        for (IAEItemStack io : tmpOutputs.values()) {
            this.condensedOutputs[offset] = io;
            offset++;
        }
    }

    public static IRecipe findMatchingRecipe(InventoryCrafting matrix, World world) {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        int j;
        for (j = 0; j < matrix.getSizeInventory(); j++) {
            ItemStack itemstack2 = matrix.getStackInSlot(j);
            if (itemstack2 != null) {
                if (i == 0)
                    itemstack = itemstack2;
                if (i == 1)
                    itemstack1 = itemstack2;
                i++;
            }
        }
        if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && itemstack.getItem()
                .isRepairable())
            return null;
        for (j = 0; j < ExtremeCraftingManager.getInstance().getRecipeList().size(); j++) {
            IRecipe irecipe = (IRecipe) ExtremeCraftingManager.getInstance().getRecipeList().get(j);
            if (irecipe.matches(matrix, world))
                return irecipe;
        }
        return null;
    }

    private void markItemAs(int slotIndex, ItemStack i, TestStatus b) {
        if (b == TestStatus.TEST || i.hasTagCompound())
            return;
        ((b == TestStatus.ACCEPT) ? this.passCache : this.failCache).add(new TestLookup(slotIndex, i));
    }

    public ItemStack getPattern() {
        return this.patternItem;
    }

    public synchronized boolean isValidItemForSlot(int slotIndex, ItemStack i, World w) {
        if (!this.isCrafting)
            return false;
        TestStatus result = getStatus(slotIndex, i);
        switch (result) {
            case ACCEPT:
                return true;
            case DECLINE:
                return false;
        }
        for (int x = 0; x < this.crafting.getSizeInventory(); x++)
            this.testFrame.setInventorySlotContents(x, this.crafting.getStackInSlot(x));
        this.testFrame.setInventorySlotContents(slotIndex, i);
        if (this.standardRecipe.matches(this.testFrame, w)) {
            ItemStack testOutput = this.standardRecipe.getCraftingResult(this.testFrame);
            if (Platform.isSameItemPrecise(this.correctOutput, testOutput)) {
                this.testFrame.setInventorySlotContents(slotIndex, this.crafting.getStackInSlot(slotIndex));
                markItemAs(slotIndex, i, TestStatus.ACCEPT);
                return true;
            }
        } else {
            ItemStack testOutput = CraftingManager.getInstance().findMatchingRecipe(this.testFrame, w);
            if (Platform.isSameItemPrecise(this.correctOutput, testOutput)) {
                this.testFrame.setInventorySlotContents(slotIndex, this.crafting.getStackInSlot(slotIndex));
                markItemAs(slotIndex, i, TestStatus.ACCEPT);
                return true;
            }
        }
        markItemAs(slotIndex, i, TestStatus.DECLINE);
        return false;
    }

    public boolean isCraftable() {
        return false;
    }

    public IAEItemStack[] getInputs() {
        return this.inputs;
    }

    public IAEItemStack[] getCondensedInputs() {
        return this.condensedInputs;
    }

    public IAEItemStack[] getCondensedOutputs() {
        return this.condensedOutputs;
    }

    public IAEItemStack[] getOutputs() {
        return this.outputs;
    }

    public boolean canSubstitute() {
        return this.canSubstitute;
    }

    public ItemStack getOutput(InventoryCrafting craftingInv, World w) {
        for (int x = 0; x < craftingInv.getSizeInventory(); x++) {
            if (!isValidItemForSlot(x, craftingInv.getStackInSlot(x), w))
                return null;
        }
        if (this.outputs != null && this.outputs.length > 0)
            return this.outputs[0].getItemStack();
        return null;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    private TestStatus getStatus(int slotIndex, ItemStack i) {
        if (this.crafting.getStackInSlot(slotIndex) == null)
            return (i == null) ? TestStatus.ACCEPT : TestStatus.DECLINE;
        if (i == null)
            return TestStatus.DECLINE;
        if (i.hasTagCompound())
            return TestStatus.TEST;
        if (this.passCache.contains(new TestLookup(slotIndex, i)))
            return TestStatus.ACCEPT;
        if (this.failCache.contains(new TestLookup(slotIndex, i)))
            return TestStatus.DECLINE;
        return TestStatus.TEST;
    }

    public int compareTo(BigPatternHelper o) {
        return ItemSorters.compareInt(o.priority, this.priority);
    }

    public int hashCode() {
        return this.pattern.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BigPatternHelper other = (BigPatternHelper)obj;
        if (this.pattern != null && other.pattern != null)
            return this.pattern.equals(other.pattern);
        return false;
    }

    public int getHeight() {
        return 9;
    }

    public int getWidth() {
        return 9;
    }

    private enum TestStatus {
        ACCEPT, DECLINE, TEST;
    }

    private static final class TestLookup {
        private final int slot;

        private final int ref;

        private final int hash;

        public TestLookup(int slot, ItemStack i) {
            this(slot, i.getItem(), i.getItemDamage());
        }

        public TestLookup(int slot, Item item, int dmg) {
            this.slot = slot;
            this.ref = dmg << 16 | Item.getIdFromItem(item) & 0xFFFF;
            int offset = 3 * slot;
            this.hash = this.ref << offset | this.ref >> offset + 32;
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            boolean equality;
            if (obj instanceof TestLookup) {
                TestLookup b = (TestLookup)obj;
                equality = (b.slot == this.slot && b.ref == this.ref);
            } else {
                equality = false;
            }
            return equality;
        }
    }
}

