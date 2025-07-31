package foxiwhitee.hellmod.proxy;

import appeng.client.texture.CableBusTextures;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.ModBlocks;
import foxiwhitee.hellmod.client.ClientEventHandler;
import foxiwhitee.hellmod.client.render.RenderBlockAdvancedDriver;
import foxiwhitee.hellmod.client.render.RenderBlockCustomEnergyCell;
import foxiwhitee.hellmod.client.render.assemblers.*;
import foxiwhitee.hellmod.tile.TileAdvancedDrive;
import foxiwhitee.hellmod.tile.assemblers.TileBaseMolecularAssembler;
import foxiwhitee.hellmod.tile.assemblers.TileHybridMolecularAssembler;
import foxiwhitee.hellmod.tile.assemblers.TileUltimateMolecularAssembler;
import foxiwhitee.hellmod.tile.energycell.TileAdvancedEnergyCell;
import foxiwhitee.hellmod.tile.energycell.TileHybridEnergyCell;
import foxiwhitee.hellmod.tile.energycell.TileUltimateEnergyCell;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class ClientProxy extends CommonProxy {
    private static final HashMap<String, Integer> hash = new HashMap<>();

    public static int[] displayList = new int[1];

    public static int getRenderBlocks(String model) {
        if (hash.containsKey(model))
            return ((Integer)hash.get(model)).intValue();
        int displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(displayList, 4864);
        AdvancedModelLoader.loadModel(new ResourceLocation(HellCore.MODID, "models/blocks/" + model + ".obj")).renderAll();
        GL11.glEndList();
        hash.put(model, Integer.valueOf(displayList));
        return displayList;
    }

    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        RegisterUtils.registerItemRenderer(Item.getItemFromBlock(ModBlocks.baseMolecularAssembler), (IItemRenderer)new RenderItemBaseMolecularAssembler());
        RegisterUtils.registerItemRenderer(Item.getItemFromBlock(ModBlocks.hybridMolecularAssembler), (IItemRenderer)new RenderItemHybridMolecularAssembler());
        RegisterUtils.registerItemRenderer(Item.getItemFromBlock(ModBlocks.ultimateMolecularAssembler), (IItemRenderer)new RenderItemUltimateMolecularAssembler());
        RegisterUtils.registerTileRenderer(TileBaseMolecularAssembler.class, (TileEntitySpecialRenderer)new RenderBlockBaseMolecularAssembler());
        RegisterUtils.registerTileRenderer(TileHybridMolecularAssembler.class, (TileEntitySpecialRenderer)new RenderBlockHybridMolecularAssembler());
        RegisterUtils.registerTileRenderer(TileUltimateMolecularAssembler.class, (TileEntitySpecialRenderer)new RenderBlockUltimateMolecularAssembler());

        //RegisterUtils.registerTileRenderer(TileAdvancedDrive.class, (TileEntitySpecialRenderer)new RenderBlockAdvancedDriver());


    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        registerCableBusTextures();
    }

    private void registerCableBusTextures() {
        PART_ADV_INTERFACE_FRONT = EnumHelper.addEnum(CableBusTextures.class, "PartAdvInterfaceFront", new Class[]{String.class}, new Object[]{"PartAdvInterfaceFront"});
        PART_HYBRID_INTERFACE_FRONT = EnumHelper.addEnum(CableBusTextures.class, "PartHybridInterfaceFront", new Class[]{String.class}, new Object[]{"PartHybridInterfaceFront"});
        PART_ULTIMATE_INTERFACE_FRONT = EnumHelper.addEnum(CableBusTextures.class, "PartUltimateInterfaceFront", new Class[]{String.class}, new Object[]{"PartUltimateInterfaceFront"});

        cableBusTexturesBright.put(0, EnumHelper.addEnum(CableBusTextures.class, "AdvancedInterfaceTerminal", new Class[] { String.class }, new Object[] { "PartAdvancedInterfaceTerminal_Bright" }));
        cableBusTexturesDark.put(0, EnumHelper.addEnum(CableBusTextures.class, "AdvancedInterfaceTerminal", new Class[] { String.class }, new Object[] { "PartAdvancedInterfaceTerminal_Dark" }));
        cableBusTexturesColored.put(0, EnumHelper.addEnum(CableBusTextures.class, "AdvancedInterfaceTerminal", new Class[] { String.class }, new Object[] { "PartAdvancedInterfaceTerminal_Colored" }));
    }
}
