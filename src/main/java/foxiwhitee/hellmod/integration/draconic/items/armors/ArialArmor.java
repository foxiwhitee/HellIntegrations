package foxiwhitee.hellmod.integration.draconic.items.armors;

import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.brandonscore.common.utills.Utills;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.entity.EntityPersistentItem;
import com.brandon3055.draconicevolution.common.handler.ConfigHandler;
import com.brandon3055.draconicevolution.common.items.armor.ICustomArmor;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.ToolBase;
import com.brandon3055.draconicevolution.common.utills.IConfigurableItem;
import com.brandon3055.draconicevolution.common.utills.IInventoryTool;
import com.brandon3055.draconicevolution.common.utills.ItemConfigField;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.client.render.armor.ModelArialArmor;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.utils.helpers.EnergyUtility;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@InterfaceList({@Interface(iface = "thaumcraft.api.IGoggles", modid = "Thaumcraft"), @Interface(iface = "thaumcraft.api.nodes.IRevealer", modid = "Thaumcraft")})
public class ArialArmor extends ItemArmor implements ISpecialArmor, IConfigurableItem, IInventoryTool, IGoggles, IRevealer, ICustomArmor, ICustomUpgradableItem {
    @SideOnly(Side.CLIENT)
    private IIcon helmIcon;

    @SideOnly(Side.CLIENT)
    private IIcon chestIcon;

    @SideOnly(Side.CLIENT)
    private IIcon leggsIcon;

    @SideOnly(Side.CLIENT)
    private IIcon bootsIcon;

    private int maxEnergy;

    private int maxTransfer;

    @SideOnly(Side.CLIENT)
    public ModelBiped model;

    public ArialArmor(ArmorMaterial armorMaterial, int armorType, String name) {
        super(armorMaterial, 0, armorType);
        this.maxEnergy = HellConfig.arialArmorBaseStorage;
        this.maxTransfer = HellConfig.arialArmorMaxTransfer;
        setUnlocalizedName(name);
        setCreativeTab(DraconicEvolution.tabToolsWeapons);
    }

    public boolean isItemTool(ItemStack p_77616_1_) {
        return true;
    }

    public void getSubItems(Item item, CreativeTabs p_150895_2_, List list) {
        list.add(ItemNBTHelper.setInteger(new ItemStack(item), "Energy", 0));
        list.add(ItemNBTHelper.setInteger(new ItemStack(item), "Energy", this.maxEnergy));
    }

    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.helmIcon = iconRegister.registerIcon(HellCore.MODID + ":arial_helmet");
        this.chestIcon = iconRegister.registerIcon(HellCore.MODID + ":arial_chestplate");
        this.leggsIcon = iconRegister.registerIcon(HellCore.MODID + ":arial_leggings");
        this.bootsIcon = iconRegister.registerIcon(HellCore.MODID + ":arial_boots");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (stack.getItem() == DraconicEvolutionIntegration.arial_helm)
            return this.helmIcon;
        if (stack.getItem() == DraconicEvolutionIntegration.arial_chest)
            return this.chestIcon;
        return (stack.getItem() == DraconicEvolutionIntegration.arial_legs) ? this.leggsIcon : this.bootsIcon;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack) {
        if (stack.getItem() == DraconicEvolutionIntegration.arial_helm)
            return this.helmIcon;
        if (stack.getItem() == DraconicEvolutionIntegration.arial_chest)
            return this.chestIcon;
        return (stack.getItem() == DraconicEvolutionIntegration.arial_legs) ? this.leggsIcon : this.bootsIcon;
    }

    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (!ConfigHandler.useOldArmorModel)
            return HellCore.MODID + ":textures/models/armor/armorArial.png";
        return (stack.getItem() != DraconicEvolutionIntegration.arial_helm && stack.getItem() != DraconicEvolutionIntegration.arial_chest && stack.getItem() != DraconicEvolutionIntegration.arial_boots) ? HellCore.MODID + ":textures/models/armor/arial_layer_2.png" : "draconicadditions:textures/models/armor/arial_layer_1.png";
    }

    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return EnumRarity.epic;
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0D - ItemNBTHelper.getInteger(stack, "Energy", 0) / getMaxEnergyStored(stack);
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return (getEnergyStored(stack) < getMaxEnergyStored(stack));
    }

    protected float getProtectionShare() {
        switch (this.armorType) {
            case 0:
                return 1.5F;
            case 1:
                return 5.0F;
            case 2:
                return 4.3F;
            case 3:
                return 1.5F;
        }
        return 0.0F;
    }

    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return (!source.isUnblockable() && !source.isDamageAbsolute() && !source.isMagicDamage()) ? new ArmorProperties(0, this.damageReduceAmount / 24.5D, 1000) : new ArmorProperties(0, this.damageReduceAmount / 100.0D, 15);
    }

    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int)(getProtectionShare() * 20.0D);
    }

    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {}

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (stack != null) {
            if (stack.getItem() == DraconicEvolutionIntegration.arial_helm) {
                if (world.isRemote)
                    return;
                if (getEnergyStored(stack) >= HellConfig.arialArmorEnergyToRemoveEffects && clearNegativeEffects((Entity)player))
                    extractEnergy(stack, HellConfig.arialArmorEnergyToRemoveEffects, false);
                if (player.worldObj.getBlockLightValue((int)Math.floor(player.posX), (int)player.posY + 1, (int)Math.floor(player.posZ)) < 5 && ProfileHelper.getBoolean(stack, "ArmorNVActive", false)) {
                    player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 419, 0, true));
                } else if (ProfileHelper.getBoolean(stack, "ArmorNVActive", false) && ProfileHelper.getBoolean(stack, "ArmorNVLock", true)) {
                    player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 419, 0, true));
                } else if (player.isPotionActive(Potion.nightVision.id)) {
                    player.removePotionEffect(Potion.nightVision.id);
                }
                if (player.getFoodStats().getFoodLevel() < 20 && ProfileHelper.getBoolean(stack, "Saturation", false)) {
                    player.getFoodStats().setFoodLevel(20);
                    extractEnergy(stack, HellConfig.arialArmorEnergyToAddFood, false);
                }
            }
            if (stack.getItem() == DraconicEvolutionIntegration.arial_chest && ProfileHelper.getBoolean(stack, "DamageBoost", false)) {
                player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 20, 1, true));
                extractEnergy(stack, HellConfig.arialArmorEnergyToAddFood, false);
            }
            if (stack.getItem() == DraconicEvolutionIntegration.arial_boots && ProfileHelper.getBoolean(stack, "SpeedBoost", false)) {
                player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 20, 1, true));
                extractEnergy(stack, HellConfig.arialArmorEnergyToAddFood, false);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
        EnergyUtility.addEnergyAndLore(stack, list);
        ToolBase.holdCTRLForUpgrades(list, stack);
    }

    public boolean clearNegativeEffects(Entity par3Entity) {
        boolean flag = false;
        if (par3Entity.ticksExisted % 20 == 0 && par3Entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)par3Entity;
            Collection<PotionEffect> potions = player.getActivePotionEffects();
            if (player.isBurning())
                player.extinguish();
            Iterator<PotionEffect> i$ = potions.iterator();
            while (i$.hasNext()) {
                PotionEffect potion = i$.next();
                int id = potion.getPotionID();
                if (((Boolean)ReflectionHelper.getPrivateValue(Potion.class, Potion.potionTypes[id], new String[] { "isBadEffect", "field_76418_K", "J" })).booleanValue()) {
                    if (potion.getPotionID() != Potion.digSlowdown.id && (player.getHeldItem() == null || (player.getHeldItem().getItem() != ModItems.wyvernBow && player.getHeldItem().getItem() != ModItems.draconicBow) || id != 2)) {
                        player.removePotionEffect(id);
                        flag = true;
                    }
                    break;
                }
            }
        }
        return flag;
    }

    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int stored = ItemNBTHelper.getInteger(container, "Energy", 0);
        int receive = Math.min(maxReceive, Math.min(getMaxEnergyStored(container) - stored, this.maxTransfer));
        if (!simulate) {
            stored += receive;
            ItemNBTHelper.setInteger(container, "Energy", stored);
        }
        return receive;
    }

    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        int stored = ItemNBTHelper.getInteger(container, "Energy", 0);
        int extract = Math.min(maxExtract, Math.min(this.maxTransfer, stored));
        if (!simulate) {
            stored -= extract;
            ItemNBTHelper.setInteger(container, "Energy", stored);
        }
        return extract;
    }

    public int getEnergyStored(ItemStack container) {
        return ItemNBTHelper.getInteger(container, "Energy", 0);
    }

    public int getMaxEnergyStored(ItemStack container) {
        int points = EnumUpgrade.RF_CAPACITY.getUpgradePoints(container);
        return HellConfig.arialArmorBaseStorage + points * HellConfig.arialArmorStoragePerUpgrade;
    }

    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return (Entity)new EntityPersistentItem(world, location, itemstack);
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    public List<ItemConfigField> getFields(ItemStack stack, int slot) {
        List<ItemConfigField> list = new ArrayList<>();
        if (this.armorType == 0) {
            list.add((new ItemConfigField(6, slot, "ArmorNVActive")).readFromItem(stack, Boolean.valueOf(false)));
            list.add((new ItemConfigField(6, slot, "ArmorNVLock")).readFromItem(stack, Boolean.valueOf(true)));
            list.add((new ItemConfigField(6, slot, "Saturation")).readFromItem(stack, Boolean.valueOf(false)));
            if (Loader.isModLoaded("Thaumcraft"))
                list.add((new ItemConfigField(6, slot, "GogglesOfRevealing")).readFromItem(stack, Boolean.valueOf(true)));
        } else if (this.armorType == 1) {
            list.add((new ItemConfigField(4, slot, "VerticalAcceleration")).setMinMaxAndIncromente(Float.valueOf(0.0F), Float.valueOf(12.0F), Float.valueOf(0.1F)).readFromItem(stack, Float.valueOf(0.0F)).setModifier("PLUSPERCENT"));
            list.add((new ItemConfigField(4, slot, "ArmorFlightSpeedMult")).setMinMaxAndIncromente(Float.valueOf(0.0F), Float.valueOf(10.0F), Float.valueOf(0.1F)).readFromItem(stack, Float.valueOf(0.0F)).setModifier("PLUSPERCENT"));
            list.add((new ItemConfigField(6, slot, "EffectiveOnSprint")).readFromItem(stack, Boolean.valueOf(false)));
            list.add((new ItemConfigField(6, slot, "ArmorFlightLock")).readFromItem(stack, Boolean.valueOf(false)));
            list.add((new ItemConfigField(6, slot, "ArmorInertiaCancellation")).readFromItem(stack, Boolean.valueOf(false)));
            list.add((new ItemConfigField(6, slot, "DamageBoost")).readFromItem(stack, Boolean.valueOf(false)));
        } else if (this.armorType == 2) {
            list.add((new ItemConfigField(4, slot, "ArmorSpeedMult")).setMinMaxAndIncromente(Float.valueOf(0.0F), Float.valueOf(12.0F), Float.valueOf(0.1F)).readFromItem(stack, Float.valueOf(0.0F)).setModifier("PLUSPERCENT"));
            list.add((new ItemConfigField(6, slot, "ArmorSprintOnly")).readFromItem(stack, Boolean.valueOf(false)));
        } else if (this.armorType == 3) {
            list.add((new ItemConfigField(4, slot, "ArmorJumpMult")).setMinMaxAndIncromente(Float.valueOf(0.0F), Float.valueOf(20.0F), Float.valueOf(0.1F)).readFromItem(stack, Float.valueOf(0.0F)).setModifier("PLUSPERCENT"));
            list.add((new ItemConfigField(6, slot, "ArmorSprintOnly")).readFromItem(stack, Boolean.valueOf(false)));
            list.add((new ItemConfigField(6, slot, "ArmorHillStep")).readFromItem(stack, Boolean.valueOf(true)));
            list.add((new ItemConfigField(6, slot, "SpeedBoost")).readFromItem(stack, Boolean.valueOf(false)));
        }
        return list;
    }

    public String getInventoryName() {
        return LocalizationUtils.localize("info.de.toolInventoryEnch.txt");
    }

    public int getInventorySlots() {
        return 0;
    }

    public boolean isEnchantValid(Enchantment enchant) {
        return (enchant.type == EnumEnchantmentType.armor || (this.armorType == 0 && enchant.type == EnumEnchantmentType.armor_head) || (this.armorType == 1 && enchant.type == EnumEnchantmentType.armor_torso) || (this.armorType == 2 && enchant.type == EnumEnchantmentType.armor_legs) || (this.armorType == 3 && enchant.type == EnumEnchantmentType.armor_feet));
    }

    @Method(modid = "Thaumcraft")
    public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
        return ProfileHelper.getBoolean(itemstack, "GogglesOfRevealing", true);
    }

    @Method(modid = "Thaumcraft")
    public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
        return ProfileHelper.getBoolean(itemstack, "GogglesOfRevealing", true);
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (HellConfig.useOldArmorModel)
            return super.getArmorModel(entityLiving, itemStack, armorSlot);
        if (this.model == null) {
            if (this.armorType == 0) {
                this.model = (ModelBiped)new ModelArialArmor(1.1F, true, false, false, false);
            } else if (this.armorType == 1) {
                this.model = (ModelBiped)new ModelArialArmor(1.1F, false, true, false, false);
            } else if (this.armorType == 2) {
                this.model = (ModelBiped)new ModelArialArmor(1.1F, false, false, true, false);
            } else {
                this.model = (ModelBiped)new ModelArialArmor(1.1F, false, false, false, true);
            }
            this.model.bipedHead.showModel = (this.armorType == 0);
            this.model.bipedHeadwear.showModel = (this.armorType == 0);
            this.model.bipedBody.showModel = (this.armorType == 1 || this.armorType == 2);
            this.model.bipedLeftArm.showModel = (this.armorType == 1);
            this.model.bipedRightArm.showModel = (this.armorType == 1);
            this.model.bipedLeftLeg.showModel = (this.armorType == 2 || this.armorType == 3);
            this.model.bipedRightLeg.showModel = (this.armorType == 2 || this.armorType == 3);
        }
        if (entityLiving == null)
            return this.model;
        this.model.isSneak = entityLiving.isSneaking();
        this.model.isRiding = entityLiving.isRiding();
        this.model.isChild = entityLiving.isChild();
        this.model.aimedBow = false;
        this.model.heldItemRight = (entityLiving.getHeldItem() != null) ? 1 : 0;
        if (entityLiving instanceof EntityPlayer && ((EntityPlayer)entityLiving).getItemInUseDuration() > 0) {
            EnumAction enumaction = ((EntityPlayer)entityLiving).getItemInUse().getItemUseAction();
            if (enumaction == EnumAction.block) {
                this.model.heldItemRight = 3;
            } else if (enumaction == EnumAction.bow) {
                this.model.aimedBow = true;
            }
        }
        return this.model;
    }

    public List<EnumUpgrade> getCUpgrades(ItemStack itemstack) {
        return new ArrayList<EnumUpgrade>() {
            {
                this.add(EnumUpgrade.RF_CAPACITY);
                this.add(EnumUpgrade.SHIELD_CAPACITY);
                this.add(EnumUpgrade.SHIELD_RECOVERY);
            }
        };
    }


    public int getUpgradeCap(ItemStack itemstack) {
        return HellConfig.arialArmorMaxUpgrades;
    }

    public int getMaxTier(ItemStack itemstack) {
        return 4;
    }

    public List<String> getUpgradeStats(ItemStack stack) {
        List<String> strings = new ArrayList<>();
        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.RFCapacity.txt") + ": " + InfoHelper.HITC() + Utills.formatNumber(getMaxEnergyStored(stack)));
        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.ShieldCapacity.txt") + ": " + InfoHelper.HITC() + (int)getProtectionPoints(stack));
        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.ShieldRecovery.txt") + ": " + InfoHelper.HITC() + Utills.round(getRecoveryPoints(stack) * 0.2D, 10.0D) + " EPS");
        return strings;
    }

    public int getMaxUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == EnumUpgrade.RF_CAPACITY.index)
            return 120;
        if (upgradeIndex == EnumUpgrade.SHIELD_CAPACITY.index)
            return 400;
        return 328;
    }

    public int getMaxUpgradePoints(int upgradeIndex, ItemStack stack) {
        return getMaxUpgradePoints(upgradeIndex);
    }

    public int getBaseUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == EnumUpgrade.SHIELD_CAPACITY.index) {
            int cap = (int)(getProtectionShare() * 13.0F) + ((this.armorType == 2) ? 2 : 0);
            return cap;
        }
        return (upgradeIndex == EnumUpgrade.SHIELD_RECOVERY.index) ? 40 : 0;
    }

    public float getProtectionPoints(ItemStack stack) {
        return (EnumUpgrade.SHIELD_CAPACITY.getUpgradePoints(stack) * 30);
    }

    public int getRecoveryPoints(ItemStack stack) {
        return EnumUpgrade.SHIELD_RECOVERY.getUpgradePoints(stack);
    }

    public float getSpeedModifier(ItemStack stack, EntityPlayer player) {
        if (ProfileHelper.getBoolean(stack, "ArmorSprintOnly", false))
            return player.isSprinting() ? ProfileHelper.getFloat(stack, "ArmorSpeedMult", 0.0F) : (ProfileHelper.getFloat(stack, "ArmorSpeedMult", 0.0F) / 5.0F);
        return ProfileHelper.getFloat(stack, "ArmorSpeedMult", 0.0F);
    }

    public float getJumpModifier(ItemStack stack, EntityPlayer player) {
        if (!ProfileHelper.getBoolean(stack, "ArmorSprintOnly", false))
            return ProfileHelper.getFloat(stack, "ArmorJumpMult", 0.0F);
        return (!player.isSprinting() && !BrandonsCore.proxy.isCtrlDown()) ? (ProfileHelper.getFloat(stack, "ArmorJumpMult", 0.0F) / 5.0F) : ProfileHelper.getFloat(stack, "ArmorJumpMult", 0.0F);
    }

    public boolean hasHillStep(ItemStack stack, EntityPlayer player) {
        if (!ProfileHelper.getBoolean(stack, "ArmorSprintOnly", false))
            return ProfileHelper.getBoolean(stack, "ArmorHillStep", true);
        return ((player.isSprinting() || BrandonsCore.proxy.isCtrlDown()) && ProfileHelper.getBoolean(stack, "ArmorHillStep", true));
    }

    public float getFireResistance(ItemStack stack) {
        return 2.0F;
    }

    public boolean[] hasFlight(ItemStack stack) {
        return new boolean[] { true, ProfileHelper.getBoolean(stack, "ArmorFlightLock", false), ProfileHelper.getBoolean(stack, "ArmorInertiaCancellation", false) };
    }

    public float getFlightSpeedModifier(ItemStack stack, EntityPlayer player) {
        if (ProfileHelper.getBoolean(stack, "EffectiveOnSprint", false))
            return BrandonsCore.proxy.isCtrlDown() ? ProfileHelper.getFloat(stack, "ArmorFlightSpeedMult", 0.0F) : 0.0F;
        return ProfileHelper.getFloat(stack, "ArmorFlightSpeedMult", 0.0F);
    }

    public float getFlightVModifier(ItemStack stack, EntityPlayer player) {
        if (ProfileHelper.getBoolean(stack, "EffectiveOnSprint", false))
            return BrandonsCore.proxy.isCtrlDown() ? ProfileHelper.getFloat(stack, "VerticalAcceleration", 0.0F) : 0.0F;
        return ProfileHelper.getFloat(stack, "VerticalAcceleration", 0.0F);
    }

    public int getEnergyPerProtectionPoint() {
        return HellConfig.arialArmorEnergyPerProtectionPoint;
    }

    public boolean hasProfiles() {
        return false;
    }
}
