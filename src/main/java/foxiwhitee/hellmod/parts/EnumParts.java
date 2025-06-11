package foxiwhitee.hellmod.parts;

import appeng.api.parts.IPart;
import appeng.core.features.AEFeature;
import appeng.integration.IntegrationType;
import foxiwhitee.hellmod.ModItems;
import foxiwhitee.hellmod.parts.cables.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Constructor;
import java.util.*;

public enum EnumParts {
    InvalidType(-1, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), null),
    PART_ADV_INTERFACE(0, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), (Class)PartAdvancedInterface.class),
    PART_HYBRID_INTERFACE(1, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), (Class)PartHybridInterface.class),
    PART_ULTIMATE_INTERFACE(2, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), (Class)PartUltimateInterface.class),
    ALITE_SMART_CABLE(25, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartCableAlite.class) {
        public boolean isCable() {
            return true;
        }
    },
    BIMARE_SMART_CABLE(42, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartCableBimare.class) {
        public boolean isCable() {
            return true;
        }
    },
    DEFIT_SMART_CABLE(59, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartCableDefit.class) {
        public boolean isCable() {
            return true;
        }
    },
    EFRIM_SMART_CABLE(76, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartCableEfrim.class) {
        public boolean isCable() {
            return true;
        }
    },
    NUR_DENSE_CABLE(93, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartDenseCableNur.class) {
        public boolean isCable() {
            return true;
        }
    },
    XAUR_DENSE_CABLE(110, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartDenseCableXaur.class) {
        public boolean isCable() {
            return true;
        }
    };

    private final int baseDamage;

    private final Set<AEFeature> features;

    private final Set<IntegrationType> integrations;

    private final Class<? extends IPart> myPart;

    private Constructor<? extends IPart> constructor;

    EnumParts(int baseMetaValue, Set<AEFeature> features, Set<IntegrationType> integrations, Class<? extends IPart> c) {
        this.features = Collections.unmodifiableSet(features);
        this.integrations = Collections.unmodifiableSet(integrations);
        this.myPart = c;
        this.baseDamage = baseMetaValue;
    }

    public boolean isCable() {
        return false;
    }

    public Set<AEFeature> getFeature() {
        return this.features;
    }

    public Set<IntegrationType> getIntegrations() {
        return this.integrations;
    }

    public Class<? extends IPart> getPart() {
        return this.myPart;
    }

    public Constructor<? extends IPart> getConstructor() {
        return this.constructor;
    }

    public void setConstructor(Constructor<? extends IPart> constructor) {
        this.constructor = constructor;
    }

    public int getBaseDamage() {
        return this.baseDamage;
    }

    public ItemStack getStack() {
        return new ItemStack((Item)ModItems.ITEM_PARTS, 1, getBaseDamage());
    }
}