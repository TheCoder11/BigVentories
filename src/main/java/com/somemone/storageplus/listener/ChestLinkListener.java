package com.somemone.storageplus.listener;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.*;
import com.somemone.storageplus.util.FileHandler;
import org.bukkit.*;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class ChestLinkListener implements Listener {

    public NamespacedKey definedStorageKey = new NamespacedKey(StoragePlus.getPlugin(StoragePlus.class), "defined-storage");

    @EventHandler
    public void onItemClick (PlayerInteractEvent event) {
        if (event.getClickedBlock() instanceof InventoryHolder) {
            if (event.getClickedBlock().getType().equals(Material.HOPPER)) return;

            if (!(event.getClickedBlock().getState() instanceof TileState)) return;
            TileState tileState = (TileState) event.getClickedBlock().getState();

            if (event.getPlayer().isSneaking()) {
                if (tileState.getPersistentDataContainer().has(definedStorageKey, PersistentDataType.STRING)) {
                    event.getPlayer().sendMessage(ChatColor.GOLD + "Storage has been unlinked!");
                    tileState.getPersistentDataContainer().remove(definedStorageKey);
                } else {
                    event.getPlayer().openInventory(getAllowedInventories(event.getPlayer()));
                }
            } else {
                if (tileState.getPersistentDataContainer().has(definedStorageKey, PersistentDataType.STRING)) {
                    UUID selectedID = UUID.fromString(tileState.getPersistentDataContainer().get(definedStorageKey, PersistentDataType.STRING));
                    PersonalStorage ps = FileHandler.loadPersonalStorageFromID(selectedID);
                    ChunkStorage cs = FileHandler.loadChunkStorage(selectedID);
                    GroupStorage gs = FileHandler.loadGroupStorage(selectedID);

                    if (!ps.isEmpty) {
                        if (ps.owner.equals(event.getPlayer().getUniqueId())) {

                            OpenStorage os = new OpenStorage(ps.buildInventories(), ps.uuid, true, ps.rows);
                            StoragePlus.openStorages.add(os);
                            event.getPlayer().openInventory(os.inventory.get(0));

                        }
                    } else if (!cs.isEmpty) {
                        OpenStorage os = new OpenStorage(cs.buildInventories(), cs.uuid, true, cs.rows);
                        StoragePlus.openStorages.add(os);
                        event.getPlayer().openInventory(os.inventory.get(0));
                    } else if (!gs.isEmpty) {
                        if (gs.accessList.contains(event.getPlayer().getUniqueId())) {

                            OpenStorage os = new OpenStorage(gs.buildInventories(), gs.uuid, true, gs.rows);
                            StoragePlus.openStorages.add(os);
                            event.getPlayer().openInventory(os.inventory.get(0));

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Select Storage")) {

            if (event.getCurrentItem() == null) return;

            if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(definedStorageKey, PersistentDataType.STRING)) {

                TileState state;

                if (event.getCurrentItem().getType() == Material.ENDER_CHEST) {
                    PersonalStorage ps = FileHandler.loadPersonalStorageFromID(UUID.fromString(
                            event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(definedStorageKey, PersistentDataType.STRING)
                    ));

                    if (ps.isEmpty) return;
                    if (!(event.getWhoClicked().getTargetBlock(10).getState() instanceof TileState)) return;

                    state = (TileState) event.getWhoClicked().getTargetBlock(10).getState();
                    state.getPersistentDataContainer().set(definedStorageKey, PersistentDataType.STRING, ps.uuid.toString());
                }
                else if (event.getCurrentItem().getType() == Material.GRASS_BLOCK) {
                    ChunkStorage cs = FileHandler.loadChunkStorage(UUID.fromString(
                            event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(definedStorageKey, PersistentDataType.STRING)
                    ));

                    if (cs.isEmpty) return;
                    if (!(event.getWhoClicked().getTargetBlock(10).getState() instanceof TileState)) return;

                    state = (TileState) event.getWhoClicked().getTargetBlock(10).getState();
                    state.getPersistentDataContainer().set(definedStorageKey, PersistentDataType.STRING, cs.uuid.toString());
                }
                else if (event.getCurrentItem().getType() == Material.CHEST) {
                    GroupStorage gs = FileHandler.loadGroupStorage(UUID.fromString(
                            event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(definedStorageKey, PersistentDataType.STRING)
                    ));

                    if (gs.isEmpty) return;
                    if (!(event.getWhoClicked().getTargetBlock(10).getState() instanceof TileState)) return;

                    state = (TileState) event.getWhoClicked().getTargetBlock(10).getState();
                    state.getPersistentDataContainer().set(definedStorageKey, PersistentDataType.STRING, gs.uuid.toString());
                }
                else {
                    return;
                }

                state.update();
                event.getWhoClicked().closeInventory();

            }

        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryMoved (InventoryMoveItemEvent event) {

        if (event.getInitiator().getLocation() == null || event.getDestination().getLocation() == null) return;

        TileState destBlock = (TileState) event.getDestination().getLocation().getBlock().getState();

        if (destBlock.getPersistentDataContainer().has(definedStorageKey, PersistentDataType.STRING)) {
            UUID storageID = UUID.fromString(destBlock.getPersistentDataContainer().get(definedStorageKey, PersistentDataType.STRING));
            OpenStorage os = null;
            Storage st = null;

            for (OpenStorage tos : StoragePlus.openStorages) {
                if (tos.uuid.equals(storageID)) {
                    os = tos;
                }
            }

            if (os == null) {
                st = FileHandler.loadPersonalStorageFromID(storageID);
                if (st == null) {
                    st = FileHandler.loadChunkStorage(storageID);
                    if (st == null) {
                        st = FileHandler.loadGroupStorage(storageID);
                        if (st == null) {
                            return;
                        }
                    }
                }
            }

            if (os != null) {
                os.addItemToInventory(event.getItem(), os.inventory);
                event.getDestination().remove(event.getItem());
            }

        }

    }

    public Inventory getAllowedInventories (Player player) {

        Inventory newInventory = Bukkit.createInventory(null, 27, "Select Storage");

        PersonalStorage ps = FileHandler.loadPersonalStorage(player.getUniqueId());
        ChunkStorage cs = FileHandler.loadChunkStorage(player.getChunk().getX(), player.getChunk().getZ());
        ArrayList<GroupStorage> storages = FileHandler.loadAllowedGroupStorages(player.getUniqueId());

        if (!ps.isEmpty) {
            ItemStack pitem = new ItemStack(Material.ENDER_CHEST);
            ItemMeta psMeta = pitem.getItemMeta();
            psMeta.setDisplayName("Personal Storage");
            psMeta.getPersistentDataContainer().set(definedStorageKey, PersistentDataType.STRING, ps.uuid.toString());
            pitem.setItemMeta(psMeta);

            newInventory.setItem(0, pitem);
        }
        if (!cs.isEmpty) {
            ItemStack citem = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta csMeta = citem.getItemMeta();
            csMeta.setDisplayName("Chunk Storage");
            csMeta.getPersistentDataContainer().set(definedStorageKey, PersistentDataType.STRING, cs.uuid.toString());
            citem.setItemMeta(csMeta);

            newInventory.setItem(1, citem);
        }
        for (GroupStorage gs : storages) {
            ItemStack gitem = new ItemStack(Material.CHEST);
            ItemMeta gsMeta = gitem.getItemMeta();
            gsMeta.setDisplayName("Group Storage");
            gsMeta.getPersistentDataContainer().set(definedStorageKey, PersistentDataType.STRING, gs.uuid.toString());
            gitem.setItemMeta(gsMeta);

            newInventory.setItem(storages.indexOf(gs) + 2, gitem);
        }

        return newInventory;

    }



}
