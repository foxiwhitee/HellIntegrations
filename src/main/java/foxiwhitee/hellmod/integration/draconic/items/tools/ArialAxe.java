package foxiwhitee.hellmod.integration.draconic.items.tools;

import com.brandon3055.draconicevolution.client.render.IRenderTweak;
import com.brandon3055.draconicevolution.common.utills.IInventoryTool;
import com.brandon3055.draconicevolution.common.utills.ItemConfigField;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ArialAxe extends CMiningTool implements IInventoryTool, IRenderTweak, ICustomUpgradableItem {
    private String name;
    public ArialAxe(ToolMaterial material, String name) {
        super(material);
        this.name = name;
        setHarvestLevel("axe", 10);
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
        list.add((new ItemConfigField(6, slot, "AxeTreeMode")).readFromItem(stack, Boolean.valueOf(false)));
        return list;
    }

    public String getInventoryName() {
        return LocalizationUtils.localize("info.de.toolInventoryEnch.txt");
    }

    public int getInventorySlots() {
        return 0;
    }

    public boolean isEnchantValid(Enchantment enchant) {
        return (enchant.type == EnumEnchantmentType.digger);
    }

    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
        if (ProfileHelper.getBoolean(stack, "AxeTreeMode", false) && isTree(player.worldObj, x, y, z)) {
            trimLeavs(x, y, z, player, player.worldObj, stack);
            for (int i = 0; i < 9; i++)
                player.worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(player.worldObj.getBlock(x, y, z)) + (player.worldObj.getBlockMetadata(x, y, z) << 12));
            chopTree(x, y, z, player, player.worldObj, stack);
            return false;
        }
        return super.onBlockStartBreak(stack, x, y, z, player);
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
        return HellConfig.arialToolsBaseStorage + points * HellConfig.arialToolsStoragePerUpgrade;
    }

    public int getMaxUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == EnumUpgrade.RF_CAPACITY.index)
            return HellConfig.arialToolsMaxCapacityUpgradePoints;
        if (upgradeIndex == EnumUpgrade.DIG_AOE.index)
            return 12;
        if (upgradeIndex == EnumUpgrade.DIG_DEPTH.index)
            return 14;
        return (upgradeIndex == EnumUpgrade.DIG_SPEED.index) ? 64 : HellConfig.arialToolsMaxUpgradePoints;
    }

    public int getBaseUpgradePoints(int upgradeIndex) {
        if (upgradeIndex == EnumUpgrade.DIG_AOE.index)
            return 6;
        if (upgradeIndex == EnumUpgrade.DIG_DEPTH.index)
            return 4;
        return (upgradeIndex == EnumUpgrade.DIG_SPEED.index) ? 14 : 0;
    }

    private boolean isTree(World world, int X, int Y, int Z) {
        Block wood = world.getBlock(X, Y, Z);
        if (wood != null && wood.isWood((IBlockAccess)world, X, Y, Z)) {
            int top = Y;
            int leaves;
            for (leaves = Y; leaves <= Y + 50; leaves++) {
                if (!world.getBlock(X, leaves, Z).isWood((IBlockAccess)world, X, leaves, Z) && !world.getBlock(X, leaves, Z).isLeaves((IBlockAccess)world, X, leaves, Z)) {
                    top = Y + leaves;
                    break;
                }
            }
            leaves = 0;
            for (int xPos = X - 1; xPos <= X + 1; xPos++) {
                for (int yPos = Y; yPos <= top; yPos++) {
                    for (int zPos = Z - 1; zPos <= Z + 1; zPos++) {
                        if (world.getBlock(xPos, yPos, zPos).isLeaves((IBlockAccess)world, xPos, yPos, zPos))
                            leaves++;
                    }
                }
            }
            if (leaves >= 3)
                return true;
            return false;
        }
        return false;
    }

    void chopTree(int X, int Y, int Z, EntityPlayer player, World world, ItemStack stack) {
        for (int xPos = X - 1; xPos <= X + 1; xPos++) {
            for (int yPos = Y; yPos <= Y + 1; yPos++) {
                for (int zPos = Z - 1; zPos <= Z + 1; zPos++) {
                    Block block = world.getBlock(xPos, yPos, zPos);
                    int meta = world.getBlockMetadata(xPos, yPos, zPos);
                    if (block.isWood((IBlockAccess)world, xPos, yPos, zPos)) {
                        world.setBlockToAir(xPos, yPos, zPos);
                        if (!player.capabilities.isCreativeMode) {
                            if (block.removedByPlayer(world, player, xPos, yPos, zPos, false))
                                block.onBlockDestroyedByPlayer(world, xPos, yPos, zPos, meta);
                            block.harvestBlock(world, player, xPos, yPos, zPos, meta);
                            block.onBlockHarvested(world, xPos, yPos, zPos, meta, player);
                            onBlockDestroyed(stack, world, block, xPos, yPos, zPos, (EntityLivingBase)player);
                        }
                        chopTree(xPos, yPos, zPos, player, world, stack);
                    }
                }
            }
        }
    }

    void trimLeavs(int X, int Y, int Z, EntityPlayer player, World world, ItemStack stack) {
        scedualUpdates(X, Y, Z, player, world, stack);
    }

    void scedualUpdates(int X, int Y, int Z, EntityPlayer player, World world, ItemStack stack) {
        for (int xPos = X - 15; xPos <= X + 15; xPos++) {
            for (int yPos = Y; yPos <= Y + 50; yPos++) {
                for (int zPos = Z - 15; zPos <= Z + 15; zPos++) {
                    Block block = world.getBlock(xPos, yPos, zPos);
                    if (block.isLeaves((IBlockAccess)world, xPos, yPos, zPos))
                        world.scheduleBlockUpdate(xPos, yPos, zPos, block, 2 + world.rand.nextInt(10));
                }
            }
        }
    }

    public void tweakRender(IItemRenderer.ItemRenderType type) {
        GL11.glTranslated(0.34D, 0.69D, 0.1D);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(140.0F, 0.0F, -1.0F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glScaled(0.7D, 0.7D, 0.7D);
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glScalef(11.0F, 11.0F, 11.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(-1.3D, 0.0D, -0.45D);
        } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glRotatef(90.5F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(0.0D, 0.0D, -0.9D);
        }
    }
}
