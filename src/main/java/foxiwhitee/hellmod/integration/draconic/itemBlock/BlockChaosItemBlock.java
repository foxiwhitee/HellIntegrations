package foxiwhitee.hellmod.integration.draconic.itemBlock;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.config.HellConfig;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class BlockChaosItemBlock extends ItemBlock implements IEnergyContainerItem {
    protected static int capacity;
    protected static int maxReceive;

    public BlockChaosItemBlock(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    public static void update() {
        capacity = HellConfig.capacityChaos;
        maxReceive = HellConfig.maxReceiveChaos;
    }

    public void getSubItems(Item item, CreativeTabs p_150895_2_, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    public int getMetadata(int par1) {
        return par1;
    }

    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + stack.getItemDamage();
    }

    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (container.getItemDamage() == 0 && container.stackSize > 0) {
            maxReceive /= container.stackSize;
            if (container.stackTagCompound == null) {
                container.stackTagCompound = new NBTTagCompound();
            }

            int energy = container.stackTagCompound.getInteger("Energy");
            int energyReceived = Math.min(capacity - energy, Math.min(maxReceive, maxReceive));
            if (!simulate) {
                energy += energyReceived;
                container.stackTagCompound.setInteger("Energy", energy);
            }

            if (this.getEnergyStored(container) == this.getMaxEnergyStored(container)) {
                container.setItemDamage(1);
                container.setTagCompound((NBTTagCompound)null);
            }

            return energyReceived * container.stackSize;
        } else {
            return 0;
        }
    }

    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        return 0;
    }

    public int getEnergyStored(ItemStack container) {
        return container.stackTagCompound != null && container.stackTagCompound.hasKey("Energy") ? container.stackTagCompound.getInteger("Energy") : 0;
    }

    public int getMaxEnergyStored(ItemStack container) {
        return capacity;
    }

    public void onUpdate(ItemStack stack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        if (stack.getItemDamage() == 0 && this.getEnergyStored(stack) == this.getMaxEnergyStored(stack)) {
            stack.setItemDamage(1);
        }

        super.onUpdate(stack, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
        if (stack.hasTagCompound()) {
            list.add(this.getEnergyStored(stack) + " / " + this.getMaxEnergyStored(stack) + "RF");
        }

    }
}
