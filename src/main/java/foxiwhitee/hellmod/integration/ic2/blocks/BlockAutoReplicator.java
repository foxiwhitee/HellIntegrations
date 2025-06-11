package foxiwhitee.hellmod.integration.ic2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.ic2.tile.replicators.TileAutoAdvancedReplicator;
import foxiwhitee.hellmod.integration.ic2.tile.replicators.TileAutoNanoReplicator;
import foxiwhitee.hellmod.integration.ic2.tile.replicators.TileAutoQuantumReplicator;
import foxiwhitee.hellmod.integration.ic2.tile.replicators.TileAutoReplicator;
import ic2.core.IC2;
import ic2.core.block.BlockBase;
import ic2.core.block.BlockTextureStitched;
import ic2.core.block.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAutoReplicator  extends Block implements ITileEntityProvider {
    IIcon[] icons = new IIcon[12];
    private String name;
    public BlockAutoReplicator(String name) {
        super(Material.iron);
        this.name = name;
        setCreativeTab(HellCore.HELL_TAB);
        setBlockName(name);
        setHardness(5.0F);
        setResistance(1000.0F);
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        switch (name) {
            case "quantum_replicator": return new TileAutoQuantumReplicator();
            case "nano_replicator": return new TileAutoNanoReplicator();
            default: return new TileAutoAdvancedReplicator();
        }
    }

    public long getItemsPerSec() {
        switch (name) {
            case "quantum_replicator": return HellConfig.quantum_replicator_speed;
            case "nano_replicator": return HellConfig.nano_replicator_speed;
            default: return HellConfig.advanced_replicator_speed;
        }
    }

    public short getDiscount() {
        switch (name) {
            case "quantum_replicator": return HellConfig.quantum_replicator_discount;
            case "nano_replicator": return HellConfig.nano_replicator_discount;
            default: return HellConfig.advanced_replicator_discount;
        }
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileAutoReplicator) {
            TileAutoReplicator tile = (TileAutoReplicator)tileEntity;
            tile.setUpdate(true);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        String name = HellCore.MODID + ":ic2/" + this.name;
        for (int a = 0; a < 2; a++) {
            for (int s = 0; s < 6; s++) {
                int subIndex = a * 6 + s;
                BlockTextureStitched blockTextureStitched = new BlockTextureStitched(name + ":" + subIndex, subIndex);
                this.getIcons()[subIndex] = (IIcon)blockTextureStitched;
                ((TextureMap)register).setTextureEntry(name + ":" + subIndex, (TextureAtlasSprite)blockTextureStitched);
            }
        }
    }

    public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
        int facing = getFacing(iBlockAccess, x, y, z);
        boolean active = isActive(iBlockAccess, x, y, z);
        int meta = iBlockAccess.getBlockMetadata(x, y, z);
        int index = meta;
        if (index >= this.getIcons().length)
            return null;
        int subIndex = BlockBase.getTextureSubIndex(facing, side);
        if (active) {
            subIndex += 6;
        }
        try {
            return this.getIcons()[subIndex];
        } catch (Exception var12) {
            IC2.platform.displayError(var12, "Coordinates: %d/%d/%d\nSide: %d\nBlock: %s\nMeta: %d\nFacing: %d\nActive: %s\nIndex: %d\nSubIndex: %d", new Object[] { Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z), Integer.valueOf(side), this, Integer.valueOf(meta), Integer.valueOf(facing), Boolean.valueOf(active), Integer.valueOf(index), Integer.valueOf(subIndex) });
            return null;
        }
    }

    public IIcon getIcon(int side, int meta) {
        int facing = 3;
        int index = meta;
        int subIndex = BlockBase.getTextureSubIndex(facing, side);
        if (index >= this.getIcons().length)
            return null;
        try {
            return this.getIcons()[subIndex];
        } catch (Exception var7) {
            IC2.platform.displayError(var7, "Side: " + side + "\nBlock: " + this + "\nMeta: " + meta + "\nFacing: " + facing + "\nIndex: " + index + "\nSubIndex: " + subIndex, new Object[0]);
            return null;
        }
    }

    public IIcon[] getIcons() {
        return icons;
    }

    private boolean isActive(IBlockAccess iba, int x, int y, int z) {
        return ((TileAutoReplicator)iba.getTileEntity(x, y, z)).isBusy();
    }

    public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z) {
        TileEntity te = iBlockAccess.getTileEntity(x, y, z);
        if (te instanceof TileEntityBlock)
            return ((TileEntityBlock)te).getFacing();
        int meta = iBlockAccess.getBlockMetadata(x, y, z);
        return 3;
    }

}
