package com.somemone.storageplus.inventory;

import com.somemone.storageplus.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageInventory {

    private Storage storage;
    private Storage allItemsStorage;

    public static ItemStack allItemsButton;
    public static ItemStack borderPane;

    public ManageInventory (Storage storage) {
        this.storage = storage;

        allItemsButton = new ItemStack(Material.COBBLESTONE);
        ItemMeta aiMeta = allItemsButton.getItemMeta();
        aiMeta.setDisplayName(ChatColor.GOLD + "Get Item Distribution");
        allItemsButton.setItemMeta(aiMeta);

        borderPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta bpPane = borderPane.getItemMeta();
        bpPane.setDisplayName("");
        borderPane.setItemMeta(bpPane);


    }

    public Inventory buildManageInventory () {

        Inventory inv = Bukkit.createInventory(null, 27, "Manage Storage");

        if (true) { // This is just to close this on the IntelliJ
            inv.setItem(0, borderPane);
            inv.setItem(1, borderPane);
            inv.setItem(2, borderPane);
            inv.setItem(3, borderPane);
            inv.setItem(4, borderPane);
            inv.setItem(5, borderPane);
            inv.setItem(6, borderPane);
            inv.setItem(7, borderPane);
            inv.setItem(9, borderPane);
            inv.setItem(17, borderPane);
            inv.setItem(18, borderPane);
            inv.setItem(19, borderPane);
            inv.setItem(20, borderPane);
            inv.setItem(21, borderPane);
            inv.setItem(22, borderPane);
            inv.setItem(23, borderPane);
            inv.setItem(24, borderPane);
            inv.setItem(25, borderPane);
            inv.setItem(26, borderPane);
        }

        inv.setItem(10, allItemsButton);

        return inv;

    }

    public ArrayList<Inventory> buildItemDistInventory () {

        HashMap<Material, Integer> distribution = new HashMap<>();

        for (ItemStack item : storage.items) {
            if (distribution.containsKey( item.getType() )) {
                distribution.put( item.getType(), distribution.get( item.getType() ) + item.getAmount() );
            } else {
                distribution.put( item.getType(), item.getAmount() );
            }
        }

        allItemsStorage = new Storage(6, false);

        for ( Material material : distribution.keySet() ) {

            ItemStack item = new ItemStack(material);
            ItemMeta meta = borderPane.getItemMeta();
            meta.setDisplayName(distribution.get(material) + "x " + material.name());
            item.setItemMeta(meta);

            allItemsStorage.items.add(borderPane);

        }

        return allItemsStorage.buildInventories();
    }

}
