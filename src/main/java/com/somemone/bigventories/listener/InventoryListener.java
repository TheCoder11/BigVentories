package com.somemone.bigventories.listener;

import com.somemone.bigventories.Bigventories;
import com.somemone.bigventories.storage.Storage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class InventoryListener implements Listener {

    private Bigventories plugin;

    public InventoryListener ( Bigventories bigventories ) {
        this.plugin = bigventories;
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {

        if (event.getView().getTitle().equals("Storage")) { // If the inventory is a storage.

            if ( event.getCurrentItem() == Storage.nextButton) {

                // Action for next thing here

                ArrayList<Inventory> playerInventories = Bigventories.currentInventoryLists.get(event.getWhoClicked());

                if ( playerInventories != null && playerInventories.contains(event.getInventory()) ) {

                    if ( playerInventories.indexOf(event.getInventory()) < (playerInventories.size() - 1)) {
                        Inventory selectedInventory = playerInventories.get(playerInventories.indexOf( event.getInventory() ) + 1 );
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().openInventory(selectedInventory);
                    }

                    event.setCancelled(true);

                }

            } else if (event.getCurrentItem() == Storage.prevButton) {

                ArrayList<Inventory> playerInventories = Bigventories.currentInventoryLists.get(event.getWhoClicked());

                if ( playerInventories != null && playerInventories.contains(event.getInventory()) ) {

                    if ( playerInventories.indexOf(event.getInventory()) > 0) {
                        Inventory selectedInventory = playerInventories.get(playerInventories.indexOf( event.getInventory() ) - 1 );
                        event.getWhoClicked().openInventory(selectedInventory);
                    }

                    event.setCancelled(true);

                }

            } else if (event.getCurrentItem() == Storage.glassPane) {

                event.setCancelled(true);

            }

        }

    }

    @EventHandler
    public void onInventoryClose (InventoryCloseEvent event) {

        if (event.getView().getTitle().equals("Storage")) {

            Bigventories.currentInventoryLists.remove( event.getPlayer() );

        }

    }

}
