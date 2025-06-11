package foxiwhitee.hellmod.asm.blood;

import WayofTime.alchemicalWizardry.common.bloodAltarUpgrade.AltarUpgradeComponent;
import foxiwhitee.hellmod.integration.blood.blocks.BlockIchorRune;
import foxiwhitee.hellmod.integration.blood.blocks.BlockSingularRune;
import net.minecraft.block.Block;

public class BloodHooks {
    public static void getAdditionalUpgrades(AltarUpgradeComponent upgrades, Block block, int meta) {
        if (block instanceof BlockSingularRune) {
            switch (meta) {
                case 1:
                    for (int i = 0; i < ((BlockSingularRune)block).getRuneEffect(meta); i++) {
                        upgrades.addaltarCapacitiveUpgrade();;
                    }
                    break;
                case 2:
                    for (int i = 0; i < ((BlockSingularRune)block).getRuneEffect(meta); i++) {
                        upgrades.addBetterCapacitiveUpgrade();;
                    }
                    break;
                case 3:
                    for (int i = 0; i < ((BlockSingularRune)block).getRuneEffect(meta); i++) {
                        upgrades.addDisplacementUpgrade();;
                    }
                    break;
                case 4:
                    for (int i = 0; i < ((BlockSingularRune)block).getRuneEffect(meta); i++) {
                        upgrades.addEfficiencyUpgrade();;
                    }
                    break;
                case 5:
                    for (int i = 0; i < ((BlockSingularRune)block).getRuneEffect(meta); i++) {
                        upgrades.addorbCapacitiveUpgrade();;
                    }
                    break;
                case 6:
                    for (int i = 0; i < ((BlockSingularRune)block).getRuneEffect(meta); i++) {
                        upgrades.addSacrificeUpgrade();;
                    }
                    break;
                case 7:
                    for (int i = 0; i < ((BlockSingularRune)block).getRuneEffect(meta); i++) {
                        upgrades.addSelfSacrificeUpgrade();;
                    }
                    break;
                case 8:
                    for (int i = 0; i < ((BlockSingularRune)block).getRuneEffect(meta); i++) {
                        upgrades.addSpeedUpgrade();;
                    }
                    break;
                case 9:
                    for (int i = 0; i < ((BlockSingularRune)block).getRuneEffect(meta); i++) {
                        upgrades.addAccelerationUpgrade();;
                    }
                    break;
            }
        } else if (block instanceof BlockIchorRune) {
            switch (meta) {
                case 1:
                    for (int i = 0; i < ((BlockIchorRune)block).getRuneEffect(meta); i++) {
                        upgrades.addaltarCapacitiveUpgrade();;
                    }
                    break;
                case 2:
                    for (int i = 0; i < ((BlockIchorRune)block).getRuneEffect(meta); i++) {
                        upgrades.addBetterCapacitiveUpgrade();;
                    }
                    break;
                case 3:
                    for (int i = 0; i < ((BlockIchorRune)block).getRuneEffect(meta); i++) {
                        upgrades.addDisplacementUpgrade();;
                    }
                    break;
                case 4:
                    for (int i = 0; i < ((BlockIchorRune)block).getRuneEffect(meta); i++) {
                        upgrades.addEfficiencyUpgrade();;
                    }
                    break;
                case 5:
                    for (int i = 0; i < ((BlockIchorRune)block).getRuneEffect(meta); i++) {
                        upgrades.addorbCapacitiveUpgrade();;
                    }
                    break;
                case 6:
                    for (int i = 0; i < ((BlockIchorRune)block).getRuneEffect(meta); i++) {
                        upgrades.addSacrificeUpgrade();;
                    }
                    break;
                case 7:
                    for (int i = 0; i < ((BlockIchorRune)block).getRuneEffect(meta); i++) {
                        upgrades.addSelfSacrificeUpgrade();;
                    }
                    break;
                case 8:
                    for (int i = 0; i < ((BlockIchorRune)block).getRuneEffect(meta); i++) {
                        upgrades.addSpeedUpgrade();;
                    }
                    break;
                case 9:
                    for (int i = 0; i < ((BlockIchorRune)block).getRuneEffect(meta); i++) {
                        upgrades.addAccelerationUpgrade();;
                    }
                    break;
            }
        }

    }
}
