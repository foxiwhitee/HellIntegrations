package foxiwhitee.hellmod.utils.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class CommonEventHandler {
    public static int serverTicks = 0;

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            serverTicks++;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            CCustomArmorHandler.onPlayerTick(event);
            ACustomArmorHandler.onPlayerTick(event);
        }
    }

    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.onGround) {
            CCustomArmorHandler.ArmorSummery summery = (new CCustomArmorHandler.ArmorSummery()).getSummery(event.player);
            if (summery != null && summery.flight[0]) {
                event.player.capabilities.isFlying = true;
                event.player.sendPlayerAbilities();
            }
            ACustomArmorHandler.ArmorSummery summeryA = (new ACustomArmorHandler.ArmorSummery()).getSummery(event.player);
            if (summeryA != null && summeryA.flight[0]) {
                event.player.capabilities.isFlying = true;
                event.player.sendPlayerAbilities();
            }

        }
    }
}
