package foxiwhitee.hellmod.integration.avaritia.parts;

import appeng.client.texture.CableBusTextures;
import appeng.core.sync.GuiBridge;
import appeng.tile.inventory.AppEngInternalInventory;
import java.util.List;

import appeng.tile.inventory.InvOperation;
import foxiwhitee.hellmod.integration.avaritia.AvaritiaIntegration;
import foxiwhitee.hellmod.parts.PartCraftingTerminal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PartCraftingTerminal9x9 extends PartCraftingTerminal {
    private final AppEngInternalInventory crafting = new AppEngInternalInventory(this, 81);

    public PartCraftingTerminal9x9(ItemStack is) {
        super(is);
    }

    @Override
    public AppEngInternalInventory getInventoryCrafting() {
        return crafting;
    }

    protected ItemStack getItemFromTile(Object obj) {
        return new ItemStack((Item) AvaritiaIntegration.ITEM_PARTS_TERMINALS, 1, 1);
    }

    public CableBusTextures getFrontBright() {
        return AvaritiaIntegration.getBusTextureBright(1);
    }

    public CableBusTextures getFrontColored() {
        return AvaritiaIntegration.getBusTextureColored(1);
    }

    public CableBusTextures getFrontDark() {
        return AvaritiaIntegration.getBusTextureDark(1);
    }

    protected GuiBridge getThisGui() {
        return AvaritiaIntegration.getGuiBridge(1);
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {

    }
}
