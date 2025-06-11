package foxiwhitee.hellmod.utils.interfaces;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.helpers.DualityInterface;
import appeng.helpers.IInterfaceHost;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CustomAdvancedDualityInterface extends CustomDualityInterface {
    public static int getNumberOfPatternSlots() {
        return 18;
    }

    @Override
    protected String getLevel() {
        return "advanced";
    }

    public CustomAdvancedDualityInterface(AENetworkProxy networkProxy, IInterfaceHost ih) {
        super(networkProxy, ih);
    }
}
