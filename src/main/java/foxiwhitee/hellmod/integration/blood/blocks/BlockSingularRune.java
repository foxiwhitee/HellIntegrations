package foxiwhitee.hellmod.integration.blood.blocks;

import foxiwhitee.hellmod.config.HellConfig;
import net.minecraft.util.IIcon;

public class BlockSingularRune extends BlockIchorRune{
    public static final String[] blocks = new String[] {
            "singularRuneBlank", "singularRuneCapacity", "singularRuneBetterCapacity", "singularRuneDislocation", "singularRuneEfficiency",
            "singularRuneOrbCapacity", "singularRuneSacrifice", "singularRuneSelfSacrifice", "singularRuneSpeed", "singularRuneAcceleration"
    };

    private IIcon[] icons = new IIcon[blocks.length];
    public BlockSingularRune(String name) {
        super(name);
    }

    public IIcon[] getIcons() {
        return icons;
    }

    protected String[] getBlocks() {
        return blocks;
    }

    @Override
    public int getRuneEffect(int metaData) {
        switch (metaData) {
            case 1: return HellConfig.singularRuneCapacity;
            case 2: return HellConfig.singularRuneBetterCapacity;
            case 3: return HellConfig.singularRuneDislocation;
            case 4: return HellConfig.singularRuneEfficiency;
            case 5: return HellConfig.singularRuneOrbCapacity;
            case 6: return HellConfig.singularRuneSacrifice;
            case 7: return HellConfig.singularRuneSelfSacrifice;
            case 8: return HellConfig.singularRuneSpeed;
            case 9: return HellConfig.singularRuneAcceleration;
            default: return 0;
        }
    }
}
