package foxiwhitee.hellmod.integration.draconic.items.tools;

import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.brandonscore.common.utills.Utills;
import com.brandon3055.draconicevolution.common.handler.ConfigHandler;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.MiningTool;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.ToolBase;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.ToolHandler;
import com.brandon3055.draconicevolution.common.utills.ItemConfigField;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;

import java.util.*;

public abstract class CMiningTool extends ToolBase implements ICustomUpgradableItem {
    public CMiningTool(Item.ToolMaterial material) {
        super(0.0F, material, (Set)null);
    }

    public Map<Block, Integer> getObliterationList(ItemStack stack) {
        Map<Block, Integer> blockMap = new HashMap();
        NBTTagCompound compound = ItemNBTHelper.getCompound(stack);
        if (compound.hasNoTags()) {
            return blockMap;
        } else {
            for(int i = 0; i < 9; ++i) {
                NBTTagCompound tag = new NBTTagCompound();
                if (compound.hasKey("Item" + i)) {
                    tag = compound.getCompoundTag("Item" + i);
                }

                if (!tag.hasNoTags()) {
                    ItemStack stack1 = ItemStack.loadItemStackFromNBT(tag);
                    if (stack1 != null && stack1.getItem() instanceof ItemBlock) {
                        blockMap.put(Block.getBlockFromItem(stack1.getItem()), stack1.getItemDamage());
                    }
                }
            }

            return blockMap;
        }
    }

    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
        int radius = ProfileHelper.getInteger(stack, "ToolDigAOE", 0);
        int depth = ProfileHelper.getInteger(stack, "ToolDigDepth", 1) - 1;
        return this.getEnergyStored(stack) >= this.energyPerOperation && radius > 0 ? this.breakAOEBlocks(stack, x, y, z, radius, depth, player) : super.onBlockStartBreak(stack, x, y, z, player);
    }

    public boolean onBlockDestroyed(ItemStack stack, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_) {
        if (ProfileHelper.getInteger(stack, "ToolDigAOE", 0) == 0) {
            this.extractEnergy(stack, this.energyPerOperation, false);
        }

        return super.onBlockDestroyed(stack, p_150894_2_, p_150894_3_, p_150894_4_, p_150894_5_, p_150894_6_, p_150894_7_);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        ToolHandler.updateGhostBlocks(player, world);
        return super.onItemRightClick(stack, world, player);
    }

    public boolean breakAOEBlocks(ItemStack stack, int x, int y, int z, int breakRadius, int breakDepth, EntityPlayer player) {
        Map<Block, Integer> blockMap = (Map<Block, Integer>)(ProfileHelper.getBoolean(stack, "ToolVoidJunk", false) ? this.getObliterationList(stack) : new HashMap());
        Block block = player.worldObj.getBlock(x, y, z);
        int meta = player.worldObj.getBlockMetadata(x, y, z);
        boolean effective = false;
        if (block != null) {
            for(String s : this.getToolClasses(stack)) {
                if (block.isToolEffective(s, meta) || this.func_150893_a(stack, block) > 1.0F) {
                    effective = true;
                }
            }
        }

        if (!effective) {
            return true;
        } else {
            float refStrength = ForgeHooks.blockStrength(block, player, player.worldObj, x, y, z);
            MovingObjectPosition mop = ToolHandler.raytraceFromEntity(player.worldObj, player, (double)4.5F);
            if (mop == null) {
                ToolHandler.updateGhostBlocks(player, player.worldObj);
                return true;
            } else {
                int sideHit = mop.sideHit;
                int xMax = breakRadius;
                int xMin = breakRadius;
                int yMax = breakRadius;
                int yMin = breakRadius;
                int zMax = breakRadius;
                int zMin = breakRadius;
                int yOffset = 0;
                switch (sideHit) {
                    case 0:
                        yMax = breakDepth;
                        yMin = 0;
                        zMax = breakRadius;
                        break;
                    case 1:
                        yMin = breakDepth;
                        yMax = 0;
                        zMax = breakRadius;
                        break;
                    case 2:
                        xMax = breakRadius;
                        zMin = 0;
                        zMax = breakDepth;
                        yOffset = breakRadius - 1;
                        break;
                    case 3:
                        xMax = breakRadius;
                        zMax = 0;
                        zMin = breakDepth;
                        yOffset = breakRadius - 1;
                        break;
                    case 4:
                        xMax = breakDepth;
                        xMin = 0;
                        zMax = breakRadius;
                        yOffset = breakRadius - 1;
                        break;
                    case 5:
                        xMin = breakDepth;
                        xMax = 0;
                        zMax = breakRadius;
                        yOffset = breakRadius - 1;
                }

                if (ProfileHelper.getBoolean(stack, "BaseSafeAOE", false)) {
                    for(int xPos = x - xMin; xPos <= x + xMax; ++xPos) {
                        for(int yPos = y + yOffset - yMin; yPos <= y + yOffset + yMax; ++yPos) {
                            for(int zPos = z - zMin; zPos <= z + zMax; ++zPos) {
                                if (player.worldObj.getTileEntity(xPos, yPos, zPos) != null) {
                                    if (player.worldObj.isRemote) {
                                        player.addChatComponentMessage(new ChatComponentTranslation("msg.de.baseSafeAOW.txt", new Object[0]));
                                    } else {
                                        ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, ((EntityPlayerMP)player).worldObj));
                                    }

                                    return true;
                                }
                            }
                        }
                    }
                }

                for(int xPos = x - xMin; xPos <= x + xMax; ++xPos) {
                    for(int yPos = y + yOffset - yMin; yPos <= y + yOffset + yMax; ++yPos) {
                        for(int zPos = z - zMin; zPos <= z + zMax; ++zPos) {
                            this.breakExtraBlock(stack, player.worldObj, xPos, yPos, zPos, breakRadius * (breakDepth / 2 + 1), player, refStrength, Math.abs(x - xPos) <= 1 && Math.abs(y - yPos) <= 1 && Math.abs(z - zPos) <= 1, blockMap);
                        }
                    }
                }

                for(Object o : player.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox((double)(x - xMin), (double)(y + yOffset - yMin), (double)(z - zMin), (double)(x + xMax + 1), (double)(y + yOffset + yMax + 1), (double)(z + zMax + 1)))) {
                    if (!player.worldObj.isRemote) {
                        EntityItem item = (EntityItem)o;
                        item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                        ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(item));
                        item.delayBeforeCanPickup = 0;
                        if (ConfigHandler.rapidlyDespawnMinedItems) {
                            item.lifespan = 100;
                        }
                    }
                }

                return true;
            }
        }
    }

    protected void breakExtraBlock(ItemStack stack, World world, int x, int y, int z, int totalSize, EntityPlayer player, float refStrength, boolean breakSound, Map<Block, Integer> blockMap) {
        if (!world.isAirBlock(x, y, z)) {
            Block block = world.getBlock(x, y, z);
            if (!(block.getMaterial() instanceof MaterialLiquid) && (block.getBlockHardness(world, x, y, x) != -1.0F || player.capabilities.isCreativeMode)) {
                int meta = world.getBlockMetadata(x, y, z);
                boolean effective = false;

                for(String s : this.getToolClasses(stack)) {
                    if (block.isToolEffective(s, meta) || this.func_150893_a(stack, block) > 1.0F) {
                        effective = true;
                    }
                }

                if (effective) {
                    float strength = ForgeHooks.blockStrength(block, player, world, x, y, z);
                    if (player.canHarvestBlock(block) && ForgeHooks.canHarvestBlock(block, player, meta) && (!(refStrength / strength > 10.0F) || player.capabilities.isCreativeMode)) {
                        if (!world.isRemote) {
                            BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world, world.getWorldInfo().getGameType(), (EntityPlayerMP)player, x, y, z);
                            if (event.isCanceled()) {
                                ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
                                return;
                            }
                        }

                        int scaledPower = this.energyPerOperation + totalSize * (this.energyPerOperation / 10);
                        if (player.capabilities.isCreativeMode || blockMap.containsKey(block) && (Integer)blockMap.get(block) == meta) {
                            block.onBlockHarvested(world, x, y, z, meta, player);
                            if (block.removedByPlayer(world, player, x, y, z, false)) {
                                block.onBlockDestroyedByPlayer(world, x, y, z, meta);
                            }

                            if (!world.isRemote) {
                                ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
                            }

                            if (blockMap.containsKey(block) && (Integer)blockMap.get(block) == meta) {
                                this.extractEnergy(stack, scaledPower, false);
                            }

                            if (breakSound) {
                                world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
                            }

                        } else {
                            this.extractEnergy(stack, scaledPower, false);
                            if (!world.isRemote) {
                                block.onBlockHarvested(world, x, y, z, meta, player);
                                if (block.removedByPlayer(world, player, x, y, z, true)) {
                                    block.onBlockDestroyedByPlayer(world, x, y, z, meta);
                                    block.harvestBlock(world, player, x, y, z, meta);
                                    player.addExhaustion(-0.025F);
                                    if (block.getExpDrop(world, meta, EnchantmentHelper.getFortuneModifier(player)) > 0) {
                                        player.addExperience(block.getExpDrop(world, meta, EnchantmentHelper.getFortuneModifier(player)));
                                    }
                                }

                                EntityPlayerMP mpPlayer = (EntityPlayerMP)player;
                                mpPlayer.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
                            } else {
                                if (breakSound) {
                                    world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
                                }

                                if (block.removedByPlayer(world, player, x, y, z, true)) {
                                    block.onBlockDestroyedByPlayer(world, x, y, z, meta);
                                }

                                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(2, x, y, z, Minecraft.getMinecraft().objectMouseOver.sideHit));
                            }

                        }
                    }
                }
            }
        }
    }

    public List<ICustomUpgradableItem.EnumUpgrade> getCUpgrades(ItemStack itemstack) {
        return new ArrayList<EnumUpgrade>() {
            {
                this.add(EnumUpgrade.RF_CAPACITY);
                this.add(EnumUpgrade.DIG_SPEED);
                this.add(EnumUpgrade.DIG_AOE);
                this.add(EnumUpgrade.DIG_DEPTH);
            }
        };
    }

    public int getMaxUpgradePoints(int upgradeIndex, ItemStack stack) {
        return this.getMaxUpgradePoints(upgradeIndex);
    }

    public float getEfficiency(ItemStack stack) {
        int i = EnumUpgrade.DIG_SPEED.getUpgradePoints(stack);
        return i == 0 ? super.getEfficiency(stack) : (float)i * 3.0F;
    }

    public List<String> getUpgradeStats(ItemStack stack) {
        List<String> strings = new ArrayList();
        int digaoe = 0;
        int depth = 0;
        int attackaoe = 0;

        for(ItemConfigField field : this.getFields(stack, 0)) {
            if (field.name.equals("ToolDigAOE")) {
                digaoe = 1 + (Integer)field.max * 2;
            } else if (field.name.equals("ToolDigDepth")) {
                depth = (Integer)field.max;
            } else if (field.name.equals("WeaponAttackAOE")) {
                attackaoe = 1 + (Integer)field.max * 2;
            }
        }

        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.RFCapacity.txt") + ": " + InfoHelper.HITC() + Utills.formatNumber((long)this.getMaxEnergyStored(stack)));
        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.max.txt") + " " + LocalizationUtils.localize("gui.de.DigAOE.txt") + ": " + InfoHelper.HITC() + digaoe + "x" + digaoe);
        if (depth > 0) {
            strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.max.txt") + " " + LocalizationUtils.localize("gui.de.DigDepth.txt") + ": " + InfoHelper.HITC() + depth);
        }

        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.max.txt") + " " + LocalizationUtils.localize("gui.de.DigSpeed.txt") + ": " + InfoHelper.HITC() + this.getEfficiency(stack));
        if (attackaoe > 0) {
            strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.max.txt") + " " + LocalizationUtils.localize("gui.de.AttackAOE.txt") + ": " + InfoHelper.HITC() + attackaoe + "x" + attackaoe);
        }

        return strings;
    }
}
