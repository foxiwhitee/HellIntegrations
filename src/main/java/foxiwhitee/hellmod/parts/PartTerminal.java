package foxiwhitee.hellmod.parts;

import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.parts.IPartRenderHelper;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.IConfigManager;
import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.me.GridAccessException;
import appeng.parts.reporting.AbstractPartDisplay;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.ConfigManager;
import appeng.util.IConfigManagerHost;
import appeng.util.Platform;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public abstract class PartTerminal extends AbstractPartDisplay implements ITerminalHost, IConfigManagerHost, IAEAppEngInventory {
    private final IConfigManager cm = new ConfigManager(this);

    public PartTerminal(ItemStack is) {
        super(is);
        this.cm.registerSetting(Settings.SORT_BY, SortOrder.NAME);
        this.cm.registerSetting(Settings.VIEW_MODE, ViewItems.ALL);
        this.cm.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING);
    }

    protected abstract ItemStack getItemFromTile(Object obj);

    public IConfigManager getConfigManager() {
        return this.cm;
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.cm.readFromNBT(data);
    }

    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        this.cm.writeToNBT(data);
    }

    public boolean onPartActivate(EntityPlayer player, Vec3 pos) {
        if (!super.onPartActivate(player, pos) && !player.isSneaking()) {
            if (Platform.isClient()) {
                return true;
            } else {
                Platform.openGUI(player, this.getHost().getTile(), this.getSide(), this.getGui(player));
                return true;
            }
        } else {
            return false;
        }
    }

    public GuiBridge getGui(EntityPlayer p) {
        int x = (int)p.posX;
        int y = (int)p.posY;
        int z = (int)p.posZ;
        if (this.getHost().getTile() != null) {
            x = this.getTile().xCoord;
            y = this.getTile().yCoord;
            z = this.getTile().zCoord;
        }

        return this.getThisGui().hasPermissions(this.getHost().getTile(), x, y, z, this.getSide(), p) ? this.getThisGui() : GuiBridge.GUI_ME;
    }

    abstract protected GuiBridge getThisGui();

    @SideOnly(Side.CLIENT)
    public void renderInventory(IPartRenderHelper rh, RenderBlocks renderer) {
        rh.setBounds(2.0F, 2.0F, 14.0F, 14.0F, 14.0F, 16.0F);
        IIcon sideTexture = CableBusTextures.PartMonitorSides.getIcon();
        IIcon backTexture = CableBusTextures.PartMonitorBack.getIcon();
        rh.setTexture(sideTexture, sideTexture, backTexture, this.getItemStack().getIconIndex(), sideTexture, sideTexture);
        rh.renderInventoryBox(renderer);
        rh.setInvColor(this.getColor().whiteVariant);
        rh.renderInventoryFace(this.getFrontBright().getIcon(), ForgeDirection.SOUTH, renderer);
        rh.setInvColor(this.getColor().mediumVariant);
        rh.renderInventoryFace(this.getFrontDark().getIcon(), ForgeDirection.SOUTH, renderer);
        rh.setInvColor(this.getColor().blackVariant);
        rh.renderInventoryFace(this.getFrontColored().getIcon(), ForgeDirection.SOUTH, renderer);
        rh.setBounds(4.0F, 4.0F, 13.0F, 12.0F, 12.0F, 14.0F);
        rh.renderInventoryBox(renderer);
    }

    @SideOnly(Side.CLIENT)
    public void renderStatic(int x, int y, int z, IPartRenderHelper rh, RenderBlocks renderer) {
        this.setRenderCache(rh.useSimplifiedRendering(x, y, z, this, this.getRenderCache()));
        IIcon sideTexture = CableBusTextures.PartMonitorSides.getIcon();
        IIcon backTexture = CableBusTextures.PartMonitorBack.getIcon();
        rh.setTexture(sideTexture, sideTexture, backTexture, this.getItemStack().getIconIndex(), sideTexture, sideTexture);
        rh.setBounds(2.0F, 2.0F, 14.0F, 14.0F, 14.0F, 16.0F);
        rh.renderBlock(x, y, z, renderer);
        if (this.getLightLevel() > 0) {
            int l = 13;
            Tessellator.instance.setBrightness(13631696);
        }

        renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth = renderer.uvRotateTop = renderer.uvRotateWest = this.getSpin();
        Tessellator.instance.setColorOpaque_I(this.getColor().whiteVariant);
        rh.renderFace(x, y, z, this.getFrontBright().getIcon(), ForgeDirection.SOUTH, renderer);
        Tessellator.instance.setColorOpaque_I(this.getColor().mediumVariant);
        rh.renderFace(x, y, z, this.getFrontDark().getIcon(), ForgeDirection.SOUTH, renderer);
        Tessellator.instance.setColorOpaque_I(this.getColor().blackVariant);
        rh.renderFace(x, y, z, this.getFrontColored().getIcon(), ForgeDirection.SOUTH, renderer);
        renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth = renderer.uvRotateTop = renderer.uvRotateWest = 0;
        IIcon sideStatusTexture = CableBusTextures.PartMonitorSidesStatus.getIcon();
        rh.setTexture(sideStatusTexture, sideStatusTexture, backTexture, this.getItemStack().getIconIndex(), sideStatusTexture, sideStatusTexture);
        rh.setBounds(4.0F, 4.0F, 13.0F, 12.0F, 12.0F, 14.0F);
        rh.renderBlock(x, y, z, renderer);
        boolean hasChan = (this.getClientFlags() & 20) == 20;
        boolean hasPower = (this.getClientFlags() & 4) == 4;
        if (hasChan) {
            int l = 14;
            Tessellator.instance.setBrightness(14680288);
            Tessellator.instance.setColorOpaque_I(this.getColor().blackVariant);
        } else if (hasPower) {
            int l = 9;
            Tessellator.instance.setBrightness(9437328);
            Tessellator.instance.setColorOpaque_I(this.getColor().whiteVariant);
        } else {
            Tessellator.instance.setBrightness(0);
            Tessellator.instance.setColorOpaque_I(0);
        }

        IIcon sideStatusLightTexture = CableBusTextures.PartMonitorSidesStatusLights.getIcon();
        rh.renderFace(x, y, z, sideStatusLightTexture, ForgeDirection.EAST, renderer);
        rh.renderFace(x, y, z, sideStatusLightTexture, ForgeDirection.WEST, renderer);
        rh.renderFace(x, y, z, sideStatusLightTexture, ForgeDirection.UP, renderer);
        rh.renderFace(x, y, z, sideStatusLightTexture, ForgeDirection.DOWN, renderer);
    }

    public IMEMonitor<IAEItemStack> getItemInventory() {
        try {
            return this.getProxy().getStorage().getItemInventory();
        } catch (GridAccessException var2) {
            return null;
        }
    }

    public IMEMonitor<IAEFluidStack> getFluidInventory() {
        try {
            return this.getProxy().getStorage().getFluidInventory();
        } catch (GridAccessException var2) {
            return null;
        }
    }

    public void updateSetting(IConfigManager manager, Enum settingName, Enum newValue) {}

    abstract public CableBusTextures getFrontBright();

    abstract public CableBusTextures getFrontColored();

    abstract public CableBusTextures getFrontDark();

    public boolean isCraftingRecipe() {
        return false;
    }

    public boolean isSubstitution() {
        return false;
    }

    public void setSubstitution(boolean canSubstitute) {}

    public void setCraftingRecipe(boolean craftingMode) {}

}
