package foxiwhitee.hellmod.integration.draconic.items.tools;

import com.brandon3055.draconicevolution.client.render.IRenderTweak;
import com.brandon3055.draconicevolution.common.utills.IInventoryTool;
import com.brandon3055.draconicevolution.common.utills.ItemConfigField;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ArialPickaxe extends CMiningTool implements IInventoryTool, IRenderTweak, ICustomUpgradableItem {
    private String name;
    public ArialPickaxe(ToolMaterial material, String name) {
        super(material);
        this.name = name;
        setHarvestLevel("pickaxe", 10);
        setUnlocalizedName(name);
        setCapacity(HellConfig.arialToolsBaseStorage);
        setMaxExtract(HellConfig.arialToolsMaxTransfer);
        setMaxReceive(HellConfig.arialToolsMaxTransfer);
        this.energyPerOperation = HellConfig.arialToolsEnergyPerAction;
    }

    public String getUnlocalizedName() {
        return "item." + name;
    }

    public List<ItemConfigField> getFields(ItemStack stack, int slot) {
        List<ItemConfigField> list = super.getFields(stack, slot);
        list.add((new ItemConfigField(2, slot, "ToolDigAOE")).setMinMaxAndIncromente(Integer.valueOf(0), Integer.valueOf(EnumUpgrade.DIG_AOE.getUpgradePoints(stack)), Integer.valueOf(1)).readFromItem(stack, Integer.valueOf(0)).setModifier("AOE"));
        list.add((new ItemConfigField(2, slot, "ToolDigDepth")).setMinMaxAndIncromente(Integer.valueOf(1), Integer.valueOf(EnumUpgrade.DIG_DEPTH.getUpgradePoints(stack)), Integer.valueOf(1)).readFromItem(stack, Integer.valueOf(1)));
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
        GL11.glTranslated(0.34D, 0.69D, 0.1D);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(140.0F, 0.0F, -1.0F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glScaled(0.7D, 0.7D, 0.7D);
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glScalef(11.8F, 11.8F, 11.8F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(-1.2D, 0.0D, -0.35D);
        } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glRotatef(90.5F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(0.0D, 0.0D, -0.9D);
        }
    }

    public int getUpgradeCap(ItemStack itemstack) {
        return HellConfig.arialToolsMaxUpgrades;
    }

    public int getMaxTier(ItemStack itemstack) {
        return 4;
    }

    public List<String> getUpgradeStats(ItemStack stack) {
        return super.getUpgradeStats(stack);
    }

    public int getCapacity(ItemStack stack) {
        int points = EnumUpgrade.RF_CAPACITY.getUpgradePoints(stack);
        int cap = HellConfig.arialToolsBaseStorage + points * HellConfig.arialToolsStoragePerUpgrade;
        return cap;
    }

    public int getMaxUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == EnumUpgrade.RF_CAPACITY.index)
            return HellConfig.arialToolsMaxCapacityUpgradePoints;
        if (upgradeIndex == EnumUpgrade.DIG_AOE.index)
            return 12;
        if (upgradeIndex == EnumUpgrade.DIG_DEPTH.index)
            return 22;
        return (upgradeIndex == EnumUpgrade.DIG_SPEED.index) ? 64 : HellConfig.arialToolsMaxUpgradePoints;
    }

    public int getBaseUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == EnumUpgrade.DIG_AOE.index)
            return 6;
        if (upgradeIndex == EnumUpgrade.DIG_DEPTH.index)
            return 4;
        return (upgradeIndex == EnumUpgrade.DIG_SPEED.index) ? 14 : 0;
    }
}

