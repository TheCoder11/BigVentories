package com.somemone.storageplus.storage;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class PersonalStorage extends Storage {

    public UUID owner;

    /**
     *
     *
     *
     * @param rows
     * @param owner
     */
    public PersonalStorage(int rows, UUID owner) {
        super(rows);

        this.owner = owner;
    }

    public PersonalStorage(int rows, UUID uuid, UUID owner, ArrayList<ItemStack> items) {
        super(rows, uuid, items);

        this.owner = owner;
    }
}
