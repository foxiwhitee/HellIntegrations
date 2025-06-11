package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui.lib;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;


public class StackReference {
    protected int metadata = 0;
    protected int stackSize = 0;
    protected NBTTagCompound nbt = null;
    private ResourceLocation stack;
    private ItemStack itemStack;

    public StackReference(String stackRegName, int stackSize, int metadata, NBTTagCompound nbt) {
        this.stack = new ResourceLocation(stackRegName);
        this.metadata = metadata;
        this.stackSize = stackSize;
        this.nbt = nbt;
        if (stackSize <= 0) {
            this.stackSize = 1;
        }
    }

    public StackReference(ItemStack stack) {
        this(stack.getItem().getUnlocalizedName(), stack.stackSize, stack.getItemDamage(), stack.getTagCompound());
        this.itemStack = stack;
    }


    private StackReference() {
    }

    private static StackReference fromStringOld(String string) {
        if (!string.contains("name:") || !string.contains("size:") || !string.contains(",meta:") || !string.contains(",nbt:")) {
            return null;
        }

        try {
            String name = string.substring(5, string.indexOf(",size:"));
            int size = Integer.parseInt(string.substring(string.indexOf(",size:") + 6, string.indexOf(",meta:")));
            int meta = Integer.parseInt(string.substring(string.indexOf(",meta:") + 6, string.indexOf(",nbt:")));
            NBTTagCompound compound = (NBTTagCompound) JsonToNBT.func_150315_a(string.substring(string.indexOf(",nbt:") + 5));

            return new StackReference(name, size, meta, compound.hasNoTags() ? null : compound);
        } catch (Exception e) {
            System.out.println("An error occurred while generating a StackReference from a string");
            e.printStackTrace();
            return null;
        }
    }

    public static String stackString(ItemStack stack) {
        return (new StackReference(stack)).toString();
    }


    public static StackReference fromString(String string) {
        String stackString;
        if (string.contains("name:") && string.contains("size:")) {
            return fromStringOld(string);
        }

        String workString = string;
        String splitter = ",";


        String countString = "";
        String metaString = "";
        String nbt = "";


        if (!workString.contains(splitter)) {
            stackString = workString;
            workString = "";
        } else {
            stackString = workString.substring(0, workString.indexOf(splitter));
            workString = workString.substring(workString.indexOf(splitter) + splitter.length());
        }

        if (!stackString.contains(":")) {
            System.out.println("StackReference: Was given an invalid stack string. String did not contain \":\" - " + string);
            return null;
        }


        if (workString.length() > 0) {
            if (!workString.contains(splitter)) {
                countString = workString;
                workString = "";
            } else {
                countString = workString.substring(0, workString.indexOf(splitter));
                workString = workString.substring(workString.indexOf(splitter) + splitter.length());
            }
        }

        if (workString.length() > 0) {
            if (!workString.contains(splitter)) {
                metaString = workString;
                workString = "";
            } else {
                metaString = workString.substring(0, workString.indexOf(splitter));
                workString = workString.substring(workString.indexOf(splitter) + splitter.length());
            }
        }

        if (workString.length() > 0) {
            nbt = workString;
        }

        int count = 1;
        int meta = 0;
        NBTTagCompound compound = null;

        if (countString.length() > 0) {
            try {
                count = Integer.parseInt(countString);
            } catch (Exception e) {
                System.out.println("StackReference: Failed to parse stack size from string - " + countString + " error: " + e.getMessage());
            }
        }
        if (metaString.length() > 0) {
            try {
                meta = Integer.parseInt(metaString);
            } catch (Exception e) {
                System.out.println("StackReference: Failed to parse stack meta from string - " + metaString + " error: " + e.getMessage());
            }
        }
        if (nbt.length() > 0) {
            try {
                compound = (NBTTagCompound) JsonToNBT.func_150315_a(nbt);
            } catch (Exception e) {
                System.out.println("StackReference: Failed to parse stack nbt from string - " + nbt + " error: " + e.getMessage());
            }
        }

        return new StackReference(stackString, count, meta, compound);
    }


    public int hashCode() {
        return Objects.hash(new Object[]{this.stack, Integer.valueOf(this.stackSize), Integer.valueOf(this.metadata), this.nbt});
    }

    public ItemStack createStack() {
        ItemStack itemStack;
        if (this.itemStack != null) return this.itemStack;
        Item item = (Item) Item.itemRegistry.getObject(this.stack.toString());
        Block block = (Block) Block.blockRegistry.getObject(this.stack.toString());
        if (item == null && block == Blocks.air) {
            return new ItemStack(Blocks.bedrock);
        }

        if (item != null) {
            itemStack = new ItemStack(item, this.stackSize, this.metadata);
        } else {
            itemStack = new ItemStack(block, this.stackSize, this.metadata);
        }

        if (this.nbt != null) {
            itemStack.setTagCompound((NBTTagCompound) this.nbt.copy());
        }
        return itemStack;
    }


    public NBTTagCompound getNbt() {
        return this.nbt;
    }

    public void setNbt(NBTTagCompound nbt) {
        this.nbt = nbt;
    }

    public int getMetadata() {
        return this.metadata;
    }

    public void setMetadata(int metadata) {
        this.metadata = metadata;
    }

    public int getStackSize() {
        return this.stackSize;
    }

    public void setStack(ResourceLocation stack) {
        this.stack = stack;
    }


    public String toString() {
        if (this.nbt == null) {
            if (this.metadata == 0) {
                if (this.stackSize == 1) {
                    return this.stack.toString();
                }
                return this.stack + "," + this.stackSize;
            }

            return this.stack + "," + this.stackSize + "," + this.metadata;
        }

        return this.stack + "," + this.stackSize + "," + this.metadata + "," + this.nbt;
    }
}

