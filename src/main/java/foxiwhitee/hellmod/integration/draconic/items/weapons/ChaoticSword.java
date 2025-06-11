package foxiwhitee.hellmod.integration.draconic.items.weapons;

import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.brandonscore.common.utills.Utills;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.client.render.IRenderTweak;
import com.brandon3055.draconicevolution.common.entity.EntityPersistentItem;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.ToolBase;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.ToolHandler;
import com.brandon3055.draconicevolution.common.items.weapons.IEnergyContainerWeaponItem;
import com.brandon3055.draconicevolution.common.network.ToolModePacket;
import com.brandon3055.draconicevolution.common.utills.IConfigurableItem;
import com.brandon3055.draconicevolution.common.utills.IHudDisplayItem;
import com.brandon3055.draconicevolution.common.utills.IInventoryTool;
import com.brandon3055.draconicevolution.common.utills.ItemConfigField;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
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
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ChaoticSword extends ItemSword implements IEnergyContainerWeaponItem, IInventoryTool, IRenderTweak, ICustomUpgradableItem, IHudDisplayItem {
    protected int capacity;

    protected int maxReceive;

    protected int maxExtract;

    private String name;

    public ChaoticSword(Item.ToolMaterial material, String name) {
        super(material);
        this.name = name;
        this.capacity = HellConfig.chaoticWeaponsBaseStorage;
        this.maxReceive = HellConfig.chaoticWeaponsMaxTransfer;
        this.maxExtract = HellConfig.chaoticWeaponsMaxTransfer;
        setUnlocalizedName(name);
        setCreativeTab(HellCore.HELL_TAB);
    }

    public boolean isItemTool(ItemStack p_77616_1_) {
        return true;
    }

    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", 0));
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", this.capacity));
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(HellCore.MODID + ":" + name);
    }

    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        entity.hurtResistantTime = 0;
        ToolHandler.AOEAttack(player, entity, stack, IConfigurableItem.ProfileHelper.getInteger(stack, "WeaponAttackAOE", 0));
        ToolHandler.damageEntityBasedOnHealth(entity, player, 0.2F);
        return true;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extraInformation) {
        if (InfoHelper.holdShiftForDetails(list)) {
            List<ItemConfigField> l = getFields(stack, 0);
            Iterator<ItemConfigField> i$ = l.iterator();
            while (i$.hasNext()) {
                ItemConfigField f = i$.next();
                list.add(f.getTooltipInfo());
            }
            list.add(InfoHelper.ITC() + LocalizationUtils.localize("info.de.sword.txt"));
            InfoHelper.addLore(stack, list);
        }
        ToolBase.holdCTRLForUpgrades(list, stack);
        InfoHelper.addEnergyInfo(stack, list);
        list.add("");
        list.add(EnumChatFormatting.BLUE + "+" + ToolHandler.getBaseAttackDamage(stack) + " " + LocalizationUtils.localize("info.de.attackDamage.txt"));
        list.add(EnumChatFormatting.BLUE + "+20% " + LocalizationUtils.localize("info.de.bonusHealthDamage.txt"));
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.rare;
    }

    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (container.stackTagCompound == null)
            container.stackTagCompound = new NBTTagCompound();
        int energy = container.stackTagCompound.getInteger("Energy");
        int energyReceived = Math.min(getMaxEnergyStored(container) - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            energy += energyReceived;
            container.stackTagCompound.setInteger("Energy", energy);
        }
        return energyReceived;
    }

    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.stackTagCompound != null && container.stackTagCompound.hasKey("Energy")) {
            int energy = container.stackTagCompound.getInteger("Energy");
            int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
            if (!simulate) {
                energy -= energyExtracted;
                container.stackTagCompound.setInteger("Energy", energy);
            }
            return energyExtracted;
        }
        return 0;
    }

    public int getEnergyStored(ItemStack container) {
        return (container.stackTagCompound != null && container.stackTagCompound.hasKey("Energy")) ? container.stackTagCompound.getInteger("Energy") : 0;
    }

    public int getMaxEnergyStored(ItemStack container) {
        int points = ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.getUpgradePoints(container);
        int cap = HellConfig.chaoticWeaponsBaseStorage + points * HellConfig.chaoticWeaponsStoragePerUpgrade;
        return cap;
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return (getEnergyStored(stack) != getMaxEnergyStored(stack));
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0D - (double) getEnergyStored(stack) / getMaxEnergyStored(stack);
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return (Entity)new EntityPersistentItem(world, location, itemstack);
    }

    public String getInventoryName() {
        return LocalizationUtils.localize("info.de.toolInventoryEnch.txt");
    }

    public int getInventorySlots() {
        return 0;
    }

    public boolean isEnchantValid(Enchantment enchant) {
        return (enchant.type == EnumEnchantmentType.weapon);
    }

    public List<ItemConfigField> getFields(ItemStack stack, int slot) {
        List<ItemConfigField> list = new ArrayList<>();
        list.add((new ItemConfigField(2, slot, "WeaponAttackAOE")).setMinMaxAndIncromente(Integer.valueOf(0), Integer.valueOf(ICustomUpgradableItem.EnumUpgrade.ATTACK_AOE.getUpgradePoints(stack)), Integer.valueOf(1)).readFromItem(stack, Integer.valueOf(1)).setModifier("AOE"));
        return list;
    }

    public Multimap getAttributeModifiers(ItemStack stack) {
        Multimap map = super.getAttributeModifiers(stack);
        map.clear();
        return map;
    }

    public void tweakRender(IItemRenderer.ItemRenderType type) {
        GL11.glTranslated(-0.01D, 1.11D, -0.15D);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(140.0F, 0.0F, -1.0F, 0.0F);
        GL11.glScaled(0.7D, 0.7D, 0.7D);
        if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslated(0.0D, -0.4D, 0.0D);
        } else if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glScalef(8.0F, 8.0F, 8.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(1.9D, 0.0D, 0.0D);
        } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glRotatef(-90.5F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(-0.8D, 0.0D, 0.0D);
        }
    }

    public List<ICustomUpgradableItem.EnumUpgrade> getCUpgrades(ItemStack itemstack) {
        return new ArrayList<ICustomUpgradableItem.EnumUpgrade>() {
            {
                this.add(ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY);
                this.add(ICustomUpgradableItem.EnumUpgrade.ATTACK_AOE);
                this.add(ICustomUpgradableItem.EnumUpgrade.ATTACK_DAMAGE);
            }
        };
    }

    public int getUpgradeCap(ItemStack itemstack) {
        return HellConfig.chaoticWeaponsMaxUpgrades;
    }

    public int getMaxTier(ItemStack itemstack) {
        return 3;
    }

    public int getMaxUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.index)
            return HellConfig.chaoticWeaponsMaxCapacityUpgradePoints;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ATTACK_AOE.index)
            return 7;
        return (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ATTACK_DAMAGE.index) ? 36 : HellConfig.chaoticWeaponsMaxUpgradePoints;
    }

    public int getMaxUpgradePoints(int upgradeIndex, ItemStack stack) {
        return getMaxUpgradePoints(upgradeIndex);
    }

    public int getBaseUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ATTACK_AOE.index)
            return 3;
        return (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ARROW_DAMAGE.index) ? 0 : 0;
    }

    public List<String> getUpgradeStats(ItemStack stack) {
        List<String> strings = new ArrayList<>();
        int attackaoe = 0;
        Iterator<ItemConfigField> i$ = getFields(stack, 0).iterator();
        while (i$.hasNext()) {
            ItemConfigField field = i$.next();
            if (field.name.equals("WeaponAttackAOE"))
                attackaoe = 1 + ((Integer)field.max).intValue() * 2;
        }
        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.RFCapacity.txt") + ": " + InfoHelper.HITC() + Utills.formatNumber(getMaxEnergyStored(stack)));
        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("info.de.attackDamage.txt") + ": " + InfoHelper.HITC() + ToolHandler.getBaseAttackDamage(stack));
        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.max.txt") + " " + LocalizationUtils.localize("gui.de.AttackAOE.txt") + ": " + InfoHelper.HITC() + attackaoe + "x" + attackaoe);
        return strings;
    }

    public List<String> getDisplayData(ItemStack stack) {
        List<String> list = new ArrayList<>();
        Iterator<ItemConfigField> i$ = getFields(stack, 0).iterator();
        while (i$.hasNext()) {
            ItemConfigField field = i$.next();
            list.add(field.getTooltipInfo());
        }
        return list;
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && !BrandonsCore.proxy.isDedicatedServer()) {
            ToolBase.handleModeChange(stack, player, InfoHelper.isShiftKeyDown(), InfoHelper.isCtrlKeyDown());
        } else if (world.isRemote && BrandonsCore.proxy.getMCServer() == null) {
            ToolBase.handleModeChange(stack, player, InfoHelper.isShiftKeyDown(), InfoHelper.isCtrlKeyDown());
            DraconicEvolution.network.sendToServer((IMessage)new ToolModePacket(InfoHelper.isShiftKeyDown(), InfoHelper.isCtrlKeyDown()));
        }
        return super.onItemRightClick(stack, world, player);
    }

    public boolean hasProfiles() {
        return true;
    }

    public int getEnergyPerAttack() {
        return HellConfig.chaoticWeaponsEnergyPerAttack;
    }
}
