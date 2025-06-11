package foxiwhitee.hellmod.integration.botania.tile.ae;

import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.ISaveProvider;
import appeng.api.util.DimensionalCoord;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketUpdateMana;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

import java.util.List;

public class TileManaSupplier extends TIleAEMana implements IManaPool, ISaveProvider, ISparkAttachable {
    private final DimensionalCoord location = new DimensionalCoord(this);

    public TileManaSupplier() {
        setMaxStoredMana(HellConfig.manaMidgardPool);
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {}

    @Override
    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(1, 1, false, false);
    }

    @Override
    public DimensionalCoord getLocation() {
        return location;
    }

    @Override
    protected AppEngInternalInventory getInventory() { return new AppEngInternalInventory(this, 1); }

    @Override
    public TickRateModulation tick(IGridNode node, int ticts) {
        this.injectMana(getCurrentMana());
        return TickRateModulation.IDLE;
    }

    public boolean isFull() {
        return this.getCurrentMana() >= getMaxStoredMana();
    }

    public void recieveMana(int mana) {
        setStoredMana(Math.max(0, Math.min(this.getCurrentMana() + mana, getMaxStoredMana())));
    }

    public boolean canRecieveManaFromBursts() {
        return !isFull();
    }

    public boolean isOutputtingPower() {
        return false;
    }

    public void saveChanges(IMEInventory inv) {
        if (this.worldObj != null) {
            this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, (TileEntity)this);
        }
    }

    boolean isDwimerite() {
        return false;
    }

    @Override
    public boolean canAttachSpark(ItemStack itemStack) {
        return isDwimerite();
    }

    @Override
    public void attachSpark(ISparkEntity iSparkEntity) {}

    @Override
    public int getAvailableSpaceForMana() {
        return (int) Math.min(Integer.MAX_VALUE, Math.max(0, getMaxStoredMana() - this.getCurrentMana()));
    }

    @Override
    public ISparkEntity getAttachedSpark() {
        List<ISparkEntity> sparks = this.worldObj.getEntitiesWithinAABB(ISparkEntity.class, AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)(this.yCoord + 1), (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 2), (double)(this.zCoord + 1)));
        if (sparks.size() == 1) {
            Entity e = (Entity)sparks.get(0);
            return (ISparkEntity)e;
        } else {
            return null;
        }
    }

    @Override
    public boolean areIncomingTranfersDone() {return false;}

    @Override
    public int getCurrentMana() {
        return (int) Math.min(Integer.MAX_VALUE, getStoredMana());
    }
}