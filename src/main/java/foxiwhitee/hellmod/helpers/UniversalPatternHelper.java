package foxiwhitee.hellmod.helpers;

import appeng.api.AEApi;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import appeng.container.ContainerNull;
import appeng.util.ItemSorters;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.integration.botania.helpers.*;
import foxiwhitee.hellmod.integration.thaumcraft.helpers.InfusionPatternHellper;
import foxiwhitee.hellmod.recipes.IHellRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.*;

public abstract class UniversalPatternHelper implements ICraftingPatternDetails, Comparable<UniversalPatternHelper> {
    private final InventoryCrafting crafting;
    private final InventoryCrafting testFrame;
    private final ItemStack patternItem;
    private final ItemStack correctOutput;
    protected final IHellRecipe standardRecipe;
    private final IAEItemStack[] condensedInputs;
    private final IAEItemStack[] condensedOutputs;
    private final IAEItemStack[] inputs;
    private final IAEItemStack[] outputs;
    private final boolean isCrafting = false;
    private final boolean canSubstitute = false;
    private final Set<UniversalPatternHelper.TestLookup> failCache = new HashSet();
    private final Set<UniversalPatternHelper.TestLookup> passCache = new HashSet();
    private final IAEItemStack pattern;
    private int priority = 0;


    public UniversalPatternHelper(ItemStack is, World w) {
        NBTTagCompound encodedValue = is.getTagCompound();
        if (encodedValue == null) {
            throw new IllegalArgumentException("No pattern here!");
        } else {
            crafting = new InventoryCrafting(new ContainerNull(), this.getWidthInventory(), this.getHeightInventory());
            testFrame = new InventoryCrafting(new ContainerNull(), this.getWidthInventory(), this.getHeightInventory());
            NBTTagList inTag = encodedValue.getTagList("in", 10);
            NBTTagList outTag = encodedValue.getTagList("out", 10);
            this.patternItem = is;
            this.pattern = AEItemStack.create(is);
            List<IAEItemStack> in = new ArrayList();
            List<IAEItemStack> out = new ArrayList();

            for(int x = 0; x < inTag.tagCount(); ++x) {
                ItemStack gs = ItemStack.loadItemStackFromNBT(inTag.getCompoundTagAt(x));
                this.crafting.setInventorySlotContents(x, gs);
                if (gs != null && (!this.isCrafting || !gs.hasTagCompound())) {
                    this.markItemAs(x, gs, TestStatus.ACCEPT);
                }

                in.add(AEApi.instance().storage().createItemStack(gs));
                this.testFrame.setInventorySlotContents(x, gs);
            }
            for(int x = 0; x < outTag.tagCount(); ++x) {
                ItemStack gs = ItemStack.loadItemStackFromNBT(outTag.getCompoundTagAt(x));
                if (gs != null) {
                    out.add(AEApi.instance().storage().createItemStack(gs));
                }
            }

            this.standardRecipe = findMatchingRecipeIn(this.crafting);
            correctOutput = standardRecipe.getOut();

            Objects.requireNonNull(out);
            this.outputs = (IAEItemStack[])out.toArray(new IAEItemStack[out.size()]);
            this.inputs = (IAEItemStack[])in.toArray(new IAEItemStack[in.size()]);
            Map<IAEItemStack, IAEItemStack> tmpOutputs = new HashMap();

            for(IAEItemStack io : this.outputs) {
                if (io != null) {
                    IAEItemStack g = (IAEItemStack)tmpOutputs.get(io);
                    if (g == null) {
                        tmpOutputs.put(io, io.copy());
                    } else {
                        g.add(io);
                    }
                }
            }

            Map<IAEItemStack, IAEItemStack> tmpInputs = new HashMap();

            for(IAEItemStack io : this.inputs) {
                if (io != null) {
                    IAEItemStack g = (IAEItemStack)tmpInputs.get(io);
                    if (g == null) {
                        tmpInputs.put(io, io.copy());
                    } else {
                        g.add(io);
                    }
                }
            }

            if (!tmpOutputs.isEmpty() && !tmpInputs.isEmpty()) {
                this.condensedInputs = new IAEItemStack[tmpInputs.size()];
                int offset = 0;

                for(IAEItemStack io : tmpInputs.values()) {
                    this.condensedInputs[offset] = io;
                    ++offset;
                }

                offset = 0;
                this.condensedOutputs = new IAEItemStack[tmpOutputs.size()];

                for(IAEItemStack io : tmpOutputs.values()) {
                    this.condensedOutputs[offset] = io;
                    ++offset;
                }

            } else {
                throw new IllegalStateException("No pattern here!");
            }
        }
    }

    private IHellRecipe findMatchingRecipeIn(InventoryCrafting matrix) {
        if(this instanceof ElvenTradePatternHelper) {
            return ElvenTradePatternHelper.findMatchingRecipe(matrix);
        }  else if(this instanceof InfusionPatternHellper) {
            return InfusionPatternHellper.findMatchingRecipe(matrix);
        } else if(this instanceof PetalsPatternHelper) {
            return PetalsPatternHelper.findMatchingRecipe(matrix);
        } else if(this instanceof PureDaisyPatternHelper) {
            return PureDaisyPatternHelper.findMatchingRecipe(matrix);
        } else if(this instanceof RuneAltalPatternHelper) {
            return RuneAltalPatternHelper.findMatchingRecipe(matrix);
        }
        return null;
    }

    private void markItemAs(int slotIndex, ItemStack i, TestStatus b) {
        if (b != TestStatus.TEST && !i.hasTagCompound()) {
            (b == TestStatus.ACCEPT ? this.passCache : this.failCache).add(new UniversalPatternHelper.TestLookup(slotIndex, i));
        }
    }

    public ItemStack getPattern() {
        return this.patternItem;
    }

    public synchronized boolean isValidItemForSlot(int slotIndex, ItemStack i, World w) {
        if (!this.isCrafting) {
            return false;
        } else {
            TestStatus result = this.getStatus(slotIndex, i);
            switch (result) {
                case ACCEPT:
                    return true;
                case DECLINE:
                    return false;
                case TEST:
                default:
                    for(int x = 0; x < this.crafting.getSizeInventory(); ++x) {
                        this.testFrame.setInventorySlotContents(x, this.crafting.getStackInSlot(x));
                    }

                    this.testFrame.setInventorySlotContents(slotIndex, i);
                    if (this.standardRecipe.matches(new ArrayList<>(Arrays.asList(this.testFrame.getStackInSlot(0))))) {
                        ItemStack testOutput = this.standardRecipe.getOut();
                        if (Platform.isSameItemPrecise(this.correctOutput, testOutput)) {
                            this.testFrame.setInventorySlotContents(slotIndex, this.crafting.getStackInSlot(slotIndex));
                            this.markItemAs(slotIndex, i, TestStatus.ACCEPT);
                            return true;
                        }
                    } else {
                        ItemStack testOutput = CraftingManager.getInstance().findMatchingRecipe(this.testFrame, w);
                        if (Platform.isSameItemPrecise(this.correctOutput, testOutput)) {
                            this.testFrame.setInventorySlotContents(slotIndex, this.crafting.getStackInSlot(slotIndex));
                            this.markItemAs(slotIndex, i, TestStatus.ACCEPT);
                            return true;
                        }
                    }

                    this.markItemAs(slotIndex, i, TestStatus.DECLINE);
                    return false;
            }
        }
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
        for(int x = 0; x < craftingInv.getSizeInventory(); ++x) {
            if (!this.isValidItemForSlot(x, craftingInv.getStackInSlot(x), w)) {
                return null;
            }
        }

        if (this.outputs != null && this.outputs.length > 0) {
            return this.outputs[0].getItemStack();
        } else {
            return null;
        }
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    private TestStatus getStatus(int slotIndex, ItemStack i) {
        if (this.crafting.getStackInSlot(slotIndex) == null) {
            return i == null ? TestStatus.ACCEPT : TestStatus.DECLINE;
        } else if (i == null) {
            return TestStatus.DECLINE;
        } else if (i.hasTagCompound()) {
            return TestStatus.TEST;
        } else if (this.passCache.contains(new UniversalPatternHelper.TestLookup(slotIndex, i))) {
            return TestStatus.ACCEPT;
        } else {
            return this.failCache.contains(new UniversalPatternHelper.TestLookup(slotIndex, i)) ? TestStatus.DECLINE : TestStatus.TEST;
        }
    }

    public int compareTo(UniversalPatternHelper o) {
        return ItemSorters.compareInt(o.priority, this.priority);
    }

    public int hashCode() {
        return this.pattern.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            UniversalPatternHelper other = (UniversalPatternHelper)obj;
            return this.pattern != null && other.pattern != null && this.pattern.equals(other.pattern);
        }
    }

    private static enum TestStatus {
        ACCEPT,
        DECLINE,
        TEST;

        private TestStatus() {
        }
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
            this.ref = dmg << 16 | Item.getIdFromItem(item) & '\uffff';
            int offset = 3 * slot;
            this.hash = this.ref << offset | this.ref >> offset + 32;
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            boolean equality;
            if (obj instanceof UniversalPatternHelper.TestLookup) {
                UniversalPatternHelper.TestLookup b = (UniversalPatternHelper.TestLookup)obj;
                equality = b.slot == this.slot && b.ref == this.ref;
            } else {
                equality = false;
            }

            return equality;
        }
    }

    abstract protected int getWidthInventory();

    abstract protected int getHeightInventory();

}


