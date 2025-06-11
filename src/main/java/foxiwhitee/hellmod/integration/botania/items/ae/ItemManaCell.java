package foxiwhitee.hellmod.integration.botania.items.ae;

import appeng.api.AEApi;
import appeng.api.storage.ICellInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IItemList;
import appeng.util.InventoryAdaptor;
import appeng.util.Platform;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.helpers.IManaStorageCell;
import foxiwhitee.hellmod.integration.botania.me.ManaCellInventory;
import foxiwhitee.hellmod.integration.botania.me.ManaCellInventoryHandler;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

public class ItemManaCell extends Item implements IManaStorageCell {


    static {
        int[] sizes = new int[11];
        for (int i = 0; i < 11; i++) {
            sizes[i] = (int) Math.pow(4.0, i);
        }
        ItemManaCell.sizes = sizes;
    }
    private final static String[] prefixes = {"1K", "4K", "16K", "64K", "256K", "1M", "4M", "16M", "65M", "262M", "1B"};
    private IIcon[] icons = new IIcon[prefixes.length];
    private static int[] sizes;
    private String name;

    public ItemManaCell(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setMaxStackSize(1);
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean shift) {
        if (stack != null && stack.getItem() == this) {
            ManaCellInventoryHandler cellHandler = (ManaCellInventoryHandler) ManaCellInventory.getCell(stack, null);
            if (cellHandler != null && cellHandler.getCellInv() != null) {
                ICellInventory cell = cellHandler.getCellInv();
                long stored = cell.getStoredItemCount() / 1000000L;
                long max = 8L * cell.getTotalBytes() / 1000000L;
                list.add(LocalizationUtils.localize("tooltip.botania.storedInCell", stored, max));
            }
        }
    }

    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < prefixes.length; i++) {
            ItemStack stack = new ItemStack(BotaniaIntegration.manaStorageCell, 1, i);
            list.add(stack);
            ItemStack fullStack = stack.copy();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setLong("ic", getBytes(stack) * 8L);
            fullStack.setTagCompound(tag);
            list.add(fullStack);
        }
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return LocalizationUtils.localize(getUnlocalizedName() + ".name", prefixes[stack != null ? stack.getItemDamage() : 0]);
    }

    public boolean isManaCell(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() == this;
    }

    public double getIdleDrain(ItemStack itemStack) {
        return 1.0;
    }

    public long getBytes(ItemStack itemStack) {
        if (itemStack == null) return 0L;
        int meta = Math.max(0, Math.min(itemStack.getItemDamage(), prefixes.length - 1));
        return sizes[meta] * 1000000L / 8L;
    }

    public void registerIcons(IIconRegister getItemDamage) {
        for (int i = 0; i < prefixes.length; i++) {
            icons[i] = getItemDamage.registerIcon(HellCore.MODID + ":storageCells/" + name + prefixes[i]);
        }
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        disassemble(stack, world, player);
        return stack;
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return ForgeEventFactory.onItemUseStart(player, stack, 1) <= 0 || disassemble(stack, world, player);
    }

    public IIcon getIconFromDamage(int meta) {
        return icons[Math.max(0, Math.min(meta, 10))];
    }

    private boolean disassemble(ItemStack stack, World world, EntityPlayer player) {
        if (!player.isSneaking() || Platform.isClient()) return false;
        InventoryPlayer playerInventory = player.inventory;
        IMEInventoryHandler inv = AEApi.instance().registries().cell().getCellInventory(stack, null, StorageChannel.ITEMS);
        if (inv != null && playerInventory.getCurrentItem() == stack) {
            InventoryAdaptor ia = InventoryAdaptor.getAdaptor(player, ForgeDirection.UNKNOWN);
            IItemList list = inv.getAvailableItems(StorageChannel.ITEMS.createList());
            if (list.isEmpty() && ia != null) {
                playerInventory.setInventorySlotContents(playerInventory.currentItem, null);
                ItemStack extraA = ia.addItems(new ItemStack(BotaniaIntegration.manaStorageComponent, 1, stack.getItemDamage()));
                if (extraA != null) player.dropPlayerItemWithRandomChoice(extraA, false);
                ItemStack extraB = ia.addItems(new ItemStack(BotaniaIntegration.empty_mana_storage_cell));
                if (extraB != null) player.dropPlayerItemWithRandomChoice(extraB, false);
                if (player.inventoryContainer != null) player.inventoryContainer.detectAndSendChanges();
                return true;
            }
        }
        return false;
    }
}
