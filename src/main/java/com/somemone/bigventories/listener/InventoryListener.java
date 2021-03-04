package com.somemone.bigventories.listener;

import com.somemone.bigventories.Bigventories;
import com.somemone.bigventories.storage.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {

        if (event.getView().getTitle().equals("Storage")) { // If the inventory is a storage.

            ArrayList<Inventory> playerInventories = null;

            for (OpenStorage os : Bigventories.openStorages) {
                if (os.checkIfViewing((Player) event.getWhoClicked())) {

                    playerInventories = os.inventory;
                    break;

                }
            }

            if (playerInventories == null) {

                return;
            }

            if (event.getCurrentItem() != null) {

                    if (playerInventories != null && playerInventories.contains(event.getInventory())) {

                        if (playerInventories.indexOf(event.getInventory()) < (playerInventories.size() - 1)) {
                            Inventory selectedInventory = playerInventories.get(playerInventories.indexOf(event.getInventory()) + 1);
                            try {
                                event.getWhoClicked().openInventory(selectedInventory);
                            } catch (ArrayIndexOutOfBoundsException ignored) { }
                        }


                    }
                    event.setCancelled(true);


                } else if (event.getCurrentItem().equals(Storage.prevButton)) {

                    if (playerInventories != null && playerInventories.contains(event.getInventory())) {

                        if (playerInventories.indexOf(event.getInventory()) > 0) {
                            Inventory selectedInventory = playerInventories.get(playerInventories.indexOf(event.getInventory()) - 1);
                            try {
                                event.getWhoClicked().openInventory(selectedInventory);
                            } catch (ArrayIndexOutOfBoundsException ignored) { }
                        }

                        event.setCancelled(true);

                    }

                    event.setCancelled(true);

                } else if (event.getCurrentItem().equals(Storage.glassPane)) {

                    event.setCancelled(true);

                }
            }

        }



    @EventHandler
    public void onInventoryClose (InventoryCloseEvent event) {

        if (event.getView().getTitle().equals("Storage")) { // "Deconstructs" inventories to their

            for (OpenStorage os : Bigventories.openStorages) {

                if (os.checkIfViewing((Player) event.getPlayer())) {

                    if (os.getViewers().size() == 1) {  // Player is the only viewer of the inventory

                        Storage st = null;
                        for (PersonalStorage ps : Bigventories.personalStorages) {
                            if (ps.uuid == os.uuid) {
                                st = ps;
                            }
                        }
                        for (ChunkStorage cs : Bigventories.chunkStorages) {
                            if (cs.uuid == os.uuid) {
                                st = cs;
                            }
                        }
                        for (GroupStorage gs : Bigventories.groupStorages) {
                            if (gs.uuid == os.uuid) {
                                st = gs;
                            }
                        }

                        if (st != null) {
                            if ( !(event.getPlayer().getOpenInventory().getTitle().equals("Storage") )) {
                                event.getPlayer().sendMessage("Removed from UUID!");

                                ArrayList<ItemStack> totalItems = new ArrayList<>();

                                for (Inventory inv : os.inventory) {
                                    ArrayList<ItemStack> items = new ArrayList<>();
                                    if (inv.getContents().length > 0) {
                                        for (ItemStack item : inv.getContents()) {
                                            items.add(item);
                                        }
                                    }

                                    int removed = 0;
                                    while (removed < 9) {
                                        items.remove(items.size() - 1);
                                    }

                                    totalItems.addAll(items);
                                }

                                st.items = totalItems;
                            }
                        }

                    }

                }

            }

        }

    }

}
