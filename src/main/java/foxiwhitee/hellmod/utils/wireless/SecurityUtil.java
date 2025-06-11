package foxiwhitee.hellmod.utils.wireless;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.ISecurityGrid;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;

public class SecurityUtil {
    public static int getProfileId(GameProfile gameProfile) {
        return AEApi.instance().registries().players().getID(gameProfile);
    }

    public static int getUserId(EntityPlayer entityPlayer) {
        return AEApi.instance().registries().players().getID(entityPlayer);
    }

    public static EntityPlayer getPlayer(int n) {
        return AEApi.instance().registries().players().findPlayer(n);
    }

    public static boolean isAccess(IGrid grid, int n, SecurityPermissions securityPermissions) {
        if (grid == null)
            return true;
        ISecurityGrid securityGrid = (ISecurityGrid)grid.getCache(ISecurityGrid.class);
        return (securityGrid == null || !securityGrid.isAvailable() || securityGrid.hasPermission(n, securityPermissions));
    }

    public static boolean isAccess(IGridNode gridNode) {
        if (gridNode.getGrid() == null)
            return false;
        ISecurityGrid securityGrid = (ISecurityGrid)gridNode.getGrid().getCache(ISecurityGrid.class);
        return (securityGrid != null && securityGrid.isAvailable());
    }
}
