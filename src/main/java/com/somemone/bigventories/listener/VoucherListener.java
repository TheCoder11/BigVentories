package com.somemone.bigventories.listener;

import com.somemone.bigventories.Bigventories;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class VoucherListener implements Listener {

    @EventHandler
    public void onItemClick (PlayerInteractEvent event) {

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {

            ItemStack item = event.getItem();

            if (item.getItemMeta().getPersistentDataContainer().has( new NamespacedKey(Bigventories.plugin, "rows-alloted"), PersistentDataType.INTEGER)) {



            }

        }

    }

}
