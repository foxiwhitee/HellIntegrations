package foxiwhitee.hellmod.utils.event;


import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class ArmorUtils {
    public static boolean isAvaritiaInstalled;

    private static Item avaritiaSword;

    public static void init() {
        isAvaritiaInstalled = Loader.isModLoaded("Avaritia");
    }

    public static boolean isHoldingAvaritiaSword(EntityPlayer player) {
        if (!isAvaritiaInstalled)
            return false;
        if (avaritiaSword == null)
            avaritiaSword = GameRegistry.findItem("Avaritia", "Infinity_Sword");
        return (avaritiaSword != null && player.getHeldItem() != null && player.getHeldItem().getItem().equals(avaritiaSword));
    }

    public static float applyModDamageAdjustments(CCustomArmorHandler.ArmorSummery summery, LivingAttackEvent event) {
        if (summery == null)
            return event.ammount;
        EntityPlayer attacker = (event.source.getEntity() instanceof EntityPlayer) ? (EntityPlayer)event.source.getEntity() : null;
        if (attacker == null)
            return event.ammount;
        if (isHoldingAvaritiaSword(attacker)) {
            event.entityLiving.hurtResistantTime = 0;
            return 300.0F;
        }
        if (!event.source.isUnblockable() && !event.source.canHarmInCreative())
            return event.ammount;
        summery.entropy += 3.0F;
        if (summery.entropy > 100.0F)
            summery.entropy = 100.0F;
        return event.ammount * 2.0F;
    }

    public static float applyModDamageAdjustments(ACustomArmorHandler.ArmorSummery summery, LivingAttackEvent event) {
        if (summery == null)
            return event.ammount;
        EntityPlayer attacker = (event.source.getEntity() instanceof EntityPlayer) ? (EntityPlayer)event.source.getEntity() : null;
        if (attacker == null)
            return event.ammount;
        if (isHoldingAvaritiaSword(attacker)) {
            event.entityLiving.hurtResistantTime = 0;
            return 300.0F;
        }
        if (!event.source.isUnblockable() && !event.source.canHarmInCreative())
            return event.ammount;
        summery.entropy += 3.0F;
        if (summery.entropy > 100.0F)
            summery.entropy = 100.0F;
        return event.ammount * 2.0F;
    }


}

