package com.somemone.storageplus.util;

import org.bukkit.configuration.Configuration;

public class ConfigHandler {

    private Configuration config;

    public ConfigHandler (Configuration config ) {

        this.config = config;

    }

    public double getPersonalStoragePrice (int rows) {

        if (config.getBoolean("personal-storage.purchaseable")) {
            return config.getInt("personal-storage.price") * Math.pow(config.getDouble("personal-storage.upgrade-multiplier"), rows);
        } else {
            return 0;
        }

    }

    public double getChunkStoragePrice (int rows) {

        if (config.getBoolean("chunk-storage.purchaseable")) {
            return config.getInt("chunk-storage.price") * Math.pow(config.getDouble("chunk-storage.upgrade-multiplier"), rows);
        } else {
            return 0;
        }

    }

    public double getGroupStoragePrice (int rows) {

        if (config.getBoolean("group-storage.purchaseable")) {
            return (config.getInt("group-storage.price") * Math.pow(config.getDouble("group-storage.upgrade-multiplier"), rows));
        } else {
            return 0;
        }

    }

    public int getGroupStorageNum () {

        return Math.min(config.getInt("default-player-groups"), 7);

    }

}
