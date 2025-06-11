package foxiwhitee.hellmod.parts;

import appeng.core.sync.GuiBridge;
import appeng.tile.inventory.AppEngInternalInventory;
import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public abstract class PartCraftingTerminal extends PartTerminal{
    public PartCraftingTerminal(ItemStack is) {
        super(is);
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.getInventoryCrafting().readFromNBT(data, "crafting");
    }

    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        this.getInventoryCrafting().writeToNBT(data, "crafting");
    }

    public IInventory getInventoryByName(String name) {
        return name.equals("crafting") ? this.getInventoryCrafting() : super.getInventoryByName(name);
    }

    public void getDrops(List<ItemStack> drops, boolean wrenched) {
        super.getDrops(drops, wrenched);
        for (ItemStack is : this.getInventoryCrafting()) {
            if (is != null)
                drops.add(is);
        }
    }

    public GuiBridge getGui(EntityPlayer p) {
        int x = (int)p.posX;
        int y = (int)p.posY;
        int z = (int)p.posZ;
        if (getHost().getTile() != null) {
            x = (getTile()).xCoord;
            y = (getTile()).yCoord;
            z = (getTile()).zCoord;
        }
        if (getThisGui().hasPermissions(getHost().getTile(), x, y, z, getSide(), p))
            return getThisGui();
        return GuiBridge.GUI_ME;
    }

    abstract public AppEngInternalInventory getInventoryCrafting();

}
