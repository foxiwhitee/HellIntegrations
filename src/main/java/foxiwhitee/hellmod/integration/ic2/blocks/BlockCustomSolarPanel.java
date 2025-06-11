package foxiwhitee.hellmod.integration.ic2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.client.gui.GuiCustomSolarPanel;
import foxiwhitee.hellmod.integration.ic2.container.ContainerCustomSolarPanel;
import foxiwhitee.hellmod.integration.ic2.helpers.ISpecialSintezatorPanel;
import foxiwhitee.hellmod.integration.ic2.tile.generators.panels.*;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SimpleGuiHandler(index = GuiHandlers.customSolarPanel, tile = TileCustomSolarPanel.class, gui = GuiCustomSolarPanel.class, container = ContainerCustomSolarPanel.class, integration = "IC2")
public class BlockCustomSolarPanel extends BlockContainer implements ISpecialSintezatorPanel {
    @SideOnly(Side.CLIENT)
    private IIcon topIcon, downIcon;
    private String name;
    public BlockCustomSolarPanel(String name) {
        super(Material.rock);
        this.name = name;
        setBlockName(name);
        setHardness(3.0F);
        setStepSound(soundTypeMetal);
        setCreativeTab((CreativeTabs) HellCore.HELL_TAB);
    }

    public int getGenDay() {
        switch (name) {
            case "panel2": return HellConfig.panel2GenDay;
            case "panel3": return HellConfig.panel3GenDay;
            case "panel4": return HellConfig.panel4GenDay;
            case "panel5": return HellConfig.panel5GenDay;
            case "panel6": return HellConfig.panel6GenDay;
            case "panel7": return HellConfig.panel7GenDay;
            case "panel8": return HellConfig.panel8GenDay;
            default: return HellConfig.panel1GenDay;
        }
    }

    public int getGenNight() {
        switch (name) {
            case "panel2": return HellConfig.panel2GenNight;
            case "panel3": return HellConfig.panel3GenNight;
            case "panel4": return HellConfig.panel4GenNight;
            case "panel5": return HellConfig.panel5GenNight;
            case "panel6": return HellConfig.panel6GenNight;
            case "panel7": return HellConfig.panel7GenNight;
            case "panel8": return HellConfig.panel8GenNight;
            default: return HellConfig.panel1GenNight;
        }
    }



    public TileEntity createNewTileEntity(World world, int meta) {
        switch (name) {
            case "panel2": return new TileSolarPanelLevel2();
            case "panel3": return new TileSolarPanelLevel3();
            case "panel4": return new TileSolarPanelLevel4();
            case "panel5": return new TileSolarPanelLevel5();
            case "panel6": return new TileSolarPanelLevel6();
            case "panel7": return new TileSolarPanelLevel7();
            case "panel8": return new TileSolarPanelLevel8();
            default: return new TileSolarPanelLevel1();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {

        switch(side) {
            case 0:
                return downIcon;
            case 1:
                return topIcon;
            default:
                return blockIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        switch(side) {
            case 0:
                return downIcon;
            case 1:
                return topIcon;
            default:
                return blockIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        blockIcon = register.registerIcon( HellCore.MODID+ ":panels/" + this.getUnlocalizedName().replace("tile.", "") + "Side");
        topIcon = register.registerIcon(HellCore.MODID+ ":panels/" + this.getUnlocalizedName().replace("tile.", "") + "Top");
        downIcon = register.registerIcon(HellCore.MODID+ ":panels/bottom");
    }


    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int s, float f1, float f2, float f3) {
        if (player.isSneaking())
            return false;
        if (world.isRemote)
            return true;
        TileEntity tileentity = world.getTileEntity(i, j, k);
        if (tileentity != null)
            player.openGui(HellCore.instance, GuiHandlers.customSolarPanel, world, i, j, k);
        return true;
    }

    @Override
    public double getDayGen(ItemStack paramItemStack) {
        return getGenDay();
    }

    @Override
    public double getNightGen(ItemStack paramItemStack) {
        return getGenNight();
    }
}

