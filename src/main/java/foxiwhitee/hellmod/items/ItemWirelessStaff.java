package foxiwhitee.hellmod.items;

import appeng.api.config.SecurityPermissions;
import appeng.api.exceptions.FailedConnection;
import appeng.util.Platform;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.tile.wireless.TileCustomWireless;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import foxiwhitee.hellmod.utils.wireless.ConnectionUtil;
import foxiwhitee.hellmod.utils.wireless.SecurityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class ItemWirelessStaff extends Item {
    public ItemWirelessStaff(String name) {
        setUnlocalizedName(name);
        setTextureName(HellCore.MODID + ":" + name);
        setCreativeTab(HellCore.HELL_TAB);
        setMaxStackSize(1);
    }

    public boolean checkSecurity(TileCustomWireless firstWireless, TileCustomWireless secondWireless, EntityPlayer entityPlayer) {
        int securityId = SecurityUtil.getUserId(entityPlayer);
        return (SecurityUtil.isAccess(firstWireless.getProxy().getNode().getGrid(), securityId, SecurityPermissions.BUILD) && SecurityUtil.isAccess(secondWireless.getProxy().getNode().getGrid(), securityId, SecurityPermissions.BUILD));
    }

    public boolean hasLocation(ItemStack itemStack) {
        return (itemStack.getItem() == this && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("loc"));
    }

    public ConnectionUtil getLocation(ItemStack itemStack) {
        return ConnectionUtil.getTags(itemStack.getTagCompound().getCompoundTag("loc"));
    }

    public int getDimension(ItemStack itemStack) {
        return itemStack.getTagCompound().getInteger("dim");
    }

    public void setLocation(ItemStack itemStack, ConnectionUtil connectionUtil, int n) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        tagCompound.setTag("loc", (NBTBase)connectionUtil.writeToNBTWireless(new NBTTagCompound()));
        tagCompound.setInteger("dim", n);
    }

    public void clearLocation(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            itemStack.getTagCompound().removeTag("loc");
            itemStack.getTagCompound().removeTag("dim");
        }
    }

    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int n, int n2, int n3, int n4, float n5, float n6, float n7) {
        if (Platform.isServer()) {
            ConnectionUtil connectionUtil = new ConnectionUtil(n, n2, n3);
            TileEntity tile = connectionUtil.getTile((IBlockAccess) world);
            if (tile instanceof TileCustomWireless) {
                TileCustomWireless firsWireless = (TileCustomWireless) tile;
                int id = SecurityUtil.getUserId(entityPlayer);
                if (!SecurityUtil.isAccess(firsWireless.getProxy().getNode().getGrid(), id, SecurityPermissions.BUILD)) {
                    entityPlayer.addChatMessage((IChatComponent) new ChatComponentTranslation("ae2stuff.wireless.tool.security.player", new Object[0]));
                } else if (hasLocation(itemStack)) {
                    ConnectionUtil location = getLocation(itemStack);
                    if (getDimension(itemStack) != world.provider.dimensionId) {
                        entityPlayer.addChatMessage((IChatComponent) new ChatComponentTranslation("ae2stuff.wireless.tool.dimension", new Object[0]));
                    } else if (connectionUtil.equals(location)) {
                        clearLocation(itemStack);
                    } else {
                        TileEntity tileEntity = location.getTile((IBlockAccess) world);
                        if (tileEntity instanceof TileCustomWireless) {
                            TileCustomWireless secondWireless = (TileCustomWireless) tileEntity;
                            if (!SecurityUtil.isAccess(secondWireless.getProxy().getNode().getGrid(), id, SecurityPermissions.BUILD)) {
                                entityPlayer.addChatMessage((IChatComponent) new ChatComponentTranslation("ae2stuff.wireless.tool.security.player", new Object[0]));
                            } else {
                                firsWireless.breakConnection();
                                secondWireless.breakConnection();
                                firsWireless.getProxy().getNode().setPlayerID(id);
                                secondWireless.getProxy().getNode().setPlayerID(id);
                                try {
                                    if (firsWireless.runConnection(secondWireless)) {
                                        if (firsWireless.hasConnectionUtil()) {
                                            entityPlayer.addChatMessage((IChatComponent) new ChatComponentTranslation("ae2stuff.wireless.tool.connected", new Object[]{Integer.valueOf(connectionUtil.x), Integer.valueOf(connectionUtil.y), Integer.valueOf(connectionUtil.z)}));
                                        } else {
                                            entityPlayer.addChatMessage((IChatComponent) new ChatComponentTranslation("ae2stuff.wireless.tool.limit", new Object[]{Integer.valueOf(connectionUtil.x), Integer.valueOf(connectionUtil.y), Integer.valueOf(connectionUtil.z)}));
                                        }
                                    } else {
                                        entityPlayer.addChatMessage((IChatComponent) new ChatComponentTranslation("ae2stuff.wireless.tool.failed", new Object[0]));
                                    }
                                } catch (FailedConnection failedConnection) {
                                    failedConnection.printStackTrace();
                                    entityPlayer.addChatComponentMessage((IChatComponent) new ChatComponentTranslation("ae2stuff.wireless.tool.failed", new Object[]{failedConnection.getMessage()}));
                                    firsWireless.breakConnection();
                                }
                            }
                        } else {
                            entityPlayer.addChatMessage((IChatComponent) new ChatComponentTranslation("ae2stuff.wireless.tool.noexist", new Object[0]));
                        }
                        clearLocation(itemStack);
                    }
                } else {
                    entityPlayer.addChatMessage((IChatComponent) new ChatComponentTranslation("ae2stuff.wireless.tool.bound1", new Object[]{Integer.valueOf(connectionUtil.x), Integer.valueOf(connectionUtil.y), Integer.valueOf(connectionUtil.z)}));
                    setLocation(itemStack, connectionUtil, world.provider.dimensionId);
                }
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean b) {
        if (hasLocation(itemStack)) {
            ConnectionUtil location = getLocation(itemStack);
            list.add(EnumChatFormatting.GREEN + LocalizationUtils.localize("msg.wireless.staff.bound1", new Object[] { Integer.valueOf(location.x), Integer.valueOf(location.y), Integer.valueOf(location.z) }));
            list.add(EnumChatFormatting.GREEN + LocalizationUtils.localize("msg.wireless.staff.bound2"));
        } else {
            list.add(EnumChatFormatting.RED + LocalizationUtils.localize("msg.wireless.staff.empty"));
        }
    }
}