package foxiwhitee.hellmod.integration.draconic.items;

import cofh.api.energy.IEnergyContainerItem;
import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.brandonscore.common.utills.Utills;
import com.brandon3055.draconicevolution.common.entity.EntityPersistentItem;
import com.brandon3055.draconicevolution.common.utills.IConfigurableItem;
import com.brandon3055.draconicevolution.common.utills.IHudDisplayItem;
import com.brandon3055.draconicevolution.common.utills.ItemConfigField;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;

import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RFItemBase extends Item implements IConfigurableItem, IHudDisplayItem, IEnergyContainerItem {
    protected long capacity = 0L;

    protected int maxReceive = 0;

    protected int maxExtract = 0;

    public RFItemBase() {
        setMaxStackSize(1);
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    public String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return (Entity)new EntityPersistentItem(world, location, itemstack);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public long getCapacity(ItemStack stack) {
        return this.capacity;
    }

    public int getMaxExtract(ItemStack stack) {
        return this.maxExtract;
    }

    public int getMaxReceive(ItemStack stack) {
        return this.maxReceive;
    }

    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        long energy = ItemNBTHelper.getLong(container, "Energy", 0L);
        int energyReceived = (int)Math.min(maxReceive, getMaxEnergyStoredL(container) - getEnergyStoredL(container));
        if (!simulate) {
            energy += energyReceived;
            ItemNBTHelper.setLong(container, "Energy", energy);
        }
        return energyReceived;
    }

    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        long energy = ItemNBTHelper.getLong(container, "Energy", 0L);
        int energyExtracted = (int)Math.min(maxExtract, getEnergyStoredL(container));
        if (!simulate) {
            energy -= energyExtracted;
            ItemNBTHelper.setLong(container, "Energy", energy);
        }
        return energyExtracted;
    }

    public int getEnergyStored(ItemStack container) {
        long delta = getMaxEnergyStoredL(container) - getEnergyStoredL(container);
        if (delta > 2147483647L)
            return 0;
        if (delta <= 0L)
            return (int)Math.min(2147483647L, getMaxEnergyStoredL(container));
        return (int)(Math.min(2147483647L, getMaxEnergyStoredL(container)) - delta);
    }

    public int getMaxEnergyStored(ItemStack container) {
        return (int)Math.min(2147483647L, getCapacity(container));
    }

    public long getEnergyStoredL(ItemStack is) {
        return ItemNBTHelper.getLong(is, "Energy", 0L);
    }

    public long getMaxEnergyStoredL(ItemStack is) {
        return getCapacity(is);
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return (getEnergyStored(stack) != getMaxEnergyStored(stack));
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0D - getEnergyStored(stack) / getMaxEnergyStored(stack);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(ItemNBTHelper.setLong(new ItemStack(item, 1, 0), "Energy", 0L));
        if (this.capacity > 0L)
            list.add(ItemNBTHelper.setLong(new ItemStack(item, 1, 0), "Energy", this.capacity));
    }

    public boolean getHasSubtypes() {
        return (this.capacity > 0L);
    }

    public List<ItemConfigField> getFields(ItemStack stack, int slot) {
        return new ArrayList<>();
    }

    public List<String> getDisplayData(ItemStack stack) {
        List<String> list = new ArrayList<>();
        if (hasProfiles()) {
            int preset = ItemNBTHelper.getInteger(stack, "ConfigProfile", 0);
            list.add(EnumChatFormatting.DARK_PURPLE + LocalizationUtils.localize("info.de.capacitorMode.txt") + ": " + ItemNBTHelper.getString(stack, "ProfileName" + preset, "Profile " + preset));
        }
        for (ItemConfigField field : getFields(stack, 0))
            list.add(field.getTooltipInfo());
        if (getCapacity(stack) > 0L)
            list.add(InfoHelper.ITC() + LocalizationUtils.localize("info.de.charge.txt") + ": " + InfoHelper.HITC() + Utills.formatNumber(getEnergyStored(stack)) + " / " + Utills.formatNumber(this.capacity));
        long t = 100000000000L;
        list.add("Test: " + Utills.formatNumber(t));
        return list;
    }

    public boolean hasProfiles() {
        return true;
    }
}