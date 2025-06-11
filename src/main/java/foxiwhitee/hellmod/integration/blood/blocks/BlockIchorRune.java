package foxiwhitee.hellmod.integration.blood.blocks;

import WayofTime.alchemicalWizardry.common.block.BloodRune;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockIchorRune extends BloodRune {
    public static final String[] blocks = new String[] {
            "ichorRuneBlank", "ichorRuneCapacity", "ichorRuneBetterCapacity", "ichorRuneDislocation", "ichorRuneEfficiency",
            "ichorRuneOrbCapacity", "ichorRuneSacrifice", "ichorRuneSelfSacrifice", "ichorRuneSpeed", "ichorRuneAcceleration"
    };

    private IIcon[] icons = new IIcon[blocks.length];

    public BlockIchorRune(String name) {
        setBlockName(name);
        setCreativeTab(HellCore.HELL_TAB);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeStone);
    }

    @Override
    public int getRuneEffect(int metaData) {
        switch (metaData) {
            case 1: return HellConfig.ichorRuneCapacity;
            case 2: return HellConfig.ichorRuneBetterCapacity;
            case 3: return HellConfig.ichorRuneDislocation;
            case 4: return HellConfig.ichorRuneEfficiency;
            case 5: return HellConfig.ichorRuneOrbCapacity;
            case 6: return HellConfig.ichorRuneSacrifice;
            case 7: return HellConfig.ichorRuneSelfSacrifice;
            case 8: return HellConfig.ichorRuneSpeed;
            case 9: return HellConfig.ichorRuneAcceleration;
            default: return 0;
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < getBlocks().length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        String name;
        for (int i = 0; i < getBlocks().length; i++) {
            name = HellCore.MODID + ":bloodMagic/" + getBlocks()[i];
            getIcons()[i] = register.registerIcon(name);
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta >= icons.length) meta = 0;
        return getIcons()[meta];
    }

    public IIcon[] getIcons() {
        return icons;
    }

    protected String[] getBlocks() {
        return blocks;
    }
}