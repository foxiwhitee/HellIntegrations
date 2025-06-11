package foxiwhitee.hellmod.integration.botania.blocks;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBaseBotania extends Block {
    public String name;

    public BlockBaseBotania(String name) {
        this(name, Material.rock);
    }

    public BlockBaseBotania(String name, Material material) {
        super(material);
        this.name = name;
        setHardness(3.0F);
        setBlockName(name);
        setBlockTextureName(HellCore.MODID + ":botania/" + name);
        setCreativeTab(HellCore.HELL_TAB);
    }

    public BlockBaseBotania register() {
        RegisterUtils.registerBlock(this);
        if (hasTileEntity(0))
            RegisterUtils.registerTile(createTileEntity(null, 0).getClass());
        return this;
    }
}
