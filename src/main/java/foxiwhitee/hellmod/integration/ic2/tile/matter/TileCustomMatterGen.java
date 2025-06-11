package foxiwhitee.hellmod.integration.ic2.tile.matter;

import ic2.api.recipe.*;
import ic2.core.BasicMachineRecipeManager;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import ic2.core.upgrade.IUpgradeItem;
import ic2.core.upgrade.UpgradableProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public abstract class TileCustomMatterGen extends TileEntityMatter {

    protected void updateEntityServer() {
        super.updateEntityServer();
        this.redstonePowered = false;
        boolean needsInvUpdate = false;

        for(int i = 0; i < this.upgradeSlot.size(); ++i) {
            ItemStack stack = this.upgradeSlot.get(i);
            if (stack != null && stack.getItem() instanceof IUpgradeItem && ((IUpgradeItem)stack.getItem()).onTick(stack, this)) {
                needsInvUpdate = true;
            }
        }

        if (!this.redstone.hasRedstoneInput() && !(this.energy <= (double)0.0F)) {
            MutableObject<ItemStack> output = new MutableObject();
            if (this.containerslot.transferFromTank(this.getFluidTank(), output, true) && (output.getValue() == null || this.outputSlot.canAdd((ItemStack)output.getValue()))) {
                this.containerslot.transferFromTank(this.getFluidTank(), output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add((ItemStack)output.getValue());
                }
            }

            if (needsInvUpdate) {
                this.markDirty();
            }
        } else {
            this.setActive(false);
        }

    }

    public static void init() {
        Recipes.matterAmplifier = (IMachineRecipeManager)new BasicMachineRecipeManager();
        addAmplifier(Ic2Items.scrap, 1, 5000); // 5000
        addAmplifier(Ic2Items.scrapBox, 1, 45000); //45000
    }

    public static void addAmplifier(ItemStack input, int amount, int amplification) {
        addAmplifier((IRecipeInput)new RecipeInputItemStack(input, amount), amplification);
    }

    public static void addAmplifier(String input, int amount, int amplification) {
        addAmplifier((IRecipeInput)new RecipeInputOreDict(input, amount), amplification);
    }

    public static void addAmplifier(IRecipeInput input, int amplification) {
        NBTTagCompound metadata = new NBTTagCompound();
        metadata.setInteger("amplification", amplification);
        Recipes.matterAmplifier.addRecipe(input, metadata, new ItemStack[0]);
    }

    private static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int)ret;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.getFluidTank().readFromNBT(nbttagcompound.getCompoundTag("fluidTank"));
        try {
            this.scrap = nbttagcompound.getInteger("scrap");
        } catch (Throwable var3) {
            this.scrap = nbttagcompound.getShort("scrap");
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagCompound fluidTankTag = new NBTTagCompound();
        this.getFluidTank().writeToNBT(fluidTankTag);
        nbttagcompound.setTag("fluidTank", fluidTankTag);
        nbttagcompound.setInteger("scrap", this.scrap);
    }

    public String getInventoryName() {
        return "Mass Fabricator";
    }

    public boolean attemptGeneration() {
        if (this.getFluidTank().getFluidAmount() + 1 > this.getFluidTank().getCapacity())
            return false;
        fill(null, new FluidStack(BlocksItems.getFluid(InternalName.fluidUuMatter), getMatter()), true);
        this.energy -= this.maxEnergy;
        return true;
    }

    public double getDemandedEnergy() {
        return this.redstone.hasRedstoneInput() ? 0.0D : (this.maxEnergy - this.energy);
    }

    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        if (this.energy < this.maxEnergy && !this.redstone.hasRedstoneInput()) {
            int bonus = Math.min((int)amount, this.scrap);
            this.scrap -= bonus;
            this.energy += amount + (5 * bonus);
            return 0.0D;
        }
        return amount;
    }

    public String getProgressAsString() {
        int p = Math.min((int)(this.energy * 100.0D / this.maxEnergy), 100);
        return "" + p + "%";
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public List<String> getNetworkedFields() {
        List<String> ret = new Vector<>(1);
        ret.add("state");
        ret.addAll(super.getNetworkedFields());
        return ret;
    }

    public float getWrenchDropRate() {
        return 0.7F;
    }

    public boolean amplificationIsAvailable() {
        if (this.scrap > 0)
            return true;
        RecipeOutput amplifier = this.amplifierSlot.process();
        return (amplifier != null && amplifier.metadata.getInteger("amplification") > 0);
    }

    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return (fluid == BlocksItems.getFluid(InternalName.fluidUuMatter));
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating())
            setUpgradestat();
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating())
            setUpgradestat();
    }

    public void setUpgradestat() {
        this.upgradeSlot.onChanged();
        setTier(applyModifier(this.defaultTier, this.upgradeSlot.extraTier, 1.0D));
    }

    public double getEnergy() {
        return this.energy;
    }

    public boolean useEnergy(double amount) {
        if (this.energy >= amount) {
            this.energy -= amount;
            return true;
        }
        return false;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.Transformer, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing);
    }

    public abstract String getName();

    public abstract int getMatter();

    public abstract FluidTank getFluidTank();
}
