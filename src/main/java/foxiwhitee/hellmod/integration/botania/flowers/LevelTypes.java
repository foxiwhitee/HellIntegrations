package foxiwhitee.hellmod.integration.botania.flowers;

public enum LevelTypes {
    BURNING,
    CRYSTAL,
    DESTRUCTIVE,
    ETERNAL_FOG,
    ETERNAL_SOLSTICE,
    FULL_MOON,
    THUNDER,
    WILD_HUNT,
    YGGDRASIL,
    ASGARD;

    public static LevelTypes getType(String name) {
        for (LevelTypes type : values()) {
            if (type.name().equalsIgnoreCase(name)) return type;
        }
        return null;
    }
}
