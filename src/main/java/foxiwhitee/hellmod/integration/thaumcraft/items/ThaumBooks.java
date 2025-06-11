package foxiwhitee.hellmod.integration.thaumcraft.items;

import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.List;

public class ThaumBooks extends Item {
    private String typeBook;
    public ThaumBooks(String name, String texture, String typeBook) {
        this.canRepair = false;
        this.setUnlocalizedName(name);
        this.setTextureName(HellCore.MODID+":"+texture);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.maxStackSize = 1;
        this.setMaxDamage(1);
        this.typeBook = typeBook;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (typeBook == "ThaumBook") {
            par3List.add(LocalizationUtils.localize("tooltip.book.thaum.desc"));
        } else if (typeBook == "DistortionBook") {
            par3List.add(LocalizationUtils.localize("tooltip.book.distortion.desc"));
        } else {
            par3List.add(LocalizationUtils.localize("tooltip.book.error"));
        }

    }

    @Override
    @Subscribe
    public ItemStack onItemRightClick(ItemStack heldStack, World world, EntityPlayer player){
        if (!world.isRemote) {
            if (typeBook == "ThaumBook") {
                player.addChatMessage(new ChatComponentText(LocalizationUtils.localize("tooltip.book.thaum.message.1")));
                player.addChatMessage(new ChatComponentText(LocalizationUtils.localize("tooltip.book.thaum.message.2")));
                MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "tc research @p all");
                MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "tc aspect @p all 1");
            } else if (typeBook == "DistortionBook") {
                player.addChatMessage(new ChatComponentText(LocalizationUtils.localize("tooltip.book.distortion.message.1")));
                player.addChatMessage(new ChatComponentText(LocalizationUtils.localize("tooltip.book.distortion.message.2")));
                MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "tc warp @p set 0 perm");
                MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "tc warp @p set 0 temp");
                MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "tc warp @p set 0 all");
            }

        }
        heldStack.damageItem(2, player);
        return super.onItemRightClick(heldStack, world, player);
    }
}