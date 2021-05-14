package com.somemone.storageplus.listener;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.OpenStorage;
import com.somemone.storageplus.storage.Storage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ManageListener implements Listener {

    @EventHandler
    public void onManageClick (InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Sorted Storage")) return;
        if (event.getClickedInventory() == null) return;

        event.getWhoClicked().sendMessage("Clicked");

        if (event.isShiftClick()) event.setCancelled(true);

        OpenStorage os = null;
        ArrayList<Inventory> qtInv = null;
        Material requiredMaterial = null;
        for (OpenStorage tos : StoragePlus.openStorages) {
            if (tos.checkIfViewingQT((Player) event.getWhoClicked())) {
                os = tos;
                for (Map.Entry<Material, ArrayList<Inventory>> qtInventory : os.quickTakeInventories.entrySet()) {
                    if ( event.getInventory().getItem( event.getInventory().getSize() - 1 ).getType().equals(qtInventory.getKey()) ) {
                        qtInv = qtInventory.getValue();
                        requiredMaterial = qtInventory.getKey();
                    }
                }
            }
        }
        if (os == null) return;

        ArrayList<Integer> lastVals = createLastRowButtonValues(event.getInventory().getSize());

        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().equals(Storage.nextButton)) {
                if (qtInv != null && qtInv.contains(event.getInventory())) {
                    StoragePlus.closedByPlugin.add((Player) event.getWhoClicked());

                    if (qtInv.indexOf(event.getInventory()) < (qtInv.size() - 1)) {
                        Inventory selectedInventory = qtInv.get(qtInv.indexOf(event.getInventory()) + 1);
                        try {
                            event.getWhoClicked().openInventory(selectedInventory);
                        } catch (ArrayIndexOutOfBoundsException ignored) { }
                    }

                    event.setCancelled(true);
                }
            } else if (event.getCurrentItem().equals(Storage.prevButton)) {
                StoragePlus.closedByPlugin.add((Player) event.getWhoClicked());

                if (qtInv.indexOf(event.getInventory()) > 0) {
                    Inventory selectedInventory = qtInv.get(qtInv.indexOf(event.getInventory()) - 1);
                    try {
                        event.getWhoClicked().openInventory(selectedInventory);
                    } catch (ArrayIndexOutOfBoundsException ignored) { }
                }

                event.setCancelled(true);
            } else if (lastVals.contains(event.getSlot())) {
                os.itemDistribution = os.getItemDistribution();
                StoragePlus.closedByPlugin.add((Player) event.getWhoClicked());
                event.getWhoClicked().openInventory(os.itemDistribution.get(0));
                event.setCancelled(true);
            } else if (event.getCurrentItem().equals(Storage.backButton)) {
                StoragePlus.closedByPlugin.add((Player) event.getWhoClicked());
                event.getWhoClicked().openInventory(os.inventory.get(0));
                event.setCancelled(true);
            } else if (event.getCurrentItem().equals(OpenStorage.takenItem)) {
                event.setCancelled(true);
            } else {
                if (event.getClickedInventory().equals(event.getInventory())) {
                    os.removeItemToInventory(event.getCurrentItem(), os.inventory);
                }
            }
        } else {
            if (event.getClickedInventory().equals(event.getInventory())) {
                if (event.getCursor().getType().equals(requiredMaterial)) {
                    os.addItemToInventory(event.getCursor(), os.inventory);
                } else {
                    event.setCancelled(true);
                }
            } else if (event.isShiftClick()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDistroClick (InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Item Distribution")) return;

        OpenStorage os = null;
        ArrayList<Inventory> qtInv = null;
        for (OpenStorage tos : StoragePlus.openStorages) {
            if (tos.checkIfViewingID((Player) event.getWhoClicked())) {
                os = tos;
                qtInv = os.itemDistribution;
            }
        }
        if (os == null) return;



        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().equals(Storage.nextButton)) {
                if (qtInv != null && qtInv.contains(event.getInventory())) {

                    if (qtInv.indexOf(event.getInventory()) < (qtInv.size() - 1)) {
                        Inventory selectedInventory = qtInv.get(qtInv.indexOf(event.getInventory()) + 1);
                        try {
                            event.getWhoClicked().openInventory(selectedInventory);
                        } catch (ArrayIndexOutOfBoundsException ignored) { }
                    }

                    event.setCancelled(true);
                }
            } else if (event.getCurrentItem().equals(Storage.prevButton)) {
                if (qtInv.indexOf(event.getInventory()) > 0) {
                    Inventory selectedInventory = qtInv.get(qtInv.indexOf(event.getInventory()) - 1);
                    try {
                        event.getWhoClicked().openInventory(selectedInventory);
                    } catch (ArrayIndexOutOfBoundsException ignored) { }
                }

                event.setCancelled(true);
            } else if (event.getCurrentItem().equals(Storage.backButton)) {
                event.getWhoClicked().openInventory(os.inventory.get(0));
                event.setCancelled(true);
            } else if (event.getCurrentItem().equals(Storage.glassPane)) {
                event.setCancelled(true);
            } else {
                os.quickTakeInventories.put(event.getCurrentItem().getType(), os.getSortedInventory(event.getCurrentItem().getType()));
                event.getWhoClicked().openInventory(os.quickTakeInventories.get(event.getCurrentItem().getType()).get(0));
                event.setCancelled(true);
            }
        }
    }

    private ArrayList<Integer> createLastRowButtonValues (int size) {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(size - 1);
        a.add(size - 2);
        a.add(size - 3);
        a.add(size - 7);
        a.add(size - 8);
        a.add(size - 9);
        return a;
    }
}
