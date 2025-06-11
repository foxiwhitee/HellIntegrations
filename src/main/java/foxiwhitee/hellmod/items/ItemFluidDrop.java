package foxiwhitee.hellmod.items;

import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.core.features.AEFeature;
import appeng.items.AEBaseItem;
import appeng.util.Platform;
import appeng.util.item.AEFluidStack;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.render.ItemDropRender;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ItemFluidDrop extends AEBaseItem {
    public static ItemFluidDrop DROP = null;

    @SideOnly(Side.CLIENT)
    public IIcon shape;

    public ItemFluidDrop() {
        setUnlocalizedName("itemFluidDrop");
        setFeature(EnumSet.of(AEFeature.Core));
        DROP = this;
        if (Platform.isClient())
            MinecraftForgeClient.registerItemRenderer((Item)this, (IItemRenderer)new ItemDropRender());
    }

    public String getUnlocalizedName(ItemStack stack) {
        FluidStack fluid = getFluidStack(stack);
        return (fluid != null) ? fluid.getUnlocalizedName() : getUnlocalizedName();
    }

    public String getItemStackDisplayName(ItemStack stack) {
        FluidStack fluid = getFluidStack(stack);
        return LocalizationUtils.localize("item.fluid_drop.name", (fluid == null) ? "???" : fluid.getLocalizedName());
    }

    protected void addCheckedInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean displayMoreInfo) {
        FluidStack fluid = getFluidStack(stack);
        if (fluid != null) {
            tooltip.add(String.format(EnumChatFormatting.GRAY + "%s, 1 mB", fluid.getLocalizedName()));
        } else {
            tooltip.add(EnumChatFormatting.RED + LocalizationUtils.localize("invalid_fluid"));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        super.registerIcons(aIconRegister);
        this.shape = aIconRegister.registerIcon(HellCore.MODID + ":ItemFluidDrop");
    }

    @Nullable
    public static ItemStack newStack(@Nullable FluidStack fluid) {
        if (fluid == null || fluid.amount <= 0)
            return null;
        ItemStack stack = new ItemStack((Item)DROP, fluid.amount);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("fluid", fluid.getFluid().getName());
        stack.setTagCompound(tag);
        return stack;
    }

    public static boolean isFluidStack(ItemStack stack) {
        return (getFluidStack(stack) != null);
    }

    public static boolean isFluidStack(@Nullable IAEItemStack stack) {
        if (stack == null)
            return false;
        return (getFluidStack(stack.getItemStack()) != null);
    }

    @Nullable
    public static FluidStack getFluidStack(ItemStack stack) {
        if (stack == null || stack.getItem() != DROP || !stack.hasTagCompound())
            return null;
        NBTTagCompound tag = Objects.<NBTTagCompound>requireNonNull(stack.getTagCompound());
        if (!tag.hasKey("fluid", 8))
            return null;
        Fluid fluid = FluidRegistry.getFluid(tag.getString("fluid").toLowerCase());
        if (fluid == null)
            return null;
        FluidStack fluidStack = new FluidStack(fluid, stack.stackSize);
        if (tag.hasKey("fluidTag", 10))
            fluidStack.tag = tag.getCompoundTag("fluidTag");
        return fluidStack;
    }

    @Nullable
    public static IAEFluidStack getAeFluidStack(@Nullable IAEItemStack stack) {
        if (stack == null)
            return null;
        AEFluidStack aEFluidStack = AEFluidStack.create(getFluidStack(stack.getItemStack()));
        if (aEFluidStack == null)
            return null;
        aEFluidStack.setStackSize(stack.getStackSize());
        return (IAEFluidStack)aEFluidStack;
    }

    @Nullable
    public static IAEItemStack newAeStack(@Nullable FluidStack fluid) {
        if (fluid == null || fluid.amount <= 0)
            return null;
        AEItemStack aEItemStack = AEItemStack.create(newStack(fluid));
        if (aEItemStack == null)
            return null;
        aEItemStack.setStackSize(fluid.amount);
        return (IAEItemStack)aEItemStack;
    }

    @Nullable
    public static IAEItemStack newAeStack(@Nullable IAEFluidStack fluid) {
        if (fluid == null || fluid.getStackSize() <= 0L)
            return null;
        AEItemStack aEItemStack = AEItemStack.create(newStack(fluid.getFluidStack()));
        if (aEItemStack == null)
            return null;
        aEItemStack.setStackSize(fluid.getStackSize());
        return (IAEItemStack)aEItemStack;
    }
}

