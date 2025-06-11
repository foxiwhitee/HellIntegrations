package foxiwhitee.hellmod.items;

import appeng.api.config.AccessRestriction;
import appeng.api.config.PowerUnits;
import appeng.api.definitions.IBlockDefinition;
import appeng.api.implementations.items.IAEItemPowerStorage;
import appeng.block.AEBaseItemBlock;
import appeng.core.Api;
import appeng.core.localization.GuiText;
import appeng.util.Platform;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.blocks.BlockCustomEnergyCell;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

public class ItemBlockCustomEnergyCell extends AEBaseItemBlock implements IAEItemPowerStorage {
    public ItemBlockCustomEnergyCell(Block id) {
        super(id);
    }

    @SideOnly(Side.CLIENT)
    public void addCheckedInformation(ItemStack itemStack, EntityPlayer player, List<String> toolTip, boolean advancedTooltips) {
        NBTTagCompound tag = itemStack.getTagCompound();
        double internalCurrentPower = (double)0.0F;
        double internalMaxPower = this.getMaxEnergyCapacity();
        if (tag != null) {
            internalCurrentPower = tag.getDouble("internalCurrentPower");
        }

        double percent = internalCurrentPower / internalMaxPower;
        toolTip.add(GuiText.StoredEnergy.getLocal() + ':' + MessageFormat.format(" {0,number,#} ", internalCurrentPower) + Platform.gui_localize(PowerUnits.AE.unlocalizedName) + " - " + MessageFormat.format(" {0,number,#.##%} ", percent));
    }

    private double getMaxEnergyCapacity() {
        Block blockID = Block.getBlockFromItem(this);
        if (blockID instanceof BlockCustomEnergyCell) {
            return ((BlockCustomEnergyCell)blockID).getMaxPower();
        }
        return 0;
    }

    public double injectAEPower(ItemStack is, double amt) {
        double internalCurrentPower = this.getInternal(is);
        double internalMaxPower = this.getMaxEnergyCapacity();
        internalCurrentPower += amt;
        if (internalCurrentPower > internalMaxPower) {
            amt = internalCurrentPower - internalMaxPower;
            this.setInternal(is, internalMaxPower);
            return amt;
        } else {
            this.setInternal(is, internalCurrentPower);
            return (double)0.0F;
        }
    }

    private double getInternal(ItemStack is) {
        NBTTagCompound nbt = Platform.openNbtData(is);
        return nbt.getDouble("internalCurrentPower");
    }

    private void setInternal(ItemStack is, double amt) {
        NBTTagCompound nbt = Platform.openNbtData(is);
        nbt.setDouble("internalCurrentPower", amt);
    }

    public double extractAEPower(ItemStack is, double amt) {
        double internalCurrentPower = this.getInternal(is);
        if (internalCurrentPower > amt) {
            internalCurrentPower -= amt;
            this.setInternal(is, internalCurrentPower);
            return amt;
        } else {
            this.setInternal(is, (double)0.0F);
            return internalCurrentPower;
        }
    }

    public double getAEMaxPower(ItemStack is) {
        return this.getMaxEnergyCapacity();
    }

    public double getAECurrentPower(ItemStack is) {
        return this.getInternal(is);
    }

    public AccessRestriction getPowerFlow(ItemStack is) {
        return AccessRestriction.WRITE;
    }
}
