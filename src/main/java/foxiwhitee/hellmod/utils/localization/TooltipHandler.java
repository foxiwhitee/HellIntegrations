package foxiwhitee.hellmod.utils.localization;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;

public class TooltipHandler {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        List<String> newTooltip = new ArrayList<>();
        for (String line : event.toolTip) {
            if (line.contains("#")) {
                String hex = line.substring(line.indexOf("#"), line.indexOf("#") + 7);
                //line = line.replace(hex, "§r");
                newTooltip.add(line);
            } else {
                newTooltip.add(line);
            }
        }
        event.toolTip.clear();
        event.toolTip.addAll(newTooltip);
    }
}
