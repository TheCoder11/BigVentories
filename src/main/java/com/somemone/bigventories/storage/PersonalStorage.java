package com.somemone.bigventories.storage;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class PersonalStorage extends Storage {

    public OfflinePlayer owner;

    public PersonalStorage(int rows, OfflinePlayer owner) {
        super(rows);

        this.owner = owner;
    }

    public PersonalStorage(int rows, UUID uuid, OfflinePlayer owner, ArrayList<ItemStack> items) {
        super(rows, uuid, items);

        this.owner = owner;
    }
}
