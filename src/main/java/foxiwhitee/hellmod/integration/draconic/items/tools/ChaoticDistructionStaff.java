package foxiwhitee.hellmod.integration.draconic.items.tools;

import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.draconicevolution.client.render.IRenderTweak;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.MiningTool;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.ToolHandler;
import com.brandon3055.draconicevolution.common.items.weapons.IEnergyContainerWeaponItem;
import com.brandon3055.draconicevolution.common.utills.IConfigurableItem;
import com.brandon3055.draconicevolution.common.utills.IInventoryTool;
import com.brandon3055.draconicevolution.common.utills.ItemConfigField;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ChaoticDistructionStaff extends MiningTool implements IInventoryTool, IRenderTweak, IEnergyContainerWeaponItem, ICustomUpgradableItem {
    private String name;
    public ChaoticDistructionStaff(Item.ToolMaterial material, String name) {
        super(material);
        this.name = name;
        setUnlocalizedName(name);
        setHarvestLevel("pickaxe", 10);
        setHarvestLevel("shovel", 10);
        setHarvestLevel("axe", 10);
        setCapacity(HellConfig.chaoticToolsBaseStorage * 4 + HellConfig.chaoticWeaponsBaseStorage);
        setMaxExtract(HellConfig.chaoticToolsMaxTransfer * 4 + HellConfig.chaoticWeaponsMaxTransfer);
        setMaxReceive(HellConfig.chaoticToolsMaxTransfer * 4 + HellConfig.chaoticWeaponsMaxTransfer);
        this.energyPerOperation = HellConfig.chaoticToolsEnergyPerAction;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", 0));
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", HellConfig.chaoticToolsBaseStorage * 4 + HellConfig.chaoticWeaponsBaseStorage));
    }

    public String getUnlocalizedName() {
        return "item." + name;
    }

    public float func_150893_a(ItemStack stack, Block block) {
        return getEfficiency(stack);
    }

    public List<ItemConfigField> getFields(ItemStack stack, int slot) {
        List<ItemConfigField> list = super.getFields(stack, slot);
        list.add((new ItemConfigField(2, slot, "ToolDigAOE")).setMinMaxAndIncromente(Integer.valueOf(0), Integer.valueOf(ICustomUpgradableItem.EnumUpgrade.DIG_AOE.getUpgradePoints(stack)), Integer.valueOf(1)).readFromItem(stack, Integer.valueOf(0)).setModifier("AOE"));
        list.add((new ItemConfigField(2, slot, "ToolDigDepth")).setMinMaxAndIncromente(Integer.valueOf(1), Integer.valueOf(ICustomUpgradableItem.EnumUpgrade.DIG_DEPTH.getUpgradePoints(stack)), Integer.valueOf(1)).readFromItem(stack, Integer.valueOf(1)));
        list.add((new ItemConfigField(2, slot, "WeaponAttackAOE")).setMinMaxAndIncromente(Integer.valueOf(0), Integer.valueOf(ICustomUpgradableItem.EnumUpgrade.ATTACK_AOE.getUpgradePoints(stack)), Integer.valueOf(1)).readFromItem(stack, Integer.valueOf(1)).setModifier("AOE"));
        list.add((new ItemConfigField(6, slot, "ToolVoidJunk")).readFromItem(stack, Boolean.valueOf(false)));
        return list;
    }

    public String getInventoryName() {
        return LocalizationUtils.localize("info.de.toolInventoryOblit.txt");
    }

    public int getInventorySlots() {
        return 9;
    }

    public boolean isEnchantValid(Enchantment enchant) {
        return (enchant.type == EnumEnchantmentType.digger || enchant.type == EnumEnchantmentType.weapon);
    }

    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        entity.hurtResistantTime = 0;
        ToolHandler.damageEntityBasedOnHealth(entity, player, 0.3F);
        ToolHandler.AOEAttack(player, entity, stack, IConfigurableItem.ProfileHelper.getInteger(stack, "WeaponAttackAOE", 0));
        return true;
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        return super.onItemRightClick(stack, world, player);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extended) {
        super.addInformation(stack, player, list, extended);
        list.add("");
        list.add(EnumChatFormatting.BLUE + "+" + ToolHandler.getBaseAttackDamage(stack) + " " + LocalizationUtils.localize("info.de.attackDamage.txt"));
        list.add(EnumChatFormatting.BLUE + "+30% " + LocalizationUtils.localize("info.de.bonusHealthDamage.txt"));
    }

    public void tweakRender(IItemRenderer.ItemRenderType type) {
        GL11.glTranslated(0.77D, 0.19D, -0.15D);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-35.0F, 0.0F, -1.0F, 0.0F);
        GL11.glScaled(0.7D, 0.7D, 0.7D);
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glScalef(6.0F, 6.0F, 6.0F);
            GL11.glRotatef(145.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(-1.7D, 0.0D, 1.8D);
        } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glRotatef(-34.5F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(-1.1D, 0.0D, -0.2D);
        } else if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslated(0.0D, 0.4D, 0.0D);
        }
    }

    public int getUpgradeCap(ItemStack itemstack) {
        return HellConfig.chaoticStaffMaxUpgrades;
    }

    public int getMaxTier(ItemStack itemstack) {
        return 3;
    }

    public int getMaxUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.index)
            return HellConfig.chaoticStaffMaxCapacityUpgradePoints;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_AOE.index)
            return 8;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_DEPTH.index)
            return 11;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ATTACK_AOE.index)
            return 13;
        return (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ATTACK_DAMAGE.index) ? 252 : HellConfig.chaoticStaffMaxUpgradePoints;
    }

    public int getBaseUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_AOE.index)
            return 3;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_DEPTH.index)
            return 7;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ATTACK_AOE.index)
            return 3;
        return (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.ATTACK_DAMAGE.index) ? 60 : 0;
    }

    public int getCapacity(ItemStack stack) {
        int points = ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.getUpgradePoints(stack);
        int cap = HellConfig.chaoticToolsBaseStorage * 4 + HellConfig.chaoticWeaponsBaseStorage + points * (HellConfig.chaoticToolsStoragePerUpgrade + HellConfig.chaoticWeaponsStoragePerUpgrade) / 2;
        return cap;
    }

    @Override
    public List<ICustomUpgradableItem.EnumUpgrade> getCUpgrades(ItemStack itemstack) {
        List<ICustomUpgradableItem.EnumUpgrade> list = transformUpgrades(super.getUpgrades(itemstack));
        list.add(ICustomUpgradableItem.EnumUpgrade.ATTACK_AOE);
        list.add(ICustomUpgradableItem.EnumUpgrade.ATTACK_DAMAGE);
        list.remove(ICustomUpgradableItem.EnumUpgrade.DIG_SPEED);
        return list;
    }

    public List<String> getUpgradeStats(ItemStack stack) {
        List<String> list = super.getUpgradeStats(stack);
        list.add(InfoHelper.ITC() + LocalizationUtils.localize("info.de.attackDamage.txt") + ": " + InfoHelper.HITC() + ToolHandler.getBaseAttackDamage(stack));
        return list;
    }

    public int getEnergyPerAttack() {
        return HellConfig.chaoticWeaponsEnergyPerAttack;
    }
}