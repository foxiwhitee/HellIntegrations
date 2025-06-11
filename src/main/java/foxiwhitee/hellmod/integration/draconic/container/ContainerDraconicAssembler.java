package foxiwhitee.hellmod.integration.draconic.container;

import foxiwhitee.hellmod.container.HellBaseContainer;
import foxiwhitee.hellmod.container.slots.HellSlot;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import foxiwhitee.hellmod.integration.draconic.container.slots.DraconicAssemblerSlot;
import foxiwhitee.hellmod.integration.draconic.container.slots.DraconicAssemblerUpgradeSlot;
import foxiwhitee.hellmod.integration.draconic.items.ItemDraconicEnergyUpgrades;
import foxiwhitee.hellmod.integration.draconic.tile.TileDraconicAssembler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerDraconicAssembler extends HellBaseContainer {
    private final TileDraconicAssembler tile;
    private final Slot[] up;
    private final Slot[] inv;
    public ContainerDraconicAssembler(EntityPlayer ip, TileDraconicAssembler myTile) {
        super(ip.inventory, myTile);
        this.tile = myTile;
        this.up = new DraconicAssemblerUpgradeSlot[tile.getUpgrades().getSizeInventory()];
        this.inv = new DraconicAssemblerSlot[tile.getInv().getSizeInventory()];
        this.bindPlayerInventory(ip.inventory, 172, 284);
        this.addSlotToContainer(new SlotFurnace(ip.inventory.player, myTile.getOut(), 0, 252, 220));
        this.addSlotToContainer(inv[0] = new DraconicAssemblerSlot(myTile.getInv(), 0, 227, 159));
        this.addSlotToContainer(inv[1] = new DraconicAssemblerSlot(myTile.getInv(), 1, 252, 159));
        this.addSlotToContainer(inv[2] = new DraconicAssemblerSlot(myTile.getInv(), 2, 277, 159));
        this.addSlotToContainer(inv[3] = new DraconicAssemblerSlot(myTile.getInv(), 3, 214, 184));
        this.addSlotToContainer(inv[4] = new DraconicAssemblerSlot(myTile.getInv(), 4, 239, 184));
        this.addSlotToContainer(inv[5] = new DraconicAssemblerSlot(myTile.getInv(), 5, 264, 184));
        this.addSlotToContainer(inv[6] = new DraconicAssemblerSlot(myTile.getInv(), 6, 289, 184));

        this.addSlotToContainer(up[0] = new DraconicAssemblerUpgradeSlot(myTile.getUpgrades(), 0, -9000, 169));
        this.addSlotToContainer(up[1] = new DraconicAssemblerUpgradeSlot(myTile.getUpgrades(), 1, -9000, 194));
        this.addSlotToContainer(up[2] = new DraconicAssemblerUpgradeSlot(myTile.getUpgrades(), 2, -9000, 219));
    }

    public Slot[] getUpgradesSlots() {
        return up;
    }

    public Slot[] getInventorySlots() {
        return inv;
    }

    public ItemStack slotClick(int slot, int op, int mode, EntityPlayer player) {
        if (mode == 1 && slot >= 0 && slot < inventorySlots.size()) {
            Slot s = (Slot) inventorySlots.get(slot);
            ItemStack cursorStack = player.inventory.getItemStack();

            if (cursorStack != null && s.inventory == player.inventory) {
                ItemStack remaining = tryInsertIntoUpgradeInventory(cursorStack.copy());
                if (remaining == null || remaining.stackSize == 0) {
                    player.inventory.setItemStack(null);
                } else {
                    player.inventory.setItemStack(remaining);
                }
                return null;
            }
        }
        if (slot >= 0 && slot < this.inventorySlots.size()) {
            Slot s = (Slot) this.inventorySlots.get(slot);
            if (s instanceof DraconicAssemblerSlot) {
                ItemStack is = player.inventory.getItemStack();

                if (is != null && is.getItem() == DraconicEvolutionIntegration.draconicAssemblerUpgrades && is.getItemDamage() >= 2) {
                    ItemStack ret = this.tile.installUpgrade(s.getSlotIndex(), is.copy());
                    player.inventory.setItemStack(ret);
                    s.onSlotChanged();
                    return ret;
                }

                if (is != null && s.isItemValid(is)) {
                    ItemStack slotStack = s.getStack();
                    if (mode == 0 && op == 0) {
                        if (slotStack != null && slotStack.isItemEqual(is) && ItemStack.areItemStackTagsEqual(slotStack, is) &&
                                slotStack.stackSize < s.getSlotStackLimit()) {
                            int space = s.getSlotStackLimit() - slotStack.stackSize;
                            int toAdd = Math.min(space, is.stackSize);
                            slotStack.stackSize += toAdd;
                            is.stackSize -= toAdd;
                            if (is.stackSize <= 0) {
                                player.inventory.setItemStack(null);
                            }
                            s.putStack(slotStack);
                            s.onSlotChanged();
                            return is;
                        } else if (slotStack == null) {
                            s.putStack(is.copy());
                            player.inventory.setItemStack(null);
                            s.onSlotChanged();
                            return null;
                        } else {
                            player.inventory.setItemStack(slotStack.copy());
                            s.putStack(is.copy());
                            s.onSlotChanged();
                            return is;
                        }
                    } else if (mode == 0 && op == 1) {
                        if (slotStack == null) {
                            ItemStack toPlace = is.copy();
                            toPlace.stackSize = 1;
                            s.putStack(toPlace);
                            is.stackSize--;
                            if (is.stackSize <= 0) {
                                player.inventory.setItemStack(null);
                            }
                            s.onSlotChanged();
                            return null;
                        } else if (slotStack.isItemEqual(is) && ItemStack.areItemStackTagsEqual(slotStack, is) &&
                                slotStack.stackSize < s.getSlotStackLimit()) {
                            slotStack.stackSize++;
                            is.stackSize--;
                            if (is.stackSize <= 0) {
                                player.inventory.setItemStack(null);
                            }
                            s.putStack(slotStack);
                            s.onSlotChanged();
                            return null;
                        }
                    }
                }

                if (is == null && s.getHasStack()) {
                    ItemStack slotStack = s.getStack();
                    if (mode == 1) {
                        ItemStack result = transferStackInSlot(player, slot);
                        if (result != null) {
                            s.onSlotChanged();
                        }
                        return result;
                    } else if (mode == 0 && op == 0) {
                        player.inventory.setItemStack(slotStack.copy());
                        s.putStack(null);
                        s.onSlotChanged();
                        return null;
                    } else if (mode == 0 && op == 1) {
                        int halfSize = (slotStack.stackSize + 1) / 2;
                        ItemStack toTake = slotStack.copy();
                        toTake.stackSize = halfSize;
                        slotStack.stackSize -= halfSize;
                        player.inventory.setItemStack(toTake);
                        if (slotStack.stackSize <= 0) {
                            s.putStack(null);
                        } else {
                            s.putStack(slotStack);
                        }
                        s.onSlotChanged();
                        return null;
                    }
                }
                return is;
            }
        }
        return super.slotClick(slot, op, mode, player);
    }

    public TileDraconicAssembler getTile() {
        return tile;
    }

    private ItemStack tryInsertIntoUpgradeInventory(ItemStack stack) {
        for (int i = 0; i < tile.getUpgrades().getSizeInventory(); i++) {
            ItemStack existing = tile.getUpgrades().getStackInSlot(i);
            if (existing == null) {
                ItemStack toInsert = stack.copy();
                toInsert.stackSize = Math.min(toInsert.stackSize, toInsert.getMaxStackSize());
                tile.getUpgrades().setInventorySlotContents(i, toInsert);
                return null;
            } else if (existing.getItem() == stack.getItem() && existing.getItemDamage() == stack.getItemDamage() && existing.stackSize < existing.getMaxStackSize()) {
                int space = existing.getMaxStackSize() - existing.stackSize;
                int toAdd = Math.min(space, stack.stackSize);
                existing.stackSize += toAdd;
                tile.getUpgrades().setInventorySlotContents(i, existing);
                if (toAdd == stack.stackSize) {
                    return null;
                } else {
                    stack.stackSize -= toAdd;
                    return stack;
                }
            }
        }
        return stack;
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        if (slot < 36) {
            ItemStack playerInventoryItemStack = player.inventory.getStackInSlot(slot);
            if (playerInventoryItemStack == null)
                return null;
            boolean validUpgrade = (playerInventoryItemStack.getItem() == DraconicEvolutionIntegration.draconicAssemblerUpgrades &&
                    playerInventoryItemStack.getItemDamage() >= 0 &&
                    playerInventoryItemStack.getItemDamage() <= 1) || playerInventoryItemStack.getItem() instanceof ItemDraconicEnergyUpgrades;
            if (validUpgrade) {
                int internalInventorySize = tile.getUpgrades().getSizeInventory();
                for (int slotIndex = 0; slotIndex < internalInventorySize; slotIndex++) {
                    ItemStack hasStack = tile.getUpgrades().getStackInSlot(slotIndex);
                    if (hasStack == null) {
                        if (playerInventoryItemStack.stackSize > 1) {
                            playerInventoryItemStack.stackSize--;
                            ItemStack toExtract = playerInventoryItemStack.copy();
                            toExtract.stackSize = 1;
                            tile.getUpgrades().setInventorySlotContents(slotIndex, toExtract);
                            break;
                        }
                        tile.getUpgrades().setInventorySlotContents(slotIndex, playerInventoryItemStack);
                        player.inventory.setInventorySlotContents(slot, null);
                        break;
                    }
                }
            } else if (playerInventoryItemStack.getItem() == DraconicEvolutionIntegration.draconicAssemblerUpgrades &&
                    playerInventoryItemStack.getItemDamage() >= 2) {
                return null;
            } else {
                int internalInventorySize = tile.getInv().getSizeInventory();
                for (int slotIndex = 0; slotIndex < internalInventorySize; slotIndex++) {
                    ItemStack hasStack = tile.getInv().getStackInSlot(slotIndex);
                    if (hasStack == null) {
                        tile.getInv().setInventorySlotContents(slotIndex, playerInventoryItemStack);
                        player.inventory.setInventorySlotContents(slot, null);
                        break;
                    }
                }
            }
        }
        if (slot >= 44 && slot < 47) {
            try {
                ItemStack tileEntityItemStack = tile.getUpgrades().getStackInSlot(slot - 44);
                if (tileEntityItemStack != null) {
                    int playerInventorySize = player.inventory.getSizeInventory() - 4;
                    for (int slotIndex = 0; slotIndex < playerInventorySize; slotIndex++) {
                        ItemStack playerInventoryStack = player.inventory.getStackInSlot(slotIndex);
                        if (playerInventoryStack == null) {
                            player.inventory.setInventorySlotContents(slotIndex, tileEntityItemStack);
                            tile.getUpgrades().setInventorySlotContents(slot - 44, null);
                            break;
                        }
                        if (playerInventoryStack.getItem() == tileEntityItemStack.getItem() &&
                                playerInventoryStack.getItemDamage() == tileEntityItemStack.getItemDamage() &&
                                ItemStack.areItemStackTagsEqual(tileEntityItemStack, playerInventoryStack) &&
                                playerInventoryStack.stackSize < playerInventoryStack.getMaxStackSize()) {
                            playerInventoryStack.stackSize++;
                            tile.getUpgrades().setInventorySlotContents(slot - 44, null);
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (slot == 36) {
            try {
                ItemStack tileEntityItemStack = tile.getOut().getStackInSlot(0);
                if (tileEntityItemStack != null) {
                    int playerInventorySize = player.inventory.getSizeInventory() - 4;
                    for (int slotIndex = 0; slotIndex < playerInventorySize; slotIndex++) {
                        ItemStack playerInventoryStack = player.inventory.getStackInSlot(slotIndex);
                        if (playerInventoryStack == null) {
                            player.inventory.setInventorySlotContents(slotIndex, tileEntityItemStack);
                            tile.getOut().setInventorySlotContents(0, null);
                            break;
                        }
                        if (playerInventoryStack.getItem() == tileEntityItemStack.getItem() &&
                                playerInventoryStack.getItemDamage() == tileEntityItemStack.getItemDamage() &&
                                ItemStack.areItemStackTagsEqual(tileEntityItemStack, playerInventoryStack) &&
                                playerInventoryStack.stackSize < playerInventoryStack.getMaxStackSize()) {
                            playerInventoryStack.stackSize++;
                            tile.getOut().setInventorySlotContents(0, null);
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (slot >= 37 && slot < 44) {
            try {
                ItemStack tileEntityItemStack = tile.getInv().getStackInSlot(slot - 37);
                if (tileEntityItemStack != null) {
                    int playerInventorySize = player.inventory.getSizeInventory() - 4;
                    for (int slotIndex = 0; slotIndex < playerInventorySize; slotIndex++) {
                        ItemStack playerInventoryStack = player.inventory.getStackInSlot(slotIndex);
                        if (playerInventoryStack == null) {
                            player.inventory.setInventorySlotContents(slotIndex, tileEntityItemStack);
                            tile.getInv().setInventorySlotContents(slot - 37, null);
                            break;
                        }
                        if (playerInventoryStack.getItem() == tileEntityItemStack.getItem() &&
                                playerInventoryStack.getItemDamage() == tileEntityItemStack.getItemDamage() &&
                                ItemStack.areItemStackTagsEqual(tileEntityItemStack, playerInventoryStack) &&
                                playerInventoryStack.stackSize < playerInventoryStack.getMaxStackSize()) {
                            playerInventoryStack.stackSize++;
                            tile.getInv().setInventorySlotContents(slot - 37, null);
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }
}
