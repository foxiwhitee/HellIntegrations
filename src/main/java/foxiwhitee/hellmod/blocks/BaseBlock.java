package foxiwhitee.hellmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import foxiwhitee.hellmod.HellCore;

public class BaseBlock extends Block {
    public BaseBlock(String name) {
        super(Material.rock);
        this.setBlockName(name);
        this.setLightLevel(2f);
        this.setLightOpacity(10);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setHardness(3);
        this.setResistance(10);
        this.setHarvestLevel("pickaxe", 3);
        this.setStepSound(soundTypeMetal);
        this.setBlockTextureName(HellCore.MODID+":"+name);
    }
}