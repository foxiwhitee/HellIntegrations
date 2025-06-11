package foxiwhitee.hellmod.integration.botania.blocks;

import java.awt.Color;
import java.util.Random;

import foxiwhitee.hellmod.integration.botania.tile.TileManaCharger;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.Vector3;

public class BlockManaCharger extends BlockBaseBotania implements ITileEntityProvider {
    public IIcon iIcon;

    public BlockManaCharger(String name) {
        super(name);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
    }

    public static int renderID = 443;

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        ItemStack stack = player.getHeldItem();
        TileManaCharger charger = (TileManaCharger)worldIn.getTileEntity(x, y, z);
        if (charger.stack != null) {
            dropBlockAsItem(worldIn, x, y + 1, z, charger.stack.copy());
            charger.stack = null;
        }
        if (stack != null && stack.getItem() instanceof vazkii.botania.api.mana.IManaItem) {
            charger.stack = stack.copy();
            stack.splitStack(1);
            if (stack.stackSize == 0)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }
        charger.sync();
        return true;
    }

    public int getRenderType() {
        return renderID;
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        if (!worldIn.isRemote) {
            TileManaCharger charger = (TileManaCharger)worldIn.getTileEntity(x, y, z);
            if (charger.stack != null) {
                dropBlockAsItem(worldIn, x, y + 1, z, charger.stack.copy());
                charger.stack = null;
            }
        }
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return (TileEntity)new TileManaCharger();
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean canPlaceBlockAt(World w, int x, int y, int z) {
        return w.getTileEntity(x, y - 1, z) instanceof vazkii.botania.api.mana.IManaPool;
    }

    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        int color = getRenderColor(par1World.getBlockMetadata(par2, par3, par4));
        int colorBright = (new Color(color)).brighter().getRGB();
        int colorDark = (new Color(color)).darker().getRGB();
        Vector3 origVector = new Vector3(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D);
        Vector3 endVector = origVector.copy().add(par1World.rand.nextDouble() * 2.0D - 1.0D, par1World.rand.nextDouble() * 2.0D - 1.0D, par1World.rand.nextDouble() * 2.0D - 1.0D);
        Botania.proxy.lightningFX(par1World, origVector, endVector, 5.0F, colorDark, colorBright);
    }

    public IIcon getIcon(int side, int meta) {
        return ModBlocks.livingrock.getIcon(0, 0);
    }
}

