package foxiwhitee.hellmod.integration.botania.items.ae;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public final class ItemManaDrop extends Item {
    public ItemManaDrop(String name) {
        setTextureName(HellCore.MODID + ":" + name);
        setUnlocalizedName(name);
        setCreativeTab(HellCore.HELL_TAB);
        this.maxStackSize = 1;
    }

    public void onUpdate(ItemStack stack, World w, Entity e, int slot, boolean b) {
        if (e instanceof EntityPlayer && !((EntityPlayer)e).capabilities.isCreativeMode) {
            ((EntityPlayer)e).inventory.setInventorySlotContents(slot, null);
        }
    }
}