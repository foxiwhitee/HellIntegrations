package foxiwhitee.hellmod.integration.draconic.items;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockFusionInjector extends ItemBlock {
    public ItemBlockFusionInjector(Block block) {
        super(block);
        setHasSubtypes(true);
        setCreativeTab(HellCore.HELL_TAB);
    }

    public String getUnlocalizedName(ItemStack item) {
        return item.getItem().getUnlocalizedName() + "." + item.getItemDamage();
    }

    public int getMetadata(int i) {
        return i;
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }
}
