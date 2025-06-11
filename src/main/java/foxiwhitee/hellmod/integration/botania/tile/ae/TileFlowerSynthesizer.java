package foxiwhitee.hellmod.integration.botania.tile.ae;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.flowers.ICoreFunctionalFlower;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalFlowerLogic;
import foxiwhitee.hellmod.integration.botania.flowers.IFunctionalMana;
import foxiwhitee.hellmod.integration.botania.items.ae.ItemUpgradeFlowerSynthesizer;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketUpdateMana;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileFlowerSynthesizer extends TIleAEMana implements IFunctionalMana {
    private final AppEngInternalInventory inventory = new AppEngInternalInventory(this, 9);
    private final AppEngInternalInventory inventoryUpgrade = new AppEngInternalInventory(this, 2);

    public TileFlowerSynthesizer() {
        setMaxStoredMana(HellConfig.manaMidgardPool);
        inventory.setMaxStackSize(1);
        inventoryUpgrade.setMaxStackSize(1);
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_AEMana(NBTTagCompound data) {
        super.readFromNBT_AEMana(data);
        inventory.readFromNBT(data, "inv");
        inventoryUpgrade.readFromNBT(data, "invU");
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_AEMana(NBTTagCompound data) {
        super.writeToNBT_AEMana(data);
        inventory.writeToNBT(data, "inv");
        inventoryUpgrade.writeToNBT(data, "invU");
    }

    @Override
    public TickRateModulation tick(IGridNode node, int ticks) {
        if (Platform.isServer()) {
            extractMana(getMaxStoredMana() - getStoredMana());
            ItemStack stack;
            IFunctionalFlowerLogic logic;
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                stack = inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ICoreFunctionalFlower) {
                    logic = ((ICoreFunctionalFlower) stack.getItem()).getLogic(stack);
                    if (logic != null) {
                        for (int j = 0; j < stack.stackSize; j++) {
                            logic.use(this);
                        }
                    }
                }
            }
            NetworkManager.instance.sendToAll(new PacketUpdateMana(this));
        }
        return TickRateModulation.IDLE;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public AppEngInternalInventory getInventory() {
        return inventory;
    }

    public AppEngInternalInventory getInventoryUpgrade() {
        return inventoryUpgrade;
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(1, 1, false, false);
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {
        if (iInventory == inventoryUpgrade) {
            int l = 0;
            for (int j = 0; j < inventoryUpgrade.getSizeInventory(); j++) {
                if (inventoryUpgrade.getStackInSlot(j) != null && inventoryUpgrade.getStackInSlot(j).getItem() instanceof ItemUpgradeFlowerSynthesizer) {
                    l += 2;
                }
            }
            l = l == 0 ? 1 : l;
            inventory.setMaxStackSize(l);
            dropExcessItems(inventory, inventory.getInventoryStackLimit());
        }
    }

    private void dropExcessItems(AppEngInternalInventory inv, int limit) {
        ItemStack stack;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            stack = inv.getStackInSlot(i);
            if (stack != null && stack.stackSize > limit) {
                int excess = stack.stackSize - limit;
                ItemStack excessStack = stack.splitStack(excess);
                inv.setInventorySlotContents(i, stack.stackSize > 0 ? stack : null);

                if (!worldObj.isRemote) {
                    float offsetX = worldObj.rand.nextFloat() * 0.8F + 0.1F;
                    float offsetY = worldObj.rand.nextFloat() * 0.8F + 0.1F;
                    float offsetZ = worldObj.rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem = new EntityItem(
                            worldObj,
                            xCoord + offsetX,
                            yCoord + offsetY,
                            zCoord + offsetZ,
                            excessStack.copy()
                    );
                    entityItem.motionX = worldObj.rand.nextGaussian() * 0.05;
                    entityItem.motionY = worldObj.rand.nextGaussian() * 0.05 + 0.2;
                    entityItem.motionZ = worldObj.rand.nextGaussian() * 0.05;
                    worldObj.spawnEntityInWorld(entityItem);
                }
            }
        }
    }

    @Override
    public void injectNewItems(AEItemStack stack) {
        try {
            IAEItemStack remainder = getProxy().getStorage().getItemInventory().injectItems(stack.copy(), Actionable.SIMULATE, getSource());
            if (remainder == null/* && remainder.getStackSize() > 0*/) {
                getProxy().getStorage().getItemInventory().injectItems(stack.copy(), Actionable.MODULATE, getSource());
            }
        } catch (GridAccessException ignore) {}
    }

    @Override
    public boolean extractNeedItems(AEItemStack stack) {
        try {
            IAEItemStack extracted = getProxy().getStorage().getItemInventory().extractItems(stack.copy(), Actionable.SIMULATE, getSource());
            if (extracted != null && extracted.getStackSize() == stack.getStackSize()) {
                getProxy().getStorage().getItemInventory().extractItems(extracted.copy(), Actionable.MODULATE, getSource());
                return true;
            }
        } catch (GridAccessException ignore) {}
        return false;
    }
}
