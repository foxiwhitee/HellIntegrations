package foxiwhitee.hellmod.integration.nei.draconic.assembler;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.recipe.IRecipeHandler;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketDrAssemblerRecipe;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class DrAssemblerRecipeOverlayHandler implements IOverlayHandler {
    public void overlayRecipe(GuiContainer firstGui, IRecipeHandler recipe, int recipeIndex, boolean shift) {
        List<PositionedStack> items = recipe.getIngredientStacks(recipeIndex);
        Map<Integer, List<NBTTagCompound>> stacks = new HashMap<>();
        int count = 0;
        for (PositionedStack pStack : items) {
            if (pStack == null)
                continue;
            List<NBTTagCompound> nbts = Arrays.stream(pStack.items)
                    .filter(Objects::nonNull)
                    .map(stack -> {
                        NBTTagCompound compound = new NBTTagCompound();
                        stack.writeToNBT(compound);
                        return compound;
                    })
                    .collect(Collectors.toList());
            stacks.put(count++, nbts);
        }
        NBTTagCompound tag = new NBTTagCompound();
        for (int i = 0; i < 7; i++) {
            List<NBTTagCompound> list = stacks.getOrDefault(i, Collections.emptyList());
            if (!list.isEmpty()) {
                tag.setTag("#" + i, list.get(0));
            } else {
                tag.setTag("#" + i, new NBTTagCompound());
            }
        }
        NetworkManager.instance.sendToServer(new PacketDrAssemblerRecipe(tag));
    }
}
