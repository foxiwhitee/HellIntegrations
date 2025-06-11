package foxiwhitee.hellmod.container.terminals;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.container.AEBaseContainer;
import appeng.helpers.InventoryAction;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.InventoryAdaptor;
import appeng.util.Platform;
import appeng.util.inv.AdaptorIInventory;
import appeng.util.inv.AdaptorPlayerHand;
import appeng.util.inv.WrapperInvSlot;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import foxiwhitee.hellmod.helpers.IInterfaceTerminalSupport;
import foxiwhitee.hellmod.helpers.InterfaceTerminalSupportedClassProvider;
import foxiwhitee.hellmod.network.NetworkManager;
import foxiwhitee.hellmod.network.packets.PacketAdvancedInterfaceTerminal;
import foxiwhitee.hellmod.parts.PartAdvancedInterfaceTerminal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import appeng.api.util.DimensionalCoord;
import appeng.util.inv.ItemSlot;
import java.util.Collection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;

public final class ContainerAdvancedInterfaceTerminal extends AEBaseContainer {
    private static long autoBase = Long.MIN_VALUE;

    private final Multimap<IInterfaceTerminalSupport, InvTracker> supportedInterfaces = HashMultimap.<IInterfaceTerminalSupport, InvTracker>create();

    private final Map<Long, InvTracker> byId = new HashMap<>();

    private IGrid grid;

    private NBTTagCompound data = new NBTTagCompound();

    public ContainerAdvancedInterfaceTerminal(InventoryPlayer ip, PartAdvancedInterfaceTerminal anchor) {
        super(ip, anchor);
        if (Platform.isServer())
            this.grid = anchor.getActionableNode().getGrid();
        bindPlayerInventory(ip, 35, -1);
    }

    public void detectAndSendChanges() {
        if (Platform.isClient())
            return;
        super.detectAndSendChanges();
        if (this.grid == null)
            return;
        int total = 0;
        boolean missing = false;
        IActionHost host = getActionHost();
        if (host != null) {
            IGridNode agn = host.getActionableNode();
            if (agn != null && agn.isActive())
                for (Class<? extends IInterfaceTerminalSupport> clz : (Iterable<Class<? extends IInterfaceTerminalSupport>>) InterfaceTerminalSupportedClassProvider.getSupportedClasses()) {
                    for (IGridNode gn : this.grid.getMachines(clz)) {
                        IInterfaceTerminalSupport interfaceTerminalSupport = (IInterfaceTerminalSupport)gn.getMachine();
                        if (!gn.isActive() || !interfaceTerminalSupport.shouldDisplay())
                            continue;
                        Collection<InvTracker> t = this.supportedInterfaces.get(interfaceTerminalSupport);
                        String name = interfaceTerminalSupport.getName();
                        missing = (t.isEmpty() || t.stream().anyMatch(it -> !it.unlocalizedName.equals(name)));
                        total += (interfaceTerminalSupport.getPatternsConfigurations()).length;
                        if (missing)
                            break;
                    }
                    if (missing)
                        break;
                }
        }
        if (total != this.supportedInterfaces.size() || missing) {
            regenList(this.data);
        } else {
            for (InvTracker inv : this.supportedInterfaces.values()) {
                for (int x = 0; x < inv.client.getSizeInventory(); x++) {
                    if (isDifferent(inv.server.getStackInSlot(inv.offset + x), inv.client.getStackInSlot(x)))
                        addItems(this.data, inv, x, 1);
                }
            }
        }
        if (!this.data.hasNoTags()) {
            try {
                NetworkManager.instance
                        .sendToPlayer(new PacketAdvancedInterfaceTerminal(this.data), (EntityPlayerMP)(getPlayerInv()).player);
            } catch (IOException iOException) {}
            this.data = new NBTTagCompound();
        }
    }

    public void doAction(EntityPlayerMP player, InventoryAction action, int slot, long id) {
        InvTracker inv = this.byId.get(Long.valueOf(id));
        if (inv != null) {
            IInventory mySlot;
            InventoryAdaptor playerInvAd, playerInv;
            int x;
            ItemStack is = inv.server.getStackInSlot(slot + inv.offset);
            boolean hasItemInHand = (player.inventory.getItemStack() != null);
            AdaptorPlayerHand adaptorPlayerHand = new AdaptorPlayerHand((EntityPlayer)player);
            WrapperInvSlot slotInv = new PatternInvSlot(inv.server);
            IInventory theSlot = slotInv.getWrapper(slot + inv.offset);
            AdaptorIInventory adaptorIInventory = new AdaptorIInventory(theSlot);
            IInventory interfaceHandler = inv.server;
            boolean canInsert = true;
            switch (action) {
                case PICKUP_OR_SET_DOWN:
                    if (hasItemInHand) {
                        if (!(player.inventory.getItemStack().getItem() instanceof appeng.items.misc.ItemEncodedPattern)) {
                            canInsert = false;
                        }
                        for (int s = 0; s < interfaceHandler.getSizeInventory(); s++) {
                            if (Platform.isSameItemPrecise(interfaceHandler
                                    .getStackInSlot(s), player.inventory
                                    .getItemStack())) {
                                canInsert = false;
                                break;
                            }
                        }
                        if (canInsert) {
                            ItemStack inSlot = theSlot.getStackInSlot(0);
                            if (inSlot == null) {
                                player.inventory.setItemStack(adaptorIInventory.addItems(player.inventory.getItemStack()));
                                break;
                            }
                            inSlot = inSlot.copy();
                            ItemStack inHand = player.inventory.getItemStack().copy();
                            theSlot.setInventorySlotContents(0, null);
                            player.inventory.setItemStack(null);
                            player.inventory.setItemStack(adaptorIInventory.addItems(inHand.copy()));
                            if (player.inventory.getItemStack() == null) {
                                player.inventory.setItemStack(inSlot);
                                break;
                            }
                            player.inventory.setItemStack(inHand);
                            theSlot.setInventorySlotContents(0, inSlot);
                        }
                        break;
                    }
                    mySlot = slotInv.getWrapper(slot + inv.offset);
                    mySlot.setInventorySlotContents(0, adaptorPlayerHand.addItems(mySlot.getStackInSlot(0)));
                    break;
                case SPLIT_OR_PLACE_SINGLE:
                    if (hasItemInHand) {
                        if (!(player.inventory.getItemStack().getItem() instanceof appeng.items.misc.ItemEncodedPattern)) {
                            canInsert = false;
                        }
                        for (int s = 0; s < interfaceHandler.getSizeInventory(); s++) {
                            if (Platform.isSameItemPrecise(interfaceHandler
                                    .getStackInSlot(s), player.inventory
                                    .getItemStack())) {
                                canInsert = false;
                                break;
                            }
                        }
                        if (canInsert) {
                            ItemStack extra = adaptorPlayerHand.removeItems(1, null, null);
                            if (extra != null && !adaptorIInventory.containsItems())
                                extra = adaptorIInventory.addItems(extra);
                            if (extra != null)
                                adaptorPlayerHand.addItems(extra);
                        }
                        break;
                    }
                    if (is != null) {
                        ItemStack extra = adaptorIInventory.removeItems((is.stackSize + 1) / 2, null, null);
                        if (extra != null)
                            extra = adaptorPlayerHand.addItems(extra);
                        if (extra != null)
                            adaptorIInventory.addItems(extra);
                    }
                    break;
                case SHIFT_CLICK:
                    mySlot = slotInv.getWrapper(slot + inv.offset);
                    playerInv = InventoryAdaptor.getAdaptor(player, ForgeDirection.UNKNOWN);
                    mySlot.setInventorySlotContents(0, mergeToPlayerInventory(playerInv, mySlot.getStackInSlot(0)));
                    break;
                case MOVE_REGION:
                    playerInvAd = InventoryAdaptor.getAdaptor(player, ForgeDirection.UNKNOWN);
                    for (x = 0; x < inv.client.getSizeInventory(); x++)
                        inv.server.setInventorySlotContents(x + inv
                                        .offset,
                                mergeToPlayerInventory(playerInvAd, inv.server.getStackInSlot(x + inv.offset)));
                    break;
                case CREATIVE_DUPLICATE:
                    if (player.capabilities.isCreativeMode && !hasItemInHand)
                        player.inventory.setItemStack((is == null) ? null : is.copy());
                    break;
                default:
                    return;
            }
            updateHeld(player);
        }
    }

    private ItemStack mergeToPlayerInventory(InventoryAdaptor playerInv, ItemStack stack) {
        if (stack == null)
            return null;
        for (ItemSlot slot : playerInv) {
            if (Platform.isSameItemPrecise(slot.getItemStack(), stack) &&
                    (slot.getItemStack()).stackSize < slot.getItemStack().getMaxStackSize()) {
                (slot.getItemStack()).stackSize++;
                return null;
            }
        }
        return playerInv.addItems(stack);
    }

    private void regenList(NBTTagCompound data) {
        this.byId.clear();
        this.supportedInterfaces.clear();
        IActionHost host = getActionHost();
        if (host != null) {
            IGridNode agn = host.getActionableNode();
            if (agn != null && agn.isActive())
                for (Class<? extends IInterfaceTerminalSupport> clz : InterfaceTerminalSupportedClassProvider.getSupportedClasses()) {

                    for (IGridNode gn : this.grid.getMachines(clz)) {
                        IInterfaceTerminalSupport terminalSupport = (IInterfaceTerminalSupport)gn.getMachine();
                        if (!gn.isActive() || !terminalSupport.shouldDisplay())
                            continue;
                        IInterfaceTerminalSupport.PatternsConfiguration[] configurations = terminalSupport.getPatternsConfigurations();
                        int ii = 1+1;
                        for (int i = 0; i < configurations.length; i++)
                            this.supportedInterfaces
                                    .put(terminalSupport, new InvTracker(terminalSupport, configurations[i], i));
                    }
                }
        }
        data.setBoolean("clear", true);
        for (InvTracker inv : this.supportedInterfaces.values()) {
            this.byId.put(Long.valueOf(inv.which), inv);
            addItems(data, inv, 0, inv.client.getSizeInventory());
        }
    }

    private boolean isDifferent(ItemStack a, ItemStack b) {
        if (a == null && b == null)
            return false;
        if (a == null || b == null)
            return true;
        return !ItemStack.areItemStacksEqual(a, b);
    }

    private void addItems(NBTTagCompound data, InvTracker inv, int offset, int length) {
        String name = '=' + Long.toString(inv.which, 36);
        NBTTagCompound tag = data.getCompoundTag(name);
        if (tag.hasNoTags()) {
            tag.setLong("sortBy", inv.sortBy);
            tag.setString("un", inv.unlocalizedName);
            tag.setInteger("x", inv.X);
            tag.setInteger("y", inv.Y);
            tag.setInteger("z", inv.Z);
            tag.setInteger("size", inv.client.getSizeInventory());
        }
        for (int x = 0; x < length; x++) {
            NBTTagCompound itemNBT = new NBTTagCompound();
            ItemStack is = inv.server.getStackInSlot(x + offset + inv.offset);
            inv.client.setInventorySlotContents(offset + x, (is == null) ? null : is.copy());
            if (is != null)
                is.writeToNBT(itemNBT);
            tag.setTag(Integer.toString(x + offset), (NBTBase)itemNBT);
        }
        data.setTag(name, (NBTBase)tag);
    }

    private static class InvTracker {
        private final long sortBy;

        private final long which = ContainerAdvancedInterfaceTerminal.autoBase++;

        private final String unlocalizedName;

        private final IInventory client;

        private final IInventory server;

        private final int offset;

        private final int X;

        private final int Y;

        private final int Z;


        public InvTracker(DimensionalCoord coord, long sortValue, IInventory patterns, String unlocalizedName, int offset, int size) {
            this(coord.x, coord.y, coord.z, sortValue, patterns, unlocalizedName, offset, size);
        }

        public InvTracker(int x, int y, int z, long sortValue, IInventory patterns, String unlocalizedName, int offset, int size) {
            this.server = patterns;
            this.client = (IInventory)new AppEngInternalInventory(null, size);
            this.unlocalizedName = unlocalizedName;
            this.sortBy = sortValue + offset << 16L;
            this.offset = offset;
            this.X = x;
            this.Y = y;
            this.Z = z;
        }

        public InvTracker(IInterfaceTerminalSupport terminalSupport, IInterfaceTerminalSupport.PatternsConfiguration configuration, int index) {
            this(terminalSupport
                    .getLocation(), terminalSupport
                    .getSortValue(), terminalSupport
                    .getPatterns(index), terminalSupport
                    .getName(), configuration.offset, configuration.size);
        }
    }

    private static class PatternInvSlot extends WrapperInvSlot {
        public PatternInvSlot(IInventory inv) {
            super(inv);
        }

        public boolean isItemValid(ItemStack itemstack) {
            return (itemstack != null);
        }
    }
}