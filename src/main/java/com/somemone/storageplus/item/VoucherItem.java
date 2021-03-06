package com.somemone.storageplus.item;

import com.somemone.storageplus.StoragePlus;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class VoucherItem {

    private ItemStack voucherItem = new ItemStack(Material.PAPER);

    public VoucherItem (int rows) {

        ItemMeta meta = this.voucherItem.getItemMeta();
        meta.setDisplayName("Upgrade Voucher");

        List<String> lore = new ArrayList<>();
        lore.add("Rows: " + String.valueOf(rows));
        meta.setLore( lore );

        NamespacedKey key = new NamespacedKey(StoragePlus.plugin, "rows-alloted");

        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, rows);

        this.voucherItem.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return voucherItem;
    }

}
