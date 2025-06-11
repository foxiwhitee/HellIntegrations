package foxiwhitee.hellmod.integration.draconic.items.tools;

import com.brandon3055.draconicevolution.client.render.IRenderTweak;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.MiningTool;
import com.brandon3055.draconicevolution.common.utills.IInventoryTool;
import com.brandon3055.draconicevolution.common.utills.ItemConfigField;
import java.util.List;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ChaoticShovel extends CMiningTool implements IInventoryTool, IRenderTweak, ICustomUpgradableItem {
    private String name;
    public ChaoticShovel(Item.ToolMaterial material, String name) {
        super(material);
        this.name = name;
        setHarvestLevel("shovel", 10);
        setUnlocalizedName(name);
        setCapacity(HellConfig.chaoticToolsBaseStorage);
        setMaxExtract(HellConfig.chaoticToolsMaxTransfer);
        setMaxReceive(HellConfig.chaoticToolsMaxTransfer);
        this.energyPerOperation = HellConfig.chaoticToolsEnergyPerAction;
    }

    public String getUnlocalizedName() {
        return "item." + name;
    }

    public List<ItemConfigField> getFields(ItemStack stack, int slot) {
        List<ItemConfigField> list = super.getFields(stack, slot);
        list.add((new ItemConfigField(2, slot, "ToolDigAOE")).setMinMaxAndIncromente(Integer.valueOf(0), Integer.valueOf(ICustomUpgradableItem.EnumUpgrade.DIG_AOE.getUpgradePoints(stack)), Integer.valueOf(1)).readFromItem(stack, Integer.valueOf(0)).setModifier("AOE"));
        list.add((new ItemConfigField(2, slot, "ToolDigDepth")).setMinMaxAndIncromente(Integer.valueOf(1), Integer.valueOf(ICustomUpgradableItem.EnumUpgrade.DIG_DEPTH.getUpgradePoints(stack)), Integer.valueOf(1)).readFromItem(stack, Integer.valueOf(1)));
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
        return (enchant.type == EnumEnchantmentType.digger);
    }

    public void tweakRender(IItemRenderer.ItemRenderType type) {
        GL11.glTranslated(0.15D, 0.9D, -0.12D);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(140.0F, 0.0F, -1.0F, 0.0F);
        GL11.glScaled(0.7D, 0.7D, 0.7D);
        if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslated(0.0D, -0.4D, 0.0D);
        } else if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(-1.45D, 0.0D, -0.15D);
        } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glRotatef(-90.5F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(-0.38D, 0.0D, -0.6D);
        }
    }

    public int getUpgradeCap(ItemStack itemstack) {
        return HellConfig.chaoticToolsMaxUpgrades;
    }

    public int getMaxTier(ItemStack itemstack) {
        return 3;
    }

    public int getCapacity(ItemStack stack) {
        int points = ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.getUpgradePoints(stack);
        int cap = HellConfig.chaoticToolsBaseStorage + points * HellConfig.chaoticToolsStoragePerUpgrade;
        return cap;
    }

    public int getMaxUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.index)
            return HellConfig.chaoticToolsMaxCapacityUpgradePoints;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_AOE.index)
            return 6;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_DEPTH.index)
            return 11;
        return (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_SPEED.index) ? 32 : HellConfig.chaoticToolsMaxUpgradePoints;
    }

    public int getBaseUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_AOE.index)
            return 3;
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_DEPTH.index)
            return 2;
        return (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.DIG_SPEED.index) ? 7 : 0;
    }
}
