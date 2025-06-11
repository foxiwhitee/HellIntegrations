package foxiwhitee.hellmod.integration.botania.tile.ae;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import foxiwhitee.hellmod.integration.botania.flowers.*;
import foxiwhitee.hellmod.integration.botania.items.generating.ItemUpgradeGeneratorMultiply;
import foxiwhitee.hellmod.integration.botania.items.generating.ItemUpgradeGeneratorSpeed;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketUpdateMana;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class TileManaGenerator extends TIleAEMana implements IManaGenerator {
    private int level;
    private AppEngInternalInventory inventory;
    private AppEngInternalInventory inventoryConsume;
    private AppEngInternalInventory inventoryConsumeFake;
    private AppEngInternalInventory inventoryUpgrade = new AppEngInternalInventory(this, 4);
    private AppEngInternalInventory inventoryEssence = new AppEngInternalInventory(this, 1);
    private AppEngInternalInventory inventoryCores = new AppEngInternalInventory(this, 1);
    private double multiply = 0;
    private double[] speed;
    private double[] ticks;
    private double[] chanceToFreeItemUse;

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord);
    }


    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_AEMana(NBTTagCompound data) {
        super.readFromNBT_AEMana(data);
        this.level = data.getInteger("level");
        this.multiply = data.getDouble("multiply");
        NBTTagCompound additionalTag = data.getCompoundTag("speed");
        Set<String> set = additionalTag.func_150296_c();
        this.speed = new double[set.size()];
        int i = 0;
        for (String s : set) {
            this.speed[i] = additionalTag.getDouble(s);
            ++i;
        }
        additionalTag = data.getCompoundTag("ticks");
        set = additionalTag.func_150296_c();
        this.ticks = new double[set.size()];
        i = 0;
        for (String s : set) {
            this.ticks[i] = additionalTag.getDouble(s);
            ++i;
        }
        additionalTag = data.getCompoundTag("chanceToFreeItemUse");
        set = additionalTag.func_150296_c();
        this.chanceToFreeItemUse = new double[set.size()];
        i = 0;
        for (String s : set) {
            this.chanceToFreeItemUse[i] = additionalTag.getDouble(s);
            ++i;
        }
        this.inventory.readFromNBT(data, "inv");
        this.inventoryConsume.readFromNBT(data, "invC");
        this.inventoryConsumeFake.readFromNBT(data, "invF");
        this.inventoryUpgrade.readFromNBT(data, "invU");
        this.inventoryEssence.readFromNBT(data, "invE");
        this.inventoryCores.readFromNBT(data, "invCo");

    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_AEMana(NBTTagCompound data) {
        super.writeToNBT_AEMana(data);
        data.setInteger("level", level);
        data.setDouble("multiply", multiply);
        NBTTagCompound additionalTag = new NBTTagCompound();
        for (int i = 0; i < speed.length; i++) {
            additionalTag.setDouble("" + i, speed[i]);
        }
        data.setTag("speed", additionalTag);
        additionalTag = new NBTTagCompound();
        for (int i = 0; i < ticks.length; i++) {
            additionalTag.setDouble("" + i, ticks[i]);
        }
        data.setTag("ticks", additionalTag);
        additionalTag = new NBTTagCompound();
        for (int i = 0; i < chanceToFreeItemUse.length; i++) {
            additionalTag.setDouble("" + i, chanceToFreeItemUse[i]);
        }
        data.setTag("chanceToFreeItemUse", additionalTag);
        inventory.writeToNBT(data, "inv");
        inventoryConsume.writeToNBT(data, "invC");
        inventoryConsumeFake.writeToNBT(data, "invF");
        inventoryUpgrade.writeToNBT(data, "invU");
        inventoryEssence.writeToNBT(data, "invE");
        inventoryCores.writeToNBT(data, "invCo");

    }

    public TileManaGenerator(int level, int inputsCount, long maxStoredMana, int maxItems) {
        this.level = level;
        setMaxStoredMana(maxStoredMana);
        this.inventory = new AppEngInternalInventory(this, inputsCount);
        this.inventoryConsume = new AppEngInternalInventory(this, inputsCount);
        this.inventoryConsumeFake = new AppEngInternalInventory(this, inputsCount);
        inventory.setMaxStackSize(maxItems);
        inventoryConsumeFake.setMaxStackSize(1);
        inventoryUpgrade.setMaxStackSize(1);
        inventoryEssence.setMaxStackSize(1);
        inventoryCores.setMaxStackSize(1);
        speed = new double[inputsCount];
        ticks = new double[inputsCount];
        chanceToFreeItemUse = new double[inputsCount];
    }

    public TickRateModulation tick(IGridNode node, int t) {
        if (Platform.isServer()) {
            ItemStack stack = null;
            ItemStack consume = null;
            ItemStack consumeReal = null;
            IGeneratingFlowerLogic logic = null;
            int sp = 0;
            for (int i = 0; i < ticks.length; i++) {
                stack = inventory.getStackInSlot(i);
                if (stack != null) {
                    logic = ((ICoreGeneratingFlower) inventory.getStackInSlot(i).getItem()).getLogic(((ICoreGeneratingFlower) inventory.getStackInSlot(i).getItem()).getName(inventory.getStackInSlot(i)));
                    if (logic != null && logic.getDefaultNeedTicks() != 0 && speed.length > i) {
                        ticks[i]++;
                        sp = (int) (logic.getDefaultNeedTicks() * 1024 - logic.getDefaultNeedTicks() * speed[i]);
                        if (ticks[i] > speed[i]) {
                            ticks[i] = 0;
                            for (int j = 0; j < stack.stackSize; j++) {
                                if (logic.getLevel() <= this.getLevel()) {
                                    logic.generate(this, i);
                                }
                            }
                        }
                    }
                    consume = inventoryConsumeFake.getStackInSlot(i);
                    consumeReal = inventoryConsume.getStackInSlot(i);
                    if (consume != null && (consumeReal == null || (consumeReal != null && consumeReal.stackSize < inventoryConsume.getInventoryStackLimit()))) {
                        try {
                            AEItemStack item = AEItemStack.create(consume);
                            item.setStackSize(consumeReal == null ? 64 : inventoryConsume.getInventoryStackLimit() - consumeReal.stackSize);
                            IAEItemStack remainder = getProxy().getStorage().getItemInventory().extractItems(item.copy(), Actionable.SIMULATE, getSource());
                            if (remainder != null && remainder.getStackSize() > 0) {
                                IAEItemStack items = getProxy().getStorage().getItemInventory().extractItems(remainder.copy(), Actionable.MODULATE, getSource());
                                if (consumeReal == null) {
                                    inventoryConsume.setInventorySlotContents(i, items.getItemStack());
                                } else if (simpleAreStacksEqual(consumeReal, items.getItemStack())) {
                                    consumeReal.stackSize += (int) items.getStackSize();
                                    inventoryConsume.setInventorySlotContents(i, consumeReal.copy());
                                }
                            }
                        } catch (GridAccessException e) {
                        }
                    }
                }
            }
            injectMana(this.getStoredMana());
            markDirty();
            markForUpdate();
            NetworkManager.instance.sendToAll(new PacketUpdateMana(this));
        }
        return TickRateModulation.IDLE;
    }

    private boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }

    @Override
    public boolean consumeItem(int slot) {
        ItemStack stack = inventoryConsume.getStackInSlot(slot);
        if (stack != null) {
            List<IAEStack> can = ((ICoreGeneratingFlower)inventory.getStackInSlot(slot).getItem()).getLogic(((ICoreGeneratingFlower) inventory.getStackInSlot(slot).getItem()).getName(inventory.getStackInSlot(slot))).getConsumed();
            IAEItemStack iaeItemStack = null;
            boolean is = false;
            if (can.isEmpty()) {
                is = true;
                iaeItemStack = AEItemStack.create(stack);
                iaeItemStack.setStackSize(1);
            }

            for (IAEStack iae : can) {
                if (((IAEItemStack)iae).equals(AEItemStack.create(stack))) {
                    iaeItemStack = (IAEItemStack) iae;
                    is = true;
                    break;
                }
            }
            if (is && iaeItemStack != null) {
                if (stack.stackSize >= iaeItemStack.getStackSize()) {
                    if (Math.random() < chanceToFreeItemUse[slot]) {
                        return true;
                    }
                    if (stack.stackSize > iaeItemStack.getStackSize()) {
                        stack.stackSize -= (int) iaeItemStack.getStackSize();
                        inventoryConsume.setInventorySlotContents(slot, stack.copy());
                    } else {
                        inventoryConsume.setInventorySlotContents(slot, null);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public long calculateEffectsGenerating(long generating, int slot) {
        double result = 0;
        double essence = 0;
        double core = 0;
        double effect;
        String m;
        ItemStack essenceOfLife = inventoryEssence.getStackInSlot(0);
        ItemStack coreOfLife = inventoryCores.getStackInSlot(0);

        if (essenceOfLife != null && coreOfLife != null) {
            NBTTagCompound tagE = essenceOfLife.getTagCompound();
            NBTTagCompound tagC = coreOfLife.getTagCompound();
            if (tagE != null && tagC != null) {
                Set<String> keys = tagE.func_150296_c();
                for (String entry : keys) {
                    if (Objects.equals(entry, "rarity")) {
                        continue;
                    }
                    effect = tagE.getDouble(entry);
                    m = entry.replaceAll("[\\+-\\.0-9]", "");
                    ItemStack coreFlower = inventory.getStackInSlot(slot);
                    if (coreFlower != null) {
                        if (entry.contains(".0") || GeneratingFlowersRegister.getNameByIndex(Integer.parseInt(entry.split("\\.")[1])).equals(((ICoreGeneratingFlower) coreFlower.getItem()).getName(coreFlower))) {
                            switch (m) {
                                case "bonus":
                                case "exp_bonus":
                                    essence += effect;
                                    break;
                                case "day_bonus":
                                    if (isDay()) {
                                        essence += effect;
                                    }
                                    break;
                                case "night_bonus":
                                    if (isNight()) {
                                        essence += effect;
                                    }
                                    break;
                                case "rain_bonus":
                                    if (isRaining()) {
                                        essence += effect;
                                    }
                                    break;
                                case "flame_material_bonus":
                                    IGeneratingFlowerLogic logic = ((ICoreGeneratingFlower) coreFlower.getItem()).getLogic(((ICoreGeneratingFlower) coreFlower.getItem()).getName(coreFlower));
                                    if (logic != null && logic.getTypes() != null) {
                                        if (
                                                logic.getTypes().contains(LevelTypes.BURNING) ||
                                                        logic.getTypes().contains(LevelTypes.DESTRUCTIVE) ||
                                                        logic.getTypes().contains(LevelTypes.ETERNAL_SOLSTICE) ||
                                                        logic.getTypes().contains(LevelTypes.ASGARD)
                                        ) {
                                            core += effect;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
                keys = tagC.func_150296_c();
                for (String entry : keys) {
                    if (Objects.equals(entry, "rarity")) {
                        continue;
                    }
                    m = entry.replaceAll("[\\+-\\.0-9]", "");
                    effect = tagC.getDouble(entry);
                    ItemStack coreFlower = inventory.getStackInSlot(slot);
                    if (coreFlower != null) {
                        if (entry.contains(".0") || GeneratingFlowersRegister.getNameByIndex(Integer.parseInt(entry.split("\\.")[1])).equals(((ICoreGeneratingFlower) coreFlower.getItem()).getName(coreFlower))) {
                            switch (m) {
                                case "bonus":
                                case "exp_bonus":
                                    core += effect;
                                    break;
                                case "day_bonus":
                                    if (isDay()) {
                                        core += effect;
                                    }
                                    break;
                                case "night_bonus":
                                    if (isNight()) {
                                        core += effect;
                                    }
                                    break;
                                case "rain_bonus":
                                    if (isRaining()) {
                                        core += effect;
                                    }
                                    break;
                                case "flame_material_bonus":
                                    IGeneratingFlowerLogic logic = ((ICoreGeneratingFlower) coreFlower.getItem()).getLogic(((ICoreGeneratingFlower) coreFlower.getItem()).getName(coreFlower));
                                    if (logic != null && logic.getTypes() != null) {
                                        if (
                                                logic.getTypes().contains(LevelTypes.BURNING) ||
                                                        logic.getTypes().contains(LevelTypes.DESTRUCTIVE) ||
                                                        logic.getTypes().contains(LevelTypes.ETERNAL_SOLSTICE) ||
                                                        logic.getTypes().contains(LevelTypes.ASGARD)
                                        ) {
                                            core += effect;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }
        else {
            return 0;
        }

        result += generating;
        result += (generating * (essence + core));
        if (multiply > 0.0D) {
            result *= multiply > 0.0D ? multiply : 1;
        }
        return (long) Math.min(Long.MAX_VALUE, result);

    }

    @Override
    public ItemStack getStackInSLot(int slot) {
        return inventoryConsume.getStackInSlot(slot);
    }

    public int getLevel() { return level; }

    public AppEngInternalInventory getInventory() { return inventory; }

    public AppEngInternalInventory getInventoryConsume() {
        return inventoryConsume;
    }

    public AppEngInternalInventory getInventoryConsumeFake() {
        return inventoryConsumeFake;
    }

    public AppEngInternalInventory getInventoryCores() {
        return inventoryCores;
    }

    public AppEngInternalInventory getInventoryEssence() {
        return inventoryEssence;
    }

    public AppEngInternalInventory getInventoryUpgrade() {
        return inventoryUpgrade;
    }

    public void onChangeInventory(IInventory inv, int slot, InvOperation op, ItemStack remove, ItemStack add) {
        if (inv == inventoryUpgrade) {
            updateSpeedAndMultiplyInventory();
        } else if (inv == inventoryEssence || inv == inventoryCores) {
            updateSpeedAndMultiplyInventory();
            updateChanceToFreeItemUse();
        } else if (inv == inventoryConsumeFake) {
            ItemStack stack;
            for (int i = 0; i < inventoryConsume.getSizeInventory(); i++) {
                stack = inventoryConsume.getStackInSlot(i);
                if (stack != null) {
                    try {
                        AEItemStack item = AEItemStack.create(stack);
                        IAEItemStack remainder = getProxy().getStorage().getItemInventory().injectItems(item.copy(), Actionable.SIMULATE, getSource());
                        if (remainder != null && remainder.getStackSize() > 0) {
                            getProxy().getStorage().getItemInventory().injectItems(remainder.copy(), Actionable.MODULATE, getSource());
                            if (stack.stackSize == item.getStackSize()) {
                                inventoryConsume.setInventorySlotContents(i, null);
                            } else {
                                stack.stackSize -= (int) remainder.getStackSize();
                                inventoryConsume.setInventorySlotContents(i, stack.copy());
                            }
                        }
                    } catch (GridAccessException e) {}
                }
            }
        }
        markDirty();
    }

    private void updateChanceToFreeItemUse() {
        Arrays.fill(chanceToFreeItemUse, 0);
        chanceToFreeItemUse = getEffectsFromEveryCore("chance_to_free_item_use", chanceToFreeItemUse);
    }

    private void updateSpeedAndMultiplyInventory() {
        multiply = 0;
        Arrays.fill(speed, 0);
        ItemStack stack;
        for (int i = 0; i < inventoryUpgrade.getSizeInventory(); i++) {
            stack = inventoryUpgrade.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof ItemUpgradeGeneratorSpeed) {
                    Arrays.fill(speed, speed[0] + (1.5 * ( stack.getItemDamage() + 1)));
                } else if (stack.getItem() instanceof ItemUpgradeGeneratorMultiply) {
                    multiply += stack.getItemDamage() == 2 ? multiply + 2 : (stack.getItemDamage() == 1 ? multiply + 1.6 : multiply + 1.2);
                }
            }
        }

        speed = getEffectsFromEveryCore("speed_boost", speed);
    }

    private double[] getEffectsFromEveryCore(String name, double[] old) {
        double[] result = old.clone();
        String[] names = new String[inventory.getSizeInventory()];
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i) != null) {
                names[i] = ((ICoreGeneratingFlower) inventory.getStackInSlot(i).getItem()).getName(inventory.getStackInSlot(i));
            } else {
                names[i] = "null";
            }
        }
        ItemStack essenceOfLife = inventoryEssence.getStackInSlot(0);
        ItemStack coreOfLife = inventoryCores.getStackInSlot(0);
        if (essenceOfLife != null && coreOfLife != null) {
            NBTTagCompound tagE = essenceOfLife.getTagCompound();
            NBTTagCompound tagC = coreOfLife.getTagCompound();
            if (tagE != null && tagC != null) {
                Set<String> keysE = tagE.func_150296_c();
                Set<String> keysC = tagC.func_150296_c();
                for (int i = 0; i < result.length; i++) {
                    for (String entry : keysE) {
                        if (entry.contains(name)) {
                            if (entry.contains(".0") || GeneratingFlowersRegister.getNameByIndex(Integer.parseInt(entry.split("\\.")[1])).equals(names[i])) {
                                result[i] += tagE.getDouble(entry);
                            }
                        }
                    }
                    for (String entry : keysC) {
                        if (entry.contains(name)) {
                            if (entry.contains(".0") || GeneratingFlowersRegister.getNameByIndex(Integer.parseInt(entry.split("\\.")[1])).equals(names[i])) {
                                result[i] += tagC.getDouble(entry);
                            }
                        }
                    }

                }
            }
        }
        return result;
    }

    public TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(1, 5, false, false);
    }

    protected boolean isDay() {
        long time = worldObj.getWorldTime() % 24000;
        return time >= 0 && time < 12000;
    }

    protected boolean isNight() {
        long time = worldObj.getWorldTime() % 24000;
        return time >= 12000 && time < 24000;
    }

    protected boolean isRaining() {
        return worldObj.isRaining();
    }

    @Override
    public void getDrops(World w, int x, int y, int z, List<ItemStack> drops) {
        ItemStack item;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            item = inventory.getStackInSlot(i);
            if (item != null) {
                drops.add(item);
            }
        }

        for (int i = 0; i < inventoryConsume.getSizeInventory(); i++) {
            item = inventoryConsume.getStackInSlot(i);
            if (item != null) {
                drops.add(item);
            }
        }

        for (int i = 0; i < inventoryUpgrade.getSizeInventory(); i++) {
            item = inventoryUpgrade.getStackInSlot(i);
            if (item != null) {
                drops.add(item);
            }
        }

        for (int i = 0; i < inventoryCores.getSizeInventory(); i++) {
            item = inventoryCores.getStackInSlot(i);
            if (item != null) {
                drops.add(item);
            }
        }

        for (int i = 0; i < inventoryEssence.getSizeInventory(); i++) {
            item = inventoryEssence.getStackInSlot(i);
            if (item != null) {
                drops.add(item);
            }
        }

    }
}