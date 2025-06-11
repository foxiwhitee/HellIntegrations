package foxiwhitee.hellmod.integration.draconic.items.capicators;

import cofh.api.energy.IEnergyContainerItem;
import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.brandonscore.common.utills.Utills;
import com.brandon3055.draconicevolution.common.items.tools.baseclasses.ToolBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.helpers.ICustomUpgradableItem;
import foxiwhitee.hellmod.integration.draconic.items.RFItemBase;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ChaoticFluxCapicator extends RFItemBase implements ICustomUpgradableItem {
    IIcon[] icons = new IIcon[2];
    private String name;
    public ChaoticFluxCapicator(String name) {
        this.name = name;
        setTextureName(HellCore.MODID + ":draconic/" + name);
        setUnlocalizedName(name);
        setCreativeTab(HellCore.HELL_TAB);
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons[0] = iconRegister.registerIcon(HellCore.MODID + ":draconic/" + name);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return this.icons[damage];
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(ItemNBTHelper.setLong(new ItemStack(item, 1, 0), "Energy", 0L));
        list.add(ItemNBTHelper.setLong(new ItemStack(item, 1, 0), "Energy", HellConfig.chaoticCapacitorBaseStorage));
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName(itemStack);
    }

    public long getCapacity(ItemStack stack) {
        int points = ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.getUpgradePoints(stack);
        return HellConfig.chaoticCapacitorBaseStorage + points * HellConfig.chaoticCapacitorStoragePerUpgrade;
    }

    public int getMaxExtract(ItemStack stack) {
        return HellConfig.chaoticCapacitorMaxExtract;
    }

    public int getMaxReceive(ItemStack stack) {
        return HellConfig.chaoticCapacitorMaxReceive;
    }

    public void onUpdate(ItemStack container, World world, Entity entity, int var1, boolean b) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            int mode = ItemNBTHelper.getShort(container, "Mode", (short)0);
            if (mode == 1 || mode == 3)
                for (int i = 0; i < 9; i++) {
                    long max = Math.min(getEnergyStored(container), getMaxExtract(container));
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    if (stack != null && stack.getItem() instanceof IEnergyContainerItem && stack.getItem() != DraconicEvolutionIntegration.chaotic_capicator) {
                        IEnergyContainerItem item = (IEnergyContainerItem)stack.getItem();
                        extractEnergy(container, item.receiveEnergy(stack, (int)max, false), false);
                    }
                }
            if (mode == 2 || mode == 3)
                for (int i = (mode == 3) ? 1 : 0; i < 5; i++) {
                    long max = Math.min(getEnergyStored(container), getMaxExtract(container));
                    ItemStack stack = player.getEquipmentInSlot(i);
                    if (stack != null && stack.getItem() instanceof IEnergyContainerItem && stack.getItem() != DraconicEvolutionIntegration.chaotic_capicator) {
                        IEnergyContainerItem item = (IEnergyContainerItem)stack.getItem();
                        extractEnergy(container, item.receiveEnergy(stack, (int)max, false), false);
                    }
                }
        }
    }

    public boolean hasEffect(ItemStack stack, int pass) {
        return (ItemNBTHelper.getShort(stack, "Mode", (short)0) > 0);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            int mode = ItemNBTHelper.getShort(stack, "Mode", (short)0);
            int newMode = (mode == 3) ? 0 : (mode + 1);
            ItemNBTHelper.setShort(stack, "Mode", (short)newMode);
            if (world.isRemote)
                player.addChatComponentMessage((IChatComponent)new ChatComponentTranslation(InfoHelper.ITC() + LocalizationUtils.localize("info.de.capacitorMode.txt") + ": " + InfoHelper.HITC() + LocalizationUtils.localize("info.de.capacitorMode" + ItemNBTHelper.getShort(stack, "Mode", (short)0) + ".txt"), new Object[0]));
        }
        return stack;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extraInformation) {
        if (InfoHelper.holdShiftForDetails(list)) {
            list.add(LocalizationUtils.localize("info.de.changwMode.txt"));
            list.add(InfoHelper.ITC() + LocalizationUtils.localize("info.de.capacitorMode.txt") + ": " + InfoHelper.HITC() + LocalizationUtils.localize("info.de.capacitorMode" + ItemNBTHelper.getShort(stack, "Mode", (short)0) + ".txt"));
        }
        ToolBase.holdCTRLForUpgrades(list, stack);
        addEnergyInfo(stack, list);
    }

    public static void addEnergyInfo(ItemStack stack, List<String> list) {
        RFItemBase item = (RFItemBase)stack.getItem();
        long energy = item.getEnergyStoredL(stack);
        long maxEnergy = item.getMaxEnergyStoredL(stack);
        String eS = "";
        String eM = "";
        if (energy < 1000L) {
            eS = String.valueOf(energy);
        } else if (energy < 1000000L) {
            eS = String.valueOf(energy);
        } else {
            eS = (Math.round(energy / 1000.0D) / 1000.0D) + "m";
        }
        if (maxEnergy < 1000L) {
            eM = String.valueOf(maxEnergy);
        } else if (maxEnergy < 1000000L) {
            eM = (Math.round(maxEnergy / 100.0D) / 10.0D) + "k";
        } else {
            eM = (Math.round(maxEnergy / 10000.0D) / 100.0D) + "m";
        }
        list.add(LocalizationUtils.localize("info.de.charge.txt") + ": " + eS + " / " + eM + " RF");
    }

    public boolean hasProfiles() {
        return false;
    }

    public List<ICustomUpgradableItem.EnumUpgrade> getCUpgrades(ItemStack itemstack) {
        return new ArrayList<ICustomUpgradableItem.EnumUpgrade>() {
            {
                this.add(ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY);
            }
        };
    }

    public int getUpgradeCap(ItemStack stack) {
        return 8;
    }

    public int getMaxTier(ItemStack stack) {
        return 3;
    }

    public int getMaxUpgradePoints(int upgradeIndex) {
        return HellConfig.chaoticCapacitorMaxUpgradePoints;
    }

    public int getMaxUpgradePoints(int upgradeIndex, ItemStack stack) {
        if (stack == null)
            return getMaxUpgradePoints(upgradeIndex);
        if (upgradeIndex == ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY.index)
            return 100;
        return 0;
    }

    public int getBaseUpgradePoints(int upgradeIndex) {
        return 0;
    }

    public List<String> getUpgradeStats(ItemStack stack) {
        List<String> strings = new ArrayList<>();
        strings.add(InfoHelper.ITC() + LocalizationUtils.localize("gui.de.RFCapacity.txt") + ": " + InfoHelper.HITC() + Utills.formatNumber(getMaxEnergyStoredL(stack)));
        return strings;
    }
}
