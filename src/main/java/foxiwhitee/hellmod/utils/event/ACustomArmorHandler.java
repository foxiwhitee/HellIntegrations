package foxiwhitee.hellmod.utils.event;

import cofh.api.energy.IEnergyContainerItem;
import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.common.handler.BalanceConfigHandler;
import com.brandon3055.draconicevolution.common.handler.ConfigHandler;
import com.brandon3055.draconicevolution.common.items.armor.ICustomArmor;
import com.brandon3055.draconicevolution.common.network.ShieldHitPacket;
import com.brandon3055.draconicevolution.common.utills.IUpgradableItem;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.items.armors.ArialArmor;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.*;

public class ACustomArmorHandler {
    public static final UUID WALK_SPEED_UUID = UUID.fromString("0ea6ce8e-d2e8-11e5-ab30-625662870761");

    private static final DamageSource ADMIN_KILL = (new DamageSource("administrative.kill")).setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute();

    public static Map<EntityPlayer, Boolean> playersWithFlight = new WeakHashMap<>();

    public static List<String> playersWithUphillStep = new ArrayList<>();

    public static void onPlayerHurt(LivingHurtEvent event) {}

    public static void onPlayerAttacked(LivingAttackEvent event) {
        if (event.isCanceled())
            return;
        EntityPlayer player = (EntityPlayer)event.entityLiving;
        ArmorSummery summery = (new ArmorSummery()).getSummery(player);
        float hitAmount = ArmorUtils.applyModDamageAdjustments(summery, event);
        if (applyArmorDamageBlocking(event, summery))
            return;
        if (summery == null || summery.protectionPoints <= 0.0F || event.source == ADMIN_KILL)
            return;
        event.setCanceled(true);
        if (hitAmount == Float.MAX_VALUE && !event.source.damageType.equals(ADMIN_KILL.damageType)) {
            player.attackEntityFrom(ADMIN_KILL, Float.MAX_VALUE);
            return;
        }
        if (player.hurtResistantTime > player.maxHurtResistantTime / 2.0F)
            return;
        float newEntropy = Math.min(summery.entropy + 1.0F + hitAmount / 20.0F, 100.0F);
        float totalAbsorbed = 0.0F;
        int remainingPoints = 0;
        for (int i = 0; i < summery.allocation.length; i++) {
            if (summery.allocation[i] != 0.0F) {
                ItemStack armorPeace = summery.armorStacks[i];
                float dmgShear = summery.allocation[i] / summery.protectionPoints;
                float dmg = dmgShear * hitAmount;
                float absorbed = Math.min(dmg, summery.allocation[i]);
                totalAbsorbed += absorbed;
                summery.allocation[i] = summery.allocation[i] - absorbed;
                remainingPoints = (int)(remainingPoints + summery.allocation[i]);
                ItemNBTHelper.setFloat(armorPeace, "ProtectionPoints", summery.allocation[i]);
                ItemNBTHelper.setFloat(armorPeace, "ShieldEntropy", newEntropy);
            }
        }
        if (summery.protectionPoints > 0.0F) {
            DraconicEvolution.network.sendToAllAround((IMessage)new ShieldHitPacket(player, summery.protectionPoints / summery.maxProtectionPoints), new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64.0D));
            player.worldObj.playSoundEffect(player.posX + 0.5D, player.posY + 0.5D, player.posZ + 0.5D, "draconicevolution:shieldStrike", 0.9F, player.worldObj.rand.nextFloat() * 0.1F + 1.055F);
        }
        if (remainingPoints > 0) {
            player.hurtResistantTime = 20;
        } else if (hitAmount - totalAbsorbed > 0.0F) {
            player.attackEntityFrom(event.source, hitAmount - totalAbsorbed);
        }
    }

    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled())
            return;
        EntityPlayer player = (EntityPlayer)event.entityLiving;
        ArmorSummery summery = (new ArmorSummery()).getSummery(player);
        if (summery == null || event.source == ADMIN_KILL)
            return;
        if (summery.protectionPoints > 500.0F) {
            event.setCanceled(true);
            event.entityLiving.setHealth(10.0F);
            return;
        }
        if (!summery.hasArial)
            return;
        int[] charge = new int[summery.armorStacks.length];
        long totalCharge = 0L;
        int i;
        for (i = 0; i < summery.armorStacks.length; i++) {
            if (summery.armorStacks[i] != null) {
                charge[i] = ((IEnergyContainerItem)summery.armorStacks[i].getItem()).getEnergyStored(summery.armorStacks[i]);
                totalCharge += charge[i];
            }
        }
        if (totalCharge < HellConfig.arialArmorBaseStorage)
            return;
        for (i = 0; i < summery.armorStacks.length; i++) {
            if (summery.armorStacks[i] != null)
                ((IEnergyContainerItem)summery.armorStacks[i].getItem()).extractEnergy(summery.armorStacks[i], (int)(charge[i] / totalCharge * HellConfig.arialArmorBaseStorage), false);
        }
        player.addChatComponentMessage((new ChatComponentTranslation("msg.de.shieldDepleted.txt", new Object[0])).setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.DARK_RED)));
        event.setCanceled(true);
        player.setHealth(2.0F);
    }

    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        ArmorSummery summery = (new ArmorSummery()).getSummery(player);
        tickShield(summery, player);
        tickArmorEffects(summery, player);
    }

    public static void tickShield(ArmorSummery summery, EntityPlayer player) {
        if (summery == null || ((summery.maxProtectionPoints - summery.protectionPoints) < 0.01D && summery.entropy == 0.0F) || player.worldObj.isRemote)
            return;
        float totalPointsToAdd = Math.min(summery.maxProtectionPoints - summery.protectionPoints, summery.maxProtectionPoints / 60.0F);
        totalPointsToAdd *= 1.0F - summery.entropy / 100.0F;
        totalPointsToAdd = Math.min(totalPointsToAdd, (float)(summery.totalEnergyStored / (summery.hasArial ? HellConfig.arialArmorEnergyPerProtectionPoint : BalanceConfigHandler.wyvernArmorEnergyPerProtectionPoint)));
        if (totalPointsToAdd < 0.0F)
            totalPointsToAdd = 0.0F;
        summery.entropy -= summery.meanRecoveryPoints * 0.01F;
        if (summery.entropy < 0.0F)
            summery.entropy = 0.0F;
        for (int i = 0; i < summery.armorStacks.length; i++) {
            ItemStack stack = summery.armorStacks[i];
            if (stack != null && summery.totalEnergyStored > 0L) {
                float maxForPeace = ((ICustomArmor)stack.getItem()).getProtectionPoints(stack);
                int energyAmount = ((ICustomArmor)summery.armorStacks[i].getItem()).getEnergyPerProtectionPoint();
                ((IEnergyContainerItem)stack.getItem()).extractEnergy(stack, (int)(summery.energyAllocation[i] / summery.totalEnergyStored * (totalPointsToAdd * energyAmount)), false);
                float pointsForPeace = summery.pointsDown[i] / Math.max(1.0F, summery.maxProtectionPoints - summery.protectionPoints) * totalPointsToAdd;
                summery.allocation[i] = summery.allocation[i] + pointsForPeace;
                if (summery.allocation[i] > maxForPeace || maxForPeace - summery.allocation[i] < 0.1F)
                    summery.allocation[i] = maxForPeace;
                ItemNBTHelper.setFloat(stack, "ProtectionPoints", summery.allocation[i]);
                if (player.hurtResistantTime <= 0)
                    ItemNBTHelper.setFloat(stack, "ShieldEntropy", summery.entropy);
            }
        }
    }

    public static void tickArmorEffects(ArmorSummery summery, EntityPlayer player) {
        if (ConfigHandler.enableFlight)
            if (summery != null && summery.flight[0]) {
                playersWithFlight.put(player, Boolean.valueOf(true));
                player.capabilities.allowFlying = true;
                if (summery.flight[1])
                    player.capabilities.isFlying = true;
                if (player.worldObj.isRemote)
                    setPlayerFlySpeed(player, 0.05F + 0.05F * summery.flightSpeedModifier);
                if (!player.onGround && player.capabilities.isFlying && player.motionY != 0.0D && summery.flightVModifier > 0.0F) {
                    if (BrandonsCore.proxy.isSpaceDown() && !BrandonsCore.proxy.isShiftDown())
                        player.motionY = (0.225F * summery.flightVModifier);
                    if (BrandonsCore.proxy.isShiftDown() && !BrandonsCore.proxy.isSpaceDown())
                        player.motionY = (-0.225F * summery.flightVModifier);
                }
                if (summery.flight[2] && player.moveForward == 0.0F && player.moveStrafing == 0.0F && player.capabilities.isFlying) {
                    player.motionX *= 0.5D;
                    player.motionZ *= 0.5D;
                }
            } else {
                if (!playersWithFlight.containsKey(player))
                    playersWithFlight.put(player, Boolean.valueOf(false));
                if (((Boolean)playersWithFlight.get(player)).booleanValue() && !player.worldObj.isRemote) {
                    playersWithFlight.put(player, Boolean.valueOf(false));
                    if (!player.capabilities.isCreativeMode) {
                        player.capabilities.allowFlying = false;
                        player.capabilities.isFlying = false;
                        player.sendPlayerAbilities();
                    }
                }
                if (player.worldObj.isRemote && ((Boolean)playersWithFlight.get(player)).booleanValue()) {
                    playersWithFlight.put(player, Boolean.valueOf(false));
                    if (!player.capabilities.isCreativeMode) {
                        player.capabilities.allowFlying = false;
                        player.capabilities.isFlying = false;
                    }
                    setPlayerFlySpeed(player, 0.05F);
                }
            }
        IAttribute speedAttr = SharedMonsterAttributes.movementSpeed;
        if (summery != null && summery.speedModifier > 0.0F) {
            double value = summery.speedModifier;
            if (player.getEntityAttribute(speedAttr).getModifier(WALK_SPEED_UUID) == null) {
                player.getEntityAttribute(speedAttr).applyModifier(new AttributeModifier(WALK_SPEED_UUID, speedAttr.getAttributeUnlocalizedName(), value, 1));
            } else if (player.getEntityAttribute(speedAttr).getModifier(WALK_SPEED_UUID).getAmount() != value) {
                player.getEntityAttribute(speedAttr).removeModifier(player.getEntityAttribute(speedAttr).getModifier(WALK_SPEED_UUID));
                player.getEntityAttribute(speedAttr).applyModifier(new AttributeModifier(WALK_SPEED_UUID, speedAttr.getAttributeUnlocalizedName(), value, 1));
            }
            if (!player.onGround && player.ridingEntity == null)
                player.jumpMovementFactor = 0.02F + 0.02F * summery.speedModifier;
        } else if (player.getEntityAttribute(speedAttr).getModifier(WALK_SPEED_UUID) != null) {
            player.getEntityAttribute(speedAttr).removeModifier(player.getEntityAttribute(speedAttr).getModifier(WALK_SPEED_UUID));
        }
        if (summery != null && player.worldObj.isRemote) {
            boolean highStepListed = (playersWithUphillStep.contains(player.getDisplayName()) && player.stepHeight >= 1.0F);
            boolean hasHighStep = summery.hasHillStep;
            if (hasHighStep && !highStepListed) {
                playersWithUphillStep.add(player.getDisplayName());
                player.stepHeight = 1.0F;
            }
            if (!hasHighStep && highStepListed) {
                playersWithUphillStep.remove(player.getDisplayName());
                player.stepHeight = 0.5F;
            }
        }
    }

    private static void setPlayerFlySpeed(EntityPlayer player, float speed) {
        player.capabilities.setFlySpeed(speed);
    }

    private static boolean applyArmorDamageBlocking(LivingAttackEvent event, ArmorSummery summery) {
        if (summery == null)
            return false;
        if (event.source.isFireDamage() && summery.fireResistance >= 1.0F) {
            event.setCanceled(true);
            event.entityLiving.extinguish();
            return true;
        }
        if (event.source.damageType.equals("fall") && summery.jumpModifier > 0.0F) {
            if (event.ammount < summery.jumpModifier * 5.0F)
                event.setCanceled(true);
            return true;
        }
        if ((event.source.damageType.equals("inWall") || event.source.damageType.equals("drown")) && summery.armorStacks[3] != null) {
            if (event.ammount <= 2.0F)
                event.setCanceled(true);
            return true;
        }
        return false;
    }

    public static class ArmorSummery {
        public float maxProtectionPoints = 0.0F;

        public float protectionPoints = 0.0F;

        public int peaces = 0;

        public float[] allocation;

        public float[] pointsDown;

        public ItemStack[] armorStacks;

        public float entropy = 0.0F;

        public int meanRecoveryPoints = 0;

        public long totalEnergyStored = 0L;

        public long maxTotalEnergyStorage = 0L;

        public int[] energyAllocation;

        public boolean[] flight = new boolean[] { false, false, false };

        public float flightVModifier = 0.0F;

        public float speedModifier = 0.0F;

        public float jumpModifier = 0.0F;

        public float fireResistance = 0.0F;

        public float flightSpeedModifier = 0.0F;

        public boolean hasHillStep = false;

        public boolean hasArial = false;

        public ArmorSummery getSummery(EntityPlayer player) {
            ItemStack[] armorSlots = player.inventory.armorInventory;
            float totalEntropy = 0.0F;
            int totalRecoveryPoints = 0;
            this.allocation = new float[armorSlots.length];
            this.armorStacks = new ItemStack[armorSlots.length];
            this.pointsDown = new float[armorSlots.length];
            this.energyAllocation = new int[armorSlots.length];
            for (int i = 0; i < armorSlots.length; i++) {
                ItemStack stack = armorSlots[i];
                if (stack != null && stack.getItem() instanceof ICustomArmor) {
                    ICustomArmor armor = (ICustomArmor)stack.getItem();
                    this.peaces++;
                    this.allocation[i] = ItemNBTHelper.getFloat(stack, "ProtectionPoints", 0.0F);
                    this.protectionPoints += this.allocation[i];
                    totalEntropy += ItemNBTHelper.getFloat(stack, "ShieldEntropy", 0.0F);
                    this.armorStacks[i] = stack;
                    totalRecoveryPoints += IUpgradableItem.EnumUpgrade.SHIELD_RECOVERY.getUpgradePoints(stack);
                    float maxPoints = armor.getProtectionPoints(stack);
                    this.pointsDown[i] = maxPoints - this.allocation[i];
                    this.maxProtectionPoints += maxPoints;
                    this.energyAllocation[i] = armor.getEnergyStored(stack);
                    this.totalEnergyStored += this.energyAllocation[i];
                    this.maxTotalEnergyStorage += armor.getMaxEnergyStored(stack);
                    if (stack.getItem() instanceof ArialArmor)
                        this.hasArial = true;
                    this.fireResistance += armor.getFireResistance(stack);
                    switch (i) {
                        case 2:
                            this.flight = armor.hasFlight(stack);
                            if (this.flight[0]) {
                                this.flightVModifier = armor.getFlightVModifier(stack, player);
                                this.flightSpeedModifier = armor.getFlightSpeedModifier(stack, player);
                            }
                            break;
                        case 1:
                            this.speedModifier = armor.getSpeedModifier(stack, player);
                            break;
                        case 0:
                            this.hasHillStep = armor.hasHillStep(stack, player);
                            this.jumpModifier = armor.getJumpModifier(stack, player);
                            break;
                    }
                }
            }
            if (this.peaces == 0)
                return null;
            this.entropy = totalEntropy / this.peaces;
            this.meanRecoveryPoints = totalRecoveryPoints / this.peaces;
            return this;
        }
    }
}

