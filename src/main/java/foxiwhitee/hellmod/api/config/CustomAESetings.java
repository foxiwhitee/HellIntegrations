package foxiwhitee.hellmod.api.config;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public enum CustomAESetings {
    CRAFT_DIRECTION(EnumSet.of(Buttons.NEXT, Buttons.PAST)),
    TYPE_CRAFT(EnumSet.of(Buttons.CHANGE_TYPE));

    private final EnumSet<? extends Enum<?>> values;

    private CustomAESetings(@Nonnull EnumSet<? extends Enum<?>> possibleOptions) {
        if (possibleOptions.isEmpty()) {
            throw new IllegalArgumentException("Tried to instantiate an empty setting.");
        } else {
            this.values = possibleOptions;
        }
    }

    public EnumSet<? extends Enum<?>> getPossibleValues() {
        return this.values;
    }
}

