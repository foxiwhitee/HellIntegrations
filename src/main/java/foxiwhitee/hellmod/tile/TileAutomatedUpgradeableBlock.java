package foxiwhitee.hellmod.tile;

import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.storage.StorageChannel;
import appeng.api.util.AEColor;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import foxiwhitee.hellmod.helpers.IAutomatedBlockUpgrade;
import foxiwhitee.hellmod.recipes.BaseAutoBlockRecipe;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TileAutomatedUpgradeableBlock extends TileAutomatedInvBlock {
    private static final int[] SIDES = {0};

    private final AppEngInternalInventory inventory = new AppEngInternalInventory(this, 1);
    private Map<String, List<BaseAutoBlockRecipe>> recipes = new HashMap<>();
    private Map<Class<? extends IAutomatedBlockUpgrade>, String> upgrades = new HashMap<>();
    private String status = "null";

    public TileAutomatedUpgradeableBlock() {
        initializeCrafts();
    }
    protected List<BaseAutoBlockRecipe> getRecipes() {
        if (status == "null") return null;
        return recipes.get(status);
    }

    @Override
    public IInventory getInternalInventory() {
        return inventory;
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {
        if (iInventory == inventory) {
            if (iInventory.getStackInSlot(0) == null) {
                status = "null";
                return;
            }
            if (iInventory.getStackInSlot(0).getItem() instanceof IAutomatedBlockUpgrade) {
                for (Map.Entry<Class<? extends IAutomatedBlockUpgrade>, String> entry : upgrades.entrySet()) {
                    if (entry.getKey().isInstance(iInventory.getStackInSlot(0).getItem())) {
                        status = entry.getValue();
                    }
                }
            }
        }
        try {
            getProxy().getGrid().postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
        } catch (GridAccessException ignored) {}
    }

    @Override
    public int[] getAccessibleSlotsBySide(ForgeDirection side) {
        return SIDES;
    }

    protected void initializeCrafts() {
        for (int i = 0; i < getStatuses().length; i++) {
            recipes.put(getStatuses()[i], getCrafts()[i]);
            upgrades.put(getUpgrades()[i], getStatuses()[i]);
        }
    }

    protected abstract String[] getStatuses();

    protected abstract Class<? extends IAutomatedBlockUpgrade>[] getUpgrades();

    protected abstract List<BaseAutoBlockRecipe>[] getCrafts();

    @TileEvent(TileEventType.NETWORK_WRITE)
    public void writeToStream(ByteBuf data) {
        ItemStack item = inventory.getStackInSlot(0);
        data.writeInt(item == null ? 0 : (item.getItemDamage() << 16) | Item.getIdFromItem(item.getItem()));
    }

    @TileEvent(TileEventType.NETWORK_READ)
    public boolean readFromStream(ByteBuf data) {
        int item = data.readInt();
        boolean r = simpleAreStacksEqual(inventory.getStackInSlot(0), item == 0 ? null : new ItemStack(Item.getItemById(item & 0xFFFF), 1, item >> 16));
        inventory.setInventorySlotContents(0, item == 0 ? null : new ItemStack(Item.getItemById(item & 0xFFFF), 1, item >> 16));
        return r;
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBTC(NBTTagCompound data) {
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBTC(NBTTagCompound data) {

    }

    private boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }
}
