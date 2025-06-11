package foxiwhitee.hellmod.integration.avaritia.tile;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.tile.grid.AENetworkInvTile;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.items.LudicrousItems;
import java.util.ArrayList;

import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import foxiwhitee.hellmod.integration.avaritia.helpers.INeutronCollector;
import foxiwhitee.hellmod.tile.TileNetworkInv;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.Nullable;

public class TileNeutronUnifier extends AENetworkInvTile implements ISaveProvider, IGridTickable {
    public static final int[] slots = new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
            30, 31, 32, 33, 34, 35 };

    private final AppEngInternalInventory inventory = new AppEngInternalInventory((IAEAppEngInventory)this, 36);

    private final BaseActionSource source = (BaseActionSource)new MachineSource((IActionHost)this);

    private int progress;

    private int maxProgress;

    private final ArrayList<IAEItemStack> list;

    @Nullable
    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack(AvaritiaIntegration.neutronUnifier);
    }

    public IStorageGrid getStorage() {
        try {
            return getProxy().getStorage();
        } catch (GridAccessException e) {
            return null;
        }
    }

    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(1, 1, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode iGridNode, int i) {
        this.progress++;
        if (this.progress >= maxProgress) {
            spawnNeutronInME();
            progress = 0;
        }
        return TickRateModulation.IDLE;
    }

    public TileNeutronUnifier() {
        this.list = new ArrayList<>();
        getProxy().setFlags(GridFlags.REQUIRE_CHANNEL);
        getProxy().setIdlePowerUsage(100.0D);
    }

    public void spawnNeutronInME() {
        for (int i = 0; i < slots.length; i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack != null) {
                Block block = Block.getBlockFromItem(stack.getItem());
                if (block != null && (
                        block instanceof INeutronCollector || block == LudicrousBlocks.neutron_collector))
                    try {
                        AEItemStack aEItemStack;
                        if (block instanceof INeutronCollector) {
                            aEItemStack = AEItemStack.create(new ItemStack(((INeutronCollector)block).getStack().getItem(), (((INeutronCollector)block).getStack()).stackSize, ((INeutronCollector)block).getStack().getItemDamage()));
                            aEItemStack.setStackSize((long) ((((INeutronCollector) block).getStack()).stackSize) * stack.stackSize);
                        } else {
                            aEItemStack = AEItemStack.create(new ItemStack(LudicrousItems.resource, 1, 2));
                            aEItemStack.setStackSize((long) stack.stackSize);
                        }
                        IStorageGrid storage = getProxy().getStorage();
                        IAEItemStack ret = (IAEItemStack)storage.getItemInventory().injectItems((IAEItemStack) aEItemStack.copy(), Actionable.MODULATE, this.source);
                        int injSize = (int)aEItemStack.getStackSize();
                        if (ret != null) {
                            int retSize = (int)ret.getStackSize();
                            aEItemStack.setStackSize((injSize - retSize));
                        }
                        if (aEItemStack.getStackSize() != 0L)
                            this.list.add(aEItemStack);
                    } catch (Exception exception) {}
            }
        }
        makeUpdate(getProxy(), this.list, this.source);
    }

    public IInventory getInternalInventory() {
        return (IInventory)this.inventory;
    }

    public void onChangeInventory(IInventory iInventory, int slotId, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {
        if (Platform.isServer()) {
            if (iInventory == inventory) {
                int count = 0;
                int ticks = 0;
                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    if (inventory.getStackInSlot(i) != null) {
                        for (int j = 0; j < inventory.getStackInSlot(i).stackSize; j++) {
                            count++;
                            if (Block.getBlockFromItem(inventory.getStackInSlot(i).getItem()) instanceof INeutronCollector) {
                                ticks += ((INeutronCollector) Block.getBlockFromItem(inventory.getStackInSlot(i).getItem())).getTicks();
                            } else if (Block.getBlockFromItem(inventory.getStackInSlot(i).getItem()) == LudicrousBlocks.neutron_collector) {
                                ticks += 7111;
                            }
                        }
                    }
                }
                maxProgress = count > 0 ? ticks / count : 0;
            }
        }
    }

    public int[] getAccessibleSlotsBySide(ForgeDirection forgeDirection) {
        return slots;
    }

    public void saveChanges(IMEInventory imeInventory) {
        this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, (TileEntity)this);
    }

    public DimensionalCoord getLocation() {
        return new DimensionalCoord((TileEntity)this);
    }

    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }

    public static <T> void makeUpdate(AENetworkProxy proxy, Iterable<T> changes, BaseActionSource src) {
        try {
            proxy.getStorage().postAlterationOfStoredItems(StorageChannel.ITEMS, (Iterable<? extends IAEStack>) changes, src);
        } catch (GridAccessException gridAccessException) {}
    }
}
