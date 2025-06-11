package foxiwhitee.hellmod.items;

import appeng.api.AEApi;
import appeng.api.config.FuzzyMode;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEItemStack;
import appeng.items.contents.CellConfig;
import appeng.items.contents.CellUpgrades;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.ModItems;
import foxiwhitee.hellmod.utils.cells.ICustomStorageCell;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class ItemCustomStorageCells extends Item implements ICustomStorageCell {
    public static final String[] suffixes = new String[]{"256K",
            "1M", "4M", "16M", "65M", "262M",
            "1G", "4G", "16G", "67G", "268G",
            "1T", "4T", "17T", "68T", "274T",
            "1P", "4P", "17P", "70P", "281P",
            "9E"};
    public static final long[] bytes_cell = new long[]{262144,
            1048576, 4194304, 16777216, 67108864, 268435456,
            1073741824, 4294967296L, 17179869184L, 68719476736L, 274877906944L,
            1099511627776L, 4398046511104L, 17592186044416L, 70368744177664L, 281474976710656L,
            1125899906842624L, 4503599627370496L, 1794402976530432L, 72057594037927936L, 288230376151711744L,
            Long.MAX_VALUE};
    public static final int[] types_cell = new int[]{63,
            HellConfig.typesCellM, HellConfig.typesCellM, HellConfig.typesCellM, HellConfig.typesCellM, HellConfig.typesCellM,
            HellConfig.typesCellG, HellConfig.typesCellG, HellConfig.typesCellG, HellConfig.typesCellG, HellConfig.typesCellG,
            HellConfig.typesCellT, HellConfig.typesCellT, HellConfig.typesCellT, HellConfig.typesCellT, HellConfig.typesCellT,
            HellConfig.typesCellP, HellConfig.typesCellP, HellConfig.typesCellP, HellConfig.typesCellP, HellConfig.typesCellP,
            HellConfig.typesCellE};
    private IIcon[] icons;

    public ItemCustomStorageCells() {
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("customStorageCells");
    }

    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        ICellRegistry cellRegistry = AEApi.instance().registries().cell();
        IMEInventoryHandler<IAEItemStack> invHandler = cellRegistry.getCellInventory(itemStack, (ISaveProvider)null, StorageChannel.ITEMS);
        ICellInventoryHandler inventoryHandler = (ICellInventoryHandler)invHandler;
        ICellInventory cellInv = inventoryHandler.getCellInv();
        long usedBytes = cellInv.getUsedBytes();
        list.add(LocalizationUtils.localize("tooltip.storage.physical.bytes", usedBytes, cellInv.getTotalBytes()));
        list.add(LocalizationUtils.localize("tooltip.storage.physical.types", cellInv.getStoredItemTypes(), cellInv.getTotalItemTypes()));
        if (usedBytes > 0L) {
            list.add(LocalizationUtils.localize("tooltip.storage.physical.content", cellInv.getStoredItemCount()));
        }

    }

    public int getBytesPerType(ItemStack cellItem) {
        long result = 8;//(bytes_cell[(int) (clamp_long(cellItem.getItemDamage(), 0, suffixes.length - 1))] / 128);
        if (result > 2097152) {
            return 2097152;
        } else {
            return (int) result;
        }
    }

    public static long clamp_long(long p_clamp_int_0_, long p_clamp_int_1_, long p_clamp_int_2_) {
        if (p_clamp_int_0_ < p_clamp_int_1_) {
            return p_clamp_int_1_;
        } else {
            return p_clamp_int_0_ > p_clamp_int_2_ ? p_clamp_int_2_ : p_clamp_int_0_;
        }
    }

    private NBTTagCompound ensureTagCompound(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.getTagCompound();
    }

    public long getBytes(ItemStack cellItem) {
        return bytes_cell[MathHelper.clamp_int(cellItem.getItemDamage(), 0, suffixes.length - 1)];
    }

    public IInventory getConfigInventory(ItemStack is) {
        return new CellConfig(is);
    }

    public FuzzyMode getFuzzyMode(ItemStack is) {
        if (!is.hasTagCompound()) {
            is.setTagCompound(new NBTTagCompound());
        }

        return FuzzyMode.values()[is.getTagCompound().getInteger("fuzzyMode")];
    }

    public IIcon getIconFromDamage(int dmg) {
        return this.icons[MathHelper.clamp_int(dmg, 0, suffixes.length - 1)];
    }

    public double getIdleDrain() {
        return (double)0.0F;
    }

    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack == null) {
            return super.getItemStackDisplayName(stack);
        } else {
            return super.getItemStackDisplayName(stack);
        }
    }

    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.epic;
    }

    public void getSubItems(Item item, CreativeTabs creativeTab, List itemList) {
        for(int i = 0; i < suffixes.length; ++i) {
            itemList.add(new ItemStack(item, 1, i));
        }

    }

    public int getTotalTypes(ItemStack cellItem) {
        return types_cell[MathHelper.clamp_int(cellItem.getItemDamage(), 0, suffixes.length - 1)];
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return "item.storageCell" + suffixes[itemStack.getItemDamage()];
    }

    public IInventory getUpgradesInventory(ItemStack is) {
        return new CellUpgrades(is, 2);
    }

    public boolean isBlackListed(ItemStack cellItem, IAEItemStack requestedAddition) {
        return false;
    }

    public boolean isEditable(ItemStack is) {
        return true;
    }

    public boolean isStorageCell(ItemStack i) {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (itemStack == null) {
            return itemStack;
        } else if (!entityPlayer.isSneaking()) {
            return itemStack;
        } else {
            IMEInventoryHandler<IAEItemStack> invHandler = AEApi.instance().registries().cell().getCellInventory(itemStack, (ISaveProvider)null, StorageChannel.ITEMS);
            ICellInventoryHandler inventoryHandler = (ICellInventoryHandler)invHandler;
            ICellInventory cellInv = inventoryHandler.getCellInv();
            return cellInv.getUsedBytes() == 0L && entityPlayer.inventory.addItemStackToInventory(new ItemStack(getItemEmptyCell(suffixes[itemStack.getItemDamage()].replaceAll("\\d+", "")))) ? new ItemStack(ModItems.ITEMS.get("storageComponent" + suffixes[itemStack.getItemDamage()])) : itemStack;
        }
    }


    private Item getItemEmptyCell(String suffix) {
        switch (suffix) {
            case "M": return ModItems.ITEMS.get("advancedEmptyCell");
            case "G": return ModItems.ITEMS.get("hybridEmptyCell");
            case "T": return ModItems.ITEMS.get("ultimateEmptyCell");
            case "P": return ModItems.ITEMS.get("quantumEmptyCell");
            case "E": return ModItems.ITEMS.get("singularEmptyCell");
            default: return AEApi.instance().definitions().materials().emptyStorageCell().maybeItem().get();
        }
    }

    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[suffixes.length];

        for(int i = 0; i < suffixes.length; ++i) {
            this.icons[i] = iconRegister.registerIcon("hellmod:storageCells/storageCell" + suffixes[i]);
        }

    }

    public void setFuzzyMode(ItemStack is, FuzzyMode fzMode) {
        if (!is.hasTagCompound()) {
            is.setTagCompound(new NBTTagCompound());
        }

        is.getTagCompound().setInteger("fuzzyMode", fzMode.ordinal());
    }


    public boolean storableInStorageCell() {
        return false;
    }


}

