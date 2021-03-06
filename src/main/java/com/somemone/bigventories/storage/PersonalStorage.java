package com.somemone.bigventories.storage;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class PersonalStorage extends Storage {

    public Player owner;

    public PersonalStorage(int rows, Player owner) {
        super(rows);

        this.owner = owner;
    }

    public PersonalStorage(int rows, UUID uuid, Player owner, ArrayList<ItemStack> items) {
        super(rows, uuid, items);

        this.owner = owner;
    }
}
