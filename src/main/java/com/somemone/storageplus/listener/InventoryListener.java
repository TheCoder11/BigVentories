package com.somemone.storageplus.listener;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.*;
import com.somemone.storageplus.util.FileHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView().getTitle().equals("Storage")) { // If the inventory is a storage.

            ArrayList<Inventory> playerInventories = null;
            OpenStorage os = null;

            for (OpenStorage tos : StoragePlus.openStorages) {
                if (tos.checkIfViewing((Player) event.getWhoClicked())) {

                    os = tos;
                    playerInventories = os.inventory;
                    break;

                }
            }

            if (playerInventories == null || os == null) {

                return;
            }

            if (event.getCurrentItem() != null) {

                if (event.getCurrentItem().equals(Storage.nextButton)) {
                    if (playerInventories != null && playerInventories.contains(event.getInventory())) {

                        StoragePlus.closedByPlugin.add((Player) event.getWhoClicked());

                        if (playerInventories.indexOf(event.getInventory()) < (playerInventories.size() - 1)) {
                            Inventory selectedInventory = playerInventories.get(playerInventories.indexOf(event.getInventory()) + 1);
                            try {
                                event.getWhoClicked().openInventory(selectedInventory);
                            } catch (ArrayIndexOutOfBoundsException ignored) {
                            }
                        }

                        event.setCancelled(true);

                    }
                } else if (event.getCurrentItem().equals(Storage.prevButton)) {

                    if (playerInventories != null && playerInventories.contains(event.getInventory())) {

                        StoragePlus.closedByPlugin.add((Player) event.getWhoClicked());

                        if (playerInventories.indexOf(event.getInventory()) > 0) {
                            Inventory selectedInventory = playerInventories.get(playerInventories.indexOf(event.getInventory()) - 1);
                            try {
                                event.getWhoClicked().openInventory(selectedInventory);
                            } catch (ArrayIndexOutOfBoundsException ignored) {
                            }
                        }


                        event.setCancelled(true);

                    }

                    event.setCancelled(true);

                } else if (event.getCurrentItem().equals(Storage.glassPane)) {

                    event.setCancelled(true);

                } else if (event.getCurrentItem().equals(Storage.manageButton)) {

                    event.getWhoClicked().sendMessage(ChatColor.GOLD + "This feature hasn't been implemented yet!");
                    event.setCancelled(true);

                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose (InventoryCloseEvent event) {

        if (!(StoragePlus.closedByPlugin.contains(event.getPlayer()))) {

            if (event.getView().getTitle().equals("Storage")) { // "Deconstructs" inventories to their

                OpenStorage permStorage = null;
                PersonalStorage ps = null;
                ChunkStorage cs = null;
                GroupStorage gs = null;

                for (OpenStorage os : StoragePlus.openStorages) {
                    if (os.checkIfViewing((Player) event.getPlayer())) {
                        if (os.getViewers().size() == 1) {  // Player is the only viewer of the inventory

                            ps = FileHandler.loadPersonalStorageFromID(os.uuid);
                            if (!ps.isEmpty) {
                                permStorage = os;
                            }
                            cs = FileHandler.loadChunkStorage(os.uuid);
                            if (!cs.isEmpty) {
                                permStorage = os;
                            }
                            gs = FileHandler.loadGroupStorage(os.uuid);
                            if (!gs.isEmpty) {
                                permStorage = os;
                            }
                        }
                    }
                }

                if (permStorage != null) {

                    ArrayList<ItemStack> totalItems = new ArrayList<>();

                    StoragePlus.openStorages.remove(permStorage);


                    for (Inventory inv : permStorage.inventory) {
                        ArrayList<ItemStack> items = new ArrayList<>();
                        if (inv.getContents().length > 0) {
                            for (ItemStack item : inv.getContents()) {
                                items.add(item);
                            }
                        }


                        int removed = 0;
                        while ( removed < Math.min( items.size(), 9 )) {
                            items.remove(items.size() - 1);
                            removed++;
                        }

                        items.removeAll(Collections.singleton(null));

                        totalItems.addAll(items);
                    }

                    if (!ps.isEmpty) {
                        ps.items = totalItems;
                        FileHandler.saveStorage(ps);
                    }
                    if (!cs.isEmpty) {
                        cs.items = totalItems;
                        FileHandler.saveStorage(cs);
                    }
                    if (!gs.isEmpty) {
                        gs.items = totalItems;
                        FileHandler.saveStorage(gs);
                    }

                }

            }

        } else {
            StoragePlus.closedByPlugin.remove(event.getPlayer());
        }

    }

}



