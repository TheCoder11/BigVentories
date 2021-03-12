package com.somemone.bigventories.listener;

import com.somemone.bigventories.Bigventories;
import com.somemone.bigventories.storage.PersonalStorage;
import com.somemone.bigventories.util.SearchHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class VoucherListener implements Listener {

    @EventHandler
    public void onItemClick (PlayerInteractEvent event) {

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {

            ItemStack item = event.getItem();

            if (item.getItemMeta().getPersistentDataContainer().has( new NamespacedKey(Bigventories.plugin, "rows-alloted"), PersistentDataType.INTEGER)) {

                Inventory inventory = Bukkit.createInventory(null, 56);

                ItemStack paperItem = new ItemStack(Material.PAPER);
                ItemMeta meta = paperItem.getItemMeta();
                meta.setDisplayName("Upgrade Voucher");
                paperItem.setItemMeta(meta);

                // Builds our variables for the inventory

                int rows = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Bigventories.plugin, "rows-alloted"), PersistentDataType.INTEGER);

                SearchHandler handler = new SearchHandler();

                if ( handler.searchPersonalStorage(event.getPlayer()) != null ) {

                    // Creates ItemStacks

                    ItemStack psItem = new ItemStack(Material.ENDER_CHEST);
                    meta = psItem.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Personal Storage");

                }

            }

        }

    }

}
