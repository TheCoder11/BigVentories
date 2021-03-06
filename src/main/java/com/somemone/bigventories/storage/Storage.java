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
import java.util.UUID;

public class Storage {

    public static ItemStack nextButton;
    public static ItemStack prevButton;
    public static ItemStack glassPane;

    public ArrayList<ItemStack> items;
    public int rows;
    public UUID uuid;

    public Storage (int rows) {
        this.rows = rows;
        items = new ArrayList<>();
        this.uuid = UUID.randomUUID();

        nextButton = new ItemStack(Material.ARROW);
        ItemMeta nBMeta = nextButton.getItemMeta();
        nBMeta.setDisplayName(ChatColor.GREEN + "NEXT PAGE");
        nextButton.setItemMeta(nBMeta);

        prevButton = new ItemStack(Material.ARROW);
        ItemMeta pBMeta = prevButton.getItemMeta();
        pBMeta.setDisplayName(ChatColor.RED + "PREVIOUS PAGE");
        prevButton.setItemMeta(pBMeta);

        glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gPMeta = glassPane.getItemMeta();
        gPMeta.setDisplayName(ChatColor.GOLD + "|");
        glassPane.setItemMeta(gPMeta);
    }

    public Storage (int rows, UUID uuid, ArrayList<ItemStack> items) {
        this.rows = rows;
        this.items = items;
        this.uuid = uuid;

        nextButton = new ItemStack(Material.ARROW);
        ItemMeta nBMeta = nextButton.getItemMeta();
        nBMeta.setDisplayName(ChatColor.GREEN + "NEXT PAGE");
        nextButton.setItemMeta(nBMeta);

        prevButton = new ItemStack(Material.ARROW);
        ItemMeta pBMeta = prevButton.getItemMeta();
        pBMeta.setDisplayName(ChatColor.RED + "PREVIOUS PAGE");
        prevButton.setItemMeta(pBMeta);

        glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gPMeta = glassPane.getItemMeta();
        gPMeta.setDisplayName(ChatColor.GOLD + "|");
        glassPane.setItemMeta(gPMeta);
    }

    private boolean checkRows() {
        int maxSize = this.rows * 9;
        return maxSize <= this.items.size();
    }

    public ArrayList<Inventory> buildInventories ( ) {

        ArrayList<Inventory> inventories = new ArrayList<>();
        double localRows = this.rows;
        ArrayList<ItemStack> localItems = this.items;
        int neededInventory = (int) Math.ceil(localRows / 5);

        for ( int i = 0; i < neededInventory; i++) {

            int invSize;
            if ( localRows > 5 ) {
                invSize = 54;
                localRows = localRows - 5;
            } else {
                invSize = ((int) localRows * 9) + 9;
                localRows = 0;
            }

            Inventory inv = Bukkit.createInventory(null, invSize, "Storage");

            int itemNumber = invSize - 9;

            for ( int a = 0; a < itemNumber; a++) {

                if (localItems.size() > 0) {
                    inv.addItem(localItems.get(0));
                    localItems.remove(0);
                }

            }

            inv.setItem( invSize - 9, prevButton);
            inv.setItem( invSize - 8, glassPane);
            inv.setItem( invSize - 7, glassPane);
            inv.setItem( invSize - 6, glassPane);
            inv.setItem( invSize - 5, glassPane);
            inv.setItem( invSize - 4, glassPane);
            inv.setItem( invSize - 3, glassPane);
            inv.setItem( invSize - 2, glassPane);
            inv.setItem( invSize - 1, nextButton);

            inventories.add( inv ) ;

        }

        return inventories;

    }

}
