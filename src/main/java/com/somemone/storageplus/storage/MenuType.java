package com.somemone.storageplus.storage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum MenuType {

    ALL_ITEMS (),
    ITEM_DISTRIBUTION (),
    SORTING ();

    MenuType() {
    }
}
