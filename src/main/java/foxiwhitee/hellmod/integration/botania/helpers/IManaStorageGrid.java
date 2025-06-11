package foxiwhitee.hellmod.integration.botania.helpers;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridCache;
import appeng.api.networking.security.BaseActionSource;

public interface IManaStorageGrid extends IGridCache {
    long injectMana(long paramLong, Actionable paramActionable, BaseActionSource paramBaseActionSource);

    long extractMana(long paramLong, Actionable paramActionable, BaseActionSource paramBaseActionSource);
}

