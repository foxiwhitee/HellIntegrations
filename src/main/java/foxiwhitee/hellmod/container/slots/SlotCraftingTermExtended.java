package foxiwhitee.hellmod.container.slots;

import appeng.api.config.Actionable;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.container.ContainerNull;
import appeng.container.slot.SlotCraftingTerm;
import appeng.helpers.IContainerCraftingPacket;
import appeng.helpers.InventoryAction;
import appeng.util.InventoryAdaptor;
import appeng.util.Platform;
import appeng.util.inv.AdaptorPlayerHand;
import appeng.util.item.AEItemStack;
import appeng.util.prioitylist.IPartitionList;
import appeng.util.prioitylist.MergedPriorityList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import java.util.ArrayList;
import java.util.List;

import foxiwhitee.hellmod.integration.avaritia.helpers.BigPatternHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotCraftingTermExtended extends SlotCraftingTerm {
    private final IInventory craftInv1;

    private final IInventory pattern1;

    private final BaseActionSource mySrc1;

    private final IEnergySource energySrc1;

    private final IStorageMonitorable storage1;

    private final IContainerCraftingPacket container1;

    private final EntityPlayer thePlayer;

    private final int width;

    private final int height;

    public SlotCraftingTermExtended(EntityPlayer player, BaseActionSource mySrc, IEnergySource energySrc, IStorageMonitorable storage, IInventory cMatrix, IInventory secondMatrix, IInventory output, int x, int y, IContainerCraftingPacket ccp, int width, int height) {
        super(player, mySrc, energySrc, storage, cMatrix, secondMatrix, output, x, y, ccp);
        this.energySrc1 = energySrc;
        this.storage1 = storage;
        this.mySrc1 = mySrc;
        this.pattern1 = cMatrix;
        this.craftInv1 = secondMatrix;
        this.container1 = ccp;
        this.thePlayer = player;
        this.width = width;
        this.height = height;
    }

    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
        FMLCommonHandler.instance().firePlayerCraftingEvent(par1EntityPlayer, par2ItemStack, this.craftInv1);
        onCrafting(par2ItemStack);
        for (int i = 0; i < this.craftInv1.getSizeInventory(); i++) {
            ItemStack itemstack1 = this.craftInv1.getStackInSlot(i);
            if (itemstack1 != null) {
                this.craftInv1.decrStackSize(i, 1);
                if (itemstack1.getItem().hasContainerItem(itemstack1)) {
                    ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);
                    if (itemstack2 != null && itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {
                        MinecraftForge.EVENT_BUS.post((Event)new PlayerDestroyItemEvent(this.thePlayer, itemstack2));
                    } else if (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1) || !this.thePlayer.inventory.addItemStackToInventory(itemstack2)) {
                        if (this.craftInv1.getStackInSlot(i) == null) {
                            this.craftInv1.setInventorySlotContents(i, itemstack2);
                        } else {
                            this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
                        }
                    }
                }
            }
        }
    }

    public void doClick(InventoryAction action, EntityPlayer who) {
        AdaptorPlayerHand adaptorPlayerHand = null;
        if (getStack() == null)
            return;
        if (Platform.isClient())
            return;
        IMEMonitor<IAEItemStack> inv = this.storage1.getItemInventory();
        int howManyPerCraft = (getStack()).stackSize;
        int maxTimesToCraft = 0;
        InventoryAdaptor ia = null;
        if (action == InventoryAction.CRAFT_SHIFT) {
            ia = InventoryAdaptor.getAdaptor(who, null);
            maxTimesToCraft = (int)Math.floor(getStack().getMaxStackSize() / howManyPerCraft);
        } else if (action == InventoryAction.CRAFT_STACK) {
            adaptorPlayerHand = new AdaptorPlayerHand(who);
            maxTimesToCraft = (int)Math.floor(getStack().getMaxStackSize() / howManyPerCraft);
        } else {
            adaptorPlayerHand = new AdaptorPlayerHand(who);
            maxTimesToCraft = 1;
        }
        maxTimesToCraft = capCraftingAttempts(maxTimesToCraft);
        if (adaptorPlayerHand == null)
            return;
        ItemStack rs = Platform.cloneItemStack(getStack());
        if (rs == null)
            return;
        for (int x = 0; x < maxTimesToCraft; x++) {
            if (adaptorPlayerHand.simulateAdd(rs) == null) {
                IItemList<IAEItemStack> all = inv.getStorageList();
                ItemStack extra = adaptorPlayerHand.addItems(craftItem(who, rs, inv, all));
                if (extra != null) {
                    List<ItemStack> drops = new ArrayList<>();
                    drops.add(extra);
                    Platform.spawnDrops(who.worldObj, (int)who.posX, (int)who.posY, (int)who.posZ, drops);
                    return;
                }
            }
        }
    }

    private int capCraftingAttempts(int maxTimesToCraft) {
        return maxTimesToCraft;
    }

    private ItemStack craftItem(EntityPlayer p, ItemStack request, IMEMonitor<IAEItemStack> inv, IItemList all) {
        ItemStack is = getStack();
        if (is != null && Platform.isSameItem(request, is)) {
            ItemStack[] set = new ItemStack[getPattern().getSizeInventory()];
            if (Platform.isServer()) {
                InventoryCrafting ic = new InventoryCrafting((Container)new ContainerNull(), this.width, this.height);
                for (int x = 0; x < this.width * this.height; x++)
                    ic.setInventorySlotContents(x, getPattern().getStackInSlot(x));
                IRecipe r = BigPatternHelper.findMatchingRecipe(ic, p.worldObj);
                if (r == null) {
                    Item target = request.getItem();
                    if (target.isDamageable() && target.isRepairable()) {
                        boolean isBad = false;
                        for (int i = 0; i < ic.getSizeInventory(); i++) {
                            ItemStack pis = ic.getStackInSlot(i);
                            if (pis != null)
                                if (pis.getItem() != target)
                                    isBad = true;
                        }
                        if (!isBad) {
                            onPickupFromSlot(p, is);
                            p.openContainer.onCraftMatrixChanged(this.craftInv1);
                            return request;
                        }
                    }
                    return null;
                }
                is = r.getCraftingResult(ic);
                if (inv != null)
                    for (int i = 0; i < getPattern().getSizeInventory(); i++) {
                        if (getPattern().getStackInSlot(i) != null) {
                            set[i] = Platform.extractItemsByRecipe(this.energySrc1, this.mySrc1, inv, p.worldObj, r, is, ic, getPattern().getStackInSlot(i), i, all, Actionable.MODULATE, (IPartitionList)new MergedPriorityList());
                            ic.setInventorySlotContents(i, set[i]);
                        }
                    }
            }
            if (preCraft(p, inv, set, is)) {
                makeItem(p, is);
                postCraft1(p, inv, set, is);
            }
            p.openContainer.onCraftMatrixChanged(this.craftInv1);
            return is;
        }
        return null;
    }

    private boolean preCraft(EntityPlayer p, IMEMonitor<IAEItemStack> inv, ItemStack[] set, ItemStack result) {
        return true;
    }

    private void makeItem(EntityPlayer p, ItemStack is) {
        onPickupFromSlot(p, is);
    }

    private void postCraft1(EntityPlayer p, IMEMonitor<IAEItemStack> inv, ItemStack[] set, ItemStack result) {
        List<ItemStack> drops = new ArrayList<>();
        if (Platform.isServer())
            for (int x = 0; x < this.craftInv1.getSizeInventory(); x++) {
                if (this.craftInv1.getStackInSlot(x) == null) {
                    this.craftInv1.setInventorySlotContents(x, set[x]);
                } else if (set[x] != null) {
                    IAEItemStack fail = (IAEItemStack)inv.injectItems((IAEItemStack) AEItemStack.create(set[x]), Actionable.MODULATE, this.mySrc1);
                    if (fail != null)
                        drops.add(fail.getItemStack());
                }
            }
        if (drops.size() > 0)
            Platform.spawnDrops(p.worldObj, (int)p.posX, (int)p.posY, (int)p.posZ, drops);
    }

    IInventory getPattern() {
        return this.pattern1;
    }
}
