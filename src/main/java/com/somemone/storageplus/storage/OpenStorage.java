package com.somemone.storageplus.storage;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.UUID;

public class OpenStorage {

    public UUID uuid;
    public ArrayList<Inventory> inventory;
    public boolean canView;

    public OpenStorage ( ArrayList<Inventory> inventories, UUID uuid, boolean canView ) {
        this.inventory = inventories;
        this.uuid = uuid;
        this.canView = canView;
    }

    public boolean checkIfViewing (Player player) {

        return this.getViewers().contains(player);

    }

    public ArrayList<Player> getViewers () {
        ArrayList<Player> players = new ArrayList<>();

        for (Inventory inventory : this.inventory) {

            if (inventory.getViewers().size() > 0) {
                for (HumanEntity player : inventory.getViewers()) {
                    players.add((Player) player);
                }
            }

        }

        return players;
    }

}
