package foxiwhitee.hellmod.integration.thaumcraft.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import thaumcraft.common.tiles.TileInfusionMatrix;

import java.util.List;

public class ItemStabilizationChecker  extends Item {
    public ItemStabilizationChecker() {
        this.setUnlocalizedName("itemStabilizationChecker");
        this.setMaxStackSize(1);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setTextureName(HellCore.MODID + ":" + "itemStabilizationChecker");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
        if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
            for (int i = 1; i <= 7; i++) {
                list.add(LocalizationUtils.localize("tooltip.stabilizationChecker.desc." + i));
            };
        } else {
            list.add(LocalizationUtils.localize("tooltip.pressShift"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            try {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileInfusionMatrix) {
                    ((TileInfusionMatrix)tile).checkSurroundings = true;
                    ((TileInfusionMatrix)tile).updateEntity();
                    int symmetry = ((TileInfusionMatrix) tile).symmetry;
                    int instability = ((TileInfusionMatrix) tile).instability;
                    player.addChatMessage(new ChatComponentText("Symmetry: " + symmetry + ", Instability: " + instability));
                }
            } catch (Exception e) {}

        }
        return true;
    }




}



