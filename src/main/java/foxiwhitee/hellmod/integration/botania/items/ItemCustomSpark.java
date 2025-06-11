package foxiwhitee.hellmod.integration.botania.items;

import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.botania.BotaniaIntegration;
import foxiwhitee.hellmod.integration.botania.entity.*;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class ItemCustomSpark  extends Item implements ICraftAchievement, IManaGivingItem {
    private String name;
    private int manaPerSec;
    public ItemCustomSpark(String name, int manaPerSec) {
        this.name = name;
        this.manaPerSec = manaPerSec;
        setUnlocalizedName(name);
        setCreativeTab(HellCore.HELL_TAB);
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xv, float yv, float zv) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof ISparkAttachable) {
            ISparkAttachable attach = (ISparkAttachable)tile;
            if (attach.canAttachSpark(stack) && attach.getAttachedSpark() == null) {
                stack.stackSize--;
                if (!world.isRemote) {
                    CustomSpark spark;
                    switch (name) {
                        case "asgardSpark": spark = new AsgardSpark(world); break;
                        case "helhelmSpark": spark = new HelhelmSpark(world); break;
                        case "valhallaSpark": spark = new ValhallaSpark(world); break;
                        default: spark = new MidgardSpark(world);
                    }
                    spark.setPosition(x + 0.5D, y + 1.5D, z + 0.5D);
                    world.spawnEntityInWorld((Entity)spark);
                    attach.attachSpark((ISparkEntity)spark);
                    VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, x, y, z);
                }
                return true;
            }
        }
        return false;
    }

    public void registerIcons(IIconRegister par1IconRegister) {}

    public IIcon getIconFromDamage(int meta) {
        return ModItems.spark.getIconFromDamage(0);
    }

    public int getColorFromItemStack(ItemStack stack, int pass) {
        switch (name) {
            case "asgardSpark": return BotaniaIntegration.sparkColorAsgard;
            case "helhelmSpark": return BotaniaIntegration.sparkColorHelhelm;
            case "valhallaSpark": return BotaniaIntegration.sparkColorValhalla;
            default: return BotaniaIntegration.sparkColorMidgard;
        }
        //return 0;
    }

    public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
        return ModAchievements.sparkCraft;
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if (HellConfig.enable_tooltips) {
            p_77624_3_.add(LocalizationUtils.localize("tooltip.spark", manaPerSec));
        }
    }
}
