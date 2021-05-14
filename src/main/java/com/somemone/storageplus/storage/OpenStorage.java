package com.somemone.storageplus.storage;

import com.somemone.storageplus.StoragePlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class OpenStorage {

    public static ItemStack takenItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    public boolean canAddItems;

    public UUID uuid;
    public ArrayList<Inventory> inventory;
    public boolean canView;
    public HashMap<Material, ArrayList<Inventory>> quickTakeInventories = new HashMap<>();
    public ArrayList<Inventory> itemDistribution = new ArrayList<>();
    public int rows;
    public int takenItems;

    public OpenStorage ( ArrayList<Inventory> inventories, UUID uuid, boolean canView, int rows ) {
        this.inventory = inventories;
        this.uuid = uuid;
        this.canView = canView;
        this.itemDistribution.add(Bukkit.createInventory(null, 9, "Item Distribution"));
        this.rows = rows;

        ItemMeta tiMeta = takenItem.getItemMeta();
        tiMeta.setDisplayName(" ");
        takenItem.setItemMeta(tiMeta);
    }

    public boolean checkIfViewingMain (Player player) {
        for (Inventory inventory : this.inventory) {
            if (inventory.getViewers().size() > 0) {
                if (inventory.getViewers().contains(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkIfViewingQT (Player player) {
        for (ArrayList<Inventory> qtInventories : this.quickTakeInventories.values()) {
            for (Inventory qtInventory : qtInventories) {
                if (qtInventory.getViewers().size() > 0) {
                    if (qtInventory.getViewers().contains(player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkIfViewingID (Player player) {
        for (Inventory inventory : this.itemDistribution) {
            if (inventory.getViewers().size() > 0) {
                if (inventory.getViewers().contains(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkIfViewingAll (Player player) {
        if (checkIfViewingMain(player) || checkIfViewingQT(player)) {
            return true;
        }
        return false;
    }

    public ArrayList<Player> getViewers() {
        ArrayList<Player> players = new ArrayList<>();

        for (Inventory inventory : this.inventory) {
            if (inventory.getViewers().size() > 0) {
                for (HumanEntity player : inventory.getViewers()) {
                    players.add((Player) player);
                }
            }
        }

        for (ArrayList<Inventory> qtInventories : this.quickTakeInventories.values()) {
            for (Inventory qtInventory : qtInventories) {
                if (qtInventory.getViewers().size() > 0) {
                    for (HumanEntity player : qtInventory.getViewers()) {
                        players.add((Player) player);
                    }
                }
            }
        }

        return players;
    }

    public ArrayList<ItemStack> getItemList (ArrayList<Inventory> inventoryList) {
        ArrayList<ItemStack> totalItems = new ArrayList<>();

        for (Inventory inv : inventoryList) {
            ArrayList<ItemStack> items = new ArrayList<>();
            if (inv.getContents().length > 0) {
                items.addAll(Arrays.asList(inv.getContents()));
            }

            int removed = 0;
            while (removed < Math.min(items.size(), 9)) {
                items.remove(items.size() - 1);
                removed++;
            }
            items.removeAll(Collections.singleton(null));

            totalItems.addAll(items);
        }

        return totalItems;
    }

    public ArrayList<Inventory> getSortedInventory (Material type) {
        ArrayList<ItemStack> items = getItemList(this.inventory);

        int itemIndex = 0;
        int numTaken = 0;

        ArrayList<Inventory> inventories = new ArrayList<>();
        double localRows = this.rows;
        int neededInventory = (int) Math.ceil(localRows / 5);

        for ( int i = 0; i < neededInventory; i++) {
            int invSize;
            if ( localRows > 5 ) {
                invSize = 54;
                localRows = localRows - 5;
            } else {
                invSize = ((int) localRows * 9) + 9;
                localRows = 0;
            }
            Inventory inv = Bukkit.createInventory(null, invSize, "Sorted Storage");

            int itemNumber = invSize - 9;

            /**
            for ( int a = 0; a < itemNumber; a++) {
                if (items.size() > itemIndex) {
                    if (items.get(itemIndex).getType().equals(type)) {
                        inv.addItem( items.get(itemIndex) );
                    } else {
                        numTaken++;
                    }
                    itemIndex++;
                }
            }
             **/

            ItemStack sortedIcon = new ItemStack(type);
            ItemMeta siMeta = sortedIcon.getItemMeta();
            siMeta.setDisplayName("Click to Select Item!");
            sortedIcon.setItemMeta(siMeta);

            inv.setItem( invSize - 9, sortedIcon);
            inv.setItem( invSize - 8, sortedIcon);
            inv.setItem( invSize - 7, sortedIcon);
            inv.setItem( invSize - 6, Storage.prevButton);
            inv.setItem( invSize - 5, Storage.backButton);
            inv.setItem( invSize - 4, Storage.nextButton);
            inv.setItem( invSize - 3, sortedIcon);
            inv.setItem( invSize - 2, sortedIcon);
            inv.setItem( invSize - 1, sortedIcon);

            inventories.add( inv ) ;
        }

        for (ItemStack item : items) {
            if (item.getType().equals(type)) {
                this.addItemToInventory(item, inventories);
            } else {
                numTaken++;
            }
        }

        Collections.reverse(inventories);
        for (Inventory inv : inventories) {
            int size = inv.getSize() - 1;
            for (int i = size; i >= 0; i = i - 1) {
                if ( inv.getItem(i) == null ) {
                    if (numTaken > 0) {
                        inv.setItem(i, OpenStorage.takenItem);
                        numTaken = numTaken - 1;
                    } else {
                        break;
                    }
                }
            }
        }
        Collections.reverse(inventories);
        return inventories;

    }

    public ArrayList<Inventory> getItemDistribution () {
        Player player = Bukkit.getPlayer("Somemone");
        ArrayList<ItemStack> items = getItemList(this.inventory);
        ArrayList<Material> materialsAdded = new ArrayList<>();
        ArrayList<ItemStack> itemsToAdd = new ArrayList<>();

        for (ItemStack item : items) {
            if (!materialsAdded.contains(item.getType())) {
                ItemStack nItem = new ItemStack(item.getType());
                ItemMeta iMeta = nItem.getItemMeta();
                iMeta.setDisplayName("Sort by " + item.getType().name());
                nItem.setItemMeta(iMeta);

                itemsToAdd.add(nItem);
                materialsAdded.add(nItem.getType());
            }
        }

        ArrayList<Inventory> inventories = new ArrayList<>();
        double localRows = Math.ceil( (double) itemsToAdd.size() / 9);
        int neededInventory = (int) Math.ceil( localRows / 5);

        for ( int i = 0; i < neededInventory; i++) {
            int invSize;
            if ( localRows > 5 ) {
                invSize = 54;
                localRows = localRows - 5;
            } else {
                invSize = ((int) localRows * 9) + 9;
                localRows = 0;
            }
            Inventory inv = Bukkit.createInventory(null, invSize, "Item Distribution");

            int itemNumber = invSize - 9;

            for ( int a = 0; a < itemNumber; a++) {
                if (itemsToAdd.size() > 0) {
                    inv.addItem(itemsToAdd.get(0));
                    itemsToAdd.remove(0);
                }
            }

            inv.setItem( invSize - 9, Storage.glassPane);
            inv.setItem( invSize - 8, Storage.glassPane);
            inv.setItem( invSize - 7, Storage.glassPane);
            inv.setItem( invSize - 6, Storage.prevButton);
            inv.setItem( invSize - 5, Storage.backButton);
            inv.setItem( invSize - 4, Storage.nextButton);
            inv.setItem( invSize - 3, Storage.glassPane);
            inv.setItem( invSize - 2, Storage.glassPane);
            inv.setItem( invSize - 1, Storage.glassPane);

            inventories.add( inv ) ;
        }

        return inventories;
    }

    public void addItemToInventory (ItemStack item, ArrayList<Inventory> invSet) {

        if (!canAddItems) return;

        for (Inventory inv : invSet) {
            int firstEmpty = inv.firstEmpty();
            if (inv.firstEmpty() != -1) {
                inv.setItem(firstEmpty, item);
                break;
            }
        }

        for (ArrayList<Inventory> invS : quickTakeInventories.values()) {
            Collections.reverse(invS);
            for (Inventory inv : invS) {
                int size = inv.getSize() - 1;
                for (int i = size; i >= 0; i = i - 1) {
                    if (inv.getItem(i) == null) {
                        inv.setItem(i, OpenStorage.takenItem);
                        break;
                    }
                }
            }
            Collections.reverse(invS);
        }

        canAddItems = false;

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(StoragePlus.getPlugin(StoragePlus.class), new Runnable() {
            @Override
            public void run() {
                canAddItems = true;
            }
        }, 2);
    }

    public void removeItemToInventory (ItemStack item, ArrayList<Inventory> invSet) {

        for (Inventory inv : invSet) {
            if (inv.contains(item)) {
                inv.removeItem(item);
                break;
            }
        }

        Bukkit.broadcastMessage("Removed2");

        for (ArrayList<Inventory> invS : quickTakeInventories.values()) {
            for (Inventory inv : invS) {
                if (inv.contains(OpenStorage.takenItem)) {
                    inv.remove(OpenStorage.takenItem);
                    break;
                }
            }
        }

    }
}
