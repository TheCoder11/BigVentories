package com.somemone.bigventories.item;

import com.google.common.collect.Lists;
import com.somemone.bigventories.Bigventories;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class VoucherItem {

    private ItemStack voucherItem = new ItemStack(Material.PAPER);

    public void create (int rows) {

        this.voucherItem.addEnchantment( Enchantment.ARROW_FIRE, 1 );
        ItemMeta meta = this.voucherItem.getItemMeta();
        meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        meta.setDisplayName("Upgrade Voucher");

        List<String> lore = new ArrayList<>();
        lore.add("Rows: " + String.valueOf(rows));
        meta.setLore( lore );

        Plugin plugin;
        NamespacedKey key = new NamespacedKey(Bigventories.plugin, "rows-alloted");

        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, rows);

        this.voucherItem.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return voucherItem;
    }

}
