package foxiwhitee.hellmod.integration.draconic.items;

import com.brandon3055.draconicevolution.common.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.entity.EntityHeart;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemChaoticHeart extends ItemHeart {
    //Code taken from Draconic Evolution

    public ItemChaoticHeart(String name) {
        this.setUnlocalizedName(name);
        this.setTextureName(HellCore.MODID + ":draconic/" + name);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(LocalizationUtils.localize("tooltip.ChaosHeart"));
        par3List.add(LocalizationUtils.localize("tooltip.Heart.1", LocalizationUtils.localize(DraconicEvolutionIntegration.chaotic_block.getUnlocalizedName() + "1.name")));
        par3List.add(LocalizationUtils.localize("tooltip.Heart.2"));
        par3List.add(LocalizationUtils.localize("tooltip.Heart.3", HellConfig.coresNeedsForChaotic, LocalizationUtils.localize(ModItems.awakenedCore.getUnlocalizedName() + ".name")));
        par3List.add(LocalizationUtils.localize("tooltip.Heart.4"));
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityHeart(world, location, itemstack);
    }
}
