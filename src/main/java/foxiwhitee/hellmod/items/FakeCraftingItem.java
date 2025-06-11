package foxiwhitee.hellmod.items;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.lang.invoke.*;
import java.util.HashMap;
import java.util.function.Function;

public class FakeCraftingItem extends Item implements ICraftingPatternItem {
    public static final FakeCraftingItem instance;

    private static final HashMap<ICraftingPatternDetails, ItemStack> cache = new HashMap<>();
    private static final HashMap<String, Function<ItemStack, ICraftingPatternDetails>> callCache = new HashMap<>();
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    public FakeCraftingItem() {
        setUnlocalizedName("fake_crafting_item");
    }

    static {
        instance = new FakeCraftingItem();
    }
    public static ItemStack getStackFor(ICraftingPatternDetails details) {
        if (cache.containsKey(details)) return cache.get(details);
        ItemStack stack = new ItemStack(instance);
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("class", details.getClass().getName());
        tagCompound.setBoolean("substitute", details.canSubstitute());
        tagCompound.setBoolean("craftbale", details.isCraftable());
        tagCompound.setInteger("priority", details.getPriority());

        NBTTagCompound inTag = new NBTTagCompound();
        NBTTagCompound outTag = new NBTTagCompound();
        tagCompound.setTag("inTag", inTag);
        tagCompound.setTag("outTag", outTag);

        IAEItemStack[] inputs = details.getInputs();
        int inSize = inputs.length;
        tagCompound.setInteger("inSize", inSize);
        for (int i = 0; i < inSize; i++) {
            IAEItemStack in = inputs[i];
            if (in == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            in.writeToNBT(tag);
            inTag.setTag(String.valueOf(i), tag);
        }

        IAEItemStack[] outputs = details.getOutputs();
        int outSize = outputs.length;
        tagCompound.setInteger("outSize", outSize);
        for (int i = 0; i < outSize; i++) {
            IAEItemStack out = outputs[i];
            if (out == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            out.writeToNBT(tag);
            outTag.setTag(String.valueOf(i), tag);
        }

        stack.stackTagCompound = tagCompound;
        cache.put(details, stack);
        return stack;
    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack itemStack, World world) {
        if (itemStack.hasTagCompound() && itemStack.stackTagCompound.hasKey("class")) {
            NBTTagCompound tagCompound = itemStack.stackTagCompound;
            String className = tagCompound.getString("class");

            if (callCache.containsKey(className)) {
                return callCache.get(className) != null ? callCache.get(className).apply(itemStack) : null;
            } else {
                Function<ItemStack, ICraftingPatternDetails> function = buildFunctionFor(className);
                callCache.put(className, function);
                return function != null ? function.apply(itemStack) : null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Function<ItemStack, ICraftingPatternDetails> buildFunctionFor(String className) {
        try {
            Class<? extends ICraftingPatternDetails> clazz = (Class<? extends ICraftingPatternDetails>) Class.forName(className);
            MethodHandle body = lookup.findConstructor(clazz, MethodType.methodType(void.class, ItemStack.class));
            MethodType type = MethodType.methodType(ICraftingPatternDetails.class, ItemStack.class);
            CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply",
                    MethodType.methodType(Function.class),
                    type.generic(),
                    body,
                    type
            );
            return (Function<ItemStack, ICraftingPatternDetails>) callSite.getTarget().invokeExact();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
