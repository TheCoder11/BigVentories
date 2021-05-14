package com.somemone.storageplus.storage;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.util.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.UUID;

public class Storage {

    public static ItemStack nextButton;
    public static ItemStack prevButton;
    public static ItemStack glassPane;
    public static ItemStack manageButton;
    public static ItemStack backButton;

    public ArrayList<ItemStack> items;
    public int rows;
    public UUID uuid;
    public boolean isEmpty = false;

    public Storage (int rows) {
        this.rows = rows;
        items = new ArrayList<>();
        this.uuid = UUID.randomUUID();

        generateButtons();
    }

    public Storage (int rows, boolean isEmpty) {
        this.rows = rows;
        items = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.isEmpty = isEmpty;

        generateButtons();
    }

    public Storage (int rows, UUID uuid, ArrayList<ItemStack> items) {
        this.rows = rows;
        this.items = items;
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.uuid = uuid;

        generateButtons();
    }

    private void generateButtons() {
        prevButton = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1MmUyYjkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OTc3MzRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0=");
        ItemMeta nBMeta = prevButton.getItemMeta();
        nBMeta.setDisplayName(ChatColor.RED + "PREVIOUS PAGE");
        prevButton.setItemMeta(nBMeta);

        nextButton = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjhmNjgxZGFhZDhiZjQzNmNhZThkYTNmZTgxMzFmNjJhMTYyYWI4MWFmNjM5YzNlMDY0NGFhNmFiYWMyZiJ9fX0=");
        ItemMeta pBMeta = nextButton.getItemMeta();
        pBMeta.setDisplayName(ChatColor.GREEN + "NEXT PAGE");
        nextButton.setItemMeta(pBMeta);

        glassPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta gPMeta = glassPane.getItemMeta();
        gPMeta.setDisplayName(" ");
        glassPane.setItemMeta(gPMeta);

        manageButton = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta mBMeta = manageButton.getItemMeta();
        mBMeta.setDisplayName(ChatColor.GOLD + "Manage Button");
        manageButton.setItemMeta(mBMeta);

        backButton = new ItemStack(Material.ARROW);
        ItemMeta bbMeta = manageButton.getItemMeta();
        bbMeta.setDisplayName(ChatColor.GOLD + "Back");
        backButton.setItemMeta(bbMeta);
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

            inv.setItem( invSize - 9, glassPane);
            inv.setItem( invSize - 8, glassPane);
            inv.setItem( invSize - 7, glassPane);
            inv.setItem( invSize - 6, prevButton);
            inv.setItem( invSize - 5, glassPane);
            inv.setItem( invSize - 4, nextButton);
            inv.setItem( invSize - 3, glassPane);
            inv.setItem( invSize - 2, glassPane);
            inv.setItem( invSize - 1, glassPane);

            inventories.add( inv ) ;

        }

        return inventories;

    }

    public ArrayList<Inventory> drawAmountInventory (Material type) {
        ArrayList<Inventory> inventories = new ArrayList<>();
        ArrayList<ItemStack> matchingItems = new ArrayList<>();

        for (ItemStack item : this.items) {
            if (item.getType().equals(type)) {
                matchingItems.add(item);
            }
        }
        double localRows = Math.ceil(matchingItems.size() / 7);
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

                if (matchingItems.size() > 0) {

                    inv.addItem(matchingItems.get(0));
                    matchingItems.remove(0);

                }

            }

            ItemStack currentItem = new ItemStack(type);
            ItemMeta ciItem = currentItem.getItemMeta();
            ciItem.setDisplayName("Drag an item to this to select!");
            currentItem.setItemMeta(ciItem);

            inv.setItem( invSize - 9, currentItem);
            inv.setItem( invSize - 8, currentItem);
            inv.setItem( invSize - 7, currentItem);
            inv.setItem( invSize - 6, prevButton);
            inv.setItem( invSize - 5, backButton);
            inv.setItem( invSize - 4, nextButton);
            inv.setItem( invSize - 3, currentItem);
            inv.setItem( invSize - 2, currentItem);
            inv.setItem( invSize - 1, currentItem);

            inventories.add( inv ) ;

        }

        return inventories;
    }

    public void saveSpecificType (ArrayList<ItemStack> replacementItems, Material typeReplacing) {

        // Deletes items of selected type
        ArrayList<ItemStack> itemsToDelete = new ArrayList<>();
        for (ItemStack item : this.items) {
            itemsToDelete.add(item);
        }
        this.items.removeAll(itemsToDelete);

        //Adds items from replacement items
        this.items.addAll(replacementItems);

    }

    public void addRows (int rows) {
        if (StoragePlus.openStorages.size() == 0) {
            OpenStorage toDelete = null;
            for (OpenStorage os : StoragePlus.openStorages) {
                if (os.uuid == this.uuid) {
                    for (Player player : os.getViewers()) {
                        player.closeInventory();
                        player.sendMessage(ChatColor.GOLD + "This storage has been upgraded!");
                    }
                }
            }

            if (toDelete != null) {
                StoragePlus.openStorages.remove(toDelete);
            }
        }

        this.rows = this.rows + rows;
    }

}
