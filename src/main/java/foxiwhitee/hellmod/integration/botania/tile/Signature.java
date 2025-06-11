package foxiwhitee.hellmod.integration.botania.tile;

import foxiwhitee.hellmod.HellCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.api.subtile.signature.SubTileSignature;

import java.util.List;

public class Signature  extends SubTileSignature {
    String name;

    IIcon icon;

    public Signature(String nombre) {
        this.name = nombre;
    }

    public void registerIcons(IIconRegister reg) {
        this.icon = reg.registerIcon( HellCore.MODID +":botania/flowers/" + this.name);
    }

    public IIcon getIconForStack(ItemStack item) {
        return this.icon;
    }

    public String getUnlocalizedNameForStack(ItemStack item) {
        return HellCore.MODID + ".flower." + this.name;
    }

    public String getUnlocalizedLoreTextForStack(ItemStack item) {
        return "tile." + HellCore.MODID + ".flower." + this.name + ".lore";
    }

    public void addTooltip(ItemStack stack, EntityPlayer player, List<String> tooltip) {
        super.addTooltip(stack, player, tooltip);
    }
}
