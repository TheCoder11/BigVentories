package com.somemone.storageplus.storage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum StorageType {

    OPEN_STORAGE {
        @Override
        public ItemStack getCenterItem() {

            ItemStack item = new ItemStack(Material.CRAFTING_TABLE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "Manage Storage");
            item.setItemMeta(meta);

            return item;

        }

        @Override
        public void centerButtonAction(InventoryClickEvent event, OpenStorage os) {

        }

        @Override
        public void itemClickAction(InventoryClickEvent event) {

        }
    },
    DIST_STORAGE {
        @Override
        public ItemStack getCenterItem() {
            return null;
        }

        @Override
        public void centerButtonAction(InventoryClickEvent event, OpenStorage os) {

        }

        @Override
        public void itemClickAction(InventoryClickEvent event) {

        }
    },
    ADD_STORAGE {
        @Override
        public ItemStack getCenterItem() {
            return null;
        }

        @Override
        public void centerButtonAction(InventoryClickEvent event, OpenStorage os) {

        }

        @Override
        public void itemClickAction(InventoryClickEvent event) {

        }
    },
    REMOVE_STORAGE {
        @Override
        public ItemStack getCenterItem() {
            return null;
        }

        @Override
        public void centerButtonAction(InventoryClickEvent event, OpenStorage os) {

        }

        @Override
        public void itemClickAction(InventoryClickEvent event) {

        }
    };

    public abstract ItemStack getCenterItem();

    public abstract void centerButtonAction(InventoryClickEvent event, OpenStorage os);

    public abstract void itemClickAction(InventoryClickEvent event);

}
