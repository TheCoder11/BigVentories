package com.somemone.bigventories.inventory;

import com.somemone.bigventories.StoragePlus;
import com.somemone.bigventories.storage.ChunkStorage;
import com.somemone.bigventories.storage.GroupStorage;
import com.somemone.bigventories.storage.PersonalStorage;
import com.somemone.bigventories.util.SearchHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class VoucherInventory {

    private Player user;
    private Chunk chunk;
    private int rowsLeft;

    public static ItemStack upgradeOne;
    public static ItemStack upgradeAll;

    /**
     *
     * @param rows Number of rows that voucher has
     * @param user Player using voucher
     */

    public VoucherInventory (int rows, Player user) {
        this.rowsLeft = rows;
        this.user = user;
        this.chunk = user.getLocation().getChunk();

        upgradeOne = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta oneMeta = upgradeOne.getItemMeta();
        oneMeta.setDisplayName("Upgrade One Row");
        upgradeOne.setItemMeta(oneMeta);

        upgradeAll = new ItemStack(Material.GREEN_STAINED_GLASS);
        ItemMeta allMeta = upgradeAll.getItemMeta();
        allMeta.setDisplayName("Upgrade All Rows");
        upgradeAll.setItemMeta(allMeta);
    }

    public Inventory buildInventory () {
        Inventory inv = Bukkit.createInventory(null, 45, "Voucher");

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(ChatColor.GOLD + "Rows: " + rowsLeft);
        paper.setItemMeta(paperMeta);
        inv.setItem(4, paper);

        SearchHandler searchHandler = new SearchHandler();
        PersonalStorage ps = searchHandler.searchPersonalStorage(user);
        if (ps != null) {

            ItemStack enderChest = new ItemStack(Material.ENDER_CHEST);
            ItemMeta psMeta = enderChest.getItemMeta();
            psMeta.setDisplayName(ChatColor.GOLD + "Personal Storage");

            List<String> psLore = new ArrayList<>();
            psLore.add(ChatColor.RED + "Rows: " + ChatColor.RESET + ps.rows);
            psMeta.setLore(psLore);

            enderChest.setItemMeta(psMeta);
            inv.setItem(36, enderChest);

            upgradeAll.setAmount(rowsLeft);
            inv.setItem(27, upgradeOne);
            inv.setItem(18, upgradeAll);

        }

        ChunkStorage cs = searchHandler.searchChunkStorage(chunk);
        if (cs != null) {

            ItemStack grassBlock = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta gbMeta = grassBlock.getItemMeta();

            gbMeta.setDisplayName(ChatColor.GOLD + "Chunk Storage");

            List<String> gbLore = new ArrayList<>();
            gbLore.add(ChatColor.RED + "Rows: " + ChatColor.RESET + cs.rows);
            gbMeta.setLore(gbLore);

            grassBlock.setItemMeta(gbMeta);
            inv.setItem(37, grassBlock);

            upgradeAll.setAmount(rowsLeft);
            inv.setItem(28, upgradeOne);
            inv.setItem(19, upgradeAll);

        }

        int storageSlot = 38;
        int upgradeOne = 29;
        int upgradeAll = 20;

        for (GroupStorage gs : searchHandler.searchGroupStorages(user.getUniqueId())) {

            ItemStack groupStorage = new ItemStack(Material.CHEST);
            ItemMeta gsMeta = groupStorage.getItemMeta();

            gsMeta.setDisplayName(ChatColor.GOLD + gs.name);

            List<String> gbLore = new ArrayList<>();
            gbLore.add(ChatColor.RED + "Rows: " + ChatColor.RESET + gs.rows);
            gsMeta.setLore(gbLore);

            gsMeta.getPersistentDataContainer().set( new NamespacedKey( StoragePlus.plugin, "group-name"), PersistentDataType.STRING, gs.name);

            groupStorage.setItemMeta(gsMeta);
            inv.setItem(storageSlot, groupStorage);

            this.upgradeAll.setAmount(rowsLeft);
            inv.setItem(upgradeOne, this.upgradeOne);
            inv.setItem(upgradeAll, this.upgradeAll);

            storageSlot++;
            upgradeOne++;
            upgradeAll++;

        }

        return inv;
    }

    public void removeRows (int rows) {
        this.rowsLeft = this.rowsLeft - rows;
    }

    public int getRows() {
        return this.rowsLeft;
    }

    public Player getPlayer() {
        return this.user;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

}
