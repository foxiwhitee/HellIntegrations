package foxiwhitee.hellmod.utils.event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;

import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CEventHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingAttack(LivingAttackEvent event) {
        if (event.entityLiving instanceof EntityPlayer)
            CCustomArmorHandler.onPlayerAttacked(event);
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.entityLiving instanceof EntityPlayer)
            CCustomArmorHandler.onPlayerHurt(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.entityLiving instanceof EntityPlayer)
            CCustomArmorHandler.onPlayerDeath(event);
    }

    @SubscribeEvent
    public void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            CCustomArmorHandler.ArmorSummery summery = (new CCustomArmorHandler.ArmorSummery()).getSummery(player);
            if (summery != null && summery.jumpModifier > 0.0F)
                player.motionY += (summery.jumpModifier * 0.1F);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onDropEvent(LivingDropsEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity instanceof com.brandon3055.draconicevolution.common.entity.EntityChaosGuardian) {
            Entity attacker = event.source.getEntity();
            if (attacker instanceof EntityPlayer)
                ((EntityPlayer)attacker).inventory.addItemStackToInventory(new ItemStack(DraconicEvolutionIntegration.chaoticHeart));
            Iterator i$ = event.entity.worldObj.playerEntities.iterator();
            while (i$.hasNext()) {
                Object o = i$.next();
                if (o instanceof EntityPlayer)
                    ((EntityPlayer)o).addChatComponentMessage((IChatComponent)new ChatComponentText(LocalizationUtils.localize("msg.de.dragonDeath.txt")));
            }
        }
    }
}
