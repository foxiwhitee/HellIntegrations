package foxiwhitee.hellmod.integration.draconic.items.weapons;

import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.brandonscore.common.utills.Utills;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.common.entity.EntityPersistentItem;
import com.brandon3055.draconicevolution.common.handler.ConfigHandler;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.ToolBase;
import com.brandon3055.draconicevolution.common.items.weapons.BowHandler;
import com.brandon3055.draconicevolution.common.items.weapons.IEnergyContainerWeaponItem;
import com.brandon3055.draconicevolution.common.utills.IHudDisplayItem;
import com.brandon3055.draconicevolution.common.utills.IInventoryTool;
import com.brandon3055.draconicevolution.common.utills.ItemConfigField;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ChaoticBow extends ItemBow implements IInventoryTool, IEnergyContainerWeaponItem, IHudDisplayItem, ICustomUpgradableItem {
    public static final String[] bowPullIconNameArray = new String[] { "pulling_0", "pulling_1", "pulling_2" };

    protected int capacity;

    protected int maxReceive;

    protected int maxExtract;

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    private String name;

    public ChaoticBow(String name) {
        this.name = name;
        this.capacity = HellConfig.chaoticWeaponsBaseStorage;
        this.maxReceive = HellConfig.chaoticWeaponsMaxTransfer;
        this.maxExtract = HellConfig.chaoticWeaponsMaxTransfer;
        this.maxStackSize = 1;
        setMaxDamage(-1);
        setUnlocalizedName(name);
    }

    public boolean isItemTool(ItemStack p_77616_1_) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(HellCore.MODID + ":" + name  + "_standby");
        this.iconArray = new IIcon[bowPullIconNameArray.length];
        for (int i = 0; i < this.iconArray.length; i++)
            this.iconArray[i] = iconRegister.registerIcon(HellCore.MODID + ":" + name + "_" + bowPullIconNameArray[i]);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        float j = stack.getMaxItemUseDuration() - useRemaining;
        if (usingItem == null)
            return this.itemIcon;
        BowHandler.BowProperties properties = new BowHandler.BowProperties(stack, player);
        if (j > properties.getDrawTicks())
            j = properties.getDrawTicks();
        j /= properties.getDrawTicks();
        int j2 = (int)(j * 2.0F);
        if (j2 < 0) {
            j2 = 0;
        } else if (j2 > 2) {
            j2 = 2;
        }
        return getItemIconForUseDuration(j2);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getItemIconForUseDuration(int par1) {
        return this.iconArray[par1];
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extraInformation) {
        boolean show = InfoHelper.holdShiftForDetails(list);
        if (show) {
            int preset = ItemNBTHelper.getInteger(stack, "ConfigProfile", 0);
            list.add(EnumChatFormatting.DARK_PURPLE + LocalizationUtils.localize("info.de.capacitorMode.txt") + ": " + ItemNBTHelper.getString(stack, "ProfileName" + preset, "Profile " + preset));
            List<ItemConfigField> l = getFields(stack, 0);
            Iterator<ItemConfigField> i$ = l.iterator();
            while (i$.hasNext()) {
                ItemConfigField f = i$.next();
                list.add(f.getTooltipInfo());
            }
        }
        ToolBase.holdCTRLForUpgrades(list, stack);
        InfoHelper.addEnergyInfo(stack, list);
        if (show && !ConfigHandler.disableLore)
            InfoHelper.addLore(stack, list, true);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", 0));
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", this.capacity));
    }

    public boolean getHasSubtypes() {
        return true;
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return (getEnergyStored(stack) != getMaxEnergyStored(stack));
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0D - getEnergyStored(stack) / getMaxEnergyStored(stack);
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return (Entity)new EntityPersistentItem(world, location, itemstack);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        return BowHandler.onBowRightClick((Item)this, stack, world, player);
    }

    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        BowHandler.onBowUsingTick(stack, player, count);
    }

    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int count) {
        BowHandler.onPlayerStoppedUsingBow(stack, world, player, count);
    }

    public String getInventoryName() {
        return LocalizationUtils.localize("info.de.toolInventoryEnch.txt");
    }

    public int getInventorySlots() {
        return 0;
    }

    public boolean isEnchantValid(Enchantment enchant) {
        return (enchant.type == EnumEnchantmentType.bow || enchant.effectId == DraconicEvolution.reaperEnchant.effectId);
    }

    public List<ItemConfigField> getFields(ItemStack stack, int slot) {
        List<ItemConfigField> list = new ArrayList<>();
        list.add((new ItemConfigField(4, slot, "BowArrowDamage")).setMinMaxAndIncromente(Float.valueOf(getBaseUpgradePoints(ICustomUpgradableItem.EnumUpgrade.ARROW_DAMAGE.index)), Float.valueOf(ICustomUpgradableItem.EnumUpgrade.ARROW_DAMAGE.getUpgradePoints(stack)), Float.valueOf(0.1F)).readFromItem(stack, Float.valueOf(ICustomUpgradableItem.EnumUpgrade.ARROW_DAMAGE.getUpgradePoints(stack))));
        list.add((new ItemConfigField(4, slot, "BowArrowSpeedModifier")).setMinMaxAndIncromente(Float.valueOf(0.0F), Float.valueOf(ICustomUpgradableItem.EnumUpgrade.ARROW_SPEED.getUpgradePoints(stack)), Float.valueOf(0.01F)).readFromItem(stack, Float.valueOf(0.0F)).setModifier("PLUSPERCENT"));
        list.add((new ItemConfigField(6, slot, "BowAutoFire")).readFromItem(stack, Boolean.valueOf(false)));
        list.add((new ItemConfigField(4, slot, "BowExplosionPower")).setMinMaxAndIncromente(Float.valueOf(0.0F), Float.valueOf(6.0F), Float.valueOf(0.1F)).readFromItem(stack, Float.valueOf(0.0F)));
        list.add((new ItemConfigField(4, slot, "BowShockWavePower")).setMinMaxAndIncromente(Float.valueOf(0.0F), Float.valueOf(10.0F), Float.valueOf(0.1F)).readFromItem(stack, Float.valueOf(0.0F)));
        list.add((new ItemConfigField(6, slot, "BowEnergyBolt")).readFromItem(stack, Boolean.valueOf(false)));
        list.add((new ItemConfigField(4, slot, "BowZoomModifier")).setMinMaxAndIncromente(Float.valueOf(0.0F), Float.valueOf(6.0F), Float.valueOf(0.01F)).readFromItem(stack, Float.valueOf(0.0F)).setModifier("PLUSPERCENT"));
        return list;
    }

    public List<ICustomUpgradableItem.EnumUpgrade> getCUpgrades(ItemStack itemstack) {
        return new ArrayList<ICustomUpgradableItem.EnumUpgrade>() {
            {
                this.add(ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY);
                this.add(ICustomUpgradableItem.EnumUpgrade.DRAW_SPEED);
                this.add(ICustomUpgradableItem.EnumUpgrade.ARROW_SPEED);
                this.add(ICustomUpgradableItem.EnumUpgrade.ARROW_DAMAGE);
            }
        };
    }


    public int getUpgradeCap(ItemStack itemstack) {
        return HellConfig.chaoticBowMaxUpgrades;
    }

    public int getMaxTier(ItemStack itemstack) {
        return 3;
    }

    public int getMaxUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.index)
            return HellConfig.chaoticBowMaxCapacityUpgradePoints;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DRAW_SPEED.index)
            return 8;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ARROW_SPEED.index)
            return 20;
        return (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ARROW_DAMAGE.index) ? 30 : HellConfig.chaoticBowMaxUpgradePoints;
    }

    public int getMaxUpgradePoints(int upgradeIndex, ItemStack stack) {
        return getMaxUpgradePoints(upgradeIndex);
    }

    public int getBaseUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DRAW_SPEED.index)
            return 5;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ARROW_SPEED.index)
            return 5;
        return (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ARROW_DAMAGE.index) ? 6 : 0;
    }

    public List<String> getUpgradeStats(ItemStack stack) {
        BowHandler.BowProperties properties = new BowHandler.BowProperties(stack, null);
        List<String> list = new ArrayList<>();
        list.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.RFCapacity.txt") + ": " + InfoHelper.HITC() + Utills.formatNumber(getMaxEnergyStored(stack)));
        list.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.max.txt") + " " + LocalizationUtils.localize("gui.de.ArrowSpeed.txt") + ": " + InfoHelper.HITC() + "+" + (ICustomUpgradableItem.EnumUpgrade.ARROW_SPEED.getUpgradePoints(stack) * 100) + "%");
        list.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.max.txt") + " " + LocalizationUtils.localize("gui.de.ArrowDamage.txt") + ": " + InfoHelper.HITC() + ICustomUpgradableItem.EnumUpgrade.ARROW_DAMAGE.getUpgradePoints(stack) + "");
        list.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.DrawSpeed.txt") + ": " + InfoHelper.HITC() + (properties.getDrawTicks() / 20.0D) + "s");
        return list;
    }

    public boolean hasProfiles() {
        return true;
    }

    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int energy = ItemNBTHelper.getInteger(container, "Energy", 0);
        int energyReceived = Math.min(getMaxEnergyStored(container) - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            energy += energyReceived;
            ItemNBTHelper.setInteger(container, "Energy", energy);
        }
        return energyReceived;
    }

    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        int energy = ItemNBTHelper.getInteger(container, "Energy", 0);
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            energy -= energyExtracted;
            ItemNBTHelper.setInteger(container, "Energy", energy);
        }
        return energyExtracted;
    }

    public int getEnergyStored(ItemStack container) {
        return ItemNBTHelper.getInteger(container, "Energy", 0);
    }

    public int getMaxEnergyStored(ItemStack stack) {
        int points = ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.getUpgradePoints(stack);
        return HellConfig.chaoticWeaponsBaseStorage + points * HellConfig.chaoticWeaponsStoragePerUpgrade;
    }

    public List<String> getDisplayData(ItemStack stack) {
        List<String> list = new ArrayList<>();
        if (BrandonsCore.proxy.getClientPlayer() != null && BrandonsCore.proxy.getClientPlayer().getItemInUse() != null && BrandonsCore.proxy.getClientPlayer().getItemInUseDuration() > 2) {
            EntityPlayer player = BrandonsCore.proxy.getClientPlayer();
            BowHandler.BowProperties properties = new BowHandler.BowProperties(stack, player);
            int power = (int)Math.min(player.getItemInUseDuration() / properties.getDrawTicks() * 100.0F, 100.0F);
            list.add(InfoHelper.ITC() + LocalizationUtils.localize("info.de.power.txt") + ": " + InfoHelper.HITC() + power + "%");
            return list;
        }
        int preset = ItemNBTHelper.getInteger(stack, "ConfigProfile", 0);
        list.add(EnumChatFormatting.DARK_PURPLE + LocalizationUtils.localize("info.de.capacitorMode.txt") + ": " + ItemNBTHelper.getString(stack, "ProfileName" + preset, "Profile " + preset));
        Iterator<ItemConfigField> i$ = getFields(stack, 0).iterator();
        while (true) {
            if (!i$.hasNext()) {
                list.add(InfoHelper.ITC() + LocalizationUtils.localize("info.de.charge.txt") + ": " + InfoHelper.HITC() + Utills.formatNumber(getEnergyStored(stack)) + " / " + Utills.formatNumber(getMaxEnergyStored(stack)));
                if (BrandonsCore.proxy.getClientPlayer() != null) {
                    BowHandler.BowProperties properties = new BowHandler.BowProperties(stack, BrandonsCore.proxy.getClientPlayer());
                    list.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.rfPerShot.txt") + ": " + InfoHelper.HITC() + Utills.addCommas(properties.calculateEnergyCost()));
                    if (!properties.canFire() && properties.cantFireMessage != null) {
                        list.add(EnumChatFormatting.DARK_RED + LocalizationUtils.localize(properties.cantFireMessage));
                        return list;
                    }
                }
                return list;
            }
            ItemConfigField field = i$.next();
            if ((field.datatype == 4 && ((Float)field.value).floatValue() > 0.0F) || (field.datatype == 6 && ((Boolean)field.value).booleanValue()))
                list.add(field.getTooltipInfo());
        }
    }

    public int getEnergyPerAttack() {
        return HellConfig.chaoticBowEnergyPerShot;
    }
}
