package foxiwhitee.hellmod.integration.draconic.blocks;
import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.draconicevolution.common.utills.IHudDisplayBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.GuiAutomatedUpgradeableBlock;
import foxiwhitee.hellmod.container.ContainerAutomatedUpgradeableBlock;
import foxiwhitee.hellmod.integration.draconic.client.gui.GuiFusionCraftingCore;
import foxiwhitee.hellmod.integration.draconic.container.ContainerFusionCraftingCore;
import foxiwhitee.hellmod.integration.draconic.helpers.IFusionCraftingCharger;
import foxiwhitee.hellmod.integration.draconic.tile.TileFusionCraftingCore;
import foxiwhitee.hellmod.tile.TileAutomatedUpgradeableBlock;
import foxiwhitee.hellmod.utils.handler.GuiHandlers;
import foxiwhitee.hellmod.utils.handler.SimpleGuiHandler;
import foxiwhitee.hellmod.utils.helpers.RenderIDs;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SimpleGuiHandler(index = GuiHandlers.fusionCraftingCore, tile = TileFusionCraftingCore.class, gui = GuiFusionCraftingCore.class, container = ContainerFusionCraftingCore.class, integration = "DraconicEvolution")
public class BlockFusionCraftingCore extends Block implements ITileEntityProvider, IHudDisplayBlock {
    private final Random rand;

    public IIcon[] icon;

    public BlockFusionCraftingCore(String name) {
        super(Material.iron);
        this.rand = new Random();
        setHardness(5.0F);
        setResistance(10.0F);
        setBlockName(name);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.icon = new IIcon[1];
        this.icon[0] = register.registerIcon(HellCore.MODID + ":models/fusion_crafting_core");
    }

    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return (TileEntity)new TileFusionCraftingCore();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileFusionCraftingCore) {
            TileFusionCraftingCore te = (TileFusionCraftingCore)tile;
            te.updateInjectors();
            if (world.isRemote)
                return true;
        }
        if (tile != null)
            player.openGui(HellCore.MODID, GuiHandlers.fusionCraftingCore, world, x, y, z);
        return true;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    public int getRenderType() {
        return RenderIDs.FUSION_CORE.getId();
    }

    public List<String> getDisplayData(World world, int x, int y, int z) {
        List<String> list = new ArrayList<>();
        TileFusionCraftingCore tile = (TileFusionCraftingCore)world.getTileEntity(x, y, z);
        if (tile != null) {
            list.add(InfoHelper.HITC() + LocalizationUtils.localize(getUnlocalizedName() + ".name"));
            if (tile.getStackInCore(0) != null)
                list.add(I18n.format("tooltip.fusionCrafting.currentItem", new Object[0]) + ": " + tile.getStackInCore(0).getDisplayName() + " " + (tile.getStackInCore(0)).stackSize);
            if (tile.getStackInCore(1) != null)
                list.add(I18n.format("tooltip.fusionCrafting.finalItem", new Object[0]) + ": " + tile.getStackInSlot(1).getDisplayName() + " " + (tile.getStackInCore(1)).stackSize);
            if (tile.isCrafting() && tile.activeRecipe != null && tile.getCraftingStage() >= 0) {
                int state = tile.getCraftingStage();
                String status = (state > 1000) ? I18n.format("info.fusionCrafting.status.crafting", new Object[0]) : I18n.format("info.fusionCrafting.status.charging", new Object[0]);
                int d = (int)((state > 1000) ? ((state - 1000.0F) / 1000.0D) : (state / 1000.0D));
                String progress = (d * 100) + "%";
                if (state < 1000) {
                    long totalCharge = 0L;
                    for (IFusionCraftingCharger pedestal : tile.getChargers())
                        totalCharge += pedestal.getInjectorCharge();
                    long averageCharge = totalCharge / tile.activeRecipe.getInputs().size();
                    double percentage = averageCharge / tile.activeRecipe.getIngredientEnergyCost();
                    progress = ((int)(percentage * 100000.0D) / 1000.0D) + "%";
                }
                if (tile.craftingStage <= 0) {
                    list.add(I18n.format("tooltip.fusionCrafting.status.sleep", new Object[0]));
                } else {
                    list.add(I18n.format("tooltip.fusionCrafting.status", new Object[0]) + ": " + status + " " + progress);
                }
            }
        }
        return list;
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (world.isBlockIndirectlyGettingPowered(x, y, z) &&
                    tile instanceof TileFusionCraftingCore)
                ((TileFusionCraftingCore)tile).attemptStartCrafting();
        }
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileFusionCraftingCore)
            return ((TileFusionCraftingCore)tile).getComparatorOutput();
        return super.getComparatorInputOverride(world, x, y, z, p_149736_5_);
    }

    public boolean canProvidePower() {
        return true;
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int wut) {
        TileFusionCraftingCore tile = (TileFusionCraftingCore)world.getTileEntity(x, y, z);
        if (tile != null) {
            for (int i1 = 0; i1 < tile.getSizeInventory(); i1++) {
                ItemStack itemstack = tile.getStackInSlot(i1);
                if (itemstack != null) {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f3 = this.rand.nextFloat() * 0.8F + 0.1F;
                    while (itemstack.stackSize > 0) {
                        int j1 = this.rand.nextInt(21) + 10;
                        if (j1 > itemstack.stackSize)
                            j1 = itemstack.stackSize;
                        ItemStack itemStack = itemstack;
                        itemStack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (x + f), (y + f2), (z + f3), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f4 = 0.05F;
                        entityitem.motionX = ((float)this.rand.nextGaussian() * f4);
                        entityitem.motionY = ((float)this.rand.nextGaussian() * f4 + 0.2F);
                        entityitem.motionZ = ((float)this.rand.nextGaussian() * f4);
                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        world.spawnEntityInWorld((Entity)entityitem);
                    }
                }
            }
            world.func_147453_f(x, y, z, block);
        }
        super.breakBlock(world, x, y, z, block, wut);
    }
}
