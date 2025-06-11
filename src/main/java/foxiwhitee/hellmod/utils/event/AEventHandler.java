package foxiwhitee.hellmod.utils.event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.*;

import java.util.Iterator;

public class AEventHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingAttack(LivingAttackEvent event) {
        if (event.entityLiving instanceof EntityPlayer)
            ACustomArmorHandler.onPlayerAttacked(event);
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.entityLiving instanceof EntityPlayer)
            ACustomArmorHandler.onPlayerHurt(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.entityLiving instanceof EntityPlayer)
            ACustomArmorHandler.onPlayerDeath(event);
    }

    @SubscribeEvent
    public void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            ACustomArmorHandler.ArmorSummery summery = (new ACustomArmorHandler.ArmorSummery()).getSummery(player);
            if (summery != null && summery.jumpModifier > 0.0F)
                player.motionY += (summery.jumpModifier * 0.1F);
        }
    }

}
