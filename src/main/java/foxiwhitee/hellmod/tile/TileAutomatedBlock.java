package foxiwhitee.hellmod.tile;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingMedium;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.data.IAEItemStack;
import appeng.me.GridAccessException;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.recipes.BaseAutoBlockRecipe;
import foxiwhitee.hellmod.utils.craft.ICraftingCPUClusterAccessor;
import foxiwhitee.hellmod.utils.craft.IPreCraftingMedium;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TileAutomatedBlock extends TileNetwork implements IPreCraftingMedium {
    private List<ICraftingPatternDetails> patternList = new ArrayList<>();
    private ICraftingPatternDetails activePattern;
    private long craftCount;
    private InventoryCrafting craftingGrid;

    public void provideCrafting(ICraftingProviderHelper iCraftingProviderHelper) {
        if (getRecipes() != null) {
            for (BaseAutoBlockRecipe recipe : getRecipes()) {
                ICraftingPatternDetails details = new InternalPattern(recipe);
                patternList.add(details);
                iCraftingProviderHelper.addCraftingOption((ICraftingMedium) this, details);
            }
        }
    }

    public TileAutomatedBlock() {
        getProxy().setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    protected abstract ItemStack getItemFromTile(Object obj);

    protected abstract List<BaseAutoBlockRecipe> getRecipes();

    protected abstract long getMaxCount();

    public boolean pushPattern(ICraftingPatternDetails iCraftingPatternDetails, InventoryCrafting inventoryCrafting) {
        return false;
    }

    public boolean pushPattern(ICraftingPatternDetails details, InventoryCrafting ic, CraftingCPUCluster cluster) {
        if (!(details instanceof InternalPattern))
            return false;
        if (patternList == null || !patternList.contains(details) || details.isCraftable()) {
            return false;
        }
        ICraftingCPUClusterAccessor accessor = (ICraftingCPUClusterAccessor)((Object) cluster);
        long required = accessor.getWaitingFor(details) - 1;
        long actualRequired = required + 1;
        required = Math.min(required, getMaxCount());
        IMEInventory<IAEItemStack> inventory = cluster.getInventory();
        for (IAEItemStack input : details.getCondensedInputs()) {
            IAEItemStack copy = input.copy();
            copy.setStackSize(copy.getStackSize() * required);
            IAEItemStack extracted = inventory.extractItems(copy, Actionable.SIMULATE, cluster.getActionSource());
            long available = extracted == null ? 0 : extracted.getStackSize();
            if (copy.getStackSize() > available) {
                required = available / input.getStackSize();
            }
        }
        if (required < 0) return false;
        if (required >= 1) {
            for (IAEItemStack input : details.getCondensedInputs()) {
                IAEItemStack copy = input.copy();
                copy.setStackSize(copy.getStackSize() * required);
                inventory.extractItems(copy, Actionable.MODULATE, cluster.getActionSource());
            }
            for (IAEItemStack output : details.getCondensedOutputs()) {
                IAEItemStack copy = output.copy();
                copy.setStackSize(copy.getStackSize() * required);
                accessor.callPostChange(copy, cluster.getActionSource());
                accessor.getWaitingFor().add(copy.copy());
                accessor.callPostCraftingStatusChange(copy.copy());
            }
            accessor.setWaitingFor(details, actualRequired - required);
        }
        activePattern = details;
        craftCount = required + 1;
        craftingGrid = ic;
        return true;
    }

    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(1, 1, false, false);
    }

    public TickRateModulation tickingRequest(IGridNode iGridNode, int ticksSinceLastCall) {
        if (activePattern == null) return TickRateModulation.IDLE;
        List<IAEItemStack> outputs = new ArrayList<>();
        Arrays.stream(activePattern.getCondensedOutputs())
                .map(stack -> {
                    IAEItemStack copy = stack.copy();
                    copy.setStackSize(copy.getStackSize() * craftCount);
                    return copy;
                }).forEach(outputs::add);
        for (int i = 0; i < craftingGrid.getSizeInventory(); i++) {
            ItemStack item = Platform.getContainerItem(craftingGrid.getStackInSlot(i));
            if (item != null) {
                outputs.add(AEItemStack.create(item));
            }
        }
        boolean canCraft = true;
        for (IAEItemStack output : outputs) {
            try {
                IAEItemStack remainder = getProxy().getStorage().getItemInventory()
                        .injectItems(output.copy(), Actionable.SIMULATE, new MachineSource(this));
                if (remainder != null && remainder.getStackSize() > 0) {
                    canCraft = false;
                    break;
                }
            } catch (GridAccessException e) {
                canCraft = false;
                break;
            }
        }
        if (canCraft) {
            for (IAEItemStack output : outputs) {
                try {
                    getProxy().getStorage().getItemInventory()
                            .injectItems(output.copy(), Actionable.MODULATE, new MachineSource(this));
                } catch (GridAccessException ignored) {}
            }
            activePattern = null;
            craftCount = 0;
        }
        return TickRateModulation.IDLE;
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void write(NBTTagCompound compound) {
        compound.setBoolean("isBusy", this.isBusy);
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void read(NBTTagCompound compound) {
        if (compound.hasKey("isBusy"))
            this.isBusy = compound.getBoolean("isBusy");
    }

    public static class InternalPattern implements ICraftingPatternDetails {
        private final AEItemStack output;
        private final AEItemStack[] inputs;

        public InternalPattern(BaseAutoBlockRecipe recipe) {
            output = AEItemStack.create(recipe.getOut());
            inputs = new AEItemStack[recipe.getInputs().size()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = AEItemStack.create((ItemStack) recipe.getInputs().get(i));
            }
        }

        public ItemStack getPattern() {
            return null;
        }

        public boolean isValidItemForSlot(int i, ItemStack itemStack, World world) {
            return false;
        }

        public boolean isCraftable() {
            return false;
        }

        public IAEItemStack[] getInputs() {
            return inputs;
        }

        public IAEItemStack[] getCondensedInputs() {
            return inputs;
        }

        public IAEItemStack[] getCondensedOutputs() {
            return new IAEItemStack[] { this.output };
        }

        public IAEItemStack[] getOutputs() {
            return new IAEItemStack[] { this.output };
        }

        public boolean canSubstitute() {
            return false;
        }

        public ItemStack getOutput(InventoryCrafting inventoryCrafting, World world) {
            return this.output.getItemStack();
        }

        public int getPriority() {
            return 0;
        }

        public void setPriority(int i) {}
    }
}