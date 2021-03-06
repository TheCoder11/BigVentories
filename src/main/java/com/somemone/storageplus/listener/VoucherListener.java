package com.somemone.storageplus.listener;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.inventory.VoucherInventory;
import com.somemone.storageplus.item.VoucherItem;
import com.somemone.storageplus.storage.ChunkStorage;
import com.somemone.storageplus.storage.GroupStorage;
import com.somemone.storageplus.storage.PersonalStorage;
import com.somemone.storageplus.util.FileHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class VoucherListener implements Listener {

    private HashMap<UUID, VoucherInventory> rowsRemaining = new HashMap<>();

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick (InventoryClickEvent event ) {
        if (event.getView().getTitle().equals("Voucher")) {
            if (event.getCurrentItem() != null) {

                VoucherInventory vInv = rowsRemaining.get(event.getWhoClicked().getUniqueId());
                int originalRows = vInv.getRows();
                rowsRemaining.remove(event.getWhoClicked().getUniqueId());

                ItemStack modulatedAllRowsStack = VoucherInventory.upgradeAll;
                modulatedAllRowsStack.setAmount(originalRows);

                if (event.getCurrentItem().equals(VoucherInventory.upgradeOne)) {
                    vInv.removeRows(1);

                    switch (event.getSlot()) {
                        case 27:
                            PersonalStorage ps = FileHandler.loadPersonalStorage(vInv.getPlayer().getUniqueId());

                            if (!ps.isEmpty) {
                                ps.addRows(1);
                                FileHandler.saveStorage(ps);
                            }
                            break;
                        case 28:
                            ChunkStorage cs = FileHandler.loadChunkStorage(vInv.getChunk().getX(), vInv.getChunk().getZ());

                            if (!cs.isEmpty) {
                                cs.addRows(1);
                                FileHandler.saveStorage(cs);
                            }
                            break;
                        default:
                            GroupStorage gs = FileHandler.loadGroupStorage(event.getClickedInventory().getItem(event.getSlot() + 9).getItemMeta().getPersistentDataContainer()
                                    .get(new NamespacedKey(StoragePlus.plugin, "group-name"), PersistentDataType.STRING));

                            if (!gs.isEmpty) {
                                gs.addRows(1);
                                FileHandler.saveStorage(gs);
                            }
                            break;
                    }

                    if (vInv.getRows() == 0) {
                        ItemStack origItem = new VoucherItem(originalRows).getItem();
                        for ( ItemStack item : event.getWhoClicked().getInventory().getContents() ) {
                            if (item != null) {
                                if (item.isSimilar(origItem)) {
                                    if (item.getAmount() == 1) {
                                        event.getWhoClicked().getInventory().remove(item);
                                    } else {
                                        item.setAmount(item.getAmount() - 1);
                                    }
                                }
                            }
                        }
                        event.getWhoClicked().closeInventory();

                        event.setCancelled(true);
                        return;
                    }


                } else if (event.getCurrentItem().equals(modulatedAllRowsStack)) {
                    vInv.removeRows(originalRows);

                    switch (event.getSlot()) {
                        case 18:
                            PersonalStorage ps = FileHandler.loadPersonalStorage(vInv.getPlayer().getUniqueId());

                            ps.addRows(originalRows);
                            break;
                        case 19:
                            ChunkStorage cs = FileHandler.loadChunkStorage(vInv.getChunk().getX(), vInv.getChunk().getZ());

                            cs.addRows(originalRows);
                            break;
                        default:
                            GroupStorage gs = FileHandler.loadGroupStorage(event.getClickedInventory().getItem(event.getSlot() + 18).getItemMeta().getPersistentDataContainer()
                                    .get(new NamespacedKey(StoragePlus.plugin, "group-name"), PersistentDataType.STRING));

                            gs.addRows(originalRows);
                            break;

                    }


                    ItemStack origItem = new VoucherItem(originalRows).getItem();
                    for ( ItemStack item : event.getWhoClicked().getInventory().getContents() ) {
                        if (item != null) {
                            if (item.isSimilar(origItem)) {
                                item.setAmount(item.getAmount() - 1);
                            }
                        }
                    }
                    event.getWhoClicked().closeInventory();

                    event.setCancelled(true);
                    return;

                } else if ( event.getCurrentItem().equals(VoucherInventory.createChunk)) {

                    vInv.removeRows(1);

                    ChunkStorage newCS = new ChunkStorage(1, event.getWhoClicked().getLocation().getChunk().getX(), event.getWhoClicked().getLocation().getChunk().getZ());
                    FileHandler.saveStorage(newCS);

                } else if (event.getCurrentItem().equals(VoucherInventory.createPersonal)) {

                    vInv.removeRows(1);

                    PersonalStorage newCS = new PersonalStorage(1, event.getWhoClicked().getUniqueId());
                    FileHandler.saveStorage(newCS);

                }

                ItemStack newItem = new VoucherItem(vInv.getRows()).getItem();
                ItemStack origItem = new VoucherItem(originalRows).getItem();

                for ( ItemStack item : event.getWhoClicked().getInventory().getContents() ) {
                    if (item != null) {
                        if (item.isSimilar(origItem)) {
                            if (item.getAmount() == 1) {
                                event.getWhoClicked().getInventory().remove(item);
                            } else {
                                item.setAmount(item.getAmount() - 1);
                            }
                        }
                    }
                }

                event.getWhoClicked().getInventory().addItem(newItem);

                rowsRemaining.put(event.getWhoClicked().getUniqueId(), vInv);

                event.getWhoClicked().openInventory(vInv.buildInventory());

                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onItemClick (PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (event.getItem() != null) {
                if (event.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(StoragePlus.plugin, "rows-alloted"), PersistentDataType.INTEGER)) {

                    VoucherInventory vInv = new VoucherInventory(event.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(StoragePlus.plugin, "rows-alloted"), PersistentDataType.INTEGER), event.getPlayer());

                    rowsRemaining.put(event.getPlayer().getUniqueId(), vInv);

                    event.getPlayer().openInventory(vInv.buildInventory());

                }
            }

        }
    }


}
