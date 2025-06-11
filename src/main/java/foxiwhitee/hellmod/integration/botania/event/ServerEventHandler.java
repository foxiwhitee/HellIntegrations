package foxiwhitee.hellmod.integration.botania.event;

import appeng.me.storage.CellInventory;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.utils.cells.CustomCellInventory;
import net.minecraft.item.Item;
import net.minecraftforge.event.world.WorldEvent;

public class ServerEventHandler {
    public static final ServerEventHandler INSTANCE = new ServerEventHandler();

    @SubscribeEvent
    public final void worldLoad(WorldEvent.Load e) {
        CellInventory.addBasicBlackList(Item.getIdFromItem(BotaniaIntegration.mana_drop), 32767);
        CustomCellInventory.addBasicBlackList(Item.getIdFromItem(BotaniaIntegration.mana_drop), 32767);
    }
}
