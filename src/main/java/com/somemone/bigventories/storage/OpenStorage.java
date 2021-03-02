package com.somemone.bigventories.storage;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.UUID;

public class OpenStorage {

    public UUID uuid;
    public ArrayList<Inventory> inventory;
    public ArrayList<Player> viewers;

    public OpenStorage ( ArrayList<Inventory> inventories, UUID uuid ) {
        this.inventory = inventories;
        this.uuid = uuid;
        this.viewers = new ArrayList<>();
    }

}
