package foxiwhitee.hellmod.integration.botania.tile.ae;


import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkTile;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketUpdateMana;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

import java.util.List;

public class TileManaReceiver extends TIleAEMana implements IManaReceiver, ISaveProvider, ISparkAttachable, IManaPool {
    private final DimensionalCoord location = new DimensionalCoord(this);

    public TileManaReceiver() {
        setMaxStoredMana(HellConfig.manaMidgardPool);
    }

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
    public TickRateModulation tick(IGridNode iGridNode, int i) {
        extractMana(getMaxStoredMana() - getStoredMana());
        return TickRateModulation.IDLE;
    }

    public int getCurrentMana() {
        return (int) Math.min(Integer.MAX_VALUE, getStoredMana());
    }

    public boolean isFull() {
        return false;
    }

    public void recieveMana(int mana) {
        if (mana >= 0) return;
        if (Math.abs(mana) > getMaxStoredMana()) setStoredMana(0);
        else setStoredMana(this.getStoredMana() - Math.abs(mana));
    }

    public boolean canRecieveManaFromBursts() {
        return true;
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
    public int getAvailableSpaceForMana() { return 0; }

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
    public boolean areIncomingTranfersDone() { return false; }

    @Override
    public boolean isOutputtingPower() {
        return false;
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {}
}