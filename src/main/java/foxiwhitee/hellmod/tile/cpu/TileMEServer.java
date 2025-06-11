package foxiwhitee.hellmod.tile.cpu;

import appeng.api.AEApi;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingCpuChange;
import appeng.api.networking.security.MachineSource;
import appeng.api.util.WorldCoord;
import appeng.core.AELog;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.AENetworkProxyMultiblock;
import appeng.tile.crafting.TileCraftingTile;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import cpw.mods.fml.common.FMLCommonHandler;
import foxiwhitee.hellmod.blocks.cpu.BlockCustomAccelerators;
import foxiwhitee.hellmod.blocks.cpu.BlockCustomCraftingStorage;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public class TileMEServer extends TileCraftingTile implements IAEAppEngInventory {
    private final List<CraftingCPUCluster> virtualClusters = new ArrayList<>(12);
    private boolean isFormed = true;
    private AppEngInternalInventory storage = new AppEngInternalInventory(this, 12);
    private AppEngInternalInventory accelerators = new AppEngInternalInventory(this, 12);

    private long[] storage_bytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] accelerators_count = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public TileMEServer() {
        this.getProxy().setFlags(GridFlags.REQUIRE_CHANNEL);
        WorldCoord loc = new WorldCoord(this);
        for (int i = 0; i < 12; i++) {
            virtualClusters.add(new CraftingCPUCluster(loc, loc));
        }
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {
        if (this.getProxy().isReady() && (itemStack != null || itemStack1 != null)) {
            System.out.println("S " + itemStack1);
            if (iInventory == storage) {
                calculateCraftingStorage(i, itemStack1);
            }
            if (iInventory == accelerators) {
                calculateCraftingAccelerators(i, itemStack1);
            }
            initializeClusters();
        }
    }

    private void calculateCraftingStorage(int id, ItemStack stack) {
        if (id >= 0 && id <= storage_bytes.length) {
            long storageAmt = 0;
            int storageItemsAmt = 0;
            if (stack != null) {
                storageItemsAmt += stack.stackSize;
                for (int i = 0; i < stack.stackSize; i++) {
                    storageAmt += AEApi.instance().definitions().blocks().craftingStorage1k().isSameAs(stack) ? 1024
                            : AEApi.instance().definitions().blocks().craftingStorage4k().isSameAs(stack) ? 1024 * 4
                            : AEApi.instance().definitions().blocks().craftingStorage16k().isSameAs(stack) ? 1024 * 16
                            : AEApi.instance().definitions().blocks().craftingStorage64k().isSameAs(stack) ? 1024 * 64
                            : Block.getBlockFromItem(stack.getItem()) instanceof BlockCustomCraftingStorage ? BlockCustomCraftingStorage.getBytesFromBlock(Block.getBlockFromItem(stack.getItem()))
                            : 0;
                }
            }
            storage_bytes[id] = storageAmt;
        }
    }

    private void calculateCraftingAccelerators(int id, ItemStack stack) {
        if (id >= 0 && id <= accelerators_count.length) {
            int acceleratorAmt = 0;
            int acceleratorItemsAmt = 0;
            if (stack != null) {
                acceleratorItemsAmt += stack.stackSize;
                for (int i = 0; i < stack.stackSize; i++) {
                    acceleratorAmt += AEApi.instance().definitions().blocks().craftingAccelerator().isSameAs(stack) ? 1
                            : Block.getBlockFromItem(stack.getItem()) instanceof BlockCustomAccelerators ? BlockCustomAccelerators.getAcceleratorsFromMeta(stack.getItemDamage())
                            : 0;
                }
            }
            accelerators_count[id] = acceleratorAmt;
        }
    }

    public long getClusterStorageBytes(int clusterIndex) {
        if (clusterIndex >= 0 && clusterIndex < storage_bytes.length) {
            return storage_bytes[clusterIndex];
        }
        return 0;
    }

    public int getClusterAccelerator(int clusterIndex) {
        if (clusterIndex >= 0 && clusterIndex < accelerators_count.length) {
            return accelerators_count[clusterIndex];
        }
        return 0;
    }

    public int getClusterIndex(CraftingCPUCluster cluster) {
        return virtualClusters.indexOf(cluster);
    }

    @Override
    public final void updateMultiBlock() {}

    @Override
    protected AENetworkProxy createProxy() {
        return new AENetworkProxyMultiblock(this, "proxy", this.getItemFromTile(this), true);
    }

    public void initializeClusters() {
        if (Platform.isClient()) return;

        try {
            virtualClusters.clear();

            WorldCoord loc = new WorldCoord(this);
            for (int i = 0; i < 12; i++) {
                CraftingCPUCluster cluster = new CraftingCPUCluster(loc, loc);
                virtualClusters.add(cluster);
                FMLCommonHandler.instance().bus().register(cluster);
                invokeAddTile(cluster, this);
                cluster.updateStatus(true);
            }

            updateMeta(true);
            notifyNetwork();
        } catch (Throwable err) {
            AELog.debug("Error initializing ME Server clusters: %s", err.getMessage());
            disconnect(true);
        }
    }

    private void invokeAddTile(CraftingCPUCluster cluster, TileMEServer tile) {
        try {
            Method addTileMethod = CraftingCPUCluster.class.getDeclaredMethod("addTile", TileCraftingTile.class);
            addTileMethod.setAccessible(true);
            addTileMethod.invoke(cluster, tile);
        } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            AELog.debug("Failed to invoke addTile: %s", e.getMessage());
        }
    }

    @Override
    public void updateMeta(boolean updateFormed) {
        if (worldObj == null || notLoaded()) return;

        boolean power = getProxy().isActive();
        int newMeta = (isFormed ? 8 : 0) | (power ? 4 : 0);

        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != newMeta) {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 2);
        }

        if (updateFormed) {
            getProxy().setValidSides(isFormed ? EnumSet.allOf(ForgeDirection.class) : EnumSet.noneOf(ForgeDirection.class));
        }
    }

    @Override
    public boolean isFormed() {
        return isFormed;
    }

    @Override
    public void writeToNBT_TileCraftingTile(NBTTagCompound data) {
        storage.writeToNBT(data, "storage");
        accelerators.writeToNBT(data, "accelerators");
        data.setBoolean("core", true);
        NBTTagCompound clusters = new NBTTagCompound();
        for (int i = 0; i < virtualClusters.size(); i++) {
            NBTTagCompound clusterData = new NBTTagCompound();
            virtualClusters.get(i).writeToNBT(clusterData);
            clusters.setTag("cluster_" + i, clusterData);
        }
        data.setTag("virtualClusters", clusters);
    }

    @Override
    public void readFromNBT_TileCraftingTile(NBTTagCompound data) {
        storage.readFromNBT(data, "storage");
        accelerators.readFromNBT(data, "accelerators");
        setCoreBlock(data.getBoolean("core"));
        NBTTagCompound clusters = data.getCompoundTag("virtualClusters");
        for (int i = 0; i < virtualClusters.size(); i++) {
            NBTTagCompound clusterData = clusters.getCompoundTag("cluster_" + i);
            virtualClusters.get(i).readFromNBT(clusterData);
        }
    }

    @Override
    public void disconnect(boolean update) {
        for (CraftingCPUCluster cluster : virtualClusters) {
            if (cluster != null) cluster.destroy();
        }
        virtualClusters.clear();
        if (update) updateMeta(true);
    }

    private void notifyNetwork() {
        IGridNode node = getGridNode(ForgeDirection.UNKNOWN);
        if (node != null && node.getGrid() != null) {
            node.getGrid().postEvent(new MENetworkCraftingCpuChange(node));
        }
    }

    @Override
    public void onReady() {
        super.onReady();
        initializeClusters();
    }

    public List<CraftingCPUCluster> getVirtualClusters() {
        return virtualClusters;
    }

    public AppEngInternalInventory getStorage() {
        return storage;
    }

    public AppEngInternalInventory getAccelerators() {
        return accelerators;
    }
}