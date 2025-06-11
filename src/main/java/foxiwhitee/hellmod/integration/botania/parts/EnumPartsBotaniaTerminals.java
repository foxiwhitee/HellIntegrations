package foxiwhitee.hellmod.integration.botania.parts;

import appeng.api.parts.IPart;
import appeng.core.features.AEFeature;
import appeng.core.localization.GuiText;
import appeng.integration.IntegrationType;
import foxiwhitee.hellmod.integration.thaumcraft.ThaumcraftIntegration;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum EnumPartsBotaniaTerminals {
    InvalidTypeBotania(-1, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), (Class)null),
    PART_ELVEN_TRADE_PATTERN_TERMINAL(0, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), (Class) PartElvenTradePatternTerminal.class),
    PART_MANA_POOL_PATTERN_TERMINAL(1, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartManaPoolPatternTerminal.class),
    PART_PETALS_PATTERN_TERMINAL(2, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartPetalsPatternTerminal.class),
    PART_PURE_DAISY_PATTERN_TERMINAL(3, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartPureDaisyPatternTerminal.class),
    PART_RUNE_ALTAR_PATTERN_TERMINAL(4, EnumSet.of(AEFeature.Core), EnumSet.noneOf(IntegrationType.class), PartRuneAltarPatternTerminal.class);

    private final int baseDamage;
    private final Set<AEFeature> features;
    private final Set<IntegrationType> integrations;
    private final Class<? extends IPart> myPart;
    private final GuiText extraName;
    private Constructor<? extends IPart> constructor;

    private EnumPartsBotaniaTerminals(int baseMetaValue, Set<AEFeature> features, Set<IntegrationType> integrations, Class<? extends IPart> c) {
        this(baseMetaValue, features, integrations, c, (GuiText)null);
    }

    private EnumPartsBotaniaTerminals(int baseMetaValue, Set<AEFeature> features, Set<IntegrationType> integrations, Class<? extends IPart> c, GuiText en) {
        this.features = Collections.unmodifiableSet(features);
        this.integrations = Collections.unmodifiableSet(integrations);
        this.myPart = c;
        this.extraName = en;
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

    public GuiText getExtraName() {
        return this.extraName;
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
        return new ItemStack(ThaumcraftIntegration.ITEM_PARTS_TERMINALS, 1, this.getBaseDamage());
    }
}