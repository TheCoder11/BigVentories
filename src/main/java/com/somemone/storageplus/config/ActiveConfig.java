package com.somemone.storageplus.config;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.ChunkStorage;
import com.somemone.storageplus.storage.PersonalStorage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ActiveConfig {

    private FileConfiguration config;

    private int startingRows;

    private boolean vaultEnabled;

    private Integer personalStoragePrice;
    private double personalStorageMultiplier;

    private int chunkStoragePrice;
    private double chunkStorageMultiplier;

    private int groupStoragePrice;
    private double groupStorageMultiplier;

    private boolean townyEnabled;

    public ActiveConfig(FileConfiguration config) {
        this.config = config;
        loadConfig();
    }

    public void loadConfig() {
        vaultEnabled = config.getBoolean("vault-pricing");
        startingRows = config.getInt("player-starting-rows");

        personalStoragePrice = config.getInt("personal-storage.price");
        personalStorageMultiplier = config.getDouble("personal-storage.upgrade-multiplier");

        chunkStoragePrice = config.getInt("chunk-storage.price");
        chunkStorageMultiplier = config.getDouble("chunk-storage.upgrade-multiplier");

        groupStoragePrice = config.getInt("group-storage.price");
        groupStorageMultiplier = config.getDouble("group-storage.upgrade-multiplier");

        townyEnabled = config.getBoolean("towny");
    }

    public boolean validateStorage(Player player, int rows) {
        if (vaultEnabled) {
            Double price = personalStoragePrice.doubleValue() * (Math.pow(personalStorageMultiplier, rows));

            if (StoragePlus.getEcon().has(player, price)) {
                StoragePlus.getEcon().withdrawPlayer(player, price);
                player.sendMessage(ChatColor.GOLD + String.valueOf( price.intValue() )+ "$ has been deducted from your account");
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setVaultEnabled (boolean value) {
        this.vaultEnabled = value;
    }

    public boolean validateChunk(ChunkStorage cs, Location location) {
        return true;
    }

}
