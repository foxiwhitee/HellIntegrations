package foxiwhitee.hellmod.integration.ic2.blocks;

import appeng.block.AEBaseBlock;
import appeng.client.gui.AEBaseGui;
import appeng.util.Platform;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.client.gui.GuiSynthesizer;
import foxiwhitee.hellmod.integration.ic2.container.ContainerSynthesizer;
import foxiwhitee.hellmod.integration.ic2.tile.TileSynthesizer;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@SimpleGuiHandler(index = GuiHandlers.synthesizer, tile = TileSynthesizer.class, gui = GuiSynthesizer.class, container = ContainerSynthesizer.class, integration = "IC2")
public final class BlockSynthesizer extends AEBaseBlock implements ITileEntityProvider {
    public BlockSynthesizer(String name) {
        super(Material.iron);
        setBlockName(name);
        setBlockTextureName(HellCore.MODID + ":ic2/" + name);
        this.isOpaque = false;
    }

    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return (TileEntity)new TileSynthesizer();
    }

    public boolean isBlockNormalCube() {
        return false;
    }

    public void breakBlock(World w, int x, int y, int z, Block a, int meta) {
        TileEntity tileEntity = (w != null) ? w.getTileEntity(x, y, z) : null;
        TileSynthesizer te = (tileEntity instanceof TileSynthesizer) ? (TileSynthesizer)tileEntity : null;
        if (te != null) {
            List<ItemStack> drops = new ArrayList();
            for (int i = 0, j = te.getSizeInventory(); i < j; i++) {
                if (te.getStackInSlot(i) != null) {
                    drops.add(te.getStackInSlot(i));
                }
            }
            Platform.spawnDrops(w, x, y, z, drops);
        }
        super.breakBlock(w, x, y, z, a, meta);
    }

    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (player != null && ((player.isSneaking() == true)))
            return false;
        if (w != null && (!w.isRemote))
            if (player != null) {
                player.openGui(HellCore.instance, GuiHandlers.synthesizer, w, x, y, z);
            }
        return true;
    }
}
