package foxiwhitee.hellmod.integration.botania.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;

public class TileManaCharger extends TileBaseBotania {
    public ItemStack stack;

    public static int TRANSFER = 10000000;

    public void writeToNBT(NBTTagCompound compound) {
        if (this.stack != null) {
            NBTTagCompound tag = new NBTTagCompound();
            this.stack.writeToNBT(tag);
            compound.setTag("stack", (NBTBase)tag);
        }
        super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("stack"));
    }

    public void updateEntity() {
        if (!this.worldObj.isRemote && this.stack != null) {
            TileEntity tileEntity = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
            if (tileEntity instanceof IManaPool) {
                IManaPool pool = (IManaPool)tileEntity;
                IManaItem manaItem = (IManaItem)this.stack.getItem();
                if (pool.isOutputtingPower()) {
                    if (manaItem.canReceiveManaFromPool(this.stack, tileEntity) && manaItem.getMana(this.stack) != manaItem.getMaxMana(this.stack) && pool.getCurrentMana() > 0) {
                        int mana = Math.min(manaItem.getMaxMana(this.stack) - manaItem.getMana(this.stack), TRANSFER);
                        mana = Math.min(pool.getCurrentMana(), mana);
                        pool.recieveMana(-mana);
                        manaItem.addMana(this.stack, mana);
                    }
                } else if (manaItem.canExportManaToPool(this.stack, tileEntity)) {
                    int currentManaInStack = manaItem.getMana(this.stack);
                    if (!pool.isFull() && currentManaInStack > 0) {
                        int mana = Math.min(currentManaInStack, TRANSFER);
                        pool.recieveMana(mana);
                        manaItem.addMana(this.stack, -mana);
                    }
                }
                sync();
            }
        }
    }
}