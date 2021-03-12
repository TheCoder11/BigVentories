package com.somemone.bigventories.storage;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class GroupStorage extends Storage {

    public UUID owner;
    public ArrayList<UUID> accessList;
    public String name;

    public GroupStorage(String name, int rows, UUID owner) {
        super(rows);

        this.owner = owner;
        this.accessList = new ArrayList<>();
        this.accessList.add(owner);
        this.name = name;
    }

    public GroupStorage(String name, int rows, ArrayList<ItemStack> items, UUID owner, UUID uuid, ArrayList<UUID> accessList) {
        super(rows, uuid, items);

        this.owner = owner;
        this.accessList = accessList;
        this.accessList.add(owner);
        this.name = name;
    }

    public void addPlayer ( Player added ) {
        this.accessList.add(added.getUniqueId());
    }

    public void addPlayer ( UUID uuid ) {
        this.accessList.add(uuid);
    }

    public void removePlayer ( Player removed ) {
        this.accessList.add(removed.getUniqueId());
    }

    public void removePlayers ( UUID uuid ) {
        this.accessList.add(uuid);
    }

    public boolean checkAccess (UUID checking) {
        return this.accessList.contains(checking);
    }
}
