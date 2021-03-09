package com.somemone.bigventories.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VoucherItem implements Listener {

    Material type;
    private ItemStack voucherItem = new ItemStack(Material.PAPER);

    public VoucherItem (int rows) {



    }

    public Inventory createInventory () {

        Inventory inv = Bukkit.createInventory(null, 54, "Upgrade your inventories!");

    }


    @EventHandler
    public void onItemClick (PlayerInteractEvent event) {



    }

}
