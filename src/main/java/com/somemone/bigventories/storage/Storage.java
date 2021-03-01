package com.somemone.bigventories.storage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Storage {

    public static ItemStack nextButton;
    public static ItemStack prevButton;
    public static ItemStack glassPane;

    public ArrayList<ItemStack> items;
    public int rows;

    public Storage (int rows) {
        this.rows = rows;

        nextButton.setType(Material.ARROW);
        ItemMeta nBMeta = nextButton.getItemMeta();
        nBMeta.setDisplayName(ChatColor.GREEN + "NEXT PAGE");
        nextButton.setItemMeta(nBMeta);

        prevButton.setType(Material.ARROW);
        ItemMeta pBMeta = prevButton.getItemMeta();
        pBMeta.setDisplayName(ChatColor.RED + "PREVIOUS PAGE");
        prevButton.setItemMeta(pBMeta);

        glassPane.setType(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta gPMeta = glassPane.getItemMeta();
        gPMeta.setDisplayName(ChatColor.GOLD + "|");
        glassPane.setItemMeta(pBMeta);
    }

    private boolean checkRows() {
        int maxSize = this.rows * 9;
        return maxSize <= this.items.size();
    }

    public ArrayList<Inventory> buildInventories () {

        ArrayList<Inventory> inventories = new ArrayList<>();
        int localRows = this.rows;
        ArrayList<ItemStack> localItems = new ArrayList<>();
        int neededInventory = (int) Math.ceil(localRows / 5);

        for ( int i = 0; i < neededInventory; i++) {

            int invSize;
            if ( localRows > 5 ) {
                invSize = 56;
                localRows =- 5;
            } else {
                invSize = (localRows * 9) + 9;
                localRows = 0;
            }

            Inventory inv = Bukkit.createInventory(null, invSize, "Storage");

            int itemNumber = invSize - 9;

            for ( int a = 0; a < itemNumber; a++) {

                inv.addItem( localItems.get(0) );
                localItems.remove(0);

            }

            inv.setItem( invSize - 8, prevButton);
            inv.setItem( invSize - 7, glassPane);
            inv.setItem( invSize - 6, glassPane);
            inv.setItem( invSize - 5, glassPane);
            inv.setItem( invSize - 4, glassPane);
            inv.setItem( invSize - 3, glassPane);
            inv.setItem( invSize - 2, glassPane);
            inv.setItem( invSize - 1, glassPane);
            inv.setItem( invSize          , nextButton);

            inventories.add( inv ) ;

        }

        return inventories;

    }

}
