package foxiwhitee.hellmod.event;

import appeng.me.storage.CellInventory;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import foxiwhitee.hellmod.items.ItemFluidDrop;
import foxiwhitee.hellmod.utils.cells.CustomCellInventory;
import net.minecraft.item.Item;
import net.minecraftforge.event.world.WorldEvent;

public class ServerEventHandler {
    public static final foxiwhitee.hellmod.integration.botania.event.ServerEventHandler INSTANCE = new foxiwhitee.hellmod.integration.botania.event.ServerEventHandler();

    @SubscribeEvent
    public final void worldLoad(WorldEvent.Load e) {
        CellInventory.addBasicBlackList(Item.getIdFromItem(ItemFluidDrop.DROP), 32767);
        CustomCellInventory.addBasicBlackList(Item.getIdFromItem(ItemFluidDrop.DROP), 32767);
    }
}
