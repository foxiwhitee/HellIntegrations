package foxiwhitee.hellmod.integration.draconic.helpers;

import cofh.api.energy.IEnergyContainerItem;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.draconicevolution.common.utills.IConfigurableItem;
import com.brandon3055.draconicevolution.common.utills.IUpgradableItem;
import com.brandon3055.draconicevolution.common.utills.LogHelper;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public interface ICustomUpgradableItem {

    int getUpgradeCap(ItemStack var1);

    int getMaxTier(ItemStack var1);

    int getMaxUpgradePoints(int var1);

    int getMaxUpgradePoints(int var1, ItemStack var2);

    int getBaseUpgradePoints(int var1);

    List<String> getUpgradeStats(ItemStack var1);

    default List<EnumUpgrade> getCUpgrades(ItemStack itemstack) {
        return new ArrayList<EnumUpgrade>() {

        };
    }

    default ICustomUpgradableItem.EnumUpgrade transformUpgrade(IUpgradableItem.EnumUpgrade en) {
        switch (en) {
            case RF_CAPACITY: return ICustomUpgradableItem.EnumUpgrade.RF_CAPACITY;
            case DIG_SPEED: return ICustomUpgradableItem.EnumUpgrade.DIG_SPEED;
            case DIG_AOE: return ICustomUpgradableItem.EnumUpgrade.DIG_AOE;
            case DIG_DEPTH: return ICustomUpgradableItem.EnumUpgrade.DIG_DEPTH;
            case ATTACK_DAMAGE: return ICustomUpgradableItem.EnumUpgrade.ATTACK_DAMAGE;
            case ATTACK_AOE: return ICustomUpgradableItem.EnumUpgrade.ATTACK_AOE;
            case ARROW_DAMAGE: return ICustomUpgradableItem.EnumUpgrade.ARROW_DAMAGE;
            case DRAW_SPEED: return ICustomUpgradableItem.EnumUpgrade.DRAW_SPEED;
            case ARROW_SPEED: return ICustomUpgradableItem.EnumUpgrade.ARROW_SPEED;
            case SHIELD_CAPACITY: return ICustomUpgradableItem.EnumUpgrade.SHIELD_CAPACITY;
            case SHIELD_RECOVERY: return ICustomUpgradableItem.EnumUpgrade.SHIELD_RECOVERY;
            case MOVE_SPEED: return ICustomUpgradableItem.EnumUpgrade.MOVE_SPEED;
            case JUMP_BOOST: return ICustomUpgradableItem.EnumUpgrade.JUMP_BOOST;
        }
        return null;
    }

    default List<ICustomUpgradableItem.EnumUpgrade> transformUpgrades(List<IUpgradableItem.EnumUpgrade> ens) {
        List<ICustomUpgradableItem.EnumUpgrade> enums = new ArrayList<>();
        for (IUpgradableItem.EnumUpgrade en : ens) {
            enums.add(transformUpgrade(en));
        }
        return enums;
    }

    public static enum EnumUpgrade {
        RF_CAPACITY(0, 1, "RFCapacity") {
            public void onRemovedFromItem(ItemStack itemStack) {
                if (itemStack != null && itemStack.getItem() instanceof IEnergyContainerItem) {
                    IEnergyContainerItem item = (IEnergyContainerItem)itemStack.getItem();

                    for(int i = 0; i < 500 && item.getEnergyStored(itemStack) > item.getMaxEnergyStored(itemStack); ++i) {
                        item.extractEnergy(itemStack, item.getEnergyStored(itemStack) - item.getMaxEnergyStored(itemStack), false);
                    }
                }

            }
        },
        DIG_SPEED(1, 1, "DigSpeed"),
        DIG_AOE(2, 4, "DigAOE") {
            public void onRemovedFromItem(ItemStack itemStack) {
                int profile = ItemNBTHelper.getInteger(itemStack, "ConfigProfile", 0);

                for(int i = 0; i < 5; ++i) {
                    ItemNBTHelper.setInteger(itemStack, "ConfigProfile", i);
                    if (IConfigurableItem.ProfileHelper.getInteger(itemStack, "ToolDigAOE", 0) > this.getUpgradePoints(itemStack)) {
                        IConfigurableItem.ProfileHelper.setInteger(itemStack, "ToolDigAOE", this.getUpgradePoints(itemStack));
                    }
                }

                ItemNBTHelper.setInteger(itemStack, "ConfigProfile", profile);
            }
        },
        DIG_DEPTH(3, 2, "DigDepth") {
            public void onRemovedFromItem(ItemStack itemStack) {
                int profile = ItemNBTHelper.getInteger(itemStack, "ConfigProfile", 0);

                for(int i = 0; i < 5; ++i) {
                    ItemNBTHelper.setInteger(itemStack, "ConfigProfile", i);
                    if (IConfigurableItem.ProfileHelper.getInteger(itemStack, "ToolDigDepth", 0) > this.getUpgradePoints(itemStack)) {
                        IConfigurableItem.ProfileHelper.setInteger(itemStack, "ToolDigDepth", this.getUpgradePoints(itemStack));
                    }
                }

                ItemNBTHelper.setInteger(itemStack, "ConfigProfile", profile);
            }
        },
        ATTACK_DAMAGE(4, 1, "AttackDamage"),
        ATTACK_AOE(5, 2, "AttackAOE") {
            public void onRemovedFromItem(ItemStack itemStack) {
                int profile = ItemNBTHelper.getInteger(itemStack, "ConfigProfile", 0);

                for(int i = 0; i < 5; ++i) {
                    ItemNBTHelper.setInteger(itemStack, "ConfigProfile", i);
                    if (IConfigurableItem.ProfileHelper.getInteger(itemStack, "WeaponAttackAOE", 0) > this.getUpgradePoints(itemStack)) {
                        IConfigurableItem.ProfileHelper.setInteger(itemStack, "WeaponAttackAOE", this.getUpgradePoints(itemStack));
                    }
                }

                ItemNBTHelper.setInteger(itemStack, "ConfigProfile", profile);
            }
        },
        ARROW_DAMAGE(6, 1, "ArrowDamage") {
            public void onRemovedFromItem(ItemStack itemStack) {
                int profile = ItemNBTHelper.getInteger(itemStack, "ConfigProfile", 0);

                for(int i = 0; i < 5; ++i) {
                    ItemNBTHelper.setInteger(itemStack, "ConfigProfile", i);
                    if (IConfigurableItem.ProfileHelper.getInteger(itemStack, "BowArrowDamage", 0) > this.getUpgradePoints(itemStack)) {
                        IConfigurableItem.ProfileHelper.setInteger(itemStack, "BowArrowDamage", this.getUpgradePoints(itemStack));
                    }
                }

                ItemNBTHelper.setInteger(itemStack, "ConfigProfile", profile);
            }
        },
        DRAW_SPEED(7, 2, "DrawSpeed"),
        ARROW_SPEED(8, 2, "ArrowSpeed") {
            public void onRemovedFromItem(ItemStack itemStack) {
                int profile = ItemNBTHelper.getInteger(itemStack, "ConfigProfile", 0);

                for(int i = 0; i < 5; ++i) {
                    ItemNBTHelper.setInteger(itemStack, "ConfigProfile", i);
                    if (IConfigurableItem.ProfileHelper.getInteger(itemStack, "BowArrowSpeedModifier", 0) > this.getUpgradePoints(itemStack)) {
                        IConfigurableItem.ProfileHelper.setInteger(itemStack, "BowArrowSpeedModifier", this.getUpgradePoints(itemStack));
                    }
                }

                ItemNBTHelper.setInteger(itemStack, "ConfigProfile", profile);
            }
        },
        SHIELD_CAPACITY(9, 1, "ShieldCapacity"),
        SHIELD_RECOVERY(10, 1, "ShieldRecovery"),
        MOVE_SPEED(11, 1, "MoveSpeed"),
        JUMP_BOOST(12, 1, "JumpBoost");

        public int index;
        public int pointConversion;
        public String name;
        private final String COMPOUND_NAME;

        private EnumUpgrade(int index, int pointConversion, String name) {
            this.COMPOUND_NAME = "Upgrades";
            this.index = index;
            this.pointConversion = pointConversion;
            this.name = name;
        }

        public int[] getCoresApplied(ItemStack stack) {
            if (stack == null) {
                return new int[]{0, 0, 0, 0, 0};
            } else {
                NBTTagCompound compound = ItemNBTHelper.getCompound(stack);
                if (!compound.hasKey("Upgrades")) {
                    return new int[]{0, 0, 0, 0, 0};
                } else {
                    NBTTagCompound upgrades = compound.getCompoundTag("Upgrades");
                    return upgrades.hasKey(this.name) && upgrades.getIntArray(this.name).length == 5 ? upgrades.getIntArray(this.name) : new int[]{0, 0, 0, 0, 0};
                }
            }
        }

        public void setCoresApplied(ItemStack stack, int[] cores) {
            if (cores.length != 5) {
                LogHelper.error("[EnumUpgrade] Error applying upgrades to stack.");
            } else {
                NBTTagCompound compound = ItemNBTHelper.getCompound(stack);
                NBTTagCompound upgrades;
                if (compound.hasKey("Upgrades")) {
                    upgrades = compound.getCompoundTag("Upgrades");
                } else {
                    upgrades = new NBTTagCompound();
                }

                upgrades.setIntArray(this.name, cores);
                compound.setTag("Upgrades", upgrades);
            }
        }

        public String getLocalizedName() {
            return LocalizationUtils.localize("gui.de." + this.name + ".txt");
        }

        public static ICustomUpgradableItem.EnumUpgrade getUpgradeByIndex(int index) {
            for(ICustomUpgradableItem.EnumUpgrade upgrade : values()) {
                if (upgrade.index == index) {
                    return upgrade;
                }
            }

            return null;
        }

        public int getUpgradePoints(ItemStack itemStack) {
            int[] applied = this.getCoresApplied(itemStack);
            int totalPoints = applied[0] + applied[1] * 2 + applied[2] * 4 + applied[3] * 8 + applied[4] * 16;
            if (itemStack != null && itemStack.getItem() instanceof ICustomUpgradableItem) {
                int points = ((ICustomUpgradableItem)itemStack.getItem()).getBaseUpgradePoints(this.index) + totalPoints / this.pointConversion;
                return Math.min(points, ((ICustomUpgradableItem)itemStack.getItem()).getMaxUpgradePoints(this.index, itemStack));
            } else {
                return 0;
            }
        }

        public void onAppliedToItem(ItemStack itemStack) {
        }

        public void onRemovedFromItem(ItemStack itemStack) {
        }
    }
}